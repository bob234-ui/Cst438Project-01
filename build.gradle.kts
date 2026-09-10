// Top-level build file where you can add configuration options common to all sub-projects/modules.
import org.gradle.api.plugins.quality.PmdExtension

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.google.devtools.ksp") version "2.3.11" apply false
    
    // Core Gradle plugins
    id("pmd")
    
    // External static analysis plugins
    id("com.github.spotbugs") version "6.5.11"
    id("net.ltgt.errorprone") version "5.1.1"
}

// Use configure<PmdExtension> to avoid name collision with the 'pmd' plugin accessor
configure<PmdExtension> {
    toolVersion = "7.27.0"
    ruleSetFiles = files("config/pmd/ruleset.xml")
    isIgnoreFailures = false // red build on violations
}

dependencies {
    // Access 'errorprone' configuration by name to avoid unresolved reference in IDE
    "errorprone"("com.google.errorprone:error_prone_core:2.50.0")
}

// CI step: - run: ./gradlew check --no-daemon
