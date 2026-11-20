package io.coreroot.manager.data

import android.content.Context
import org.json.JSONObject

/**
 * 本地数据仓库：授权记录与模块开关持久化到 SharedPreferences。
 * 不联网、不上传任何应用列表，全部数据仅存于本机。
 */
class CoreRepository(context: Context) {

    private val prefs = context.getSharedPreferences("coreroot_prefs", Context.MODE_PRIVATE)

    // ---- 预置演示数据（原创命名，不对应任何真实厂商应用）----
    private val presetApps = listOf(
        AppGrant("io.coreroot.demo.shell", "终端助手", 10201, GrantStatus.GRANT),
        AppGrant("io.coreroot.demo.backup", "备份工具", 10202, GrantStatus.ASK),
        AppGrant("io.coreroot.demo.tuner", "系统调校", 10203, GrantStatus.GRANT),
        AppGrant("io.coreroot.demo.monitor", "进程监控", 10204, GrantStatus.DENY)
    )

    private val presetModules = listOf(
        Module("thermal-guard", "温控守护", "2.1.0", "CoreRoot Team",
            "监控 SoC 温度并在过热时调度降频策略，保持机身冷静。", true),
        Module("net-scheduler", "网络调度", "1.4.2", "CoreRoot Team",
            "按应用优先级分配带宽，抑制后台抢占，改善前台延迟。", true),
        Module("hid-config", "外设配置", "1.0.7", "CoreRoot Team",
            "校准键鼠、手柄与触摸板的按键映射与上报速率。", false)
    )

    fun loadGrants(): List<AppGrant> {
        val saved = readGrantMap()
        return presetApps.map { app ->
            val status = saved[app.packageName]?.let { GrantStatus.fromName(it) } ?: app.status
            app.copy(status = status)
        }
    }

    fun setGrant(packageName: String, status: GrantStatus) {
        val obj = JSONObject()
        readGrantMap().forEach { (k, v) -> obj.put(k, v) }
        obj.put(packageName, status.name)
        prefs.edit().putString(KEY_GRANTS, obj.toString()).apply()
    }

    fun loadModules(): List<Module> {
        val saved = prefs.getString(KEY_MODULES, null)?.let { JSONObject(it) }
        return presetModules.map { m ->
            val enabled = saved?.optString(m.id)?.let { it == "1" } ?: m.enabled
            m.copy(enabled = enabled)
        }
    }

    fun setModuleEnabled(id: String, enabled: Boolean) {
        val obj = prefs.getString(KEY_MODULES, null)?.let { JSONObject(it) } ?: JSONObject()
        obj.put(id, if (enabled) "1" else "0")
        prefs.edit().putString(KEY_MODULES, obj.toString()).apply()
    }

    fun grantSummary(grants: List<AppGrant>): Map<GrantStatus, Int> =
        GrantStatus.entries.associateWith { status -> grants.count { it.status == status } }

    private fun readGrantMap(): Map<String, String> {
        val raw = prefs.getString(KEY_GRANTS, null) ?: return emptyMap()
        val obj = JSONObject(raw)
        return obj.keys().asSequence().associateWith { obj.getString(it) }
    }

    companion object {
        private const val KEY_GRANTS = "grant_map"
        private const val KEY_MODULES = "module_map"
    }
}
