package design.ore.forge.api.applets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class AppletRegistration
{
    String name;
    @Setter String logoResourcePath;
    String targetUrl;
}
