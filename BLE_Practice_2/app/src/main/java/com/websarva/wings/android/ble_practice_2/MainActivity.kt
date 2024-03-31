package com.websarva.wings.android.ble_practice_2

import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File


class MainActivity : AppCompatActivity() {

	//クラス
	private lateinit var recyclerView:RecyclerView
	private lateinit var permissionManager: PermissionManager
	//private lateinit var bluetoothManager: BluetoothManager
	private  lateinit var bleScanner: BleScanner
	private lateinit var bluetoothLeService: BluetoothLeService
	private lateinit var bluetoothFileTransfer:BluetoothFileTransfer
	lateinit var prefSetting: SharedPreferences

	private var addList = ArrayList<BTdata>()       //まずは空のリストを用意
	private var recyclerAdapter = RecyclerAdapter(addList,this)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)



		enableEdgeToEdge()
		setContentView(R.layout.activity_main)
		setSupportActionBar(findViewById(R.id.toolbar)) //アクションバー

		recyclerView = findViewById(R.id.my_recycler_view)              //使うRecyclerViewのid取得
		recyclerView.adapter = recyclerAdapter                          //アダプタ接続
		recyclerView.layoutManager = LinearLayoutManager(this)   //各アイテムを縦に並べる指示
		recyclerView.setHasFixedSize(true)

		// PermissionManagerのインスタンスを生成し、MainActivityのインスタンスを渡す
		permissionManager = PermissionManager(this)

		// BluetoothManagerのインスタンスを生成し、MainActivityのインスタンスを渡す
		//bluetoothManager = BluetoothManager(this)
		bleScanner = BleScanner(this)

		bluetoothLeService = BluetoothLeService(this)

		bluetoothFileTransfer = BluetoothFileTransfer(this)

		prefSetting = PreferenceManager.getDefaultSharedPreferences(this)

		//ボタン押下
		findViewById<Button>(R.id.scan_button).setOnClickListener {

			//パーミッションチェック
			permissionManager.checkPermission()

			//BTスキャン
			//bluetoothManager.scanLeDevice()
			bleScanner.scanLeDevice()

			//BT GATTサーバ接続
		//	bluetoothManager.connect2GATT()
		}

		//ボタン押下
		findViewById<Button>(R.id.send_button).setOnClickListener {

			//bluetoothManager.sendStringToDevice("aaaaaaaaaaaaaaaaaaaaa\n")

			//val bluetoothFileTransfer = BluetoothFileTransfer(context)

			//bluetoothManager.connectDevice()
		}

		//ボタン押下
		findViewById<Button>(R.id.advatise_button).setOnClickListener {

			//bluetoothManager.startAdvertisingAsHID()

			//bluetoothManager.disconnectDevice()

		}

	}

	// BluetoothManagerからスキャン結果を受け取る
	fun setAddData(addData: BTdata){
		addList.add(addData)

		//追加した情報がRecyclerViewの末尾に追加される
		recyclerAdapter.notifyItemInserted(addList.lastIndex)
	}

	//GATTサーバーに接続する
	fun connectGATT(device: BluetoothDevice){

		//bluetoothLeService.connect(device.address)
		//bluetoothManager.connect2GATT(device)
		//bluetoothManager.connectToDevice(device)
		//bluetoothManager.connectDevice(device)
		//bluetoothManager.setUUID(device)
	}

	fun sendFile(device: BluetoothDevice){

		val file = File("/storage/emulated/0/Download/20220323eturann1.pdf")

		bluetoothFileTransfer.sendFile(device, file)
	}

	fun transScanActivity(device: BluetoothDevice){
		//ScanActivityに遷移
        val intent = Intent(
            application,
            SendActivity::class.java
        )

        intent.putExtra(Constants.KEY_TRANS_SEND_ACTIVITY,device)

        startActivity(intent)
	}

}

