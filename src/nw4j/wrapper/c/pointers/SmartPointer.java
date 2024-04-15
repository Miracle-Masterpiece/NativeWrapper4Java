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

import java.lang.ref.Cleaner;
import java.util.Objects;
import javax.naming.OperationNotSupportedException;

import nw4j.wrapper.c.allocators.MemoryAccessor;

/**
 * The current class is a "smart pointer" that stores a reference to a pointer of type {@link VoidPointer}.
 * And which automatically frees it during "garbage collection"
 * @see VoidPointer
 * 
 * @since 0.1
 * @author miracle-masterpiece
 * */
public final class SmartPointer<PTR extends VoidPointer> extends VoidPointer{

	private static final Cleaner cleaner = Cleaner.create();

	/**
	 * A pointer that will be automatically free.
	 * */
	private final PTR pointer;

	public SmartPointer(PTR pointer) {
		super(NULL);
		this.pointer = Objects.<PTR>requireNonNull(pointer);
		this.address = pointer.address;
		cleaner.register(this, new PointerCleaner<>(pointer.address));
	}

	/**
	 * @return Returns the original pointer
	 * */
	public PTR pointer() {
		return pointer;
	}

	//Deallocator class
	private static final class PointerCleaner<P extends VoidPointer> implements Runnable {

		final long ptr;
		
		PointerCleaner(long ptr) {
			this.ptr = ptr;
		}

		@Override
		public void run() {
			try {
				MemoryAccessor.free(ptr);
			}catch(Exception ignore) {}
		}
	}

	@Override
	public void close() {
		throw new RuntimeException(new OperationNotSupportedException("This object clears memory itself."));
	}

	@Override
	public long sizeof() {
		return POINTER_SIZE;
	}

	@Override
	public VoidPointer clone() {
		throw new RuntimeException(new CloneNotSupportedException());
	}
}
