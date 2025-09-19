package design.ore.forge.api.interfaces;

import ch.qos.logback.classic.Logger;
import design.ore.forge.api.applets.AppletRegistration;

import java.io.File;
import java.util.function.Consumer;

public interface IModuleContext
{
    Logger getLog();
    File getModulePersistentDataDirectory();
    void registerApplet(AppletRegistration registration);
}
