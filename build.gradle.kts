plugins {
    id(Plugins.androidLibrary) version Versions.androidLibrary apply(false)
    id(Plugins.kotlinAndroid) version Versions.kotlin apply(false)
    id(Plugins.kotlinParcelize) version Versions.kparcelablePlugin apply(false)
    id(Plugins.chaquopy) version Versions.chaquopy apply(false)
}

ext["javaVersion"] = libs.versions.javaVersion.get()

tasks.register("clean", Delete::class){
    description = "Deletes the build directory"
    delete(rootProject.layout.buildDirectory)
}
