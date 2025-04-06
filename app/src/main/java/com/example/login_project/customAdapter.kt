package com.example.login_project

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class customAdapter(private val context:Activity, private val list:MutableList<holdInfo>):RecyclerView.Adapter<customAdapter.ViewHolder2>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder2 {
        val view =LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false)
        return ViewHolder2(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder2, position: Int) {
        val ivm=list[position]
        holder.reelno.text= ivm.reelno
        holder.barcode.text=ivm.barcode
        holder.delete.setImageResource(R.drawable.baseline_cancel_24)
        holder.delete.setOnClickListener {
            deleteItem(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    private fun deleteItem(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, list.size)
    }
    fun getData(): MutableList<holdInfo> {
        return list
    }
    class ViewHolder2(ItemView: View):RecyclerView.ViewHolder(ItemView){
        val reelno: TextView = itemView.findViewById(R.id.tvReelNo)
        val barcode: TextView = itemView.findViewById(R.id.tvBarcode)
        val delete:ImageButton=itemView.findViewById(R.id.tvDelete)
    }
}
