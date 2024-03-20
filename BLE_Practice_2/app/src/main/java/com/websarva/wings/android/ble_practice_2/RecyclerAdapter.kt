package com.websarva.wings.android.ble_practice_2

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView


/*impriment 埋め込む*/
/*クラス名(private val 変数名:ArrayList or MutableList<データクラス>)とする。ちなArrayListの方がシンプルに使える。 */
class RecyclerAdapter(private val list:ArrayList<BTdata>,private val activity: MainActivity):RecyclerView.Adapter<RecyclerAdapter.ViewHolderItem>() {


	//インナークラス
	inner class ViewHolderItem(v: View):RecyclerView.ViewHolder(v){

		//xmlから、指定のidを見つける処理
		val itemName: TextView = v.findViewById(R.id.text_view)

		//クリック処理(1行分の画面が押されたら～)
		//private val recyclerAdapter = RecyclerAdapter
		//private val viewList = RecyclerAdapter

		init {
			//アイテムがクリックされたときの処理
			v.setOnClickListener{
				val pos:Int = adapterPosition

				//val item = list[pos]
				//トースト
				Toast.makeText(v.context, list[pos].device.address ?: "No Address", Toast.LENGTH_SHORT).show()
				//activity.connectGATT(list[pos].device)
				//activity.sendFile(list[pos].device)
				activity.setUUID(list[pos].device)
			}
		}
	}



	//1行分のレイアウトを生成
	//ViewHolderのxmlとktを紐づける
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItem {
		//xmlレイアウトを取得（インフレート）

		//ここはもはや定型文、一言一句覚えなくて良い
		val itemXml = LayoutInflater.from(parent.context)
			.inflate(R.layout.my_text_view,parent,false)

		return ViewHolderItem(itemXml) //これで紐づけ完了
	}

	//position番目のデータをレイアウト(xml)に表示するようセット
	override fun onBindViewHolder(holder: ViewHolderItem, position: Int) {

		val currentItem = list[position]    //何番目のリストですか

		if (ActivityCompat.checkSelfPermission(
				activity,
				Manifest.permission.BLUETOOTH_CONNECT
			) != PackageManager.PERMISSION_GRANTED
		) {
			return
		}
		holder.itemName.text = currentItem.device.name   //そのリストの中の要素を指定して代入


	}


	//データが何件あるのかをカウントする
	override fun getItemCount(): Int {
		return list.size /** .sizeじゃなくて .count() でもできる。が、最初に作った人は.sizeを使っていたので、それにみんなが習っている状況 **/
	}

	/**こんな略し方もできる**/
//	override fun getItemCount(): Int = viewList.size /** .sizeじゃなくて .count() でもできる。が、最初に作った人は.sizeを使っていたので、それにみんなが習っている状況 **/
}