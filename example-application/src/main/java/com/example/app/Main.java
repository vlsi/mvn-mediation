package com.example.app;

import com.example.liba.LibA;
import com.example.libb.LibB;

/**
 * Main application that uses both lib-a and lib-b.
 *
 * This application will fail at runtime due to Maven dependency mediation.
 * Maven will select only one version of Guava and one version of Guice,
 * which means either lib-a or lib-b will fail with NoSuchMethodError or ClassNotFoundException.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Maven Dependency Mediation Demo");
        System.out.println("========================================");
        System.out.println();

        System.out.println("Calling LibA (requires Guava 30.1-jre+ and Guice 5+):");
        System.out.println("----------------------------------------");
        LibA.performOperation();
        System.out.println();

        System.out.println("Calling LibB (requires Guava 33.5.0-jre+ and Guice 3+):");
        System.out.println("----------------------------------------");
        LibB.performOperation();
        System.out.println();

        System.out.println("========================================");
        System.out.println("SUCCESS: Both libraries worked!");
        System.out.println("This means Maven resolved compatible versions.");
        System.out.println("========================================");
    }
}
