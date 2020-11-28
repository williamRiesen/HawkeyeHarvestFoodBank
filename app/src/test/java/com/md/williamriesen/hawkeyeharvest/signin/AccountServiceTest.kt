package com.md.williamriesen.hawkeyeharvest.signin

import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AccountServiceTest {

    var accountService: AccountService? = null

    // Mock values
    var db: FirebaseFirestore? = null

    @BeforeEach
    fun init() {
        db = mockk()
        accountService = AccountService(db!!)
    }

    @Test
    fun test_that_failure_callback_is_invoked_on_failure() {
        val collection: CollectionReference = mockk()
        val docRef: DocumentReference = mockk()
        val docSnapshot: DocumentSnapshot = mockk()

        every { db?.collection("accounts") } returns collection
        every { docRef.get() } returns Tasks.call { docSnapshot }

        every { docSnapshot["lastOrderDate"] } returns Timestamp.now()
        every { docSnapshot["familySize"] } returns 5
        every { docSnapshot["lastOrderType"] } returns "ON_SITE"
        every { docSnapshot["orderState"] } returns "NOT STARTED YET"
        every { docSnapshot["pickUpHour24"] } returns 0

        accountService!!.fetchAccount("foobar")
            .addOnSuccessListener {
                println("Did we make it!")
            }

        Thread.sleep(15000L)
    }
}