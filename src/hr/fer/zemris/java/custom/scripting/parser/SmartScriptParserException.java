package hr.fer.zemris.java.custom.scripting.parser;

public class SmartScriptParserException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SmartScriptParserException(){}
	public SmartScriptParserException(String message) {
		super(message);
	}
	public SmartScriptParserException(Throwable cause) {
		super(cause);
	}
	public SmartScriptParserException(String message, Throwable cause) {
		super(message, cause);
	}
}
