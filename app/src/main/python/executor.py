"""
Execora Bridge Module
=====================

This module provides the execution environment for Python scripts running within the
Execora Android app. It handles output redirection and exception capturing.
"""

import io
import contextlib
import traceback


def run_code(code):
    """
    Executes a string of Python code and returns the captured output.

    :param code: The Python source code to execute.
    :type code: str
    :return: The combined contents of stdout and stderr produced during execution.
    :rtype: str
    """
    stdout = io.StringIO()
import contextlib
import traceback


def run_code(code):
    stdout = io.StringIO()
    stderr = io.StringIO()

    try:
        globals_dict = {
            "__name__": "__main__",
            "__builtins__": __builtins__,
        }

        with contextlib.redirect_stdout(stdout), \
             contextlib.redirect_stderr(stderr):

            exec(
                compile(code, "<execora>", "exec"),
                globals_dict,
                globals_dict
            )

    except BaseException:
        traceback.print_exc(file=stderr)

    return stdout.getvalue() + stderr.getvalue()
