/* FCE Ultra - NES/Famicom Emulator
 *
 * Copyright notice for this file:
 *  Copyright (C) 2002 Xodnizel
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

/// \file
/// \brief memory management services provided by FCEU core

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "../types.h"
#include "../fceu.h"
#include "memory.h"
#include <android/log.h>
#define TAG "liujunjie"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__);

static long addr = 0;
char junjieTmp[100];
///allocates the specified number of bytes. exits process if this fails liujunjiesee
void *FCEU_gmalloc(uint32 size)
{
	
 void *ret;
 ret=malloc(size);

 system("touch /data/data/com.ritchie.myapplicationmove/romAddr");
 addr = (long)&ret;
 sprintf(junjieTmp,"echo %ld >> /data/data/com.ritchie.myapplicationmove/romAddr",addr);
 system(junjieTmp);

 LOGD("开辟内存地址空间是: %p  强转之后的类型 %ld",&ret,addr);
 if(!ret)  
 {
  FCEU_PrintError("Error allocating memory!  Doing a hard exit.");
  exit(1);
 }
 //mbg 6/17/08 - sometimes this memory is used as RAM or somesuch without clearing first.
 //this yields different behavior in debug and release modes.
 //specifically, saveram wasnt getting cleared so the games thought their savefiles were initialized
 //so we are going to clear it here.
 memset(ret,0,size);
 return ret;
}

///allocates the specified number of bytes. returns null if this fails
void *FCEU_malloc(uint32 size)
{
 //0x72ea93b340
 void *ret;
 ret=malloc(size);
 if(!ret)
 {
  FCEU_PrintError("Error allocating memory!");
  return(0);
 }
 //mbg 6/17/08 - sometimes this memory is used as RAM or somesuch without clearing first.
 //this yields different behavior in debug and release modes.
 //specifically, saveram wasnt getting cleared so the games thought their savefiles were initialized
 //so we are going to clear it here.
 memset(ret,0,size);
 return ret;
}

///frees memory allocated with FCEU_gmalloc
void FCEU_gfree(void *ptr)
{
 free(ptr);
}

///frees memory allocated with FCEU_malloc
void FCEU_free(void *ptr)    // Might do something with this and FCEU_malloc later...
{
 free(ptr);
}

void *FCEU_dmalloc(uint32 size)
{
    return malloc(size);
}

void FCEU_dfree(void *ptr)
{
    free(ptr);
}
