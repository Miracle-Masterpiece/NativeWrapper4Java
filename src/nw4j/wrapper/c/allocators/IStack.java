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

import java.io.Closeable;

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
 * Interface for implementations of memory allocation in the form of a stack.
 *
 * @see ThreadLocalStack
 * 
 * @since 0.1
 * @author miracle-masterpiece
 * */
public interface IStack extends AutoCloseable, Closeable{

	/**
	 * A method for completely freeing a pointer pointing to the beginning of the stack.
	 * This method should completely clear the native memory.
	 * And make the stack unusable for subsequent memory allocations.
	 * */
	public void 			destroy();

	/**
	 * When implementing this method, it is necessary that this method should not completely destroy the stack and free the native memory.
	 * But only the one that was isolated using the following methods:
	 *
	 * {@link IStack#pushByte(long)}
	 * {@link IStack#pushChar(long)}
	 * {@link IStack#pushShort(long)}
	 * {@link IStack#pushInt(long)}
	 * {@link IStack#pushFloat(long)}
	 * {@link IStack#pushLong(long)}
	 * {@link IStack#pushDouble(long)}
	 * {@link IStack#pushBoolean(long)}
	 * */
	@Override
	public void 			close();

	/**
	 * Allocates memory for a custom pointer that inherits from VoidPointer.
	 * */
	public <T extends VoidPointer> T push(int n, IInstanceFabric<T> instance_fabric);

	/**
	 * Allocates memory for string.
	 *
	 * @param str - String to allocate.
	 *
	 * @return Pointer to string.
	 * */
	public BytePointer 		pushUTF8(CharSequence str);
	
	/**
	 * Allocates memory for n bytes.
	 *
	 * @param n - Number of bytes to allocate.
	 *
	 * @return Pointer to byte(s).
	 * */
	public BytePointer 		pushByte(int n);

	/**
	 * Allocates memory for n chars.
	 *
	 * @param n - Number of chars to allocate.
	 *
	 * @return Pointer to char(s).
	 * */
	public CharPointer 		pushChar(int n);

	/**
	 * Allocates memory for n shorts.
	 *
	 * @param n - Number of shorts to allocate.
	 *
	 * @return Pointer to short(s).
	 * */
	public ShortPointer 	pushShort(int n);

	/**
	 * Allocates memory for n ints.
	 *
	 * @param n - Number of ints to allocate.
	 *
	 * @return Pointer to int(s).
	 * */
	public IntPointer 		pushInt(int n);

	/**
	 * Allocates memory for n floats.
	 *
	 * @param n - Number of floats to allocate.
	 *
	 * @return Pointer to float(s).
	 * */
	public FloatPointer 	pushFloat(int n);

	/**
	 * Allocates memory for n longs.
	 *
	 * @param n - Number of longs to allocate.
	 *
	 * @return Pointer to long(s).
	 * */
	public LongPointer 		pushLong(int n);

	/**
	 * Allocates memory for n doubles.
	 *
	 * @param n - Number of doubles to allocate.
	 *
	 * @return Pointer to double(s).
	 * */
	public DoublePointer 	pushDouble(int n);

	/**
	 * Allocates memory for n booleans.
	 *
	 * @param n - Number of booleans to allocate.
	 *
	 * @return Pointer to boolean(s).
	 * */
	public BooleanPointer 	pushBoolean(int n);

	/**
	 * Sets the new stack size.
	 * If the operation is successful, it returns true, otherwise false.
	 *
	 * @param newSize New stack size.
	 *
	 * @return Has memory been reallocated.
	 * */
	public boolean 			reallocate(int newSize);

	/**
	 * Allocates memory for string and returns a raw pointer.
	 *
	 * @param string - String to allocate.
	 *
	 * @return Address to string.
	 * */
	public @NativeType("char*") long rawUTF8(CharSequence string);
	
	/**
	 * Allocates memory for n bytes and returns a raw pointer.
	 *
	 * @param n - Number of bytes to allocate.
	 *
	 * @return Address to byte(s).
	 * */
	public @NativeType("char*") long rawByte(int n);

	/**
	 * Allocates memory for n chars and returns a raw pointer.
	 *
	 * @param n - Number of chars to allocate.
	 *
	 * @return Address to char(s).
	 * */
	public @NativeType("uint16_t*") long rawChar(int n);

	/**
	 * Allocates memory for n shorts and returns a raw pointer.
	 *
	 * @param n - Number of shorts to allocate.
	 *
	 * @return Address to short(s).
	 * */
	public @NativeType("short*") long rawShort(int n);

	/**
	 * Allocates memory for n ints and returns a raw pointer.
	 *
	 * @param n - Number of ints to allocate.
	 *
	 * @return Address to int(s).
	 * */
	public @NativeType("int*") long rawInt(int n);

	/**
	 * Allocates memory for n floats and returns a raw pointer.
	 *
	 * @param n - Number of floats to allocate.
	 *
	 * @return Address to float(s).
	 * */
	public @NativeType("float*") long rawFloat(int n);

	/**
	 * Allocates memory for n longs and returns a raw pointer.
	 *
	 * @param n - Number of long to allocate.
	 *
	 * @return Address to long(s).
	 * */
	public @NativeType("long*") long rawLong(int n);

	/**
	 * Allocates memory for n doubles and returns a raw pointer.
	 *
	 * @param n - Number of doubles to allocate.
	 *
	 * @return Address to double(s).
	 * */
	public @NativeType("double*") long rawDouble(int n);

	/**
	 * Allocates memory for n booleans and returns a raw pointer.
	 *
	 * @param n - Number of booleans to allocate.
	 *
	 * @return Address to boolean(s).
	 * */
	public @NativeType("boolean*") long rawBoolean(int n);

	/**
	 * Advances the stack pointer back by the size of the previously called push method.
	 * */
	public void 						pop();
	
	/**
	 * @return Returns the address to the first byte of the stack
	 * */
	public long 						address();
}
