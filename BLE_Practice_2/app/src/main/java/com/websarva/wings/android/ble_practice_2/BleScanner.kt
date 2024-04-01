package com.websarva.wings.android.ble_practice_2

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class BleScanner(val activity: MainActivity):BluetoothManager() {

    private var scanning = false

    // BLEデバイスをスキャンする
    fun scanLeDevice() {
        //val scanSettings = ScanSettings.Builder().build()
        //val scanFilters = mutableListOf<ScanFilter>()
        //val scanFilter = ScanFilter.Builder()
        //    .setServiceUuid(ParcelUuid.fromString("00001812-0000-1000-8000-00805f9b34fb")) // HID Service UUID
        //    .build()

        //scanFilters.add(scanFilter)

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
                //scanner.startScan(leScanCallback)
                //scanner.startScan(scanFilters, scanSettings, leScanCallback)
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
                //BTdata(device)
                val data = BTdata(device)

                //scanResult.add(data)   //リストに追加
                activity.setAddData(data)
                //recyclerAdapter.notifyItemInserted(addList.lastIndex)   //追加した情報がRecyclerViewの末尾に追加される

                //Log.d("TAG", scanResult.toString())
            }

            // スキャン結果からBluetoothデバイスを取得し、リストに追加
            //leDeviceListAdapter.addDevice(result.device)

            // デバイスリストの変更を通知し、表示を更新
            //leDeviceListAdapter.notifyDataSetChanged()
        }

    }
}