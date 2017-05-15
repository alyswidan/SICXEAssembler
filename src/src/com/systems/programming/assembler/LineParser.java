package com.systems.programming.assembler;

import com.systems.programming.assembler.Exceptions.AssemblerException;
import com.systems.programming.assembler.Exceptions.DuplicateLabelException;
import com.systems.programming.assembler.Exceptions.InvalidExpressionException;
import com.systems.programming.assembler.Exceptions.UndefinedMnemonicException;
import com.systems.programming.assembler.ParseTree.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohamed Mahmoud on 3/26/2017.
 */

public class LineParser {
    public enum Mode {DEEP, SHALLOW}

    ;
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
        //start the journey to c1lassify the line components
        node = new RootNode();
        node = getNextNode(node, tokenList.get(0));

        //let the line travel through the tree
        for (int i = 1; i < tokenList.size(); i++) {

            node = getNextNode(node, tokenList.get(i));
        }

        if (mode.equals(Mode.SHALLOW))
            return shallowParse(parsedLine);
        else
            return deepParse(parsedLine);
    }

    private Line deepParse(Line parsedLine) throws AssemblerException {
        ObjectCode code = new ObjectCode();
        path.forEach(n -> {

            if (n instanceof LabelNode) parsedLine.setLabel(n.getState("label"));

            if (n instanceof InstructionNode) {
                parsedLine.setMnemonic(n.getState("instruction"));
                code.setOpcode(n.getState("opcode"));
                code.setLength(OpTable.getInstance().getFormat(n.getState("instruction")));
            }
            if (n instanceof SingleArgNode) {
                code.setFlags(Integer.parseInt(n.getState("flags")));
                code.setArg1(Integer.parseInt(n.getState("arg")));
                parsedLine.setOperand(n.getState("operand"));
            }
            if (n instanceof DoubleArgsNode) {
                code.setArg1(Integer.parseInt(n.getState("arg1")));
                code.setArg2(Integer.parseInt(n.getState("arg2")));
            }
            if (n instanceof DirectiveNode) {
                parsedLine.setMnemonic(n.getState("directive"));
                code.setDirective(true);
            }
            if (n instanceof DirectiveArgNode) {
                parsedLine.setOperand(n.getState("arg"));

                code.setOpcode(n.getState("objectCode"));
            }
        });
        parsedLine.setObjectCode(code);
        return parsedLine;


    }

    private Line shallowParse(Line parsedLine) throws UndefinedMnemonicException, InvalidExpressionException, DuplicateLabelException {

        DirectiveResolver dr = DirectiveResolver.getInstance();
        //traverse the path and get components into corresponding variables
        path.forEach((ParseNode n) ->
        {
            if (n instanceof LabelNode) parsedLine.setLabel(n.getState("label"));
            if (n instanceof InstructionNode) parsedLine.setMnemonic(n.getState("instruction"));
            if (n instanceof DirectiveNode) parsedLine.setMnemonic(n.getState("directive"));
            if (n instanceof SingleArgNode || n instanceof DoubleArgsNode) parsedLine.setOperand(n.getState("arg"));
        });

        //special treatment for those as their effect appears in pass1
        if (parsedLine.isStart())
            Assembler.setProgName(parsedLine.getLabel());

        if (parsedLine.isEqu())
            dr.executeEqu(parsedLine);

        // TODO: 15/05/17 this is not correct we need and should be done in pass 2 as it is done 
        if(OpTable.getInstance().getFormat(parsedLine.getMnemonic())==4)
        {
            Assembler.addMRecord(new MRecord(Assembler.getLocationCounter()+1,'+',5,Assembler.getProgName()));
        }
        if (dr.isDirective(parsedLine.getMnemonic()) && dr.isExpression(parsedLine.getOperand())) {
            parsedLine.setOperand(String.valueOf(dr.evalExpression(parsedLine.getOperand())));
            List<MRecord>recs = dr.getMrecords(parsedLine.getOperand());
            recs.forEach(mRecord -> mRecord.setAddress(Assembler.getLocationCounter()));
            Assembler.appendMRecords(recs);
        }

        if (parsedLine.getMnemonic().equalsIgnoreCase("extref"))
            dr.executeExtRef(parsedLine.getOperand());

        if (parsedLine.getMnemonic().equalsIgnoreCase("org"))
            dr.executeOrg(parsedLine.getOperand());

        return parsedLine;
    }

    private ParseNode getNextNode(ParseNode current, String token) throws AssemblerException {
        ParseNode node;
        node = current.nextNode(token);
        path.add(node);
        return node;
    }

    public void deactivateBase() {
        base = -1;
    }

    public boolean isBaseActivated() {
        return base > 0;
    }

    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        System.out.println("activating base");
        this.base = base;
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
