package io.coreroot.manager.data

/** 一条原创模块记录 */
data class Module(
    val id: String,
    val name: String,
    val version: String,
    val author: String,
    val description: String,
    var enabled: Boolean
)
