import com.idle.app.configureHiltAndroid
import com.idle.app.configureTestAndroid
import com.idle.app.configureKotlinAndroid

plugins {
    id("com.android.application")
}

configureKotlinAndroid()
configureHiltAndroid()
configureTestAndroid()