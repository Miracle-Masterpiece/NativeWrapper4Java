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
import java.io.Closeable;
import java.lang.foreign.MemorySegment;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import nw4j.helpers.Helpers;
import nw4j.helpers.NativeType;
import nw4j.wrapper.c.allocators.MemoryAccessor;
import nw4j.wrapper.c.pointers.VoidPointer;

/**
 * @since 0.1
 * @author miracle-masterpiece
 * */
public final class StackMatrix2 implements AutoCloseable, Closeable {

	private static final int MAX_MATRIX_STACK_SIZE;
	private static final short MATRIX_SIZE 			= (4 * 4);

	static {
		//-DnwMatrixMaxStackSize
		String stackSize = System.getProperty("nwMatrixMaxStackSize");
		if (stackSize != null) {
			try {
				int _stackSize = Integer.parseInt(stackSize);
				MAX_MATRIX_STACK_SIZE = _stackSize;
			}catch(NumberFormatException e) {
				throw e;
			}
		}else {
			MAX_MATRIX_STACK_SIZE = 32;
		}
	}

	public static final byte PROJECTION 			= 0;
	public static final byte MODELVIEW 			 	= 1;

	private final float[] modelviewStack 	= new float[MAX_MATRIX_STACK_SIZE * MATRIX_SIZE];
	private final float[] projectionStack 	= new float[MAX_MATRIX_STACK_SIZE * MATRIX_SIZE];
	private final float[] tmp 				= new float[MATRIX_SIZE];
	private byte mode;
	private int modelViewStackPointer, projectionStackPointer;

	private final float[] result 			= new float[MATRIX_SIZE];
	private final long $float_matrix;
	private final FloatBuffer float_matrix;


	public StackMatrix2() {
		$float_matrix 			= Helpers.addressNonNull(MemoryAccessor.malloc(MATRIX_SIZE * VoidPointer.FLOAT_SIZE));
		float_matrix = MemorySegment.ofAddress($float_matrix).reinterpret(MATRIX_SIZE * VoidPointer.FLOAT_SIZE).asByteBuffer().order(ByteOrder.nativeOrder()).asFloatBuffer();
	}

	@Override
	public void close() {
		free();
	}

	public void free() {
		MemoryAccessor.free($float_matrix);
	}

	public void pushMatrix() {
		if (mode == MODELVIEW) {
			final float[] data = modelviewStack;
			final int ptr = modelViewStackPointer;
			System.arraycopy(data, (ptr * MATRIX_SIZE), data, (++modelViewStackPointer) * MATRIX_SIZE, MATRIX_SIZE);
		}else if (mode == PROJECTION) {
			final float[] data = projectionStack;
			final int ptr = projectionStackPointer;
			System.arraycopy(data, (ptr * MATRIX_SIZE), data, (++projectionStackPointer) * MATRIX_SIZE, MATRIX_SIZE);
		}
	}

	public void popMatrix() {
		if (mode == MODELVIEW) {
			--modelViewStackPointer;
		}else if (mode == PROJECTION) {
			--projectionStackPointer;
		}
	}

	public void matrixMode(int mode) {
		if (Helpers.ENABLE_CHECKS) if (mode != MODELVIEW && mode != PROJECTION) throw new RuntimeException(mode + " is no matrix mode!");
		this.mode = (byte)mode;
	}

	public @NativeType("float*") long getMatrix() {
		int offset 	= 0;
		float[] m 	= null;
		switch(mode) {
		case MODELVIEW : {
			offset = modelViewStackPointer << 4;
			m = modelviewStack;
		} break;
		case PROJECTION:{
			offset = projectionStackPointer << 4;
			m = projectionStack;
		}break;
		}
		float_matrix.put(0, m, offset, 16);
		return $float_matrix;
	}

	public @NativeType("float*") long getProjectionModelView() {
		final float[] proj 		= projectionStack;
		final int projOffset 	= projectionStackPointer << 4;
		final float[] modl 		= modelviewStack;
		final int modlOffset 	= modelViewStackPointer << 4;
		multMatrix(proj, projOffset, modl, modlOffset);
		float_matrix.put(0, result, 0, 16);
		return $float_matrix;
	}

