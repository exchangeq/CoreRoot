package io.coreroot.manager.data

/** 一条应用授权记录 */
data class AppGrant(
    val packageName: String,
    val label: String,
    val uid: Int,
    var status: GrantStatus
)
