package com.diegopizzo.network.cache

import org.koin.dsl.module

val cacheInteractorModule = module {
    single<ISpaceXCacheInteractor> { SpaceXCacheInteractor(get(), TTL_MINUTE) }
}

private const val TTL_MINUTE = 5L