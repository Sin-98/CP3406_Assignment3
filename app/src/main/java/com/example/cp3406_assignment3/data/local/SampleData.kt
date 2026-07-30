package com.example.studybuddy.data.local

import com.example.studybuddy.data.local.entity.FlashcardEntity

/**
 * Starter content so the Flashcard tab is never empty on first launch.
 * Inserted once via StudyBuddyDatabase.Callback.onCreate (see di/AppModule.kt).
 */
object SampleData {

    val flashcards: List<FlashcardEntity> = listOf(
        // Java
        FlashcardEntity(question = "What is polymorphism?", answer = "The ability of objects to take many forms, typically via method overriding or overloading.", topic = "Java"),
        FlashcardEntity(question = "What is the difference between == and .equals()?", answer = "== compares references (object identity); .equals() compares content, unless overridden.", topic = "Java"),
        FlashcardEntity(question = "What is a constructor?", answer = "A special method used to initialize a newly created object.", topic = "Java"),

        // Python
        FlashcardEntity(question = "What is a list comprehension?", answer = "A concise way to build a list, e.g. [x*2 for x in range(5)].", topic = "Python"),
        FlashcardEntity(question = "What does PEP 8 refer to?", answer = "The official style guide for writing readable Python code.", topic = "Python"),
        FlashcardEntity(question = "What is the difference between a list and a tuple?", answer = "Lists are mutable; tuples are immutable.", topic = "Python"),

        // SQL
        FlashcardEntity(question = "What is a Primary Key?", answer = "A unique identifier for each record in a table.", topic = "SQL"),
        FlashcardEntity(question = "Which SQL command retrieves data?", answer = "SELECT", topic = "SQL"),
        FlashcardEntity(question = "What does a JOIN do?", answer = "Combines rows from two or more tables based on a related column.", topic = "SQL"),

        // Networking
        FlashcardEntity(question = "What does TCP stand for?", answer = "Transmission Control Protocol — a reliable, connection-oriented protocol.", topic = "Networking"),
        FlashcardEntity(question = "What is the purpose of DNS?", answer = "Translates human-readable domain names into IP addresses.", topic = "Networking"),
        FlashcardEntity(question = "What layer does HTTP operate at in the OSI model?", answer = "Application layer (Layer 7).", topic = "Networking"),

        // Cyber Security
        FlashcardEntity(question = "What is phishing?", answer = "A social engineering attack that tricks users into revealing sensitive information.", topic = "Cyber Security"),
        FlashcardEntity(question = "What does the CIA triad stand for?", answer = "Confidentiality, Integrity, and Availability.", topic = "Cyber Security"),
        FlashcardEntity(question = "What is two-factor authentication?", answer = "A security process requiring two different forms of identification to verify identity.", topic = "Cyber Security")
    )
}
