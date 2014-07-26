package net.trajano.apt.jpa.test;

import static java.util.Collections.singleton;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.tools.JavaFileObject;

import net.trajano.apt.jpa.EntityProcessor;
import net.trajano.apt.jpa.internal.MetaOperation;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Mock tests
 */
public class MockTest {

    @Test
    public void testEmptyEnvironment() {
        final RoundEnvironment roundEnv = mock(RoundEnvironment.class);
        final ProcessingEnvironment processingEnv = mock(ProcessingEnvironment.class);
        final EntityProcessor entityProcessor = new EntityProcessor();
        entityProcessor.init(processingEnv);
        assertTrue(entityProcessor.process(
                Collections.<TypeElement> emptySet(), roundEnv));
    }

    @Test
    public void testFileWrittenOut() throws Exception {
        final Name packageName = mock(Name.class);

        final PackageElement packageElement = mock(PackageElement.class);
        when(packageElement.getQualifiedName()).thenReturn(packageName);

        final Name entitySimpleName = mock(Name.class);
        final Name entityQualifiedName = mock(Name.class);

        final TypeElement entityElement = mock(TypeElement.class);
        when(entityElement.getEnclosingElement()).thenReturn(packageElement);
        when(entityElement.getQualifiedName()).thenReturn(entityQualifiedName);
        when(entityElement.getSimpleName()).thenReturn(entitySimpleName);

        final RoundEnvironment roundEnv = mock(RoundEnvironment.class);
        when(roundEnv.getElementsAnnotatedWith(Entity.class)).thenAnswer(
                new Answer<Set<? extends Element>>() {

                    @Override
                    public Set<? extends Element> answer(
                            final InvocationOnMock invocation) throws Throwable {
                        return Collections.singleton(entityElement);
                    }
                });

        final StringWriter writer = new StringWriter();
        final JavaFileObject javaFileObject = mock(JavaFileObject.class);
        when(javaFileObject.openWriter()).thenReturn(writer);

        final Filer filer = mock(Filer.class);
        when(
                filer.createSourceFile(any(CharSequence.class),
                        any(Element.class))).thenReturn(javaFileObject);

        final Messager messsager = mock(Messager.class);
        final ProcessingEnvironment processingEnv = mock(ProcessingEnvironment.class);
        when(processingEnv.getFiler()).thenReturn(filer);
        when(processingEnv.getMessager()).thenReturn(messsager);

        final EntityProcessor entityProcessor = new EntityProcessor();
        entityProcessor.init(processingEnv);
        entityProcessor.process(Collections.<TypeElement> emptySet(), roundEnv);
        assertTrue(entityProcessor.process(
                Collections.<TypeElement> emptySet(), roundEnv));
        assertTrue(writer.toString().startsWith("package"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testIOExceptionEnvironment() {
        final Name packageName = mock(Name.class);

        final PackageElement packageElement = mock(PackageElement.class);
        when(packageElement.getQualifiedName()).thenReturn(packageName);

        final Name entitySimpleName = mock(Name.class);
        final Name entityQualifiedName = mock(Name.class);

        final TypeElement entityElement = mock(TypeElement.class);
        when(entityElement.getEnclosingElement()).thenReturn(packageElement);
        when(entityElement.getQualifiedName()).thenReturn(entityQualifiedName);
        when(entityElement.getSimpleName()).thenReturn(entitySimpleName);

        final RoundEnvironment roundEnv = mock(RoundEnvironment.class);
        when(roundEnv.getElementsAnnotatedWith(Entity.class)).thenAnswer(
                new Answer<Set<? extends Element>>() {

                    @Override
                    public Set<? extends Element> answer(
                            final InvocationOnMock invocation) throws Throwable {
                        return Collections.singleton(entityElement);
                    }
                });

        final Messager messsager = mock(Messager.class);
        final ProcessingEnvironment processingEnv = mock(ProcessingEnvironment.class);
        when(processingEnv.getFiler()).thenThrow(IOException.class);
        when(processingEnv.getMessager()).thenReturn(messsager);

        final EntityProcessor entityProcessor = new EntityProcessor();
        entityProcessor.init(processingEnv);
        entityProcessor.process(Collections.<TypeElement> emptySet(), roundEnv);
        assertFalse(entityProcessor.process(
                Collections.<TypeElement> emptySet(), roundEnv));
    }

    @Test
    public void testMockOperationWithParametersAndThrows() {
        final Name extraOperationElementName = mock(Name.class);
        when(extraOperationElementName.toString()).thenReturn("extraOp");

        final TypeMirror returnType = mock(TypeMirror.class);

        final ExecutableElement extraOperationElement = mock(ExecutableElement.class);
        when(extraOperationElement.getKind()).thenReturn(ElementKind.METHOD);
        when(extraOperationElement.getReturnType()).thenReturn(returnType);
        when(extraOperationElement.getModifiers()).thenReturn(
                new HashSet<Modifier>(Arrays.asList(Modifier.STATIC,
                        Modifier.PUBLIC)));
        when(extraOperationElement.getSimpleName()).thenReturn(
                extraOperationElementName);
        when(extraOperationElement.getThrownTypes()).thenAnswer(
                new Answer<List<? extends TypeMirror>>() {

                    @Override
                    public List<? extends TypeMirror> answer(
                            final InvocationOnMock invocation) throws Throwable {
                        final TypeMirror exceptionType = mock(TypeMirror.class);
                        when(exceptionType.toString()).thenReturn(
                                "java.MyException");
                        return Arrays.asList(exceptionType);
                    }
                });
        when(extraOperationElement.getParameters()).thenAnswer(
                new Answer<List<? extends VariableElement>>() {

                    @Override
                    public List<? extends VariableElement> answer(
                            final InvocationOnMock invocation) throws Throwable {
                        final TypeMirror entityManagerParameterType = mock(TypeMirror.class);
                        when(entityManagerParameterType.toString()).thenReturn(
                                "javax.persistence.EntityManager");
                        final VariableElement entityManagerParameter = mock(VariableElement.class);
                        when(entityManagerParameter.asType()).thenReturn(
                                entityManagerParameterType);

                        final TypeMirror otherParameterType = mock(TypeMirror.class);
                        when(otherParameterType.toString())
                                .thenReturn("String");

                        final Name otherParameterName = mock(Name.class);
                        when(otherParameterName.toString()).thenReturn(
                                "otherParameter");

                        final VariableElement otherParameter = mock(VariableElement.class);
                        when(otherParameter.getSimpleName()).thenReturn(
                                otherParameterName);
                        when(otherParameter.asType()).thenReturn(
                                otherParameterType);

                        return Arrays.asList(entityManagerParameter,
                                otherParameter);
                    }
                });
        final MetaOperation operation = new MetaOperation(extraOperationElement);
        assertEquals("otherParameter", operation.getParameterNames().get(0));
        assertEquals("final String otherParameter", operation
                .getParameterDeclarations().get(0));
        operation.toString();
    }

    @Test
    public void testWithAll() throws Exception {
        final Name packageName = mock(Name.class);

        final PackageElement packageElement = mock(PackageElement.class);
        when(packageElement.getQualifiedName()).thenReturn(packageName);

        final Name entitySimpleName = mock(Name.class);
        final Name entityQualifiedName = mock(Name.class);

        final NamedQuery namedQueryNoParam = mock(NamedQuery.class);
        when(namedQueryNoParam.name()).thenReturn("Foo.barOp");
        when(namedQueryNoParam.query()).thenReturn("select * from dual");

        final NamedQuery namedQueryWithParam = mock(NamedQuery.class);
        when(namedQueryWithParam.name()).thenReturn("Foo.barParam");
        when(namedQueryWithParam.query()).thenReturn(
                "select * from dual where foo = :nyaa");

        final NamedQuery namedQueryWithTwoParam = mock(NamedQuery.class);
        when(namedQueryWithTwoParam.name()).thenReturn("Foo.barParam");
        when(namedQueryWithTwoParam.query()).thenReturn(
                "select * from dual where foo = :nyaa and bar = :cat");

        final NamedQueries namedQueries = mock(NamedQueries.class);
        when(namedQueries.value()).thenReturn(
                new NamedQuery[] { namedQueryNoParam, namedQueryWithParam,
                        namedQueryWithTwoParam });
        final TypeElement entityElement = mock(TypeElement.class);
        when(entityElement.getEnclosingElement()).thenReturn(packageElement);
        when(entityElement.getQualifiedName()).thenReturn(entityQualifiedName);
        when(entityElement.getSimpleName()).thenReturn(entitySimpleName);
        when(entityElement.getAnnotation(NamedQueries.class)).thenReturn(
                namedQueries);

        final VariableElement idElement = mock(VariableElement.class);
        when(idElement.getKind()).thenReturn(ElementKind.FIELD);
        when(idElement.getAnnotation(Id.class)).thenReturn(mock(Id.class));
        when(idElement.asType()).thenReturn(mock(TypeMirror.class));

        final VariableElement extraElement = mock(VariableElement.class);
        when(extraElement.getKind()).thenReturn(ElementKind.FIELD);
        when(extraElement.asType()).thenReturn(mock(TypeMirror.class));

        final Name extraOperationElementName = mock(Name.class);
        when(extraOperationElementName.toString()).thenReturn("extraOp");

        final TypeMirror returnType = mock(TypeMirror.class);

        final ExecutableElement extraOperationElement = mock(ExecutableElement.class);
        when(extraOperationElement.getKind()).thenReturn(ElementKind.METHOD);
        when(extraOperationElement.getReturnType()).thenReturn(returnType);
        when(extraOperationElement.getModifiers()).thenReturn(
                new HashSet<Modifier>(Arrays.asList(Modifier.STATIC,
                        Modifier.PUBLIC)));
        when(extraOperationElement.getSimpleName()).thenReturn(
                extraOperationElementName);
        when(extraOperationElement.getParameters()).thenAnswer(
                new Answer<List<? extends VariableElement>>() {

                    @Override
                    public List<? extends VariableElement> answer(
                            final InvocationOnMock invocation) throws Throwable {
                        final TypeMirror entityManagerParameterType = mock(TypeMirror.class);
                        when(entityManagerParameterType.toString()).thenReturn(
                                "javax.persistence.EntityManager");
                        final VariableElement entityManagerParameter = mock(VariableElement.class);
                        when(entityManagerParameter.asType()).thenReturn(
                                entityManagerParameterType);
                        return Arrays.asList(entityManagerParameter);
                    }
                });

        final ExecutableElement dontAddElement = mock(ExecutableElement.class);
        when(dontAddElement.getKind()).thenReturn(ElementKind.METHOD);
        when(dontAddElement.getReturnType()).thenReturn(returnType);
        when(dontAddElement.getModifiers()).thenReturn(
                new HashSet<Modifier>(Arrays.asList(Modifier.PUBLIC)));
        when(dontAddElement.getSimpleName()).thenReturn(
                extraOperationElementName);

        final ExecutableElement dontAddElement2 = mock(ExecutableElement.class);
        when(dontAddElement2.getKind()).thenReturn(ElementKind.METHOD);
        when(dontAddElement2.getReturnType()).thenReturn(returnType);
        when(dontAddElement2.getModifiers()).thenReturn(
                new HashSet<Modifier>(Arrays.asList(Modifier.STATIC,
                        Modifier.PUBLIC)));
        when(dontAddElement2.getSimpleName()).thenReturn(
                extraOperationElementName);
        when(dontAddElement2.getParameters()).thenAnswer(
                new Answer<List<? extends VariableElement>>() {

                    @Override
                    public List<? extends VariableElement> answer(
                            final InvocationOnMock invocation) throws Throwable {
                        final TypeMirror entityManagerParameterType = mock(TypeMirror.class);
                        when(entityManagerParameterType.toString()).thenReturn(
                                "javax.persistence.FooManager");
                        final VariableElement entityManagerParameter = mock(VariableElement.class);
                        when(entityManagerParameter.asType()).thenReturn(
                                entityManagerParameterType);
                        return Arrays.asList(entityManagerParameter);
                    }
                });

        when(entityElement.getEnclosedElements()).thenAnswer(
                new Answer<List<? extends Element>>() {

                    @Override
                    public List<? extends Element> answer(
                            final InvocationOnMock invocation) throws Throwable {
                        return Arrays.asList(idElement, extraOperationElement,
                                dontAddElement, dontAddElement2, extraElement);
                    }
                });

        final RoundEnvironment roundEnv = mock(RoundEnvironment.class);
        when(roundEnv.getElementsAnnotatedWith(Entity.class)).thenAnswer(
                new Answer<Set<? extends Element>>() {

                    @Override
                    public Set<? extends Element> answer(
                            final InvocationOnMock invocation) throws Throwable {
                        return singleton(entityElement);
                    }
                });

        final StringWriter writer = new StringWriter();
        final JavaFileObject javaFileObject = mock(JavaFileObject.class);
        when(javaFileObject.openWriter()).thenReturn(writer);

        final Filer filer = mock(Filer.class);
        when(
                filer.createSourceFile(any(CharSequence.class),
                        any(Element.class))).thenReturn(javaFileObject);

        final Messager messsager = mock(Messager.class);
        final ProcessingEnvironment processingEnv = mock(ProcessingEnvironment.class);
        when(processingEnv.getFiler()).thenReturn(filer);
        when(processingEnv.getMessager()).thenReturn(messsager);

        final EntityProcessor entityProcessor = new EntityProcessor();
        entityProcessor.init(processingEnv);
        entityProcessor.process(Collections.<TypeElement> emptySet(), roundEnv);
        assertTrue(entityProcessor.process(
                Collections.<TypeElement> emptySet(), roundEnv));
        assertTrue(writer.toString().startsWith("package"));
        assertTrue(writer.toString().contains("barOp()"));
        assertTrue(writer.toString().contains("barParam(final Object nyaa)"));
    }
}