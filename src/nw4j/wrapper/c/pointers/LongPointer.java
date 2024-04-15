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
public final @NativeType("long*")class LongPointer extends VoidPointer{

	private LongPointer(long address) {
		super(address);
	}

	private LongPointer(VoidPointer voidPtr) {
		super(voidPtr);
	}

	private LongPointer(VoidPointer voidPtr, IPreInit<LongPointer> initializer) {
		super(voidPtr);
		initializer.init(this);
	}

	/**================== Getters & Setters ==============================*/
	@Override
	public long sizeof() {
		return LONG_SIZE;
	}

	public void set(long index, long v) {
		setLong(index * LONG_SIZE, v);
	}

	public long get(long index) {
		return getLong(index * LONG_SIZE);
	}

	public void set(long v) {
		setLong(0, v);
	}

	public long get() {
		return getLong(0);
	}

	/**==================== Allocators ============================*/

	public static LongPointer alloc(long size) {
		return new LongPointer(MemoryAccessor.malloc(size * LONG_SIZE));
	}

	public static LongPointer asAddress(long address) {
		return new LongPointer(address);
	}

	public static LongPointer alloc() {
		return alloc(1);
	}

	public static LongPointer asPointer(VoidPointer pointer) {
		return new LongPointer(pointer);
	}


	public static LongPointer alloc(long size, IPreInit<LongPointer> init) {
		LongPointer ptr = alloc(size);
		init.init(ptr);
		return ptr;
	}

	@Override
	public LongPointer clone() {
		final LongPointer _new = alloc();
		_new.copydata(this);
		return _new;
	}
}
