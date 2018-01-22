/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.mazarineblue.utilities;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.utilities.exceptions.ConversionMethodAlreadyRegisteredException;
import org.mazarineblue.utilities.exceptions.ConversionMethodNotFoundException;
import org.mazarineblue.utilities.exceptions.InputConversionException;
import org.mazarineblue.utilities.exceptions.PrimativesNotAllowedException;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class TypeConvertorTest {

    private TypeConvertor convertor = new TypeConvertor();

    @After
    public void teardown() {
        convertor = null;
    }

    @SuppressWarnings("PublicInnerClass")
    public class GivenAnEmptyConvertor {

        @Before
        public void setup() {
            convertor = new TypeConvertor();
        }

        @Test
        public void hasConversionMethod() {
            assertFalse(convertor.hasConversionMethod(String.class, ID.class));
        }

        public class RegisterConversionMethod_Primatives {
            //<editor-fold defaultstate="collapsed" desc="Test registerConversionFunction of a primative">
            @Test(expected = PrimativesNotAllowedException.class) public void registerConversionFunction_InputPrimativeByte() { convertor.registerConversionFunction(byte.class, ID.class, input -> new ID()); }
            @Test(expected = PrimativesNotAllowedException.class) public void registerConversionFunction_InputPrimativeShort() { convertor.registerConversionFunction(short.class, ID.class, input -> new ID()); }
            @Test(expected = PrimativesNotAllowedException.class) public void registerConversionFunction_InputPrimativeInt() { convertor.registerConversionFunction(int.class, ID.class, input -> new ID()); }
            @Test(expected = PrimativesNotAllowedException.class) public void registerConversionFunction_InputPrimativeLong() { convertor.registerConversionFunction(long.class, ID.class, input -> new ID()); }
            @Test(expected = PrimativesNotAllowedException.class) public void registerConversionFunction_InputPrimativeFloat() { convertor.registerConversionFunction(float.class, ID.class, input -> new ID()); }
            @Test(expected = PrimativesNotAllowedException.class) public void registerConversionFunction_InputPrimativeDouble() { convertor.registerConversionFunction(double.class, ID.class, input -> new ID()); }
            @Test(expected = PrimativesNotAllowedException.class) public void registerConversionFunction_InputPrimativeChar() { convertor.registerConversionFunction(char.class, ID.class, input -> new ID()); }
            @Test(expected = PrimativesNotAllowedException.class) public void registerConversionFunction_InputPrimativeBoolean() { convertor.registerConversionFunction(boolean.class, ID.class, input -> new ID()); }

            @Test(expected = PrimativesNotAllowedException.class) public void registerConversionFunction_OutputPrimativeByte() { convertor.registerConversionFunction(ID.class, byte.class, input -> (byte)1); }
            @Test(expected = PrimativesNotAllowedException.class) public void registerConversionFunction_OutputPrimativeShort() { convertor.registerConversionFunction(ID.class, short.class, input -> (short)1); }
            @Test(expected = PrimativesNotAllowedException.class) public void registerConversionFunction_OutputPrimativeInt() { convertor.registerConversionFunction(ID.class, int.class, input -> 1); }
            @Test(expected = PrimativesNotAllowedException.class) public void registerConversionFunction_OutputPrimativeLong() { convertor.registerConversionFunction(ID.class, long.class, input -> 1L); }
            @Test(expected = PrimativesNotAllowedException.class) public void registerConversionFunction_OutputPrimativeFloat() { convertor.registerConversionFunction(ID.class, float.class, input -> 1f); }
            @Test(expected = PrimativesNotAllowedException.class) public void registerConversionFunction_OutputPrimativeDouble() { convertor.registerConversionFunction(ID.class, double.class, input -> 1d); }
            @Test(expected = PrimativesNotAllowedException.class) public void registerConversionFunction_OutputPrimativeChar() { convertor.registerConversionFunction(ID.class, char.class, input -> 'c'); }
            @Test(expected = PrimativesNotAllowedException.class) public void registerConversionFunction_OutputPrimativeBoolean() { convertor.registerConversionFunction(ID.class, boolean.class, input -> true); }
            //</editor-fold>
        }

        @Test
        public void registerConversionFunction_RegisterMethodOnes() {
            convertor.registerConversionFunction(String.class, ID.class, input -> new ID());
            assertTrue(convertor.hasConversionMethod(String.class, ID.class));
        }

        @Test(expected = ConversionMethodAlreadyRegisteredException.class)
        public void registerConversionFunction_RegisterMethodTwice() {
            convertor.registerConversionFunction(String.class, ID.class, input -> new ID());
            convertor.registerConversionFunction(String.class, ID.class, input -> new ID());
        }

        @Test(expected = ConversionMethodNotFoundException.class)
        public void unregisterConversionFunction_MethodNotRegistered() {
            convertor.unregisterConversionFunction(String.class, ID.class);
        }

        @Test
        public void unregisterConversionFunction_MethodRegistered() {
            convertor.registerConversionFunction(String.class, ID.class, input -> new ID());
            convertor.unregisterConversionFunction(String.class, ID.class);
            assertFalse(convertor.hasConversionMethod(String.class, ID.class));
        }
    }

    @SuppressWarnings("PublicInnerClass")
    private abstract class GivenConvertorInstance {

        @Before
        public void setup() {
            convertor = getConvertor();
        }

        protected abstract TypeConvertor getConvertor();

        //<editor-fold defaultstate="collapsed" desc="Test conver">
        @Test public void convert_Byte_PrimativeByte()          { assertEquals((Byte) (byte) 1, convertor.convert((byte) 1, Byte.TYPE)); }
        @Test public void convert_Short_PrimativeByte()         { assertEquals((Byte) (byte) 1, convertor.convert((short) 1, Byte.TYPE)); }
        @Test public void convert_Integer_PrimativeByte()       { assertEquals((Byte) (byte) 1, convertor.convert(1, Byte.TYPE)); }
        @Test public void convert_Long_PrimativeByte()          { assertEquals((Byte) (byte) 1, convertor.convert(1l, Byte.TYPE)); }
        @Test public void convert_Float_PrimativeByte()         { assertEquals((Byte) (byte) 1, convertor.convert(1f, Byte.TYPE)); }
        @Test public void convert_Double_PrimativeByte()        { assertEquals((Byte) (byte) 1, convertor.convert(1d, Byte.TYPE)); }
        @Test public void convert_Character_PrimativeByte()     { assertEquals((Byte) (byte) 1, convertor.convert('1', Byte.TYPE)); }
        @Test public void convert_String_PrimativeByte()        { assertEquals((Byte) (byte) 1, convertor.convert("1", Byte.TYPE)); }
        @Test public void convert_Boolean_PrimativeByte()       { assertEquals((Byte) (byte) 1, convertor.convert(true, Byte.TYPE)); }

        @Test public void convert_Byte_PrimativeShort()         { assertEquals((Short) (short) 1, convertor.convert((byte) 1, Short.TYPE)); }
        @Test public void convert_Short_PrimativeShort()        { assertEquals((Short) (short) 1, convertor.convert((short) 1, Short.TYPE)); }
        @Test public void convert_Integer_PrimativeShort()      { assertEquals((Short) (short) 1, convertor.convert(1, Short.TYPE)); }
        @Test public void convert_Long_PrimativeShort()         { assertEquals((Short) (short) 1, convertor.convert(1l, Short.TYPE)); }
        @Test public void convert_Float_PrimativeShort()        { assertEquals((Short) (short) 1, convertor.convert(1f, Short.TYPE)); }
        @Test public void convert_Double_PrimativeShort()       { assertEquals((Short) (short) 1, convertor.convert(1d, Short.TYPE)); }
        @Test public void convert_Character_PrimativeShort()    { assertEquals((Short) (short) 1, convertor.convert('1', Short.TYPE)); }
        @Test public void convert_String_PrimativeShort()       { assertEquals((Short) (short) 1, convertor.convert("1", Short.TYPE)); }
        @Test public void convert_Boolean_PrimativeShort()      { assertEquals((Short) (short) 1, convertor.convert(true, Short.TYPE)); }

        @Test public void convert_Byte_PrimativeInteger()       { assertEquals((Integer) 1, convertor.convert((byte) 1, Integer.TYPE)); }
        @Test public void convert_Short_PrimativeInteger()      { assertEquals((Integer) 1, convertor.convert((short) 1, Integer.TYPE)); }
        @Test public void convert_Integer_PrimativeInteger()    { assertEquals((Integer) 1, convertor.convert(1, Integer.TYPE)); }
        @Test public void convert_Long_PrimativeInteger()       { assertEquals((Integer) 1, convertor.convert(1l, Integer.TYPE)); }
        @Test public void convert_Float_PrimativeInteger()      { assertEquals((Integer) 1, convertor.convert(1f, Integer.TYPE)); }
        @Test public void convert_Double_PrimativeInteger()     { assertEquals((Integer) 1, convertor.convert(1d, Integer.TYPE)); }
        @Test public void convert_Character_PrimativeInteger()  { assertEquals((Integer) 1, convertor.convert('1', Integer.TYPE)); }
        @Test public void convert_String_PrimativeInteger()     { assertEquals((Integer) 1, convertor.convert("1", Integer.TYPE)); }
        @Test public void convert_Boolean_PrimativeInteger()    { assertEquals((Integer) 1, convertor.convert(true, Integer.TYPE)); }

        @Test public void convert_Byte_PrimativeLong()          { assertEquals((Long) 1l, convertor.convert((byte) 1, Long.TYPE)); }
        @Test public void convert_Short_PrimativeLong()         { assertEquals((Long) 1l, convertor.convert((short) 1, Long.TYPE)); }
        @Test public void convert_Integer_PrimativeLong()       { assertEquals((Long) 1l, convertor.convert(1, Long.TYPE)); }
        @Test public void convert_Long_PrimativeLong()          { assertEquals((Long) 1l, convertor.convert(1l, Long.TYPE)); }
        @Test public void convert_Float_PrimativeLong()         { assertEquals((Long) 1l, convertor.convert(1f, Long.TYPE)); }
        @Test public void convert_Double_PrimativeLong()        { assertEquals((Long) 1l, convertor.convert(1d, Long.TYPE)); }
        @Test public void convert_Character_PrimativeLong()     { assertEquals((Long) 1l, convertor.convert('1', Long.TYPE)); }
        @Test public void convert_String_PrimativeLong()        { assertEquals((Long) 1l, convertor.convert("1", Long.TYPE)); }
        @Test public void convert_Boolean_PrimativeLong()       { assertEquals((Long) 1l, convertor.convert(true, Long.TYPE)); }

        @Test public void convert_Byte_PrimativeFloat()         { assertEquals((Float) 1f, convertor.convert((byte) 1, Float.TYPE)); }
        @Test public void convert_Short_PrimativeFloat()        { assertEquals((Float) 1f, convertor.convert((short) 1, Float.TYPE)); }
        @Test public void convert_Integer_PrimativeFloat()      { assertEquals((Float) 1f, convertor.convert(1, Float.TYPE)); }
        @Test public void convert_Long_PrimativeFloat()         { assertEquals((Float) 1f, convertor.convert(1f, Float.TYPE)); }
        @Test public void convert_Float_PrimativeFloat()        { assertEquals((Float) 1f, convertor.convert(1f, Float.TYPE)); }
        @Test public void convert_Double_PrimativeFloat()       { assertEquals((Float) 1f, convertor.convert(1d, Float.TYPE)); }
        @Test public void convert_Character_PrimativeFloat()    { assertEquals((Float) 1f, convertor.convert('1', Float.TYPE)); }
        @Test public void convert_String_PrimativeFloat()       { assertEquals((Float) 1f, convertor.convert("1", Float.TYPE)); }
        @Test(expected = InputConversionException.class) public void convert_Boolean_PrimativeFloat() { assertEquals((Float) 1f, convertor.convert(true, Float.TYPE)); }

        @Test public void convert_Byte_PrimativeDouble()        { assertEquals((Double) 1d, convertor.convert((byte) 1, Double.TYPE)); }
        @Test public void convert_Short_PrimativeDouble()       { assertEquals((Double) 1d, convertor.convert((short) 1, Double.TYPE)); }
        @Test public void convert_Integer_PrimativeDouble()     { assertEquals((Double) 1d, convertor.convert(1, Double.TYPE)); }
        @Test public void convert_Long_PrimativeDouble()        { assertEquals((Double) 1d, convertor.convert(1l, Double.TYPE)); }
        @Test public void convert_Float_PrimativeDouble()       { assertEquals((Double) 1d, convertor.convert(1f, Double.TYPE)); }
        @Test public void convert_Double_PrimativeDouble()      { assertEquals((Double) 1d, convertor.convert(1d, Double.TYPE)); }
        @Test public void convert_Character_PrimativeDouble()   { assertEquals((Double) 1d, convertor.convert('1', Double.TYPE)); }
        @Test public void convert_String_PrimativeDouble()      { assertEquals((Double) 1d, convertor.convert("1", Double.TYPE)); }
        @Test(expected = InputConversionException.class) public void convert_Boolean_PrimativeDouble() { assertEquals((Double) 1d, convertor.convert(true, Double.TYPE)); }

        @Test public void convert_Byte_PrimativeChar()          { assertEquals((Character) '1', convertor.convert((byte) 1, Character.TYPE)); }
        @Test public void convert_Short_PrimativeChar()         { assertEquals((Character) '1', convertor.convert((short) 1, Character.TYPE)); }
        @Test public void convert_Integer_PrimativeChar()       { assertEquals((Character) '1', convertor.convert(1, Character.TYPE)); }
        @Test public void convert_Long_PrimativeChar()          { assertEquals((Character) '1', convertor.convert(1l, Character.TYPE)); }
        @Test(expected = InputConversionException.class) public void convert_Float_PrimativeChar()         { assertEquals((Character) '1', convertor.convert(1f, Character.TYPE)); }
        @Test(expected = InputConversionException.class) public void convert_Double_PrimativeChar()        { assertEquals((Character) '1', convertor.convert(1d, Character.TYPE)); }
        @Test public void convert_Character_PrimativeChar()     { assertEquals((Character) '1', convertor.convert('1', Character.TYPE)); }
        @Test public void convert_String_PrimativeChar()        { assertEquals((Character) '1', convertor.convert("1", Character.TYPE)); }
        @Test public void convert_Boolean_PrimativeChar()       { assertEquals((Character) 't', convertor.convert(true, Character.TYPE)); }

        @Test public void convert_Byte_PrimativeBoolean()       { assertEquals((Boolean) true, convertor.convert((byte) 1, Boolean.TYPE)); }
        @Test public void convert_Short_PrimativeBoolean()      { assertEquals((Boolean) true, convertor.convert((short) 2, Boolean.TYPE)); }
        @Test public void convert_Integer_PrimativeBoolean()    { assertEquals((Boolean) true, convertor.convert(1, Boolean.TYPE)); }
        @Test public void convert_Long_PrimativeBoolean()       { assertEquals((Boolean) true, convertor.convert(1l, Boolean.TYPE)); }
        @Test(expected = InputConversionException.class) public void convert_Float_PrimativeBoolean()      { assertEquals((Boolean) true, convertor.convert(1f, Boolean.TYPE)); }
        @Test(expected = InputConversionException.class) public void convert_Double_PrimativeBoolean()     { assertEquals((Boolean) true, convertor.convert(1d, Boolean.TYPE)); }
        @Test public void convert_Character_PrimativeBoolean()  { assertEquals((Boolean) true, convertor.convert('t', Boolean.TYPE)); }
        @Test public void convert_String_PrimativeBoolean()     { assertEquals((Boolean) true, convertor.convert("true", Boolean.TYPE)); }
        @Test public void convert_Boolean_PrimativeBoolean()    { assertEquals((Boolean) true, convertor.convert(true, Boolean.TYPE)); }

        @Test public void convert_Byte_Byte()           { assertEquals((Byte) (byte) 1, convertor.convert((byte) 1, Byte.class)); }
        @Test public void convert_Short_Byte()          { assertEquals((Byte) (byte) 1, convertor.convert((short) 1, Byte.class)); }
        @Test public void convert_Integer_Byte()        { assertEquals((Byte) (byte) 1, convertor.convert(1, Byte.class)); }
        @Test public void convert_Long_Byte()           { assertEquals((Byte) (byte) 1, convertor.convert(1l, Byte.class)); }
        @Test public void convert_Float_Byte()          { assertEquals((Byte) (byte) 1, convertor.convert(1f, Byte.class)); }
        @Test public void convert_Double_Byte()         { assertEquals((Byte) (byte) 1, convertor.convert(1d, Byte.class)); }
        @Test public void convert_Character_Byte()      { assertEquals((Byte) (byte) 1, convertor.convert('1', Byte.class)); }
        @Test public void convert_String_Byte()         { assertEquals((Byte) (byte) 1, convertor.convert("1", Byte.class)); }
        @Test public void convert_Boolean_Byte()        { assertEquals((Byte) (byte) 1, convertor.convert(true, Byte.class)); }

        @Test public void convert_Byte_Short()          { assertEquals((Short) (short) 1, convertor.convert((byte) 1, Short.class)); }
        @Test public void convert_Short_Short()         { assertEquals((Short) (short) 1, convertor.convert((short) 1, Short.class)); }
        @Test public void convert_Integer_Short()       { assertEquals((Short) (short) 1, convertor.convert(1, Short.class)); }
        @Test public void convert_Long_Short()          { assertEquals((Short) (short) 1, convertor.convert(1l, Short.class)); }
        @Test public void convert_Float_Short()         { assertEquals((Short) (short) 1, convertor.convert(1f, Short.class)); }
        @Test public void convert_Double_Short()        { assertEquals((Short) (short) 1, convertor.convert(1d, Short.class)); }
        @Test public void convert_Character_Short()     { assertEquals((Short) (short) 1, convertor.convert('1', Short.class)); }
        @Test public void convert_String_Short()        { assertEquals((Short) (short) 1, convertor.convert("1", Short.class)); }
        @Test public void convert_Boolean_Short()       { assertEquals((Short) (short) 1, convertor.convert(true, Short.class)); }

        @Test public void convert_Byte_Integer()        { assertEquals((Integer) 1, convertor.convert((byte) 1, Integer.class)); }
        @Test public void convert_Short_Integer()       { assertEquals((Integer) 1, convertor.convert((short) 1, Integer.class)); }
        @Test public void convert_Integer_Integer()     { assertEquals((Integer) 1, convertor.convert(1, Integer.class)); }
        @Test public void convert_Long_Integer()        { assertEquals((Integer) 1, convertor.convert(1l, Integer.class)); }
        @Test public void convert_Float_Integer()       { assertEquals((Integer) 1, convertor.convert(1f, Integer.class)); }
        @Test public void convert_Double_Integer()      { assertEquals((Integer) 1, convertor.convert(1d, Integer.class)); }
        @Test public void convert_Character_Integer()   { assertEquals((Integer) 1, convertor.convert('1', Integer.class)); }
        @Test public void convert_String_Integer()      { assertEquals((Integer) 1, convertor.convert("1", Integer.class)); }
        @Test public void convert_Boolean_Integer()     { assertEquals((Integer) 1, convertor.convert(true, Integer.class)); }

        @Test public void convert_Byte_Long()           { assertEquals((Long) 1l, convertor.convert((byte) 1, Long.class)); }
        @Test public void convert_Short_Long()          { assertEquals((Long) 1l, convertor.convert((short) 1, Long.class)); }
        @Test public void convert_Integer_Long()        { assertEquals((Long) 1l, convertor.convert(1, Long.class)); }
        @Test public void convert_Long_Long()           { assertEquals((Long) 1l, convertor.convert(1l, Long.class)); }
        @Test public void convert_Float_Long()          { assertEquals((Long) 1l, convertor.convert(1f, Long.class)); }
        @Test public void convert_Double_Long()         { assertEquals((Long) 1l, convertor.convert(1d, Long.class)); }
        @Test public void convert_Character_Long()      { assertEquals((Long) 1l, convertor.convert('1', Long.class)); }
        @Test public void convert_String_Long()         { assertEquals((Long) 1l, convertor.convert("1", Long.class)); }
        @Test public void convert_Boolean_Long()        { assertEquals((Long) 1l, convertor.convert(true, Long.class)); }

        @Test public void convert_Byte_Float()          { assertEquals((Float) 1f, convertor.convert((byte) 1, Float.class)); }
        @Test public void convert_Short_Float()         { assertEquals((Float) 1f, convertor.convert((short) 1, Float.class)); }
        @Test public void convert_Integer_Float()       { assertEquals((Float) 1f, convertor.convert(1, Float.class)); }
        @Test public void convert_Long_Float()          { assertEquals((Float) 1f, convertor.convert(1f, Float.class)); }
        @Test public void convert_Float_Float()         { assertEquals((Float) 1f, convertor.convert(1f, Float.class)); }
        @Test public void convert_Double_Float()        { assertEquals((Float) 1f, convertor.convert(1d, Float.class)); }
        @Test public void convert_Character_Float()     { assertEquals((Float) 1f, convertor.convert('1', Float.class)); }
        @Test public void convert_String_Float()        { assertEquals((Float) 1f, convertor.convert("1", Float.class)); }
        @Test(expected = InputConversionException.class) public void convert_Boolean_Float() { assertEquals((Float) 1f, convertor.convert(true, Float.class)); }

        @Test public void convert_Byte_Double()         { assertEquals((Double) 1d, convertor.convert((byte) 1, Double.class)); }
        @Test public void convert_Short_Double()        { assertEquals((Double) 1d, convertor.convert((short) 1, Double.class)); }
        @Test public void convert_Integer_Double()      { assertEquals((Double) 1d, convertor.convert(1, Double.class)); }
        @Test public void convert_Long_Double()         { assertEquals((Double) 1d, convertor.convert(1l, Double.class)); }
        @Test public void convert_Float_Double()        { assertEquals((Double) 1d, convertor.convert(1f, Double.class)); }
        @Test public void convert_Double_Double()       { assertEquals((Double) 1d, convertor.convert(1d, Double.class)); }
        @Test public void convert_Character_Double()    { assertEquals((Double) 1d, convertor.convert('1', Double.class)); }
        @Test public void convert_String_Double()       { assertEquals((Double) 1d, convertor.convert("1", Double.class)); }
        @Test(expected = InputConversionException.class) public void convert_Boolean_Double() { assertEquals((Double) 1d, convertor.convert(true, Double.class)); }

        @Test public void convert_Byte_Char()           { assertEquals((Character) '1', convertor.convert((byte) 1, Character.class)); }
        @Test public void convert_Short_Char()          { assertEquals((Character) '1', convertor.convert((short) 1, Character.class)); }
        @Test public void convert_Integer_Char()        { assertEquals((Character) '1', convertor.convert(1, Character.class)); }
        @Test public void convert_Long_Char()           { assertEquals((Character) '1', convertor.convert(1l, Character.class)); }
        @Test(expected = InputConversionException.class) public void convert_Float_Char()         { assertEquals((Character) '1', convertor.convert(1f, Character.class)); }
        @Test(expected = InputConversionException.class) public void convert_Double_Char()        { assertEquals((Character) '1', convertor.convert(1d, Character.class)); }
        @Test public void convert_Character_Char()      { assertEquals((Character) '1', convertor.convert('1', Character.class)); }
        @Test public void convert_String_Char()         { assertEquals((Character) '1', convertor.convert("1", Character.class)); }
        @Test public void convert_Boolean_Char()        { assertEquals((Character) 't', convertor.convert(true, Character.class)); }

        @Test public void convert_Byte_String()        { assertEquals("1", convertor.convert((byte) 1, String.class)); }
        @Test public void convert_Short_String()       { assertEquals("1", convertor.convert((short) 1, String.class)); }
        @Test public void convert_Integer_String()     { assertEquals("1", convertor.convert(1, String.class)); }
        @Test public void convert_Long_String()        { assertEquals("1", convertor.convert(1l, String.class)); }
        @Test public void convert_Float_String()       { assertEquals("1.0", convertor.convert(1f, String.class)); }
        @Test public void convert_Double_String()      { assertEquals("1.0", convertor.convert(1d, String.class)); }
        @Test public void convert_Character_String()   { assertEquals("t", convertor.convert('t', String.class)); }
        @Test public void convert_String_String()      { assertEquals("true", convertor.convert("true", String.class)); }
        @Test public void convert_Boolean_String()     { assertEquals("true", convertor.convert(true, String.class)); }

        @Test public void convert_Byte_Boolean()        { assertEquals(true, convertor.convert((byte) 1, Boolean.class)); }
        @Test public void convert_Short_Boolean()       { assertEquals(true, convertor.convert((short) 2, Boolean.class)); }
        @Test public void convert_Integer_Boolean()     { assertEquals(true, convertor.convert(1, Boolean.class)); }
        @Test public void convert_Long_Boolean()        { assertEquals(true, convertor.convert(1l, Boolean.class)); }
        @Test(expected = InputConversionException.class) public void convert_Float_Boolean()      { assertEquals(true, convertor.convert(1f, Boolean.class)); }
        @Test(expected = InputConversionException.class) public void convert_Double_Boolean()     { assertEquals(true, convertor.convert(1d, Boolean.class)); }
        @Test public void convert_Character_Boolean()   { assertEquals(true, convertor.convert('t', Boolean.class)); }
        @Test public void convert_String_Boolean()      { assertEquals(true, convertor.convert("true", Boolean.class)); }
        @Test public void convert_Boolean_Boolean()     { assertEquals(true, convertor.convert(true, Boolean.class)); }
        //</editor-fold>
    }

    @SuppressWarnings("PublicInnerClass")
    public class GivenTheSingletonConvertor
            extends GivenConvertorInstance {

        @Override
        protected TypeConvertor getConvertor() {
            return TypeConvertor.getSingletonInstance();
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class GivenTheDefaultConvertor
            extends GivenConvertorInstance {

        @Override
        protected TypeConvertor getConvertor() {
            return TypeConvertor.newDefaultInstance();
        }
    }
}
