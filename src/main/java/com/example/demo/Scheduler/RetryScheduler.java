package com.example.demo.Scheduler;

import com.example.demo.Entity.Job;
import com.example.demo.Service.JobQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RetryScheduler {
    @Autowired
    private  RedisTemplate<String, Job> redisTemplate;
    @Autowired
    private JobQueueService jobQueueService;
    private static final String RETRY_QUEUE="retry_queue";

    @Scheduled(fixedRate=5000)
    public void moveReadyRetries(){
        long now=System.currentTimeMillis();
        Set<Job> jobs=redisTemplate.opsForZSet().rangeByScore(RETRY_QUEUE,0,now);
        if(jobs==null) return;
        for(Job job:jobs){
            jobQueueService.removeRetry(job);
            jobQueueService.addJob(job);
            System.out.println("Retrying jobs"+job.getId());
        }
    }
}
