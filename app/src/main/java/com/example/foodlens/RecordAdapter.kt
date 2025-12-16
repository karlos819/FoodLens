package com.example.foodlens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


data class FoodRecordUi(
    val name: String,
    val kcal: Int,
    val carb: Int,
    val protein: Int,
    val fat: Int
)


class RecordAdapter : RecyclerView.Adapter<RecordAdapter.VH>() {

    private val items = mutableListOf<FoodRecordUi>()

    fun submit(list: List<FoodRecordUi>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvFoodName)
        val kcal: TextView = itemView.findViewById(R.id.tvFoodKcal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food_record, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.name.text = item.name
        holder.kcal.text = "${item.kcal} calories"
    }

    override fun getItemCount(): Int = items.size
}
