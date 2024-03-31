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
package nw4j.wrapper.c.plusplus;

import static java.lang.foreign.ValueLayout.JAVA_FLOAT;
import static java.lang.foreign.ValueLayout.JAVA_LONG;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.Linker.Option;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;

import nw4j.helpers.Helpers;
import nw4j.helpers.NativeType;
import nw4j.wrapper.c.allocators.MemoryAccessor;
import nw4j.wrapper.c.pointers.FloatPointer;

/**
 * A class with methods for working with float arrays, 
 * which are a two-dimensional 4x4 matrix.
 * 
 * @since 0.1
 * @author miracle-masterpiece
 * */
public final class Mat4Funcs {

	private Mat4Funcs() {}

	private static final MethodHandle multMatrix;
	private static final MethodHandle setIdentity;
	private static final MethodHandle setScale;
	private static final MethodHandle setTranslate;
	private static final MethodHandle setXRotate;
	private static final MethodHandle setYRotate;
	private static final MethodHandle setZRotate;
	private static final MethodHandle setOrtho;
	private static final MethodHandle setFrustum;
	private static final MethodHandle setProjection;
	private static final MethodHandle transpose;
	private static final MethodHandle add;
	private static final MethodHandle sub;
	private static final MethodHandle setZero;
	private static final MethodHandle negate;
	private static final MethodHandle toVector;

	static {
		Helpers.loadlib("MathNativeFuncs");

		multMatrix 		= createCriticalMethodHandle("mult_matrix", 	FunctionDescriptor.ofVoid(JAVA_LONG, JAVA_LONG, JAVA_LONG));
		setIdentity 	= createCriticalMethodHandle("setIdentity", 	FunctionDescriptor.ofVoid(JAVA_LONG));
		setScale 		= createCriticalMethodHandle("setScale", 		FunctionDescriptor.ofVoid(JAVA_LONG, JAVA_FLOAT, JAVA_FLOAT, JAVA_FLOAT));
		setTranslate 	= createCriticalMethodHandle("setTranslate", 	FunctionDescriptor.ofVoid(JAVA_LONG, JAVA_FLOAT, JAVA_FLOAT, JAVA_FLOAT));
		setXRotate 		= createCriticalMethodHandle("setXRotate", 		FunctionDescriptor.ofVoid(JAVA_LONG, JAVA_FLOAT));
		setYRotate 		= createCriticalMethodHandle("setYRotate", 		FunctionDescriptor.ofVoid(JAVA_LONG, JAVA_FLOAT));
		setZRotate 		= createCriticalMethodHandle("setZRotate", 		FunctionDescriptor.ofVoid(JAVA_LONG, JAVA_FLOAT));
		setOrtho 		= createCriticalMethodHandle("setOrtho", 		FunctionDescriptor.ofVoid(JAVA_LONG, JAVA_FLOAT, JAVA_FLOAT, JAVA_FLOAT, JAVA_FLOAT, JAVA_FLOAT, JAVA_FLOAT));
		setFrustum 		= createCriticalMethodHandle("setFrustum", 		FunctionDescriptor.ofVoid(JAVA_LONG, JAVA_FLOAT, JAVA_FLOAT, JAVA_FLOAT, JAVA_FLOAT, JAVA_FLOAT, JAVA_FLOAT));
		setProjection 	= createCriticalMethodHandle("setProjection", 	FunctionDescriptor.ofVoid(JAVA_LONG, JAVA_FLOAT, JAVA_FLOAT, JAVA_FLOAT, JAVA_FLOAT));
		transpose 		= createCriticalMethodHandle("transpose", 		FunctionDescriptor.ofVoid(JAVA_LONG));
		add 			= createCriticalMethodHandle("add", 			FunctionDescriptor.ofVoid(JAVA_LONG, JAVA_LONG, JAVA_LONG));
		sub 			= createCriticalMethodHandle("sub", 			FunctionDescriptor.ofVoid(JAVA_LONG, JAVA_LONG, JAVA_LONG));		
		setZero 		= createCriticalMethodHandle("setZero", 		FunctionDescriptor.ofVoid(JAVA_LONG));
		negate 			= createCriticalMethodHandle("negate", 			FunctionDescriptor.ofVoid(JAVA_LONG));
		toVector		= createCriticalMethodHandle("mult_vector", 	FunctionDescriptor.ofVoid(JAVA_LONG, JAVA_LONG));
	}

