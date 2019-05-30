package club.spreadme.core.codec.support;

import club.spreadme.core.codec.Id;


public class UUID implements Id<String> {

    @Override
    public String generate() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }
}
