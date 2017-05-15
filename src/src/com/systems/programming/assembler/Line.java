package com.systems.programming.assembler;

import com.systems.programming.assembler.Exceptions.UndefinedMnemonicException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ADMIN on 3/30/2017.
 */
public class Line {
    private static String start, pLength;
    private String label, mnemonic, operand, comment, line;
    private ObjectCode objectCode;
    private int address = -1;
    private List<String> tokenList;
    private ArgType argType;

    public ArgType getArgType() {
        return argType;
    }

    public void setArgType(ArgType argType) {
        this.argType = argType;
    }

    public boolean isCSECT() {
        return mnemonic!=null && mnemonic.equalsIgnoreCase("CSECT");
    }

    public enum ArgType {IMMEDIATE,INDIRECT,DIRECT};


    public Line(String line) {
        this.line = line.trim();
        setAddress();
        if (address != -1) {
            List<String> s = getTokens();
            getTokens().remove(0);
            this.line = s.stream().collect(Collectors.joining(""));
        }
    }

    public boolean isEmpty() {
        return line.matches("^(\\n*|\\s*)$");
    }

    //checks if this line is a comment :D
    public boolean isComment() {
        //this matches any any number of white spaces at beginning then a dot and any comment
        return line.matches("\\s*\\..*");
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;

    }

    public String getOperand() {
        return operand;
    }

    public void setOperand(String operand) {
        this.operand = operand;
    }

    public String getComment() {

        if (comment == null) {
            if (isComment())
                setComment(line);
            else {
                //split into tokens
                String tokens[] = line.split("\\.");
                //clean line after removing comment
                line = tokens[0].trim();
                //if there's a comment add it
                if (tokens.length == 2) setComment("." + tokens[1]);
            }
        }

        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment.replaceAll("\\s", "");
    }

    public List<String> getTokens() {
        if (tokenList == null) {
            tokenList = new ArrayList<>(Arrays.asList(line.split("\\s+")));
            //this is needed while traversing the parse tree to detect extra arguments or missing arguments
            tokenList.add("");
        }
        return tokenList;

    }

    public boolean isStart() {

        if (mnemonic.equalsIgnoreCase("start"))
            start = this.getOperand();
        return mnemonic.equalsIgnoreCase("start");
    }
    public boolean isEqu() {
        return mnemonic.equalsIgnoreCase("equ");
    }


    public boolean isEnd() {
        return mnemonic.equalsIgnoreCase("end");
    }

    //classifies mnemonic and gets length of instruction
    public int getLength() throws UndefinedMnemonicException {

        int len = -1;
        if (OpTable.getInstance().isInstruction(getMnemonic()))
            len = OpTable.getInstance().getLength(getMnemonic());
        else if (DirectiveResolver.getInstance().isDirective(getMnemonic()))
            len = DirectiveResolver.getInstance().getLength(getMnemonic(), getOperand());
        if (len == -1) throw new UndefinedMnemonicException();
        return len;
    }

    public static void setLength(String pLength) {
        Line.pLength = pLength;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public void setAddress() {
        try {
            address = Integer.parseInt(getTokens().get(0), 16);
        } catch (NumberFormatException ex) {
            address = -1;
        }

    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public boolean isAssemblerExecutable() {
        return DirectiveResolver.getInstance().isDirective(mnemonic)
                && DirectiveResolver.getInstance().isAssemblerExecutable(mnemonic);
    }

    public void execute() throws UndefinedMnemonicException {

        DirectiveResolver.getInstance().executeDirective(mnemonic, operand);
    }

    public boolean hasOpCode() {
        return !DirectiveResolver.getInstance().isAssemblerExecutable(mnemonic)
                && !DirectiveResolver.getInstance().isAReserve(mnemonic);
    }

    public ObjectCode getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(ObjectCode objectCode) {
        this.objectCode = objectCode;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Arrays.asList(getLabel(),
                getMnemonic(),
                getOperand(),
                getComment()).
                forEach(token -> builder.append(String.format("%15s", token == null ? "" : token)));
        return builder.toString();
    }


}
