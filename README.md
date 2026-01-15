# Paparazzi Context Inspection Project

This is a minimal Gradle project designed to inspect and debug context-related issues in Paparazzi tests, particularly those that manifest in specific datacenters.

# Paparazzi Context Inspection Project

This is a minimal Gradle project designed to inspect and debug context-related issues in Paparazzi tests, particularly those that manifest in specific datacenters.

## Project Details

- **Gradle Version**: 8.14.3
- **Paparazzi Version**: 1.3.5 (stable)
- **Android Gradle Plugin**: 8.5.2
- **Kotlin**: 2.0.0
- **Project Type**: Android Application
- **Java Version**: 21

## Purpose

This project contains a comprehensive test that prints all context-related information from Paparazzi's renderer library, including:

- Context class information
- Resources and assets configuration
- Display metrics
- Configuration details (locale, density, screen size)
- System properties
- Environment variables
- Build information
- File system details
- Paparazzi-specific classloader information

## Running the Test

To run the context inspection test and see all the output:

```bash
./gradlew :app:testDebugUnitTest --tests "com.example.paparazzitest.ContextInspectionTest.printAllContextInformation" --info
```

Or simply run all tests:

```bash
./gradlew :app:testDebugUnitTest
```

## What to Look For

When debugging datacenter-specific issues, compare the output between different environments:

1. **Locale and Language Settings** - Check `LANG`, `LC_ALL`, and locale configuration
2. **File System Paths** - Verify temp directories and file permissions
3. **Environment Variables** - Look for differences in `HOME`, `TMPDIR`, `PATH`
4. **System Properties** - Check `file.encoding`, `user.timezone`, etc.
5. **Display Metrics** - Ensure density and screen dimensions are consistent
6. **Classloader Information** - Verify classloader hierarchy

## Project Structure

```
paparazzi-test/
├── app/
│   ├── build.gradle.kts
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   └── java/com/example/paparazzitest/
│       │       └── SimpleTestView.kt
│       └── test/
│           └── java/com/example/paparazzitest/
│               └── ContextInspectionTest.kt
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── gradlew
```

## Troubleshooting

If you encounter issues:

1. **Java Version**: Ensure you're using Java 17 or later
2. **Gradle Daemon**: Try `./gradlew --stop` to reset the daemon
3. **Clean Build**: Run `./gradlew clean` before testing
4. **Network Issues**: Check if repositories (Google, Maven Central) are accessible

## Comparing Outputs

To compare outputs between environments:

```bash
# In datacenter A
./gradlew :app:testDebugUnitTest --tests "*.ContextInspectionTest.*" --info > output-datacenter-a.log 2>&1

# In datacenter B
./gradlew :app:testDebugUnitTest --tests "*.ContextInspectionTest.*" --info > output-datacenter-b.log 2>&1

# Compare
diff output-datacenter-a.log output-datacenter-b.log
```

## Key Context Areas to Investigate

Based on common NullPointerException issues in Paparazzi:

1. **Resources/Assets Loading** - Check if resources are being loaded correctly
2. **Locale Configuration** - Ensure locale is properly set
3. **Temp Directory Access** - Verify write permissions to temp directories
4. **Classloader Chain** - Check if the classloader hierarchy is correct
5. **System Fonts** - Font loading can be environment-dependent

## License

This is a test project for debugging purposes.
