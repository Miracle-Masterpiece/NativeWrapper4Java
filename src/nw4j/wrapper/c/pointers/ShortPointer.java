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
public final @NativeType("short*")class ShortPointer extends VoidPointer{

	private ShortPointer(long address) {
		super(address);
	}

	private ShortPointer(VoidPointer voidPtr) {
		super(voidPtr);
	}

	private ShortPointer(VoidPointer voidPtr, IPreInit<ShortPointer> initializer) {
		super(voidPtr);
		initializer.init(this);
	}

	/**================== Getters & Setters ==============================*/
	@Override
	public long sizeof() {
		return SHORT_SIZE;
	}

	public void set(long index, short v) {
		setShort(index * SHORT_SIZE, v);
	}

	public short get(long index) {
		return getShort(index * SHORT_SIZE);
	}

	public void set(short v) {
		setShort(0, v);
	}

	public short get() {
		return getShort(0);
	}

	/**==================== Allocators ============================*/

	public static ShortPointer alloc(long size) {
		return new ShortPointer(MemoryAccessor.malloc(size * SHORT_SIZE));
	}

	public static ShortPointer asAddress(long address) {
		return new ShortPointer(address);
	}

	public static ShortPointer alloc() {
		return alloc(1);
	}

	public static ShortPointer asPointer(VoidPointer pointer) {
		return new ShortPointer(pointer);
	}


	public static ShortPointer alloc(long size, IPreInit<ShortPointer> init) {
		ShortPointer ptr = alloc(size);
		init.init(ptr);
		return ptr;
	}

	@Override
	public ShortPointer clone() {
		final ShortPointer _new = alloc();
		_new.copydata(this);
		return _new;
	}
}
