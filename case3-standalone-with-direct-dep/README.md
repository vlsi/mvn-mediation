# Maven Dependency Mediation Demo — Case 3: Standalone Libraries + Direct Guice dependency

The example application in case 3 adds a direct dependency on Guice 3.0.

The use case is as follows:
- User wanted Guice for their application, so they added guice:3.0
- User also wanted to use lib-a and lib-b, so they added them as direct dependencies
- Maven resolved the dependencies, but they were incompatible with each other

## Run the demo (fast path)
```bash
./test.sh
```
This script builds and installs `lib-a` and `lib-b`, then runs the example application to demonstrate the mediation behavior.

## Manual commands (optional)
If you prefer to run steps yourself:
```bash
( cd lib-a && ./mvnw -q -DskipTests install )
( cd lib-b && ./mvnw -q -DskipTests install )
( cd example-application && ./mvnw verify )
```

Tip: Use `dependency:tree` in the application to inspect resolved versions, as shown in Case 1’s README.

## Known workarounds

The only known workaround is
1. Explicitly bump Guice to 5.x in the application pom.xml
2. And add Maven 4 undocumented features that allow resolving the highest versions:

```bash
cd example-application
 ./mvnw verify -Daether.conflictResolver.versionSelector.selectionStrategy=highest -Daether.dependencyCollector.bf.skipper=versioned 
```
