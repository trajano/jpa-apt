package net.trajano.apt.jpa.internal;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Types;

public class MetaTableModel {
    /**
     * Pluralizes a string. The logic is add "s" if the last character is not an
     * "s" with the following exceptions:
     * <ul>
     * <li>add "es" if the input string ends with an "s".
     * <li>add "es" if the input string ends with an "x".
     * <li>replace the last character and add "ies" if the input string ends
     * with an "y".
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

    private final String className;
    private final String entityClassName;

    private final String packageName;

    private final String qualifiedName;

    public MetaTableModel(final TypeElement entityType, final Types types) {
        packageName = ((PackageElement) entityType.getEnclosingElement())
                .getQualifiedName().toString();
        entityClassName = entityType.getSimpleName().toString();
        className = pluralize(entityClassName);
        qualifiedName = pluralize(entityType.getQualifiedName().toString());
    }

    public String getClassName() {
        return className;
    }

    public String getEntityClassName() {
        return entityClassName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }
}
