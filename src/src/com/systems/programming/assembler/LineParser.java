package com.systems.programming.assembler;

import com.systems.programming.assembler.Exceptions.*;
import com.systems.programming.assembler.ParseTree.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mohamed Mahmoud on 3/26/2017.
 */

/*
* TODO:
*       data persists from parse to the next
*       consider removing singleton or making a line class
*       ======================================================
*       consider removing the error list and throwing the exception
*       then catching it in the main class and printing it
* */

public class LineParser {

    private Line parsedLine;
    private String line;   //the four buckets or columns
    private List<String> errors;    //list of errors in a line of instruction
    private List<String> warnings;
    private static LineParser instance;
    private ParseNode node;
    private List<ParseNode>path;
    private LineParser() {
        errors = new ArrayList<>();
        warnings = new ArrayList<>();
        path = new ArrayList<>();
    }

    //This object is only an engine for parsing and we only need one, and we don't need it to preserve state
    //so guess what singleton hahahaha our all time friend
    public static LineParser getInstance() {
        if (instance== null)instance = new LineParser();
        return instance;
    }

    public void parse(String line)
    {
        parsedLine = new Line();
        //get a clean copy of the passed line
        this.line = line.trim();
        if(isAComment()){parsedLine.setComment(line);return;}

        //seperate comment from line first
        String tokens[] = line.split("\\.");
        //clean line after removing comment
        this.line = tokens[0].trim();
        if(tokens.length==2) parsedLine.setComment(tokens[1]);

        //now split the line itself into tokens
        tokens = this.line.split("\\s+");
        List<String>tokenList = new ArrayList<>(Arrays.asList(tokens));
        //this is needed while traversing the parse tree to detect extra argumants or missing arguments
        tokenList.add("");
        //clear the path
        path.clear();
        //start the journey to classify the line components
        node = new RootNode();
        node = getNext(node,tokenList.get(0));
        tokenList.stream().skip(1).forEach(token->node = getNext(node,token));
        //traverse the path and get components into corresponding variables
        path.forEach((ParseNode n) ->
        {
            if(n instanceof LabelNode)parsedLine.setLabel(n.getState());
            if(n instanceof InstructionNode || n instanceof DirectiveNode)parsedLine.setMnemonic(n.getState());
            if(n instanceof SingleArgNode || n instanceof DoubleArgsNode)parsedLine.setOperand(n.getState());
        });
    }

    private ParseNode getNext(ParseNode current,String token)
    {
        ParseNode node = null;
        try {
            node = current.nextNode(token);
            path.add(node);
        } catch (UnExpectedTokenException e) {
            System.err.println("unexpected token");
        }
        return node;
    }
    public String getMnemonic() {
        return parsedLine.getMnemonic();
    }

    public String getOperand() {
        return parsedLine.getOperand();
    }

    public String getLabel() {
        return parsedLine.getLabel();
    }

    public String getComment() {
        return parsedLine.getComment();
    }

    //checks if this line is a comment :D
    public boolean isAComment() {
        //this matches any any numbe of white spaces at beginning then a dot and any comment
        return line.matches("\\s?\\..*");
    }

    /*
    *    just checks if the label is unique for simplicity of address calculation, let's
    *    add to symbol table in the main class
    * */
    public boolean isUniqueLabel() throws DuplicateLabelException {
        if (parsedLine.getLabel() == null) return false;

        if (SymTab.getInstance().containsKey(parsedLine.getLabel()))throw new DuplicateLabelException();
        return true;
    }

    //static method to be accessed from remote classes
    public void addError(String error) {
        errors.add(error);
    }

    public void addWarning(String warning)
    {

    }

    //classifies mnemonic and gets length of instruction
    public int getLength() {
        /*
        * eedy 7ara2etny ya twinz bas el 7eta di betgeeb el length ezay?
        * lazem tkoon ya instruction ya directive el ghalat 7atraga3 -1
        * law el 2 -1 len b -1 hoba error :)
        * */
        int len = Math.max(OpTable.getInstance().getLength(getMnemonic()), DirectiveResolver.getInstance().getLength(getMnemonic(), getOperand()));
        if (len == -1) addError("Invalid mnemonic or operand to directive");
        return len;
    }

    public static void main(String[] args) {

        OpTable.getInstance().init(new File("E:\\aly\\projects\\SICXE-Assembler\\SICXE-Assembler\\SICXE-BOMBA\\src\\instructionSet"));
        System.out.println(OpTable.getInstance());
        try (BufferedReader br = new BufferedReader(new FileReader("E:\\aly\\projects\\SICXE-Assembler\\" +
                                                        "SICXE-Assembler\\SICXE-BOMBA\\src\\testProg.txt"))){
            br.lines().forEach(line->{
                LineParser.getInstance().parse(line);
                System.out.println(line);
                System.out.println("__________________________");
                System.out.println("label: "+LineParser.getInstance().getLabel());
                System.out.println("mnemonic: " + LineParser.getInstance().getMnemonic());
                System.out.println("operand: " + LineParser.getInstance().getOperand());
                System.out.println("comment : " + LineParser.getInstance().getComment());
                System.out.println("=================================================");

            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
