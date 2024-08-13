package com.idle.domain.model.map

data class MapMarker(
    val type: String = "e",
    val icon: String,
    val pos: String,
){
    override fun toString(): String {
        return "type:${type}|icon:${icon}|pos:${pos}"
    }
}
