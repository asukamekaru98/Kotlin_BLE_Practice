package com.websarva.wings.android.ble_practice_2

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.pm.PackageManager
import android.net.wifi.p2p.WifiP2pDevice.CONNECTED
import android.os.Handler
import android.os.ParcelUuid
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.UUID


class BluetoothManager(private val activity: MainActivity) {

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private val bluetoothLeScanner: BluetoothLeScanner? = bluetoothAdapter?.bluetoothLeScanner

    private var bluetoothLeAdvertiser: BluetoothLeAdvertiser? = bluetoothAdapter?.bluetoothLeAdvertiser

    private var scanning = false
    private val handler = Handler()

    private var scanResult = ArrayList<BTdata>()       //まずは空のリストを用意
    //private val recyclerView:RecyclerView = rV

    // BLEスキャンの時間制限(10秒)
    private val SCAN_PERIOD: Long = 1000

    var bluetoothGatt: BluetoothGatt? = null



    // BLEデバイスをスキャンする
    fun scanLeDevice() {
        val scanSettings = ScanSettings.Builder().build()
        val scanFilters = mutableListOf<ScanFilter>()
        val scanFilter = ScanFilter.Builder()
            .setServiceUuid(ParcelUuid.fromString("00001812-0000-1000-8000-00805f9b34fb")) // HID Service UUID
            .build()

        scanFilters.add(scanFilter)

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
                scanner.startScan(scanFilters, scanSettings, leScanCallback)

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

                //Log.d("TAG", scanResult.toString())
            }

            // スキャン結果からBluetoothデバイスを取得し、リストに追加
            //leDeviceListAdapter.addDevice(result.device)

            // デバイスリストの変更を通知し、表示を更新
            //leDeviceListAdapter.notifyDataSetChanged()
        }

    }

    fun connect2GATT(device: BluetoothDevice) {

        val gattCallback = object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.i("TAG", "Connected to GATT server.")
                    if (ActivityCompat.checkSelfPermission(
                            activity,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    gatt.discoverServices()
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    Log.i("TAG", "Disconnected from GATT server.")
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    val services = gatt.services
                    services.forEach { service ->
                        Log.i("TAG", "Service UUID: ${service.uuid}")
                        val characteristics = service.characteristics
                        characteristics.forEach { characteristic ->
                            Log.i("TAG", "Characteristic UUID: ${characteristic.uuid}")
                            // 必要に応じてキャラクタリスティックの読み取りや書き込みを行う
                        }
                    }
                } else {
                    Log.w("TAG", "onServicesDiscovered received: $status")
                }
            }
        }


/*
        val callback = object : BluetoothGattCallback() {
            // 接続状態が変更されたときに実行される
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                // 接続された
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.i("TAG", "Connected to GATT server.")
                    // サービスを検索する
                    if (ActivityCompat.checkSelfPermission(
                            activity,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    Log.i("TAG", "Attempting to start service discovery:" + gatt.discoverServices())

                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    // 切断された
                    Log.i("TAG", "Disconnected from GATT server.")
                }
            }

            // サービスの検索結果を返す
            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                // 成功
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    // サービスのリストを取得
                   /* val gattService = getSupportedGattServices(address)
                    for (service in gattService) {
                        val uuid = service.uuid
                        // サービスからCharacteristicsのリストを取得
                        val characteristics = service.characteristics
                        // Characteristic に Notification の受信要求を設定
                        for (charastic in characteristics) {
                            //
                            if (ActivityCompat.checkSelfPermission(
                                    activity,
                                    Manifest.permission.BLUETOOTH_CONNECT
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                return
                            }
                            gatt.setCharacteristicNotification(charastic, true)
                        }
                    }*/
                    Log.w("TAG", "成功")
                } else {
                    Log.w("TAG", "onServicesDiscovered received: $status")
                }
            }

            // 読み込み通知
            override fun onCharacteristicRead(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                status: Int
            ) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    // Characteristicの読込成功
                }
            }

            // 書き込み通知
            override fun onCharacteristicWrite(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                status: Int
            ) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    // Characteristicの書込成功
                }
                // ...
            }
        }
*/
        /*
        val mGattCallback = object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                super.onConnectionStateChange(gatt, status, newState)

                // 接続成功し、サービス取得
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    bluetoothGatt = gatt
                    discoverService()
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                super.onServicesDiscovered(gatt, status)

                val serviceList = gatt.services

                for (s in serviceList) {
                    // サービス一覧を取得したり探したりする処理
                    // あとキャラクタリスティクスを取得したり探したりしてもよい
                    Log.d("TAG", s.toString())
                }
            }
        }

         */
