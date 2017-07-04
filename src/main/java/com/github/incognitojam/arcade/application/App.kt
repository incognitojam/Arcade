package com.github.incognitojam.arcade.application

import com.github.incognitojam.arcade.Arcade
import com.github.incognitojam.arcade.display.Display
import com.github.incognitojam.arcade.machine.Context
import com.github.incognitojam.arcade.machine.Machine
import java.io.File

data class AppDescription(val name: String, val version: String, val author: String, val usesMouse: Boolean = false)

abstract class App constructor(val description: AppDescription, val machine: Machine) : IApplication {

    val display: Display = machine.display
    val context: Context = machine.context
    var isCreated = false

    override fun onCreate() {

    }

    fun getResource(filename: String): File {
        val subFolder = File(Arcade.DATA_FOLDER.absolutePath, "applications" + File.separatorChar + description.name.toLowerCase())
        val targetFile = File(subFolder, filename)

        if (targetFile.toPath().startsWith(subFolder.toPath())) {
            return targetFile
        } else {
            throw SecurityException("Application tried to access a file outside of its sub-folder!")
        }
    }

}
