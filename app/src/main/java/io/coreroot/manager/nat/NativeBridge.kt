package io.coreroot.manager.nat

/**
 * 原生桥接：Kotlin <-> NDK C 层。
 * 所有原生调用均在 C 侧做了异常兜底，避免原生崩溃穿透到 UI。
 */
object NativeBridge {

    init {
        System.loadLibrary("coreroot")
    }

    /** 原生库版本串 */
    external fun nativeVersion(): String

    /** 当前运行的 ABI */
    external fun nativeAbi(): String

    /** 内核版本 (uname -r) */
    external fun nativeKernel(): String

    /** 是否在常见路径探测到 su 可执行文件 */
    external fun nativeSuInstalled(): Boolean

    /**
     * 执行一条受控命令，返回形如 `rc=<code>\n<output>` 的文本。
     */
    external fun nativeExec(command: String): String

    /** 解析 nativeExec 返回码 */
    fun parseExit(result: String): Int {
        val line = result.lineSequence().firstOrNull() ?: "rc=-1"
        return line.removePrefix("rc=").trim().toIntOrNull() ?: -1
    }

    /** 解析 nativeExec 输出正文 */
    fun parseOutput(result: String): String {
        val lines = result.lineSequence().drop(1).toList()
        return lines.joinToString("\n").trim()
    }
}
