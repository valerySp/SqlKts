package com.example.sqlnote.db

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sqlnote.EditActivity
import com.example.sqlnote.R

class MyAdapter(listMain:ArrayList<ListItem>,contextAdp: Context):RecyclerView.Adapter<MyAdapter.MyHolder>() {
    var listArray=listMain
    var context=contextAdp


    class MyHolder(itemView: View,contextV: Context) : RecyclerView.ViewHolder(itemView) {
        val tvTitle=itemView.findViewById<TextView>(R.id.tvTitle)
        val tvTime=itemView.findViewById<TextView>(R.id.tvTime)
        val context=contextV

        fun setData(item:ListItem){
            tvTitle.text=item.title
            tvTime.text=item.time
            itemView.setOnClickListener {
                val intent=Intent (context,EditActivity::class.java).apply {
                    putExtra(MyContentConst.I_TITLE_KEY,item.title)
                    putExtra(MyContentConst.I_DESC_KEY,item.desc)
                    putExtra(MyContentConst.I_URI_KEY,item.uri)
                    putExtra(MyContentConst.I_ID_KEY,item.id)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater=LayoutInflater.from(parent.context)
        return MyHolder(inflater.inflate(R.layout.rc_item,parent,false),context)
    }

    override fun getItemCount(): Int {
        return listArray.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.setData(listArray.get(position))
    }

    fun updateAdapter(listItems:List<ListItem>){
        listArray.clear()
        listArray.addAll(listItems)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int,dbManager: MyDdManager){

        dbManager.removeToDb(listArray.get(position).id.toString())
        listArray.removeAt(position)
        notifyItemChanged(0,listArray.size)
        notifyItemRemoved(position)
    }
}