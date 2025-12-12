package design.ore.forge.api.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Simplified user information exposed to modules.
 * This wrapper prevents exposing the internal ForgeUser entity to the public API.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ForgeUserInfo
{
    private long id;
    private String email;
    private String firstName;
    private String lastName;
    private String encodedProfilePicture;
    private List<ForgeRoleInfo> roles;

    public String getFullName()
    {
        return firstName + " " + lastName;
    }

    public boolean hasRole(String roleName)
    {
        return roles != null && roles.stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase(roleName));
    }

    public boolean hasPermission(String permission)
    {
        return roles != null && roles.stream()
                .anyMatch(role -> role.hasPermission(permission));
    }
}