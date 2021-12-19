package com.diegopizzo.network.interactor

import org.koin.dsl.module

val interactorModule = module {
    factory<ISpaceXInteractor> { SpaceXInteractor(get(), get()) }
}