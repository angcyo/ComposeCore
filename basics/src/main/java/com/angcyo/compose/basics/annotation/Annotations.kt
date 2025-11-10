package com.angcyo.compose.basics.annotation

/**
 * @author <a href="mailto:angcyo@126.com">angcyo</a>
 * @date 2025/11/08
 */

/**供外部使用的接口*/
@MustBeDocumented
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.FUNCTION,
)
annotation class Api

/**自身的属性, 不推荐外部直接使用*/
@MustBeDocumented
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY,
)
annotation class Property

/**外部可以配置的属性*/
@MustBeDocumented
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY,
)
annotation class Config

/**输出的属性*/
@MustBeDocumented
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY,
)
annotation class Output