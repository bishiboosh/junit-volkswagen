Volkswagen for JUnit4
================

[![Release](https://img.shields.io/github/release/bishiboosh/junit-volkswagen.svg?label=maven)](https://jitpack.io/#bishiboosh/junit-volkswagen)

Volkswagen for JUnit4 makes failing test cases pass in CI servers.

This project is inspired by [volkswagen](https://github.com/auchenberg/volkswagen) for javascript, and [Volkswagen-Xcode](https://github.com/cezheng/Volkswagen-Xcode) for XCode.

Installation
------------

Follow the instructions on [Jitpack](https://jitpack.io/#jitpack/gradle-simple) to use with your favorite build system.

Usage
-----

Add this at the top of your JUnit4 class :

    @RunWith(VolkswagenTestRunner.class)