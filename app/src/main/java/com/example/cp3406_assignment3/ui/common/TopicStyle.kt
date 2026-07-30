package com.example.studybuddy.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.studybuddy.ui.theme.TopicCyberSecurity
import com.example.studybuddy.ui.theme.TopicJava
import com.example.studybuddy.ui.theme.TopicNetworking
import com.example.studybuddy.ui.theme.TopicPython
import com.example.studybuddy.ui.theme.TopicSql

val ALL_TOPICS = listOf("Java", "Python", "SQL", "Networking", "Cyber Security")

fun topicColor(topic: String): Color = when (topic) {
    "Java" -> TopicJava
    "Python" -> TopicPython
    "SQL" -> TopicSql
    "Networking" -> TopicNetworking
    "Cyber Security" -> TopicCyberSecurity
    else -> TopicNetworking
}

fun topicIcon(topic: String): ImageVector = when (topic) {
    "Java" -> Icons.Filled.Code
    "Python" -> Icons.Filled.Code
    "SQL" -> Icons.Filled.Storage
    "Networking" -> Icons.Filled.Wifi
    "Cyber Security" -> Icons.Filled.Lock
    else -> Icons.Filled.Book
}
