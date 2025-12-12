package design.ore.forge.api.registrations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Getter
public class AppletRegistration extends Registration
{
    public AppletRegistration(String name, String targetUrl, String logoResourcePath)
    {
        super(name, targetUrl);
        this.logoResourcePath = logoResourcePath;
    }

    @Setter String logoResourcePath;
}
