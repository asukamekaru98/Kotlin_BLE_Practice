package com.websarva.wings.android.ble_practice_2

import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class SendActivity : AppCompatActivity() {

    private lateinit var useDevice: BluetoothDevice

    //クラス
    private lateinit var bluetoothManager: BluetoothManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send)

        bluetoothManager = BluetoothManager(this)

        useDevice = intent.getParcelableExtra(Constants.KEY_TRANS_SEND_ACTIVITY)!!

        Thread(Runnable {

            try {
                //BlueTooth接続待機
                bluetoothManager.connectDevice(useDevice)

            } catch (e: InterruptedException) {
                return@Runnable
            }

            runOnUiThread {
                //描画切替 ロード画面 -> カメラ
                //setContentView(R.layout.activity_scan)
                findViewById<View>(R.id.LL_Load).visibility = View.GONE
                findViewById<View>(R.id.LL_Main).visibility = View.VISIBLE
            }
        }).start()


    }
}