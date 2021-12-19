package com.diegopizzo.network.creator

import org.koin.dsl.module

val creatorModule = module {
    factory<ISpaceXCreator> { SpaceXCreator() }
}