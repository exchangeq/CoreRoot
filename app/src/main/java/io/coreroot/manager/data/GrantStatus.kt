package io.coreroot.manager.data

/** 授权状态：允许 / 拒绝 / 询问 */
enum class GrantStatus(val label: String) {
    GRANT("允许"),
    DENY("拒绝"),
    ASK("询问");

    companion object {
        fun fromName(name: String?): GrantStatus =
            entries.firstOrNull { it.name == name } ?: ASK
    }
}
