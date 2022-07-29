package com.example.gurufin

import android.content.Context
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gurufin.dto.Target

class TargetAdapter(val context: Context): RecyclerView.Adapter<TargetAdapter.TargetViewHolder>() {
    private var list = mutableListOf<Target>()

    inner class TargetViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var title = itemView.findViewById<TextView>(R.id.targetTitle)
        var targetContents = itemView.findViewById<TextView>(R.id.targetContents)
        var checkbox = itemView.findViewById<CheckBox>(R.id.cbCheck)

        fun onBind(data: Target) {
            title.text=data.title
            targetContents.text=data.contents
            checkbox.isChecked=data.isChecked

            if (data.isChecked) {
                title.paintFlags=title.paintFlags or STRIKE_THRU_TEXT_FLAG
            } else {
                title.paintFlags = title.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
            }

            checkbox.setOnClickListener {
                itemCheckBoxClickListener.onClick(it, layoutPosition, list[layoutPosition].num)
            }

            itemView.setOnClickListener {
                itemClickListener.onClick(it, layoutPosition, list[layoutPosition].num)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TargetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler, parent, false)
        return TargetViewHolder(view)
    }

    override fun onBindViewHolder(holder: TargetViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun update(newList: MutableList<Target>) {
        this.list=newList
        notifyDataSetChanged()
    }

    interface ItemCheckBoxClickListener {
        fun onClick(view: View, position: Int, itemld: Int)
    }

    private lateinit var itemCheckBoxClickListener: ItemCheckBoxClickListener

    fun setItemCheckBoxClickListener(itemCheckBoxClickListener: ItemCheckBoxClickListener) {
        this.itemCheckBoxClickListener=itemCheckBoxClickListener
    }

    interface ItemClickListener {
        fun onClick(view: View, position: Int, itemld: Int)
    }

    private lateinit var itemClickListener: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener=itemClickListener
    }
}