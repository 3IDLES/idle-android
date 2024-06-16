import com.idle.app.configureHiltAndroid
import com.idle.app.configureTest
import com.idle.app.configureKotlinAndroid
import com.idle.app.configureTestAndroid

plugins {
    id("com.android.library")
}

configureKotlinAndroid()
configureTestAndroid()
configureHiltAndroid()