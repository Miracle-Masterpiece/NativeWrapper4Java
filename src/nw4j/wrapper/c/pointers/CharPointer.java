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

import nw4j.helpers.NativeType;
import nw4j.wrapper.c.allocators.MemoryAccessor;

/**
 * @see VoidPointer
 * 
 * @since 0.1
 * @author miracle-masterpiece
 * */
public final @NativeType("wchar_t*") class CharPointer extends VoidPointer{

	private CharPointer(long address) {
		super(address);
	}

	private CharPointer(VoidPointer voidPtr) {
		super(voidPtr);
	}

	private CharPointer(VoidPointer voidPtr, IPreInit<CharPointer> initializer) {
		super(voidPtr);
		initializer.init(this);
	}

	/**================== Getters & Setters ==============================*/
	@Override
	public long sizeof() {
		return CHAR_SIZE;
	}

	public void set(long index, char v) {
		setChar(index * CHAR_SIZE, v);
	}

	public char get(long index) {
		return getChar(index * CHAR_SIZE);
	}

	public void set(char v) {
		setChar(0, v);
	}

	public char get() {
		return getChar(0);
	}

	/**==================== Allocators ============================*/

	public static CharPointer alloc(long size) {
		return new CharPointer(MemoryAccessor.malloc(size * CHAR_SIZE));
	}

	public static CharPointer asAddress(long address) {
		return new CharPointer(address);
	}

	public static CharPointer alloc() {
		return alloc(1);
	}

	public static CharPointer asPointer(VoidPointer pointer) {
		return new CharPointer(pointer);
	}

	public static CharPointer alloc(long size, IPreInit<CharPointer> init) {
		CharPointer ptr = alloc(size);
		init.init(ptr);
		return ptr;
	}

	@Override
	public CharPointer clone() {
		final CharPointer _new = alloc();
		_new.copydata(this);
		return _new;
	}
}
