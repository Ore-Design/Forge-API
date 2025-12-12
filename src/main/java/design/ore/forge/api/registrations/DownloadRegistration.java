package design.ore.forge.api.registrations;

import design.ore.forge.api.enums.OS;
import lombok.Getter;

@Getter
public class DownloadRegistration extends AppletRegistration
{
    public DownloadRegistration(String name, String targetUrl, String logoResourcePath)
    {
        super(name, targetUrl, logoResourcePath);
    }

    public DownloadRegistration(String name, String targetUrl, String logoResourcePath, OS dependentOs)
    {
        this(name, targetUrl, logoResourcePath);
        this.dependentOs = dependentOs;
    }

    protected OS dependentOs;
}
