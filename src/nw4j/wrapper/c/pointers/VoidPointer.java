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

import java.io.Closeable;

import nw4j.wrapper.c.allocators.MemoryAccessor;

/**
 * This class and its descendants represent a wrapper over a raw pointer to data in native memory.
 * <p>
 * To clear memory, call the {@link VoidPointer#close()}, {@link VoidPointer#free()} method.
 *
 * try(DoublePointer PI = DoublePointer.alloc()){
 *		PI.set(Math.PI);
 *		System.out.println(PI.get());
 * }//auto clear resource.
 *
 * @see BytePointer
 * @see CharPointer
 * @see ShortPointer
 * @see IntPointer
 * @see FloatPointer
 * @see LongPointer
 * @see DoublePointer
 * @see BooleanPointer
 * 
 * @since 0.1
 * @author miracle-masterpiece
 * */
public abstract class VoidPointer implements Closeable, AutoCloseable, Cloneable{

	/**sizeof data*/
	public static final byte
	VOID_SIZE 		= 1,
	BYTE_SIZE 		= 1,
	CHAR_SIZE 		= 2,
	SHORT_SIZE 		= 2,
	INT_SIZE 		= 4,
	FLOAT_SIZE 		= 4,
	LONG_SIZE 		= 8,
	DOUBLE_SIZE 	= 8,
	BOOLEAN_SIZE 	= 1,
	POINTER_SIZE 	= 8;

	/**null pointer value*/
	public static final byte 
	nullptr = 0, 
	NULL = 0;

	static final class VoidPointerImpl extends VoidPointer{
		public VoidPointerImpl(long address) {
			super(address);
		}

		@Override
		public long sizeof() {
			return VOID_SIZE;
		}

		@Override
		public VoidPointer clone() {
			VoidPointerImpl _new = new VoidPointerImpl(MemoryAccessor.malloc(sizeof()));
			_new.copydata(this);
			return _new;
		}
	}

	/**Raw pointer value*/
	protected long address;

	protected VoidPointer(VoidPointer voidPtr) {
		this.address = voidPtr.address;
	}

	protected VoidPointer(long address) {
		this.address = address;
	}

	protected VoidPointer() {
		this.address = MemoryAccessor.malloc(sizeof());
	}

	/**
	 * @return Returns the address pointed to by a pointer object.
	 *
	 * @see {@link VoidPointer#assign(long)}
	 * @see {@link VoidPointer#assign(VoidPointer))}
	 * */
	public final long address() {
		return address;
	}

	public final long addressIndex(long offset) {
		return address + (offset * sizeof());
	}

	/**
	 * @return Returns the size of the data type pointed to by the pointer.
	 * sizeof() does not return the length of the allocated bytes, only the length of the type it stores.
	 * So, if the pointer points to an integer, then sizeof() will return 4.
	 * */
	public abstract long sizeof();

	/**
	 * Frees the allocated memory pointed to by the raw address.
	 * @see {@link VoidPointer#address}
	 * */
	public void free() {
		close();
	}

	/**
	 * Frees the allocated memory pointed to by the raw address.
	 * @see {@link VoidPointer#address}
	 * */
	@Override public void close() {
		MemoryAccessor.free(address);
	}

	/**
	 * @return Returns a string representation of the address as a hex number.
	 * */
	@Override public String toString() {
		return Long.toHexString(address);
	}

	@Override public int hashCode() {
		return (int)(address >> 32);
	}

