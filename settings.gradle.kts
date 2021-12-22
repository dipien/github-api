plugins {
	id("com.gradle.enterprise").version("3.8")
}

include(":github-api-java")

apply(from = java.io.File(settingsDir, "buildCacheSettings.gradle"))
