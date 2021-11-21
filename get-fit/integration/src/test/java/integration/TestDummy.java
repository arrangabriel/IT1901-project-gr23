package integration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class TestDummy {

    @Test
    public void testDummy() {
        Dummy dummy = new Dummy();
        assertNotNull(dummy);
    }
    
}
