# Execora Reference Manual

This manual describes the internal Python environment and the bridge between Android and CPython.

## The `executor` Module (Internal)

The `executor` module is the primary bridge between the Kotlin UI and the Python interpreter.

### `run_code(code: str) -> str`
Compiles and executes a string of Python code.
- **Parameters**: `code` - A string containing the Python source code to execute.
- **Returns**: A combined string of `stdout` and `stderr`.
- **Environment**:
    - `__name__` is set to `"__main__"`.
    - `stdout` and `stderr` are redirected using `io.StringIO`.
    - Execution happens within a dedicated `globals_dict` to prevent pollution of the bridge's own namespace.

## Syntax Highlighting Rules

The `PythonSyntaxHighlighter` uses the following regex patterns to identify tokens:

| Token Type | Regex Pattern | Color (Dark Theme) |
| :--- | :--- | :--- |
| **Keywords** | `\b(def|class|if|else|elif|while|for|...)\b` | `#FF7B72` (Red-Orange) |
| **Strings** | `"(.*?)"` or `'(.*?)'` | `#A5D6FF` (Light Blue) |
| **Comments** | `#.*` | `#8B949E` (Grey) |
| **Numbers** | `\b\d+(\.\d+)?\b` | `#D2A8FF` (Purple) |
| **Functions** | `\b[a-zA-Z_][a-zA-Z0-9_]*(?=\()` | `#D2A8FF` (Purple) |

## Android-Python Interop

Execora uses **Chaquopy** to host the Python interpreter. 

### Threading Model
- **UI Thread**: Handles user input and syntax highlighting.
- **Background Thread**: Every time **Run** is pressed, a new `java.lang.Thread` is spawned to execute the Python code. This ensures the Android UI remains responsive even during heavy computation.

### Output Redirection
Execora does not use a standard PTY. Instead, it wraps the Python `exec()` call in a `contextlib.redirect_stdout` block. This means interactive input (`input()`) is currently **not supported** in the terminal view.
