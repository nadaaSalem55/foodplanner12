buildscript {
    repositories {
        google()
        mavenCentral()
        dependencies {
            classpath ("com.google.gms:google-services:4.4.2")

            // classpath("com.google.gms:google-services:4.4.1")
            classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.6")
            classpath("com.android.tools.build:gradle:8.5.0")
          // classpath ("com.google.android.gms:play-services-identity:19.0.0") // Ensure this is correct
           // classpath ("com.github.smarteist:autoimageslider:1.4.0") // Mak

        }
        // or any other repository that might host these dependencies
    }
}





// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.5.0" apply false
    alias(libs.plugins.google.gms.google.services) apply false
}
