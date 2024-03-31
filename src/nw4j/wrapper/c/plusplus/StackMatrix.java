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

import nw4j.helpers.Helpers;
import nw4j.helpers.NativeType;
import nw4j.wrapper.c.allocators.MemoryAccessor;
import nw4j.wrapper.c.pointers.VoidPointer;

/**
 * @since 0.1
 * @author miracle-masterpiece
 * */
public class StackMatrix implements AutoCloseable, Closeable {

	private static final int MAX_MATRIX_STACK_SIZE;
	private static final short MATRIX_SIZEOF 			= VoidPointer.FLOAT_SIZE * (4 * 4);

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

	@NativeType("float*") private final long modelviewStack;
	@NativeType("float*") private final long projectionStack;
	@NativeType("float*") private final long result;

	private byte offsetPointerModelview;
	private byte offsetPointerProjection;

	private byte mode;

	public StackMatrix() {
		modelviewStack 	= Helpers.addressNonNull(MemoryAccessor.malloc(MAX_MATRIX_STACK_SIZE * MATRIX_SIZEOF));
		projectionStack = Helpers.addressNonNull(MemoryAccessor.malloc(MAX_MATRIX_STACK_SIZE * MATRIX_SIZEOF));
		result 			= Helpers.addressNonNull(MemoryAccessor.malloc(MATRIX_SIZEOF));
		mode 			= PROJECTION;
	}

	@Override
	public void close() {
		free();
	}

	public void free() {
		MemoryAccessor.free(modelviewStack);
		MemoryAccessor.free(projectionStack);
		MemoryAccessor.free(result);
	}
	
	public void pushMatrix() {
		if (mode == PROJECTION) {
			if (offsetPointerProjection+1  > MAX_MATRIX_STACK_SIZE) {
				throw new RuntimeException(new StackOverflowError("Projection matrix push stackoverflow!"));
			}
			long preview = projectionStack + offsetPointerProjection * MATRIX_SIZEOF;
			offsetPointerProjection++;
			MemoryAccessor.memcopy(preview, projectionStack + offsetPointerProjection * MATRIX_SIZEOF, MATRIX_SIZEOF);
		}else if (mode == MODELVIEW) {
			if (offsetPointerModelview+1  > MAX_MATRIX_STACK_SIZE) {
				throw new RuntimeException(new StackOverflowError("Modelview matrix push stackoverflow!"));
			}
			long preview = modelviewStack + offsetPointerModelview * MATRIX_SIZEOF;
			offsetPointerModelview++;
			MemoryAccessor.memcopy(preview, modelviewStack + offsetPointerModelview * MATRIX_SIZEOF, MATRIX_SIZEOF);
		}
	}

	public void popMatrix() {
		if (mode == PROJECTION) {
			if (offsetPointerProjection - 1 < 0) {
				return;
			}
			offsetPointerProjection -= 1;
		}else if (mode == MODELVIEW) {
			if (offsetPointerModelview - 1 < 0) {
				return;
			}
			offsetPointerModelview -= 1;
		}
	}

	public void matrixMode(int mode) {
		this.mode = (byte)mode;
	}

	public @NativeType("float*") long getMatrix() {
		return getAddressForCurrentMode();
	}

	public @NativeType("float*") long getProjectionModelView() {
		return Mat4Funcs.mult(projectionStack + (offsetPointerProjection * MATRIX_SIZEOF), modelviewStack + (offsetPointerModelview * MATRIX_SIZEOF), result);
	}

	private long getAddressForCurrentMode() {
		if (mode == MODELVIEW) {
			return modelviewStack + offsetPointerModelview * MATRIX_SIZEOF;
		}if (mode == PROJECTION) {
			return projectionStack + offsetPointerProjection * MATRIX_SIZEOF;
		}
		throw new IllegalArgumentException("Mode is: " + mode);
	}

	public void multMatrix(@NativeType("float*") long mat4) {
		long mat = getAddressForCurrentMode();
		Mat4Funcs.mult(mat, mat4, mat);
	}

	public void negate() {
		long mat = getAddressForCurrentMode();
		Mat4Funcs.negate(mat);
	}
	
	public void transpose() {
		long mat = getAddressForCurrentMode();
		Mat4Funcs.transpose(mat);
	}
	
	public void loadIdentity() {
		Mat4Funcs.loadIdentity(getAddressForCurrentMode());
	}

	public void scale(float x, float y, float z) {
		long mat = getAddressForCurrentMode();
		check();
		long tmp = mat + MATRIX_SIZEOF;
		Mat4Funcs.scale(tmp, x, y, z);
		Mat4Funcs.mult(mat, tmp, mat);
	}

	public void translate(float x, float y, float z) {
		long mat = getAddressForCurrentMode();
		check();
		long tmp = mat + MATRIX_SIZEOF;
		Mat4Funcs.translate(tmp, x, y, z);
		Mat4Funcs.mult(mat, tmp, mat);
	}

	public void rotate(float angle, int x, int y, int z) {
		long mat = getAddressForCurrentMode();
		check();
		long tmp = mat + MATRIX_SIZEOF;
		if (x > 0) {
			Mat4Funcs.xRotate(tmp, angle);
		} else if (y > 0) {
			Mat4Funcs.yRotate(tmp, angle);
		} else if (z > 0) {
			Mat4Funcs.zRotate(tmp, angle);
		}
		Mat4Funcs.mult(mat, tmp, mat);
	}

	public void ortho(float left, float right, float bottom, float top, float znear, float zfar) {
		long mat = getAddressForCurrentMode();
		check();
		long tmp = mat + MATRIX_SIZEOF;
		Mat4Funcs.ortho(tmp,left, right, bottom, top, znear, zfar);
		Mat4Funcs.mult(mat, tmp, mat);
	}

	public void frustum(float left, float right, float bottom, float top, float znear, float zfar) {
		long mat = getAddressForCurrentMode();
		check();
		long tmp = mat + MATRIX_SIZEOF;
		Mat4Funcs.frustum(tmp,left, right, bottom, top, znear, zfar);
		Mat4Funcs.mult(mat, tmp, mat);
	}

	public void projection(float fov, float aspect, float znear, float zfar) {
		long mat = getAddressForCurrentMode();
		check();
		long tmp = mat + MATRIX_SIZEOF;
		Mat4Funcs.projection(mat, fov, aspect, znear, zfar);
		Mat4Funcs.mult(mat, tmp, mat);
	}
	
	private void check() {
		if (Helpers.ENABLE_CHECKS) {
			if (mode == MODELVIEW) {
				if (offsetPointerModelview + 1 > MAX_MATRIX_STACK_SIZE) {
					throw new RuntimeException(new StackOverflowError("Matrix modelview stack overflow!"));
				}
			}else if (mode == PROJECTION) {
				if (offsetPointerProjection + 1 > MAX_MATRIX_STACK_SIZE) {
					throw new RuntimeException(new StackOverflowError("Matrix projection stack overflow!"));
				}
			}	
		}
	}
}
