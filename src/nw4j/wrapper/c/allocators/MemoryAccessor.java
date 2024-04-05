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

import static java.lang.foreign.ValueLayout.JAVA_BOOLEAN;
import static java.lang.foreign.ValueLayout.JAVA_BYTE;
import static java.lang.foreign.ValueLayout.JAVA_CHAR;
import static java.lang.foreign.ValueLayout.JAVA_DOUBLE;
import static java.lang.foreign.ValueLayout.JAVA_FLOAT;
import static java.lang.foreign.ValueLayout.JAVA_INT;
import static java.lang.foreign.ValueLayout.JAVA_LONG;
import static java.lang.foreign.ValueLayout.JAVA_SHORT;
import static nw4j.helpers.Helpers.getCriticalMethodHandle;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nw4j.helpers.Helpers;
/**
 * A class that provides access to memory outside the java heap.
 * Methods {@link MemoryAccessor#malloc(long)}, {@link MemoryAccessor#calloc(long, int)}, {@link MemoryAccessor#realloc(long, long)}
 * return a raw pointer to memory.
 * <p>
 * To free memory, use the {@link MemoryAccessor#free(long)} method
 * 
 * @since 0.1
 * @author miracle-masterpiece
 * */
import nw4j.helpers.NativeType;
import nw4j.wrapper.c.pointers.VoidPointer;
public final class MemoryAccessor{

	private static final boolean ENABLE_NATIVE_TRACKING;
	private static final HashMap<Long, AllocateData> activeAllocates;

	static {
		
		String prop = System.getProperty("enableNativeTracking");
		
		if (prop != null) {
			if (prop.equals("true")) {
				ENABLE_NATIVE_TRACKING = true;
				activeAllocates = new HashMap<>();
			}else {
				ENABLE_NATIVE_TRACKING = false;
				activeAllocates = null;
			}
		}else {
			ENABLE_NATIVE_TRACKING = false;
			activeAllocates = null;
		}
	}

	private MemoryAccessor() {}

	/**@see {@link MemoryAccessor#malloc(long)}*/
	private static final MethodHandle allocateMemory;

	/**@see {@link MemoryAccessor#free(long)}*/
	private static final MethodHandle freeMemory;

	/**@see {@link MemoryAccessor#realloc(long, long)}*/
	private static final MethodHandle reallocateMemory;

	/**@see {@link MemoryAccessor#memset(long, int, long)}*/
	private static final MethodHandle setMemory;

	/**@see {@link MemoryAccessor#calloc(long)}*/
	private static final MethodHandle callocateMemory;

	/**@see {@link MemoryAccessor#memcopy(long, long, long)}*/
	private static final MethodHandle copyMemory;

	/**@see {@link MemoryAccessor#setByte}*/
	private static final MethodHandle setByte;

	/**@see {@link MemoryAccessor#setChar(long, char)}*/
	private static final MethodHandle setChar;

	/**@see {@link MemoryAccessor#setShort(long, short)}*/
	private static final MethodHandle setShort;

	/**@see {@link MemoryAccessor#setInt(long, int)}*/
	private static final MethodHandle setInt;

	/**@see {@link MemoryAccessor#setFloat(long, float)}*/
	private static final MethodHandle setFloat;

	/**@see {@link MemoryAccessor#setLong(long, long)}*/
	private static final MethodHandle setLong;

	/**@see {@link MemoryAccessor#setDouble(long, double)}*/
	private static final MethodHandle setDouble;

	/**@see {@link MemoryAccessor#setBoolean}*/
	private static final MethodHandle setBoolean;

	/**@see {@link MemoryAccessor#getByte(long)}*/
	private static final MethodHandle getByte;

	/**@see {@link MemoryAccessor#getChar(long)}*/
	private static final MethodHandle getChar;

	/**@see {@link MemoryAccessor#getShort(long)}*/
	private static final MethodHandle getShort;

	/**@see {@link MemoryAccessor#getInt(long)}*/
	private static final MethodHandle getInt;

	/**@see {@link MemoryAccessor#getFloat(long)}*/
	private static final MethodHandle getFloat;

	/**@see {@link MemoryAccessor#getLong(long)}*/
	private static final MethodHandle getLong;

