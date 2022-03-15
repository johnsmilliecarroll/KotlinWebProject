import kotlinx.serialization.Serializable

/**
 * A class for the items in our list
 */
@Serializable //allows us to define our models directly in common code
data class ListItem(val desc: String, val priority: Int) {
    val id: Int = desc.hashCode() //generates an item id

    companion object {
        const val path = "/shoppingList"
    }
}