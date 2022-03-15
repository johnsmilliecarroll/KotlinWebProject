import react.*
import kotlinx.coroutines.*
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ul
import react.dom.html.ReactHTML.img
import react.dom.html.ReactHTML.button
import react.css.css
import csstype.Position
import csstype.px
import kotlin.random.Random

private val scope = MainScope() //scope of our app

var xpos = 100 //start position of character
var ypos = 100
val movebuffer = 50 //amount of pixel character moves with each step
//all the food items
val items = listOf("Cucumbers 🥒","Tomatoes 🍅" ,"Orange Juice 🍊", "Pizza 🍕")
val randlocations = listOf( //random locations of food items
    (Random.nextInt(1, 28)* 50), (Random.nextInt(1, 18)* 50),
    (Random.nextInt(1, 28)* 50), (Random.nextInt(1, 18)* 50),
    (Random.nextInt(1, 28)* 50), (Random.nextInt(1, 18)* 50),
    (Random.nextInt(1, 28)* 50), (Random.nextInt(1, 18)* 50),
)
val itemcollect = mutableListOf(false, false, false, false) //bools for if item is collected

//create the app
val App = FC<Props> {
    var shoppingList by useState(emptyList<ListItem>()) //init shopping list

    //launch the scope
    useEffectOnce {
        scope.launch {
            shoppingList = getShoppingList() //get empty shopping list
        }
    }


    fun move(x: Int, y: Int){ //function used to move the character
        xpos+=x
        ypos+=y
        for (i in 0..3) //for all the food items...
        {
            //check if character's current location is the same as a food item
            if(xpos == randlocations[i*2] && ypos == randlocations[i*2+1]){
                itemcollect[i] = true //if so, set item collected to true
                scope.launch {
                    //add new item to list and refresh the scope
                    addShoppingListItem(ListItem(items[i], 1))
                    shoppingList = getShoppingList()
                }
            }
            else {
                //refresh scope so character can move.
                scope.launch {
                    shoppingList = getShoppingList()
                }
            }
        }
    }

    h1 { //header title
        +"Full-Stack Shopping Quest"
    }
    ul { //shopping list, sorted by priority value
        shoppingList.sortedByDescending(ListItem::priority).forEach { item ->
            li {
                key = item.toString()
                +"[${item.priority}] ${item.desc} " //display items as such
            }
        }
    }
    //image for our character
    img {
        src = "tangela.png"
        css {
            position = Position.absolute
            top = ypos.px //position image with ypos and xpos
            right = xpos.px
        }
    }

    //food images:
    if(!itemcollect[0]) { //if item not collected, display it
        img {
            src = "cucumber.png"
            css {
                position = Position.absolute
                top = randlocations[1].px //display item at its location
                right = randlocations[0].px
            }
        }
    }
    if(!itemcollect[1]) {
        img {
            src = "tomato.png"
            css {
                position = Position.absolute
                top = randlocations[3].px
                right = randlocations[2].px
            }
        }
    }
    if(!itemcollect[2]) {
        img {
            src = "orange.png"
            css {
                position = Position.absolute
                top = randlocations[5].px
                right = randlocations[4].px
            }
        }
    }
    if(!itemcollect[3]) {
        img {
            src = "pizza.png"
            css {
                position = Position.absolute
                top = randlocations[7].px
                right = randlocations[6].px
            }
        }
    }

    //movement buttons
    button {
        +"Move Left"
        css {
            position = Position.relative
            left = 30.px
            top = 30.px
        }
        onClick = {move(movebuffer,0)} //when clicked, move the step amount in the correct direction
    }
    button {
        +"Move Up"
        css {
            position = Position.relative
            left = 30.px
        }
        onClick = {move(0,-movebuffer,)}
    }
    button {
        +"Move Down"
        css {
            position = Position.relative
            left = 30.px
            top = 60.px
        }
        onClick = {move(0,movebuffer)}
    }
    button {
        +"Move Right"
        css {
            position = Position.relative
            left = 30.px
            top = 30.px
        }
        onClick = {move(-movebuffer,0)}
    }
}


