# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interfaces
# class:
#-keepclassmembers class fqcn.of.javascript.interfaces.for.webview {
#   public *;
#}

-optimizationpasses 5                   # 指定代码的压缩级别
-dontpreverify                          # 不做预校验
-dontusemixedcaseclassnames             # 混淆时不使用大小写混合，混淆后的类名为小写
-dontskipnonpubliclibraryclasses        # 指定不去忽略非公共的库的类
-dontskipnonpubliclibraryclassmembers   # 指定不去忽略非公共的库的类的成员

-verbose                                # 混淆时是否记录日志
-printmapping proguardMapping.txt

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#继承activity,application,service,broadcastReceiver,contentprovider....不进行混淆
 -keep public class * extends android.app.Activity
 -keep public class * extends android.app.Application
 -keep public class * extends android.support.multidex.MultiDexApplication
 -keep public class * extends android.app.Service
 -keep public class * extends android.content.BroadcastReceiver
 -keep public class * extends android.content.ContentProvider
 -keep public class * extends android.app.backup.BackupAgentHelper
 -keep public class * extends android.preference.Preference
 -keep public class * extends android.view.View
 -keep class android.support.** {*;}   ## 保留support下的所有类及其内部类

 # 保留继承的
 -keep public class * extends android.support.v4.**
 -keep public class * extends android.support.v7.**
 -keep public class * extends android.support.annotation.**


-keepattributes *Annotation*        #保持注解

-keep public class * extends android.widget.BaseAdapter {*;}

# 保留在Activity中的方法参数是view的方法，
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

# 保留自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View {
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 枚举类不能被混淆
-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}

# 保留Parcelable序列化的类不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# 保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 对于R（资源）下的所有类及其方法，都不能被混淆
-keep class **.R$* {
    *;
}

# 对于带有回调函数onXXEvent的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
}


-keep class cn.pedant.SweetAlert.** { *;}

-keep class com.shwangce.nt10g.client.library.** {*;}
#butterknife不混淆
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
#okio
-dontwarn okio.**
-keep class okio.**{*;}
-keep interface okio.**{*;}

#retrofit2
-dontnote retrofit2.Platform
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
-dontwarn retrofit2.Platform$Java8

-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

#rx
-dontwarn rx.**
-keep class rx.**{*;}

#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}





