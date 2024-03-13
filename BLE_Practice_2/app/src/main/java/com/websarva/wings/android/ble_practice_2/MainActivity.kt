package com.websarva.wings.android.ble_practice_2

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {

	private val PERMISSION_REQUEST_CODE = 1000  //パーミッションコード
	private val REQUEST_ENABLE_BT = 1   //

	private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
	private val bluetoothLeScanner: BluetoothLeScanner? = bluetoothAdapter?.bluetoothLeScanner


	private var scanning = false
	private val handler = Handler()

	// BLEスキャンの時間制限(10秒)
	private val SCAN_PERIOD: Long = 10000

	//private val leDeviceListAdapter = LeDeviceListAdapter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContentView(R.layout.activity_main)
		/*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}*/

		/** 権限確認 **/
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // デバイスのAndroidバージョンがAndroid 10（APIレベル29）以上であるかどうかを確認
			// 指定の権限が既に許可されているか確認
			if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_BACKGROUND_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
				// 権限がなければ、ユーザーに権限を要求する
				ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION), PERMISSION_REQUEST_CODE) // 権限要求の識別コード
			}
		}

		//// Bluetooth Scanパーミッションをチェック
		//if (ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
		//	// パーミッションが許可されていない場合、ユーザーにパーミッションの許可を求める
		//	ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
		//	//return
		//}

		checkPermission()   //パーミッション確認

		//ボタン押下
		findViewById<Button>(R.id.scan_button).setOnClickListener {
			scanLeDevice()
		}

	}

	fun checkPermission() {
		// 既に許可している
		if (checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
			return
		}

		// 許可していない場合、パーミッションの取得を行う
		// 以前拒否されている場合は、なぜ必要かを通知し、手動で許可してもらう
		if (!shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH_SCAN)) {
			Toast.makeText(this, "ファイル書き込みのために許可してください", Toast.LENGTH_SHORT)
				.show()
		}
		// パーミッションの取得を依頼
		requestPermissions(
			arrayOf<String>(Manifest.permission.BLUETOOTH_SCAN),
			PERMISSION_REQUEST_CODE
		)
	}

	// BLEデバイスをスキャンする
	private fun scanLeDevice() {

		/** 権限確認 **/
		// Bluetoothパーミッションをチェック
		if (ActivityCompat.checkSelfPermission(this@MainActivity,Manifest.permission.BLUETOOTH)!= PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this@MainActivity,arrayOf(Manifest.permission.BLUETOOTH), PERMISSION_REQUEST_CODE)
			//return
		}

		// Bluetooth Adminパーミッションをチェック
		if (ActivityCompat.checkSelfPermission(this@MainActivity,Manifest.permission.BLUETOOTH_ADMIN)!= PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this@MainActivity,arrayOf(Manifest.permission.BLUETOOTH_ADMIN), PERMISSION_REQUEST_CODE)
			//return
		}

		// Bluetooth Scanパーミッションをチェック
//		if (ContextCompat.checkSelfPermission(baseContext,Manifest.permission.BLUETOOTH_SCAN)!= PackageManager.PERMISSION_GRANTED) {
//			ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.BLUETOOTH_SCAN), PERMISSION_REQUEST_CODE)
//		}

		if (ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
			// 権限が付与されていない場合の処理
			val permissions = arrayOf(Manifest.permission.BLUETOOTH_SCAN)
			ActivityCompat.requestPermissions(this@MainActivity, permissions, PERMISSION_REQUEST_CODE)
			//return
		}

		// Bluetooth Scanパーミッションをチェック
		if (ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// パーミッションが許可されていない場合、ユーザーにパーミッションの許可を求める
			ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
			//return
		}



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

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)

		when (requestCode) {
			PERMISSION_REQUEST_CODE -> {
				if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
					// パーミッションが許可されたときの処理を書く
				} else {
					// パーミッションが拒否されたときの処理を書く
					Toast.makeText(this, "権限ないです", Toast.LENGTH_SHORT).show()
				}
				return
			}
			// 他の 'when' ブランチをチェックするためのコード
			else -> {
				// 未知の requestCode
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
				Log.d("TAG", device.name ?: "No Name")
				Log.d("TAG", device.address ?: "No Address")
				Log.d("TAG", device.bondState.toString())
				Log.d("TAG", device.type.toString())
			}

			//Log.d("TAG","aaaaa")
			// スキャン結果からBluetoothデバイスを取得し、リストに追加
			//leDeviceListAdapter.addDevice(result.device)

			// デバイスリストの変更を通知し、表示を更新
			//leDeviceListAdapter.notifyDataSetChanged()
		}

	}

}