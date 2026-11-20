package io.coreroot.manager.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import io.coreroot.manager.R
import io.coreroot.manager.data.AppGrant
import io.coreroot.manager.data.GrantStatus

class AppGrantAdapter(
    private var items: List<AppGrant>,
    private val onPick: (AppGrant, GrantStatus) -> Unit
) : RecyclerView.Adapter<AppGrantAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val card: View = view.findViewById(R.id.app_card)
        val label: TextView = view.findViewById(R.id.app_label)
        val pkg: TextView = view.findViewById(R.id.app_package)
        val status: TextView = view.findViewById(R.id.app_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val app = items[position]
        holder.label.text = app.label
        holder.pkg.text = app.packageName
        holder.status.text = app.status.label

        val colorRes = when (app.status) {
            GrantStatus.GRANT -> R.color.state_grant
            GrantStatus.DENY -> R.color.state_deny
            GrantStatus.ASK -> R.color.state_ask
        }
        holder.status.backgroundTintList =
            ContextCompat.getColorStateList(holder.itemView.context, colorRes)

        holder.card.setOnClickListener { showPicker(holder.itemView.context, app) }
    }

    override fun getItemCount(): Int = items.size

    fun submit(list: List<AppGrant>) {
        items = list
        notifyDataSetChanged()
    }

    private fun showPicker(context: android.content.Context, app: AppGrant) {
        val options = GrantStatus.entries.toTypedArray()
        val labels = options.map { it.label }.toTypedArray()
        AlertDialog.Builder(context)
            .setTitle(R.string.grant_status)
            .setSingleChoiceItems(labels, options.indexOf(app.status)) { dialog, which ->
                onPick(app, options[which])
                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }
}
