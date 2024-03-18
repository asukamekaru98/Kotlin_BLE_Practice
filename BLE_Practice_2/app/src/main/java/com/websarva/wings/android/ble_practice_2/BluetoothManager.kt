package com.websarva.wings.android.ble_practice_2

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class BluetoothManager(private val activity: MainActivity) {

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private val bluetoothLeScanner: BluetoothLeScanner? = bluetoothAdapter?.bluetoothLeScanner

    private var scanning = false
    private val handler = Handler()

    private var scanResult = ArrayList<BTdata>()       //まずは空のリストを用意
    //private val recyclerView:RecyclerView = rV

    // BLEスキャンの時間制限(10秒)
    private val SCAN_PERIOD: Long = 1000

   // var bluetoothGatt: BluetoothGatt? = null

    // BLEデバイスをスキャンする
    fun scanLeDevice() {

        bluetoothLeScanner?.let { scanner ->
            if (!scanning) {
                // スキャン中でない場合、一定のスキャン期間後にスキャンを停止します。
                handler.postDelayed({
                    scanning = false


                    if (ActivityCompat.checkSelfPermission(
                            activity,
                            Manifest.permission.BLUETOOTH_SCAN
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return@postDelayed
                    }
                    scanner.stopScan(leScanCallback)

                    //スキャン完了後、スキャン結果を返す
                    //activity.setScanResults(scanResult)

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
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return
            }


            result.device?.let { device ->
                //names.add(device.name?:"No Name")
                //val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names)

                //adapter.notifyDataSetChanged()


                //val data = BTdata(device.name ?: "No Name")
                val data = BTdata(device)

                //scanResult.add(data)   //リストに追加
                activity.setAddData(data)
                //recyclerAdapter.notifyItemInserted(addList.lastIndex)   //追加した情報がRecyclerViewの末尾に追加される

                Log.d("TAG", scanResult.toString())
            }

            // スキャン結果からBluetoothデバイスを取得し、リストに追加
            //leDeviceListAdapter.addDevice(result.device)

            // デバイスリストの変更を通知し、表示を更新
            //leDeviceListAdapter.notifyDataSetChanged()
        }

    }

/*
    fun connect2GATT(device: BluetoothDevice) {



        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // ここでGattを使用して通信を行う
            bluetoothGatt = device.connectGatt(activity, false, gattCallback)
        }else{
            Toast.makeText(activity, "パーミッションが許可されていません", Toast.LENGTH_SHORT).show()
        }

        //



    }

 */

}