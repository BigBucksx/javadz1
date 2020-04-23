package hr.fer.zemris.java.custom.scripting.parser;
import hr.fer.zemris.java.custom.collections.*;
import hr.fer.zemris.java.custom.scripting.nodes.*;
import hr.fer.zemris.java.custom.scripting.tokens.*;
import java.lang.StringBuilder;

public class SmartScriptParser {
	private String docBody;
	private ObjectStack parseTree;
	private DocumentNode documentNode;
	private StringBuilder sb;
       	private ArrayBackendIndexedCollection symbols;
	private ArrayBackendIndexedCollection buffer;
	private State[] stateList;
	private State state;
	private int prevStateIndex;
	private int stateIndex;

	private int tail;
	private int length;

	/*State indexes*/
	int STATE_ESCAPE		= 0;
	int STATE_TEXT			= 1;
	int STATE_OPEN			= 2;
	int STATE_FUNCTION		= 3;
	int STATE_VARIABLE		= 4;
	int STATE_OPERATOR		= 5;
	int STATE_NUMBER		= 6;
	int STATE_NUMBER_OR_OPERATOR	= 7;
	int STATE_OPERATOR_PLUS		= 8;
	int STATE_OPERATOR_MINUS	= 9;
	int STATE_OPERATOR_MULTI	= 10;
	int STATE_CONST_STRING		= 11;
	int STATE_ECHO_TAG		= 12;
	int STATE_FOR_TAG		= 13;
	int STATE_END_TAG		= 14;
	int STATE_TAG			= 15;
	int STATE_CLOSE			= 16;
	int STATE_FINISH		= 17;

	public abstract class State {
		protected ArrayBackendIndexedCollection subSetState;

		public State() {
			subSetState = new ArrayBackendIndexedCollection();
		}

		public void request(char c) { 
			int newStateIndex = checkState(c);
			if(subSetState.contains(new Integer(newStateIndex))) {
				if(stateIndex < newStateIndex) {
					create();
					deactivate();
				} else if(newStateIndex == STATE_VARIABLE || newStateIndex == STATE_NUMBER) {
					sb.append(c);
				} 
				state = stateList[newStateIndex];
				prevStateIndex = stateIndex;
				stateIndex = newStateIndex;
			} else {
				sb.append(c);
			}
		}

		protected void deactivate() {
			sb.setLength(0);
		}

		protected int checkState(char c) {
			Character test = new Character(c);
			String tmp = sb.toString();

			if(symbols.contains(test)) {
				return symbols.indexOf(test);
			} else if(c == ' ') {
				return stateIndex; 	
			} else if(Character.isLetter(c)){
				return STATE_VARIABLE;	
			} else if(Character.isDigit(c)) {
				return STATE_NUMBER;
			} else {
				return -1;
			}
		}

		protected abstract void create();
	}

	private class StateText extends State {
		StateText() {
			super();
			subSetState.add(new Integer(STATE_ESCAPE));
			subSetState.add(new Integer(STATE_OPEN));
			subSetState.add(new Integer(STATE_FINISH));
		}

		@Override
		protected void create() {
			if(parseTree.isEmpty()) {
				throw new SmartScriptParserException("Too many ends!");
			} else {
				Node node = getRoot(); 
				String tmp = sb.toString();
				TextNode text = new TextNode(sb.toString());
				node.addChildNode(text);
			}

		}
	}

	private class StateEscape extends State {
		@Override 
		public void request(char c) {
			if(c == '[') {
				sb.append('[');
			} else {
				sb.append('\\');
				sb.append(c);
			}
			state = stateList[STATE_TEXT];
			stateIndex = STATE_TEXT;	
		}

		@Override
		protected void create() {}
	}

	private class StateOpen extends State {
		public StateOpen() {
			super();
			subSetState.add(new Integer(STATE_TAG));
		}

		@Override
		protected void create() {}
	}

	private class StateTag extends State {
		public StateTag() {
			super();
			subSetState.add(new Integer (STATE_ECHO_TAG));
			subSetState.add(new Integer (STATE_FOR_TAG));
			subSetState.add(new Integer (STATE_TAG));
			subSetState.add(new Integer (STATE_END_TAG));
			subSetState.add(new Integer (STATE_CLOSE));
		}

		@Override
		protected void create() {}
	}

	private class StateForTag extends State {
		public StateForTag() {
			super();
			subSetState.add(new Integer(STATE_VARIABLE));
			subSetState.add(new Integer(STATE_NUMBER));
			subSetState.add(new Integer(STATE_TAG));
		}

		@Override
		protected void create() {
			buffer.remove(0); 		// ukloni "variable" (F)OR iz bufera. Nastanak te varijable je nusprodukt ovakvog procesa. 
			int n = buffer.size();          // F je znak koji prebacuje u ovo stanje a OR zaostaje te radi istoimenu varijablu koja nam ne treba
			if(n < 2) {
				throw new SmartScriptParserException("For loop: too few arguments! Total arguments: " + n + "Expected minimum of 3");
			} else if(!(buffer.get(0) instanceof TokenVariable)) {
				Token tok = (Token) buffer.get(0);
				throw new SmartScriptParserException("Unexpected token: " + tok.asText()); 
			} 

			Token[] tokens = new Token[n];
			for(int i = 0; i < n; i++) {
				tokens[i] = (Token) buffer.get(i);
			}
			ForLoopNode loop = new ForLoopNode(tokens);
			Node root = getRoot();
			root.addChildNode(loop);
			parseTree.push(loop);
		}

