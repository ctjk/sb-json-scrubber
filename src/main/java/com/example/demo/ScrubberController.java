package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.google.gson.Gson;

@Controller
public class ScrubberController {

    @GetMapping("/scrubber")
    public String greetingForm(Model model) {
        model.addAttribute("formInput", new FormInput());
        return "form";
    }

    @PostMapping("/scrubber")
    public String greetingSubmit(@ModelAttribute FormInput formInput) {
    	if (isJSONValid(formInput.getContent())) {
    		formInput.setContent(scrubJson(formInput.getContent()));
    		return "result";
    	} else {
    		return "error";
    	}
    }
    
    private boolean isJSONValid(String jsonInString) {
    	Gson gson = new Gson();
    	try {
            gson.fromJson(jsonInString, Object.class);
            return true;
        } catch(com.google.gson.JsonSyntaxException ex) { 
            return false;
        }
    }
    
    private String scrubJson(String json) {
		// I chose to scrub all alphanumerics in the JSON for now; an interesting tweak for a snowy day would be to scrub only the property values.
    	return json.replaceAll("[\\pL\\pN\\p{Pc}]", "*");
    }
}
