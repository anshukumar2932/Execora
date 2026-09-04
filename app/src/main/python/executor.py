import io
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
