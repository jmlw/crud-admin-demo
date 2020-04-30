package com.joshmlwood.crudadmindemo.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class AppConfig {
    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.maxAge = 1800
        config.allowCredentials = true
        config.exposedHeaders = "Authorization,Link,X-Total-Count,Access-Control-Allow-Origin".split(',')
        config.allowedHeaders = listOf("*")
        config.allowedMethods = listOf("*")
        config.allowedOrigins = listOf("*")
        if (config.allowedOrigins != null && config.allowedOrigins!!.isNotEmpty()) {
            source.apply {
                registerCorsConfiguration("/api/**", config)
                registerCorsConfiguration("/management/**", config)
            }
        }
        return CorsFilter(source)
    }
}
