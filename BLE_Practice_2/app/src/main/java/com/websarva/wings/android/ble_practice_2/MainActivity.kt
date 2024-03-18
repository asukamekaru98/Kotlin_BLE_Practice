package com.websarva.wings.android.ble_practice_2

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
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

	//クラス
	private lateinit var recyclerView:RecyclerView
	private lateinit var permissionManager: PermissionManager
	private lateinit var bluetoothManager: BluetoothManager

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
		bluetoothManager = BluetoothManager(this)

		//ボタン押下
		findViewById<Button>(R.id.scan_button).setOnClickListener {

			//パーミッションチェック
			permissionManager.checkPermission()

			//BTスキャン
			bluetoothManager.scanLeDevice()

			//BT GATTサーバ接続
		//	bluetoothManager.connect2GATT()
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

		bluetoothManager.connect2GATT(device)
	}
}

