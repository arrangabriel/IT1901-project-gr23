package restapi;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import localpersistence.EntrySaverJson;

@SpringBootTest(classes = EntryManagerController.class)
@AutoConfigureMockMvc
public class GetfitApplicationTest {

    @Autowired
    private MockMvc mMvc;


    @Autowired
    private EntryManagerController controller;

    private final static String path = "/api/v1/entries";

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
            this.mMvc.perform(get("/api/v1/entries/filters"))
        .andDo(print()).andExpect(status()
        .isOk());

        } catch (Exception e) {
            fail();
        }
        

        //Integration testing.
    }

    /*@Test
    public void testGetLogEntry() throws Exception {
        try {  
        this.mMvc.perform(get("/api/v1/entries"))
        .andDo(print()).andExpect(status()
        .isOk());

        } catch (Exception e) {
            fail();
        }*/

    

        
    }

    

    
}
