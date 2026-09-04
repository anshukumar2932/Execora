# Execora 🐍

Execora is a lightweight, multi-file Python development environment built entirely with **Jetpack Compose** and **Chaquopy**. It brings a desktop-like IDE experience to Android, allowing you to write, manage, and execute Python code on the go.

![License](https://img.shields.io/badge/License-MIT-blue.svg)
![Platform](https://img.shields.io/badge/Platform-Android-green.svg)
![Python](https://img.shields.io/badge/Python-3.10%20%7C%203.11-yellow.svg)

##  Features

- **Multi-File Management**: Create and switch between multiple `.py` files using a tabbed interface (similar to VS Code).
- **Python Syntax Highlighting**: Real-time color coding for keywords, strings, comments, and functions.
- **Integrated Terminal**: Capture `stdout` and `stderr` directly within the app's terminal.
- **Persistent Storage**: Your theme preferences and settings are saved using **Jetpack DataStore**.
- **Modern UI**: Built with **Material 3**, featuring a clean "Edge-to-Edge" look and responsive layout.
- **Background Execution**: Python scripts run on a background thread to keep the UI smooth and responsive.
- **Smart File Creation**: Automated `.py` extension locking and duplicate filename detection.

## Tech Stack

- **UI Framework**: Jetpack Compose (Material 3)
- **Python Integration**: [Chaquopy](https://chaquo.com/chaquopy/)
- **Navigation**: Compose Navigation
- **Local Persistence**: DataStore Preferences
- **Architecture**: Repository Pattern
- **Concurrency**: Kotlin Coroutines & Background Threads

##  Getting Started

### Prerequisites
- Android Studio Ladybug (or newer)
- Android SDK 25+
- Internet connection (for initial Chaquopy setup)

### Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/execora.git
   ```
2. Open the project in Android Studio.
3. Sync Project with Gradle Files.
4. Ensure you have the NDK installed (required by Chaquopy for bundling Python).

##  Project Structure

- `MainActivity.kt`: The heart of the app, containing the Editor, Navigation, and UI logic.
- `PythonSyntaxHighlighter.kt`: Custom logic for real-time code colorization.
- `SettingsRepository.kt`: Handles persistent data for app settings.
- `executor.py`: (Internal) Python bridge for capturing output and managing execution.

##  Contributing

Contributions are welcome! Whether it's fixing bugs, adding new features, or improving documentation, feel free to open a Pull Request.

##  License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

