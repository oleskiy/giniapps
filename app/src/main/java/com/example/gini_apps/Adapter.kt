package com.example.gini_apps

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class Adapter(val context: Context, var list: SortedSet<Int>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType==0)
             {
                var view = LayoutInflater.from(context).inflate(
                    R.layout.view_holder_small,
                    parent,
                    false
                )
                return ViewHolderSmall(view)
            }else
             {
                var view = LayoutInflater.from(context).inflate(
                        R.layout.view_holder_large,
                        parent,
                        false
                )
                return ViewHolderLarge(view)
            }
        }

    override fun getItemViewType(position: Int): Int {
            if(list.contains(list.elementAt(position)*-1)){
                return 1
            }else{
                return 0
            }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(list.contains(list.elementAt(position)*-1)){
            (holder as ViewHolderLarge).title.text=list.elementAt(position).toString()
        }else{
            (holder as ViewHolderSmall).title.text=list.elementAt(position).toString()
        }
    }

    override fun getItemCount(): Int {
       return list.size
    }

    fun updateList(newList: SortedSet<Int>){
        list.addAll(newList)

    }

    class ViewHolderSmall(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.title)
    }

    class ViewHolderLarge(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.title)
    }

}