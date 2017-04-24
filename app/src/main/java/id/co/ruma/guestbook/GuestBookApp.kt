package id.co.ruma.guestbook

import android.app.Application

/**
 * Created by ariabima on 4/24/17.
 */
class GuestBookApp: Application() {
    companion object {
        lateinit var guestBookComponent: GuestBookComponent
    }
    override fun onCreate() {
        super.onCreate()
        guestBookComponent = DaggerGuestBookComponent.builder().appModule(AppModule(this)).build()
    }
}