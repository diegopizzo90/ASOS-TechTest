package com.diegopizzo.asostechtest.ui.config

import com.diegopizzo.asostechtest.ui.MainViewModel
import io.reactivex.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get(), CompositeDisposable()) }
}