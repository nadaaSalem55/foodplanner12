plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs")
}

android {
    namespace = "com.example.foodplanner"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.foodplanner"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

        // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))

        // Add the dependency for the Firebase Authentication library
        // When using the BoM, you don't specify versions in Firebase library dependencies
        implementation("com.google.firebase:firebase-auth")

        // Also add the dependency for the Google Play services library and specify its version
        implementation("com.google.android.gms:play-services-auth:21.2.0")

    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("com.google.firebase:firebase-firestore:24.10.1")
  //  implementation("com.google.android.gms:play-services-auth:20.7.0")
 //   implementation("com.google.android.gms:play-services-identity:19.0.0")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Retrofit and Gson
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.12.0")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-rxjava3:2.4.0")

    // SliderView
    //implementation("com.github.smarteist:autoimageslider:1.4.0")

    // CircleImageView
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // Navigation component
    implementation("androidx.navigation:navigation-fragment:2.7.6")
    implementation("androidx.navigation:navigation-ui:2.7.6")

    // RxJava and RxAndroid
    implementation("io.reactivex.rxjava3:rxjava:3.0.2")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")
    implementation("com.squareup.retrofit2:adapter-rxjava3:2.9.0")

    // Lottie
    implementation("com.airbnb.android:lottie:3.7.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
