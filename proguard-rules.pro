# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/rexus/kskv/android-sdk/tools/proguard/proguard-android.txt
# and can be edited to change the default behavior.
# For more information, see
#   developer.android.com/guide/developing/tools/proguard.html

# Keep classes that are referenced in the AndroidManifest.xml
-keepattributes *Annotation*

# Keep classes that extend android.app.Application
-keep public class * extends android.app.Application

# Keep classes that have public constructors
-keepclassmembers class * {
    public <init>(...);
}

# Keep classes that are referenced in XML layouts
-keepclassmembers class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
    public void *(android.view.View);
    public void *(android.view.ViewGroup);
}

# Keep names of classes that are referenced from JNI
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep enum values
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep Parcelable creators
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Keep R.java
-keepclassmembers class **.R$* {
    public static <fields>;
}

# Keep classes that are accessed via reflection
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Xposed specific rules
-keep class de.robv.android.xposed.* { *; }
-keepclassmembers class * {
    @de.robv.android.xposed.XposedInit <methods>;
}

# Gson specific rules
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.example.smsspoof.model.Sender { *; }

# Prevent obfuscation of model classes
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