		@Override
		protected void deactivate() {
			buffer.clear();
		}
	}
	private class StateEchoTag extends State {
		public StateEchoTag() {
			super();
			subSetState.add(new Integer (STATE_VARIABLE));
			subSetState.add(new Integer (STATE_NUMBER));
			subSetState.add(new Integer (STATE_CONST_STRING));
			subSetState.add(new Integer (STATE_OPERATOR_PLUS));
			subSetState.add(new Integer (STATE_OPERATOR_MINUS));
			subSetState.add(new Integer (STATE_OPERATOR_MULTI));
			subSetState.add(new Integer (STATE_FUNCTION));
			subSetState.add(new Integer (STATE_TAG));
		}

		@Override
		protected void create() {
			int n = buffer.size();
			if(parseTree.isEmpty()) {
				throw new SmartScriptParserException("Too many END's");
			} else {
				Token[] tokens = new Token[n];
				for(int i = 0; i < n; i++) {
					tokens[i] = (Token) buffer.get(i);
				}
				Node node = getRoot();
				node.addChildNode(new EchoNode(tokens));
			}
		}

		@Override
		protected void deactivate() {
			buffer.clear();
		}
	}

	public abstract class StateFinal extends State {
		public StateFinal() {
			super();
			subSetState.add(new Integer(STATE_ECHO_TAG));
			subSetState.add(new Integer(STATE_FOR_TAG));
			subSetState.add(new Integer(STATE_TAG));
		}

		@Override 
		protected int checkState(char c) {
			if(c == ' ') {
				return prevStateIndex;
			} else {
				return -1;
			}
		}
	}

	private class StateNumber extends StateFinal {
		@Override
		protected void create() {
			String text = sb.toString();
			Token tok;
			try {
				if(text.indexOf('.') != -1) {
					double number = Double.parseDouble(text);
					tok = new TokenConstantDouble(number);
				} else {
					int number = Integer.parseInt(text);
					tok = new TokenConstantInteger(number);
				}
			} catch (NumberFormatException ex) {
				throw new SmartScriptParserException("Illegal token: " + text);
			}
			buffer.add(tok);
		}
	}

	private class StateVariable extends StateFinal {
		@Override
		protected void create() {
			char c = sb.charAt(0);
			if(c == '_' || Character.isDigit(c)) {
				throw new SmartScriptParserException("Illegal variable name: " + sb.toString());
			} else {
				buffer.add(new TokenVariable(sb.toString()));
			}
		}
	}	

	private class StateFunction extends StateFinal {
		@Override
		protected void create() {
			buffer.add(new TokenFunction(sb.toString()));
		}
	}

	private class StateOperatorPlus extends StateFinal {
		@Override
		protected void create() {
			buffer.add(new TokenOperator("+"));
		}
	}

	private class StateOperatorMinus extends StateFinal {
		@Override
		protected void create() {
			buffer.add(new TokenOperator("-"));
		}
	}

	private class StateOperatorMulti extends StateFinal {
		@Override
		protected void create() {
			buffer.add(new TokenOperator("*"));
		}
	}

	private class StateConstString extends StateFinal {
		@Override 
		protected void create() {
			buffer.add(new TokenString(sb.toString()));
		}

		@Override
		protected int checkState(char c) {
			if(c == '"') {
				return prevStateIndex;
			} else {
				return -1;
			}
		}
	}

	private class StateClose extends State {
		@Override 
		public void request(char c) {
			if(c != ' ') {
				sb.append(c);	
			}

			prevStateIndex = stateIndex;
			stateIndex = STATE_TEXT;
			state = stateList[STATE_TEXT];
		}

		@Override
		protected void create() {}
	}

	private class StateEndTag extends State {
		public StateEndTag() {
			super();
			subSetState.add(new Integer(STATE_TAG));
		}
		@Override
		protected void create() {
			parseTree.pop();	
		}
	}
	
	public SmartScriptParser(String docBody) {
		this.docBody  = docBody;

		parseTree     = new ObjectStack();
		documentNode  = new DocumentNode();
		parseTree.push(documentNode);

		sb = new StringBuilder();
		symbols = new ArrayBackendIndexedCollection(18);
		buffer  = new ArrayBackendIndexedCollection();
		
		char[] s = {'\\', '\0', '[', '@', '\0', '\0', '\0', '\0', '+', '-', '*', '"', '=', 'F', 'E', '$', ']', '~'};
		
		for(int i = 0; i < 18; i++) {
			symbols.add(new Character(s[i]));
		}

		stateList     = new State[18];
		stateList[0]  = new StateEscape();
		stateList[1]  = new StateText();
		stateList[2]  = new StateOpen();
		stateList[3]  = new StateFunction();
		stateList[4]  = new StateVariable();
		stateList[5]  = null;
		stateList[6]  = new StateNumber();
		stateList[7]  = null;
		stateList[8]  = new StateOperatorPlus();
		stateList[9]  = new StateOperatorMinus();
		stateList[10] = new StateOperatorMulti();
		stateList[11] = new StateConstString();
		stateList[12] = new StateEchoTag();
		stateList[13] = new StateForTag();
		stateList[14] = new StateEndTag();
		stateList[15] = new StateTag();
		stateList[16] = new StateClose();
		stateList[17] = null;

		state = stateList[STATE_TEXT];
		stateIndex     = STATE_TEXT;
		prevStateIndex = -1;

		parse();
	}

	private void parse() {
		tail = 0;
		length = docBody.length();	
		char c;
		while(tail < length) {
			c = docBody.charAt(tail);	
			state.request(c);
			tail++;
		}
		state.request('~');
	}	
	private Node getRoot() {
		if(parseTree.isEmpty()) {
			throw new SmartScriptParserException("Too many END's");
		} else {
			return (Node) parseTree.peek();
		}
	}
	public DocumentNode getDocumentNode() {
		return documentNode;
	}
}