	/**@see {@link MemoryAccessor#getDouble(long)}*/
	private static final MethodHandle getDouble;

	/**@see {@link MemoryAccessor#getBoolean(long)}*/
	private static final MethodHandle getBoolean;

	/**
	 * Stores the length in bytes of native primitive types.
	 * */
	public static final byte 
	NATIVE_SIZEOF_CHAR,
	NATIVE_SIZEOF_SHORT,
	NATIVE_SIZEOF_INT,
	NATIVE_SIZEOF_FLOAT,
	NATIVE_SIZEOF_LONG,
	NATIVE_SIZEOF_DOUBLE,
	NATIVE_SIZEOF_POINTER;

	static {

		Helpers.loadlib("MemoryAccessor");

		allocateMemory 		= getCriticalMethodHandle(new String(new char[]{'a','l','l','o','c','a','t','e','M','e','m','o','r','y'}), FunctionDescriptor.of(JAVA_LONG, JAVA_LONG));
		freeMemory 			= getCriticalMethodHandle(new String(new char[]{'f','r','e','e','M','e','m','o','r','y'}), FunctionDescriptor.ofVoid(JAVA_LONG));
		reallocateMemory 	= getCriticalMethodHandle(new String(new char[]{'r','e','a','l','l','o','c','a','t','e','M','e','m','o','r','y'}), FunctionDescriptor.of(JAVA_LONG, JAVA_LONG, JAVA_LONG));
		setMemory 			= getCriticalMethodHandle(new String(new char[]{'s','e','t','M','e','m','o','r','y'}), FunctionDescriptor.ofVoid(JAVA_LONG, JAVA_INT, JAVA_LONG));
		callocateMemory 	= getCriticalMethodHandle(new String(new char[]{'c','a','l','l','o','c','a','t','e','M','e','m','o','r','y'}), FunctionDescriptor.of(JAVA_LONG, JAVA_LONG, JAVA_INT));
		copyMemory 			= getCriticalMethodHandle(new String(new char[]{'c','o','p','y','M','e','m','o','r','y'}), FunctionDescriptor.ofVoid(JAVA_LONG, JAVA_LONG, JAVA_LONG));

		setByte				= getCriticalMethodHandle(new String(new char[]{'s','e','t','B','y','t','e'}), 	FunctionDescriptor.ofVoid(JAVA_LONG, JAVA_BYTE));
		setChar				= getCriticalMethodHandle(new String(new char[]{'s','e','t','C','h','a','r'}), 	FunctionDescriptor.ofVoid(JAVA_LONG, JAVA_CHAR));
		setShort			= getCriticalMethodHandle(new String(new char[]{'s','e','t','S','h','o','r','t'}), 	FunctionDescriptor.ofVoid(JAVA_LONG, JAVA_SHORT));
		setInt				= getCriticalMethodHandle(new String(new char[]{'s','e','t','I','n','t'}), 	FunctionDescriptor.ofVoid(JAVA_LONG, JAVA_INT));
		setFloat			= getCriticalMethodHandle(new String(new char[]{'s','e','t','F','l','o','a','t'}), 	FunctionDescriptor.ofVoid(JAVA_LONG, JAVA_FLOAT));
		setLong				= getCriticalMethodHandle(new String(new char[]{'s','e','t','L','o','n','g'}), 	FunctionDescriptor.ofVoid(JAVA_LONG, JAVA_LONG));
		setDouble			= getCriticalMethodHandle(new String(new char[]{'s','e','t','D','o','u','b','l','e'}), 	FunctionDescriptor.ofVoid(JAVA_LONG, JAVA_DOUBLE));
		setBoolean			= getCriticalMethodHandle(new String(new char[]{'s','e','t','B','o','o','l','e','a','n'}), FunctionDescriptor.ofVoid(JAVA_LONG, JAVA_BOOLEAN));

		getByte				= getCriticalMethodHandle(new String(new char[]{'g','e','t','B','y','t','e'}), 	FunctionDescriptor.of(JAVA_BYTE, JAVA_LONG));
		getChar				= getCriticalMethodHandle(new String(new char[]{'g','e','t','C','h','a','r'}), 	FunctionDescriptor.of(JAVA_CHAR, JAVA_LONG));
		getShort			= getCriticalMethodHandle(new String(new char[]{'g','e','t','S','h','o','r','t'}), 	FunctionDescriptor.of(JAVA_SHORT, JAVA_LONG));
		getInt				= getCriticalMethodHandle(new String(new char[]{'g','e','t','I','n','t'}), 	FunctionDescriptor.of(JAVA_INT, JAVA_LONG));
		getFloat			= getCriticalMethodHandle(new String(new char[]{'g','e','t','F','l','o','a','t'}), 	FunctionDescriptor.of(JAVA_FLOAT, JAVA_LONG));
		getLong				= getCriticalMethodHandle(new String(new char[]{'g','e','t','L','o','n','g'}), 	FunctionDescriptor.of(JAVA_LONG, JAVA_LONG));
		getDouble			= getCriticalMethodHandle(new String(new char[]{'g','e','t','D','o','u','b','l','e'}), 	FunctionDescriptor.of(JAVA_DOUBLE, JAVA_LONG));
		getBoolean			= getCriticalMethodHandle(new String(new char[]{'g','e','t','B','o','o','l','e','a','n'}), FunctionDescriptor.of(JAVA_BOOLEAN, JAVA_LONG));

		try {			
			NATIVE_SIZEOF_CHAR 		= (byte)getCriticalMethodHandle("sizeofChar", 		FunctionDescriptor.of(ValueLayout.JAVA_BYTE)).invoke();
			NATIVE_SIZEOF_SHORT 	= (byte)getCriticalMethodHandle("sizeofShort", 		FunctionDescriptor.of(ValueLayout.JAVA_BYTE)).invoke();
			NATIVE_SIZEOF_INT 		= (byte)getCriticalMethodHandle("sizeofInt", 		FunctionDescriptor.of(ValueLayout.JAVA_BYTE)).invoke();
			NATIVE_SIZEOF_FLOAT 	= (byte)getCriticalMethodHandle("sizeofFloat", 		FunctionDescriptor.of(ValueLayout.JAVA_BYTE)).invoke();
			NATIVE_SIZEOF_LONG 		= (byte)getCriticalMethodHandle("sizeofLong", 		FunctionDescriptor.of(ValueLayout.JAVA_BYTE)).invoke();
			NATIVE_SIZEOF_DOUBLE 	= (byte)getCriticalMethodHandle("sizeofDouble", 	FunctionDescriptor.of(ValueLayout.JAVA_BYTE)).invoke();
			NATIVE_SIZEOF_POINTER 	= (byte)getCriticalMethodHandle("sizeofPointer", 	FunctionDescriptor.of(ValueLayout.JAVA_BYTE)).invoke();
			System.gc();
		}catch(Throwable t) {
			throw new RuntimeException(t);
		}
	}


