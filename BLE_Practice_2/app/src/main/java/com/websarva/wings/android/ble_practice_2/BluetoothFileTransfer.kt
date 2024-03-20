package com.websarva.wings.android.ble_practice_2

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.widget.Toast
import androidx.core.app.ActivityCompat
import java.io.File
import java.io.IOException
import java.util.UUID

class BluetoothFileTransfer(private val context: Context) {

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    fun sendFile(device: BluetoothDevice, file: File) {
        SendFileTask(device, file).execute()
    }

    private inner class SendFileTask(private val device: BluetoothDevice, private val file: File) :
        AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg params: Void?): Boolean {

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }

            val socket: BluetoothSocket? = device.createRfcommSocketToServiceRecord(UUID.randomUUID())
            bluetoothAdapter?.cancelDiscovery()

            try {
                socket?.connect()
                val outputStream = socket?.outputStream
                val fileInputStream = file.inputStream()

                outputStream?.write(fileInputStream.readBytes())
                outputStream?.flush()
                outputStream?.close()
                socket?.close()
                return true
            } catch (e: IOException) {
                e.printStackTrace()
                return false
            }


        }


        override fun onPostExecute(result: Boolean) {
            super.onPostExecute(result)
            if (result) {
                Toast.makeText(context, "File sent successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to send file", Toast.LENGTH_SHORT).show()
            }
        }


    }


}