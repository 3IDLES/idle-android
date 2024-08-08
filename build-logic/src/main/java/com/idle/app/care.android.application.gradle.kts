import com.idle.app.configureHiltAndroid
import com.idle.app.configureKotlinAndroid
import com.idle.app.configureTestAndroid

plugins {
    id("com.android.application")
}

configureKotlinAndroid()
configureHiltAndroid()
configureTestAndroid()