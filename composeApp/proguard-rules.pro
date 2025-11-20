# Suppress warnings for missing Java Management Extensions (JMX) classes
# that are not available on Android but are referenced by libraries like Ktor.
-dontwarn java.lang.management.**

# Suppress warnings for Reactor BlockHound, which is also not part of Android.
-dontwarn reactor.blockhound.**
