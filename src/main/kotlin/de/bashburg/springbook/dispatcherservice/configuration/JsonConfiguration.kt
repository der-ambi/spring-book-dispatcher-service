package de.bashburg.springbook.dispatcherservice.configuration

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.beans.factory.support.DefaultListableBeanFactory
import org.springframework.cloud.function.json.JacksonMapper
import org.springframework.cloud.function.json.JsonMapper
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary


//@Configuration(proxyBeanMethods = false)
//@AutoConfigureAfter(ContextFunctionCatalogAutoConfiguration::class)
class JsonConfiguration {

    companion object {

        @Bean
        fun removeJsonMapperBean(): BeanFactoryPostProcessor {
            return BeanFactoryPostProcessor { beanFactory ->
                if (beanFactory is DefaultListableBeanFactory && beanFactory.containsBeanDefinition("jsonMapper")) {
                    // Remove the existing JsonMapper bean definition
                    beanFactory.removeBeanDefinition("jsonMapper")
                }
            }

        }
    }

    @Bean("jsonMapper")
    @Primary
    fun kotlinEnabledjsonMapper(context: ApplicationContext): JsonMapper {
        val kotlinModule = KotlinModule.Builder()
            .enable(KotlinFeature.StrictNullChecks)
            .build()

        return JacksonMapper(
            com.fasterxml.jackson.databind.json.JsonMapper.builder()
                .addModules(kotlinModule, JavaTimeModule())
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(DeserializationFeature.FAIL_ON_TRAILING_TOKENS, true)
                .build()
        )
    }
}