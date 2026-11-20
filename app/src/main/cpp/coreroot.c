/*
 * CoreRoot 原生桥接层 (C / NDK)
 * 原创实现：负责设备信息读取、su 探测与受控命令执行。
 * 设计目标：低开销、强兜底、不依赖任何已知厂商的私有二进制或守护进程。
 */

#include <jni.h>
#include <android/log.h>
#include <unistd.h>
#include <sys/utsname.h>
#include <sys/stat.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <limits.h>

#define LOG_TAG "CoreRoot-Native"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

#define OUTPUT_CAP 16384

static const char *SU_CANDIDATES[] = {
    "/system/bin/su",
    "/system/xbin/su",
    "/sbin/su",
    "/system/sd/xbin/su",
    "/data/local/bin/su",
    NULL
};

/* 返回 1 表示在常见路径探测到 su 可执行文件；未探测到返回 0。 */
static jint detect_su(void) {
    for (int i = 0; SU_CANDIDATES[i] != NULL; ++i) {
        struct stat st;
        if (stat(SU_CANDIDATES[i], &st) == 0 &&
            (st.st_mode & S_IXUSR)) {
            return 1;
        }
    }
    return 0;
}

JNIEXPORT jstring JNICALL
Java_io_coreroot_manager_nat_NativeBridge_nativeVersion(JNIEnv *env, jobject thiz) {
    return (*env)->NewStringUTF(env, "1.0.0-native");
}

JNIEXPORT jstring JNICALL
Java_io_coreroot_manager_nat_NativeBridge_nativeAbi(JNIEnv *env, jobject thiz) {
#ifdef __aarch64__
    return (*env)->NewStringUTF(env, "arm64-v8a");
#elif defined(__arm__)
    return (*env)->NewStringUTF(env, "armeabi-v7a");
#elif defined(__x86_64__)
    return (*env)->NewStringUTF(env, "x86_64");
#else
    return (*env)->NewStringUTF(env, "unknown");
#endif
}

JNIEXPORT jstring JNICALL
Java_io_coreroot_manager_nat_NativeBridge_nativeKernel(JNIEnv *env, jobject thiz) {
    struct utsname u;
    if (uname(&u) == 0) {
        return (*env)->NewStringUTF(env, u.release);
    }
    return (*env)->NewStringUTF(env, "unknown");
}

JNIEXPORT jboolean JNICALL
Java_io_coreroot_manager_nat_NativeBridge_nativeSuInstalled(JNIEnv *env, jobject thiz) {
    return detect_su() ? JNI_TRUE : JNI_FALSE;
}

/*
 * 执行一条受控 shell 命令，返回文本结果：
 *   rc=<退出码>\n<标准输出/错误>
 * 任何异常都被兜住，绝不向 Java 层抛出原生崩溃。
 */
JNIEXPORT jstring JNICALL
Java_io_coreroot_manager_nat_NativeBridge_nativeExec(JNIEnv *env, jobject thiz, jstring command) {
    if (command == NULL) {
        return (*env)->NewStringUTF(env, "rc=-1\nempty command");
    }

    const char *cmd = (*env)->GetStringUTFChars(env, command, NULL);
    if (cmd == NULL) {
        return (*env)->NewStringUTF(env, "rc=-1\ninvalid command");
    }

    char buffer[OUTPUT_CAP];
    buffer[0] = '\0';
    size_t used = 0;
    int rc = 127;

    FILE *pipe = popen(cmd, "r");
    if (pipe != NULL) {
        size_t n;
        while ((n = fread(buffer + used, 1, OUTPUT_CAP - used - 1, pipe)) > 0) {
            used += n;
            if (used >= OUTPUT_CAP - 1) break;
        }
        buffer[used] = '\0';
        rc = pclose(pipe);
        if (rc < 0) rc = 127;
    } else {
        snprintf(buffer, sizeof(buffer), "popen failed");
    }

    (*env)->ReleaseStringUTFChars(env, command, cmd);

    char result[OUTPUT_CAP + 64];
    snprintf(result, sizeof(result), "rc=%d\n%s", rc, buffer);
    return (*env)->NewStringUTF(env, result);
}
