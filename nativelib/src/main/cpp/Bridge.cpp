#include <jni.h>
#include "Emulator.h"
#include "Bridge.h"
#include <jni.h>
#include <jni.h>
#include <android/log.h>
#include <stdlib.h>
#define TAG "liujunjie"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__);

extern "C" {
using namespace emudroid;

Emulator *emu;

#ifndef BRIDGE_PACKAGE
#define BRIDGE_PACKAGE :-)
#endif

Bridge::Bridge(Emulator *emulator) {
    emu = emulator;
}
/**
 * @param gfx 好像是0
 * @param sfx 204  表示聲音編碼格式  是否立體聲音 播放速度
 * @param general  配置模拟器模式
 *  int x = zapperEnabled ? 1 : 0;  是否支持光枪
        x += historyEnabled ? 10 : 0; 启用历史记录
        x += loadSavFiles ? 100 : 0;  加载保存文件
        x += saveSavFiles ? 1000 : 0; 保存存档文件
        x += quality * 10000;  画面质量
 * */
JNIEXPORT jboolean JNICALL
BRIDGE_PACKAGE(start)(JNIEnv *env, jobject obj, jint gfx, jint sfx,
                      jint general) {
    system("rm /data/data/com.ritchie.myapplicationmove/romAddr");
    return (jboolean) emu->start(gfx, sfx, general);
}


JNIEXPORT jboolean JNICALL
BRIDGE_PACKAGE(readPalette)(JNIEnv *env, jobject obj, jintArray result) {
    return (jboolean) emu->readPalette(env, result);
}

/**
 * @param path   "/mnt/sdcard/Download/rom/tanke.nes"
 * @param batteryPath  "/mnt/sdcard/Download/rom"
 * @param batteryFullPath "/mnt/sdcard/Download/rom/tanke.sav" 这个是游戏的存档文件 名称
 * */
JNIEXPORT jboolean JNICALL
BRIDGE_PACKAGE(loadGame)(JNIEnv *env, jobject obj, jstring path,
                         jstring batteryPath, jstring batteryFullPath) {
    jboolean isCopy;
    jboolean isCopy2;
    jboolean isCopy3;
    const char *fname = env->GetStringUTFChars(path, &isCopy);
    const char *fbattery = env->GetStringUTFChars(batteryPath, &isCopy2);
    const char *fbatteryFullPath = env->GetStringUTFChars(batteryFullPath, &isCopy3);
    bool success = emu->loadGame(fname, fbattery, fbatteryFullPath);
    env->ReleaseStringUTFChars(path, fname);
    env->ReleaseStringUTFChars(batteryPath, fbattery);
    env->ReleaseStringUTFChars(batteryFullPath, fbatteryFullPath);
    return success;
}
/**
 * @param path /storage/emulated/0/Android/data/nostalgia.appnes/files
 * */
JNIEXPORT jboolean JNICALL
BRIDGE_PACKAGE(setBaseDir)(JNIEnv *env, jobject obj, jstring path) {
    jboolean isCopy;

    const char *fname = env->GetStringUTFChars(path, &isCopy);
    bool success = emu->setBaseDir(fname);
    env->ReleaseStringUTFChars(path, fname);
    return success;
}

JNIEXPORT jboolean JNICALL
BRIDGE_PACKAGE(enableCheat)(JNIEnv *env, jobject obj, jstring gg,
                            jint type) {
    jboolean isCopy;
    const char *cheat = env->GetStringUTFChars(gg, &isCopy);
    bool success = emu->enableCheat(cheat, type);
    env->ReleaseStringUTFChars(gg, cheat);
    return success;
}


JNIEXPORT jboolean JNICALL
BRIDGE_PACKAGE(enableRawCheat)(JNIEnv *env, jobject obj, jint addr, jint val, jint comp) {
    jboolean isCopy;
    bool success = emu->enableRawCheat(addr, val, comp);
    return success;
}


/**
 * @param keys 0
 * @param turbos -1
 * @param numFramesToSkip 0 字面意思就是需要跳过的帧率
 * */
JNIEXPORT jboolean JNICALL
BRIDGE_PACKAGE(emulate)(JNIEnv *env, jobject obj, jint keys,
                        jint turbos, jint numFramesToSkip) {
    int res = emu->emulateFrame(keys, turbos, numFramesToSkip);
    //LOGD("emulate--------------keys:%d --- turbos:%d-----numFramesToSkip:%d",keys,turbos,numFramesToSkip);
    return res;
}

JNIEXPORT jboolean
JNICALL BRIDGE_PACKAGE(render)(JNIEnv *env, jobject obj, jobject bitmap) {
    //LOGD("render----------------");
    return emu->render(env, bitmap, -1, -1, NULL);
}


JNIEXPORT jboolean JNICALL
BRIDGE_PACKAGE(renderVP)(JNIEnv *env, jobject obj, jobject bitmap, int w, int h) {
    return emu->render(env, bitmap, w, h, NULL);
}


JNIEXPORT jboolean JNICALL
BRIDGE_PACKAGE(renderGL)(JNIEnv *env, jobject obj) {
    return emu->renderGL();
}


JNIEXPORT jint JNICALL
BRIDGE_PACKAGE(getHistoryItemCount)(JNIEnv *env, jobject obj) {
    return emu->getHistoryItemCount();
}

JNIEXPORT jboolean JNICALL
BRIDGE_PACKAGE(loadHistoryState)(JNIEnv *env, jobject obj, int pos) {
    return emu->loadHistoryState(pos);
}

JNIEXPORT jboolean JNICALL
BRIDGE_PACKAGE(renderHistory)(JNIEnv *env, jobject obj, jobject bmp, int pos, int w, int h) {
    return emu->renderHistory(env, bmp, pos, w, h);
}

JNIEXPORT jboolean JNICALL
BRIDGE_PACKAGE(setViewPortSize)(JNIEnv *env, jobject obj, jint w, jint h) {
    return emu->setViewPortSize(w, h);
}

JNIEXPORT jboolean JNICALL
BRIDGE_PACKAGE(fireZapper)(JNIEnv *env, jobject obj, jint x, jint y) {
    return emu->fireZapper(x, y);
}

/**
 * 硬件加速图像接口
 * 返回值是这个这个数组的长度
 * data   short[]数组
 * */
JNIEXPORT jint JNICALL
BRIDGE_PACKAGE(readSfxBuffer)(JNIEnv *env, jobject obj, jshortArray data) {
    return emu->readSfxBuffer(env, obj, data);
}

JNIEXPORT jboolean JNICALL
BRIDGE_PACKAGE(loadState)(JNIEnv *env, jobject obj, jstring path, int slot) {
    jboolean isCopy;
    const char *fname = env->GetStringUTFChars(path, &isCopy);
    bool success = emu->loadState(fname, slot);
    env->ReleaseStringUTFChars(path, fname);
    return success;
}

JNIEXPORT jboolean JNICALL
BRIDGE_PACKAGE(saveState)(JNIEnv *env, jobject obj, jstring path, int slot) {
    jboolean isCopy;
    const char *fname = env->GetStringUTFChars(path, &isCopy);
    bool success = emu->saveState(fname, slot);
    env->ReleaseStringUTFChars(path, fname);
    return success;
}

JNIEXPORT jboolean JNICALL
BRIDGE_PACKAGE(reset)(JNIEnv *env, jobject obj) {
    return emu->reset();
}

JNIEXPORT jboolean JNICALL
BRIDGE_PACKAGE(stop)(JNIEnv *env, jobject obj) {
    return emu->stop();
}

}
