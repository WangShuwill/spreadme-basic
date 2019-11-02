package club.spreadme.core.codec.support;

import club.spreadme.core.codec.Id;

/**
 * uid
 * @author shuwei.wang
 * @since 1.0.0
 */
public class UID implements Id<Long> {

	/** 机器id (0~15 保留 16~31作为备份机器) */
	private final long workerId;

	/** 初始偏移时间戳 */
	private final long offset = 1546300800L;

	/** 机器id所占位数 (5bit, 支持最大机器数 2^5 = 32)*/
	private final long workerIdBits = 5L;

	/** 自增序列所占位数 (16bit, 支持最大每秒生成 2^16 = ‭65536‬) */
	private final long sequenceIdBits = 16L;

	/** 机器id偏移位数 */
	private final long workerShiftBits = sequenceIdBits;

	/** 自增序列偏移位数 */
	private final long offsetShiftBits = sequenceIdBits + workerIdBits;

	/** 机器标识最大值 (2^5 / 2 - 1 = 15) */
	private final long workerIdMax = ((1 << workerIdBits) - 1) >> 1;

	/** 备份机器ID开始位置 (2^5 / 2 = 16) */
	private final long backWorkerIdBegin = (1 << workerIdBits) >> 1;

	/** 自增序列最大值 (2^16 - 1 = ‭65535) */
	private final long sequenceMax = (1 << workerShiftBits) - 1;

	/** 发生时间回拨时容忍的最大回拨时间 (秒) */
	private final long backTimeMax = 1L;

	/** 上次生成ID的时间戳 (秒) */
	private long lastTimestamp = 0L;

	/** 当前秒内序列 (2^16)*/
	private long sequence = 0L;

	/** 备份机器上次生成ID的时间戳 (秒) */
	private long lastTimestampBak = 0L;

	/** 备份机器当前秒内序列 (2^16)*/
	private long sequenceBak = 0L;

	public UID(long workerId) {
		if (workerId > this.workerIdMax || workerId < 0) {// workid < 1024[10位：2的10次方]
			throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", this.workerIdMax));
		}
		this.workerId = workerId;
	}

	/**
	 * 获取自增序列
	 * @return long
	 */
	public long nextId() {
		return nextId(timeGen() / 1000);
	}

	/**
	 * 主机器自增序列
	 * @param timestamp 当前Unix时间戳
	 * @return long
	 */
	private synchronized long nextId(long timestamp) {
		// 时钟回拨检查
		if (timestamp < lastTimestamp) {
			// 发生时钟回拨
			return nextIdBackup(timestamp);
		}

		// 开始下一秒
		if (timestamp != lastTimestamp) {
			lastTimestamp = timestamp;
			sequence = 0L;
		}
		if (0L == (++sequence & sequenceMax)) {
			// 秒内序列用尽
			sequence--;
			return nextIdBackup(timestamp);
		}

		return ((timestamp - offset) << offsetShiftBits) | (workerId << workerShiftBits) | sequence;
	}

	/**
	 * 备份机器自增序列
	 * @param timestamp timestamp 当前Unix时间戳
	 * @return long
	 */
	private long nextIdBackup(long timestamp) {
		if (timestamp < lastTimestampBak) {
			if (lastTimestampBak - timeGen() / 1000 <= backTimeMax) {
				timestamp = lastTimestampBak;
			}
			else {
				throw new RuntimeException(String.format("时钟回拨: now: [%d] last: [%d]", timestamp, lastTimestampBak));
			}
		}

		if (timestamp != lastTimestampBak) {
			lastTimestampBak = timestamp;
			sequenceBak = 0L;
		}

		if (0L == (++sequenceBak & sequenceMax)) {
			// 秒内序列用尽
			return nextIdBackup(timestamp + 1);
		}

		return ((timestamp - offset) << offsetShiftBits) | ((workerId ^ backWorkerIdBegin) << workerShiftBits) | sequenceBak;
	}

	/**
	 * 获得系统当前毫秒数
	 */
	private long timeGen() {
		return System.currentTimeMillis();
	}

	@Override
	public Long generate() {
		return nextId();
	}
}
