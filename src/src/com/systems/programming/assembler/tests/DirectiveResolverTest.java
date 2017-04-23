package com.systems.programming.assembler.tests;

import com.systems.programming.assembler.Assembler;
import com.systems.programming.assembler.DirectiveResolver;
import com.systems.programming.assembler.LineParser;
import com.systems.programming.assembler.Pass1;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by ADMIN on 4/20/2017.
 */
public class DirectiveResolverTest {

    private String directives[] = {"word", "byte", "resb", "resw", "base", "start", "end", "nobase"};
    private String hasNoObjectCode[] = {"start", "base", "nobase"};
    DirectiveResolver d = DirectiveResolver.getInstance();

    //length
    @Test
    public void reswLength() throws Exception {
        assertEquals(60,d.getLength("resw","20"));
    }

    @Test
    public void byteHexLength() throws Exception {
        assertEquals(2,d.getLength("byte","X'f1a2'"));
    }

    @Test
    public void byteCharsLength() throws Exception {
        assertEquals(3,d.getLength("byte","C'abc'"));
    }

    @Test
    public void wordLength() throws Exception {
        assertEquals(3,d.getLength("word","55"));
    }

    @Test
    public void resbLength() throws Exception {
        assertEquals(20,d.getLength("resb","20"));
    }

    //object code generation
    @Test
    public void resb() throws Exception {
        assertEquals(null,d.getObjectCode("resb","20").getOpcode());
    }

    @Test
    public void resw() throws Exception {
        assertEquals(null,d.getObjectCode("resw","20").getOpcode());
    }

    @Test
    public void byteHex() throws Exception {
        assertEquals("f1a2",d.getObjectCode("byte","X'f1a2'").getOpcode());
    }

    @Test
    public void byteCharsabc() throws Exception {
        assertEquals("616263",d.getObjectCode("byte","C'abc'").getOpcode());
    }


    @Test
    public void byteChars123() throws Exception {
        assertEquals("313233",d.getObjectCode("byte","C'123'").getOpcode());
    }

    @Test
    public void word() throws Exception {
        assertEquals("000037",d.getObjectCode("word","55").getOpcode());
    }

    //exeutes
    @Test
    public void executeStart1000() throws Exception {
        d.executeStart("1000");
        assertEquals(4096, Assembler.getLocationCounter());
    }

    @Test
    public void executeBase1000() throws Exception {
        d.executeBase("1000");
        assertEquals(1000, LineParser.getInstance().getBase());
        assertTrue(LineParser.getInstance().isBaseActivated());
    }

    @Test
    public void executeNoBase() throws Exception {
        d.executeNoBase();
        assertFalse(LineParser.getInstance().isBaseActivated());
    }


}