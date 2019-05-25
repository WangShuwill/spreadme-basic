package club.spreadme.basic.code;

import java.io.Serializable;

import club.spreadme.basic.code.support.TwitterLongIdGenerator;
import club.spreadme.basic.code.support.UUIDGenerator;

public interface IdGenerator<ID extends Serializable> {

	IdGenerator<Long> ID_GENERATOR = new TwitterLongIdGenerator();
	IdGenerator<String> UUID_GENERATOR = new UUIDGenerator();

	ID generate();
}
