package com.example.littlelemon

import com.example.littlelemon.ui.theme.MenuItemRoom
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MenuNetworkdata(
    // add code here
    @SerialName("menu")
    val menu: List<MenuItemNetwork>
)

@Serializable
data class MenuItemNetwork(
    // add code here
    @SerialName("id")
    val id: Int,

    @SerialName("title")
    val title: String,

    @SerialName("description")
    val description: String,

    @SerialName("price")
    val price: String,

    @SerialName("image")
    val image: String,

    @SerialName("category")
    val category: String
){
    fun toMenuItemRoom() = MenuItemRoom(
        // add code here
        id,
        title,
        description,
        price,
        image,
        category
    )
}