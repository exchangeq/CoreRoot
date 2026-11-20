package io.coreroot.manager.core

/** 汇总后的设备与 Root 状态 */
data class DeviceInfo(
    val rootInstalled: Boolean,
    val abi: String,
    val kernel: String,
    val sdkInt: Int,
    val androidRelease: String,
    val model: String,
    val brand: String,
    val hardware: String,
    val nativeVersion: String
)
