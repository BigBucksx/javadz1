package hr.fer.zemris.java.custom.scripting.tokens;

public class TokenFunction extends Token{
	private String name;
	
	public TokenFunction(String name) {
		this.name = name;
	}

	@Override
	public String asText() {
		return "@"+name;
	}
}
