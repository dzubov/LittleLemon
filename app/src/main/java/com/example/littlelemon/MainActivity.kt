package com.example.littlelemon

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.littlelemon.ui.theme.AppDatabase
import com.example.littlelemon.ui.theme.LittleLemonTheme
import com.example.littlelemon.ui.theme.MenuItemRoom
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import io.ktor.http.ContentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import coil.compose.rememberAsyncImagePainter

class MainActivity : ComponentActivity() {

    private val httpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(contentType = ContentType("text", "plain"))
        }
    }

    private suspend fun fetchMenu(): List<MenuItemNetwork> {
        val url = "https://raw.githubusercontent.com/Meta-Mobile-Developer-PC/Working-With-Data-API/main/menu.json"
        val menuNetwork = httpClient.get(url).body<MenuNetworkdata>()
        return menuNetwork.menu
        // TODO("Retrieve data")
        // data URL: https://raw.githubusercontent.com/Meta-Mobile-Developer-PC/Working-With-Data-API/main/littleLemonSimpleMenu.json
    }

    private val database by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database").build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LittleLemonTheme {
                AppNavigation()
            }
        }
        Log.e("Tag", "Okay 1")
        lifecycleScope.launch(Dispatchers.IO) {
            if (database.menuItemDao().isEmpty()) {
                // add code here
                saveMenuToDatabase(fetchMenu())
            }
        }
        Log.e("Tag", "Okay 2")
    }

    private fun saveMenuToDatabase(menuItemsNetwork: List<MenuItemNetwork>) {
        val menuItemsRoom = menuItemsNetwork.map { it.toMenuItemRoom() }
        database.menuItemDao().insertAll(*menuItemsRoom.toTypedArray())
    }

    @Composable
    fun AppNavigation() {
        //Here get enter string value from sharedPreferences other activity
        /*                val sharedPreferences = getSharedPreferences("LoginInfo", MODE_PRIVATE)
                        with(sharedPreferences.edit()) {
                            putString("DataInTheSystem", "0")  // "0" Unsuccessful
                            apply()
                        }
        */
        val sharedPreferences1 = getSharedPreferences("LoginInfo", MODE_PRIVATE)
        val string = sharedPreferences1.getString("DataInTheSystem", "hi") //"hi" is default value
        Log.e("sharedPreferences1", "sharedPreferences1 Val is -->> $string")
        if (string=="0") {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "Onboarding") {
                composable("Onboarding") { Onboarding(navController) }
                composable("Home") { Home(navController) }
                composable("Profile") { Profile(navController) }
            }
        } else{
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "Home") {
                composable("Onboarding") { Onboarding(navController) }
                composable("Home") { Home(navController) }
                composable("Profile") { Profile(navController) }
            }
        }
    }


    @Composable
    fun Home(navController: NavController) {
        // add databaseMenuItems code here
        val databaseMenuItems = database.menuItemDao().getAll().observeAsState(emptyList())
        // add orderMenuItems variable here
        val orderMenuItems = remember {
            mutableStateOf(false)
        }
        // add menuItems variable here
        var menuItems = if (orderMenuItems.value) {
            databaseMenuItems.value.sortedBy { it.title }
        } else {
            databaseMenuItems.value
        }
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Image(
                    painter = painterResource(id = R.drawable.littlelemonimgtxt),
                    contentDescription = "logo",
                    modifier = Modifier .clickable { navController.navigate("Profile") }
                        .width(200.dp)
                        .heightIn(100.dp)
                        .padding(start = 50.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.photoprofile),
                    contentDescription = "profilePhoto",
                    modifier = Modifier .clickable { navController.navigate("Profile") }
                        .clip(CircleShape)
                        .size(50.dp)
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, bottom = 16.dp)
                    .background(LittleLemonColor.green)
            ) {
                Text(
                    text = stringResource(id = R.string.title),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = LittleLemonColor.yellow
                )
                Text(
                    text = stringResource(id = R.string.location),
                    fontSize = 24.sp,
                    color = LittleLemonColor.cloud
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(top = 10.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.description),
                        modifier = Modifier
                            .padding(bottom = 10.dp, end = 10.dp)
                            .fillMaxWidth(0.6f),
                        color = LittleLemonColor.cloud
                    )
                    Image(
                        painter = painterResource(id = R.drawable.upperpanelimage),
                        contentDescription = "Upper Panel Image",
                        modifier = Modifier.clip(RoundedCornerShape(10.dp))
                    )
                }

                // add searchPhrase variable here
                val searchPhrase = remember {
                    mutableStateOf("")
                }

                // Add OutlinedTextField
                OutlinedTextField(
                    value = searchPhrase.value,
                    onValueChange = { searchPhrase.value = it },
                    label = { Text("Search") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 0.dp, start = 50.dp, end = 50.dp)
                        .background(Color.White)
                        .clip(RoundedCornerShape(10.dp))
                )

                // add is not empty check here
                if (searchPhrase.value.isNotEmpty()) {
                    menuItems = menuItems.filter { it.title.lowercase().contains(searchPhrase.value.lowercase()) }
                }
            }

            // add Button code here
            Button(modifier = Modifier.align(CenterHorizontally),
                onClick = { orderMenuItems.value = true }) {
                Text(text = "Tap to Order By Name")
            }

            Log.e("Tag", "Okay 4")
            MenuItemsList(items = menuItems)
            Log.e("Tag", "Okay 5")
        }
    }

    @Composable
    fun Profile(navController: NavController) {
        Column (modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            Image(
                painter = painterResource(id = R.drawable.littlelemonimgtxt),
                contentDescription = "Menu Icon",
                modifier = Modifier
                    .size(200.dp, 130.dp)
            )
            Text(text = "\r\nPersonal information\n",
                color = Color(0xFF495E57),
                fontSize = 25.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, start = 10.dp, bottom = 20.dp)
            )
            val sharedPreferences1 = getSharedPreferences("LoginInfo", MODE_PRIVATE)
            var text1 = sharedPreferences1.getString("FirstName", "hi").toString() //"hi" is default value
            var text2 = sharedPreferences1.getString("LastName", "hi").toString() //"hi" is default value
            var text3 = sharedPreferences1.getString("Email", "hi").toString() //"hi" is default value

            TextField(text1, onValueChange = { text1 = it }, label = { Text(text = "First name") })
            TextField(
                text2, onValueChange = { text2 = it },
                label = { Text(text = "Last name") },
                modifier = Modifier .padding(top = 10.dp)
            )
            TextField(
                text3, onValueChange = { text3 = it },
                label = { Text(text = "Email") },
                modifier = Modifier .padding(top = 10.dp)
            )
            Button(
                onClick = {
                    if (text1 != "" && text2 != "" && text3 != ""){
                        val sharedPreferences = getSharedPreferences("LoginInfo", MODE_PRIVATE)
                        with(sharedPreferences.edit()) {
                            putString("DataInTheSystem", "0")  // "0" Unsuccessful
                            apply()
                        }
                    }
                    navController.navigate("Onboarding")
                },
                colors = ButtonDefaults.buttonColors(
                    Color(0xFFF4CE14)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 100.dp, start = 10.dp, end = 10.dp)
            ) {
                Text(
                    text = "Log out",
                    color = Color.Black,
                    fontSize = 20.sp
                )
            }
        }
    }

    @Composable
    fun Onboarding(navController: NavController){
        var (text1,setText1) = remember { mutableStateOf("") }
        var (text2, setText2) = remember { mutableStateOf("") }
        var (text3, setText3) = remember { mutableStateOf("") }
        Column (modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            Image(
                painter = painterResource(id = R.drawable.littlelemonimgtxt),
                contentDescription = "Menu Icon",
                modifier = Modifier
                    .size(200.dp, 130.dp)
            )
            Text(text = "\r\nLet's get to know you\n",
                color = Color.White,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier .background(Color(0xFF495E57))
                    .fillMaxWidth()
                    .size(100.dp,100.dp)
                    .padding(top = 10.dp)
            )
            Text(text = "\r\nPersonal information\n",
                color = Color(0xFF495E57),
                fontSize = 25.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, start = 10.dp)
            )
            TextField(text1,setText1, label = { Text(text = "First name") })
            TextField(
                text2,setText2,
                label = { Text(text = "Last name") },
                modifier = Modifier .padding(top = 10.dp)
            )
            TextField(
                text3,setText3,
                label = { Text(text = "Email") },
                modifier = Modifier .padding(top = 10.dp)
            )
            Button(
                onClick = {
                    if (text1 != "" && text2 != "" && text3 != ""){
                        Toast.makeText(applicationContext,"Data stored successfully!", Toast.LENGTH_SHORT).show()
                        Log.e("TAG", "Successful")
                        // For Add String Value in sharedPreferences
                        // https://stackoverflow.com/questions/74956289/android-shared-preference-kotlin
                        val sharedPreferences = getSharedPreferences("LoginInfo", MODE_PRIVATE)
                        with(sharedPreferences.edit()) {
                            putString("DataInTheSystem", "1")  // "1" Successful
                            putString("FirstName", text1)  // First Name
                            putString("LastName", text2)  // Last Name
                            putString("Email", text3)  // Last Name
                            apply()
                        }
                        navController.navigate("Home")
                    }
                    else {
                        Toast.makeText(applicationContext,"Data was not stored", Toast.LENGTH_SHORT).show()
                        Log.e("TAG", "Unsuccessful")
                        // For Add String Value in sharedPreferences
                        // https://stackoverflow.com/questions/74956289/android-shared-preference-kotlin
                        val sharedPreferences = getSharedPreferences("LoginInfo", MODE_PRIVATE)
                        with(sharedPreferences.edit()) {
                            putString("DataInTheSystem", "0")  // "0" Unsuccessful
                            apply()
                        }

                    }
                },
                colors = ButtonDefaults.buttonColors(
                    Color(0xFFF4CE14)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 100.dp, start = 10.dp, end = 10.dp)
            ) {
                Text(
                    text = "Register",
                    color = Color.Black,
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Composable
private fun MenuItemsList(items: List<MenuItemRoom>) {
    HorizontalDivider(
        thickness = 1.dp,
        color = LittleLemonColor.yellow
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .padding(top = 20.dp)
    ) {
        items(
            items = items,
            itemContent = { menuItem ->
                Card{
                    //TODO: Insert code here
                    Row (
                        modifier = Modifier.fillMaxWidth()
                            .padding(8.dp)
                            .background(Color.White)
                    ){
                        Column{
                            Text(text = menuItem.title,
                                color = LittleLemonColor.charcoal,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(text =  menuItem.description,
                                modifier = Modifier.fillMaxWidth(.75f)
                                    .padding(top = 5.dp, bottom = 5.dp)
                                , color = LittleLemonColor.green
                            )
                            Text(text = menuItem.price,
                                fontWeight = FontWeight.Bold,
                                color = LittleLemonColor.green
                            )
                        }
                        Image(
                            painter = rememberAsyncImagePainter(menuItem.image),
                            contentDescription = null,
                            modifier = Modifier
                                .size(128.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )
                    }
                }
            }
        )
    }
}