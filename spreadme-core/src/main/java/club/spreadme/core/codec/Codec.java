package club.spreadme.core.codec;

public class Codec {

	public static String toHex(byte[] src){
		char[] result = Hex.toHex(src);
		return new String(result);
	}

}
