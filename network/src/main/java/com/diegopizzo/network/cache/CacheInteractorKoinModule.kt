package com.diegopizzo.network.cache

import org.koin.dsl.module

val cacheInteractorModule = module {
    single<ISpaceXCacheInteractor> { SpaceXCacheInteractor(get(), get()) }
}