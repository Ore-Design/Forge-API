package design.ore.forge.api.registrations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class LinkRegistration extends Registration
{
    public LinkRegistration(String name, String targetUrl, String logoOverrideUrl)
    {
        super(name, targetUrl);
        this.logoOverrideUrl = logoOverrideUrl;
    }

    @Setter String logoOverrideUrl;
}
