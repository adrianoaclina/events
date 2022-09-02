package br.com.events

import android.app.Application
import android.content.Intent
import br.com.events.home.presentation.activity.HomeActivity

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this)
        redirectToHome(this)
    }

    private fun redirectToHome(myApplication: Application) {
        val intent = Intent(myApplication.applicationContext, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}