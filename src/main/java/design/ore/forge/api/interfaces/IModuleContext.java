package design.ore.forge.api.interfaces;

import ch.qos.logback.classic.Logger;
import design.ore.forge.api.registrations.AppletRegistration;
import design.ore.forge.api.registrations.LinkRegistration;

import java.io.File;

public interface IModuleContext
{
    Logger getLog();
    File getModulePersistentDataDirectory();
    void registerApplet(AppletRegistration registration);
    void registerExternalLink(LinkRegistration registration);
}
