package com.systems.programming.assembler.tests;

import com.systems.programming.assembler.Exceptions.DuplicateLabelException;
import com.systems.programming.assembler.ParseTree.DirectNode;
import com.systems.programming.assembler.ParseTree.ImmediateNode;
import com.systems.programming.assembler.ParseTree.IndirectNode;
import com.systems.programming.assembler.SymTab;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ADMIN on 4/21/2017.
 */
public class SingleArgNodeTest {
    public SingleArgNodeTest() {
        try {
            SymTab.getInstance().put("RETADR",48);
        } catch (DuplicateLabelException e) {
            e.printStackTrace();
        }
    }
/*002A		J	@RETADR	3E2003*/
    @Test
    public void getDisplacementPcDirectFormat3() throws Exception {
        DirectNode n = new DirectNode();
        n.addState("format","3");
        n.addState("isIndexed","false");
        n.addState("arg","RETADR");
        assertEquals("02d",String.format("%03x",n.getDisplacement()));
    }
    @Test
    public void getDisplacementPcIndirectFormat3() throws Exception {
        IndirectNode n = new IndirectNode();
        n.addState("format","3");
        n.addState("isIndexed","false");
        n.addState("arg","RETADR");
        assertEquals("003",String.format("%03x",n.getDisplacement()));
    }

}