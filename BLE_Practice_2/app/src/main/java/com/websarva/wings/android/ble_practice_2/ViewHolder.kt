package com.websarva.wings.android.ble_practice_2

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
	val itemName: TextView = itemView.findViewById(R.id.text_view)

}