public class Main {
    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(2);

        for (int i = 0; i < 5; i++) {
            int taskId = i;
            pool.execute(() -> System.out.println("Task " + taskId + " executed by " + Thread.currentThread().getName()));
        }

        pool.shutdown();
        pool.awaitTermination();
        System.out.println("All tasks executed successfully");
    }
}
