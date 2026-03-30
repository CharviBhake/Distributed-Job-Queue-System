package com.example.demo.Controller;

import com.example.demo.Entity.Job;
import com.example.demo.Service.JobQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/jobs")
public class JobController {
    @Autowired
    private JobQueueService jobQueueService;

    @PostMapping
    public String createJob(@RequestBody Job job){
        job.setId(UUID.randomUUID().toString());
        jobQueueService.addJob(job);
        return "Job added tp queue with id"+job.getId();
    }
    @GetMapping("/test")
    public String test() {
        return "API working";
    }
}
