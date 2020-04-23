package hr.fer.zemris.java.custom.scripting.tokens;

public class TokenString extends Token{
	private String value;
	
	public TokenString(String value) {
		this.value = value;
	}
	@Override
	public String asText() {
		return "\"" + value + "\""; 
	}

}
