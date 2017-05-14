package com.systems.programming.assembler.tests;

import com.systems.programming.assembler.Assembler;
import com.systems.programming.assembler.DirectiveResolver;
import com.systems.programming.assembler.SymTab;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by alyswidan on 14/05/17.
 */
public class ExpressionsTest {
    DirectiveResolver dr = DirectiveResolver.getInstance();
    SymTab tab = SymTab.getInstance();
    @Test
    public void evalExpression() throws Exception {
    }

    @Test
    public void getExpressionType() throws Exception {
        Assembler.setProgName("prog");
        tab.putFull("buffer",123, SymTab.Type.RELATIVE,"prog");
        tab.putFull("buffend",123, SymTab.Type.RELATIVE,"prog");
        tab.putFull("test",123, SymTab.Type.RELATIVE,"prog");
        tab.putFull("writer",123, SymTab.Type.ABSOLUTE,"prog");
        tab.putFull("reader",123, SymTab.Type.ABSOLUTE,"prog");
        assertEquals(SymTab.Type.ABSOLUTE,dr.getExpressionType("100-buffer"));
    }

    @Test
    public void hasOperators() throws Exception {
        assertEquals(true,dr.isExpression("a+1+b+1"));
    }
    @Test
    public void hasNoOperators() throws Exception {
        assertEquals(false,dr.isExpression("a"));
    }

}