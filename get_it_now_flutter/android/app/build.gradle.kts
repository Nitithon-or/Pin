android {
    // ⭐️ แก้ไข 1: เปลี่ยน namespace ให้ตรงกับ Manifest
    namespace = "om.example.getitnow.flutter" 
    compileSdk = flutter.compileSdkVersion
    ndkVersion = flutter.ndkVersion

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    defaultConfig {
        // ⭐️ แก้ไข 2: ใช้เครื่องหมาย = สำหรับ applicationId ใน Kotlin
        applicationId = "om.example.getitnow.flutter" 
        
        // ⭐️ แก้ไข 3: ใช้เครื่องหมาย = สำหรับตัวแปรอื่นๆ ด้วย
        minSdk = flutter.minSdkVersion
        targetSdk = flutter.targetSdkVersion
        versionCode = flutter.versionCode.toInt() // ⚠️ ใช้ .toInt() หรือ .toInteger()
        versionName = flutter.versionName
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}