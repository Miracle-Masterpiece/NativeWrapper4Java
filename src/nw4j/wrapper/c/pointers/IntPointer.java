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
public final @NativeType("int*")class IntPointer extends VoidPointer{

	private IntPointer(long address) {
		super(address);
	}

	private IntPointer(VoidPointer voidPtr) {
		super(voidPtr);
	}

	private IntPointer(VoidPointer voidPtr, IPreInit<IntPointer> initializer) {
		super(voidPtr);
		initializer.init(this);
	}

	/**================== Getters & Setters ==============================*/
	@Override
	public long sizeof() {
		return INT_SIZE;
	}

	public void set(long index, int v) {
		setInt(index * INT_SIZE, v);
	}

	public int get(long index) {
		return getInt(index * INT_SIZE);
	}

	public void set(int v) {
		setInt(0, v);
	}

	public int get() {
		return getInt(0);
	}

	/**==================== Allocators ============================*/

	public static IntPointer alloc(long size) {
		return new IntPointer(MemoryAccessor.malloc(size * INT_SIZE));
	}

	public static IntPointer asAddress(long address) {
		return new IntPointer(address);
	}

	public static IntPointer alloc() {
		return alloc(1);
	}

	public static IntPointer asPointer(VoidPointer pointer) {
		return new IntPointer(pointer);
	}


	public static IntPointer alloc(long size, IPreInit<IntPointer> init) {
		IntPointer ptr = alloc(size);
		init.init(ptr);
		return ptr;
	}

	@Override
	public IntPointer clone() {
		final IntPointer _new = alloc();
		_new.copydata(this);
		return _new;
	}
}
