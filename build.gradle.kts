
plugins {
    id("com.android.application") version "9.3.1" apply false
    // Kotlin ya viene incluido en AGP 9; esta línea solo fija la versión de Kotlin
    id("org.jetbrains.kotlin.android") version "2.4.21" apply false
    // Reemplazo de kapt compatible con el Kotlin incluido en AGP 9 (lo usa Room)
    id("com.android.legacy-kapt") version "9.3.1" apply false
}