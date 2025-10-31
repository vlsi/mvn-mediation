# Maven Dependency Mediation Demo

This project demonstrates Maven dependency mediation issues when multiple libraries depend on incompatible versions of transitive dependencies.

## Examples in this repository

This repository contains two separate, self-contained examples that reproduce the same Maven dependency mediation problem using different project layouts:

- Case 1 — Multi‑module project: `case1-multi-module/`
  - Code: [case1-multi-module](case1-multi-module)
  - Demonstrates the issue within a classic multi-module Maven build (parent + modules).

- Case 2 — Standalone libraries and application: `case2-standalone-libs/`
  - Code: [case2-standalone-libs](case2-standalone-libs)
  - Demonstrates the same conflict when libraries are built independently and then consumed by an application.

Both cases exhibit how Maven’s dependency mediation selects incompatible versions depending on declaration order, leading to runtime `NoSuchMethodError`.
