package io.rapid;

/**
 * Use for internal logging only. These logs are not visible when using release version of the SDK.
 */
public class Logcat {
	private static final String TAG = "Rapid SDK internal";

	// TODO: according to release
	private static final boolean IS_ENABLED = true;
	private static final boolean SHOW_CODE_LOCATION = true;


	private Logcat() {}


	public static void d(String msg, Object... args) {
		if(IS_ENABLED) System.out.println(getCodeLocation().toString() + formatMessage(msg, args));
	}


	public static void e(String msg, Object... args) {
		if(IS_ENABLED) System.err.println(getCodeLocation().toString() + formatMessage(msg, args));
	}


	public static void e(Throwable throwable, String msg, Object... args) {
		if(IS_ENABLED) {
			System.err.println(getCodeLocation().toString() + formatMessage(msg, args));
			if(throwable != null)
				throwable.printStackTrace();
		}
	}


	public static void i(String msg, Object... args) {
		if(IS_ENABLED) System.out.println(getCodeLocation().toString() + formatMessage(msg, args));
	}


	public static void printStackTrace(Throwable throwable) {
		e(throwable, "");
	}


	private static String formatMessage(String msg, Object... args) {
		return args.length == 0 ? msg : String.format(msg, args);
	}


	private static CodeLocation getCodeLocation() {
		return getCodeLocation(3);
	}


	private static CodeLocation getCodeLocation(int depth) {
		StackTraceElement[] stackTrace = new Throwable().getStackTrace();
		StackTraceElement[] filteredStackTrace = new StackTraceElement[stackTrace.length - depth];
		System.arraycopy(stackTrace, depth, filteredStackTrace, 0, filteredStackTrace.length);
		return new CodeLocation(filteredStackTrace);
	}


	private static class CodeLocation {
		private final String mThread;
		private final String mFileName;
		private final String mClassName;
		private final String mMethod;
		private final int mLineNumber;


		CodeLocation(StackTraceElement[] stackTrace) {
			StackTraceElement root = stackTrace[0];
			mThread = Thread.currentThread().getName();
			mFileName = root.getFileName();
			String className = root.getClassName();
			mClassName = className.substring(className.lastIndexOf('.') + 1);
			mMethod = root.getMethodName();
			mLineNumber = root.getLineNumber();
		}


		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			if(SHOW_CODE_LOCATION) {
				builder.append('(');
				builder.append(mFileName);
				builder.append(':');
				builder.append(mLineNumber);
				builder.append(") ");
			}
			return builder.toString();
		}
	}
}
