package laba;

public class Demo {
	
	private StringBuffer currentSpelling = new StringBuffer();
	private String source = "false";
	private int counter = 0;
	private char currentChar = source.charAt(counter);
	private boolean response;
	private void takeIt() {
		currentSpelling.append(currentChar);
		counter++;
		currentChar = source.charAt(counter);
	}
	private boolean isBoolean() {
		char[] trueChars = "rue".toCharArray();
		char[] falseChars = "alse".toCharArray();
		if (currentChar == 't') {
			for (char ch : trueChars) {
				takeIt();
				if (currentChar != ch)
					return false;
			}
		} else if (currentChar == 'f') {
			for (char ch : falseChars) {
				takeIt();
				if (currentChar != ch)
					return false;
			}
		}
		return true;
	}
	public static void main(String[] args) {
		Demo demo = new Demo();
		System.out.println(demo.isBoolean());

	}

}
