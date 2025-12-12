package design.ore.forge.api.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Simplified role information exposed to modules.
 * This wrapper prevents exposing the internal ForgeRole entity to the public API.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ForgeRoleInfo
{
    private long id;
    private String name;
    private List<String> permissions;

    public boolean hasPermission(String permission)
    {
        return permissions != null && permissions.contains(permission);
    }
}