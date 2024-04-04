package com.websarva.wings.android.ble_practice_2

import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class SendActivity : AppCompatActivity() {

    private lateinit var useDevice: BluetoothDevice

    //クラス
    //private lateinit var bluetoothManager: BluetoothManager
    private  lateinit var bleConnecter: BleConnecter
    private lateinit var etText: EditText           //UI EditText:Bluetoothアドレス

    private var sEditTextBtText:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send)

       //bluetoothManager = BluetoothManager(this)
        bleConnecter = BleConnecter(this)

        etText = findViewById(R.id.edit_text)    //Btn：Bluetoothアドレススキャン

        useDevice = intent.getParcelableExtra(Constants.KEY_TRANS_SEND_ACTIVITY)!!

        //bleConnecter.connectDevice(useDevice)

        Thread(Runnable {

            try {
                //BlueTooth接続待機
                //bluetoothManager.connectDevice(useDevice)
                bleConnecter.connectDevice(useDevice)

            } catch (e: InterruptedException) {
                return@Runnable
            }

            runOnUiThread {
                //描画切替 ロード画面 -> カメラ
                //setContentView(R.layout.activity_scan)
                findViewById<View>(R.id.BtSendLoad).visibility = View.GONE
                //findViewById<View>(R.id.LL_Main).visibility = View.VISIBLE
            }
        }).start()


        //ボタン押下
        findViewById<Button>(R.id.button_send).setOnClickListener {

            // テキストボックスに入力されている文字列を取得
            sEditTextBtText = etText.text.toString()

            //BTスキャン
            //bluetoothManager.scanLeDevice()
            bleConnecter.send(sEditTextBtText.toInt())

            //BT GATTサーバ接続
            //	bluetoothManager.connect2GATT()
        }


    }
}