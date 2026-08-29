# Add project specific ProGuard rules here.
# 词库数据使用 Gson 反射解析，需保留数据类
-keep class com.kqe.english.data.** { *; }
-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**
