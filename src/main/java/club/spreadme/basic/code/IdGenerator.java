package club.spreadme.basic.code;

import java.io.Serializable;

import club.spreadme.basic.code.support.TwitterLongIdGenerator;

public interface IdGenerator<ID extends Serializable> {

	IdGenerator<Long> ID_GENERATOR = new TwitterLongIdGenerator();

	ID generate();
}
