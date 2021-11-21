package restserver;

import localpersistence.EntrySaverJson;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = GetFitController.class)
@AutoConfigureMockMvc
public class GetFitApplicationTest {

    public static final String mockEntryAsString = """
            {"date":"2021-10-25","duration":"3600","distance":"3.0","exerciseSubCategory":"PULL","comment":"Example comment",%s"feeling":"7","title":"Example title","exerciseCategory":"STRENGTH","maxHeartRate":"150"}
            """.strip();
    private final static String path = "/api/v1/entries";
    private final static String date = LocalDate.now().minusYears(-1)
            + "-" + LocalDate.now();
    @Autowired
    private MockMvc mMvc;
    @Autowired
    private GetFitController controller;

    @AfterAll
    public static void teardown() {
        File f = new File(EntrySaverJson.SYSTEM_SAVE_LOCATION);
        f.delete();
    }

    @BeforeEach
    public void deleteFile() {
        File f = new File(EntrySaverJson.SYSTEM_SAVE_LOCATION);
        f.delete();
    }

    @Test
    public void testAppMainMethod() {
        GetFitApplication.main();
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
            this.mMvc.perform(
                            MockMvcRequestBuilders.post("/api/v1/entries/add")
                                    .content(String.format(mockEntryAsString, "")))
                    .andDo(print()).andExpect(status().isOk()).andReturn();
        } catch (Exception e) {
            fail();
        }
    }

    private String addEntryHelper() {
        try {
            MvcResult result = this.mMvc.perform(
                            MockMvcRequestBuilders.post("/api/v1/entries/add")
                                    .content(String.format(mockEntryAsString, "")))
                    .andReturn();
            return result.getResponse().getContentAsString().substring(7, 8);
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
            MvcResult result = this.mMvc.perform(get(path)).andDo(print())
                    .andExpect(status().isOk()).andReturn();

            Assertions.assertEquals(String.format(mockEntryAsString,
                            String.format("\"id\":\"%s\",", id)),
                    result.getResponse().getContentAsString());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        try {
            this.mMvc.perform(get(path + "20")).andDo(print())
                            .andExpect(status().is(404)).andReturn();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }


    }

    @Test
    public void testGetLogEntryList() {
        try {
            this.mMvc.perform(get("/api/v1/entries/list")).andDo(print())
                    .andExpect(status().isOk()).andReturn();
            this.mMvc.perform(get("/api/v1/entries/list?c=strength"))
                    .andDo(print()).andExpect(status().isOk()).andReturn();
            this.mMvc.perform(get("/api/v1/entries/list?c=swimming"))
                    .andDo(print()).andExpect(status().isOk()).andReturn();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    //These tests need improvements. More coverage in getfit controller.
    @Test
    public void testGetStatisticsData() {
        try {
            this.mMvc.perform(get(path + "/stats?d=" + date)).andDo(print())
                    .andExpect(status().isOk()).andReturn();
            this.mMvc.perform(get(path + "/stats?d=" + date + "&c=running"))
                    .andExpect(status().isOk()).andReturn();
            this.mMvc.perform(MockMvcRequestBuilders.post(
                            path + "/stats?d=" + date + "&c=running")
                    .content(String.format(mockEntryAsString, ""))).andReturn();

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    //Covered by the one over.
    @Test
    public void testChartData() {
        try {
            this.mMvc.perform(get(path + "/chart?d=" + date)).andDo(print())
                    .andExpect(status().isOk()).andReturn();

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testRemoveEntry() {
        String id = addEntryHelper();

        try {
            ResultActions action =
                    this.mMvc.perform(post("/api/v1/entries/remove/" + id));
            action.andDo(print()).andExpect(status().isOk())
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        try {
            this.mMvc.perform(post("/api/v1/entries/remove/" + id))
                    .andDo(print()).andExpect(status().is(404)).andReturn();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

}
