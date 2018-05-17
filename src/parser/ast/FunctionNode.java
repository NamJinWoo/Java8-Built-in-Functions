package parser.ast;

import java.util.HashMap;
import java.util.Map;

import lexer.TokenType;

public class FunctionNode implements Node { // binaryOpNodeŬ������ ���� �����ؼ� �ۼ�
	//�켱 binaryOpNode������ ���� enum�� ������ش�.
	public enum FunctionType {
		ATOM_Q { //3���� ����ó�� AtomQ�� �� ��ūŸ���� AtomQ�� ����
			TokenType tokenType() {
				return TokenType.ATOM_Q;
			}
		},
		CAR { //CAR�� �� ��ūŸ���� CAR�� ����
			TokenType tokenType() {
				return TokenType.CAR;
			}
		},
		CDR { //CAR�� �� ��ūŸ���� CDR�� ����
			TokenType tokenType() {
				return TokenType.CDR;
			}
		},
		COND { //CDR�� �� ��ūŸ���� COND�� ����
			TokenType tokenType() {
				return TokenType.COND;
			}
		},
		CONS { //CONS�� �� ��ūŸ���� CONS�� ����
			TokenType tokenType() {
				return TokenType.CONS;
			}
		},
		DEFINE { //DEFINE�� �� ��ūŸ���� DEFINE�� ����
			TokenType tokenType() {
				return TokenType.DEFINE;
			}
		},
		EQ_Q { //EQQ�� �� ��ūŸ���� EQQ�� ����
			TokenType tokenType() {
				return TokenType.EQ_Q;
			}
		},
		LAMBDA { //LAMBDA�� �� ��ūŸ���� LAMBDA�� ����
			TokenType tokenType() {
				return TokenType.LAMBDA;
			}
		},
		NOT { //NOT�� �� ��ūŸ���� NOT�� ����
			TokenType tokenType() {
				return TokenType.NOT;
			}
		},
		NULL_Q { //NULLQ�� �� ��ūŸ���� NULLQ�� ����
			TokenType tokenType() {
				return TokenType.NULL_Q;
			}
		};
		private static Map<TokenType, FunctionType> fromTokenType = new HashMap<TokenType, FunctionType>();
		static {
			for (FunctionType fType : FunctionType.values()) {
				fromTokenType.put(fType.tokenType(), fType);
			}
		}

		static FunctionType getFunctionType(TokenType tType) {
			return fromTokenType.get(tType);
		}
		abstract TokenType tokenType();
		/*�̺κе� binaryopNode�� �����Ͽ� �����ϰ� �ۼ��Ѵ�.*/
	
	}
	public FunctionType value;
	
	public FunctionNode(TokenType tType) {
		FunctionType fType = FunctionType.getFunctionType(tType);
		value = fType;// ���� ä���
		// TODO Auto-generated constructor stub
	}
	@Override
	/*���⵵ binaryOpNode�κа� �Ȱ����ϸ�ȴ�.*/
	public String toString() {
		return value.name();// ���� ä���
	}	
}