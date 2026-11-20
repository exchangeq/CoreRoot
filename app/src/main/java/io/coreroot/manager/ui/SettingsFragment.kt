package io.coreroot.manager.ui

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import io.coreroot.manager.BuildConfig
import io.coreroot.manager.R
import io.coreroot.manager.nat.NativeBridge

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var prefs: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = requireContext().getSharedPreferences("coreroot_prefs", 0)

        bindRow(view.findViewById(R.id.row_version), getString(R.string.item_version), BuildConfig.VERSION_NAME)
        bindRow(view.findViewById(R.id.row_signature), getString(R.string.item_signature), "v1 + v2")
        bindRow(view.findViewById(R.id.row_native), getString(R.string.item_native), runCatching { NativeBridge.nativeVersion() }.getOrDefault("unknown"))

        bindSwitch(view.findViewById(R.id.pref_theme), getString(R.string.pref_theme), KEY_THEME, true)
        bindSwitch(view.findViewById(R.id.pref_confirm), getString(R.string.pref_confirm), KEY_CONFIRM, true)
        bindSwitch(view.findViewById(R.id.pref_log), getString(R.string.pref_log), KEY_LOG, false)

        val openSource = view.findViewById<View>(R.id.item_open_source)
        openSource.findViewById<TextView>(R.id.link_title).text = getString(R.string.item_open_source)
        openSource.findViewById<TextView>(R.id.link_subtitle).text = "GPL-3.0 · 自由软件"
        openSource.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.license_title)
                .setMessage(R.string.license_body)
                .setPositiveButton(android.R.string.ok, null)
                .show()
        }

        val design = view.findViewById<View>(R.id.item_design)
        design.findViewById<TextView>(R.id.link_title).text = getString(R.string.item_design)
        design.findViewById<TextView>(R.id.link_subtitle).text = "标志与配色理念"
        design.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.item_design)
                .setMessage(R.string.design_body)
                .setPositiveButton(android.R.string.ok, null)
                .show()
        }
    }

    private fun bindRow(container: View, label: String, value: String) {
        container.findViewById<TextView>(R.id.row_label).text = label
        container.findViewById<TextView>(R.id.row_value).text = value
    }

    private fun bindSwitch(container: View, label: String, key: String, def: Boolean) {
        container.findViewById<TextView>(R.id.switch_label).text = label
        val switch = container.findViewById<SwitchCompat>(R.id.switch_control)
        switch.setOnCheckedChangeListener(null)
        switch.isChecked = prefs.getBoolean(key, def)
        switch.setOnCheckedChangeListener { _, checked ->
            prefs.edit().putBoolean(key, checked).apply()
        }
    }

    companion object {
        private const val KEY_THEME = "pref_theme"
        private const val KEY_CONFIRM = "pref_confirm"
        private const val KEY_LOG = "pref_log"
    }
}
