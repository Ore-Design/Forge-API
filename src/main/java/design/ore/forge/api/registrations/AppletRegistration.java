package design.ore.forge.api.registrations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class AppletRegistration
{
    String name;
    @Setter String logoResourcePath;
    @Setter String targetUrl;
}
