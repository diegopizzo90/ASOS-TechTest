package com.diegopizzo.network.base

/**
 * Validates nullable Boolean
 */
fun Boolean?.isTrue(): Boolean {
    return this == true
}