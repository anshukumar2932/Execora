# Execora 🐍

Execora is a powerful, lightweight Python development environment for Android. Built with **Jetpack Compose** and powered by **Chaquopy**, it offers a desktop-grade coding experience in the palm of your hand.

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9-purple.svg)](https://kotlinlang.org)
[![Python](https://img.shields.io/badge/Python-3.10%20%7C%203.11-yellow.svg)](https://python.org)

---

##  Features

- **Multi-File Editor**: Manage multiple `.py` files simultaneously with a smooth tabbed interface.
- **Intelligent Highlighting**: Real-time syntax highlighting for keywords, strings, comments, and numeric literals.
- **Integrated Python Terminal**: Run scripts instantly and view `stdout`/`stderr` in a dedicated terminal pane.
- **Adaptive UI**: Built with Material 3, featuring full Edge-to-Edge support and responsive layouts for phones and tablets.
- **Background Execution**: Code runs on separate threads to ensure a jank-free UI.
- **Theme Persistence**: Automatic Light/Dark mode switching with persistent user preferences via DataStore.
- **File Safety**: Built-in validation for filenames and extensions.

## 🛠 Tech Stack

- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
- **Interpreter**: [Chaquopy](https://chaquo.com/chaquopy/) (Python for Android)
- **Navigation**: [Compose Navigation](https://developer.android.com/jetpack/compose/navigation)
- **Persistence**: [Jetpack DataStore](https://developer.android.com/topic/libraries/architecture/datastore)
- **Concurrency**: [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)

##  Project Structure

```text
app/src/main/
├── java/com/execora/app/
│   ├── MainActivity.kt           # Navigation & Screen Composables
│   ├── PythonSyntaxHighlighter.kt # Logic for real-time highlighting
│   ├── SettingsRepository.kt     # DataStore abstraction
│   └── ui/theme/                 # M3 Design System & Custom Colors
└── python/
    └── executor.py               # Python execution bridge
```

##  Getting Started

### Prerequisites
- [Android Studio Ladybug](https://developer.android.com/studio) or newer.
- Android SDK 25+.
- Python 3.10 or 3.11 (Managed by Gradle flavors).

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/anshukumar2932/execora.git
   ```
2. Open the project in Android Studio.
3. Choose a build variant (e.g., `py311Debug`) from the "Build Variants" tab.
4. Sync Gradle and click **Run**.

## Documentation

Comprehensive documentation is available for both users and developers:

### For Users
- **[Getting Started Tutorial](docs/TUTORIAL.md)**: A step-by-step guide to writing your first script.
- **[User Manual](docs/TUTORIAL.md#4-managing-settings)**: How to configure themes and editor settings.

### For Developers
- **[Architecture Overview](docs/ARCHITECTURE.md)**: Details on the Kotlin-Python bridge and threading model.
- **[Technical Reference](docs/REFERENCE.md)**: API documentation for the internal `executor` module and highlighting regex.
- **[Contributing Guide](docs/CONTRIBUTING.md)**: How to set up the dev environment and submit PRs.