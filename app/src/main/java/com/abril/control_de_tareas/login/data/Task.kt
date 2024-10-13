package com.abril.control_de_tareas.login.data

data class Task(
    var id: String? = null,
    var title: String = "",
    var description: String? = "",
    var isCompleted: Boolean = false
)
