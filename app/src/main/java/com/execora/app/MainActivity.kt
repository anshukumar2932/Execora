package com.execora.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.execora.app.ui.theme.ExecoraTheme
import com.execora.app.ui.theme.ExecoraTheme.extraColors
import kotlinx.coroutines.launch
import com.chaquo.python.Python
import com.chaquo.python.PyObject
import com.chaquo.python.android.AndroidPlatform
import java.util.concurrent.atomic.AtomicBoolean
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }
        val settingsRepository = SettingsRepository(this)
        setContent {
            val theme by settingsRepository.themeFlow.collectAsState(initial = "dark")
            ExecoraTheme(darkTheme = theme == "dark") {
                ExecoraApp(settingsRepository)
            }
        }
    }
}
data class PyFile(
    val name: String,
    val code: String
)

@Composable
fun AboutScreen(
    onBackClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextButton(
                onClick = onBackClick,
                modifier = Modifier.semantics {
                    contentDescription = "Back"
                }
            ) {
                Text(
                    text = "← Back",
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(
                modifier = Modifier.width(16.dp)
            )

            Text(
                text = "About Execora",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {

            Text(
                text = "Execora",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 28.sp
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Python Terminal for Android",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                fontSize = 16.sp
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Text(
                text = "Version 1.0",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "Execora is a lightweight Python development environment built with Jetpack Compose.",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun SettingsScreen(
    settingsRepository: SettingsRepository,
    onBackClick: () -> Unit
) {
    val theme by settingsRepository.themeFlow.collectAsState(initial = "dark")
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextButton(
                onClick = onBackClick,
                modifier = Modifier.semantics {
                    contentDescription = "Back"
                }
            ) {
                Text(
                    text = "← Back",
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(
                modifier = Modifier.width(16.dp)
            )

            Text(
                text = "Settings",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {

            Text(
                text = "Appearance",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Dark Theme",
                    color = MaterialTheme.colorScheme.onBackground
                )
                Switch(
                    checked = theme == "dark",
                    onCheckedChange = { isDark ->
                        scope.launch {
                            settingsRepository.saveTheme(if (isDark) "dark" else "light")
                        }
                    }
                )
            }

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            Text(
                text = "Editor Settings",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "Font Size",
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "15sp",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun EditorScreen(
    onSettingsClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    var showNewFileDialog by remember { mutableStateOf(false) }
    var newFileName by remember { mutableStateOf("") }
    var files by remember {
        mutableStateOf(listOf(PyFile("main.py", "print(\"Hello Execora\")")))
    }
    var currentFileIndex by remember { mutableIntStateOf(0) }
    var output by remember {
        mutableStateOf("Execora Python Terminal\nReady.\n")
    }

    var isRunning by remember { mutableStateOf(false) }
    val stopRequested = remember { AtomicBoolean(false) }
    val scope = rememberCoroutineScope()

    val currentFile = files[currentFileIndex]
    var menuExpanded by remember {
        mutableStateOf(false)
    }

    val extraColors = extraColors

    fun runPython(code: String) {
        isRunning = true
        stopRequested.set(false)
        output = ">>> Running ${currentFile.name}...\n\n"

        Thread {
            val result = try {
                val py = Python.getInstance()
                val executor = py.getModule("executor")

                executor
                    .callAttr("run_code", code)
                    .toString()

            } catch (e: Exception) {
                "Error:\n${e.message}\n"
            }

            scope.launch {
                output += result
                isRunning = false
            }
        }.start()
    }
    if (showNewFileDialog) {
        val trimmedName = newFileName.trim()
        val fullName = if (trimmedName.isEmpty()) "" else "$trimmedName.py"
        val nameExists = files.any { it.name == fullName }

        AlertDialog(
            onDismissRequest = {
                showNewFileDialog = false
                newFileName = ""
            },
            title = { Text("New File") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newFileName,
                        onValueChange = { input ->
                            // Strip .py if user tries to type it manually
                            newFileName = input.removeSuffix(".py")
                        },
                        placeholder = { Text("filename") },
                        suffix = { Text(".py") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        isError = nameExists
                    )
                    if (nameExists) {
                        Text(
                            text = "A file with this name already exists",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (trimmedName.isNotBlank() && !nameExists) {
                            val newFile = PyFile(fullName, "# Write Your code here...\n")
                            files = files + newFile
                            currentFileIndex = files.size - 1
                            output = "New Python file created: $fullName\n"
                            showNewFileDialog = false
                            newFileName = ""
                        }
                    },
                    enabled = trimmedName.isNotBlank() && !nameExists
                ) {
                    Text("Create")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showNewFileDialog = false
                        newFileName = ""
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {

        // =========================
        // TOP BAR
        // =========================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Execora",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp
            )

            // Three dots
            Box {

                IconButton(
                    onClick = {
                        menuExpanded = true
                    },
                    modifier = Modifier.semantics {
                        contentDescription = "More options"
                    }
                ) {
                    Text(
                        text = "⋮",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 28.sp
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = {
                        menuExpanded = false
                    }
                ) {

                    // New File
                    DropdownMenuItem(
                        text = {
                            Text("New File")
                        },
                        onClick = {
                            showNewFileDialog = true
                            menuExpanded = false
                        }
                    )

                    // Save
                    DropdownMenuItem(
                        text = {
                            Text("Save")
                        },
                        onClick = {
                            output = "${currentFile.name} saved.\n"
                            menuExpanded = false
                        }
                    )

                    // Clear Terminal
                    DropdownMenuItem(
                        text = {
                            Text("Clear Terminal")
                        },
                        onClick = {
                            output = ""
                            menuExpanded = false
                        }
                    )

                    // Settings
                    DropdownMenuItem(
                        text = {
                            Text("Settings")
                        },
                        onClick = {
                            menuExpanded = false

                            // Open Settings screen
                            onSettingsClick()
                        }
                    )

                    // About
                    DropdownMenuItem(
                        text = {
                            Text("About Execora")
                        },
                        onClick = {
                            menuExpanded = false

                            // Open About screen
                            onAboutClick()
                        }
                    )
                }
            }
        }

        // =========================
        // FILE TABS
        // =========================

        ScrollableTabRow(
            selectedTabIndex = currentFileIndex,
            edgePadding = 0.dp,
            containerColor = MaterialTheme.colorScheme.surface,
            divider = {},
            indicator = { tabPositions ->
                if (currentFileIndex < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.then(
                            with(TabRowDefaults) {
                                Modifier.tabIndicatorOffset(tabPositions[currentFileIndex])
                            }
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        ) {
            files.forEachIndexed { index, file ->
                Tab(
                    selected = currentFileIndex == index,
                    onClick = { currentFileIndex = index },
                    text = {
                        Text(
                            text = file.name,
                            maxLines = 1,
                            fontSize = 13.sp
                        )
                    }
                )
            }
        }

        // =========================
        // CODE EDITOR
        // =========================

        OutlinedTextField(
            value = currentFile.code,
            onValueChange = { newCode ->
                files = files.toMutableList().also {
                    it[currentFileIndex] = it[currentFileIndex].copy(code = newCode)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            textStyle = LocalTextStyle.current.copy(
                fontFamily = FontFamily.Monospace,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground
            ),
            visualTransformation = PythonSyntaxHighlighter(extraColors),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background
            )
        )

        // =========================
        // TERMINAL
        // =========================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(extraColors.terminalBackground)
                .padding(12.dp)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {

            Text(
                text = "TERMINAL",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                fontSize = 12.sp
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = output,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp
            )
        }

        // =========================
        // BOTTOM CONTROLS
        // =========================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Button(
                onClick = {
                    runPython(currentFile.code)
                },
                enabled = !isRunning
            ) {
                Text("▶ Run")
            }

            Button(
                onClick = {
                    stopRequested.set(true)
                },
                enabled = isRunning
            ) {
                Text("■ Stop")
            }
        }
    }
}

@Composable
fun ExecoraApp(settingsRepository: SettingsRepository) {

    val navController = rememberNavController()

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = "editor"
        ) {

            // Main Editor
            composable("editor") {
                EditorScreen(
                    onSettingsClick = {
                        navController.navigate("settings")
                    },
                    onAboutClick = {
                        navController.navigate("about")
                    }
                )
            }

            // Settings
            composable("settings") {
                SettingsScreen(
                    settingsRepository = settingsRepository,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            // About
            composable("about") {
                AboutScreen(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}