/*
        val gattCallback = object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    // GATTサーバーに接続された
                    Toast.makeText(activity, "GATT Connect", Toast.LENGTH_SHORT).show()
                    Log.d("TAG", "GATT Connect")
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    // GATTサーバーから切断された
                    Toast.makeText(activity, "GATT No Connect", Toast.LENGTH_SHORT).show()
                    Log.d("TAG", "GATT No Connect")
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                super.onServicesDiscovered(gatt, status)

                val serviceList = gatt.services

                for (s in serviceList) {
                    Log.d("TAG", "s${s}")
                }

                if (status == BluetoothGatt.GATT_SUCCESS) {
                    // サービスが発見された
                    Log.d("TAG", "サービスが発見された")
                }
            }

            // 他のコールバックメソッド...
        }
        */

        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // ここでGattを使用して通信を行う
            bluetoothGatt = device.connectGatt(activity, false, gattCallback)
           // bluetoothGatt?.connect()

        }else{
            Toast.makeText(activity, "パーミッションが許可されていません", Toast.LENGTH_SHORT).show()
        }


    }

    fun connectToDevice(device: BluetoothDevice) {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        bluetoothGatt = device.connectGatt(activity, false, gattCallback)
    }

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i("TAG", "Connected to GATT server.")
                if (ActivityCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                gatt.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i("TAG", "Disconnected from GATT server.")
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                val services = gatt.services
                services.forEach { service ->
                    Log.i("TAG", "Service UUID: ${service.uuid}")
                    val characteristics = service.characteristics
                    characteristics.forEach { characteristic ->
                        Log.i("TAG", "Characteristic UUID: ${characteristic.uuid}")
                        // 必要に応じてキャラクタリスティックの読み取りや書き込みを行う
                    }
                }
            } else {
                Log.w("TAG", "onServicesDiscovered received: $status")
            }
        }
    }

    // HIDデバイスに文字を送信する関数
    fun sendStringToDevice(stringToSend: String) {
        // HIDサービスとキャラクタリスティックのUUID
        val serviceUuid = UUID.fromString("00001812-0000-1000-8000-00805f9b34fb") // HID Service UUID
        val characteristicUuid = UUID.fromString("00002a4d-0000-1000-8000-00805f9b34fb") // Report Characteristic UUID

        // HIDサービスを取得
        val service = bluetoothGatt?.getService(serviceUuid)
        if (service == null) {
            Log.e("TAG", "HID service not found.")
            return
        }

        // Reportキャラクタリスティックを取得
        val characteristic = service.getCharacteristic(characteristicUuid)
        if (characteristic == null) {
            Log.e("TAG", "Report characteristic not found.")
            return
        }

        // 書き込むデータを設定
        characteristic.setValue(stringToSend.toByteArray(Charsets.UTF_8))

        // キャラクタリスティックにデータを書き込む
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("TAG", "Failed to write data: $stringToSend")
        }else{
            bluetoothGatt?.writeCharacteristic(characteristic)
            Log.i("TAG", "Data written successfully: $stringToSend")
        }

    }

    fun startAdvertisingAsHID() {
        val settings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
            .setConnectable(true)
            .setTimeout(0)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
            .build()

        val data = AdvertiseData.Builder()
            .setIncludeDeviceName(false) // デバイス名は含めない
            .setIncludeTxPowerLevel(false) // Txパワーレベルも含めない
            // HIDプロファイルのデータを含める
            .addServiceUuid(ParcelUuid.fromString("00001812-0000-1000-8000-00805f9b34fb")) // HID Service UUID
            // その他の必要なHIDデータを含める
            .build()

        val callback = object : AdvertiseCallback() {
            override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
                super.onStartSuccess(settingsInEffect)
                // アドバタイズが正常に開始された場合の処理
            }

            override fun onStartFailure(errorCode: Int) {
                super.onStartFailure(errorCode)
                // アドバタイズが開始できなかった場合の処理
            }
        }

        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.BLUETOOTH_ADVERTISE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        bluetoothLeAdvertiser?.startAdvertising(settings, data, callback)
    }

  /*  private fun stopAdvertising() {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.BLUETOOTH_ADVERTISE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        bluetoothLeAdvertiser?.stopAdvertising(callback)
    }

   */
}