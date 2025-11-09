# Navigation 3

- [nav3-recipes](https://github.com/android/nav3-recipes)

https://developer.android.com/guide/navigation/navigation-3?authuser=1&hl=zh-cn

```kotlin
// Define the routes in your app and any arguments.
data object Home
data class Product(val id: String)

// Create a back stack, specifying the route the app should start with.
val backStack = remember { mutableStateListOf<Any>(Home) }

// A NavDisplay displays your back stack. Whenever the back stack changes, the display updates.
NavDisplay(
    backStack = backStack,

    // Specify what should happen when the user goes back
    onBack = { backStack.removeLastOrNull() },

    // An entry provider converts a route into a NavEntry which contains the content for that route.
    entryProvider = { route ->
        when (route) {
            is Home -> NavEntry(route) {
                Column {
                    Text("Welcome to Nav3")
                    Button(onClick = {
                        // To navigate to a new route, just add that route to the back stack
                        backStack.add(Product("123"))
                    }) {
                        Text("Click to navigate")
                    }
                }
            }
            is Product -> NavEntry(route) {
                Text("Product ${route.id} ")
            }
            else -> NavEntry(Unit) { Text("Unknown route: $route") }
        }
    }
)
```

## 开始使用

https://developer.android.com/guide/navigation/navigation-3/get-started?hl=zh-cn&authuser=1