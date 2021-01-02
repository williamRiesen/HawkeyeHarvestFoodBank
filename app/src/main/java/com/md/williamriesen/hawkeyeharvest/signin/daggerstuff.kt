package com.md.williamriesen.hawkeyeharvest.signin

import com.google.firebase.firestore.FirebaseFirestore
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
}

@Singleton
@Component(modules = [FireStoreModule::class])
interface ServicesComponent {
    fun getAccountService(): AccountService
    fun getCatalogService(): CatalogService
}
