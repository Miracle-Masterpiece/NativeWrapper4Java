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
public final @NativeType("double*") class DoublePointer extends VoidPointer{

	private DoublePointer(long address) {
		super(address);
	}

	private DoublePointer(VoidPointer voidPtr) {
		super(voidPtr);
	}

	private DoublePointer(VoidPointer voidPtr, IPreInit<DoublePointer> initializer) {
		super(voidPtr);
		initializer.init(this);
	}

	/**================== Getters & Setters ==============================*/
	@Override
	public long sizeof() {
		return DOUBLE_SIZE;
	}

	public void set(long index, double v) {
		setDouble(index * DOUBLE_SIZE, v);
	}

	public double get(long index) {
		return getDouble(index * DOUBLE_SIZE);
	}

	public void set(double v) {
		setDouble(0, v);
	}

	public double get() {
		return getDouble(0);
	}

	/**==================== Allocators ============================*/

	public static DoublePointer alloc(long size) {
		return new DoublePointer(MemoryAccessor.malloc(size * DOUBLE_SIZE));
	}

	public static DoublePointer asAddress(long address) {
		return new DoublePointer(address);
	}

	public static DoublePointer alloc() {
		return alloc(1);
	}

	public static DoublePointer asPointer(VoidPointer pointer) {
		return new DoublePointer(pointer);
	}


	public static DoublePointer alloc(long size, IPreInit<DoublePointer> init) {
		DoublePointer ptr = alloc(size);
		init.init(ptr);
		return ptr;
	}

	@Override
	public DoublePointer clone() {
		final DoublePointer _new = alloc();
		_new.copydata(this);
		return _new;
	}
}
