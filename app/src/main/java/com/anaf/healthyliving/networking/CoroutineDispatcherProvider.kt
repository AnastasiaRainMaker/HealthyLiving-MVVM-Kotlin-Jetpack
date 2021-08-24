package com.anaf.healthyliving.networking

import kotlinx.coroutines.Dispatchers

/**
 * Creating dispatcher provider for testability purposes so in tests I have replace it with test
 * coroutine dispatcher
 */
class CoroutineDispatcherProvider {

    val io
        get() = Dispatchers.IO
    val default
        get() = Dispatchers.Default
    val main
        get() = Dispatchers.Main
}