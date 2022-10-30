package com.uniplat.uniplatapi.configuration

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.Executors

@Configuration
class AsyncConfiguration {

    @Bean
    fun asyncDispatcher(): CoroutineDispatcher = Executors.newCachedThreadPool().asCoroutineDispatcher()

    @Bean
    fun applicationScope(asyncDispatcher: CoroutineDispatcher): CoroutineScope = CoroutineScope(asyncDispatcher + SupervisorJob())
}
