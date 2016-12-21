package com.gsmart.util;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class Loggers {

	final static Logger logger = Logger.getLogger(Loggers.class);

	public static void loggerStart() {

		StackTraceElement element = getStackTrace();
		String className = getClassName(element);
		String string = "Start";
		print(element, className, string);

	}

	public static void loggerStart(Object value) {

		StackTraceElement element = getStackTrace();
		String className = getClassName(element);
		String string = "Start";
		print(element, className, string);

		loggerValue(string + " :: Object Received ", value);

	}

	public static void loggerEnd() {
		StackTraceElement element = getStackTrace();
		String className = getClassName(element);
		String string = "End";
		print(element, className, string);
	}

	public static void loggerEnd(Object value) {

		StackTraceElement element = getStackTrace();
		String className = getClassName(element);
		String string = "End";
		print(element, className, string);

		loggerValue(string + " :: Object Received ", value);

	}

	public static void loggerException(String msg) {

		StackTraceElement element = getStackTrace();
		String className = getClassName(element);
		logger.info("Exception Occured :: " + className + "." + element.getMethodName() + "() :: " + msg + " :: " + element.getLineNumber());

	}

	public static void loggerValue(String stmt, Object value) {

		if (value == null) {
			logger.info(stmt + value);
		} else {
			String type = value.getClass().getSimpleName();

			switch (type) {
			case "Integer":
				logger.info(stmt + (Integer) value);
				break;
			case "Float":
				logger.info(stmt + (Float) value);
				break;
			case "Double":
				logger.info(stmt + (Double) value);
				break;
			case "Long":
				logger.info(stmt + (Long) value);
				break;
			case "Boolean":
				logger.info(stmt + (Boolean) value);
				break;
			case "String":
				logger.info(stmt + (String) value);
				break;
			case "ArrayList":
				logger.info(stmt + (ArrayList<?>) value);
				break;
				default: logger.info(value);
			}
		}
	}

	private static StackTraceElement getStackTrace() {
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		StackTraceElement element = stacktrace[3];
		return element;
	}

	private static String getClassName(StackTraceElement element) {
		String[] packageName = element.getClassName().split("\\.");
		int length = packageName.length;
		String className = packageName[length - 1];
		return className;
	}

	private static void print(StackTraceElement element, String className, String string) {
		logger.info(string + " :: " + className + "." + element.getMethodName() + "() :: " + element.getLineNumber());
	}
}