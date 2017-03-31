package com.systems.programming.assembler;

import com.systems.programming.assembler.Exceptions.*;
import com.systems.programming.assembler.ParseTree.*;

import java.io.*;
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

    private ParseNode node;
    private List<ParseNode>path;

    private LineParser() {
        path = new ArrayList<>();
    }

    //This object is only an engine for parsing and we only need one, and we don't need it to preserve state
    //so guess what singleton hahahaha our all time friend
    public static LineParser getInstance() {
        if (instance== null)instance = new LineParser();
        return instance;
    }

    public Line parse(String line) throws UnExpectedTokenException {

        Line parsedLine = new Line(line);
        if(parsedLine.isEmpty())return parsedLine;

        //get a clean copy of the passed line
        line = line.trim();
        //if the whole line is a comment return
        if(parsedLine.isComment()){parsedLine.setComment(line);return parsedLine;}

        //seperate comment from line first
        String tokens[] = line.split("\\.");

        //clean line after removing comment
        line = tokens[0].trim();

        //if there's a comment add it
        if(tokens.length==2) parsedLine.setComment("."+tokens[1]);

        //now split the line itself into tokens
        tokens = line.split("\\s+");
        List<String>tokenList = new ArrayList<>(Arrays.asList(tokens));
        //this is needed while traversing the parse tree to detect extra argumants or missing arguments
        tokenList.add("");
        //clear the path
        path.clear();

        //start the journey to classify the line components
        node = new RootNode();
        node = getNextNode(node,tokenList.get(0));


        //let the line travel through the tree
        for (int i = 1; i < tokenList.size(); i++) {node = getNextNode(node, tokenList.get(i));}

        //traverse the path and get components into corresponding variables
        path.forEach((ParseNode n) ->
        {
            if(n instanceof LabelNode)parsedLine.setLabel(n.getState());
            if(n instanceof InstructionNode || n instanceof DirectiveNode)parsedLine.setMnemonic(n.getState());
            if(n instanceof SingleArgNode || n instanceof DoubleArgsNode)parsedLine.setOperand(n.getState());
        });

        return parsedLine;
    }

    private ParseNode getNextNode(ParseNode current,String token) throws UnExpectedTokenException {
        ParseNode node;
        node = current.nextNode(token);
        path.add(node);

        return node;
    }

}
