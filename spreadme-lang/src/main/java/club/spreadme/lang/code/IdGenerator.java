package club.spreadme.lang.code;

import java.io.Serializable;

public interface IdGenerator<ID extends Serializable> {

    ID generate();

}
