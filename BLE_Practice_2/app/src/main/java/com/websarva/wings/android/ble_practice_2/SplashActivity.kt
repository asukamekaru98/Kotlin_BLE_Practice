package com.websarva.wings.android.ble_practice_2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    // スプラッシュ画面の表示時間（ミリ秒）
    private val SPLASH_DISPLAY_TIME: Long = 1000 // 1秒

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)

        // SPLASH_DISPLAY_TIME ミリ秒後にメインアクティビティに遷移
        Handler().postDelayed({
            val mainIntent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(mainIntent)
            finish() // スプラッシュ画面を閉じる
        }, SPLASH_DISPLAY_TIME)
    }
}