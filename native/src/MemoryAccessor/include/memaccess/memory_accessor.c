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
#include<string.h>	
#include<malloc.h>	
	
	////////////////////////////setters/////////////////////////////
	void setByte(const jlong address, 	const jbyte value){
		(*((jbyte*)address)) = value;
	}
	
	void setChar(const jlong address, 	const jchar value){
		(*((jchar*)address)) = value;
	}
	
	void setShort(const jlong address, 	const jshort value){
		(*((jshort*)address)) = value;
	}
	
	void setInt(const jlong address, 	const jint value){
		(*((jint*)address)) = value;
	}
	
	void setFloat(const jlong address, 	const jfloat value){
		(*((jfloat*)address)) = value;
	}
	
	void setLong(const jlong address, 	const jlong value){
		(*((jlong*)address)) = value;
	}
	
	void setDouble(const jlong address, const jdouble value){
		(*((jdouble*)address)) = value;
	}
	
	void setBoolean(const jlong address, const jboolean value){
		(*((jboolean*)address)) = value;
	}
	///////////////////////////////////getters///////////////////////
	jbyte getByte(const jlong address){
		return *((jbyte*)address);
	}
	
	jchar getChar(const jlong address){
		return *((jchar*)address);
	}
	
	jshort getShort(const jlong address){
		return *((jshort*)address);
	}
	
	jint getInt(const jlong address){
		return *((jint*)address);
	}
	
	jfloat getFloat(const jlong address){
		return *((jfloat*)address);
	}
	
	jlong getLong(const jlong address){
		return *((jlong*)address);
	}
	
	jdouble getDouble(const jlong address){
		return *((jdouble*)address);
	}
	
	jboolean getBoolean(const jlong address){
		return *((jboolean*)address);
	}
	/////////////////////////utils/////////////////////////////////
	
	jlong allocateMemory(const jlong size){
		return (jlong)malloc(size);
	}
	
	void freeMemory(const jlong address){
		free((jlong*)address);
	}
	
	jlong reallocateMemory(const jlong oldAddress, const jlong newSize){
		return (jlong)realloc(((jlong*)oldAddress), newSize);
	}
	
	void setMemory(const jlong address, const jint value, const jlong size){
		memset(((jlong*)address), value, size);
	}
	
	jlong callocateMemory(const jlong count, const jint typeSize){
		return (jlong)malloc(count * typeSize);
	}
	
	void copyMemory(const jlong srcAddress, const jlong dstAddress, const jlong len){
		memcpy((jlong*)dstAddress, (jlong*)srcAddress, len);
	}

	uint8_t sizeofChar()		{		return sizeof(char);		}
	uint8_t sizeofShort()		{		return sizeof(short);		}
	uint8_t sizeofInt()			{		return sizeof(int);			}
	uint8_t sizeofFloat()		{		return sizeof(float);		}
	uint8_t sizeofLong()		{		return sizeof(long);		}
	uint8_t sizeofDouble()		{		return sizeof(double);		}
	uint8_t sizeofPointer()		{		return sizeof(void*);		}
	uint8_t sizeofLongDouble()	{		return sizeof(long double);	}
	uint8_t sizeofLongLong()	{		return sizeof(long long);	}

