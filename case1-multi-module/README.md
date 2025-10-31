# Maven Dependency Mediation Demo

This project demonstrates Maven dependency mediation issues when multiple libraries depend on incompatible versions of transitive dependencies.

## Project Structure

```
mvn-mediation-demo/
├── lib-a/          # Third-party library A
├── lib-b/          # Third-party library B
└── example-application/  # Your application using both libraries
```

## The Problem

The repository reproduces the case described in https://issues.apache.org/jira/browse/MNG-7852?focusedCommentId=17748217&page=com.atlassian.jira.plugin.system.issuetabpanels:comment-tabpanel#comment-17748217

### Setup

- **lib-a** depends on:
  - Guava 30.1-jre
  - Guice 5.0.1 (uses `Key.withAnnotation(Class)` added in 5.0.1)

- **lib-b** depends on:
  - Guava 33.5.0-jre (uses `IntMath.saturatedAbs(int)` collector added in 33.5.0-jre)
  - Guice 3.0

- **example-application** depends on both lib-a and lib-b

### What Happens

Maven's dependency mediation algorithm selects different versions depending on declaration order:
- **Guava 30.1-jre** and **Guice 5.0.1** (if `lib-a` is declared first)
- **Guava 33.5.0-jre** and **Guice 3.0** (if `lib-b` is declared first)

Both combinations are incompatible and will fail at runtime:
- Guava 30.1-jre + Guice 5.0.1 fails because `lib-b` needs Guava 33.5.0-jre
- Guava 33.5.0-jre + Guice 3 fails because `lib-a` needs Guice 5.0.1

## Suggestion

Maven should consider all versions, not just the nearest.

**Motivation**: In most cases, resolving to a newer version is the correct choice.
Using an older version often leads to runtime failures.
If users want to use an older version, they should use explicit syntax to convey that intention.

## Discussion

* https://github.com/apache/maven/issues/9070
* https://issues.apache.org/jira/browse/MNG-7852

## Running the Demo

```bash
./mvnw verify
```

### When `lib-a` is Declared First

```
[INFO] com.example:example-application:jar:1.0-SNAPSHOT
[INFO] +- com.example:lib-a:jar:1.0-SNAPSHOT:compile
[INFO] |  +- com.google.guava:guava:jar:30.1-jre:compile
[INFO] |  |  +- com.google.guava:failureaccess:jar:1.0.1:compile
[INFO] |  |  +- com.google.guava:listenablefuture:jar:9999.0-empty-to-avoid-conflict-with-guava:compile
[INFO] |  |  +- com.google.code.findbugs:jsr305:jar:3.0.2:compile
[INFO] |  |  +- org.checkerframework:checker-qual:jar:3.5.0:compile
[INFO] |  |  +- com.google.errorprone:error_prone_annotations:jar:2.3.4:compile
[INFO] |  |  \- com.google.j2objc:j2objc-annotations:jar:1.3:compile
[INFO] |  \- com.google.inject:guice:jar:5.0.1:compile
[INFO] |     +- javax.inject:javax.inject:jar:1:compile
[INFO] |     \- aopalliance:aopalliance:jar:1.0:compile
[INFO] \- com.example:lib-b:jar:1.0-SNAPSHOT:compile
```

```
[INFO] [stdout] ========================================
[INFO] [stdout] Maven Dependency Mediation Demo
[INFO] [stdout] ========================================
[INFO] [stdout]
[INFO] [stdout] Calling LibA (requires Guava 30.1-jre+ and Guice 5+):
[INFO] [stdout] ----------------------------------------
[INFO] [stdout] LibA: Starting operation...
[INFO] [stdout] LibA: Created immutable list using Guava 30.1-jre toImmutableList(): [lib-a, requires, guava, 30.1-jre]
[INFO] [stdout] LibA: Guice 5 Key.get(String).WithAnnotation(@Named): Key[type=java.lang.String, annotation=@com.google.inject.name.Named]
[INFO] [stdout] LibA: Operation completed successfully!
[INFO] [stdout]
[INFO] [stdout] Calling LibB (requires Guava 33.5.0-jre+ and Guice 3+):
[INFO] [stdout] ----------------------------------------
[INFO] [stdout] LibB: Starting operation...
[WARNING]
java.lang.Exception: The specified mainClass doesn't contain a main method with appropriate signature.
    at org.codehaus.mojo.exec.ExecJavaMojo$1.run(ExecJavaMojo.java:283)
    at java.lang.Thread.run(Thread.java:840)
Caused by: java.lang.NoSuchMethodError: 'int com.google.common.math.IntMath.saturatedAbs(int)'
    at com.example.libb.LibB.performOperation(LibB.java:20)
    at com.example.app.Main.main(Main.java:28)
    at org.codehaus.mojo.exec.ExecJavaMojo$1.run(ExecJavaMojo.java:279)
    at java.lang.Thread.run(Thread.java:840)
```

### When `lib-b` is Declared First

