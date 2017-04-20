package com.systems.programming.assembler;

import com.systems.programming.assembler.Exceptions.AssemblerException;
import com.systems.programming.assembler.ParseTree.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohamed Mahmoud on 3/26/2017.
 */

public class LineParser {
    public enum Mode{DEEP,SHALLOW};
    private static LineParser instance;
    private String start;
    private int base = 0, currentLocation;
    private ParseNode node;
    private List<ParseNode> path;
    public Mode mode = Mode.SHALLOW;

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
        if (parsedLine.isEmpty() || parsedLine.isComment()) return parsedLine;

        //clear the path
        path.clear();
        List<String> tokenList = parsedLine.getTokens();
        System.out.println("tokenList = " + tokenList);
        int start = getMode().equals(Mode.SHALLOW)?0:1;
        System.out.println("tokenList = " + tokenList);
        //start the journey to c1lassify the line components
        node = new RootNode();
        node = getNextNode(node, tokenList.get(start));
        System.out.println(node.getState());

        //let the line travel through the tree
        for (int i = start+1; i < tokenList.size(); i++) {

            node = getNextNode(node, tokenList.get(i));
        }

        System.out.println("parsing using mode "+mode.toString());
        if(mode.equals(Mode.SHALLOW))
            return parseMode1(parsedLine);
        else
            return parseMode2(parsedLine);
    }

    private Line parseMode2(Line parsedLine)
    {
        System.out.println(parsedLine.getAddress());
        path.forEach(n->{
            System.out.println(n.getClass().getSimpleName()+"-->"+n.getState());
        });
        return parsedLine;



    }
    private Line parseMode1(Line parsedLine) {

        //traverse the path and get components into corresponding variables
        path.forEach((ParseNode n) ->
        {
            System.out.println(n.getClass().getSimpleName());
            if (n instanceof LabelNode) parsedLine.setLabel(n.getState("label"));
            if (n instanceof InstructionNode) parsedLine.setMnemonic(n.getState("instruction"));
            if (n instanceof DirectiveNode) parsedLine.setMnemonic(n.getState("directive"));
            if (n instanceof SingleArgNode || n instanceof DoubleArgsNode) parsedLine.setOperand(n.getState("arg"));
            System.out.println("parsedLine = " + parsedLine);
        });

        return parsedLine;
    }

    private ParseNode getNextNode(ParseNode current, String token) throws AssemblerException {
        ParseNode node;
        node = current.nextNode(token);
        path.add(node);
        return node;
    }

    public void deactivateBase() {base=-1;}

    public boolean isBaseActivated() {
        return base > 0;
    }

    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        this.base = base;
        System.out.println("--------------->base = " + base);
    }

    public int getCurrentAddress() {
        return currentLocation;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }


}
