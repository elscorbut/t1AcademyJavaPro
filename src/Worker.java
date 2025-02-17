import java.util.LinkedList;

public class Worker extends Thread {

    private final LinkedList<Runnable> taskQueue;
    private final ThreadPoolExecutor threadPool;

    public Worker(ThreadPoolExecutor threadPool) {
        this.taskQueue = threadPool.getTaskQueue();
        this.threadPool = threadPool;
    }

    public void run() {
        Runnable task;
        while (true) {
            synchronized (taskQueue) {
                while (taskQueue.isEmpty() && !threadPool.isShutdown()) {
                    try {
                        taskQueue.wait();
                    } catch (InterruptedException ignored) {
                    }
                }

                if (taskQueue.isEmpty() && threadPool.isShutdown()) {
                    return;
                }

                task = taskQueue.poll();
            }

            try {
                if (task != null) {
                    task.run();
                }
            } catch (RuntimeException e) {
                System.out.println("Task failed: " + e.getMessage());
            }
        }
    }
}
