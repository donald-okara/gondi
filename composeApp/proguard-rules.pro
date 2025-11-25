# -------------------------
# General warnings to suppress
# -------------------------

# Suppress warnings for Java classes not present on Android but referenced by libraries like Ktor
-dontwarn java.lang.management.**

# Suppress warnings for Reactor BlockHound
-dontwarn reactor.blockhound.**