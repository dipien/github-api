plugins {
	id("com.gradle.enterprise").version("3.7.1")
}

include(":github-api-java")

apply(from = java.io.File(settingsDir, "buildCacheSettings.gradle"))
