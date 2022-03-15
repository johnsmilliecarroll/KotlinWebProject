import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

val shoppingList = mutableListOf<ListItem>() //create empty shopping list

fun main() {
    embeddedServer(Netty, 9090) { //initialize entry port for our app
        //install needed methods and packages
        install(ContentNegotiation) {
            json()
        }
        install(CORS) {
            //our HTTP CRUD methods
            method(HttpMethod.Get)
            method(HttpMethod.Post)
            method(HttpMethod.Delete)
            anyHost()
        }
        install(Compression) {
            gzip() //for compression purposes
        }
        routing { //routes that support the creation, retrieval, and deletion of ListItems.
            route(ListItem.path) {
                get {
                    call.respond(shoppingList) //responds with the whole shopping list
                }
                post {
                    shoppingList += call.receive<ListItem>()
                    call.respond(HttpStatusCode.OK) //add an entry to the shopping list.
                }
                delete("/{id}") {
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid delete request")
                    shoppingList.removeIf { it.id == id }
                    call.respond(HttpStatusCode.OK) //remove an entry from the shopping list.
                }
            }
            get("/") {
                call.respondText( //at the / webpage, load index.html
                    this::class.java.classLoader.getResource("index.html")!!.readText(),
                    ContentType.Text.Html
                )
            }
            static("/") {
                resources("") //static directory is resources in src/commonMain
            }
        }
    }.start(wait = true)
}