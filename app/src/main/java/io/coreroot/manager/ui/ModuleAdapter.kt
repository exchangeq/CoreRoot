package io.coreroot.manager.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import io.coreroot.manager.R
import io.coreroot.manager.data.Module

class ModuleAdapter(
    private var items: List<Module>,
    private val onToggle: (Module, Boolean) -> Unit
) : RecyclerView.Adapter<ModuleAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.module_icon)
        val name: TextView = view.findViewById(R.id.module_name)
        val version: TextView = view.findViewById(R.id.module_version)
        val desc: TextView = view.findViewById(R.id.module_desc)
        val switch: SwitchCompat = view.findViewById(R.id.module_switch)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_module, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val module = items[position]
        holder.name.text = module.name
        holder.version.text = "v${module.version}"
        holder.desc.text = module.description

        holder.switch.setOnCheckedChangeListener(null)
        holder.switch.isChecked = module.enabled
        holder.switch.setOnCheckedChangeListener { _, checked ->
            onToggle(module, checked)
        }
    }

    override fun getItemCount(): Int = items.size

    fun submit(list: List<Module>) {
        items = list
        notifyDataSetChanged()
    }
}
