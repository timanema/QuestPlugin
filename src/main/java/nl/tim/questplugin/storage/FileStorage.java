package nl.tim.questplugin.storage;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import nl.tim.questplugin.QuestPlugin;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Singleton
public class FileStorage implements Storage
{
    private QuestPlugin questPlugin;
    private File storageLocation;

    @Inject
    public FileStorage(QuestPlugin questPlugin, @Named("config") File storageLocation)
    {
        this.questPlugin = questPlugin;
        this.storageLocation = storageLocation;
    }

    @Override
    public void init()
    {
        System.out.println(questPlugin.getName());
        System.out.println(storageLocation.getName());
    }

    @Override
    public void save(UUID uuid, DataType dataType, DataPair dataPair)
    {
        // Just create a array with the single value and pass on
        this.save(uuid, dataType, new DataPair[]{dataPair});
    }

    @Override
    public void save(UUID uuid, DataType dataType, DataPair[] dataPairs)
    {
        /*
        All data will be saved in the appropriate file (indicated by DataType.getFilePath()) in the following format:
        uuid:
            dataPair.getKey():  dataPair.getData()
         */

        String uid = uuid.toString();
        String path = dataType.getFilePath().replace("/", File.separator);
        File dataFile = new File(this.storageLocation + File.separator + path);
        FileConfiguration fileConfiguration = new YamlConfiguration();

        // Check if file exists, if not make one
        ConfigHandler.checkFileExists(dataFile);

        try
        {
            // Loading file
            fileConfiguration.load(dataFile);

            // Loop through all data
            for (DataPair dataPair : dataPairs)
            {
                // Create key and data pair
                String key = dataPair.getKey();
                Object data = dataPair.getData();

                // Save in file
                fileConfiguration.set(uid + "." + key, data);
            }

            // Save file
            fileConfiguration.save(dataFile);
        } catch (InvalidConfigurationException | IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public DataPair load(UUID uuid, DataType dataType, String key)
    {
        return null;
    }

    @Override
    public DataPair[] load(UUID uuid, DataType dataType)
    {
        return new DataPair[0];
    }
}