	private void multMatrix(float[] A, int aOffset, float[] B, int bOffset) {
		for (int j = 0; j < 4; j++) {
			for (int i = 0; i < 16; i+=4) {
				result[i + j] = (A[aOffset + i] * B[bOffset + j]) + (A[aOffset + i + 1] * B[bOffset +  j + 4]) + (A[aOffset + i + 2] * B[bOffset +  j + 8])  + (A[aOffset + i + 3] * B[bOffset +  j + 12]);		
			}
		}
	}

	public void multMatrix(@NativeType("float*") long mat4) {
		MemoryAccessor.memcopy(mat4, $float_matrix, 16 * VoidPointer.FLOAT_SIZE);
		float_matrix.get(0, tmp);
		multMatrix(tmp);
	}
	
	public void multMatrix(float[] mat4) {		
		switch(mode) {
		case MODELVIEW:{
			final int offset = modelViewStackPointer << 4;
			multMatrix(modelviewStack, offset, mat4, 0);
			System.arraycopy(result, 0, modelviewStack, offset, MATRIX_SIZE);
		}return;

		case PROJECTION:{
			final int offset = projectionStackPointer << 4;
			multMatrix(projectionStack, offset, mat4, 0);
			System.arraycopy(result, 0, projectionStack, offset, MATRIX_SIZE);
		}return;
		}
	}

	public static void print(float[] mat) {
		for (int y = 0; y < 4; ++y) {
			for (int x = 0; x < 4; ++x) {
				System.out.print(mat[x + (y * 4)] + " ");
			}
			System.out.print("\n");
		}
		System.out.print("\n");
	}

	public void negate() {

	}

	public void transpose() {

	}

	public void getMatrix(float[] array) {
		
	}

	public void loadIdentity() {
		float[] m = null;
		int of = 0;
		switch(mode) {
		case MODELVIEW:{
			m 	= modelviewStack;
			of 	= modelViewStackPointer << 4;
		}break;

		case PROJECTION:{
			m 	= projectionStack;
			of 	= projectionStackPointer << 4;	
		}break;

		}
		m[of + 0] 	= 1; 	m[of + 1] 	= 0; 	m[of + 2] 	= 0; 	m[of + 3] 	= 0; 
		m[of + 4] 	= 0; 	m[of + 5] 	= 1; 	m[of + 6] 	= 0; 	m[of + 7] 	= 0;
		m[of + 8] 	= 0; 	m[of + 9] 	= 0; 	m[of + 10] 	= 1; 	m[of + 11] 	= 0;
		m[of + 12] 	= 0; 	m[of + 13] 	= 0; 	m[of + 14] 	= 0; 	m[of + 15] 	= 1;
	}

	public void scale(float x, float y, float z) {
		final float[] m = tmp;
		m[0] 	= x; 	m[1] 	= 0; 	m[2] 	= 0; 	m[3] 	= 0;
		m[4] 	= 0; 	m[5] 	= y; 	m[6] 	= 0; 	m[7] 	= 0;
		m[8] 	= 0; 	m[9] 	= 0; 	m[10] 	= z; 	m[11] 	= 0;
		m[12] 	= 0; 	m[13] 	= 0; 	m[14] 	= 0; 	m[15] 	= 1;
		multMatrix(m);
	}

	public void translate(float x, float y, float z) {
		final float[] m = tmp;
		m[0] 	= 1; 	m[1] 	= 0; 	m[2] 	= 0; 	m[3] 	= x;
		m[4] 	= 0; 	m[5] 	= 1; 	m[6] 	= 0; 	m[7] 	= y;
		m[8] 	= 0; 	m[9] 	= 0; 	m[10] 	= 1; 	m[11] 	= z;
		m[12] 	= 0; 	m[13] 	= 0; 	m[14] 	= 0; 	m[15] 	= 1;
		multMatrix(m);
	}

