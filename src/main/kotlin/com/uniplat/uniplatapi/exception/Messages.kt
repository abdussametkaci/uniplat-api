package com.uniplat.uniplatapi.exception

import org.springframework.context.MessageSource
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import java.util.Locale

object Messages {
    private fun messageSource(): MessageSource {
        val messageSource = ReloadableResourceBundleMessageSource()
        messageSource.setBasename("classpath:messages")
        messageSource.setDefaultEncoding("UTF-8")
        return messageSource
    }

    fun getMessage(message: String, args: Array<Any>? = null): String {
        return messageSource().getMessage(message, args, Locale.ENGLISH)
    }
}
