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
public final class BooleanPointer extends VoidPointer{

	
	public static final byte 
	TRUE = 1, 
	FALSE = 0;
	
	private BooleanPointer(long address) {
		super(address);
	}

	private BooleanPointer(VoidPointer voidPtr) {
		super(voidPtr);
	}

	private BooleanPointer(VoidPointer voidPtr, IPreInit<BooleanPointer> initializer) {
		super(voidPtr);
		initializer.init(this);
	}

	/**================== Getters & Setters ==============================*/
	@Override
	public long sizeof() {
		return BOOLEAN_SIZE;
	}

	public void set(long index, boolean v) {
		setBool(index, v);
	}

	public boolean get(long index) {
		return getBool(index);
	}

	public void set(boolean v) {
		setBool(0, v);
	}

	public boolean get() {
		return getBool(0);
	}

	/**==================== Allocators ============================*/
	public static BooleanPointer asAddress(long address) {
		return new BooleanPointer(address);
	}

	public static BooleanPointer malloc(long size) {
		return new BooleanPointer(MemoryAccessor.malloc(size));
	}

	public static BooleanPointer malloc() {
		return malloc(1);
	}

	public static BooleanPointer asPointer(VoidPointer pointer) {
		return new BooleanPointer(pointer);
	}

	public static BooleanPointer malloc(long size, IPreInit<BooleanPointer> init) {
		BooleanPointer ptr = malloc(size);
		init.init(ptr);
		return ptr;
	}

	@Override
	public BooleanPointer clone() {
		final BooleanPointer _new = malloc();
		_new.copydata(this);
		return _new;
	}
}
