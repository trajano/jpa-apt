package net.trajano.apt.jpa.internal;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

public class MetaTableModule {
    /**
     * Pluralizes a string. The logic is add "s" if the last character is not an
     * "s" with the following exceptions:
     * <ul>
     * <li>add "es" if the input string ends with an "s".
     * <li>add "es" if the input string ends with an "x".
     * <li>replace the last character and add "ies" if the input string ends
     * with an "y".
     * </ul>
     *
     * @param s
     *            string to pluralize
     * @return plural string
     */
    public static String pluralize(final String s) {
        if (s.endsWith("s") || s.endsWith("x")) {
            return s + "es";
        } else if (s.endsWith("y")) {
            return s.substring(0, s.length() - 1) + "ies";
        } else {
            return s + "s";
        }
    }

    /**
     * Class name for the table module.
     */
    private final String className;
    private final String entityClassName;
    private final List<MetaOperation> extraOperations;

    private final String idType;

    private final List<MetaNamedQuery> namedQueries;

    private final String packageName;

    private final String qualifiedName;

    public MetaTableModule(final TypeElement entityType) {
        packageName = ((PackageElement) entityType.getEnclosingElement())
                .getQualifiedName().toString();
        entityClassName = entityType.getSimpleName().toString();
        className = pluralize(entityClassName);
        qualifiedName = pluralize(entityType.getQualifiedName().toString());
        namedQueries = new ArrayList<MetaNamedQuery>();
        final NamedQueries namedQueriesAnnotation = entityType
                .getAnnotation(NamedQueries.class);
        if (namedQueriesAnnotation != null) {
            for (final NamedQuery namedQueryAnnotation : namedQueriesAnnotation
                    .value()) {
                namedQueries.add(new MetaNamedQuery(namedQueryAnnotation));
            }
        }

        String idType = null;
        extraOperations = new ArrayList<MetaOperation>();
        for (final Element element : entityType.getEnclosedElements()) {
            if (ElementKind.FIELD == element.getKind()
                    && element.getAnnotation(Id.class) != null) {
                idType = ((VariableElement) element).asType().toString();
            } else if (isExtraOperation(element)) {
                extraOperations.add(new MetaOperation(
                        (ExecutableElement) element));
            }
        }
        this.idType = idType;
    }

    public String getClassName() {
        return className;
    }

    public String getEntityClassName() {
        return entityClassName;
    }

    public List<MetaOperation> getExtraOperations() {
        return extraOperations;
    }

    public String getIdType() {
        return idType;
    }

    public List<MetaNamedQuery> getNamedQueries() {
        return namedQueries;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    /**
     * Checks if the element represents an extra operation. An extra operation
     * is defined as a public static method that takes in an EntityManager as
     * it's first parameter.
     *
     * @param element
     *            element to evaluate
     * @return
     */
    private boolean isExtraOperation(final Element element) {
        if (!ElementKind.METHOD.equals(element.getKind())) {
            return false;
        }
        final ExecutableElement methodElement = (ExecutableElement) element;
        if (!methodElement.getModifiers().contains(Modifier.PUBLIC)) {
            return false;
        }
        if (!methodElement.getModifiers().contains(Modifier.STATIC)) {
            return false;
        }
        if (!"javax.persistence.EntityManager".equals(methodElement
                .getParameters().get(0).asType().toString())) {
            return false;
        }
        return true;
    }
}
