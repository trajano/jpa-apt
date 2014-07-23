package net.trajano.apt.jpa.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.NamedQuery;

public class MetaNamedQuery {
    private final String methodName;
    private final String name;
    private final List<String> parameters;

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
