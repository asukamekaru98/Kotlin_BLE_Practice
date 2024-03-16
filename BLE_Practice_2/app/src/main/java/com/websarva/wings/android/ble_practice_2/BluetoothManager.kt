package com.websarva.wings.android.ble_practice_2

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class BluetoothManager(private val context: Context) {
/*
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private val bluetoothLeScanner: BluetoothLeScanner? = bluetoothAdapter?.bluetoothLeScanner

    private var scanning = false
    private val handler = Handler()

    // BLEスキャンの時間制限(10秒)
    private val SCAN_PERIOD: Long = 1000


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
                Log.d("TAG", device.uuids?.contentToString() ?: "No UUIDs") // device.uuidsがnullの場合に備えて安全呼び出し演算子を使用します
                Log.d("TAG", device.bluetoothClass?.toString() ?: "No Bluetooth Class") // device.bluetoothClassがnullの場合に備えて安全呼び出し演算子を使用します


                val data = BTdata(device.name ?: "No Name")





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

    fun connectToDevice(device: BluetoothDevice, gattCallback: BluetoothGattCallback) {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        val gatt = device.connectGatt(context, false, gattCallback, BluetoothDevice.TRANSPORT_LE)
        // ここでGattを使用して通信を行う


    }

 */
}