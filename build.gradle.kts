import org.gradle.kotlin.dsl.libs

buildscript {
    dependencies {
        // Add this line
        classpath ("com.google.gms:google-services:4.3.15")
    }
}



// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.google.gms.google.services) apply false
}