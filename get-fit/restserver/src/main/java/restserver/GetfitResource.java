package restserver;

import java.io.IOException;

import org.springframework.stereotype.Service;

import core.EntryManager;
import localpersistence.EntrySaverJson;

@Service
public class GetfitResource {

    private EntryManager entryManager;

    public GetfitResource() {
        entryManager = new EntryManager();
        load(entryManager);
    }

    private void load(EntryManager entrymanager){
        try{
            EntrySaverJson.load(entryManager);
        }catch(IllegalArgumentException ia) {
        }catch(IOException io){
        }
    }


}
