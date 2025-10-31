package com.example.libb;

import com.google.common.math.IntMath;
import com.google.inject.Key;

/**
 * Library B that requires Guava 33.5.0-jre+ and Guice 3+
 *
 * Uses features specific to these versions:
 * - Guava 33.5.0-jre: IntMath.saturatedAdd in Guava 33.5.0-jre
 * - Guice 3: Basic Guice features introduced in 3.x
 */
public class LibB {

    public static void performOperation() {
        System.out.println("LibB: Starting operation...");

        // Use Guava 33.5.0-jre+ feature: toImmutableList() collector
        // IntMath.saturatedAbs was added in Guava 33.5.0-jre and is NOT available previously
        int saturatedAbs = IntMath.saturatedAbs(Integer.MIN_VALUE);

        System.out.println("LibB: IntMath.saturatedAbs(Integer.MIN_VALUE) using Guava 33: " + saturatedAbs);

        // Use Guice 3 features
        Key<String> key = Key.get(String.class);
        System.out.println("LibB: Guice 3 Key.get(String): " + key);

        System.out.println("LibB: Operation completed successfully!");
    }
}
