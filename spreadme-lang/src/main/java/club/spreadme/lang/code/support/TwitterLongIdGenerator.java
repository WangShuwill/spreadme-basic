package club.spreadme.lang.code.support;

import club.spreadme.lang.code.IdGenerator;

import java.util.Random;

public class TwitterLongIdGenerator implements IdGenerator<Long> {
    public static final String WORKER_ID_PROP = "spreadme.worker.id";
    public static final String DATACENTER_ID_PROP = "spreadme.datacenter.id";

    private long sequence = 0L;
    private long epoch = 30 * 365 * 24 * 3600000L; // 时间纪元 2000-01-01 00:00 00
    private long workerIdBits = 5L; // 节点ID长度
    private long datacenterIdBits = 5L; // 数据中心ID长度
    private long maxWorkerId = -1L ^ (-1L << workerIdBits); // 最大支持机器节点数0~31，一共32个
    private long maxDatacenterId = -1L ^ (-1L << datacenterIdBits); // 最大支持数据中心节点数0~31，一共32个
    private long sequenceBits = 12L; // 序列号12位
    private long workerIdShift = sequenceBits; // 机器节点左移12位
    private long datacenterIdShift = sequenceBits + workerIdBits; // 数据中心节点左移17位
    private long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits; // 时间毫秒数左移22位
    private long sequenceMask = -1L ^ (-1L << sequenceBits); // 4095

    private long incrementBits = 10L; // 默认自增10位
    private long incrementMask = -1L ^ (-1L << incrementBits); // 1024，自增到1024时从0重新开始自增

    private long lastTimestamp = -1L;

    private long workerId;// 支持机器节点数0~31，最多32个
    private long datacenterId;// 支持数据中心节点数0~31，最多32个


    /**
     * workerId和datacenterId从系统变量读取，若没有设置系统变量则随机一个
     */
    public TwitterLongIdGenerator() {
        Random random = new Random();

        int randomWorker = random.nextInt(Long.valueOf(maxWorkerId).intValue());
        String worker = System.getProperty(WORKER_ID_PROP, String.valueOf(randomWorker));
        long workerId = Long.valueOf(worker);
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }

        int randomDatacenter = random.nextInt(Long.valueOf(maxDatacenterId).intValue());
        String datacenter = System.getProperty(DATACENTER_ID_PROP, String.valueOf(randomDatacenter));
        long datacenterId = Long.valueOf(datacenter);
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }

        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    public synchronized long nextId() {
        long timestamp = timeGen(); // 获取当前毫秒数
        // 如果服务器时间有问题(时钟后退) 报错。
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        // 如果上次生成时间和当前时间相同,在同一毫秒内
        if (lastTimestamp == timestamp) {
            // sequence自增，因为sequence只有12bit，所以和sequenceMask相与一下，去掉高位
            sequence = (sequence + 1) & sequenceMask;
            // 判断是否溢出,也就是每毫秒内超过4095，当为4096时，与sequenceMask相与，sequence就等于0
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp); // 自旋等待到下一毫秒
            }
        }
        else {
            // 如果和上次生成时间不同,自增sequence，到incrementMask（1024）时，sequence计数重新从0开始累加
            sequence = (sequence + 1) & incrementMask;
        }
        lastTimestamp = timestamp;
        // 最后按照规则拼出ID。
        // 000000000000000000000000000000000000000000 00000 00000 000000000000
        // time datacenterId workerId sequence
        return ((timestamp - epoch) << timestampLeftShift) | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift) | sequence;
    }

    private long tilNextMillis(long lastTime) {
        long timestamp = timeGen();
        while (timestamp <= lastTime) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    @Override
    public Long generate() {
        return nextId();
    }
}