	@Override public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		return o instanceof VoidPointer && ((VoidPointer)o).address == address;
	}

	/**
	 * Creates a new pointer of the same type as the calling type.
	 * And copies all data, length sizeof, into a new object.
	 *
	 * @return Pointer to a copy of the data
	 * */
	@Override public abstract VoidPointer clone();

	/**
	 * @param  Address to be wrapped
	 * @return Returns a pointer to the void at the specified address
	 * */
	public static VoidPointer asAddress(long address) {
		return new VoidPointerImpl(address);
	}

	public final void assign(VoidPointer voidPtr) {
		address = voidPtr.address;
	}

	/**
	 * Assigns a new address to a pointer
	 *
	 * @param The address to which the pointer should point
	 *
	 * @see {@link VoidPointer#address}
	 * */
	public final void assign(long address) {
		this.address = address;
	}

	/**
	 * Copies data of size len from the src pointer to the current pointer.
	 * @param src here to copy data from.
	 * @param len Length of copied data, in bytes.
	 * */
	public final void copydata(final VoidPointer src, final long len) {
		if (len < 0) {
			throw new RuntimeException("@param len < 0!");
		}
		MemoryAccessor.memcopy(src.address, address, len);
	}

	/**
	 * Copies data of size len from the src pointer to the current pointer.
	 * 
	 * @param src 	Here to copy data from.
	 *
	 * Remarks:
	 * The length of the copied data is calculated using the sizeof method in the FROM object where the data will be copied.
	 *
	 * 	try(ShortPointer _short = ShortPointer.alloc(); IntPointer _int = IntPointer.alloc();){
	 *		_short.set((short)9347);
	 *		_int.copydata(_short); //The size of the copied area will be get from the {@link #sizeof()} method variable _short
	 *
	 *		System.out.println(_short.get());
	 *		System.out.println(_int.get());
	 *	}
	 * */
	public final void copydata(final VoidPointer src) {
		MemoryAccessor.memcopy(src.address, address, src.sizeof());
	}

	protected void setByte(long index, byte v) {
		MemoryAccessor.setByte(address + index, v);
	}

	protected byte getByte(long index) {
		return MemoryAccessor.getByte(address + index);
	}

	protected void setChar(long index, char v) {
		MemoryAccessor.setChar(address + index, v);
	}

	protected char getChar(long index) {
		return MemoryAccessor.getChar(address + index);
	}

	protected void setShort(long index, short v) {
		MemoryAccessor.setShort(address + index, v);
	}

	protected short getShort(long index) {
		return MemoryAccessor.getShort(address + index);
	}

	protected void setInt(long index, int v) {
		MemoryAccessor.setInt(address + index, v);
	}

	protected int getInt(long index) {
		return MemoryAccessor.getInt(address + index);
	}

	protected void setFloat(long index, float v) {
		MemoryAccessor.setFloat(address + index, v);
	}

	protected float getFloat(long index) {
		return MemoryAccessor.getFloat(address + index);
	}

	protected void setLong(long index, long v) {
		MemoryAccessor.setLong(address + index, v);
	}

	protected void setPointer(long index, VoidPointer v) {
		MemoryAccessor.setLong(address + index, v != null ? v.address : nullptr);
	}

	protected <T extends VoidPointer>void getPointer(long index, T v) {
		v.assign(MemoryAccessor.getLong(address + index));
	}

	protected long getLong(long index) {
		return MemoryAccessor.getLong(address + index);
	}

	protected void setDouble(long index, double v) {
		MemoryAccessor.setDouble(address + index, v);
	}

	protected double getDouble(long index) {
		return MemoryAccessor.getDouble(address + index);
	}

	protected void setBool(long index, boolean v) {
		MemoryAccessor.setBoolean(address + index, v);
	}

	protected boolean getBool(long index) {
		return MemoryAccessor.getBoolean(address + index);
	}

	/**
	 * try(IntPointer _int = IntPointer.alloc()){
	 * 		_int.set(0x0f_0a_08_05);
	 * 		BytePointer values = cast(BytePointer::asAddress, _int);	//Instead of a reference to the static asAddress method, you can pass a reference to any method that accepts a field of type long and returns a new instance of the desired class.
	 * 		for (int i = 0; i < 4; ++i) {
	 *	 		System.out.print(values.get(i) + " ");
	 * 		}//5 8 10 15
	 * }//auto free resources
	 * @see InstanceFabric#_new(long)
	 *
	 * @param c 		Factory method to create a new object
	 * @param _void 	A pointer to be cast to another pointer
	 *
	 * @return 			Pointer with the desired type
	 * */
	public static <P extends VoidPointer> P dynamic_cast(final IInstanceFabric<P> c, final VoidPointer _void){
		return c._new(_void.address);
	}

	/**
	 * Checks the address for 0, and if it is 0 an exception is thrown
	 * @throws RuntimeException;
	 * */
	public static void checkAddress(long address) {
		if (address == nullptr) {
			throw new RuntimeException("address is 0!");
		}
		return;
	}

	/**
	 * @return Checks the pointer for 0, and if it is 0, then returns true
	 * */
	public static boolean isNull(long address) {
		return address == nullptr;
	}

	/**
	 * Functional interface for creating new instances that inherit from {@link VoidPointer}.
	 * */
	@FunctionalInterface
	public static interface IInstanceFabric<PTR extends VoidPointer>{
		PTR _new(long address);
	}

	/**
	 * Functional interface for pointer initialization.
	 * */
	@FunctionalInterface
	public static interface IPreInit<PTR extends VoidPointer>{
		void init(PTR pointer);
	}
}
