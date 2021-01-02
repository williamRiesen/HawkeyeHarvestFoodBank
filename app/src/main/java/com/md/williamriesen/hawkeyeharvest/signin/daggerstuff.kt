package com.md.williamriesen.hawkeyeharvest.signin

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FireStoreModule {

    @Provides
    @Singleton
    fun provideFireStore(): FirebaseFirestore {
        val db = FirebaseFirestore.getInstance()
        db.useEmulator("10.0.2.2", 8080)
        return db
    }

    @Provides
    @Singleton
    fun provideFireBase(): FirebaseInstallations {
        return FirebaseInstallations.getInstance()
    }

    @Provides
    @Singleton
    fun provideFireBaseMessaging(): FirebaseMessaging {
        return FirebaseMessaging.getInstance()
    }
}

@Singleton
@Component(modules = [FireStoreModule::class])
interface ServicesComponent {
    fun getAccountService(): AccountService
    fun getCatalogService(): CatalogService
    fun getOrderService(): OrderService
}
