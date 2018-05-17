package lexer;

import static lexer.TokenType.FALSE;
import static lexer.TokenType.ID;
import static lexer.TokenType.INT;
import static lexer.TokenType.TRUE;
import static lexer.TransitionOutput.GOTO_ACCEPT_ID;
import static lexer.TransitionOutput.GOTO_ACCEPT_INT;
import static lexer.TransitionOutput.GOTO_EOS;
import static lexer.TransitionOutput.GOTO_FAILED;
import static lexer.TransitionOutput.GOTO_FALSE;
import static lexer.TransitionOutput.GOTO_MATCHED;
import static lexer.TransitionOutput.GOTO_MINUS;
import static lexer.TransitionOutput.GOTO_PLUS;
import static lexer.TransitionOutput.GOTO_SHARP;
import static lexer.TransitionOutput.GOTO_SIGN;
import static lexer.TransitionOutput.GOTO_SPECIAL_CHAR;
import static lexer.TransitionOutput.GOTO_START;
import static lexer.TransitionOutput.GOTO_TRUE;

enum State {
	/* �̺κп��� ��� ���� ������ �Ͼ�ٰ� �ص� ������ �ƴϴ�. */
	START {
		/*
		 * �̺κ��� ù�κ��̴�. ó�� ������ �˸��µ�, ��� ���̽��� �����ϰ� �״��� ���ڰ� ������ �������� ���� GoTo�� ������ �� Goto��
		 * �ٸ� �Լ��� ���°��� �ǹ��Ѵ�.
		 */
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			switch (ch.type()) {
			case LETTER:
				context.append(v);
				return GOTO_ACCEPT_ID;
			case DIGIT:
				context.append(v);
				return GOTO_ACCEPT_INT;
			case SIGN: {
				if(v == '+') {
				context.append(v);
				return GOTO_PLUS;
				}
				else {
					context.append(v);
					return GOTO_MINUS;
				}
//				context.getCharStream().pushBack(v);
			}
			case SPECIAL_CHAR:
				/*
				 * �̺κ��� �����Ͽ���. #T/F�� �����ϱ� ���ؼ� Special Char�� �ϳ��� #�� #�Լ��� ���� ����� �������� �������� �������.
				 */
				if (v == '#') {
					context.append(v);
					return GOTO_SHARP;
				} else {
					context.append(v);
					return GOTO_SPECIAL_CHAR;
				}
			case WS:
				return GOTO_START;
			case END_OF_STREAM:
				return GOTO_EOS;
			default:
				throw new AssertionError();
			}
		}
	},
	ACCEPT_ID {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			switch (ch.type()) {
			/*
			 * �̺κ��� ����,����, Ư�����ڰ� ������ �ٽ� ��ͷ� ������ ������� �˼��ִ�. ���⼭ Ư�����ڴ� �ƴѵ� �� �������Ĵ� ������ �ְ�����,
			 * ?�ΰ��� Question�̳� Keyword�� ����ϱ� ������ TokenŬ�������� ofName�Լ��κ��� ó���� ���ش�.
			 */
			case LETTER:
			case DIGIT:
			case SPECIAL_CHAR:
				context.append(v);
				return GOTO_ACCEPT_ID;
			case WS:
			case END_OF_STREAM:
				return GOTO_MATCHED(Token.ofName(context.getLexime()));
			/*
			 * �̺κ��� WS�̸鼭 �������϶� Token.ofName�Լ��� ȣ���� ���ǹ��� �����ش�. �̰��� Keyword, Question, ����ó������
			 * �� �� �־�� �ϱ⶧���� �Ѵ�. (DFA�� �ٰ� ID���ķ� ?�ܿ� �ٸ����� �� �� ����.)
			 */
			default:
				throw new AssertionError();
			}
		}
	},
	ACCEPT_INT {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			switch (ch.type()) {
			/*
			 * �̺κ��� �־��� �κ�������, ���ڴ����� �ٽ� ���ڰ� ������ Fail�̰� �������̶�� ���¸� INT �׸��� ���� �Է°��� ����ϴ� ����
			 * �ǹ��Ѵ�.
			 */
			case LETTER:
				return GOTO_FAILED;
			case DIGIT:
				context.append(ch.value());
				return GOTO_ACCEPT_INT;
			case SPECIAL_CHAR:
				return GOTO_FAILED;
			case WS:
			case END_OF_STREAM:
				return GOTO_MATCHED(INT, context.getLexime());
			default:
				throw new AssertionError();
			}
		}
	},
	SIGN {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			switch (ch.type()) {
			/*
			 * �̺κ��� SIGN�϶��� �����Ѵ�. Sign�κκ��� +�� -�κ��ε�, �������κп� ���ǹ��� �ɾ��־� +�϶� PLUS�� -�϶� MINUS��
			 * �����Ѵ�. �׷��ٸ� ����� ������ ��� ������ �ϴ°�? �ؿ� Digit �κ��� ���� Accept int�� �Ѿ�⶧���� ���´� INT��
			 * ������ �ȴ�.
			 */
			case LETTER:
				return GOTO_FAILED;
			case DIGIT:
				context.append(v);
				return GOTO_ACCEPT_INT;
			case WS:
			case END_OF_STREAM:
				return GOTO_FAILED;
			default:
				throw new AssertionError();
			}
		}
	},
	PLUS {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			switch (ch.type()) {
			/*
			 * �̺κ��� SIGN�϶��� �����Ѵ�. Sign�κκ��� +�� -�κ��ε�, �������κп� ���ǹ��� �ɾ��־� +�϶� PLUS�� -�϶� MINUS��
			 * �����Ѵ�. �׷��ٸ� ����� ������ ��� ������ �ϴ°�? �ؿ� Digit �κ��� ���� Accept int�� �Ѿ�⶧���� ���´� INT��
			 * ������ �ȴ�.
			 */
			case LETTER:
				return GOTO_FAILED;
			case DIGIT:
				context.append(v);
				return GOTO_ACCEPT_INT;
			case WS:
			case END_OF_STREAM:
				return GOTO_MATCHED(TokenType.PLUS, context.getLexime());

			default:
				throw new AssertionError();
			}
		}
	},	
	MINUS {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			switch (ch.type()) {
			/*
			 * �̺κ��� SIGN�϶��� �����Ѵ�. Sign�κκ��� +�� -�κ��ε�, �������κп� ���ǹ��� �ɾ��־� +�϶� PLUS�� -�϶� MINUS��
			 * �����Ѵ�. �׷��ٸ� ����� ������ ��� ������ �ϴ°�? �ؿ� Digit �κ��� ���� Accept int�� �Ѿ�⶧���� ���´� INT��
			 * ������ �ȴ�.
			 */
			case LETTER:
				return GOTO_FAILED;
			case DIGIT:
				context.append(v);
				return GOTO_ACCEPT_INT;
			case WS:
			case END_OF_STREAM:
				return GOTO_MATCHED(TokenType.MINUS, context.getLexime());
			default:
				throw new AssertionError();
			}
		}
	},
	SPECIAL_CHAR {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			switch (ch.type()) {
			/*
			 * �̺κ��� �߿��ϴ�. Special character ������ �ٸ����̿´ٸ� ������ fail �������̶�� �ѱ�� ���� �̾߱��Ѵ�.
			 */
			case LETTER:
				return GOTO_FAILED;
			case DIGIT:
				context.append(v);
				return GOTO_FAILED;
			case WS:
			case END_OF_STREAM:
				/*
				 * fromSpecialCahractor�� ����Ͽ� switch���� �������� ��ȯ�� ���¸� �̿��ϴ� ���̴�. chatAt�� ����ϴ� ����
				 * String ���� char�� ��ȯ���ֱ� �����̴�.
				 */
				String temp = context.getLexime();
				return GOTO_MATCHED(TokenType.fromSpecialCharactor(temp.charAt(0)), temp);
			default:
				throw new AssertionError();
			}
		}
	},
	SHARP {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			switch (ch.type()) {
			/*
			 * �̺κ��� ���� ���� �κ��ε�, #�� �޾��� �� ����� �Ѿ�´�. �״����� ���ڿ��� �ƴϸ� ���� fail, �׸��� ���ڿ� �߿����� T��
			 * ������ True�� ���� F�� ������ False�� ����.
			 */
			case LETTER:
				if (v == 'T') {
					context.append(v);
					return GOTO_TRUE;
				} else if (v == 'F') {
					context.append(v);
					return GOTO_FALSE;
				}
			case DIGIT:
				return GOTO_FAILED;
			case WS:
			case END_OF_STREAM:
				return GOTO_FAILED;

			default:
				throw new AssertionError();
			}
		}
	},
	TRUE {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			switch (ch.type()) {
			/*
			 * �̺κ��� �������� ������ �ȴ�. �������� TokenType���� switch������ ������ ���Ͽ��� ������ �׳� TokenType.True��
			 * ����Ͽ� ���¸� �����ϰ� ��ġ ��Ų��.
			 */
			case WS:
			case END_OF_STREAM:
				return GOTO_MATCHED(TokenType.TRUE, context.getLexime());
			default:
				throw new AssertionError();
			}
		}
	},
	FALSE {
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			switch (ch.type()) {
			/*
			 * �̺κ��� �������� ������ �ȴ�. �������� TokenType���� switch������ ������ ���Ͽ��� ������ �׳� TokenType.False��
			 * ����Ͽ� ���¸� �����ϰ� ��ġ ��Ų��.
			 */
			case LETTER:
				return GOTO_FAILED;
			case DIGIT:
				return GOTO_FAILED;
			case SPECIAL_CHAR:
				return GOTO_FAILED;
			case WS:
			case END_OF_STREAM:
				return GOTO_MATCHED(TokenType.FALSE, context.getLexime());
			default:
				throw new AssertionError();
			}
		}
	},
	MATCHED {
		@Override
		public TransitionOutput transit(ScanContext context) {
			throw new IllegalStateException("at final state");
		}
	},
	FAILED {
		@Override
		public TransitionOutput transit(ScanContext context) {
			throw new IllegalStateException("at final state");
		}
	},
	EOS {
		@Override
		public TransitionOutput transit(ScanContext context) {
			return GOTO_EOS;
		}
	};

	abstract TransitionOutput transit(ScanContext context);
}
