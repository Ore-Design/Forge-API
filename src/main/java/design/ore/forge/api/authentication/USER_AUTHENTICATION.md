# Module User Authentication

This guide explains how Forge modules can access authenticated user information.

## Overview

When a request is forwarded to a module, Forge automatically extracts the authenticated user information and makes it available through the `ForgeUserInfo` object. This is a simplified wrapper that doesn't expose internal entity details.

## User Information Available

The `ForgeUserInfo` class provides:
- `id` - User's unique identifier
- `email` - User's email address
- `firstName` - User's first name
- `lastName` - User's last name
- `encodedProfilePicture` - Base64 encoded profile picture
- `roles` - List of `ForgeRoleInfo` objects

Helper methods:
- `getFullName()` - Returns first name + last name
- `hasRole(String roleName)` - Checks if user has a specific role
- `hasPermission(String permission)` - Checks if user has a specific permission

## Usage Method 1: Annotation-Based (Recommended)

### Step 1: Configure Your Module

Create a configuration class extending `ForgeModuleWebConfig`:

```java

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyModuleWebConfig implements WebMvcConfigurer
{
    // The parent class automatically registers the user argument resolver
    // Add additional configuration here if needed
}
```

Alternatively, if you annotate your module with `@ForgeWeb`, static resource and module user authentication is auto-configured:

```java

import design.ore.forge.api.annotations.ForgeModule;
import design.ore.forge.api.annotations.ForgeWeb;
import design.ore.forge.api.interfaces.IForgeModule;

@ForgeModule(
    value = "example-module",
    name = "Example Module",
    version = "1.0.0",
    rootPackage = "com.example.module"
)
@ForgeWeb // This annotation autoconfigures module authentication
public class ExampleModule implements IForgeModule
{
    // Module implementation
}
```

### Step 2: Use in Controller Methods

Add the `@ModuleUser` annotation to your controller method parameters:

```java
import design.ore.forge.api.authentication.ModuleUser;
import design.ore.forge.api.authentication.ForgeUserInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class MyModuleController
{
    @GetMapping("/profile")
    public String getUserProfile(@ModuleUser Optional<ForgeUserInfo> user)
    {
        if (user.isPresent())
        {
            ForgeUserInfo userInfo = user.get();
            return "Welcome, " + userInfo.getFullName() + "!";
        }
        return "User not authenticated";
    }

    @GetMapping("/admin")
    public String adminEndpoint(@ModuleUser Optional<ForgeUserInfo> user)
    {
        if (user.isPresent() && user.get().hasRole("Admin"))
        {
            return "Admin access granted";
        }
        return "Access denied";
    }
}
```

## Usage Method 2: Programmatic Access

If you need to access the user outside of controller parameters, use `ForgeUserUtils`:

```java
import design.ore.forge.api.authentication.ForgeUserInfo;
import design.ore.forge.api.authentication.ForgeUserUtils;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public class MyService
{
    public void someMethod(HttpServletRequest request)
    {
        Optional<ForgeUserInfo> user = ForgeUserUtils.getCurrentUser(request);

        // If user is present, authentication is successful.
        // Otherwise, user still needs to log in.
        
        if (user.isPresent())
        {
            String email = user.get().getEmail();
            // Use user information
        }
    }
}
```

## Permission Checking

Check user permissions for fine-grained access control:

```java
@GetMapping("/manage-content")
public String manageContent(@ModuleUser Optional<ForgeUserInfo> user)
{
    if (user.isPresent() && user.get().hasPermission("manage-content"))
    {
        return "You can manage content";
    }
    return "Insufficient permissions";
}
```

## Role Information

Access role details through `ForgeRoleInfo`:

```java
@GetMapping("/roles")
public List<String> getUserRoles(@ModuleUser Optional<ForgeUserInfo> user)
{
    if (user.isEmpty()) return List.of();

    return user.get().getRoles().stream()
            .map(ForgeRoleInfo::getName)
            .collect(Collectors.toList());
}
```

## Important Notes

1. **Always use Optional**: The user parameter should always be `Optional<ForgeUserInfo>` as the user may not be authenticated.

2. **No Direct Entity Access**: The `ForgeUserInfo` is a simplified wrapper that doesn't expose internal JPA entities, keeping the API clean and stable.

3. **Automatic Injection**: User information is automatically extracted by the `ModuleNamespaceFilter` in the parent application - modules don't need to interact with authentication directly.

4. **Debug Mode**: In debug mode, a default debug user is automatically provided for testing purposes.