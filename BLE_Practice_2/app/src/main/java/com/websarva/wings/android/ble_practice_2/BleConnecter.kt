package com.websarva.wings.android.ble_practice_2

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import java.util.UUID

class BleConnecter(val activity: Context):BluetoothManager() {


    private val gattCallback2 = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            Log.i("TAG", "onConnectionStateChange")

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i("TAG", "onConnectionStateChange >  if (newState == BluetoothProfile.STATE_CONNECTED) {")

                // サービスの発見を開始
                if (ActivityCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                gatt.discoverServices()
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            Log.i("TAG", "onServicesDiscovered")

            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i("TAG", "onCharacteristicWrite >  if (status == BluetoothGatt.GATT_SUCCESS) {")

                // サービスとキャラクタリスティックを取得
                val service: BluetoothGattService? = gatt.getService(serviceUUID)
                val characteristic: BluetoothGattCharacteristic? = service?.getCharacteristic(characteristicUUID)

                // 数字をバイト配列に変換して送信
                val numberToSend: Int = 123 // 送信したい数字
                val value = ByteArray(4) // Intは4バイト
                value[0] = (numberToSend shr 24).toByte()
                value[1] = (numberToSend shr 16).toByte()
                value[2] = (numberToSend shr 8).toByte()
                value[3] = numberToSend.toByte()

                characteristic?.value = value
                if (ActivityCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                gatt.writeCharacteristic(characteristic)
            }
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            Log.i("TAG", "onCharacteristicWrite")

            if (status == BluetoothGatt.GATT_SUCCESS) {

                // 数字の送信に成功
                Toast.makeText(activity, "送信成功！", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun connectDevice(device: BluetoothDevice) {
        //val device = bluetoothAdapter?.getRemoteDevice(useDevice.address)

        //useDevice = device


        //bluetoothAdapter?.getRemoteDevice(useDevice.address)

        if(device.uuids != null) { // nullチェックを追加
            Log.i("TAG", "connectDevice > if(device.uuids != null) {")

            serviceUUID = UUID.fromString(device.uuids.toString())
        }

        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val device2 = bluetoothAdapter?.getRemoteDevice(device.address)
        //bluetoothGatt = useDevice.connectGatt(activity, false, gattCallback2)
        bluetoothGatt = device2?.connectGatt(activity, false, gattCallback2)
    }
//------------------------------------------------------------------------------------------------------------------

    /*============================= BLE解除 =============================*/
    fun disconnectDevice() {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        bluetoothGatt?.disconnect()
        Toast.makeText(activity, "GATT解除！", Toast.LENGTH_SHORT).show()
    }




}