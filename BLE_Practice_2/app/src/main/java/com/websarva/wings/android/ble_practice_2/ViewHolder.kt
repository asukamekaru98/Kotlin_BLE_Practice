package com.websarva.wings.android.ble_practice_2

import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

/*
class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){

	//xmlから、指定のidを見つける処理
	val itemName: TextView = itemView.findViewById(R.id.text_view)

	//クリック処理(1行分の画面が押されたら～)
	private val recyclerAdapter = RecyclerAdapter()
	private val viewList = recyclerAdapter.viewList

	init {
		//アイテムがクリックされたときの処理
		itemView.setOnClickListener{
			val pos:Int = adapterPosition

			//トースト
			Toast.makeText(itemView.context, viewList[pos], Toast.LENGTH_SHORT).show()
		}
	}
}

*/