package com.diegopizzo.asostechtest.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.onNavDestinationSelected
import com.diegopizzo.asostechtest.R
import com.diegopizzo.asostechtest.databinding.ActivityMainBinding
import com.diegopizzo.asostechtest.ui.base.ActivityViewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : ActivityViewBinding<ActivityMainBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        viewModel.viewEffects().observe(this, viewEffectObserver)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController =
            (supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment).navController
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    private val viewEffectObserver = Observer<ViewEffect> { viewEffect ->
        when (viewEffect) {
            is ViewEffect.ShowLaunchArticle -> {
                if (viewEffect.articleLink == null) {
                    Toast.makeText(this, R.string.not_available_wiki, Toast.LENGTH_LONG).show()
                } else {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(viewEffect.articleLink)))
                }
            }
        }
    }
}