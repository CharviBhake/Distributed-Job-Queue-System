package com.example.demo.Worker;


import com.example.demo.Entity.Job;
import com.example.demo.Service.JobQueueService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class JobWorker {
    @Autowired
    private JobQueueService jobQueueService;
    private static final int MAX_RETRIES = 3;
    private static final int WORKER_THREADS=4;
    private final ExecutorService executorService= Executors.newFixedThreadPool(WORKER_THREADS);
    @PostConstruct
    public void startWorker() {
        for (int i = 0; i < WORKER_THREADS; i++) {
            executorService.submit(() -> {
                while (true) {
                    try {
                        Job job = jobQueueService.getJob();
                        if (job == null) continue;
                        if (jobQueueService.isProcessed(job.getId())) continue;
                        processJob(job);
                        jobQueueService.markProcessed(job.getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    private void processJob(Job job) {
        try{
            System.out.println(Thread.currentThread().getName()+" processing job "+job.getId());
            if(Math.random()<0.5){
                throw new RuntimeException("Random failure");
            }
            System.out.println("Job completed: "+job.getId());
        }catch (Exception e){
            int retries=job.getRetryCount();
            if(retries<MAX_RETRIES){
                job.setRetryCount(retries+1);
                long delay;
                switch(job.getRetryCount()){
                    case 1->delay=5;
                    case 2->delay=30;
                    case 3->delay=120;
                    default->delay=300;
                }
                System.out.println("Retrying job"+job.getId()+"after"+delay+"seconds");
                jobQueueService.scheduleRetry(job,delay);
            }else{
                System.out.println("Sending job to DLQ"+job.getId());
                jobQueueService.sendToDLQ(job);
            }
        }

       // System.out.println("Job completed: " + job.getId());
    }
}
