package com.example.foodlens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(
    private val items: MutableList<ChatMessage>
) : RecyclerView.Adapter<ChatAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_message, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val msg = items[position]

        if (msg.isUser) {
            holder.tvUserMsg.visibility = View.VISIBLE
            holder.tvUserMsg.text = msg.text

            holder.tvAiMsg.visibility = View.GONE
            holder.tvAiMsg.text = ""
        } else {
            holder.tvAiMsg.visibility = View.VISIBLE
            holder.tvAiMsg.text = msg.text

            holder.tvUserMsg.visibility = View.GONE
            holder.tvUserMsg.text = ""
        }
    }

    override fun getItemCount(): Int = items.size

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUserMsg: TextView = itemView.findViewById(R.id.tvUserMsg)
        val tvAiMsg: TextView = itemView.findViewById(R.id.tvAiMsg)
    }
}
