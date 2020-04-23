package hr.fer.zemris.java.custom.scripting.tokens;

public class TokenVariable extends Token{
		private String name;
		
		public TokenVariable(String name) {
			this.name = name;
		}

		@Override
		public String asText() {
			return name;
		}
}
