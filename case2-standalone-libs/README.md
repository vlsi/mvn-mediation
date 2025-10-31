# Maven Dependency Mediation Demo — Case 2: Standalone Libraries

This README explains only how Case 2 differs from Case 1 and how to run it quickly.

## How Case 2 differs from Case 1
- Case 1 (multi-module) builds `lib-a`, `lib-b`, and `example-application` together in a single reactor.
- Case 2 (this folder) builds the libraries independently from the application. You install `lib-a` and `lib-b` to your local Maven repository first, and the `example-application` then consumes them like regular external dependencies.

Functionally, the dependency mediation problem and its outcomes are the same as in Case 1; only the build layout is different.

For the full background, discussion, and sample outputs, see Case 1’s documentation:
- case1-multi-module/README.md

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
