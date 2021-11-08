package restserver;

import java.io.IOException;

import org.springframework.stereotype.Service;

import core.EntryManager;
import localpersistence.EntrySaverJson;

@Service
public class GetfitService {

    /**
     * an EntryManager object from core
     */
    private EntryManager entryManager;

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
    public void load(){
        try{
            EntrySaverJson.load(this.entryManager);
        }catch(IllegalArgumentException ia) {
        }catch(IOException io){
        }
    }

    /**
     * Use EntrySaverJson from localpersistance to save
     * the content of the EntryManager to file.
     * 
     */
    public void save(){
        try{
            EntrySaverJson.save(this.entryManager);
        }catch(IllegalArgumentException ia) {
        }catch(IOException io){
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


}
