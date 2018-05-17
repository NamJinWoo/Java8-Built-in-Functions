package parser.parse;

import java.io.File;

import interpreter.CuteInterpreter;
import parser.ast.Node;

public class ParserMain {

	public static void main(String[] args) {
		ClassLoader cloader = CuteInterpreter.class.getClassLoader();
		File file = new File(cloader.getResource("parser/parse/as07.txt").getFile());
		CuteParser cuteParser = new CuteParser(file);
		Node parseTree = cuteParser.parseExpr();
		CuteInterpreter i = new CuteInterpreter();
		Node resultNode = i.runExpr(parseTree);
		NodePrinter.getPrinter(System.out).prettyPrint(resultNode);
	}
}
