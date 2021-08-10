plugins {
	id("com.gradle.enterprise").version("3.6.3")
}

include(":jdroid-java-github")

apply(from = java.io.File(settingsDir, "buildCacheSettings.gradle"))
