/*
 * Copyright (c) 2015 Alex de Kruijff
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.mazarineblue.parser.precedenceclimbing.operators;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class OperatorsUtil {

    static boolean areBothBoolean(Object left, Object right) {
        return isBoolean(left) && isBoolean(right);
    }

    static boolean isBoolean(Object obj) {
        boolean b = obj instanceof Boolean;
        boolean str = obj instanceof String;
        return b || str;
    }

    static Boolean toBoolean(Object obj) {
        if (obj instanceof String)
            return Boolean.parseBoolean((String) obj);
        return (Boolean) obj;
    }

    static boolean areBothInteger(Object left, Object right) {
        return isInteger(left) && isInteger(right);
    }

    static boolean isInteger(Object obj) {
        boolean b = obj instanceof Byte;
        boolean s = obj instanceof Short;
        boolean i = obj instanceof Integer;
        boolean str = obj instanceof String && isInteger((String) obj);
        return b || s || i || str;
    }

    static private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    static Integer toInteger(Object obj) {
        if (obj instanceof String)
            return Integer.parseInt((String) obj);
        if (obj instanceof Byte) {
            Byte b = (Byte) obj;
            return b.intValue();
        }
        if (obj instanceof Short) {
            Short s = (Short) obj;
            return s.intValue();
        }
        return (Integer) obj;
    }

    static boolean areBothLong(Object left, Object right) {
        return isLong(left) && isLong(right);
    }

    static boolean isLong(Object obj) {
        boolean b = obj instanceof Byte;
        boolean s = obj instanceof Short;
        boolean i = obj instanceof Integer;
        boolean l = obj instanceof Long;
        boolean str = obj instanceof String && isLong((String) obj);
        return b || s || i || l || str;
    }

    static private boolean isLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    static Long toLong(Object obj) {
        if (obj instanceof String)
            return Long.parseLong((String) obj);
        if (obj instanceof Byte) {
            Byte b = (Byte) obj;
            return b.longValue();
        }
        if (obj instanceof Short) {
            Short s = (Short) obj;
            return s.longValue();
        }
        if (obj instanceof Integer) {
            Integer i = (Integer) obj;
            return i.longValue();
        }
        return (Long) obj;
    }

    static boolean areBothFloat(Object left, Object right) {
        return isFloat(left) && isFloat(right);
    }

    static boolean isFloat(Object obj) {
        boolean b = obj instanceof Byte;
        boolean s = obj instanceof Short;
        boolean i = obj instanceof Integer;
        boolean l = obj instanceof Long;
        boolean f = obj instanceof Float;
        boolean str = obj instanceof String && isFloat((String) obj);
        return b || s || i || l || f || str;
    }

    static private boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    static Float toFloat(Object obj) {
        if (obj instanceof String)
            return Float.valueOf((String) obj);
        if (obj instanceof Byte) {
            Byte b = (Byte) obj;
            return b.floatValue();
        }
        if (obj instanceof Short) {
            Short s = (Short) obj;
            return s.floatValue();
        }
        if (obj instanceof Integer) {
            Integer i = (Integer) obj;
            return i.floatValue();
        }
        if (obj instanceof Long) {
            Long l = (Long) obj;
            return l.floatValue();
        }
        return (Float) obj;
    }

    static boolean areBothDouble(Object left, Object right) {
        return isDouble(left) && isDouble(right);
    }

    static boolean isDouble(Object obj) {
        boolean b = obj instanceof Byte;
        boolean s = obj instanceof Short;
        boolean i = obj instanceof Integer;
        boolean l = obj instanceof Long;
        boolean f = obj instanceof Float;
        boolean d = obj instanceof Double;
        boolean str = obj instanceof String && isDouble((String) obj);
        return b || s || i || l || f || d || str;
    }

    static private boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    static Double toDouble(Object obj) {
        if (obj instanceof String)
            return Double.valueOf((String) obj);
        if (obj instanceof Byte) {
            Byte b = (Byte) obj;
            return b.doubleValue();
        }
        if (obj instanceof Short) {
            Short s = (Short) obj;
            return s.doubleValue();
        }
        if (obj instanceof Integer) {
            Integer i = (Integer) obj;
            return i.doubleValue();
        }
        if (obj instanceof Long) {
            Long l = (Long) obj;
            return l.doubleValue();
        }
        if (obj instanceof Float) {
            Float f = (Float) obj;
            return f.doubleValue();
        }
        return (Double) obj;
    }

    private OperatorsUtil() {
    }
}
