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
package nw4j.wrapper.c.pointers;

import nw4j.helpers.Funcs;
import nw4j.helpers.NativeType;
import nw4j.wrapper.c.allocators.MemoryAccessor;

/**
 * @see VoidPointer
 * 
 * @since 0.1
 * @author miracle-masterpiece
 * */
public final class BytePointer extends VoidPointer{

	private BytePointer(long address) {
		super(address);
	}

	private BytePointer(VoidPointer voidPtr) {
		super(voidPtr);
	}

	private BytePointer(VoidPointer voidPtr, IPreInit<BytePointer> initializer) {
		super(voidPtr);
		initializer.init(this);
	}

	/**================== Getters & Setters ==============================*/
	@Override
	public long sizeof() {
		return BYTE_SIZE;
	}

	public void set(long index, byte v) {
		setByte(index, v);
	}

	public byte get(long index) {
		return getByte(index);
	}

	public void set(byte v) {
		setByte(0, v);
	}

	public byte get() {
		return getByte(0);
	}

	public String toUTF8() {
		final int len;
		for (int i = 0; ;++i) {
			if (getByte(i) == '\0') {
				len = i;
				break;
			}
		}

		final byte[] data = new byte[len];
		for (int i = 0; i < len ;++i) {
			data[i] = getByte(i);
		}
		return new String(data);
	}

	/**==================== Allocators ============================*/
	public static BytePointer asAddress(long address) {
		return new BytePointer(address);
	}

	public static BytePointer alloc(long size) {
		return new BytePointer(MemoryAccessor.malloc(size));
	}

	public static BytePointer alloc() {
		return alloc(1);
	}

	public static BytePointer asPointer(VoidPointer pointer) {
		return new BytePointer(pointer);
	}

	public static BytePointer alloc(long size, IPreInit<BytePointer> init) {
		BytePointer ptr = alloc(size);
		init.init(ptr);
		return ptr;
	}

	public static long allocRawUTF8(CharSequence seq) {
		@NativeType("char*") long string = MemoryAccessor.malloc(seq.length() + 1);
		putUTF8(string, seq);
		return string;
	}

	public static void putUTF8(@NativeType("char*") long string, CharSequence seq) {
		final int len = seq.length();
		
		long indx 				= 0;
		char __char 			= 0;
		int char_decode_len 	= 0;
		
		for (int i = 0; i < len; ++i) {
			__char = seq.charAt(i);
			char_decode_len = Funcs.utf8_charlen(__char); 
			
			if (char_decode_len == 1) {
				MemoryAccessor.setByte(string + (indx++), (byte)(__char & 0xff));
				continue;
			}else if (char_decode_len == 2) {
				int decoded = Funcs.utf8_encode2(__char);
				MemoryAccessor.setShort(string + indx, (short)decoded);
				indx += 2;
				continue;
			}else if (char_decode_len == 3) {
				int decoded = Funcs.utf8_encode3(__char);
				MemoryAccessor.setByte(string + (indx++), (byte)((decoded >> 16) 	& 0xff));
				MemoryAccessor.setByte(string + (indx++), (byte)((decoded >> 8) 	& 0xff));
				MemoryAccessor.setByte(string + (indx++), (byte)((decoded 			& 0xff)));
				continue;
			}
		}
		MemoryAccessor.setByte(string + indx, (byte)'\0');
	}
	
	/**
	 * Returns the length of a string in bytes in UTF-8 encoding.
	 * */
	public static final long strlen(CharSequence str) {
		int len = 0;
		final int originalLen = str.length();
		for (int i = 0; i < originalLen; ++i) {
			len += Funcs.utf8_charlen(str.charAt(i));
		}
		return len;
	}
	
	public static BytePointer allocUTF8(CharSequence seq) {
		return alloc(strlen(seq) + 1, pointer -> {putUTF8(pointer.address, seq);});
	}

	@Override
	public BytePointer clone() {
		final BytePointer _new = alloc();
		_new.copydata(this);
		return _new;
	}
}
