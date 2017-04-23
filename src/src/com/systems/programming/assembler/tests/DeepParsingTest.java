package com.systems.programming.assembler.tests;

import com.systems.programming.assembler.*;
import com.systems.programming.assembler.Exceptions.AssemblerException;
import com.systems.programming.assembler.Exceptions.DuplicateLabelException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;


/**
 * Created by ADMIN on 4/21/2017.
 */
@RunWith(value = Parameterized.class)
public class DeepParsingTest {

    private static DirectiveResolver resolver = DirectiveResolver.getInstance();
    private static OpTable opTable = OpTable.getInstance();
    private static SymTab symTab = SymTab.getInstance();
    private static LineParser parser = LineParser.getInstance();
    private static File source = new File("../resources/pass2Test.txt");
    private static BufferedReader sourceReader;

    @Parameterized.Parameter(value = 0)
    public Line line;
    @Parameterized.Parameter(value = 1)
    public String objectCode;



    @Parameterized.Parameters(name = "{index}:{0}-->{1}")
    public static Collection<Object[]> data() throws IOException {

        List<Object[]> a = new ArrayList<>();
        String l;
        String OCode;
        Assembler.init();
        sourceReader = new BufferedReader(new FileReader(source));
        parser.setMode(LineParser.Mode.DEEP);

        while ((l = sourceReader.readLine() ) != null) {

            Line parsedLine = new Line(l.trim());
            if (parsedLine.isEmpty() || parsedLine.isComment()) continue;
            List<String> tokens = parsedLine.getTokens();
            System.out.println("tokens = " + tokens);
            OCode = tokens.get(tokens.size() - 2);
            String candidateLabel = tokens.get(0);
            if (!opTable.isInstruction(candidateLabel) && !resolver.isDirective(candidateLabel)) {
                try {
                    symTab.put(tokens.get(0), parsedLine.getAddress());
                } catch (DuplicateLabelException e) {
                    System.out.println("can't add label");
                }
                parsedLine.setMnemonic(tokens.get(1));
            }
            else parsedLine.setMnemonic(tokens.get(0));
            tokens.remove(tokens.size()-2);
            parsedLine.setLine(tokens.stream().collect(Collectors.joining(" ")));
            if(parsedLine.hasOpCode())
            {
                System.out.println(parsedLine.getLine()+" @ "+parsedLine.getAddress());
                a.add(new Object[]{parsedLine,OCode.toLowerCase()});
            }
        }
        System.out.println(SymTab.getInstance().getTable().toString());
        return a;
    }

    @Test
    public void test() throws AssemblerException {
        System.out.println(line.getLine()+" @ "+line.getAddress());
        Assembler.setLocationCounter(line.getAddress());
        Line o = parser.parse(line.getLine());
        System.out.println("flags = "+Integer.toBinaryString(o.getObjectCode().getFlags()));
        System.out.printf("disp = %x\n",o.getObjectCode().getArg1());
        System.out.printf("arg2 = %d\n",o.getObjectCode().getArg2());
//        System.out.printf("opcode = %x\n",Integer.parseInt(o.getObjectCode().getOpcode()));


        assertEquals(objectCode,o.getObjectCode().toString());
    }


}