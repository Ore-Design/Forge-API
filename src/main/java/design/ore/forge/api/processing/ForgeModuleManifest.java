package design.ore.forge.api.processing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ForgeModuleManifest
{
    String moduleId, moduleName, moduleVersion, compatibleForgeAPIVersion, moduleRootPackage, moduleIconPath;
    boolean requireAuthentication;
    String sessionCreationPolicy; // ALWAYS, IF_REQUIRED, NEVER, STATELESS
    boolean disableCsrf;
    boolean acceptJwtTokens; // If true, module endpoints accept OAuth2 JWT bearer tokens
}