	private static final class AllocateData{
		final StackTraceElement[] stackTrace;
		final long allocSize;

		public AllocateData(StackTraceElement[] stackTrace, long allocSize) {
			this.stackTrace = stackTrace;
			this.allocSize = allocSize;
		}

		@Override
		public boolean equals(Object o) {
			return o == this;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(" size = ").append(allocSize).append("\n");
			for (int i = 0; i < stackTrace.length; ++i) {
				StackTraceElement e = stackTrace[i];
				sb.append(e).append("\n");
			}
			return sb.toString();
		}
	}

	/**
	 * Allocates memory outside the java heap of size n.
	 * @param n 	Number of bytes of requested memory.
	 * @return memory address.
	 * */
	public static long malloc(long n) {
		try {
			
			
			if (ENABLE_NATIVE_TRACKING) {
				synchronized (MemoryAccessor.class) {
					StackTraceElement[] stack = Thread.currentThread().getStackTrace();
					@NativeType(" void* ") final long pointer = (long)allocateMemory.invoke(n); 
					if (pointer != VoidPointer.nullptr)  
						activeAllocates.put(pointer, new AllocateData(stack, n));
					return pointer;
				}
			}else {			
				return (long)allocateMemory.invoke(n);
			}
		
		
		
		} catch (Throwable e) {throw new RuntimeException(e);}
	}
	