	/**
	 * Multiplies a matrix by a vector and puts the result in the transmitted vector.
	 * @param mat4 	A float array that is a matrix4x4.
	 * @param vec4 	A float array that is a vector4.
	 * */
	public static @NativeType("float*") long toVector(@NativeType("float*") long mat4, @NativeType("float*") long vec4) {
		try{
			toVector.invoke(mat4, vec4);
		}catch (Throwable e) {throw new RuntimeException(e);}
		return vec4;
	}

	/**
	 * Multiplies a matrix by -1 and puts the result in the transmitted matrix.
	 *    / 0  1  2  3  \ 
	 * -1*| 5  6  7  8  |
	 *	  | 8  9  10 11 |
	 *	  \	12 13 14 15 /
	 *
	 * @param mat4 	Matrix that will be multiplied by -1.
	 * */
	public static @NativeType("float*") long negate(@NativeType("float*") long mat4) {
		try{
			negate.invoke(mat4);
		}catch (Throwable e) {throw new RuntimeException(e);}
		return mat4;
	}

	/**
	 * Sets all values in a matrix to 0.
	 * @param mat4 	A float array that is a 4x4 matrix.
	 * */
	public static @NativeType("float*") long setZero(@NativeType("float*") long mat4) {
		try{
			setZero.invoke(mat4);
		}catch (Throwable e) {throw new RuntimeException(e);}
		return mat4;
	}

	/**
	 * Subtracts matrix B from matrix A and puts the result into matrix result.
	 * @param A 		First matrix.
	 * @param B 		Second matrix.
	 * @param result 	Result matrix.
	 * */
	public static @NativeType("float*") long sub(@NativeType("float*") long A, @NativeType("float*") long B, @NativeType("float*") long result) {
		try{
			sub.invoke(A, B, result);
		}catch (Throwable e) {throw new RuntimeException(e);}
		return result;
	}

	/**
	 * Adds matrix A and B and puts the result in the result matrix.
	 * @param A 		First matrix.
	 * @param B 		Second matrix.
	 * @param result 	Result matrix.
	 * */
	public static @NativeType("float*") long add(@NativeType("float*") long a, @NativeType("float*") long b, @NativeType("float*") long result) {
		try{
			add.invoke(a, b, result);
		}catch (Throwable e) {throw new RuntimeException(e);}
		return result;
	}

	/**
	 * Transposes a matrix
	 *    
	 * Before transpose:
	 *    / 0  1  2  3  \ 
	 * 	  | 5  6  7  8  |
	 *	  | 8  9  10 11 |
	 *	  \	12 13 14 15 /
	 * 	  
	 * After transpose:
	 * 	  / 0  4  8  12 \ 
	 * 	  | 1  5  9  13 |
	 *	  | 2  6  10 14 |
	 *	  \	3  7  11 15 /
	 * 
	 * */
	public static @NativeType("float*") long transpose(@NativeType("float*") long mat4) {
		try{
			transpose.invoke(mat4);
		}catch (Throwable e) {throw new RuntimeException(e);}
		return mat4;
	}

	/**
	 * Sets matrix values as projection matrices.
	 * */
	public static @NativeType("float*") long projection(@NativeType("float*") long mat4, float fov, float aspect, float znear, float zfar) {
		try{
			setProjection.invoke(mat4, fov, aspect, znear, zfar);
		}catch (Throwable e) {throw new RuntimeException(e);}
		return mat4;
	}

	/**
	 * Sets matrix values as frustum matrices.
	 * */
	public static @NativeType("float*") long frustum(@NativeType("float*")long mat4, float left, float right, float bottom, float top, float znear, float zfar) {
		try{
			setFrustum.invoke(mat4, left, right, bottom, top, znear, zfar);
		}catch (Throwable e) {throw new RuntimeException(e);}
		return mat4;
	}

	/**
	 * Sets matrix values as ortho matrices.
	 * */
	public static @NativeType("float*") long ortho(@NativeType("float*")long mat4, float left, float right, float bottom, float top, float znear, float zfar) {
		try{
			setOrtho.invoke(mat4, left, right, bottom, top, znear, zfar);
		}catch (Throwable e) {throw new RuntimeException(e);}
		return mat4;
	}

