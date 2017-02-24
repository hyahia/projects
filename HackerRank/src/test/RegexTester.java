package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTester {

	public static void main(String[] args) {
		String text="aa1AZyuy";
		Pattern pattern4=Pattern.compile("^[[a-z]{2,}[0-9]{1,}[A-Z]{2,}]{10,15}");
		Matcher matcher = pattern4.matcher(text);
		System.out.println(matcher.matches());
	}

}
