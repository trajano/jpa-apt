package net.trajano.apt.jpa.internal;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Models and operation that takes in an entity manager as it's first parameter.
 *
 * @author Archimedes Trajano
 */
public class MetaOperation {
    private final String methodName;
    private final List<String> parameterDeclarations;
    private final List<String> parameterNames;
    private final String returnType;

    private final List<String> thrownTypes;

    /**
     * Extracts data from the executable element to form the model. The
     * annotations are not copied over as the resulting SLSB may not expect them
     * and would cause problems (e.g. @Inject when there shouldn't be one).
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
            final StringBuilder parameter = new StringBuilder();

            for (final Modifier modifier : varElement.getModifiers()) {
                parameter.append(modifier);
                parameter.append(' ');
            }
            parameter.append(varElement.asType());
            parameter.append(' ');
            parameter.append(varElement.getSimpleName());
            parameterDeclarations.add(parameter.toString());
            parameterNames.add(varElement.getSimpleName().toString());
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
