package club.spreadme.basic.code.support;

import java.util.UUID;

import club.spreadme.basic.code.IdGenerator;

public class UUIDGenerator implements IdGenerator<String> {

	@Override
	public String generate() {
		return UUID.randomUUID().toString().replace("-","");
	}
}
