package com.systems.programming.assembler.ParseTree;

import com.systems.programming.assembler.Assembler;
import com.systems.programming.assembler.Exceptions.AssemblerException;
import com.systems.programming.assembler.Exceptions.DisplacementOutOfBoundException;
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
        String x=arg;
        if (arg.contains("#"))
            x = arg.replace("#", "");
        else if (arg.contains("@"))
            x = arg.replace("@", "");
        if(arg.contains(",X")) {
            System.out.println("indexed__________________________");
            x = arg.replace(",X","");
        }
        return x;
    }

    public int getDisplacement() throws AssemblerException {
        int disp;
        String clean = getCleanArg(arg);
        try {
            disp = Integer.parseInt(clean);
        } catch (NumberFormatException ex) {
            System.out.println("looking up "+clean);
            if (!SymTab.getInstance().containsKey(clean))
                throw new UndefinedLabelException();
            disp = computeRelativeDisp(SymTab.getInstance().get(clean));
        }
        System.out.printf("the computed disp is %03x\n",disp);
        return disp;
    }

    public int computeRelativeDisp(int arg) throws DisplacementOutOfBoundException {
        LineParser parser = LineParser.getInstance();
        if(getState("format").equalsIgnoreCase("4"))return arg;
        System.out.println("trying to compute relative displacement with disp = " + arg);
        //pc relative?
        if (arg > -2047 && arg < 2048) {
            System.out.println("trying pc relative");
            setFlags(getFlags()|(1<<1));
            int x = arg = arg - (Assembler.getLocationCounter() + Integer.parseInt(getState("format")));
            System.out.println("x = " + x);
            return x;
        }
        //base relative
        else if (parser.isBaseActivated() && arg < 4048) {
            System.out.println("trying base relative");
            setFlags(getFlags()|(1<<2));
            return arg - parser.getBase();
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
        if(key.equals("arg") && LineParser.getInstance().getMode().equals(LineParser.Mode.DEEP)) {
            arg = val;
            super.addState("arg",String.valueOf(getDisplacement()));
            System.out.printf("setting flags to %x\n",getFlags());
            super.addState("flags",String.valueOf(getFlags()));
        }
        else
            super.addState(key,val);
    }
}
