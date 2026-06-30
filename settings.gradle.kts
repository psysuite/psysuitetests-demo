pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            from(files("../../psysuite/gradle/libs.versions.toml"))
        }
    }
}



include(":core")
project(":core").projectDir = File(settingsDir, "../core/core")

include(":psysuitepython")
project(":psysuitepython").projectDir = File(settingsDir, "../psysuitepython/psysuitepython")

include(":psysuitecore")
project(":psysuitecore").projectDir = File(settingsDir, "../psysuitecore/psysuitecore")

include(":nativeaudio")
project(":nativeaudio").projectDir = File(settingsDir, "../psysuitecore/nativeaudio")

include(":psysuitetests")
