package com.systems.programming.assembler.ParseTree;

import com.systems.programming.assembler.Assembler;
import com.systems.programming.assembler.DirectiveResolver;
import com.systems.programming.assembler.Exceptions.AssemblerException;
import com.systems.programming.assembler.Exceptions.DisplacementOutOfBoundException;
import com.systems.programming.assembler.Exceptions.ExternalReferanceWithFormat3Exeption;
import com.systems.programming.assembler.Exceptions.UndefinedLabelException;
import com.systems.programming.assembler.LineParser;
import com.systems.programming.assembler.SymTab;

/**
 * Created by ADMIN on 3/29/2017.
 */
public class SingleArgNode extends ParseNode {

    private int flags = -1;
    private String arg;

    @Override
    public ParseNode nextNode(String token) {
        return new TerminalNode();
    }

    private String getCleanArg(String arg) {
        arg = arg.trim();
        String x = arg;
        if (arg.contains("#"))
            x = arg.replace("#", "");
        else if (arg.contains("@"))
            x = arg.replace("@", "");
        if (arg.contains(",X")) {
            x = arg.replace(",X", "");
        }
        return x;
    }

    public int getDisplacement() throws AssemblerException {
        if(DirectiveResolver.getInstance().isPass1(getState("directive")))
            return 0;
        int disp;
        String clean = getCleanArg(arg);
        /*System.out.println("clean arg is " + clean + " at instruction "
                + getState("instruction")
                + " mode is " + LineParser.getInstance().getMode());*/
        try {
            disp = Integer.parseInt(clean);
        } catch (NumberFormatException ex) {
            if (!SymTab.getInstance().containsKey(clean)) {
                //System.out.println("undefined "+clean);
                throw new UndefinedLabelException();
            } else if(getState("format").equalsIgnoreCase("3") &&
                    SymTab.getInstance().getCSect(clean).equalsIgnoreCase("other"))
                    throw new ExternalReferanceWithFormat3Exeption();

            disp = computeRelativeDisp(SymTab.getInstance().get(clean));
        }
        return disp;
    }

    public int computeRelativeDisp(int arg) throws DisplacementOutOfBoundException {
        LineParser parser = LineParser.getInstance();
        if (getState("format").equalsIgnoreCase("4"))
        {
            //System.out.printf("-> current pc is  %X\n",(Assembler.getLocationCounter() + Integer.parseInt(getState("format"))));
            return arg;
        }


        //System.out.println("is base activated = "+parser.isBaseActivated());
        //pc relative?
        int computed = arg - (Assembler.getLocationCounter() + Integer.parseInt(getState("format")));
        //System.out.println("pc->computed = " + computed);
        if (-2048 < computed && computed < 2047) {
            setFlags(getFlags() | (1 << 1));

            return computed;
        }
        //base relative
        else if (parser.isBaseActivated()) {

            computed = arg - parser.getBase();
           // System.out.println("base->computed = " + computed);
            if (computed < 4096) {
                setFlags(getFlags() | (1 << 2));
                return computed;
            }
        }

        //neither


        throw new DisplacementOutOfBoundException();
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    @Override
    public void addState(String key, String val) throws AssemblerException {
        if (key.equals("arg") && LineParser.getInstance().getMode().equals(LineParser.Mode.DEEP)) {
            arg = val;
            super.addState("operand", val);
            super.addState("arg", String.valueOf(getDisplacement()));

            super.addState("flags", String.valueOf(getFlags()));
        } else
            super.addState(key, val);
    }
}
