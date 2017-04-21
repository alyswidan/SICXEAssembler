package com.systems.programming.assembler.tests;

import com.systems.programming.assembler.DirectiveResolver;
import com.systems.programming.assembler.ObjectCode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Created by ADMIN on 4/21/2017.
 */
public class ObjectCodeTest {

    ObjectCode o;

    @Before
    public void setUp() throws Exception {
        o = new ObjectCode();
        o.setOpcode("4c");
    }


    @Test
    public void testFormat3() {
        o.setLength(3);
        o.setFlags(Integer.parseInt("011010", 2));
        o.setArg1(Integer.parseInt("ffa", 16));
        assertEquals("4daffa", o.toString());
    }

    @Test
    public void testFormat4() {
        o.setLength(4);
        o.setFlags(Integer.parseInt("110001", 2));
        o.setArg1(Integer.parseInt("ffaab", 16));
        assertEquals("4f1ffaab", o.toString());
    }

    @Test
    public void testByte() {
        String s = DirectiveResolver.getInstance().parseByte("C'1234'").toString();
        assertEquals("31323334",s);
    }

    @Test
    public void testFormat2() {

    }

    @Test
    public void testFormat1() {

    }
}