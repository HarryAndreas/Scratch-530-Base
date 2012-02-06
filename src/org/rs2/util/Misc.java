package org.rs2.util;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class Misc {
	
	/**
	 * Directions for walking/running
	 */
	public static final byte[] DIRECTION_DELTA_X = new byte[] {-1, 0, 1, -1, 1, -1, 0, 1};
	public static final byte[] DIRECTION_DELTA_Y = new byte[] {1, 1, 1, 0, 0, -1, -1, -1};
	
	public static int direction(int dx, int dy) {
		if (dx < 0) {
			if (dy < 0)
				return 5;
			else if (dy > 0)
				return 0;
			else
				return 3;
		} else if (dx > 0) {
			if (dy < 0)
				return 7;
			else if (dy > 0)
				return 2;
			else
				return 4;
		} else {
			if (dy < 0)
				return 6;
			else if (dy > 0)
				return 1;
			else
				return -1;
		}
	}
	
	/**
	 * Converts a byte array to a string
	 * @param data
	 * @return
	 */
    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) {
                    buf.append((char) ('0' + halfbyte));
                } else {
                    buf.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    /**
     * Encrypts a string
     * @param text The text to encrypt
     * @return The result
     * @throws NoSuchAlgorithmException
     */
    public static String getMD5(String text)throws Exception {
        MessageDigest md;
        md = MessageDigest.getInstance("MD5");
        byte[] md5hash = new byte[32];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        md5hash = md.digest();
        return convertToHex(md5hash);
    }

	/**
	 * Formats a name for display.
	 * @param s The name.
	 * @return The formatted name.
	 */
	public static String formatName(String s) {
		return fixName(s.replace(" ", "_"));
	}
	
	/**
	 * Method that fixes capitalization in a name.
	 * @param s The name.
	 * @return The formatted name.
	 */
	private static String fixName(final String s) {
		if(s.length() > 0) {
			final char ac[] = s.toCharArray();
			for(int j = 0; j < ac.length; j++)
				if(ac[j] == '_') {
					ac[j] = ' ';
					if((j + 1 < ac.length) && (ac[j + 1] >= 'a')
							&& (ac[j + 1] <= 'z')) {
						ac[j + 1] = (char) ((ac[j + 1] + 65) - 97);
					}
				}

			if((ac[0] >= 'a') && (ac[0] <= 'z')) {
				ac[0] = (char) ((ac[0] + 65) - 97);
			}
			return new String(ac);
		} else {
			return s;
		}
	}
	
	/**
	 * Valid characters.
	 */
	public static char[] VALID_CHARS = {
		'_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 
		'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 
		't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', 
		'3', '4', '5', '6', '7', '8', '9'
	};
	
	/**
	 * Read a string from a channel buffer
	 * @param buffer
	 * @return
	 */
	public static String readRS2String(ChannelBuffer buffer) {
		StringBuilder sb = new StringBuilder();
		byte b;
		while(buffer.readable() && (b = buffer.readByte()) != 0) {
			sb.append((char) b);
		}
		return sb.toString();
	}
	
	/**
	 * Read a string from a channel buffer
	 * @param buffer
	 * @return
	 */
	public static String readRS2String(ByteBuffer buffer) {
		StringBuilder sb = new StringBuilder();
		byte b;
		while((b = buffer.get()) != 0) {
			sb.append((char) b);
		}
		return sb.toString();
	}
	
	/**
	 * Convert a long to a string.
	 * @param l
	 * @return
	 */
	public static String longToPlayerName(long l) {
		if (l <= 0L || l >= 0x5b5b57f8a98a5dd1L) {
			return null;
		}
		if (l % 37L == 0L) {
			return null;
		}
		int i = 0;
		char ac[] = new char[12];
		while (l != 0L) {
			long l1 = l;
			l /= 37L;
			ac[11 - i++] = VALID_CHARS[(int)(l1 - l * 37L)];
		}
		return new String(ac, 12 - i, i);
	}
	
	/**
	 * Convert a String to a Long.
	 * @param s The string to convert to a long.
	 * @return Returns the string as a long.
	 */
	public static long stringToLong(String s) {
		long l = 0L;
		for (int i = 0; i < s.length() && i < 12; i++) {
			char c = s.charAt(i);
			l *= 37L;
			if (c >= 'A' && c <= 'Z')
				l += (1 + c) - 65;
			else if (c >= 'a' && c <= 'z')
				l += (1 + c) - 97;
			else if (c >= '0' && c <= '9')
				l += (27 + c) - 48;
		}
		for (; l % 37L == 0L && l != 0L; l /= 37L);
		return l;
	}

}