	/**
	 * Frees memory allocated outside the java heap.
	 * @param addres The address to be released.
	 * */
	public static void free(long address) {
		if (ENABLE_NATIVE_TRACKING) {
			synchronized (MemoryAccessor.class) {
				try {
					activeAllocates.remove(address);
					freeMemory.invoke(address);
				} catch (Throwable e) {throw new RuntimeException(e);}			
			}
		}else {
			try {
				freeMemory.invoke(address);
			} catch (Throwable e) {throw new RuntimeException(e);}			
		}

	}

	/**
	 * realloc returns a void pointer to the reallocated (and possibly moved) memory block.
	 * 
	 * If there isn't enough available memory to expand the block to the given size, the original block is left unchanged, and NULL is returned.
	 * If size is zero, then the block pointed to by memblock is freed; the return value is NULL, and memblock is left pointing at a freed block.
	 * 
	 * @param address	Pointer to previously allocated memory block.
	 * @param newsize	New size in bytes.
	 *
	 * @return value points to a storage space that is suitably aligned for storage of any type of object. To get a pointer to a type other than void, use a type cast on the return value.
	 * */
	public static long realloc(long address, long newsize) {
		try {
			
			if (ENABLE_NATIVE_TRACKING) {
				long newAddress = (long) reallocateMemory.invoke(address, newsize);
				if (newAddress != VoidPointer.nullptr) {
					activeAllocates.remove(address);
					activeAllocates.put(newAddress, new AllocateData(Thread.currentThread().getStackTrace(), newsize));
				}
				return newAddress;
			}else {				
				return (long)reallocateMemory.invoke(address, newsize);
			}
		} catch (Throwable e) {throw new RuntimeException(e);}
	}

	/**
	 * Sets all bytes in memory to the specified value.
	 * @param address	Memory address.
	 * @param value  	The value that all bytes will receive.
	 * @param len 		The number of bytes that will be set by the value.
	 * */
	public static void memset(long address, int value, long len) {
		try {
			setMemory.invoke(address, value, len);
		} catch (Throwable e) {throw new RuntimeException(e);}
	}

	/**
	 * Allocates storage space for an array of number elements, each of length size bytes. Each element is initialized to 0.
	 * @param count		Number of elements.
	 * @param typeSize	Length in bytes of each element.
	 * */
	public static long calloc(long count, int typeSize) {
		try {
			if (ENABLE_NATIVE_TRACKING) {
				@NativeType(" void* ") long pointer = (long)callocateMemory.invoke(count, typeSize);
				if (pointer != VoidPointer.nullptr) activeAllocates.put(pointer, new AllocateData(Thread.currentThread().getStackTrace(), typeSize * count));
				return pointer;
			}else {				
				return (long)callocateMemory.invoke(count, typeSize);
			}
		} catch (Throwable e) {throw new RuntimeException(e);}
	}

	/**
	 * Copies memory values from one address to another.
	 * @param srcAddress 	Source address.
	 * @param dstAddress  	The address where the data will be copied.
	 * @param len  			Length of copied bytes.
	 * */
	public static void memcopy(long srcAddress, long dstAddress, long len) {
		try {
			copyMemory.invoke(srcAddress, dstAddress, len);
		} catch (Throwable e) {throw new RuntimeException(e);}
	}

	/**
	 * Sets the byte value to a memory address.
	 * @param address	Address(Raw pointer).
	 * @param value 	Value.
	 * */
	public static void setByte(long address, byte value) {
		try {
			setByte.invoke(address, value);
		} catch (Throwable e) {throw new RuntimeException(e);}
	}

	/**
	 * Sets the char value to a memory address.
	 * @param address	Address(Raw pointer).
	 * @param value 	Value.
	 * */
	public static void setChar(long address, char value) {
		try {
			setChar.invoke(address, value);
		} catch (Throwable e) {throw new RuntimeException(e);}
	}

	/**
	 * Sets the short value to a memory address.
	 * @param address	Address(Raw pointer).
	 * @param value 	Value.
	 * */
	public static void setShort(long address, short value) {
		try {
			setShort.invoke(address, value);
		} catch (Throwable e) {throw new RuntimeException(e);}
	}

