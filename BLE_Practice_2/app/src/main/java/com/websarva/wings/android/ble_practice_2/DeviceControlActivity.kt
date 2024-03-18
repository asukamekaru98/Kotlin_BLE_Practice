package com.websarva.wings.android.ble_practice_2

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class DeviceControlActivity : AppCompatActivity() {


	// BluetoothLeServiceのインスタンスを保持する変数
	private var bluetoothService : BluetoothLeService? = null

	// サービスのライフサイクルを管理するためのコード
	private val serviceConnection: ServiceConnection = object : ServiceConnection {
		// サービスが接続されたときに呼び出されるメソッド
		override fun onServiceConnected(
			componentName: ComponentName,
			service: IBinder
		) {
			// BluetoothLeServiceのインスタンスを取得
			bluetoothService = (service as BluetoothLeService.LocalBinder).getService()

			//bluetoothService が null でない場合に実行
			bluetoothService?.let { bluetooth ->

				if (!bluetooth.initialize()) {  //BLEサービスの初期化を試みる
					Log.e(TAG, "Unable to initialize Bluetooth")
					finish()
				}

				// ここでサービスのメソッドを呼び出して接続をチェックし、デバイスに接続する
				//bluetooth.connect(deviceAddress)
			}
		}

		// サービスが切断されたときに呼び出されるメソッド
		override fun onServiceDisconnected(componentName: ComponentName) {
			// BluetoothLeServiceのインスタンスをnullに設定
			bluetoothService = null
		}
	}

	// アクティビティが作成されたときに呼び出されるメソッド
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		//setContentView(R.layout.gatt_services_characteristics)

		// BluetoothLeServiceに接続するためのIntentを作成
		val gattServiceIntent = Intent(this, BluetoothLeService::class.java)
		// サービスにバインド
		bindService(gattServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
	}

	private val gattUpdateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
		override fun onReceive(context: Context, intent: Intent) {
			when (intent.action) {
				BluetoothLeService.ACTION_GATT_CONNECTED -> {
					//connected = true
					//updateConnectionState(R.string.connected)
				}
				BluetoothLeService.ACTION_GATT_DISCONNECTED -> {
					//connected = false
					//updateConnectionState(R.string.disconnected)
				}
			}
		}
	}

	override fun onResume() {
		super.onResume()
		// BroadcastReceiverを登録し、BluetoothGattの更新を受信する準備を行う
		registerReceiver(gattUpdateReceiver, makeGattUpdateIntentFilter())
		if (bluetoothService != null) {
			//val result = bluetoothService!!.connect(deviceAddress)
			//Log.d(DeviceControlsActivity.TAG, "Connect request result=$result")
		}
	}

	// BroadcastReceiverの登録を解除し、メモリリークを防止する
	override fun onPause() {
		super.onPause()
		unregisterReceiver(gattUpdateReceiver)
	}

	// BluetoothGattの更新を受け取るためのIntentFilterを作成するメソッド
	private fun makeGattUpdateIntentFilter(): IntentFilter? {
		return IntentFilter().apply {
			addAction(BluetoothLeService.ACTION_GATT_CONNECTED)     // GATT接続アクションを追加
			addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED)  // GATT切断アクションを追加
		}
	}
}