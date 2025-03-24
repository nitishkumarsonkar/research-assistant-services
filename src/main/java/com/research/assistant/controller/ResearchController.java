package com.research.assistant.controller;

import com.research.assistant.dto.ResearchRequest;
import com.research.assistant.service.ResearchService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/research")
@CrossOrigin("*")
@AllArgsConstructor // Lombok annotation to create a constructor with all required fields like this: ResearchController(ResearchService researchService)
public class ResearchController {

    private final ResearchService researchService;

    //Process content
    @PostMapping("/process")
    public ResponseEntity<String> processContent(@RequestBody ResearchRequest researchRequest) {
        String result = researchService.processContent(researchRequest);
        return ResponseEntity.ok(result);
    }

//    // adding a new post call for explain
//    @PostMapping("/explain")
//    public ResponseEntity<String> explainContent(@RequestBody ResearchRequest researchRequest) {
//        String result = researchService.explainContent(researchRequest);
//        return ResponseEntity.ok(result);
//    }`
}
