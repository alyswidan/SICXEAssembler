package com.systems.programming.assembler;

import com.systems.programming.assembler.Exceptions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ADMIN on 3/28/2017.
 */
public class DirectiveResolver {
    private static DirectiveResolver ourInstance;
    private static String startAddress;
    private String directives[] = {"word", "byte", "resb", "resw", "base", "start", "end", "nobase","org","equ","csect"};
    private String hasNoObjectCode[] = {"end", "start", "base", "nobase", "ltorg","equ","org","csect"};
    private String operators[] = {"+","-","*","/"};
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

    public static DirectiveResolver getInstance() {
        if (ourInstance == null) {
            ourInstance = new DirectiveResolver();
        }
        return ourInstance;
    }

    public boolean isAReserve(String s)
    {
        return s.equalsIgnoreCase("resw") || s.equalsIgnoreCase("resb");
    }
    public boolean isDirective(String test) {
        return Arrays.stream(directives).anyMatch(elem -> elem.equalsIgnoreCase(test));
    }

    public boolean isAssemblerExecutable(String mnemonic) {
        return Arrays.stream(hasNoObjectCode).anyMatch(x -> x.equalsIgnoreCase(mnemonic));
    }


    public int getLength(String mnemonic, String operand) {
        ObjectCode objectCode = getObjectCode(mnemonic,operand);
        if(objectCode == null)
            return 0;
        else return objectCode.getLength();
    }

    public ObjectCode getObjectCode(String mnemonic, String operand) {
        ObjectCode ans = null;
        if(isExpression(operand))
            operand = String.valueOf(evalExpression(operand));

        if (mnemonic.equalsIgnoreCase("byte")) ans = parseByte(operand);
        else if (mnemonic.equalsIgnoreCase("word")) ans = parseWord(operand);
        else if (mnemonic.equalsIgnoreCase("resb")) ans = parseResb(operand);
        else if (mnemonic.equalsIgnoreCase("resw")) ans = parseResw(operand);
        return ans;
    }

    public void executeDirective(String mnemonic,String operand) throws UndefinedMnemonicException
    {
        if(mnemonic.equalsIgnoreCase("start")){
            startAddress = operand;
            executeStart(operand);
        }
        else if(mnemonic.equalsIgnoreCase("base"))executeBase(operand);
        else if(mnemonic.equalsIgnoreCase("nobase"))executeNoBase();
        else if(mnemonic.equalsIgnoreCase("end"))executeEnd(operand);
        else if(mnemonic.equalsIgnoreCase("org"))executeOrg(operand);
    }

    private void executeOrg(String operand) {
        Assembler.setLocationCounter(Integer.parseInt(operand));
    }

    private void executeEnd(String operand) {
        Assembler.setProgramLength(Assembler.getLocationCounter() - Integer.parseInt(startAddress,16));
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
            System.out.println("operand for byte is "+objectCode.getOpcode().length()/2);

            objectCode.setLength(objectCode.getOpcode().length()/2);
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
    public String evalExpression(String expr)
    {
        return null;
    }
    public SymTab.Type getExpressionType(String expr) throws InvalidExpressionException
    {
        return SymTab.Type.ABSOLUTE;
    }

    public boolean isExpression(String expr)
    {
        return Arrays.stream(operators).anyMatch(expr::equals);
    }



    public void executeStart(String operand)
    {
        Assembler.setLocationCounter(Integer.parseInt(operand, 16));
    }

    public void executeBase(String operand) {
        if(LineParser.getInstance().getMode()== LineParser.Mode.DEEP)
        {
            LineParser.getInstance().setBase(Integer.parseInt(operand));
        }
    }


    public void executeNoBase() {
        LineParser.getInstance().deactivateBase();
    }


    // TODO: 13/05/17 this should add the label to the sym table evaluate the operand if it is an expr and replace expression with its value in the intermediate file
    public void executeEqu(Line parsedLine)
    {
        int value;
        try {
            value = Integer.parseInt(parsedLine.getOperand());
            //This will put in SymTable the label with its value not with the address
            SymTab.getInstance().put(parsedLine.getLabel(),Integer.parseInt(parsedLine.getOperand()));
        } catch (NumberFormatException ex) {
            if(SymTab.getInstance().containsKey(parsedLine.getOperand()))
                try {
                    SymTab.getInstance().put(parsedLine.getLabel(),SymTab.getInstance().get(parsedLine.getOperand()));
                } catch (DuplicateLabelException e) {
                    e.printStackTrace();
                }

        } catch (DuplicateLabelException e) {
            e.printStackTrace();
        }
    }


}



