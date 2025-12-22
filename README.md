![Logo](./docs/logo.png)
*A simple inventories framework where you're in control*

Demo video: https://www.youtube.com/watch?v=vWEbANOHIVk

- Dynamic inventory updating
- Fully handled click events
- Most logic handled by your code, giving you total control

### Installation
This library is intended for use on Minecraft Paper plugins on version 1.21.8 and onwards. To use this package, you're going to need to add `jitpack` to your build system repositories:

If you're using gradle, put this repository into your `build.gradle.kts`
```kotlin
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}
```

Or if you're using maven, enter this into your `pom.xml`
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Once you have the repository set up, you can add the `invcontrol-core` dependency in your build system

If you use gradle:
```kotlin
dependencies {
    implementation("com.github.29cmb.InvControl:invcontrol-core:v0.1.0-1")
}
```

Or if you use maven:
```xml
<dependency>
    <groupId>com.github.29cmb</groupId>
    <artifactId>InvControl</artifactId>
    <version>v0.1.0-1</version>
</dependency>
```

### Basic Logic
The main concepts of this library are the UI and the Page.
- The UI is the collection of pages that propagates and manages the inventory instance
- The page is responsible for collecting the items to display

Items can be added to pages which get rendered by the UI.

### Examples
Here is an example of a UI and page in its simplest form
```kotlin
fun createInventory(player: Player) {
    // Creates an inventory with 3 rows
    val ui = ChestInventoryUI(
        player,
        title = Component.text("Title"),
        rows = 3
    )
    
    // Makes the main page
    val page = ChestInventoryPage()
    ui.addPage("main", page)
    
    // Adds a feather item
    page.addItem(InventoryItem(
        // The function that gets called every time the view is reloaded
        getItemStack = { page, item ->
            val itemStack = ItemStack.of(Material.FEATHER)
            val meta = itemStack.itemMeta
            
            // Sets the name of the item to "Item"
            meta.itemName(Component.text("Item").color(NamedTextColor.YELLOW))
            itemStack.itemMeta = meta
            
            // Return the ItemStack
            itemStack
        },
        // Sets the slot to 13
        slot = 13
    ))
    
    // Set the active page to the one we just made
    ui.setPage("main")
    
    // Show the UI
    ui.show()
}
```

The reason `getItemStack` is a lambda instead of a single property is so it can be dynamically changed. That function gets invoked whenever the page is initially loaded or whenever it is reloaded by the `reload()` function.

Most of the properties for items are mutable, meaning you can store a reference to an `InventoryItem`, update it, and reload the page to see those changes in effect.

You're also able to create a list of items to be displayed in order using an `InventoryItemMap`, as seen in the example below
```kotlin
val itemMap = InventoryItemMap(
    getInventoryItems = { page, item ->
        val items: ArrayList<InventoryMappedItem> = ArrayList()
        for(player in Bukkit.getOnlinePlayers()) {
            // InventoryMappedItems don't have a slot field
            // They're filled from the `startSlot` with a max of `maxItems`
            items.add(InventoryMappedItem(
                getItemStack = { page, item ->
                    val itemStack = ItemStack.of(Material.PLAYER_HEAD)
                    val meta = itemStack.itemMeta as SkullMeta
                    meta.owningPlayer = player
                    meta.itemName(Component.text(player.name).color(NamedTextColor.YELLOW))
                    itemStack.itemMeta = meta

                    itemStack
                },
                // onClick is still an optional field for MappedItems
                onClick = { page, item -> }
            ))
        }

        // Return the array of `InventoryMappedItem`
        items
    },
    // The first slot (0-based) the items will start from
    startSlot = 0,
    // The max amount of items allowed
    maxItems = 36,
    // The page of items, use `pageForward` and `pageBack` to change this
    // Max page is floor(Total Items / Items Per Page (max items)) + 1
    itemPage = 1,
)
page.addItemMap(itemMap)
// Then you can advance or retract the page using `itemMap.pageForward()` and `itemMap.pageBack()`
// (this does not reload the page, you'll need to use `page.reload()`)
```