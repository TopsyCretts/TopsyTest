# 📬 Messenger Dynamic Feature — KMP + TDLib + Decompose + Koin

Telegram-клиентская авторизация на базе **Kotlin Multiplatform & Compose Multiplatform** с использованием **TDLib**, модульной архитектуры и общего интерфейса `MessengerClient`. Поддерживаются **Android** и **iOS**, с динамической загрузкой на Android и обычным подключением на iOS. Реализация состояния авторизации через `Decompose.Value`, DI через Koin, Material 3 UI.

---

## 📦 Структура проекта

```
composeApp/ # Основной KMP модуль
iosApp/ # iOS Swift Xcode модуль
messengerDynamicFeature/
├── api/            # Общие интерфейсы (MessengerClient и Messenger)
├── compose-api/    
├── messengerImpl/  # Реализации клиента для Android и iOS
```

---

## 🧰 Используемые технологии

| Компонент        | Назначение |
|------------------|------------|
| 🧱 Decompose      | Навигация и Value<T> для UI состояний |
| ⚙️ Koin           | DI-контейнер для проброса реализаций |
| 🎨 Material 3     | Интерфейс через Material Theme Builder |
| 🧵 Kotlin MPP     | Общий код для Android и iOS |
| 🛠 TDLib          | Telegram Database Library |

---

## 🤖 Android

- `messengerImpl` реализует `MessengerClient` через JNI TDLib.
- `.so` файлы TDLib собраны вручную и находятся в:
  ```
  messengerDynamicFeature/messengerImpl/src/androidMain/jniLibs/...
  ```
- Java-обёртки (`Client.java`, `TdApi.java`) из `tdlib-java` находятся в:
  ```
  messengerImpl/src/androidMain/java/org/drinkless/tdlib/
  ```
- Эти Java-файлы используются в Kotlin-коде для вызова TDLib.

- Фича подключена как **Dynamic Feature Module**.

---

## 🍏 iOS

- Используется [TDLibKit](https://github.com/Swiftgram/TDLibKit) через Swift Package Manager.
- Реализация `MessengerClientImpl.swift` оборачивает TDLibKit и реализует интерфейс MessengerClient.

> 💡 Swift bridge (`expect/actual` или `cinterop`) **пока не настроен**, но реализация готова для проброса через `@objc`.

---

## 🔗 Общий интерфейс `MessengerClient`

```kotlin
interface MessengerClient {
    val authState: Value<AuthenticationState>
    suspend fun insertPhoneNumber(number: String)
    suspend fun insertCode(code: String)
    suspend fun insertPassword(password: String)
    fun close()

    sealed class AuthenticationState {
        object UNAUTHENTICATED : AuthenticationState()
        object UNKNOWN : AuthenticationState()
        object WAIT_FOR_NUMBER : AuthenticationState()
        object WAIT_FOR_CODE : AuthenticationState()
        object WAIT_FOR_PASSWORD : AuthenticationState()
        object AUTHENTICATED : AuthenticationState()
    }
}
```


------

## ⚙️ Конфигурация API ID и Hash

Перед запуском, добавьте ваши Telegram API ID и Hash:

### 🔧 Android

Откройте `gradle.properties` и используйте свои credentials:

```properties
# Put your values, check: https://core.telegram.org/api/obtaining_api_id
tdApiId=0
tdApiHash=YourHash
```

### 🍏 iOS

Откройте `Config.xcconfig` и используйте свои credentials:

```xcconfig
// Put your values, check: https://core.telegram.org/api/obtaining_api_id
TDLIB_API_ID = 0
TDLIB_API_HASH = yourHash
```

Убедитесь, что они также проброшены в `Info.plist` (iOS) и `BuildConfig` (Android), если используете эти методы.

---

## 🛠 Возможнные улучшения

- [ ] Подключить Swift bridge через CocoaPods или cinterop
- [ ] Добавить общие KMP Unit тесты

---

