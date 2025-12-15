package design.ore.forge.api.processing;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auto.service.AutoService;
import design.ore.forge.api.ForgeApiConstants;
import design.ore.forge.api.annotations.ForgeModule;
import design.ore.forge.api.interfaces.IForgeModule;

@SupportedAnnotationTypes({"design.ore.forge.api.annotations.ForgeModule"})
@SupportedSourceVersion(SourceVersion.RELEASE_21)
@AutoService(Processor.class)
public class ForgeModuleProcessor extends AbstractProcessor
{
    public static final String OUTPUT_FILE = "META-INF/forge-module-manifest.json";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
    {
        if (annotations.isEmpty()) return false;

        ForgeModuleManifest manifest = new ForgeModuleManifest();

        try {
            for (TypeElement annotation : annotations) {
                if (isModuleAnnotation(annotation)) {
                    for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                        if (element.getKind() != ElementKind.CLASS) {
                            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Module annotation can only be applied to classes.", element);
                            continue;
                        }

                        TypeElement classElement = (TypeElement) element;
                        if (!implementsModuleInterface(classElement, processingEnv.getTypeUtils(), processingEnv.getElementUtils())) {
                            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Annotated class must implement IForgeModule.", classElement);
                            continue;
                        }

                        manifest.setModuleId((String) getModuleAnnotationValue(classElement, "value"));
                        manifest.setModuleName((String) getModuleAnnotationValue(classElement, "name"));
                        manifest.setModuleVersion((String) getModuleAnnotationValue(classElement, "version"));
                        manifest.setModuleRootPackage((String) getModuleAnnotationValue(classElement, "rootPackage"));
                        manifest.setModuleIconPath((String) getModuleAnnotationValue(classElement, "iconPath"));
                        manifest.setCompatibleForgeAPIVersion(ForgeApiConstants.VERSION);

                        // Handle requireAuthentication with default value of true
                        Boolean requireAuth = (Boolean) getModuleAnnotationValue(classElement, "requireAuthentication");
                        manifest.setRequireAuthentication(requireAuth != null ? requireAuth : true);

                        // Handle sessionCreationPolicy with default value of "IF_REQUIRED"
                        String sessionPolicy = (String) getModuleAnnotationValue(classElement, "sessionCreationPolicy");
                        manifest.setSessionCreationPolicy(sessionPolicy != null ? sessionPolicy : "IF_REQUIRED");

                        // Handle disableCsrf with default value of false
                        Boolean disableCsrf = (Boolean) getModuleAnnotationValue(classElement, "disableCsrf");
                        manifest.setDisableCsrf(disableCsrf != null ? disableCsrf : false);

                        // Handle acceptJwtTokens with default value of false
                        Boolean acceptJwtTokens = (Boolean) getModuleAnnotationValue(classElement, "acceptJwtTokens");
                        manifest.setAcceptJwtTokens(acceptJwtTokens != null ? acceptJwtTokens : false);
                    }
                }
            }
        }
        catch (Exception e)
        {
            System.err.println("Exception while processing Forge module annotations!");
            e.printStackTrace();
        }

        if (manifest.getModuleName() != null && !manifest.getModuleName().isEmpty()) writeManifestToFile(manifest);
        else System.err.println("Valid Forge module must have a module name! Invalid name: " + manifest.getModuleName());

        return true;
    }

    private Object getModuleAnnotationValue(TypeElement methodElement, String key)
    {
        for (AnnotationMirror annotationMirror : methodElement.getAnnotationMirrors())
        {
            if (annotationMirror.getAnnotationType().toString().equals(ForgeModule.class.getCanonicalName()))
            {
                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror.getElementValues().entrySet())
                {
                    if (entry.getKey().getSimpleName().toString().equals(key)) return entry.getValue().getValue();
                }
            }
        }
        return null;
    }

    private boolean isModuleAnnotation(Element element)
    { return element.asType().toString().equals(ForgeModule.class.getCanonicalName()); }

    private boolean implementsModuleInterface(TypeElement classElement, Types typeUtils, Elements elementUtils)
    {
        // Get the TypeMirror of IForgeModule
        TypeMirror moduleInterface = elementUtils.getTypeElement(IForgeModule.class.getCanonicalName()).asType();

        return implementsInterfaceRecursive(classElement.asType(), moduleInterface, typeUtils);
    }

    private boolean implementsInterfaceRecursive(TypeMirror type, TypeMirror target, Types typeUtils)
    {
        // Base case: exact match
        if (typeUtils.isSameType(type, target)) return true;

        // Get the element for this type
        Element element = typeUtils.asElement(type);
        if (!(element instanceof TypeElement typeElement)) return false;

        // Check all directly implemented interfaces
        for (TypeMirror iface : typeElement.getInterfaces())
        {
            if(implementsInterfaceRecursive(iface, target, typeUtils)) { return true; }
        }

        // Recurse into superclass
        TypeMirror superClass = typeElement.getSuperclass();
        if (superClass != null && superClass.getKind() != TypeKind.NONE)
        { return implementsInterfaceRecursive(superClass, target, typeUtils); }

        return false;
    }

    private void writeManifestToFile(ForgeModuleManifest manifest)
    {
        try
        {
            FileObject fileObject = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", OUTPUT_FILE);
            try (PrintWriter out = new PrintWriter(fileObject.openWriter())) { out.write(MAPPER.writeValueAsString(manifest)); }
        }
        catch (IOException e)
        {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Failed to write module manifest: " + e.getMessage());
        }
    }
}