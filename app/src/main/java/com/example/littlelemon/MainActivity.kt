package com.example.littlelemon

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.littlelemon.ui.theme.LittleLemonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LittleLemonTheme {
                AppNavigation()
            }
        }
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("This is Home screen")
            Button(onClick = { navController.navigate("Profile") }) {
                Text("Go to Profile")
            }
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