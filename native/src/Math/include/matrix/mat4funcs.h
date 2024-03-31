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
#include <math.h>

typedef float jfloat;

const jfloat TO_RAD = M_PI / 180.0f;

#ifdef __cplusplus
extern "C"{
#endif

	void mult_matrix(jfloat* A, jfloat* B, jfloat* C);

	void setIdentity(jfloat* mat4);
	
	void setScale(jfloat* mat4, jfloat x, jfloat y, jfloat z);
	
	void setTranslate(jfloat* mat4, jfloat x, jfloat y, jfloat z);
	
	void setXRotate(jfloat* mat4, jfloat angle);

	void setYRotate(jfloat* mat4, jfloat angle);

	void setZRotate(jfloat* mat4, jfloat angle);
	
	void setOrtho(jfloat* mat4, jfloat left, jfloat right, jfloat bottom, jfloat top, jfloat znear, jfloat zfar);
	
	void setFrustum(jfloat* mat4, jfloat left, jfloat right, jfloat bottom, jfloat top, jfloat znear, jfloat zfar);
	
	void setProjection(jfloat* mat4, jfloat fov, jfloat aspect, jfloat znear, jfloat zfar);

	void transpose(jfloat* const mat4);

	void add(jfloat* const A, jfloat* const B, jfloat* const result);

	void sub(jfloat* const A, jfloat* const B, jfloat* const result);

#ifdef __cplusplus
}
#endif

#include"mat4funcs.c"
