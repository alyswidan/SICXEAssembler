package com.systems.programming.assembler;

import com.systems.programming.assembler.Exceptions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by ADMIN on 3/28/2017.
 */
public class DirectiveResolver {
    private static DirectiveResolver ourInstance;
    private static String startAddress;
    private String directives[] = {"word", "byte", "resb", "resw", "base",
                                    "start", "end", "nobase", "org", "equ",
                                    "csect", "extref"};
    private String hasNoObjectCode[] = {"end", "start", "base", "nobase",
                                        "ltorg", "equ", "org", "csect"};
    private String isPass1[] = {"extref","equ"};
    private Map<String, Method> handlers = new HashMap<>(13);

    private DirectiveResolver() {


        try {

            handlers.put("start", getClass().getMethod("executeStart", String.class));
            handlers.put("base", getClass().getMethod("executeBase", String.class));
            handlers.put("nobase", getClass().getMethod("executeNoBase"));
            handlers.put("resb", getClass().getMethod("parseResb", String.class));
            handlers.put("resw", getClass().getMethod("parseResw", String.class));
            handlers.put("byte", getClass().getMethod("parseByte", String.class));
            handlers.put("word", getClass().getMethod("parseWord", String.class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }
    public boolean isPass1(String dir){
        return isDirective(dir) && Stream.of(isPass1).anyMatch(dir::equalsIgnoreCase);
    }

    public static DirectiveResolver getInstance() {
        if (ourInstance == null) {
            ourInstance = new DirectiveResolver();
        }
        return ourInstance;
    }

    public boolean isAReserve(String s) {
        return s.equalsIgnoreCase("resw") || s.equalsIgnoreCase("resb");
    }

    public boolean isDirective(String test) {
        return Arrays.stream(directives).anyMatch(elem -> elem.equalsIgnoreCase(test));
    }

    public boolean isAssemblerExecutable(String mnemonic) {
        return Arrays.stream(hasNoObjectCode).anyMatch(x -> x.equalsIgnoreCase(mnemonic));
    }


    public int getLength(String mnemonic, String operand) {
        ObjectCode objectCode = getObjectCode(mnemonic, operand);
        if (objectCode == null)
            return 0;
        else return objectCode.getLength();
    }

    public ObjectCode getObjectCode(String mnemonic, String operand) {
        ObjectCode ans = null;
        if (isExpression(operand))
            operand = String.valueOf(evalExpression(operand));

        if (mnemonic.equalsIgnoreCase("byte")) ans = parseByte(operand);
        else if (mnemonic.equalsIgnoreCase("word")) ans = parseWord(operand);
        else if (mnemonic.equalsIgnoreCase("resb")) ans = parseResb(operand);
        else if (mnemonic.equalsIgnoreCase("resw")) ans = parseResw(operand);
        return ans;
    }

    public void executeDirective(String mnemonic, String operand) throws UndefinedMnemonicException {
        if (mnemonic.equalsIgnoreCase("start")) {
            startAddress = operand;
            executeStart(operand);
        } else if (mnemonic.equalsIgnoreCase("base")) executeBase(operand);
        else if (mnemonic.equalsIgnoreCase("nobase")) executeNoBase();
        else if (mnemonic.equalsIgnoreCase("end")) executeEnd(operand);
        else if (mnemonic.equalsIgnoreCase("extdef")) executeExtDef(operand);
    }

    private void executeExtDef(String operand) {


    }

    public void executeOrg(String operand) {
        Assembler.setLocationCounter(Integer.parseInt(operand));
    }

    private void executeEnd(String operand) {
        Assembler.setProgramLength(Assembler.getLocationCounter() - Integer.parseInt(startAddress, 16));
    }

    public static String getStartAddress() {
        return startAddress;
    }

    public ObjectCode parseByte(String operand) {
        ObjectCode objectCode = new ObjectCode();
        objectCode.setDirective(true);
        try {
            //try to parse as an integer
            objectCode.setOpcode(String.format("%02X", Integer.parseInt(operand)));
            objectCode.setLength(1);
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
            if (hexMatcher.find()) {
                objectCode.setOpcode(hexMatcher.group(1));
            } else if (charMatcher.find()) {
                StringBuilder builder = new StringBuilder();
                charMatcher.group(1).chars().mapToObj(i -> String.format("%02X", i)).forEach(builder::append);
                objectCode.setOpcode(builder.toString());
            }
            System.out.println("operand for byte is " + objectCode.getOpcode().length() / 2);

            objectCode.setLength(objectCode.getOpcode().length() / 2);
        }
        return objectCode;
    }

    public ObjectCode parseWord(String operand) {
        ObjectCode objectCode = new ObjectCode();
        objectCode.setOpcode(String.format("%06X", Integer.parseInt(operand)));
        objectCode.setDirective(true);
        objectCode.setLength(3);
        return objectCode;
    }

    public ObjectCode parseResb(String operand) {
        ObjectCode o = new ObjectCode();
        o.setLength(Integer.parseInt(operand));
        return o;
    }

    public ObjectCode parseResw(String operand) {
        ObjectCode o = new ObjectCode();
        o.setLength(3 * Integer.parseInt(operand));
        return o;
    }

    // TODO: 13/05/17 evaluate an expression get its type and check if it is an expression
    public int evalExpression(String operand) {
        SymTab symTab = SymTab.getInstance();
        int res = 0;
        String prev = null;
        List<String> tokens = tokenizeExpression(operand);
        for (String curr : tokens) {
            if (curr.equals("+") || curr.equals("-")) {
                prev = curr;
                continue;
            }
            int sign = prev == null || prev.equals("+") ? 1 : -1;
            if (curr.matches("[0-9]+")) {
                res += sign * Integer.parseInt(curr);
            } else
                res += sign * symTab.get(curr);
            prev = curr;
        }
        return res;
    }

    public List<String> tokenizeExpression(String operand) {
        List<String> tokens = new ArrayList<>();
        Pattern p = Pattern.compile("([+-])|([a-zA-Z]+)|(\\d+)");
        Matcher m = p.matcher(operand);
        while (m.find())
            tokens.add(m.group(0));

        return tokens;
    }

    public SymTab.Type getExpressionType(String operand) throws InvalidExpressionException {
        SymTab symTab = SymTab.getInstance();
        if(operand.matches("\\d+"))return SymTab.Type.ABSOLUTE;
        if(operand.matches("[a-zA-z]+"))return SymTab.Type.RELATIVE;
        int freq[] = new int[2];
        String prev = null;
        List<String> tokens = tokenizeExpression(operand);
        for (String curr : tokens) {
            if (curr.equals("+")
                    || curr.equals("-")
                    || !symTab.containsKey(curr)
                    || symTab.getType(curr) == SymTab.Type.ABSOLUTE) {
            } else if ((prev == null || prev.equals("+")))
                freq[symTab.getCSect(curr).equals(Assembler.getProgName()) ? 0 : 1]++;
            else if (prev.equals("-"))
                freq[symTab.getCSect(curr).equals(Assembler.getProgName()) ? 0 : 1]--;
            prev = curr;
        }
        System.out.println(operand+"->"+freq[0]+","+freq[1]);
        if (freq[0] != 1 && freq[0] != 0 || freq[1] != 1 && freq[1] != 0)
            throw new InvalidExpressionException();
        if (freq[0] == freq[1] && freq[0] == 0)
            return SymTab.Type.ABSOLUTE;
        else if (freq[0] != freq[1])
            return SymTab.Type.RELATIVE;
        else throw new InvalidExpressionException();

    }

    public boolean isExpression(String expr) {
        return expr.chars().anyMatch(i -> "+-*/".chars().anyMatch(j -> i == j));
    }

    public void executeStart(String operand) {
        Assembler.setLocationCounter(Integer.parseInt(operand, 16));
    }

    public void executeBase(String operand) {
        if (LineParser.getInstance().getMode() == LineParser.Mode.DEEP) {
            LineParser.getInstance().setBase(Integer.parseInt(operand));
        }
    }

    public void executeNoBase() {
        LineParser.getInstance().deactivateBase();
    }

    public void executeExtRef(String operands) {
        Stream.of(operands.split(",")).forEach(ref -> {
            try {
                SymTab.getInstance().putFull(ref, 0, SymTab.Type.RELATIVE,"other");
            } catch (DuplicateLabelException e) {
                e.printStackTrace();
            }
        });
    }

    // TODO: 13/05/17 this should add the label to the sym table evaluate the operand if it is an expr and replace expression with its value in the intermediate file
    public void executeEqu(Line parsedLine) throws InvalidExpressionException, DuplicateLabelException {
        String label = parsedLine.getLabel(), operand = parsedLine.getOperand();
        SymTab.Type type = getExpressionType(operand);
        SymTab.getInstance().putFull(label, evalExpression(operand),type,Assembler.getProgName());
        System.out.println(label+"->type = " + type);
    }
}



