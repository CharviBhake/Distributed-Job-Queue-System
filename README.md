# Distributed-Job-Queue-System
A production-style distributed job processing system built using Spring Boot and Redis, supporting asynchronous task execution, worker pools, retry with exponential backoff, dead-letter queue handling, idempotent processing, and monitoring APIs.

📌 Features
- Asynchronous job processing using Redis queues
- Concurrent worker pool using Java ExecutorService
- Retry mechanism with exponential backoff
- Dead Letter Queue (DLQ) for failed jobs
- Delayed retries using Redis Sorted Sets (ZSET)
- Idempotent job execution (no duplicate processing)
- Monitoring APIs for queue metrics
- Dockerized setup for easy deployment

⚙️ Tech Stack
Java 17
Spring Boot
Redis (Lists + Sorted Sets)
Docker & Docker Compose
Maven
 



