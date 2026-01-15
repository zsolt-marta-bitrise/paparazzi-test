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
        println("\n" + "=".repeat(80))
        println("PAPARAZZI CONTEXT INSPECTION REPORT")
        println("=".repeat(80))
        
        // Check if context is null first
        println("\n### NULL SAFETY CHECKS ###")
        println("Paparazzi instance: ${paparazzi}")
        println("Paparazzi.context is null: ${runCatching { paparazzi.context == null }.getOrElse { e -> "Error accessing context: ${e.message}" }}")
        
        try {
            val context = paparazzi.context
            println("Context successfully retrieved: ${context.javaClass.name}")
            
            // Context Information
            printContextInfo(context)
            
            // Resources Information
            printResourcesInfo(context.resources)
            
            // Configuration Information
            printConfigurationInfo(context.resources.configuration)
            
            // Display Metrics
            printDisplayMetrics(context.resources.displayMetrics)
            
            // Display Information
            printDisplayInfo(context)
            
            // System Properties
            printSystemProperties()
            
            // Environment Variables
            printEnvironmentVariables()
            
            // Build Information
            printBuildInformation()
            
            // File System Information
            printFileSystemInfo(context)
            
            // Paparazzi Specific Information
            printPaparazziInfo()
            
            // Create and snapshot view
            val view = SimpleTestView(context)
            println("\n### VIEW CREATION ###")
            println("View created successfully: ${view.javaClass.name}")
            println("View context: ${view.context?.javaClass?.name ?: "null"}")
            
            println("=".repeat(80))
            println("END OF REPORT")
            println("=".repeat(80) + "\n")
            
            // Take a snapshot to ensure rendering works
            paparazzi.snapshot(view)
            
        } catch (e: Exception) {
            println("\n### CRITICAL ERROR ###")
            println("Failed to access context or complete test: ${e.message}")
            println("Stack trace:")
            e.printStackTrace()
            throw e
        }
    }

    private fun printContextInfo(context: Context?) {
        println("\n### CONTEXT INFORMATION ###")
        if (context == null) {
            println("ERROR: Context is NULL!")
            return
        }
        
        println("Context class: ${context.javaClass.name}")
        println("Application context: ${runCatching { context.applicationContext }.getOrNull()?.let { 
            "class=${it.javaClass.name}, isNull=${it == null}" 
        } ?: "Error accessing application context"}")
        println("Package name: ${runCatching { context.packageName }.getOrNull() ?: "Error accessing package name"}")
        println("Package code path: ${runCatching { context.packageCodePath }.getOrNull() ?: "N/A"}")
        println("Package resource path: ${runCatching { context.packageResourcePath }.getOrNull() ?: "N/A"}")
        
        // Context theme
        println("Theme: ${context.theme}")
        
        // Cache and data directories
        println("Cache dir: ${runCatching { context.cacheDir?.absolutePath }.getOrNull() ?: "N/A"}")
        println("Files dir: ${runCatching { context.filesDir?.absolutePath }.getOrNull() ?: "N/A"}")
        println("External cache dir: ${runCatching { context.externalCacheDir?.absolutePath }.getOrNull() ?: "N/A"}")
    }

    private fun printResourcesInfo(resources: Resources?) {
        println("\n### RESOURCES INFORMATION ###")
        if (resources == null) {
            println("ERROR: Resources is NULL!")
            return
        }
        
        println("Resources class: ${resources.javaClass.name}")
        
        // Try to access asset manager
        val assets = runCatching { resources.assets }.getOrNull()
        println("Assets: ${assets?.javaClass?.name ?: "N/A or Error"}")
        println("Assets is null: ${assets == null}")
        
        // Try to access configuration
        val config = runCatching { resources.configuration }.getOrNull()
        println("Configuration: ${config?.javaClass?.name ?: "N/A or Error"}")
        println("Configuration is null: ${config == null}")
        
        // Locales
        val locales = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            runCatching { resources.configuration.locales.toLanguageTags() }.getOrNull()
        } else {
            runCatching { 
                @Suppress("DEPRECATION")
                resources.configuration.locale.toString() 
            }.getOrNull()
        }
        println("Locales: $locales")
    }

    private fun printConfigurationInfo(config: Configuration?) {
        println("\n### CONFIGURATION INFORMATION ###")
        if (config == null) {
            println("ERROR: Configuration is NULL!")
            return
        }
        
        println("Configuration class: ${config.javaClass.name}")
        println("\n### CONFIGURATION INFORMATION ###")
        println("Configuration class: ${config.javaClass.name}")
        println("Screen layout: ${config.screenLayout}")
        println("Screen width dp: ${config.screenWidthDp}")
        println("Screen height dp: ${config.screenHeightDp}")
        println("Smallest screen width dp: ${config.smallestScreenWidthDp}")
        println("Density dpi: ${config.densityDpi}")
        println("Orientation: ${if (config.orientation == Configuration.ORIENTATION_PORTRAIT) "PORTRAIT" else "LANDSCAPE"}")
        println("UI mode: ${config.uiMode}")
        @Suppress("DEPRECATION")
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
        @Suppress("DEPRECATION")
        println("Scaled density: ${metrics.scaledDensity}")
        println("Xdpi: ${metrics.xdpi}")
        println("Ydpi: ${metrics.ydpi}")
    }

    private fun printDisplayInfo(context: Context?) {
        println("\n### DISPLAY INFORMATION ###")
        if (context == null) {
            println("ERROR: Context is NULL, cannot access display information")
            return
        }
        
        try {
            // Try to get display information via WindowManager
            val windowManager = runCatching { 
                context.getSystemService(Context.WINDOW_SERVICE) 
            }.getOrNull()
            
            println("WindowManager: ${windowManager?.javaClass?.name ?: "null or not accessible"}")
            
            if (windowManager != null) {
                val display = runCatching {
                    windowManager.javaClass.getMethod("getDefaultDisplay").invoke(windowManager)
                }.getOrNull()
                
                println("Default Display: ${display?.javaClass?.name ?: "not accessible"}")
                
                if (display != null) {
                    // Get display ID
                    val displayId = runCatching {
                        display.javaClass.getMethod("getDisplayId").invoke(display)
                    }.getOrNull()
                    println("Display ID: $displayId")
                    
                    // Get display name
                    val displayName = runCatching {
                        display.javaClass.getMethod("getName").invoke(display)
                    }.getOrNull()
                    println("Display Name: $displayName")
                    
                    // Get display state
                    val displayState = runCatching {
                        display.javaClass.getMethod("getState").invoke(display)
                    }.getOrNull()
                    println("Display State: $displayState")
                    
                    // Get rotation
                    val rotation = runCatching {
                        display.javaClass.getMethod("getRotation").invoke(display)
                    }.getOrNull()
                    println("Display Rotation: $rotation")
                    
                    // Get real size
                    val point = runCatching {
                        val pointClass = Class.forName("android.graphics.Point")
                        @Suppress("DEPRECATION")
                        val pointInstance = pointClass.newInstance()
                        display.javaClass.getMethod("getRealSize", pointClass).invoke(display, pointInstance)
                        val x = pointClass.getField("x").get(pointInstance)
                        val y = pointClass.getField("y").get(pointInstance)
                        "($x, $y)"
                    }.getOrElse { "Error: ${it.message}" }
                    println("Real Size: $point")
                }
            }
            
            // Try to access DisplayManager (API 17+)
            val displayManager = runCatching {
                context.getSystemService("display")
            }.getOrNull()
            
            println("\nDisplayManager: ${displayManager?.javaClass?.name ?: "not accessible"}")
            
            if (displayManager != null) {
                val displays = runCatching {
                    displayManager.javaClass.getMethod("getDisplays").invoke(displayManager)
                }.getOrNull()
                
                println("Number of displays: ${if (displays is Array<*>) displays.size else "unknown"}")
            }
            
        } catch (e: Exception) {
            println("Error accessing display information: ${e.message}")
            e.printStackTrace()
        }
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
            "Temp dir" to System.getProperty("java.io.tmpdir")?.let { File(it) },
            "User dir" to System.getProperty("user.dir")?.let { File(it) },
            "User home" to System.getProperty("user.home")?.let { File(it) }
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
        println("Paparazzi instance class: ${paparazzi.javaClass.name}")
        println("Paparazzi class loader: ${paparazzi.javaClass.classLoader?.javaClass?.name ?: "null"}")
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
