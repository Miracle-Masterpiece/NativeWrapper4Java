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
package nw4j.wrapper.c.allocators;

import static nw4j.helpers.Helpers.ENABLE_CHECKS;

import nw4j.helpers.Helpers;
import nw4j.helpers.NativeType;
import nw4j.wrapper.c.pointers.BooleanPointer;
import nw4j.wrapper.c.pointers.BytePointer;
import nw4j.wrapper.c.pointers.CharPointer;
import nw4j.wrapper.c.pointers.DoublePointer;
import nw4j.wrapper.c.pointers.FloatPointer;
import nw4j.wrapper.c.pointers.IntPointer;
import nw4j.wrapper.c.pointers.LongPointer;
import nw4j.wrapper.c.pointers.ShortPointer;
import nw4j.wrapper.c.pointers.VoidPointer;
import nw4j.wrapper.c.pointers.VoidPointer.IInstanceFabric;

/**
 * 
 * The class represents a memory allocator on a stack architecture.
 * Each instance stores an instance of the Thread that created it. 
 * If you try to access a method of an object from another thread, exceptions will be thrown (Checks can be disabled -DnwChecks="false")
 * 
 * @since 0.1
 * @author miracle-masterpiece
 * */
public final class ThreadLocalStack implements IStack{

	/**
	 * The thread to which the stack is attached.
	 * */
	private final Thread current;

	/**
	 * Address to beginning stack.
	 * */
	private @NativeType("void*") long address;

	private int pointerOffset;

	/**
	 * Store size for each allocation
	 * */
	private @NativeType("int*") long allocSizesArray;

	private int allocPtr;

	/**
	 * Maximum stack size in bytes.
	 * */
	private long maxSize;

	public ThreadLocalStack(long stackSize) {
		current 			= Thread.currentThread();
		address 			= Helpers.addressNonNull(MemoryAccessor.malloc(stackSize));
		allocSizesArray  	= Helpers.addressNonNull(MemoryAccessor.malloc(stackSize * VoidPointer.INT_SIZE));
		maxSize 		 	= stackSize;
	}

	private final void checks(long n) {
		if (n <= 0) {
			throw new IllegalArgumentException("Allocated bytes is 0");
		}

		if (current != Thread.currentThread()) {
			throw new RuntimeException(new IllegalAccessException());
		}

		if (pointerOffset+n > maxSize) {
			throw new StackOverflowError("ThreadLocalStack overflow! MaxSize: " + maxSize + ", requested size: " + (pointerOffset+n));
		}
	}

	@Override
	public void close() {
		pointerOffset 	= 0;
		allocPtr 		= 0;
	}

	@Override
	public void destroy() {
		MemoryAccessor.free(address);
	}

	@Override
	public BytePointer pushByte(int n) {
		return BytePointer.asAddress(rawByte(n));
	}

	@Override
	public CharPointer pushChar(int n) {
		return CharPointer.asAddress(rawChar(n));
	}

	@Override
	public ShortPointer pushShort(int n) {
		return ShortPointer.asAddress(rawShort(n));
	}

	@Override
	public IntPointer pushInt(int n) {
		return IntPointer.asAddress(rawInt(n));
	}

	@Override
	public FloatPointer pushFloat(int n) {
		return FloatPointer.asAddress(rawFloat(n));
	}

	@Override
	public LongPointer pushLong(int n) {
		return LongPointer.asAddress(rawLong(n));
	}

	@Override
	public DoublePointer pushDouble(int n) {
		return DoublePointer.asAddress(rawDouble(n));
	}

	@Override
	public BooleanPointer pushBoolean(int n) {
		return BooleanPointer.asAddress(rawBoolean(n));
	}

	@Override
	public long rawByte(int n) {
		return allocNBytes(n);
	}

	@Override
	public long rawChar(int n) {
		return allocNBytes(n <<1);
	}

	@Override
	public long rawShort(int n) {
		return allocNBytes(n << 1);
	}

	@Override
	public long rawInt(int n) {
		return allocNBytes(n << 2);
	}

	@Override
	public long rawFloat(int n) {
		return allocNBytes(n << 2);
	}

	@Override
	public long rawLong(int n) {
		return allocNBytes(n << 3);
	}

	@Override
	public long rawDouble(int n) {
		return allocNBytes(n << 3);
	}

	@Override
	public long rawBoolean(int n) {
		return allocNBytes(n);
	}

	private long allocNBytes(int n) {
		if (ENABLE_CHECKS) {
			checks(n);
		}
		long _resultAddress = address + pointerOffset;
		pointerOffset += n;
		MemoryAccessor.setInt(allocSizesArray + (allocPtr << 2), n);
		++allocPtr;
		return _resultAddress;
	}

	@Override
	public boolean reallocate(int newSize) {
		long _newAddress = MemoryAccessor.realloc(address, newSize);
		if (_newAddress != VoidPointer.nullptr) {
			maxSize 		= newSize;
			pointerOffset 	= 0;
			address 		= _newAddress;
			return true;
		}
		return false;
	}

	@Override
	public <T extends VoidPointer> T push(int n, IInstanceFabric<T> instance_fabric) {
		final T _ptr = instance_fabric._new(0);
		final int sizeof = (int)_ptr.sizeof();
		_ptr.assign(rawByte(sizeof));
		if (ENABLE_CHECKS) {
			checks(sizeof);
		}
		return _ptr;
	}

	@Override
	public void pop() {
		if (ENABLE_CHECKS) {
			if (allocPtr - 1 < 0) {
				throw new IndexOutOfBoundsException(allocPtr - 1);
			}
		}
		final int currentSize = MemoryAccessor.getInt(allocSizesArray + ((allocPtr-1) << 2));
		pointerOffset -= currentSize;
		--allocPtr;
	}

	@Override
	public long address() {
		return address;
	}

	@Override
	public BytePointer pushUTF8(CharSequence str) {
		return BytePointer.asAddress(rawUTF8(str));
	}
	
	@Override
	public long rawUTF8(CharSequence string) {
		@NativeType("char*") 
		long address = rawByte(string.length()+1);
		BytePointer.putUTF8(address, string);
		return address;
	}
}
