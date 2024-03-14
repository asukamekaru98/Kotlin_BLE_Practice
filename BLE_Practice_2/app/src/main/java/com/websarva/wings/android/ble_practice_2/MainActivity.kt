package com.websarva.wings.android.ble_practice_2

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
	private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
	private val bluetoothLeScanner: BluetoothLeScanner? = bluetoothAdapter?.bluetoothLeScanner

	private var scanning = false
	private val handler = Handler()

	// BLEスキャンの時間制限(10秒)
	private val SCAN_PERIOD: Long = 1000


	private var addList = ArrayList<BTdata>()       //まずは空のリストを用意
	private lateinit var recyclerView:RecyclerView

	private var recyclerAdapter = RecyclerAdapter(addList)

	//private val names: ArrayList<String> = arrayListOf(
	//	"Bellflower", "Bougainvillea", "Cosmos", "Cosmos field",
	//	"Delphinium", "Flowers", "Lotus", "Spring Flowers"
	//)
	//private var names: ArrayList<String> = arrayListOf()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContentView(R.layout.activity_main)
		setSupportActionBar(findViewById(R.id.toolbar)) //アクションバー

		/** 権限確認 **/
		checkPermission()

		recyclerView = findViewById(R.id.my_recycler_view)              //使うRecyclerViewのid取得
		recyclerView.adapter = recyclerAdapter                          //アダプタ接続
		recyclerView.layoutManager = LinearLayoutManager(this)   //各アイテムを縦に並べる指示


		recyclerView.setHasFixedSize(true)

		//ボタン押下
		findViewById<Button>(R.id.scan_button).setOnClickListener {
			scanLeDevice()
		}

	}

	//パーミッションランチャー
	private val launcher =
		registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
			val fineLocation = it[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
			val coarseLocation = it[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
			val blueConnect = it[Manifest.permission.BLUETOOTH_CONNECT] ?: false
			val blueScanner = it[Manifest.permission.BLUETOOTH_SCAN] ?: false

			if (fineLocation && coarseLocation && blueConnect && blueScanner) {
				Toast.makeText(this, "許可されました", Toast.LENGTH_SHORT)
					.show()
			} else {
				Toast.makeText(this, "否認されました", Toast.LENGTH_SHORT)
					.show()
			}
		}



	fun checkPermission() {
		launcher.launch(
			arrayOf(
				Manifest.permission.ACCESS_FINE_LOCATION,
				Manifest.permission.ACCESS_COARSE_LOCATION,
				Manifest.permission.BLUETOOTH_CONNECT,
				Manifest.permission.BLUETOOTH_SCAN,
			)
		)
	}

	// BLEデバイスをスキャンする
	private fun scanLeDevice() {

		bluetoothLeScanner?.let { scanner ->
			if (!scanning) {
				// スキャン中でない場合、一定のスキャン期間後にスキャンを停止します。
				handler.postDelayed({
					scanning = false


					if (ActivityCompat.checkSelfPermission(
							this,
							Manifest.permission.BLUETOOTH_SCAN
						) != PackageManager.PERMISSION_GRANTED
					) {
						return@postDelayed
					}
					scanner.stopScan(leScanCallback)

				}, SCAN_PERIOD  /*10秒*/)

				//BLEスキャン開始
				scanning = true
				scanner.startScan(leScanCallback)

			} else {

				//BLEスキャン開始
				scanning = false
				scanner.stopScan(leScanCallback)
			}
		}


	}

	// BLEデバイスのスキャン結果を処理するためのScanCallbackを定義
	private val leScanCallback: ScanCallback = object : ScanCallback() {

		// スキャン結果が見つかったときに呼び出される
		override fun onScanResult(callbackType: Int, result: ScanResult) {
			super.onScanResult(callbackType, result)

			//パーミッションが許可されていなければ終わらせる
			if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
				return
			}

			result.device?.let { device ->
				//names.add(device.name?:"No Name")
				//val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names)

				//adapter.notifyDataSetChanged()

				Log.d("TAG", device.name ?: "No Name")
				Log.d("TAG", device.address ?: "No Address")
				Log.d("TAG", device.bondState.toString())
				Log.d("TAG", device.type.toString())

				val data = BTdata(device.address ?: "No Name")
				addList.add(data)   //リストに追加
				recyclerAdapter.notifyItemInserted(addList.lastIndex)   //追加した情報がRecyclerViewの末尾に追加される


			}

			//Log.d("TAG","aaaaa")
			// スキャン結果からBluetoothデバイスを取得し、リストに追加
			//leDeviceListAdapter.addDevice(result.device)

			// デバイスリストの変更を通知し、表示を更新
			//leDeviceListAdapter.notifyDataSetChanged()
		}

	}

}