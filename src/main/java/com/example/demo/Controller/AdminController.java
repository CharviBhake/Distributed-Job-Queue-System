package com.example.demo.Controller;

import com.example.demo.Entity.Job;
import com.example.demo.Service.JobQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private JobQueueService jobQueueService;
    @Autowired
    private RedisTemplate<String, Job> redisTemplate;
    @GetMapping("/queue-size")
    public Long queueSize(){
        return jobQueueService.getQueueSize();
    }
    @GetMapping("/retry-size")
    public Long retrySize(){
        return jobQueueService.getRetrySize();
    }
    @GetMapping("dlq-size")
    public Long dlqSize(){
        return jobQueueService.getDLQSize();
    }
    @GetMapping("/dlq")
    public List<Job> getDLQJobs(){
        return jobQueueService.getDLQJobs();
    }
    @GetMapping("/stats")
    public Map<String,Long> stats() {
        Map<String,Long> map = new HashMap<>();
        map.put("queueSize", jobQueueService.getQueueSize());
        map.put("retrySize", jobQueueService.getRetrySize());
        map.put("dlqSize", jobQueueService.getDLQSize());
        return map;
    }
}
