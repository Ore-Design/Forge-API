package design.ore.forge.api.registrations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class LinkRegistration
{
    public LinkRegistration(String name, String targetUrl)
    {
        this.name = name;
        this.targetUrl = targetUrl;
    }

    String name;
    String targetUrl;
    @Setter String logoOverrideUrl;
}
