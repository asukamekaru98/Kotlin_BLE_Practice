package com.websarva.wings.android.ble_practice_2

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Binder

class BluetoothLeService : Service() {  //Serviceクラスを継承している

	private val binder = LocalBinder()

	override fun onBind(intent: Intent): IBinder {
		return binder
	}

	inner class LocalBinder : Binder() {    //この内部クラスはBinderを継承している]

		// この関数は、BluetoothLeServiceのインスタンスを返すことができる
		// これにより、他のコンポーネントがサービスの公開メソッドにアクセスできるようになる。
		fun getService() : BluetoothLeService {
			return this@BluetoothLeService
		}
	}


}