	/**
	 * Sets the int value to a memory address.
	 * @param address	Address(Raw pointer).
	 * @param value 	Value.
	 * */
	public static void setInt(long address, int value) {
		try {
			setInt.invoke(address, value);
		} catch (Throwable e) {throw new RuntimeException(e);}
	}

	/**
	 * Sets the float value to a memory address.
	 * @param address	Address(Raw pointer).
	 * @param value 	Value.
	 * */
	public static void setFloat(long address, float value) {
		try {
			setFloat.invoke(address, value);
		} catch (Throwable e) {throw new RuntimeException(e);}
	}

	/**
	 * Sets the long value to a memory address.
	 * @param address	Address(Raw pointer).
	 * @param value 	Value.
	 * */
	public static void setLong(long address, long value) {
		try {
			setLong.invoke(address, value);
		} catch (Throwable e) {throw new RuntimeException(e);}
	}

	/**
	 * Sets the double value to a memory address.
	 * @param address	Address(Raw pointer).
	 * @param value 	Value.
	 * */
	public static void setDouble(long address, double value) {
		try {
			setDouble.invoke(address, value);
		} catch (Throwable e) {throw new RuntimeException(e);}
	}

	/**
	 * Sets the boolean value to a memory address.
	 * @param address	Address(Raw pointer).
	 * @param value 	Value.
	 * */
	public static void setBoolean(long address, boolean value) {
		try {
			setBoolean.invoke(address, value);
		} catch (Throwable e) {throw new RuntimeException(e);}
	}

	/**
	 * Returns a byte value from a memory address.
	 * @param address  Memory address to read.
	 * @return Readed value.
	 * */
	public static byte getByte(long address) {
		try {
			return (byte)getByte.invoke(address);
		} catch (Throwable e) {throw new RuntimeException(e);}
	}

	/**
	 * Returns a char value from a memory address.
	 * @param address  Memory address to read.
	 * @return Readed value.
	 * */
	public static char getChar(long address) {
		try {
			return (char)getChar.invoke(address);
		} catch (Throwable e) {throw new RuntimeException(e);}
	}

	/**
	 * Returns a short value from a memory address.
	 * @param address  Memory address to read.
	 * @return Readed value.
	 * */
	public static short getShort(long address) {
		try {
			return (short)getShort.invoke(address);
		} catch (Throwable e) {throw new RuntimeException(e);}
	}

	/**
	 * Returns a int value from a memory address.
	 * @param address  Memory address to read.
	 * @return Readed value.
	 * */
	public static int getInt(long address) {
		try {
			return (int)getInt.invoke(address);
		} catch (Throwable e) {throw new RuntimeException(e);}
	}

	/**
	 * Returns a float value from a memory address.
	 * @param address  Memory address to read.
	 * @return Readed value.
	 * */
	public static float getFloat(long address) {
		try {
			return (float)getFloat.invoke(address);
		} catch (Throwable e) {throw new RuntimeException(e);}
	}

	/**
	 * Returns a long value from a memory address.
	 * @param address  Memory address to read.
	 * @return Readed value.
	 * */
	public static long getLong(long address) {
		try {
			return (long)getLong.invoke(address);
		} catch (Throwable e) {throw new RuntimeException(e);}
	}

	/**
	 * Returns a double value from a memory address.
	 * @param address  Memory address to read.
	 * @return Readed value.
	 * */
	public static double getDouble(long address) {
		try {
			return (double)getDouble.invoke(address);
		} catch (Throwable e) {throw new RuntimeException(e);}
	}

	/**
	 * Returns a boolean value from a memory address.
	 * @param address  Memory address to read.
	 * @return Readed value.
	 * */
	public static boolean getBoolean(long address) {
		try {
			return (boolean)getBoolean.invoke(address);
		} catch (Throwable e) {throw new RuntimeException(e);}
	}
	
	public static String getLog() {
		if (!ENABLE_NATIVE_TRACKING) return "Native tracking is disable";
		StringBuilder sb = new StringBuilder();
		Set<Map.Entry<Long, AllocateData>> set = activeAllocates.entrySet();
		for (Entry<Long, AllocateData> entry : set) {
			sb.append(entry.getKey()).append(" = ").append(entry.getValue()); 
		}
		return sb.toString();
	}
	
}