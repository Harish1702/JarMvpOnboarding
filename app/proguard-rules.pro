#


# Retrofit
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

-keepattributes Signature, Exceptions


# Keep the generic Continuation class used by suspend functions
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# For suspend functions in Retrofit interfaces, keep the generic return type
-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>




# It's also a good idea to keep Kotlin reflection metadata, as libraries can rely on it.
-keep class kotlin.reflect.** { *; }
-keep class org.jetbrains.kotlin.** { *; }
-keep class kotlin.jvm.internal.Intrinsics { *; }