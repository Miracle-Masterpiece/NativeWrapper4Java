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
#ifdef USE_INTRINSIC
	#include <xmmintrin.h>
#endif

#include <stdio.h>
#include <string.h>
#include <inttypes.h>

	void print(jfloat* mat4) {
		for (int y = 0; y < 4; ++y) {
			for (int x = 0; x < 4; ++x) {
				int index = x + (y * 4);
				printf("%f ", mat4[index]);
			}
			printf("\n");
		}
		printf("\n");
	}	

	void printVec(jfloat* vec){
		for (int i = 0; i < 4; ++i){
			printf("%f ", vec[i]);
		}
		printf("\n");
	}

	void mult_matrix(jfloat* A, jfloat* B, jfloat* C) {

		#ifdef USE_INTRINSIC
			//This code is copied from stackoverflow xD		
			const __m128 row1 = _mm_load_ps(&B[0]);
    		const __m128 row2 = _mm_load_ps(&B[4]);
    		const __m128 row3 = _mm_load_ps(&B[8]);
    		const __m128 row4 = _mm_load_ps(&B[12]);
    		for(int i = 0; i < 4; i++) {
       			__m128 brod1 = 	_mm_set1_ps(A[4*i + 0]);
        		__m128 brod2 = 	_mm_set1_ps(A[4*i + 1]);
        		__m128 brod3 = 	_mm_set1_ps(A[4*i + 2]);
        		__m128 brod4 = 	_mm_set1_ps(A[4*i + 3]);
        	
        		__m128 row   = 	_mm_add_ps(_mm_add_ps(
                       			_mm_mul_ps(brod1, row1),
                       			_mm_mul_ps(brod2, row2)),
                       			_mm_add_ps(
                       			_mm_mul_ps(brod3, row3),
                       			_mm_mul_ps(brod4, row4)));
        		_mm_store_ps(&C[4*i], row);
    	}
		#else
			float tmp[16];			
			for (int i = 0; i < 16; i+=4) {
				for (int j = 0; j < 4; j++) {
					tmp[i + j] = (A[i] * B[j]) + (A[i + 1] * B[j + 4]) + (A[i + 2] * B[j + 8])  + (A[i + 3] * B[j + 12]);		
				}
			}
			memcpy(tmp, C, sizeof(jfloat) * 16);
		#endif
	}

	void mult_vector(jfloat* mat4, jfloat* vec4){
		/**
		 * 0		0  1  2  3 			0
		 * 1		4  5  6  7			1
		 * 2		8  9  10 11			2
		 * 3		12 13 14 15			3
		 */
		#ifdef USE_INTRINSIC
		__m128 vec = _mm_load_ps(vec4);
			for (int i = 0; i < 4; ++i){
				__m128 mat = _mm_load_ps(&mat4[i * 4]);
				__m128 res = _mm_mul_ps(mat, vec);
				vec4[i] = res[0] + res[1] + res[2] + res[3];
			}
		#else
			jfloat result[4] = {0, 0, 0, 0};
			for (int j = 0; j < 4; ++j){
		 		for (int i = 0; i < 4; ++i){
		 			result[j] += mat4[j * 4 + i] * vec4[i];
		 		}				
		 	}
			memcpy(vec4, result, (sizeof(jfloat) * 4));
		#endif
	}

	void setIdentity(jfloat* mat4){
		mat4[0]  = 1; 	mat4[1]  = 0; 	mat4[2]  = 0; 	mat4[3]  = 0;
		mat4[4]  = 0; 	mat4[5]  = 1; 	mat4[6]  = 0; 	mat4[7]  = 0;
		mat4[8]  = 0; 	mat4[9]  = 0; 	mat4[10] = 1; 	mat4[11] = 0;
		mat4[12] = 0; 	mat4[13] = 0; 	mat4[14] = 0; 	mat4[15] = 1;
	}
	
	void setScale(jfloat* mat4, jfloat x, jfloat y, jfloat z){
		mat4[0]  = x; 	mat4[1]  = 0; 	mat4[2]  = 0; 	mat4[3]  = 0;
		mat4[4]  = 0; 	mat4[5]  = y; 	mat4[6]  = 0; 	mat4[7]  = 0;
		mat4[8]  = 0; 	mat4[9]  = 0; 	mat4[10] = z; 	mat4[11] = 0;
		mat4[12] = 0; 	mat4[13] = 0; 	mat4[14] = 0; 	mat4[15] = 1;
	}
	
	void setTranslate(jfloat* mat4, jfloat x, jfloat y, jfloat z){
		mat4[0]  = 1; 	mat4[1]  = 0; 	mat4[2]  = 0; 	mat4[3]  = x;
		mat4[4]  = 0; 	mat4[5]  = 1; 	mat4[6]  = 0; 	mat4[7]  = y;
		mat4[8]  = 0; 	mat4[9]  = 0; 	mat4[10] = 1; 	mat4[11] = z;
		mat4[12] = 0; 	mat4[13] = 0; 	mat4[14] = 0; 	mat4[15] = 1;
	}
	
	void setXRotate(jfloat* mat4, jfloat angle){
		const jfloat rad = angle * TO_RAD;
		const jfloat sin_ = sinf(rad);
		const jfloat cos_ = cosf(rad); 	
		
		mat4[0]  = 1; 	mat4[1]  = 0; 		mat4[2]  = 0; 		mat4[3]  = 0;
		mat4[4]  = 0; 	mat4[5]  = cos_; 	mat4[6]  = -sin_; 	mat4[7]  = 0;
		mat4[8]  = 0; 	mat4[9]  = sin_; 	mat4[10] =  cos_; 	mat4[11] = 0;
		mat4[12] = 0; 	mat4[13] = 0; 		mat4[14] = 0; 		mat4[15] = 1;
	}

	void setYRotate(jfloat* mat4, jfloat angle){
		const jfloat rad = angle * TO_RAD;
		const jfloat sin_ = sinf(rad);
		const jfloat cos_ = cosf(rad); 	
		
		mat4[0]  = cos_; 	mat4[1]  = 0; 	mat4[2]  = -sin_; 	mat4[3]  = 0;
		mat4[4]  = 0; 		mat4[5]  = 1; 	mat4[6]  = 0; 		mat4[7]  = 0;
		mat4[8]  = sin_; 	mat4[9]  = 0; 	mat4[10] = cos_; 	mat4[11] = 0;
		mat4[12] = 0; 		mat4[13] = 0; 	mat4[14] = 0; 		mat4[15] = 1;
	}

	void setZRotate(jfloat* mat4, jfloat angle){
		const jfloat rad = angle * TO_RAD;
		const jfloat sin_ = sinf(rad);
		const jfloat cos_ = cosf(rad); 	
		
		mat4[0]  = cos_; 	mat4[1]  = -sin_; 	mat4[2]  = 0; 	mat4[3]  = 0;
		mat4[4]  = sin_; 	mat4[5]  = cos_; 	mat4[6]  = 0; 	mat4[7]  = 0;
		mat4[8]  = 0; 		mat4[9]  = 0; 		mat4[10] = 1; 	mat4[11] = 0;
		mat4[12] = 0; 		mat4[13] = 0; 		mat4[14] = 0; 	mat4[15] = 1;
	}
	
	void setOrtho(jfloat* mat4, jfloat left, jfloat right, jfloat bottom, jfloat top, jfloat znear, jfloat zfar) {
		const jfloat m0 =  2 / (right - left);
		const jfloat m3 =  -((right+left)/(right - left));
		const jfloat m5 =  2 / (top - bottom);
		const jfloat m7 =  -((top+bottom)/(top - bottom));
		const jfloat m10 = 2/(zfar - znear);
		const jfloat m11 = -((zfar+znear)/(zfar - znear));

		/**
		 * 
		 *	-m0,   0,    0,    m3,
		 *	 0,   -m5,   0,    m7,
		 *	 0,    0,   -m10,  m11,
		 *	 0,    0,    0,    1
		 * 
		 * */	
		mat4[0]  = m0; 	mat4[1]  = 0; 	mat4[2]   = 0; 	  mat4[3]  = m3;
		mat4[4]  = 0;  	mat4[5]  = m5;	mat4[6]   = 0;	  mat4[7]  = m7;
		mat4[8]  = 0;	mat4[9]  = 0;	mat4[10]  = m10;  mat4[11] = m11;
		mat4[12] = 0;   mat4[13] = 0;   mat4[14]  = 0;	  mat4[15] = 1;
	}
	
	void setFrustum(jfloat* mat4, jfloat left, jfloat right, jfloat bottom, jfloat top, jfloat znear, jfloat zfar) {
		const jfloat rightMinusLeft 	= right - left;
		const jfloat topMinusBottom 	= top - bottom;
		const jfloat zfarMinusZnear 	= zfar - znear;
		const jfloat twoMultZnear 		= 2 * znear;
		
		const jfloat m0 	= (twoMultZnear) 	 / (rightMinusLeft);
		const jfloat m2 	= (right+left) 	 	 / (rightMinusLeft);
		const jfloat m5 	= (twoMultZnear) 	 / (topMinusBottom);
		const jfloat m6 	= (top + bottom) 	 / (topMinusBottom);
		const jfloat m10 	= (zfar + znear) 	 / (zfarMinusZnear);
		const jfloat m11 	= (2 * zfar * znear) / (zfarMinusZnear);
		
		mat4[0]  = m0;	mat4[1]  = 0;	mat4[2]  = m2;	mat4[3]  = 0;
		mat4[4]  = 0;	mat4[5]  = m5;	mat4[6]	 = m6;	mat4[7]  = 0;
		mat4[8]  = 0;	mat4[9]  = 0;	mat4[10] =-m10;	mat4[11] =-m11;
		mat4[12] = 0;	mat4[13] = 0;	mat4[14] =-1;	mat4[15] = 0;
	}
	
	void setProjection(jfloat* mat4, jfloat fov, jfloat aspect, jfloat znear, jfloat zfar){
		const jfloat radiands = (fov / 2) * TO_RAD;
		const jfloat sin_ = sin(radiands);
		const jfloat ctg =  cos(radiands) / sin_;
		const jfloat zEpsilon = zfar - znear;

		/**
		 * 0  1  2  3
		 * 4  5  6  7      
		 * 8  9  10 11       
		 * 12 13 14 15		
		 * */
		mat4[0] =   ctg / aspect;
		mat4[5] =   ctg;
		mat4[10] = -(zfar + znear) / zEpsilon;
		if (0) { //transpose
			mat4[14] = -1;
			mat4[11] =  (-2 * znear * zfar) / zEpsilon;
			mat4[15] =  0;
		}else {
			mat4[11] = -1;
			mat4[14] =  (-2 * znear * zfar) / zEpsilon;
			mat4[15] =  0;
		}
	}
	
	void transpose(jfloat* const mat4) {
		for (uint32_t y = 0; y < 3; ++y) {
			for (uint32_t x = y; x < 4; ++x) {
				const uint32_t in 	= x + (y << 2 /*a.k.a * 4*/);
				const uint32_t out 	= y + (x << 2 /*a.k.a * 4*/);
				const jfloat tmp1 = mat4[in];
				const jfloat tmp2 = mat4[out];
				mat4[in] 	= tmp2;
				mat4[out] 	= tmp1;				
			}
		}
	}
	void add(jfloat* const A, jfloat* const B, jfloat* const result){
		#ifdef USE_INTRINSIC
			for (uint32_t i = 0; i < 4; ++i){
				const uint32_t index = i << 2;/*a.k.a * 4*/
				__m128 a = _mm_load_ps(&A[index]);
    			__m128 b = _mm_load_ps(&B[index]);
				__m128 c = _mm_add_ps(a, b);	
				_mm_store_ps(&result[index], c);
			}
		#else
			for (uint32_t i = 0; i < 4; ++i){
				result[i] = A[i] + B[i];
			}
		#endif
	}

	void sub(jfloat* const A, jfloat* const B, jfloat* const result){
		#ifdef USE_INTRINSIC
			for (uint32_t i = 0; i < 4; ++i){
			const uint32_t index = i << 2;/*a.k.a * 4*/
			__m128 a = _mm_load_ps(&A[index]);
    		__m128 b = _mm_load_ps(&B[index]);
			__m128 c = _mm_sub_ps(a, b);	
			_mm_store_ps(&result[index], c);
		}
		#else
			for (uint32_t i = 0; i < 4; ++i){
				result[i] = A[i] - B[i];
			}
		#endif
	}

	///=============== new ========================///
	void setZero(jfloat* const mat4){
		memset(mat4, 0, sizeof(jfloat) * 4);
	}

	void negate(jfloat* const mat4){
		#ifdef USE_INTRINSIC
			const float A[] = {-1, -1, -1, -1};
			__m128 oneNegative = _mm_load_ps(A); 
			for (int i = 0; i < 4; ++i){
				__m128 row 		= _mm_load_ps(&mat4[i * 4]); 
				__m128 result 	= _mm_mul_ps(row, oneNegative);
				_mm_store_ps(&mat4[i*4], result);
			}
		#else
			for (int i = 0; i < 16; ++i){
				mat4[i] *= -1;
			}
		#endif
	}
