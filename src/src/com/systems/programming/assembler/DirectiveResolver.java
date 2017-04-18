package com.systems.programming.assembler;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ADMIN on 3/28/2017.
 */
public class DirectiveResolver {
    private static DirectiveResolver ourInstance = new DirectiveResolver();

    private String directives[] = {"word", "byte", "resb", "resw", "base", "start", "end"};

    private DirectiveResolver() {
    }

    public static DirectiveResolver getInstance() {
        return ourInstance;
    }

    public boolean isDirective(String test) {
        return Arrays.stream(directives).anyMatch(elem -> elem.equals(test));
    }

    public int getLength(String directive, String operand) {

        int len = -2;
        switch (directive) {
            case "WORD":
                len = 3;
            case "RESB":
                len = Integer.parseInt(operand);
            case "RESW":
                len = Integer.parseInt(operand) * 3;
            case "BYTE": {
                try {
                    //try to parse as an integer
                    len = Integer.parseInt(operand);
                }
                //if this is not a valid integer try to parse as a literal
                catch (NumberFormatException ex) {
                    //valid hex literal??
                    /*
                    * X->starts with X,(what is between those is the value returned),[]->what's between those are all
                    * possible ranges(which are all possible hex digits in this case),*->this means what is before it is
                    * to be repeated more than one time
                    * $->this means end of string meaning that the ' dadas ' should be the only
                    * thing in the string
                    * */
                    Matcher hexMatcher = Pattern.compile("X'([a-zA-Z0-9]*)'$").matcher(operand);
                    //valid char literal??
                    /*
                    * this does the exact same thing except that it matches any character not nesecery valid
                    * hex digits
                    *
                    * */
                    Matcher charMatcher = Pattern.compile("C'(.*)'$").matcher(operand);
                    //we then get group 1 since 0 is the whole string ie:C'dasdas'
                    if (hexMatcher.find()) len = hexMatcher.group(1).length();
                    else if (charMatcher.find()) len = charMatcher.group(1).length();
                }
            }
        }
        return len;
    }
}
