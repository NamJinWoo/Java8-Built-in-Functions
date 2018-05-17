package lexer;
import java.util.*;

import lexer.Char.CharacterType;


public enum TokenType {
	ID,
	INT, QUESTION,
	TRUE, FALSE, NOT,
	PLUS, MINUS, TIMES, DIV,
	LT, GT, EQ, APOSTROPHE,
	L_PAREN, R_PAREN, SHARP,
	DEFINE, LAMBDA, COND, QUOTE,
	CAR, CDR, CONS,
	ATOM_Q, NULL_Q, EQ_Q;
	static TokenType fromSpecialCharactor(char ch) {
		/*���⼭�� �Լ� �̸� �״�� Special Charactor�� ������ �͵��� ���������� �����ش�.
		 * �̰͵��� StateŬ�������� �̿��� �ɰǵ� ���� ������ ���¸� ����Ʈ�ؾ� �Ǳ⶧����
		 * StateŬ�������� Start���� Special Character�� �޾����� TokenType.fromSpecialCharacter�� 
		 * ȣ�����ָ� ���� switch���� ���鼭 Goto Match �κп��� ��� ���������� ��ȯ�� �� �� �������� �����Ѵ�.*/
		/*������� case���� ���� ���ϴ� ������ �ֱ⶧���� ��ȯ������ �ʾ����� StateŬ�������� ����� ���� �����ϴ�.
		 * ������ �ᱹ���� TokenType.True�� ���� Ŭ������ ���¸� ȣ�����־���Ѵ�.*/
		switch ( ch ) {
			case '-':
				return MINUS;
			case '+':
				return PLUS;
			case '*':
				return TIMES;
			case '/':
				return DIV;
			case '(':
				return L_PAREN;
			case ')':
				return R_PAREN;
			case '<':
				return LT;
			case '=':
				return EQ;
			case '>':
				return GT;
			case '`':
				return APOSTROPHE;
			case '#':
				return SHARP;
		//���� ǥ������ �����Ͽ� ch�� ��Ī�Ǵ� keyword�� ��ȯ�ϴ� case�� �ۼ�
		default:
			throw new IllegalArgumentException("unregistered char: " + ch);
		}
	}
}
