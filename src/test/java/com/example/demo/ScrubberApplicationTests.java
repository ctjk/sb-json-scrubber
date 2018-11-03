package com.example.demo;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(ScrubberController.class)
@TestPropertySource(properties = "logging.level.org.springframework.web=DEBUG")
public class ScrubberApplicationTests {

	@Autowired
    private MockMvc mockMvc;

    @Test
    public void rendersInitialForm() throws Exception {
        mockMvc.perform(get("/scrubber"))
                .andExpect(content().string(containsString("JSON Scrubber")));
    }

    @Test
    public void testValidJSON() throws Exception {
        mockMvc.perform(post("/scrubber").param("content", "{name: \"Curtis\", city: \"Edmonton\"}"))
                .andExpect(content().string(containsString("Scrubbed JSON")))
                .andExpect(content().string(not(containsString("Curtis"))))
                .andExpect(content().string(not(containsString("Edmonton"))))
                .andExpect(content().string(containsString("*")));
    }

    @Test
    public void testInvalidJSON() throws Exception {
    	// Omit closing brace on JSON text to force an error 
        mockMvc.perform(post("/scrubber").param("content", "{name: \"Curtis\", city: \"Edmonton\""))
                .andExpect(content().string(containsString("Error")))
                .andExpect(content().string(containsString("Submitted text was not valid JSON")));
    }

    @Test
    public void testEmptyForm() throws Exception {
    	// Tests the edge case where no JSON text was provided
        mockMvc.perform(post("/scrubber").param("content", ""))
                .andExpect(content().string(containsString("Scrubbed JSON")));
    }

}
