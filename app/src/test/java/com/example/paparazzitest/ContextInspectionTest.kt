package com.example.paparazzitest

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import org.junit.Rule
import org.junit.Test
import java.io.File
import java.util.Locale

class ContextInspectionTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5,
        showSystemUi = false
    )

    @Test
    fun printAllContextInformation() {
        val view = SimpleTestView(paparazzi.context)
        
        println("\n" + "=".repeat(80))
        println("PAPARAZZI CONTEXT INSPECTION REPORT")
        println("=".repeat(80))
        
        // Context Information
        printContextInfo(paparazzi.context)
        
        // Resources Information
        printResourcesInfo(paparazzi.context.resources)
        
        // Configuration Information
        printConfigurationInfo(paparazzi.context.resources.configuration)
        
        // Display Metrics
        printDisplayMetrics(paparazzi.context.resources.displayMetrics)
        
        // System Properties
        printSystemProperties()
        
        // Environment Variables
        printEnvironmentVariables()
        
        // Build Information
        printBuildInformation()
        
        // File System Information
        printFileSystemInfo(paparazzi.context)
        
        // Paparazzi Specific Information
        printPaparazziInfo()
        
        println("=".repeat(80))
        println("END OF REPORT")
        println("=".repeat(80) + "\n")
        
        // Take a snapshot to ensure rendering works
        paparazzi.snapshot(view)
    }

    private fun printContextInfo(context: Context) {
        println("\n### CONTEXT INFORMATION ###")
        println("Context class: ${context.javaClass.name}")
        println("Application context class: ${context.applicationContext?.javaClass?.name ?: "null"}")
        println("Package name: ${context.packageName}")
        println("Package code path: ${runCatching { context.packageCodePath }.getOrNull() ?: "N/A"}")
        println("Package resource path: ${runCatching { context.packageResourcePath }.getOrNull() ?: "N/A"}")
        
        // Context theme
        println("Theme: ${context.theme}")
        
        // Cache and data directories
        println("Cache dir: ${runCatching { context.cacheDir?.absolutePath }.getOrNull() ?: "N/A"}")
        println("Files dir: ${runCatching { context.filesDir?.absolutePath }.getOrNull() ?: "N/A"}")
        println("External cache dir: ${runCatching { context.externalCacheDir?.absolutePath }.getOrNull() ?: "N/A"}")
    }

    private fun printResourcesInfo(resources: Resources) {
        println("\n### RESOURCES INFORMATION ###")
        println("Resources class: ${resources.javaClass.name}")
        
        // Try to access asset manager
        println("Assets: ${runCatching { resources.assets.javaClass.name }.getOrNull() ?: "N/A"}")
        
        // Locales
        val locales = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            runCatching { resources.configuration.locales.toLanguageTags() }.getOrNull()
        } else {
            runCatching { resources.configuration.locale.toString() }.getOrNull()
        }
        println("Locales: $locales")
    }

    private fun printConfigurationInfo(config: Configuration) {
        println("\n### CONFIGURATION INFORMATION ###")
        println("Configuration class: ${config.javaClass.name}")
        println("Screen layout: ${config.screenLayout}")
        println("Screen width dp: ${config.screenWidthDp}")
        println("Screen height dp: ${config.screenHeightDp}")
        println("Smallest screen width dp: ${config.smallestScreenWidthDp}")
        println("Density dpi: ${config.densityDpi}")
        println("Orientation: ${if (config.orientation == Configuration.ORIENTATION_PORTRAIT) "PORTRAIT" else "LANDSCAPE"}")
        println("UI mode: ${config.uiMode}")
        println("Locale: ${config.locale}")
        println("Font scale: ${config.fontScale}")
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            println("Locales: ${runCatching { config.locales.toLanguageTags() }.getOrNull() ?: "N/A"}")
        }
    }

    private fun printDisplayMetrics(metrics: DisplayMetrics) {
        println("\n### DISPLAY METRICS ###")
        println("Display metrics class: ${metrics.javaClass.name}")
        println("Width pixels: ${metrics.widthPixels}")
        println("Height pixels: ${metrics.heightPixels}")
        println("Density: ${metrics.density}")
        println("Density dpi: ${metrics.densityDpi}")
        println("Scaled density: ${metrics.scaledDensity}")
        println("Xdpi: ${metrics.xdpi}")
        println("Ydpi: ${metrics.ydpi}")
    }

    private fun printSystemProperties() {
        println("\n### SYSTEM PROPERTIES ###")
        val relevantProperties = listOf(
            "java.version",
            "java.vendor",
            "java.vm.version",
            "java.vm.vendor",
            "java.vm.name",
            "java.runtime.version",
            "os.name",
            "os.arch",
            "os.version",
            "user.name",
            "user.home",
            "user.dir",
            "user.country",
            "user.language",
            "user.timezone",
            "file.encoding",
            "file.separator",
            "path.separator",
            "line.separator",
            "java.io.tmpdir",
            "java.class.path",
            "sun.arch.data.model"
        )
        
        relevantProperties.forEach { prop ->
            val value = System.getProperty(prop)?.let {
                if (it.length > 100) it.take(100) + "..." else it
            } ?: "N/A"
            println("$prop = $value")
        }
    }

    private fun printEnvironmentVariables() {
        println("\n### ENVIRONMENT VARIABLES ###")
        val relevantEnvVars = listOf(
            "HOME",
            "USER",
            "SHELL",
            "PATH",
            "LANG",
            "LC_ALL",
            "TMPDIR",
            "JAVA_HOME",
            "ANDROID_HOME",
            "ANDROID_SDK_ROOT"
        )
        
        relevantEnvVars.forEach { envVar ->
            val value = System.getenv(envVar)?.let {
                if (it.length > 100) it.take(100) + "..." else it
            } ?: "N/A"
            println("$envVar = $value")
        }
        
        println("\nAll environment variables count: ${System.getenv().size}")
    }

    private fun printBuildInformation() {
        println("\n### BUILD INFORMATION ###")
        println("Build.VERSION.SDK_INT: ${Build.VERSION.SDK_INT}")
        println("Build.VERSION.RELEASE: ${Build.VERSION.RELEASE}")
        println("Build.MANUFACTURER: ${Build.MANUFACTURER}")
        println("Build.MODEL: ${Build.MODEL}")
        println("Build.BRAND: ${Build.BRAND}")
        println("Build.DEVICE: ${Build.DEVICE}")
        println("Build.PRODUCT: ${Build.PRODUCT}")
        println("Build.HARDWARE: ${Build.HARDWARE}")
        println("Build.ID: ${Build.ID}")
        println("Build.TYPE: ${Build.TYPE}")
        println("Build.TAGS: ${Build.TAGS}")
        println("Build.FINGERPRINT: ${Build.FINGERPRINT}")
    }

    private fun printFileSystemInfo(context: Context) {
        println("\n### FILE SYSTEM INFORMATION ###")
        
        val dirsToCheck = mapOf(
            "Cache dir" to runCatching { context.cacheDir }.getOrNull(),
            "Files dir" to runCatching { context.filesDir }.getOrNull(),
            "Temp dir" to File(System.getProperty("java.io.tmpdir")),
            "User dir" to File(System.getProperty("user.dir")),
            "User home" to File(System.getProperty("user.home"))
        )
        
        dirsToCheck.forEach { (name, dir) ->
            println("\n$name:")
            if (dir != null && dir.exists()) {
                println("  Path: ${dir.absolutePath}")
                println("  Exists: true")
                println("  Is directory: ${dir.isDirectory}")
                println("  Can read: ${dir.canRead()}")
                println("  Can write: ${dir.canWrite()}")
                println("  Total space: ${dir.totalSpace / 1024 / 1024} MB")
                println("  Free space: ${dir.freeSpace / 1024 / 1024} MB")
            } else {
                println("  Path: ${dir?.absolutePath ?: "null"}")
                println("  Exists: false")
            }
        }
    }

    private fun printPaparazziInfo() {
        println("\n### PAPARAZZI SPECIFIC INFORMATION ###")
        println("Paparazzi class loader: ${Paparazzi::class.java.classLoader?.javaClass?.name ?: "null"}")
        println("Test class loader: ${this.javaClass.classLoader?.javaClass?.name ?: "null"}")
        
        // Try to get the current thread info
        val currentThread = Thread.currentThread()
        println("Current thread: ${currentThread.name}")
        println("Thread group: ${currentThread.threadGroup?.name ?: "null"}")
        println("Thread context class loader: ${currentThread.contextClassLoader?.javaClass?.name ?: "null"}")
        
        // Check for any Paparazzi-specific system properties
        println("\nPaparazzi-related system properties:")
        System.getProperties().stringPropertyNames()
            .filter { it.contains("paparazzi", ignoreCase = true) || it.contains("layoutlib", ignoreCase = true) }
            .forEach { prop ->
                val value = System.getProperty(prop)?.let {
                    if (it.length > 100) it.take(100) + "..." else it
                } ?: "N/A"
                println("  $prop = $value")
            }
    }
}
