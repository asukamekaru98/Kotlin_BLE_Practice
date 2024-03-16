package com.websarva.wings.android.ble_practice_2

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
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



	private var addList = ArrayList<BTdata>()       //まずは空のリストを用意
	private lateinit var recyclerView:RecyclerView

	private var recyclerAdapter = RecyclerAdapter(addList)

	private lateinit var permissionManager: PermissionManager

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContentView(R.layout.activity_main)
		setSupportActionBar(findViewById(R.id.toolbar)) //アクションバー

		// PermissionManagerのインスタンスを生成し、MainActivityのインスタンスを渡す
		permissionManager = PermissionManager(this)



		recyclerView = findViewById(R.id.my_recycler_view)              //使うRecyclerViewのid取得
		recyclerView.adapter = recyclerAdapter                          //アダプタ接続
		recyclerView.layoutManager = LinearLayoutManager(this)   //各アイテムを縦に並べる指示


		recyclerView.setHasFixedSize(true)

		//ボタン押下
		findViewById<Button>(R.id.scan_button).setOnClickListener {
			//scanLeDevice()
			permissionManager.checkPermission()
		}

	}
}