# Execora Tutorial

This tutorial provides a walkthrough of the basic features of Execora. By the end, you'll have written, saved, and executed your first Python script on Android.

## 1. Creating Your First File

When you first open Execora, you'll see a default `main.py`. To create a new file:
1. Tap the **Three Dots (⋮)** in the top right corner.
2. Select **New File**.
3. Enter a name (e.g., `hello_world`). Execora automatically appends the `.py` extension.
4. A new tab will appear in the Tab Row.

## 2. Writing Code

The editor supports real-time syntax highlighting. Try typing the following:

```python
def greet(name):
    return f"Hello, {name}! Welcome to Execora."

for i in range(3):
    print(greet(f"User {i+1}"))
```

Notice how keywords like `def`, `return`, `for`, and `in` are highlighted.

## 3. Running and Stopping

- **Run**: Tap the **▶ Run** button at the bottom. The terminal will expand and show the output.
- **Stop**: If you write an infinite loop (e.g., `while True: pass`), you can tap the **■ Stop** button to interrupt the execution.

## 4. Managing Settings

Tap the **⋮** menu and select **Settings**. Here you can:
- Toggle **Dark Theme**.
- View the current Editor font size (15sp).

## 5. Working with Multiple Files

Execora allows you to have multiple scripts open. You can reference logic between files if they are in the same directory (Coming soon: persistent file system support). Currently, files are stored in-memory for the duration of the session.
