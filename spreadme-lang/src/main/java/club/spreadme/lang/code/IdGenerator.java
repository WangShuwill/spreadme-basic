package club.spreadme.lang.code;

import java.io.Serializable;

import club.spreadme.lang.code.support.TwitterLongIdGenerator;

public interface IdGenerator<ID extends Serializable> {

	IdGenerator<Long> ID_GENERATOR = new TwitterLongIdGenerator();

	ID generate();
}
