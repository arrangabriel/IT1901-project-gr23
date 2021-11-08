package restserver;

import core.EntryManager;
import localpersistence.EntrySaverJson;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GetfitService {

    private final EntryManager entryManager;

    public GetfitService() {
        this.entryManager = new EntryManager();
        load();
    }

    public void load() {
        try {
            EntrySaverJson.load(this.entryManager);
        } catch (IllegalArgumentException ia) {
        } catch (IOException io) {
        }
    }

    public void save() {
        try {
            EntrySaverJson.save(this.entryManager);
        } catch (IllegalArgumentException ia) {
        } catch (IOException io) {
        }
    }

    public EntryManager getEntryManager() {
        return this.entryManager;
    }
}
