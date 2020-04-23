package hr.fer.zemris.java.custom.scripting.main;
import hr.fer.zemris.java.custom.scripting.parser.*;
import hr.fer.zemris.java.custom.scripting.tokens.*;
import hr.fer.zemris.java.custom.scripting.nodes.*;

public class Main {
	public static void main(String[] args) {
	String docBody = new String("This is sample text.\r\n[$ FOR i 1 10 1 $]\r\n\tThis is [$= i $]-th time this message is generated.\r\n[$END$]\r\n[$FOR i 0 10 2 $]\r\n\tsin([$=i $]^2) = [$= i i * @sin \"0.000\" @decfmt $] \r\n[$END$]"); 
		SmartScriptParser parser = null;
		try {
			parser = new SmartScriptParser(docBody);
		} catch(SmartScriptParserException ex) {
			System.out.println("Unable to parse document!");
			System.out.println(ex.getMessage());
			System.exit(-1);
		} catch(Exception ex) {
			System.out.println("If this line ever executes, you have failed this class!");
			System.out.println(ex.getMessage());
			System.exit(-1);
		}

		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = createOriginalDocumentBody(document);
		System.out.println(originalDocumentBody);
	}

	private static String createOriginalDocumentBody(Node node) {
		String body = node.asText();
		for(int i = 0; i < node.numberOfChildren(); i++) {
			body += createOriginalDocumentBody(node.getChild(i));
		}

		if(node instanceof ForLoopNode) {
			body += "[$END$]";
		}
		return body;
	}

}
