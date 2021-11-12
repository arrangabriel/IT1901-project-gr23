package restserver;

import core.EntryManager;
import localpersistence.EntrySaverJson;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Gives the server access 
 * to methods from core and localpersistence.
 */
@Service
public class GetfitService {

    private final EntryManager entryManager;

    /**
     * Create a new EntryManager object on initialization
     * and loads data from localpersistance.
     */
    public GetfitService() {
        this.entryManager = new EntryManager();
        load();
    }

    /**
     * Use EntrySaverJson from localpersistance to load
     * the content of the save file to the entryManager.
     * 
     */
    public void load() {
        try {
            EntrySaverJson.load(this.entryManager);
        } catch (IllegalArgumentException ia) {
        } catch (IOException io) {
        }
    }
    /**
     * Use EntrySaverJson from localpersistance to save
     * the state of the EntryManager to file.
     * 
     */
    public void save() {
        try {
            EntrySaverJson.save(this.entryManager);
        } catch (IllegalArgumentException ia) {
        } catch (IOException io) {
        }
    }

    /**
     * Allows other classes to access the EntryManager
     * of this GeffitService.
     * 
     * @return an the EntryManager from this GetfitService
     */
    public EntryManager getEntryManager() {
        return this.entryManager;
    }

    public String convertFromSecondsToHours(final double sec) {
        double hr = sec / 3600;
        double remainder = sec - hr * 3600;
        double min = remainder / 60;
        return String.valueOf(hr) + "h : " + String.valueOf(min) + "min";
    }
}
