package io.coreroot.manager.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.coreroot.manager.R
import io.coreroot.manager.data.CoreRepository
import io.coreroot.manager.data.GrantStatus

class GrantsFragment : Fragment(R.layout.fragment_grants) {

    private lateinit var repo: CoreRepository
    private lateinit var adapter: AppGrantAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repo = CoreRepository(requireContext())

        val list = view.findViewById<RecyclerView>(R.id.grant_list)
        val empty = view.findViewById<TextView>(R.id.empty_grants)
        list.layoutManager = LinearLayoutManager(requireContext())

        adapter = AppGrantAdapter(emptyList()) { app, status ->
            repo.setGrant(app.packageName, status)
            refresh(empty)
        }
        list.adapter = adapter
        refresh(empty)
    }

    private fun refresh(empty: TextView) {
        val grants = repo.loadGrants()
        adapter.submit(grants)
        empty.visibility = if (grants.isEmpty()) View.VISIBLE else View.GONE
    }
}
