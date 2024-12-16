plugins {
	kotlin("multiplatform") version "2.1.0"
	kotlin("plugin.serialization") version "2.1.0"
}

group = "moe.emi.wanikani"
version = "1.0-SNAPSHOT"

kotlin {
	jvm()
//	iosX64()
//	iosArm64()
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
		}
	}
}

//java {
//	sourceCompatibility = JavaVersion.VERSION_17
//	targetCompatibility = JavaVersion.VERSION_17
//}

//dependencies {
////	implementation("ch.qos.logback:logback-classic:1.5.12")
//}