	public void rotate(float angle, int x, int y, int z) {
		final float[] m = tmp;

		final float TO_RAD = (float)(Math.PI / 180f);
		final float rad = angle * TO_RAD;
		final float sin = (float)Math.sin(rad);
		final float cos = (float)Math.cos(rad);

		if (x == 1) {
			m[0] 	= 1; 	m[1] 	= 0; 	m[2] 	= 0; 	m[3] 	= 0;
			m[4] 	= 0; 	m[5] 	= cos; 	m[6] 	= -sin; m[7] 	= 0;
			m[8] 	= 0; 	m[9] 	= sin; 	m[10] 	= cos; 	m[11] 	= 0;
			m[12] 	= 0; 	m[13] 	= 0; 	m[14] 	= 0; 	m[15] 	= 1;
		} else if (y == 1) {
			m[0] 	= cos; 	m[1] 	= 0; 	m[2] 	= -sin; m[3] 	= 0;
			m[4] 	= 0; 	m[5] 	= 1; 	m[6] 	= 0; 	m[7] 	= 0;
			m[8] 	= sin; 	m[9] 	= 0; 	m[10] 	= cos; 	m[11] 	= 0;
			m[12] 	= 0; 	m[13] 	= 0; 	m[14] 	= 0; 	m[15] 	= 1;
		} else if (z == 1) {
			m[0] 	= cos; 	m[1] 	= -sin; m[2] 	= 0; 	m[3] 	= 0;
			m[4] 	= sin; 	m[5] 	= cos; 	m[6] 	= 0; 	m[7] 	= 0;
			m[8] 	= 0; 	m[9] 	= 0; 	m[10] 	= 1; 	m[11] 	= 0;
			m[12] 	= 0; 	m[13] 	= 0; 	m[14] 	= 0; 	m[15] 	= 1;	
		} else {
			return;
		} 

		multMatrix(m);	
	}

	public void ortho(float left, float right, float bottom, float top, float znear, float zfar) {
		float[] mat4 = tmp;
		float m0 =  2 / (right - left);
		float m3 =  -((right+left)/(right - left));
		float m5 =  2 / (top - bottom);
		float m7 =  -((top+bottom)/(top - bottom));
		float m10 = 2/(zfar - znear);
		float m11 = -((zfar+znear)/(zfar - znear));
		mat4[0]  = m0; 	mat4[1]  = 0; 	mat4[2]   = 0; 	  mat4[3]  = m3;
		mat4[4]  = 0;  	mat4[5]  = m5;	mat4[6]   = 0;	  mat4[7]  = m7;
		mat4[8]  = 0;	mat4[9]  = 0;	mat4[10]  = m10;  mat4[11] = m11;
		mat4[12] = 0;   mat4[13] = 0;   mat4[14]  = 0;	  mat4[15] = 1;
		multMatrix(mat4);
	}

	public void frustum(float left, float right, float bottom, float top, float znear, float zfar) {
		float[] mat4 = tmp;
		float rightMinusLeft 	= right - left;
		float topMinusBottom 	= top - bottom;
		float zfarMinusZnear 	= zfar - znear;
		float twoMultZnear 		= 2 * znear;

		float m0 	= (twoMultZnear) 	 / (rightMinusLeft);
		float m2 	= (right+left) 	 	 / (rightMinusLeft);
		float m5 	= (twoMultZnear) 	 / (topMinusBottom);
		float m6 	= (top + bottom) 	 / (topMinusBottom);
		float m10 	= (zfar + znear) 	 / (zfarMinusZnear);
		float m11 	= (2 * zfar * znear) / (zfarMinusZnear);

		mat4[0]  = m0;	mat4[1]  = 0;	mat4[2]  = m2;	mat4[3]  = 0;
		mat4[4]  = 0;	mat4[5]  = m5;	mat4[6]	 = m6;	mat4[7]  = 0;
		mat4[8]  = 0;	mat4[9]  = 0;	mat4[10] =-m10;	mat4[11] =-m11;
		mat4[12] = 0;	mat4[13] = 0;	mat4[14] =-1;	mat4[15] = 0;
		multMatrix(mat4);
	}

	@SuppressWarnings("unused")
	public void projection(float fov, float aspect, float znear, float zfar) {
		float[] mat4 = tmp;
		final float TO_RAD = (float)(Math.PI / 180f);
		float radiands = (fov / 2) * TO_RAD;
		float sin_ = (float)Math.sin(radiands);
		float ctg =  (float)Math.cos(radiands) / sin_;
		float zEpsilon = zfar - znear;

		mat4[0] =   ctg / aspect;
		mat4[5] =   ctg;
		mat4[10] = -(zfar + znear) / zEpsilon;
		if (false) { //transpose
			mat4[14] = -1;
			mat4[11] =  (-2 * znear * zfar) / zEpsilon;
			mat4[15] =  0;
		}else {
			mat4[11] = -1;
			mat4[14] =  (-2 * znear * zfar) / zEpsilon;
			mat4[15] =  0;
		}
	}
}
