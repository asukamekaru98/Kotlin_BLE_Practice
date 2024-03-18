package com.websarva.wings.android.ble_practice_2

import android.Manifest
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothProfile
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.os.Binder
import android.util.Log
import androidx.core.app.ActivityCompat

class BluetoothLeService(private val activity: MainActivity) : Service() {  //Serviceクラスを継承している

	private var connectionState = STATE_DISCONNECTED

	private var bluetoothGatt: BluetoothGatt? = null

	private val binder = LocalBinder()
	// BluetoothAdapterのインスタンスを保持する変数を定義
	private var bluetoothAdapter: BluetoothAdapter? = null

	fun initialize(): Boolean {
		// デフォルトのBluetoothアダプタを取得
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

		Log.e("TAG", "initialize(): Boolean")

		// Bluetoothアダプタが存在しない場合（例えば、デバイスがBluetoothをサポートしていない場合）はエラーメッセージをログに出力し、falseを返す
		if (bluetoothAdapter == null) {
			Log.e(TAG, "Unable to obtain a BluetoothAdapter.")
			return false
		}
		// Bluetoothアダプタが正常に取得できた場合はtrueを返す
		return true
	}

	// 他のコンポーネントがサービスにバインドしようとしたときに呼び出され、LocalBinderインスタンスを返す
	// これにより、バインドしたコンポーネントはgetService()を呼び出してサービスのインスタンスを取得し
	// サービスのメソッドを直接呼び出すことができる。
	override fun onBind(intent: Intent): IBinder {
		return binder
	}

	//この内部クラスはBinderを継承している
	inner class LocalBinder : Binder() {

		// この関数は、BluetoothLeServiceのインスタンスを返すことができる
		// これにより、他のコンポーネントがサービスの公開メソッドにアクセスできるようになる。
		fun getService() : BluetoothLeService {
			return this@BluetoothLeService
		}
	}



	// BLEデバイスに接続する
	fun connect(address: String): Boolean {

		val bluetoothGattCallback = object : BluetoothGattCallback() {
			override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
				if (newState == BluetoothProfile.STATE_CONNECTED) {
					// GATTサーバーに接続された
					connectionState = STATE_CONNECTED
					broadcastUpdate(ACTION_GATT_CONNECTED)
				} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
					// GATTサーバーから切断された
					connectionState = STATE_DISCONNECTED
					broadcastUpdate(ACTION_GATT_DISCONNECTED)
				}
			}

			override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
				if (status == BluetoothGatt.GATT_SUCCESS) {
					// サービスが発見された
				}
			}

			// 他のコールバックメソッド...
		}

		Log.w("TAG", "bluetoothAdapter")

		// デフォルトのBluetoothアダプタを取得
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

		bluetoothAdapter?.let { adapter ->
			try {


				// 指定されたアドレスに対応するリモートデバイスを取得
				val device = adapter.getRemoteDevice(address)

				Log.w("TAG", "device")

				//GATT接続
				if (ActivityCompat.checkSelfPermission(
						activity,
						Manifest.permission.BLUETOOTH_CONNECT
					) != PackageManager.PERMISSION_GRANTED
				) {
					return false
				}
				bluetoothGatt = device.connectGatt(activity, false, bluetoothGattCallback)

				Log.w("TAG", "bluetoothGatt")

			} catch (exception: IllegalArgumentException) {
				// デバイスが見つからない場合は警告をログに出力し、接続失敗を返す
				Log.w("TAG", "Device not found with provided address.")
				return false
			}
			// 接続処理はここで実装される（現在はコメントアウトされている）
			// connect to the GATT server on the device
		} ?: run {
			// BluetoothAdapterが初期化されていない場合は警告をログに出力し、接続失敗を返す
			Log.w("TAG", "BluetoothAdapter not initialized")
			return false
		}

		// 接続処理は実際にはここで行われないが、成功としてtrueを返す
		return true
	}

	private fun broadcastUpdate(action: String) {
		val intent = Intent(action)
		sendBroadcast(intent)
	}

	companion object {
		const val ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED"
		const val ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED"

		private const val STATE_DISCONNECTED = 0
		private const val STATE_CONNECTED = 2

	}


}