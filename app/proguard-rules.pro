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
#是否进行混淆(默认进行)
#不进行混淆
-dontobfuscate
#是否压缩（默认进行）
#不进行压缩
-dontshrink
#是否进行优化（默认进行）
#不进行优化
#-dontoptimize

#表示proguard对代码进行迭代优化的次数，Android一般为5
-optimizationpasses 5


