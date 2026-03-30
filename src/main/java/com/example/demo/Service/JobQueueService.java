package com.example.demo.Service;

import com.example.demo.Entity.Job;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class JobQueueService {
    private final RedisTemplate<String,Job> redisTemplate;
    private static final String QUEUE_NAME="job_queue";
    private static final String RETRY_QUEUE="retry_queue";
    private static final String DLQ="dead_letter_queue";
    private static final String PROCESSED="processed_jobs";


    public JobQueueService(RedisTemplate<String,Job> redisTemplate){
        this.redisTemplate=redisTemplate;
    }
    @Autowired
    public StringRedisTemplate stringRedisTemplate;
    public void addJob(Job job){

        redisTemplate.opsForList().leftPush(QUEUE_NAME,job);
    }
    public Job getJob(){
            return redisTemplate.opsForList().rightPop(QUEUE_NAME,10, TimeUnit.SECONDS);
    }
    public Job getRetryJob(){
        return redisTemplate.opsForList().rightPop(RETRY_QUEUE,5,TimeUnit.SECONDS);
    }
    public void sendToRetry(Job job){
        redisTemplate.opsForList().leftPush(RETRY_QUEUE,job);
    }
    public void sendToDLQ(Job job){
        redisTemplate.opsForList().leftPush(DLQ,job);
    }
    public void scheduleRetry(Job job,long delaySeconds){
        long retryTime=System.currentTimeMillis()+delaySeconds*1000;
        redisTemplate.opsForZSet().add(RETRY_QUEUE,job,retryTime);
    }
    public Set<Job> getReadyRetries(long now){
          return redisTemplate.opsForZSet().rangeByScore(RETRY_QUEUE,0,now);
    }
    public void removeRetry(Job job){
        redisTemplate.opsForZSet().remove(RETRY_QUEUE,job);
    }
    public boolean isProcessed(String jobId){
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(PROCESSED,jobId));
    }
    public void markProcessed(String jobId){
      //  redisTemplate.opsForSet().add(String.join(PROCSSED,jobId));
        if(jobId!=null && !jobId.isEmpty()){
            System.out.println("Marking processed job: " + jobId);
            stringRedisTemplate.opsForSet().add(PROCESSED,jobId);
        }
    }
    public Long getQueueSize(){
        return redisTemplate.opsForList().size(QUEUE_NAME);
    }
    public Long getDLQSize(){
        return redisTemplate.opsForList().size(DLQ);
    }
    public Long getRetrySize(){
        return redisTemplate.opsForZSet().size(RETRY_QUEUE);
    }
    public List<Job> getDLQJobs(){
        return redisTemplate.opsForList().range(DLQ,0,-1);
    }

}
