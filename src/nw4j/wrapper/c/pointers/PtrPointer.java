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
public final @NativeType("T**")class PtrPointer<T extends VoidPointer> extends VoidPointer{

	private PtrPointer(long address) {
		super(address);
	}

	private PtrPointer(VoidPointer voidPtr) {
		super(voidPtr);
	}

	private PtrPointer(VoidPointer voidPtr, IPreInit<PtrPointer<T>> initializer) {
		super(voidPtr);
		initializer.init(this);
	}

	/**================== Getters & Setters ==============================*/
	@Override
	public long sizeof() {
		return POINTER_SIZE;
	}

	public void set(long index, T v) {
		setPointer(index * sizeof(), v);
	}

	public T get(long index, IInstanceFabric<T> f) {
		final T ptr = f._new(nullptr);
		getPointer(index * sizeof(), ptr);
		return ptr;
	}

	public void set(T v) {
		setPointer(0, v);
	}

	public long get() {
		return getLong(0);
	}

	/**==================== Allocators ============================*/

	public static <E extends VoidPointer>PtrPointer<E> alloc(long size) {
		return new PtrPointer<>(MemoryAccessor.malloc(size * POINTER_SIZE));
	}

	@SuppressWarnings("rawtypes")
	public static PtrPointer asAddress(long address) {
		return new PtrPointer<>(address);
	}

	public static <E extends VoidPointer>PtrPointer<E> alloc() {
		return alloc(1);
	}

	public static <E extends VoidPointer>PtrPointer<E> alloc(long size, IPreInit<PtrPointer<E>> init) {
		PtrPointer<E> ptr = alloc(size);
		init.init(ptr);
		return ptr;
	}

	@Override
	public PtrPointer<T> clone() {
		final PtrPointer<T> _new = alloc();
		_new.copydata(this);
		return _new;
	}
}
