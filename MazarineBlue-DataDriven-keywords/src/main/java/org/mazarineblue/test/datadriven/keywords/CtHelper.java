/*
 * Copyright (c) 2012-2014 Alex de Kruijff
 * Copyright (c) 2014-2015 Specialisterren
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
package org.mazarineblue.test.datadriven.keywords;

import java.util.Map;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.test.datadriven.TestContext;
import org.mazarineblue.test.datadriven.ValidationProfile;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
class CtHelper {

    static private final ClassPool pool = ClassPool.getDefault();
    private final CtClass base;
    private final String path;
    private final ConstPool constPool;

    CtHelper(long id)
            throws NotFoundException {
        base = pool.get(DynamicSuite.class.getCanonicalName());
        path = "JavassistDataDrivenKeywordsSuite" + id;
        constPool = new ConstPool(path);
    }

    public CtClass createClassDefinition() {
        CtClass clazz = pool.makeClass(path, base);
        return clazz;
    }

    public void addMethods(CtClass clazz, Map<String, Testcase> map)
            throws CannotCompileException {
        if (map == null)
            return;
        CtClass booleanClazz = pool.makeClass(Boolean.class.getCanonicalName());
        CtClass[] testParamaters = createTestParamaters(pool);
        CtClass[] profileParamaters = createProfileParameters(pool);
        for (String name : map.keySet()) {
            clazz.addMethod(createTestMethod(booleanClazz, name, testParamaters,
                                             clazz, constPool));
            clazz.addMethod(createProfileMethod(name, profileParamaters, clazz,
                                                constPool));
        }
    }

    private CtClass[] createTestParamaters(ClassPool pool) {
        CtClass[] parameters = new CtClass[2];
        parameters[0] = pool.makeClass(DataSource.class.getCanonicalName());
        parameters[1] = pool.makeClass(TestContext.class.getCanonicalName());
        return parameters;
    }

    private CtClass[] createProfileParameters(ClassPool pool) {
        CtClass[] parameters = new CtClass[1];
        parameters[0] = pool.makeClass(
                ValidationProfile.class.getCanonicalName());
        return parameters;
    }

    private CtMethod createTestMethod(CtClass returnType, String name,
                                      CtClass[] parameters, CtClass declairing,
                                      ConstPool constPool)
            throws CannotCompileException {
        CtMethod method = CtNewMethod.make(returnType, name + "Test", parameters,
                                           null, null, declairing);
        AnnotationsAttribute attr = new AnnotationsAttribute(constPool,
                                                             AnnotationsAttribute.visibleTag);
        Annotation annotation = new Annotation(
                "org.mazarineblue.test.datadriven.DataDrivenTest", constPool);
        attr.addAnnotation(annotation);
        method.getMethodInfo().addAttribute(attr);
        method.setBody("{ return execute(\"" + name + "\"); }");
        return method;
    }

    private CtMethod createProfileMethod(String name, CtClass[] parameters,
                                         CtClass declairing, ConstPool constPool)
            throws CannotCompileException {
        CtMethod method = CtNewMethod.make(Modifier.STATIC | Modifier.PUBLIC,
                                           CtClass.voidType, name + "Profile",
                                           parameters, null, null, declairing);
        AnnotationsAttribute attr = new AnnotationsAttribute(constPool,
                                                             AnnotationsAttribute.visibleTag);
        Annotation annotation = new Annotation(
                "org.mazarineblue.test.datadriven.DataDrivenProfile", constPool);
        attr.addAnnotation(annotation);
        method.getMethodInfo().addAttribute(attr);
        return method;
    }

    public DynamicSuite createInstance(CtClass clazz)
            throws CannotCompileException, InstantiationException,
                   IllegalAccessException {
        Class<DynamicSuite> toClass = clazz.toClass();
        DynamicSuite suite = toClass.newInstance();
        return suite;
    }
}
