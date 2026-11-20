package io.coreroot.manager.core

import android.os.Build
import io.coreroot.manager.nat.NativeBridge

/** 采集设备信息：Java/Build 信息 + NDK C 层探测结果 */
object DeviceCollector {

    fun collect(): DeviceInfo = DeviceInfo(
        rootInstalled = runCatching { NativeBridge.nativeSuInstalled() }.getOrDefault(false),
        abi = runCatching { NativeBridge.nativeAbi() }.getOrDefault(Build.SUPPORTED_ABIS.firstOrNull() ?: "unknown"),
        kernel = runCatching { NativeBridge.nativeKernel() }.getOrDefault("unknown"),
        sdkInt = Build.VERSION.SDK_INT,
        androidRelease = Build.VERSION.RELEASE ?: "unknown",
        model = Build.MODEL ?: "unknown",
        brand = Build.BRAND ?: "unknown",
        hardware = Build.HARDWARE ?: "unknown",
        nativeVersion = runCatching { NativeBridge.nativeVersion() }.getOrDefault("unknown")
    )
}
