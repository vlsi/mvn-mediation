package com.example.liba;

import com.google.common.collect.ImmutableList;
import com.google.inject.Key;

import javax.inject.Named;

/**
 * Library A that requires Guava 30.1-jre+ and Guice 5+
 *
 * Uses features specific to these versions:
 * - Guava 30.1-jre: Basic ImmutableList features
 * - Guice 5: Key.withAnnotations introduced in Guice 5.x
 */
public class LibA {

    public static void performOperation() {
        System.out.println("LibA: Starting operation...");

        // Use Guava 30.1-jre features: basic ImmutableList
        ImmutableList<String> list = ImmutableList.of("lib-a", "requires", "guava", "30.1-jre");

        System.out.println("LibA: Created immutable list using Guava 30.1-jre toImmutableList(): " + list);

        Key<String> key = Key.get(String.class);
        // Use Guice 5 features
        Key<String> withAnnotation = key.withAnnotation(Named.class);
        System.out.println("LibA: Guice 5 Key.get(String).WithAnnotation(@Named): " + withAnnotation);

        System.out.println("LibA: Operation completed successfully!");
    }
}
