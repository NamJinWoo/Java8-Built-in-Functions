package interpreter;

import parser.ast.*;
import parser.parse.CuteParser;
import java.io.File;

import parser.parse.NodePrinter;

import java.io.File;

public class CuteInterpreter {
	private void errorLog(String err) {
		System.out.println(err);
	}

	public Node runExpr(Node rootExpr) {
		if (rootExpr == null)
			return null;

		if (rootExpr instanceof IdNode)
			return rootExpr;
		else if (rootExpr instanceof IntNode)
			return rootExpr;
		else if (rootExpr instanceof BooleanNode)
			return rootExpr;
		else if (rootExpr instanceof ListNode)
			return runList((ListNode) rootExpr);
		else
			errorLog("run Expr error");
		return null;
	}

	private Node runList(ListNode list) {
		if (list.equals(ListNode.EMPTYLIST))
			return list;
		if (list.car() instanceof FunctionNode) {
			return runFunction((FunctionNode) list.car(), list.cdr());
		}
		if (list.car() instanceof BinaryOpNode) {
			return runBinary(list);
		}
		return list;
	}

	private Node runFunction(FunctionNode operator, ListNode operand) {
		switch (operator.value) {// 바꿈.
		// CAR, CDR, CONS등에 대한 동작 구현
		case CAR:
			return ((ListNode) ((QuoteNode) operand.car()).nodeInside()).car();
		// return operand.car();
		case CDR:
			return new QuoteNode(((ListNode) ((QuoteNode) operand.car()).nodeInside()).cdr());
		case CONS:
			if ((Node) operand.car() instanceof QuoteNode) {
				ListNode a = (ListNode) ((QuoteNode) operand.car()).nodeInside();
				ListNode x = (ListNode) ((QuoteNode) operand.cdr().car()).nodeInside();

				return new QuoteNode(ListNode.cons(a, x));
			} else
				return new QuoteNode(
						ListNode.cons((Node) operand.car(), (ListNode) ((QuoteNode) operand.cdr().car()).nodeInside()));
		case NULL_Q:
			if (((ListNode) ((QuoteNode) operand.car()).nodeInside()).equals(ListNode.ENDLIST)) {
				return BooleanNode.TRUE_NODE;
			} else {
				return BooleanNode.FALSE_NODE;
			}
		case ATOM_Q:
			// return ((QuoteNode)operand.car()).nodeInside();
			if (((QuoteNode) operand.car()).nodeInside() instanceof ListNode) {
				return BooleanNode.FALSE_NODE;
			} else
				return BooleanNode.TRUE_NODE;
		case EQ_Q:
			Node x = ((QuoteNode) operand.car()).nodeInside();
			Node y = ((QuoteNode) operand.cdr().car()).nodeInside();
			// return ((QuoteNode)operand.car()).nodeInside();
			if (x.toString().equals(y.toString())) {
				return BooleanNode.TRUE_NODE;
			} else
				return BooleanNode.FALSE_NODE;
		case COND:
			ListNode c = operand;
//			ListNode k = (ListNode) runExpr(c);
			while (!c.equals(ListNode.ENDLIST)) {
				
				if (runExpr(((ListNode)c.car()).car()).equals(BooleanNode.TRUE_NODE)) {
					
					return ((ListNode)c.car()).cdr().car();
				}
				else {
					c = c.cdr();
				}
			}
			return null;
			// return k.car();
			// return runExpr(c);
		case NOT:
			Node a = operand.car();
			if (runExpr(a).equals(BooleanNode.TRUE_NODE)) {
				return BooleanNode.FALSE_NODE;
			} else
				return BooleanNode.TRUE_NODE;
		default:
			break;
		}
		return null;
	}

	private Node runBinary(ListNode list) {
		BinaryOpNode operator = (BinaryOpNode) list.car(); // 바꿈.

		Node x = list.cdr().car();
		Node y = list.cdr().cdr().car();
		// 구현과정에서 필요한 변수 및 함수 작업 가능
		switch (operator.value) {

		// +,-,/ 등에 대한 바이너리 연산 동작 구현
		case PLUS:
			return new IntNode(Integer.toString(((IntNode) runExpr(x)).value + ((IntNode) runExpr(y)).value));
		case MINUS:
			return new IntNode(Integer.toString(((IntNode) runExpr(x)).value - ((IntNode) runExpr(y)).value));
		case DIV:
			return new IntNode(Integer.toString(((IntNode) runExpr(x)).value / ((IntNode) runExpr(y)).value));
		case TIMES:
			return new IntNode(Integer.toString(((IntNode) runExpr(x)).value * ((IntNode) runExpr(y)).value));
		case LT:
			if (((IntNode) runExpr(x)).value < ((IntNode) runExpr(y)).value) {
				return BooleanNode.TRUE_NODE;
			} else
				return BooleanNode.FALSE_NODE;
		case GT:
			if (((IntNode) runExpr(x)).value > ((IntNode) runExpr(y)).value) {
				return BooleanNode.TRUE_NODE;
			} else
				return BooleanNode.FALSE_NODE;
		case EQ:
			if (((IntNode) runExpr(x)).value.equals(((IntNode) runExpr(y)).value)) {
				return BooleanNode.TRUE_NODE;
			} else
				return BooleanNode.FALSE_NODE;

		default:
			break;
		}
		return null;
	}

	private Node runQuote(ListNode node) {
		return ((QuoteNode) node.car()).nodeInside();
	}
}