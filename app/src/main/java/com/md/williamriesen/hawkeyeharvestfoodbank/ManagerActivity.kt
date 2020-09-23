package com.md.williamriesen.hawkeyeharvestfoodbank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders

class ManagerActivity : AppCompatActivity() {

    private lateinit var viewModel: ManagerActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)
        viewModel = ViewModelProviders.of(this).get(ManagerActivityViewModel::class.java)
    }
}