plugins {
	id("com.gradle.enterprise").version("3.6.3")
}

include(":github-api-java")

apply(from = java.io.File(settingsDir, "buildCacheSettings.gradle"))
