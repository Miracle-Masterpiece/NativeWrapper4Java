/*
 * Copyright (c) 2024, Miracle-Masterpiсe <mrmiraclemasterpiece@gmail.com or https://t.me/MiracleMasterpiece>. All rights reserved.
 * Use is subject to license terms.
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * */
package nw4j.helpers;

/**
 * 
 * @since 0.1
 * @author miracle-masterpiece
 * */
public final class Funcs {
	
	private Funcs() {throw new IllegalAccessError();}
	
	/**Changes the byte order of the passed variable and returns a new value.*/
	public static char swap_order(char __) {
		return (char)swap_order((short)__);
	}
	
	/**Changes the byte order of the passed variable and returns a new value.*/
	public static short swap_order(short __) {
		short result = 0;
		final int b1 = __ 			& 0xff;
		final int b2 = __ >> 8  	& 0xff;
		result |= b2;
		result |= b1 << 8;
		return result;
	}
	
	/**Changes the byte order of the passed variable and returns a new value.*/
	public static int swap_order(int __) {
		int result = 0;
		final int b1 = __ 			& 0xff;
		final int b2 = __ >> 8  	& 0xff;
		final int b3 = __ >> 16 	& 0xff;
		final int b4 = __ >> 24 	& 0xff;
		result |= b4;
		result |= b3 << 8;
		result |= b2 << 16;
		result |= b1 << 24;
		return result;
	}
	
	/**Changes the byte order of the passed variable and returns a new value.*/
	public static float swap_order(float __) {
		return Float.intBitsToFloat(swap_order(Float.floatToIntBits(__)));
	}
	
	/**Changes the byte order of the passed variable and returns a new value.*/
	public static long swap_order(long __) {
		long result = 0;
		final long b1 = __ 			& 0xff;
		final long b2 = __ >> 8  	& 0xff;
		final long b3 = __ >> 16  	& 0xff;
		final long b4 = __ >> 24  	& 0xff;
		final long b5 = __ >> 32  	& 0xff;
		final long b6 = __ >> 40 	& 0xff;
		final long b7 = __ >> 48  	& 0xff;
		final long b8 = __ >> 56  	& 0xff;
		result |= b1 << 56;
		result |= b2 << 48;
		result |= b3 << 40;
		result |= b4 << 32;
		result |= b5 << 24;
		result |= b6 << 16;
		result |= b7 << 8;
		result |= b8;
		return result;
	}
	
	/**Changes the byte order of the passed variable and returns a new value.*/
	public static double swap_order(double __) {
		return Double.longBitsToDouble(swap_order(Double.doubleToLongBits(__)));
	}
	
	public static int utf8_encode2(char c) {
		final byte b1 = (byte)((c >> 6) 			| 0b11000000);
		final byte b2 = (byte)((c & 0b10111111) 	| 0b10000000);
		return (Byte.toUnsignedInt(b1)) | Byte.toUnsignedInt(b2) << 8;
	}
	
	public static char utf8_decode_2(int b1, int b2) {
		final int mask1 = 0b11111;
		final int mask2 = 0b111111;
		return (char)(((b1 & mask1) << 6) | (b2 & mask2));
	}

	public static int utf8_encode3(char c) {
		final byte b1 = (byte)(( c >> 12)  					| 0b11100000);
		final byte b2 = (byte)(((c >> 6) 		& 0b111111) | 0b10000000);
		final byte b3 = (byte)(((c) 			& 0b111111) | 0b10000000);
		return (Byte.toUnsignedInt(b1) << 16) | (Byte.toUnsignedInt(b2) << 8) | Byte.toUnsignedInt(b3);
	}
	
	public static char utf8_decode_3(int b1, int b2, int b3) {
		final int mask1 = 0b1111;
		final int mask2 = 0b111111;
		return (char)(((b1 & mask1) << 12) | ((b2 & mask2) << 6) | (b3 & mask2));
	}
	
	public static int utf8_charlen(char c) {
		if (c < 128) {
			return 1;
		}else if (c >= 0x80  && c < 0x7ff) {
			return 2;
		}else if (c >= 0x800 && c < 0x7fff) {
			return 3;
		}else {
			return 4;
		}
	}	
}
