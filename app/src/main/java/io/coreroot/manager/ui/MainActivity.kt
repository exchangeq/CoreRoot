package io.coreroot.manager.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.coreroot.manager.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        if (savedInstanceState == null) {
            switchFragment(DashboardFragment(), TAG_HOME)
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> switchFragment(DashboardFragment(), TAG_HOME)
                R.id.nav_grants -> switchFragment(GrantsFragment(), TAG_GRANTS)
                R.id.nav_modules -> switchFragment(ModulesFragment(), TAG_MODULES)
                R.id.nav_console -> switchFragment(ConsoleFragment(), TAG_CONSOLE)
                R.id.nav_settings -> switchFragment(SettingsFragment(), TAG_SETTINGS)
            }
            true
        }
    }

    private fun switchFragment(fragment: Fragment, tag: String) {
        val current = supportFragmentManager.findFragmentById(R.id.nav_host)
        if (current != null && current.javaClass == fragment.javaClass) return
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host, fragment, tag)
            .commit()
    }

    companion object {
        private const val TAG_HOME = "home"
        private const val TAG_GRANTS = "grants"
        private const val TAG_MODULES = "modules"
        private const val TAG_CONSOLE = "console"
        private const val TAG_SETTINGS = "settings"
    }
}
