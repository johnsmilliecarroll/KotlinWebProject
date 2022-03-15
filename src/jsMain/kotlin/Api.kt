import io.ktor.http.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import kotlinx.browser.window

val endpoint = window.location.origin // only needed until https://youtrack.jetbrains.com/issue/KTOR-453 is resolved

val jsonClient = HttpClient {
    install(JsonFeature) { serializer = KotlinxSerializer() }
    //the JsonFeature serialization out data to create typesafe HTTP requests
}
/**
 * Returns entire list
 */
suspend fun getShoppingList(): List<ListItem> {
    return jsonClient.get(endpoint + ListItem.path)
}
/**
 * Adds item to the shopping list
 */
suspend fun addShoppingListItem(shoppingListItem: ListItem) {
    jsonClient.post<Unit>(endpoint + ListItem.path) {
        contentType(ContentType.Application.Json)
        body = shoppingListItem
    }
}
