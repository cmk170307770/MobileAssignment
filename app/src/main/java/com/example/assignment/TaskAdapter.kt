package com.example.assignment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class TaskAdapter (context: Context, taskList: MutableList<Task>) : BaseAdapter(){
    private val _inflater: LayoutInflater = LayoutInflater.from(context);
    private val _tastList = taskList;

    /*
    * Get the list view
    * */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val objectId: String = _tastList.get(position).objectId as String;
        val createDate: String = _tastList.get(position).createDate as String;
        val result: String = _tastList.get(position).number as String;

        val view: View;
        val listRowHolder: ListRowHolder;

        if(convertView == null){
            view = _inflater.inflate(R.layout.log_row, parent, false);
            listRowHolder = ListRowHolder(view);
            view.tag = listRowHolder
        }else{
            view = convertView;
            listRowHolder = view.tag as ListRowHolder;
        }

        listRowHolder.createDate.text =createDate;
        listRowHolder.result.text = result;

        return view
    }

    /*
    * Get the list view count
    * */
    override fun getCount(): Int {
        return _tastList.size;
    }

    /*
    * Get the item
    * */
    override fun getItem(position: Int): Any {
        return _tastList.get(position);
    }

    /*
    * Get item id
    * */
    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    /*
    * List Row Model
    * */
    private class ListRowHolder(row: View?){
        val createDate: TextView = row!!.findViewById(R.id.createDate);
        val result: TextView = row!!.findViewById(R.id.resultTestView);

    }
}