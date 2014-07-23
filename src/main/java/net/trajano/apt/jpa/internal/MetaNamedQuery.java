package net.trajano.apt.jpa.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.NamedQuery;

/**
 * Represents the named query.
 *
 * @author Archimedes Trajano
 *
 */
public class MetaNamedQuery {
    /**
     * Method name. This is extracted from the name of the named query as the
     * text segment after the last "." character.
     */
    private final String methodName;

    /**
     * Name of the named query. Used for the generated JavaDoc.
     */
    private final String name;

    /**
     * List of parameter names.
     */
    private final List<String> parameters;

    /**
     * Extracts the data from the annotation. The parameters names are obtained
     * by finding valid parameter identifiers prefixed with a ":".
     *
     * @param namedQueryAnnotation
     *            annotation to process.
     */
    public MetaNamedQuery(final NamedQuery namedQueryAnnotation) {
        name = namedQueryAnnotation.name();
        methodName = name.substring(name.lastIndexOf('.') + 1);
        parameters = new ArrayList<String>();
        final Matcher m = Pattern.compile("\\:([^=<>\\s\\']+)").matcher(
                namedQueryAnnotation.query());
        while (m.find()) {
            parameters.add(m.group(1));
        }
    }

    public String getMethodName() {
        return methodName;
    }

    public String getName() {
        return name;
    }

    public List<String> getParameters() {
        return parameters;
    }

}
