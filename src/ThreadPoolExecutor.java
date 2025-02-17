import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ThreadPoolExecutor {

    private final LinkedList<Runnable> taskQueue = new LinkedList<>();
    private final List<Worker> workers = new ArrayList<>();
    private volatile boolean isShutdown = false;

    public ThreadPoolExecutor(int poolSize) {
        if (poolSize < 1) {
            throw new IllegalArgumentException("pool size must be greater than 0");
        }

        for (int i = 0; i < poolSize; i++) {
            Worker worker = new Worker(this);
            workers.add(worker);
            worker.start();
        }

    }

    public void execute(Runnable task) {
        synchronized (taskQueue) {
            if (isShutdown) {
                throw new IllegalStateException("ThreadPoolExecutor is shutdown");
            }
            taskQueue.addLast(task);
            taskQueue.notify();
        }
    }

    public void shutdown() {
        synchronized (taskQueue) {
            isShutdown = true;
            taskQueue.notifyAll();
        }
    }

    public void awaitTermination() throws InterruptedException {
        for (Worker worker : workers) {
            worker.join();
        }
    }

    public boolean isShutdown() {
        return isShutdown;
    }

    public LinkedList<Runnable> getTaskQueue() {
        return taskQueue;
    }
}
