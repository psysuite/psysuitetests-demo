plugins {
    id(Plugins.androidLibrary) version Versions.androidLibrary apply(false)
    id(Plugins.kotlinAndroid) version Versions.kotlin apply(false)
    id(Plugins.kotlinParcelize) version Versions.kparcelablePlugin apply(false)
    id(Plugins.chaquopy) version Versions.chaquopy apply(false)
}

tasks.register("clean", Delete::class){
    delete(rootProject.buildDir)
}
