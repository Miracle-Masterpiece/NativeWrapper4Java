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

import nw4j.wrapper.c.allocators.MemoryAccessor;

/**
 * @see VoidPointer
 * 
 * @since 0.1
 * @author miracle-masterpiece
 * */
public final class FloatPointer extends VoidPointer{

	private FloatPointer(long address) {
		super(address);
	}

	private FloatPointer(VoidPointer voidPtr) {
		super(voidPtr);
	}

	private FloatPointer(VoidPointer voidPtr, IPreInit<FloatPointer> initializer) {
		super(voidPtr);
		initializer.init(this);
	}

	/**================== Getters & Setters ==============================*/
	@Override
	public long sizeof() {
		return FLOAT_SIZE;
	}

	public void set(long index, float v) {
		setFloat(index * FLOAT_SIZE, v);
	}

	public float get(long index) {
		return getFloat(index * FLOAT_SIZE);
	}

	public void set(float v) {
		setFloat(0, v);
	}

	public float get() {
		return getFloat(0);
	}

	/**==================== Allocators ============================*/

	public static FloatPointer alloc(long size) {
		return new FloatPointer(MemoryAccessor.malloc(size * FLOAT_SIZE));
	}

	public static FloatPointer asAddress(long address) {
		return new FloatPointer(address);
	}

	public static FloatPointer alloc() {
		return alloc(1);
	}

	public static FloatPointer asPointer(VoidPointer pointer) {
		return new FloatPointer(pointer);
	}


	public static FloatPointer alloc(long size, IPreInit<FloatPointer> init) {
		FloatPointer ptr = alloc(size);
		init.init(ptr);
		return ptr;
	}

	@Override
	public FloatPointer clone() {
		final FloatPointer _new = alloc();
		_new.copydata(this);
		return _new;
	}
}