	/**
	 * Sets matrix values as zRotate matrices.
	 * 
	 * @param mat4 	A float array that is a 4x4 matrix.
	 * @param angle Angle to rotate.
	 * */
	public static @NativeType("float*") long zRotate(@NativeType("float*") long mat4, float angle) {
		try{
			setZRotate.invoke(mat4, angle);
		}catch (Throwable e) {throw new RuntimeException(e);}
		return mat4;
	}

	/**
	 * Sets matrix values as yRotate matrices.
	 * 
	 * @param mat4 	A float array that is a 4x4 matrix.
	 * @param angle Angle to rotate.
	 * */
	public static @NativeType("float*") long yRotate(@NativeType("float*") long mat4, float angle) {
		try{
			setYRotate.invoke(mat4, angle);
		}catch (Throwable e) {throw new RuntimeException(e);}
		return mat4;
	}

	/**
	 * Sets matrix values as xRotate matrices.
	 * 
	 * @param mat4 	A float array that is a 4x4 matrix.
	 * @param angle Angle to rotate.
	 * */
	public static @NativeType("float*") long xRotate(@NativeType("float*") long mat4, float angle) {
		try{
			setXRotate.invoke(mat4, angle);
		}catch (Throwable e) {throw new RuntimeException(e);}
		return mat4;
	}

	/**
	 * Sets matrix values as translate matrices.
	 * 
	 * @param mat4 	A float array that is a 4x4 matrix.
	 * @param x 	Translate x axis.
	 * @param y 	Translate y axis.
	 * @param z 	Translate z axis.
	 * */
	public static @NativeType("float*") long translate(@NativeType("float*") long mat4, float x, float y, float z) {
		try{
			setTranslate.invoke(mat4, x, y, z);
		}catch (Throwable e) {throw new RuntimeException(e);}
		return mat4;
	}

	/**
	 * Sets matrix values as scale matrices.
	 * 
	 * @param mat4 	A float array that is a 4x4 matrix.
	 * @param x 	Scale x axis.
	 * @param y 	Scale y axis.
	 * @param z 	Scale z axis.
	 * */
	public static @NativeType("float*") long scale(@NativeType("float*") long mat4, float x, float y, float z) {
		try{
			setScale.invoke(mat4, x, y, z);
		}catch (Throwable e) {throw new RuntimeException(e);}
		return mat4;
	}

	/**
	 * Multiplies matrix A by matrix B and puts the result in the result matrix.
	 * 
	 * Note: 
	 * The result can also be set to the matrix that participated in the operation, 
	 * that is, to matrix A or B, you just need to pass the result matrix pointer to matrix A or B.
	 * 
	 * @param A 		A float array that is a 4x4 matrix.
	 * @param B 		A float array that is a 4x4 matrix.
	 * @param result 	A float array that is a 4x4 matrix. Where store to result.
	 * */
	public static @NativeType("float*") long mult(@NativeType("float*") long A, @NativeType("float*") long B, @NativeType("float*") long result) {
		try{
			multMatrix.invoke(A, B, result);
		}catch (Throwable e) {throw new RuntimeException(e);}
		return result;
	}

	/**
	 * Sets the matrix to be the identity matrix.
	 * @param mat4 		A float array that is a 4x4 matrix.
	 * */
	public static @NativeType("float*") long loadIdentity(@NativeType("float*") long mat4) {
		try{
			setIdentity.invoke(mat4);
		}catch (Throwable e) {throw new RuntimeException(e);}
		return mat4;
	}

	//gen critical methodHandle
	private static MethodHandle createCriticalMethodHandle(String name, FunctionDescriptor desc) {
		final Linker nativeLinker 	= Linker.nativeLinker();
		final Option critical 		= Option.critical(false);
		MethodHandle handle = null;
		try{
			handle = nativeLinker.downcallHandle(SymbolLookup.loaderLookup().find(name).orElseThrow(), desc, critical);
		}catch(Exception e) {
			throw e;
		}
		return handle;
	}

	//print array as matrix4x4
	public static void print(FloatPointer ptr) {
		for (int y = 0; y < 4; ++y) {
			for (int x = 0; x < 4; ++x) {
				int index = x + (y * 4);
				System.out.print(ptr.get(index) + " ");
			}
			System.out.println();
		}
	}

	//print array as matrix4x4
	public static void print(long ptr) {
		for (int y = 0; y < 4; ++y) {
			for (int x = 0; x < 4; ++x) {
				int index = x + (y * 4);
				System.out.print(MemoryAccessor.getFloat(ptr+(index*4)) + " ");
			}
			System.out.println();
		}
	}
}
