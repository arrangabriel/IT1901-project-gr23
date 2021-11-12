package restserver;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import localpersistence.EntrySaverJson;
import net.minidev.json.JSONObject;
import restserver.GetfitController;
import restserver.GetfitApplication;


@SpringBootTest(classes = GetfitController.class)
@AutoConfigureMockMvc
public class GetfitApplicationTest {

    public static final String mockEntryAsString = """
{"date":"2021-10-25","duration":"3600","distance":"3.0","exerciseSubCategory":"PULL","comment":"Example comment",%s"feeling":"7","title":"Example title","exerciseCategory":"STRENGTH","maxHeartRate":"150"}
""".strip();

    @Autowired
    private MockMvc mMvc;

    @Autowired
    private GetfitController controller;

    private final static String path = "/api/v1/entries";

    @BeforeEach
    public void deleteFile() {
        File f = new File("SavedData.json");
        f.delete();
    }

    @Test
    public void testAppMainMethod() {
        GetfitApplication.main(new String[] {});
    }

    @Test
    public void testContextLoads() {
        assertNotNull(controller);
    }

    @Test
    public void testGetFilters() {
        try {
            MvcResult result = this.mMvc.perform(get("/api/v1/entries/filters")
            // .accept(MediaType.APPLICATION_JSON) Unable to import MimeTypes for some
            // reason
            ).andDo(print()).andExpect(status().isOk()).andReturn();

            Assertions.assertEquals(result.getResponse().getContentAsString(),
                    "{\"categories\":{\"running\":[\"short\",\"long\",\"highintensity\",\"lowintensity\"],\"swimming\":[\"short\",\"long\",\"highintensity\",\"lowintensity\"],\"strength\":[\"push\",\"pull\",\"legs\",\"fullbody\"],\"cycling\":[\"short\",\"long\",\"highintensity\",\"lowintensity\"]}}");
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testAddEntry() {
        try {
            MvcResult result = this.mMvc.perform(MockMvcRequestBuilders.post("/api/v1/entries/add").content(String.format(mockEntryAsString, ""))).andDo(print()).andExpect(status().isOk()).andReturn();
        } catch (Exception e) {
            fail();
        }
    }

    private String addEntryHelper() {
        try {
            MvcResult result = this.mMvc.perform(MockMvcRequestBuilders.post("/api/v1/entries/add").content(String.format(mockEntryAsString, ""))).andReturn();
            return result.getResponse().getContentAsString().substring(7,8);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    @Test
    public void testGetLogEntry() {
        String id = addEntryHelper();
        String path = "/api/v1/entries/" + id;
        try {
            MvcResult result = this.mMvc.perform(get(path)).andDo(print()).andExpect(status().isOk()).andReturn();

            Assertions.assertEquals(String.format(mockEntryAsString, String.format("\"id\":\"%s\",", id)), result.getResponse().getContentAsString());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        try {
            MvcResult result = this.mMvc.perform(get(path+"20")).andDo(print()).andExpect(status().is(404)).andReturn();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }


    }

    @Test
    public void testGetLogEntryList() {
        try {
            this.mMvc.perform(get("/api/v1/entries/list")).andDo(print()).andExpect(status().isOk()).andReturn();
            this.mMvc.perform(get("/api/v1/entries/list?c=strength")).andDo(print()).andExpect(status().isOk()).andReturn();
            this.mMvc.perform(get("/api/v1/entries/list?c=swimming")).andDo(print()).andExpect(status().isOk()).andReturn();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

    }

    @Test
    public void testRemoveEntry() {
        String id = addEntryHelper();

        try {
            ResultActions action =this.mMvc.perform(post("/api/v1/entries/remove/"+id));
            MvcResult result = action.andDo(print()).andExpect(status().isOk()).andReturn();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        try {
            this.mMvc.perform(post("/api/v1/entries/remove/"+id)).andDo(print()).andExpect(status().is(404)).andReturn();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

}
