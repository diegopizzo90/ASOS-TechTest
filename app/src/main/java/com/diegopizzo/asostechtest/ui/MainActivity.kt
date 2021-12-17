package com.diegopizzo.asostechtest.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.Observer
import com.diegopizzo.asostechtest.R
import com.diegopizzo.asostechtest.databinding.ActivityMainBinding
import com.diegopizzo.asostechtest.ui.base.ActivityViewBinding
import com.diegopizzo.network.base.isTrue
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : ActivityViewBinding<ActivityMainBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        viewModel.viewStates.observe(this, viewStateObserver)
    }

    private val viewStateObserver = Observer<MainViewState> { viewState ->
        if (viewState.isItemClicked.isTrue()) {
            if (viewState.launchArticle == null) {
                Toast.makeText(this, R.string.not_available_wiki, Toast.LENGTH_LONG).show()
            } else {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(viewState.launchArticle)))
            }
        }
    }
}