package nl.tim.questplugin;

import com.google.inject.Inject;
import com.google.inject.Injector;
import nl.tim.questplugin.storage.ConfigHandler;
import nl.tim.questplugin.storage.Storage;
import nl.tim.questplugin.storage.StorageProvider;
import nl.tim.questplugin.storage.image.builders.AreaImageBuilder;
import nl.tim.questplugin.storage.image.builders.PlayerImageBuilder;
import nl.tim.questplugin.storage.image.builders.QuestImageBuilder;
import nl.tim.questplugin.storage.image.builders.RegionImageBuilder;
import nl.tim.questplugin.utils.Constants;
import nl.tim.questplugin.utils.LocationSerializer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class QuestPlugin extends JavaPlugin
{
    public static StorageProvider.StorageType storageType;

    public static Logger logger;
    private Injector injector;

    @Inject private StorageProvider storageProvider;
    private Storage storage;
    private boolean storageLoaded = false;

    private ConfigHandler configHandler;

    @Inject private AreaImageBuilder areaImageBuilder;
    @Inject private QuestImageBuilder questImageBuilder;
    @Inject private PlayerImageBuilder playerImageBuilder;
    @Inject private RegionImageBuilder regionImageBuilder;

    @Override
    public void onEnable() {
        logger = getLogger();

        // Enable storage
        this.configHandler = new ConfigHandler(this);

        this.configHandler.init();
        String storageTypeResult = this.configHandler.getOption(String.class, Constants.STORAGE_OPTION);

        // Determining storage type for other data
        storageType = StorageProvider.StorageType.getType(storageTypeResult);

        if (storageType == null)
        {
            logger.warning("Unsupported storage type in config.yml: " + storageTypeResult);
            logger.warning("Will switch to default: FILE_BASED");

            storageType = StorageProvider.StorageType.FILE_BASED;
        }

        logger.info("Will use storage type: " + storageType.name());

        // Running DI
        logger.info("Running dependency injector");

        QuestBinder questBinder = new QuestBinder(this, getDataFolder());
        this.injector = questBinder.createInjector();

        // Inject all classes
        this.injector.injectMembers(this);

        // Continue normally
        logger.info("Loading handlers");

        this.storage = this.storageProvider.getStorage(storageType);
        LocationSerializer.configFolder = getDataFolder();

        this.storageLoaded = this.storage.init();

        // Check if storage loaded properly, otherwise disable plugin
        if (!this.storageLoaded)
        {
            logger.severe("Storage was not properly loaded, disabling plugin!");
            Bukkit.getPluginManager().disablePlugin(this);

            return;
        }

        // Continue loading if storage was properly initialized
        this.areaImageBuilder.save(null);

        // Done with loading
        logger.info("QuestPlugin is enabled!");
    }

    @Override
    public void onDisable() {
        // Check if we have to unload anything
        if (!this.storageLoaded)
        {
            // Storage wasn't loading, therefore nothing else is loaded
            return;
        }

        // Continue normal disabling
        logger.info("QuestPlugin is disabled!");
    }

    public Injector getInjector()
    {
        return this.injector;
    }

    public Storage getStorage()
    {
        return this.storage;
    }

    public ConfigHandler getConfigHandler()
    {
        return this.configHandler;
    }
}
