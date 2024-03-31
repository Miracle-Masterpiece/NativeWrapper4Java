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
#include<inttypes.h>

typedef int8_t 				jbyte;
typedef int16_t 			jchar;
typedef int16_t 			jshort;
typedef int32_t 			jint;
typedef int64_t 			jlong;

typedef float 				jfloat;
typedef double 				jdouble;

typedef int8_t 				jboolean;

#ifdef __cplusplus
extern "C"{
#endif

void setByte(	const jlong address, const jbyte value);
void setChar(	const jlong address, const jchar value);
void setShort(	const jlong address, const jshort value);
void setInt(	const jlong address, const jint value);
void setFloat(	const jlong address, const jfloat value);
void setLong(	const jlong address, const jlong value);
void setDouble(	const jlong address, const jdouble value);
void setBoolean(const jlong address, const jboolean value);

jbyte 			getByte(	const jlong address);
jchar 			getChar(	const jlong address);
jshort 			getShort(	const jlong address);
jint 			getInt(		const jlong address);
jfloat 			getFloat(	const jlong address);
jlong 			getLong(	const jlong address);
jdouble 		getDouble(	const jlong address);
jboolean 		getBoolean(	const jlong address);

jlong 	allocateMemory(		const jlong size);
void 	freeMemory(			const jlong address);
jlong 	reallocateMemory(	const jlong oldAddress, const jlong newsize);
void 	setMemory(			const jlong address, 	const jint value, 		const jlong size);
jlong 	callocateMemory(	const jlong count, 		const jint typeSize);
void 	copyMemory(			const jlong srcAddress, const jlong dstAddress, const jlong len);

#ifdef __cplusplus
}
#endif

#include "memory_accessor.c"
