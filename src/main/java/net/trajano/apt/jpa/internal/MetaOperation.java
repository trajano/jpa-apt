package net.trajano.apt.jpa.internal;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Models and operation that takes in an entity manager as it's first parameter.
 *
 * @author Archimedes Trajano
 */
public class MetaOperation {
    /**
     * Method name.
     */
    private final String methodName;

    /**
     * Parameter declarations. The parameter declaration includes the modifiers
     * such as <code>final</code>, the parameter type and parameter name.
     */
    private final List<String> parameterDeclarations;

    /**
     * Parameter names. This should be the same size as the parameter
     * declarations.
     */
    private final List<String> parameterNames;

    /**
     * The return type. This may be <code>void</code>.
     */
    private final String returnType;

    /**
     * List of types that are thrown, may be an empty list.
     */
    private final List<String> thrownTypes;

    /**
     * Extracts data from the executable element to form the model. The
     * annotations are not copied over as the resulting SLSB may not expect them
     * and would cause problems (e.g. @Inject when there shouldn't be one).
     * Modifiers are also ignored, but a <code>final</code> modifier is
     * explicitly added.
     *
     * @param element
     *            the method to process
     */
    public MetaOperation(final ExecutableElement element) {
        methodName = element.getSimpleName().toString();
        parameterDeclarations = new ArrayList<String>();
        parameterNames = new ArrayList<String>();
        thrownTypes = new ArrayList<String>();
        final List<? extends VariableElement> parameters = element
                .getParameters();
        for (int i = 1; i < parameters.size(); ++i) {
            final VariableElement varElement = parameters.get(i);
            final StringBuilder parameter = new StringBuilder("final ");
            parameter.append(varElement.asType());
            parameter.append(' ');

            final String parameterName = varElement.getSimpleName().toString();
            parameter.append(parameterName);
            parameterDeclarations.add(parameter.toString());
            parameterNames.add(parameterName);
        }

        for (final TypeMirror thrownType : element.getThrownTypes()) {
            thrownTypes.add(thrownType.toString());
        }

        returnType = element.getReturnType().toString();
    }

    public String getMethodName() {
        return methodName;
    }

    public List<String> getParameterDeclarations() {
        return parameterDeclarations;
    }

    public List<String> getParameterNames() {
        return parameterNames;
    }

    public String getReturnType() {
        return returnType;
    }

    public List<String> getThrownTypes() {
        return thrownTypes;
    }

    @Override
    public String toString() {
        return "MetaOperation [methodName=" + methodName + ", parameterTypes="
                + parameterDeclarations + ", thrownTypes=" + thrownTypes + "]";
    }

}
