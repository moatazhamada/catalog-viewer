// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
}

// Detekt configuration (applied at root level)
detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom(file("${project.rootDir}/config/detekt/detekt.yml"))
    val baselineFile = file("${project.rootDir}/config/detekt/baseline.xml")
    if (baselineFile.exists()) {
        baseline = baselineFile
    }
}