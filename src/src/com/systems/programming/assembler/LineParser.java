package com.systems.programming.assembler;

import com.systems.programming.assembler.Exceptions.AssemblerException;
import com.systems.programming.assembler.Exceptions.UnExpectedTokenException;
import com.systems.programming.assembler.ParseTree.*;

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
    private static LineParser instance;
    private String start;
    private int mod,base=0,currentLocation;
    private ParseNode node;
    private List<ParseNode> path;


    private LineParser() {
        path = new ArrayList<>();
    }

    //This object is only an engine for parsing and we only need one, and we don't need it to preserve state
    //so guess what singleton hahahaha our all time friend
    public static LineParser getInstance() {
        if (instance == null) instance = new LineParser();
        return instance;
    }

    public Line parse(String line) throws AssemblerException {

        Line parsedLine = new Line(line);
        if (parsedLine.isEmpty()) return parsedLine;

        //get a clean copy of the passed line
        line = line.trim();
        //if the whole line is a comment return
        if (parsedLine.isComment()) {
            parsedLine.setComment(line);
            return parsedLine;
        }

        //seperate comment from line first
        String tokens[] = line.split("\\.");

        //clean line after removing comment
        line = tokens[0].trim();

        //if there's a comment add it
        if (tokens.length == 2) parsedLine.setComment("." + tokens[1]);

        //now split the line itself into tokens
        tokens = line.split("\\s+");
        List<String> tokenList = new ArrayList<>(Arrays.asList(tokens));
        //this is needed while traversing the parse tree to detect extra argumants or missing arguments
        tokenList.add("");
        //clear the path
        path.clear();

        //start the journey to classify the line components
        node = new RootNode();
        node = getNextNode(node, tokenList.get(0));


        //let the line travel through the tree
        for (int i = 1; i < tokenList.size(); i++) {
            node = getNextNode(node, tokenList.get(i));
        }

        //traverse the path and get components into corresponding variables
        path.forEach((ParseNode n) ->
        {
            if (n instanceof LabelNode) parsedLine.setLabel(n.getState("label"));
            if (n instanceof InstructionNode) parsedLine.setMnemonic(n.getState("instruction"));
            if (n instanceof DirectiveNode)    parsedLine.setMnemonic(n.getState("directive"));
            if (n instanceof SingleArgNode || n instanceof DoubleArgsNode) parsedLine.setOperand(n.getState("arg"));
        });


        if (mod == 1) {
            path.forEach((ParseNode n) ->
            {
                if (n instanceof LabelNode) parsedLine.setLabel(n.getState("label"));
                if (n instanceof InstructionNode) parsedLine.setMnemonic(n.getState("instruction"));
                if (n instanceof DirectiveNode)   parsedLine.setMnemonic(n.getState("directive"));
                if (n instanceof SingleArgNode || n instanceof DoubleArgsNode) parsedLine.setOperand(n.getState("arg"));
            });
        } else if (mod == 2) {
            ObjectCode objcode = new ObjectCode();
            path.forEach((ParseNode n) ->
            {
                if (n instanceof LabelNode){
                    parsedLine.setLabel(n.getState("label"));
                }
                if (n instanceof InstructionNode) {
                    parsedLine.setMnemonic(n.getState("instruction"));
                    int temp = Integer.parseInt(n.getState("instruction"),2)>>2;
                    objcode.setOpcode(Integer.parseInt(String.valueOf(temp),16));
                } else if (n instanceof DirectiveNode) {
                    parsedLine.setMnemonic(n.getState("directive"));
                    //hello sewe

                }
                if (n instanceof SingleArgNode) {
                    objcode.setFlags(Integer.parseInt(n.getState("flag")));
                    objcode.setArg1(Integer.parseInt(n.getState("arg1"),16));

                } else if (n instanceof DoubleArgsNode) {
                    objcode.setArg1(Integer.parseInt(n.getState("arg1"),16));
                    objcode.setArg2(Integer.parseInt(n.getState("arg2"),16));

                }
                else if (n instanceof DirectiveArgNode){
                    objcode.setArg1(Integer.parseInt(n.getState("dirArg0"),16));
                }
            });
        }

        return parsedLine;
    }

    private ParseNode getNextNode(ParseNode current, String token) throws AssemblerException {
        ParseNode node;
        node = current.nextNode(token);
        path.add(node);

        return node;
    }
    public boolean isBaseActivated()
    {
        return base>0;
    }
    public int getBase()
    {
        return base;
    }

    public int getCurrentLocation() {
        return currentLocation;
    }
    public void setMod(int mod) {
        this.mod = mod;
    }
}
