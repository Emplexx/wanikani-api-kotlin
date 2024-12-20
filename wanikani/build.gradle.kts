import java.io.FileInputStream
import java.util.Properties

val props = Properties()
props.load(FileInputStream(rootProject.file("local.properties")))

plugins {
	kotlin("multiplatform") version "2.1.0"
	kotlin("plugin.serialization") version "2.1.0"
	id("com.vanniktech.maven.publish") version "0.29.0"
}

group = "moe.emi.wanikani"
version = "0.1.2"

kotlin {
	jvm()
//	iosX64()
	iosArm64()
//	iosSimulatorArm64()
//	linuxX64()

	sourceSets {
		commonMain.dependencies {
			
			val ktor = "3.0.2"
			implementation("io.ktor:ktor-client-core:$ktor")
			implementation("io.ktor:ktor-client-content-negotiation:$ktor")
			implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor")
			implementation("io.ktor:ktor-client-cio:$ktor")
			implementation("io.ktor:ktor-client-logging:$ktor")

			implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
			implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")
		}
		commonTest.dependencies {
			implementation("ch.qos.logback:logback-classic:1.5.12")
			implementation("org.jetbrains.kotlin:kotlin-test:2.1.0")
			implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
			implementation("io.kotest:kotest-assertions-core:5.9.1")
		}
	}
}

tasks.withType<Test>().configureEach {
	useJUnitPlatform()
	environment("token", props["token"]!!)
//	environment("token", properties["wk.token"]!!)
}

mavenPublishing {
//	publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
//	signAllPublications()
	coordinates(group.toString(), "wanikani", version.toString())
	
//	pom {
//		scm {
//
//		}
//	}
}

publishing {
	
	repositories {
		mavenLocal {
			name = "mavenLocal"
		}
	}

}