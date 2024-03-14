package com.websarva.wings.android.ble_practice_2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/*impriment 埋め込む*/
class RecyclerAdapter:RecyclerView.Adapter<ViewHolder>() {

	//表示するリスト
	private val viewList = listOf(
		"ライオン","くま","たかし"
	)


	//1行分のレイアウトを生成
	//ViewHolderのxmlとktを紐づける
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		//xmlレイアウトを取得（インフレート）

		//ここはもはや定型文、一言一句覚えなくて良い
		val itemXml = LayoutInflater.from(parent.context)
			.inflate(R.layout.my_text_view,parent,false)

		return ViewHolder(itemXml) //これで紐づけ完了
	}

	//position番目のデータをレイアウト(xml)に表示するようセット
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.itemName.text = viewList[position]
	}


	//データが何件あるのかをカウントする
	override fun getItemCount(): Int {
		return viewList.size /** .sizeじゃなくて .count() でもできる。が、最初に作った人は.sizeを使っていたので、それにみんなが習っている状況 **/
	}

	/**こんな略し方もできる**/
//	override fun getItemCount(): Int = viewList.size /** .sizeじゃなくて .count() でもできる。が、最初に作った人は.sizeを使っていたので、それにみんなが習っている状況 **/
}