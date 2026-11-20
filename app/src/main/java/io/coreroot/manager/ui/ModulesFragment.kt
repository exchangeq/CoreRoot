package io.coreroot.manager.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.coreroot.manager.R
import io.coreroot.manager.data.CoreRepository

class ModulesFragment : Fragment(R.layout.fragment_modules) {

    private lateinit var repo: CoreRepository
    private lateinit var adapter: ModuleAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repo = CoreRepository(requireContext())

        val list = view.findViewById<RecyclerView>(R.id.module_list)
        list.layoutManager = LinearLayoutManager(requireContext())

        adapter = ModuleAdapter(emptyList()) { module, enabled ->
            repo.setModuleEnabled(module.id, enabled)
        }
        list.adapter = adapter
        refresh()
    }

    private fun refresh() {
        adapter.submit(repo.loadModules())
    }
}
