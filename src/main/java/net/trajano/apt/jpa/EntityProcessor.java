package net.trajano.apt.jpa;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.persistence.Entity;

import net.trajano.apt.jpa.internal.MetaTableModule;
import net.trajano.apt.jpa.internal.TableModuleGenerator;

/**
 * This is used to process JPA classes to add support code. The support code is
 * the plural version of the entity name.
 */
@SupportedAnnotationTypes("javax.persistence.Entity")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class EntityProcessor extends AbstractProcessor {
    @Override
    public boolean process(final Set<? extends TypeElement> annotations,
            final RoundEnvironment roundEnv) {
        final TableModuleGenerator generator = new TableModuleGenerator();
        for (final Element element : roundEnv
                .getElementsAnnotatedWith(Entity.class)) {
            try {
                final TypeElement typeElement = (TypeElement) element;
                final MetaTableModule tableModule = new MetaTableModule(
                        typeElement);
                final Writer w = processingEnv
                        .getFiler()
                        .createSourceFile(tableModule.getQualifiedName(),
                                element).openWriter();

                w.write(generator.generate(tableModule));
                w.close();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
