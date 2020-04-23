package hr.fer.zemris.java.custom.scripting.tokens;

public class TokenConstantDouble extends Token{
	private double value;
	
	public TokenConstantDouble(double value) {
		this.value = value;
	}

	@Override
	public String asText() {
		String retval = String.valueOf(value);
		return retval;
	}
}
