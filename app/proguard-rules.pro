# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# Mantener las clases de MQTT
-keep class org.eclipse.paho.client.mqttv3.** { *; }
-keep class org.eclipse.paho.android.service.** { *; }

# Evitar la eliminación de métodos internos usados por la librería
-dontwarn org.eclipse.paho.client.mqttv3.**
-dontwarn org.eclipse.paho.android.service.**

# Mantener clases relacionadas con la conexión y sockets
-keep class javax.net.** { *; }
-keep class java.net.** { *; }