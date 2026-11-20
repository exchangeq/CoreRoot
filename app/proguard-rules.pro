# CoreRoot ProGuard / R8 规则

# 保留 JNI 桥接类及其原生方法名（C 层按符号查找）
-keepclassmembers class io.coreroot.manager.nat.NativeBridge {
    native <methods>;
}

# 保留数据模型（序列化 / 反射读取字段）
-keep class io.coreroot.manager.data.** { *; }

# Kotlin 元数据
-keepattributes *Annotation*, InnerClasses, Signature, Exceptions

# 移除日志（release）
-assumenosideeffects class android.util.Log {
    public static int d(...);
    public static int v(...);
    public static int i(...);
}