```
[INFO] com.example:example-application:jar:1.0-SNAPSHOT
[INFO] +- com.example:lib-b:jar:1.0-SNAPSHOT:compile
[INFO] |  +- com.google.guava:guava:jar:33.5.0-jre:compile
[INFO] |  |  +- com.google.guava:failureaccess:jar:1.0.3:compile
[INFO] |  |  +- com.google.guava:listenablefuture:jar:9999.0-empty-to-avoid-conflict-with-guava:compile
[INFO] |  |  +- org.jspecify:jspecify:jar:1.0.0:compile
[INFO] |  |  +- com.google.errorprone:error_prone_annotations:jar:2.41.0:compile
[INFO] |  |  \- com.google.j2objc:j2objc-annotations:jar:3.1:compile
[INFO] |  \- com.google.inject:guice:jar:3.0:compile
[INFO] |     +- javax.inject:javax.inject:jar:1:compile
[INFO] |     \- aopalliance:aopalliance:jar:1.0:compile
[INFO] \- com.example:lib-a:jar:1.0-SNAPSHOT:compile
```

```
[INFO] [stdout] ========================================
[INFO] [stdout] Maven Dependency Mediation Demo
[INFO] [stdout] ========================================
[INFO] [stdout]
[INFO] [stdout] Calling LibA (requires Guava 30.1-jre+ and Guice 5+):
[INFO] [stdout] ----------------------------------------
[INFO] [stdout] LibA: Starting operation...
[INFO] [stdout] LibA: Created immutable list using Guava 30.1-jre toImmutableList(): [lib-a, requires, guava, 30.1-jre]
[WARNING]
java.lang.Exception: The specified mainClass doesn't contain a main method with appropriate signature.
    at org.codehaus.mojo.exec.ExecJavaMojo$1.run(ExecJavaMojo.java:283)
    at java.lang.Thread.run(Thread.java:840)
Caused by: java.lang.NoSuchMethodError: 'com.google.inject.Key com.google.inject.Key.withAnnotation(java.lang.Class)'
    at com.example.liba.LibA.performOperation(LibA.java:27)
    at com.example.app.Main.main(Main.java:23)
    at org.codehaus.mojo.exec.ExecJavaMojo$1.run(ExecJavaMojo.java:279)
    at java.lang.Thread.run(Thread.java:840)
```

## Checking Resolved Dependencies

To see which versions Maven selected:

```bash
./mvnw dependency:tree -pl example-application
```

Output shows:
```
[INFO] com.example:example-application:jar:1.0-SNAPSHOT
[INFO] +- com.example:lib-a:jar:1.0-SNAPSHOT:compile
[INFO] |  +- com.google.guava:guava:jar:30.1-jre:compile
[INFO] |  |  +- com.google.guava:failureaccess:jar:1.0.1:compile
[INFO] |  |  +- com.google.guava:listenablefuture:jar:9999.0-empty-to-avoid-conflict-with-guava:compile
[INFO] |  |  +- com.google.code.findbugs:jsr305:jar:3.0.2:compile
[INFO] |  |  +- org.checkerframework:checker-qual:jar:3.5.0:compile
[INFO] |  |  +- com.google.errorprone:error_prone_annotations:jar:2.3.4:compile
[INFO] |  |  \- com.google.j2objc:j2objc-annotations:jar:1.3:compile
[INFO] |  \- com.google.inject:guice:jar:5.0.1:compile
[INFO] |     +- javax.inject:javax.inject:jar:1:compile
[INFO] |     \- aopalliance:aopalliance:jar:1.0:compile
[INFO] \- com.example:lib-b:jar:1.0-SNAPSHOT:compile
```

Note: lib-b's dependencies were mediated out - only lib-a's versions (Guava 30.1-jre and Guice 5.0.1) were selected.

## Maven Version

This demo uses Maven 4.0.0-SNAPSHOT via the Maven wrapper (`./mvnw`).

## Key Takeaways

1. **Maven's "nearest wins" strategy** causes `NoSuchMethodError` due to version conflicts
2. **Declaration order matters** - changing dependency order produces different runtime failures
3. **Transitive dependency conflicts** can cause runtime failures even when compilation succeeds
4. **You have no control** over the dependency versions chosen by third-party libraries
5. This is a real-world scenario that application developers face regularly

## Potential Solutions

1. **Dependency Management**: Use `<dependencyManagement>` to explicitly control versions
2. **Exclusions**: Exclude problematic transitive dependencies and add explicit dependencies
3. **Bill of Materials (BOM)**: Import a BOM that defines compatible versions
4. **Maven Enforcer Plugin**: Detect version conflicts at build time
5. **Wait for better dependency resolution** in future Maven versions

## Known workarounds

Maven 4 has undocumented features that allow to resolve the highest versions:

```bash
cd example-application
 ./mvnw verify -Daether.conflictResolver.versionSelector.selectionStrategy=highest -Daether.dependencyCollector.bf.skipper=versioned 
```
