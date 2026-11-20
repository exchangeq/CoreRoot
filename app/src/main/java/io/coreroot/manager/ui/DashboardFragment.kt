package io.coreroot.manager.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import io.coreroot.manager.R
import io.coreroot.manager.core.DeviceCollector
import io.coreroot.manager.data.CoreRepository
import io.coreroot.manager.data.GrantStatus

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repo = CoreRepository(requireContext())
        val info = DeviceCollector.collect()
        val grants = repo.loadGrants()
        val modules = repo.loadModules()
        val summary = repo.grantSummary(grants)

        view.findViewById<TextView>(R.id.root_status_text).text =
            getString(if (info.rootInstalled) R.string.root_status_installed else R.string.root_status_missing)

        bindPill(view.findViewById(R.id.stat_allowed), summary[GrantStatus.GRANT] ?: 0, getString(R.string.summary_allowed))
        bindPill(view.findViewById(R.id.stat_denied), summary[GrantStatus.DENY] ?: 0, getString(R.string.summary_denied))
        bindPill(view.findViewById(R.id.stat_ask), summary[GrantStatus.ASK] ?: 0, getString(R.string.summary_ask))
        bindPill(view.findViewById(R.id.stat_modules), modules.count { it.enabled }, getString(R.string.summary_modules))

        bindTile(view.findViewById(R.id.info_arch), getString(R.string.card_architecture), info.abi)
        bindTile(view.findViewById(R.id.info_kernel), getString(R.string.card_kernel), info.kernel)
        bindTile(view.findViewById(R.id.info_system), getString(R.string.card_system), "Android ${info.androidRelease} (SDK ${info.sdkInt})")
        bindTile(view.findViewById(R.id.info_brand), getString(R.string.card_brand), "${info.brand} · ${info.model}")
        bindTile(view.findViewById(R.id.info_hardware), getString(R.string.card_hardware), info.hardware)
        bindTile(view.findViewById(R.id.info_native), getString(R.string.card_native), info.nativeVersion)
    }

    private fun bindPill(pill: View, value: Int, label: String) {
        pill.findViewById<TextView>(R.id.pill_value).text = value.toString()
        pill.findViewById<TextView>(R.id.pill_label).text = label
    }

    private fun bindTile(tile: View, label: String, value: String) {
        tile.findViewById<TextView>(R.id.tile_label).text = label
        tile.findViewById<TextView>(R.id.tile_value).text = value
    }
}
