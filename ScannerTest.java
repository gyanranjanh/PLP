package cop5556sp17;

import static cop5556sp17.Scanner.Kind.*;
import static cop5556sp17.Scanner.LinePos;
import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cop5556sp17.Scanner.IllegalCharException;
import cop5556sp17.Scanner.IllegalNumberException;

public class ScannerTest {

	@Rule
    public ExpectedException thrown = ExpectedException.none();


	
	@Test
	public void testEmpty() throws IllegalCharException, IllegalNumberException {
		String input = "";
		Scanner scanner = new Scanner(input);
		scanner.scan();
	}

	@Test
	public void testSemiConcat() throws IllegalCharException, IllegalNumberException {
		//input string
		String input = ";;;";:
		//create and initialize the scanner
		Scanner scanner = new Scanner(input);
		scanner.scan();
		//get the first token and check its kind, position, and contents
		Scanner.Token token = scanner.nextToken();
		
		assertEquals(SEMI, token.kind);
		assertEquals(0, token.pos);
		String text = SEMI.getText();
		assertEquals(text.length(), token.length);
		assertEquals(text, token.getText());
		
		//get the next token and check its kind, position, and contents
		Scanner.Token token1 = scanner.nextToken();
		assertEquals(SEMI, token1.kind);
		assertEquals(1, token1.pos);
		assertEquals(text.length(), token1.length);
		assertEquals(text, token1.getText());
		
		Scanner.Token token2 = scanner.nextToken();
		assertEquals(SEMI, token2.kind);
		assertEquals(2, token2.pos);
		assertEquals(text.length(), token2.length);
		assertEquals(text, token2.getText());
		
		//check that the scanner has inserted an EOF token at the end
		Scanner.Token token3 = scanner.nextToken();
		assertEquals(Scanner.Kind.EOF,token3.kind);
	}
	
	/**
	 * This test illustrates how to check that the Scanner detects errors properly. 
	 * In this test, the input contains an int literal with a value that exceeds the range of an int.
	 * The scanner should detect this and throw and IllegalNumberException.
	 * 
	 * @throws IllegalCharException
	 * @throws IllegalNumberException
	 */
	@Test
	public void testIntOverflowError() throws IllegalCharException, IllegalNumberException{
		String input = "99999999999999999";
		Scanner scanner = new Scanner(input);
		thrown.expect(IllegalNumberException.class);
		scanner.scan();		
	}
	
	@Test
	public void testKeyWords() throws IllegalCharException, IllegalNumberException{
		String input = "if ifa";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		//get the first token and check its kind, position, and contents
		Scanner.Token token = scanner.nextToken();
		assertEquals(KW_IF, token.kind);
		assertEquals("if", token.getText());
		
		Scanner.Token token1 = scanner.nextToken();
		assertEquals(IDENT, token1.kind);
		assertEquals("ifa", token1.getText());
		
		//check that the scanner has inserted an EOF token at the end
		Scanner.Token token2 = scanner.nextToken();
		assertEquals(Scanner.Kind.EOF,token2.kind);
	}

	@Test
	public void testIntLiteral() throws IllegalCharException, IllegalNumberException{
		String input = "0234";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		
		assertEquals(3, scanner.getNoOfTokens());
		
		//get the first token and check its kind, position, and contents
		Scanner.Token token = scanner.nextToken();
		assertEquals(INT_LIT, token.kind);
		assertEquals("0", token.getText());
		
		Scanner.Token token1 = scanner.nextToken();
		assertEquals(INT_LIT, token1.kind);
		assertEquals("234", token1.getText());
		
		//check that the scanner has inserted an EOF token at the end
		Scanner.Token token2 = scanner.nextToken();
		assertEquals(Scanner.Kind.EOF, token2.kind);
	}

	@Test
	public void testLinePos() throws IllegalCharException, IllegalNumberException{
		String input = "0\n234  pole";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		
		assertEquals(4, scanner.getNoOfTokens());
		
		//get the first token and check its kind, position, and contents
		Scanner.Token token = scanner.nextToken();
		assertEquals(INT_LIT, token.kind);
		assertEquals("0", token.getText());
		LinePos linepos = token.getLinePos();
		assertEquals(0, linepos.line);
		assertEquals(0, linepos.posInLine);
		
		Scanner.Token token1 = scanner.nextToken();
		assertEquals(INT_LIT, token1.kind);
		assertEquals("234", token1.getText());
		LinePos linepos1 = token1.getLinePos();
		assertEquals(1, linepos1.line);
		assertEquals(1, linepos1.posInLine);
		
		//check that the scanner has inserted an EOF token at the end
		Scanner.Token token2 = scanner.nextToken();
		assertEquals(IDENT, token2.kind);
		assertEquals("pole", token2.getText());
		LinePos linepos2 = token2.getLinePos();
		assertEquals(1, linepos2.line);
		assertEquals(4, linepos2.posInLine);
		
		Scanner.Token token3 = scanner.nextToken();
		assertEquals(EOF, token3.kind);	
	}
}
