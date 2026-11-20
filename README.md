# Thread Pool Capacity & Worker Allocation

Here is the real math when `corePoolSize = 3`, `queueCapacity = 10`, and `maxPoolSize = 5`:

* **Core workers:** 3

* **Extra emergency workers:** $5 - 3 = 2$ (only 2 extra workers!)

* **Total maximum workers:** 5 workers (never 8)

## When Are All 5 Workers Full?

Using the same numbers (`core = 3`, `queue = 10`, `max = 5`), here is when all 5 workers become busy:

1. **Tasks 1, 2, 3 arrive** $\rightarrow$ Taken by the 3 core workers.

2. **Tasks 4 to 13 arrive (10 tasks)** $\rightarrow$ Put into the waiting queue (all 10 seats are now full).

3. **Task 14 arrives** $\rightarrow$ Java creates Worker #4 (1st extra worker).

4. **Task 15 arrives** $\rightarrow$ Java creates Worker #5 (2nd extra worker).

### At 15 tasks at the exact same time:

* **5 workers are working** (`worker-1` to `worker-5` — this is `maxPoolSize = 5`).

* **10 tasks are waiting in the queue** (`queueCapacity = 10`).

* **Total capacity = 15 tasks** (5 workers + 10 queue).

> **Note:** If **Task 16** arrives before anyone finishes, the system is 100% full and rejects Task 16 with an error!

## `CompletableFuture` & Thread Usage

`CompletableFuture.supplyAsync(...)` uses one thread from your pool to run that single task:

* **1 `supplyAsync`** = **1 task** = **1 worker thread** used.

* **3 `supplyAsync` calls** (like `userFuture`, `ordersFuture`, and `pointsFuture` in your code) = **3 tasks** = **3 worker threads** working at the same time.

### 