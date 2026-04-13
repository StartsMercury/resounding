plugins {
    // https://maven.fabricmc.net/fabric-loom/fabric-loom.gradle.plugin/
    alias(libs.plugins.fabric.loom)
    `maven-publish`
}

base {
    group = "dev.thedocruby"
    archivesName = "resounding"
    version = "1.0.0-alpha.11"
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.shedaniel.me/") }
    maven { url = uri("https://maven.terraformersmc.com/releases") }
    maven { url = uri("https://maven.maxhenkel.de/releases") }
}

java {
    toolchain {
        languageVersion = libs.versions.java.map(JavaLanguageVersion::of)
    }

    withSourcesJar()
}

dependencies {
    // to change the versions see the gradle.properties file
    minecraft(libs.minecraft)
    mappings(variantOf(libs.fabric.yarn) { classifier("v2") })
    modImplementation(libs.fabric.loader)

    // Fabric API. This is technically optional, but you probably want it anyway.
    modImplementation(libs.fabric.api)

    // Cloth Config (including Auto Config)
    modApi(libs.cloth.config) {
        exclude(group = "net.fabricmc", module = "fabric-loader")
        exclude(group = "net.fabricmc.fabric-api")
    }

    modImplementation(libs.voicechat.api) {
        exclude(group = "net.fabricmc", module = "fabric-loader")
        exclude(group = "net.fabricmc.fabric-api")
    }

    modImplementation(libs.modmenu) {
        exclude(group = "net.fabricmc", module = "fabric-loader")
        exclude(group = "net.fabricmc.fabric-api")
    }
    // PSA: Some older mods, compiled on Loom 0.2.1, might have outdated Maven POMs.
    // You may need to force-disable transitiveness on them.
}

tasks.withType<ProcessResources> {
    val data = mapOf(
        "version" to version,
    )

    inputs.properties(data)

    filesMatching("fabric.mod.json") {
        expand(data)
    }
}


tasks.withType<JavaCompile> {
    // ensure that the encoding is set to UTF-8, no matter what the system default is
    // this fixes some edge cases with special characters not displaying correctly
    // see http://yodaconditions.net/blog/fix-for-java-file-encoding-problems-with-gradle.html
    // If Javadoc is generated, this must be specified in that task too.
    options.encoding = "UTF-8"
}

tasks.jar {
    from("LICENSE")
}
