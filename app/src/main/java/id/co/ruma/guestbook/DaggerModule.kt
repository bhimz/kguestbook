package id.co.ruma.guestbook

import android.app.Application
import android.content.Context
import dagger.Component
import dagger.Module
import dagger.Provides
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Singleton

/**
 * Created by ariabima on 4/24/17.
 */
@Module
open class AppModule(var mApplication: Application) {

    @Provides
    @Singleton
    fun provideApplication(): Application = mApplication

    @Provides
    @Singleton
    fun provideRealmConfiguration(application: Application):RealmConfiguration {
        Realm.init(application as Context)
        return RealmConfiguration.Builder()
                .schemaVersion(1).migration { realm, oldVersion, newVersion -> }.build()
    }

    @Provides
    fun provideRealm(realmConfiguration: RealmConfiguration) = Realm.getInstance(realmConfiguration)
}

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface GuestBookComponent {
    fun inject(mainActivity: MainActivity)
}
