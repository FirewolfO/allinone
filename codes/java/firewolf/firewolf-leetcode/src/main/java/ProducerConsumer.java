import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

class Solution {



    public static void main(String[] args) throws InterruptedException {
//        Limiter limiter = new Limiter(100);
//
//        new Thread(()->{
//            while(true){
//                String token = limiter.getToken();
//                System.out.println("get token is "+ token);
//                try {
//                    Thread.sleep(50);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();


        Limiter2 limiter2 = new Limiter2(100);

        Thread.sleep(1000);

        new Thread(()->{
            while (true){
                try {
                    Thread.sleep(5);
                    limiter2.getToken();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }
}

class Limiter{

    private BlockingQueue<String> blockingQueue;

    private int hopeQps;
    public Limiter(int qps){
        this.hopeQps = qps;
        blockingQueue = new ArrayBlockingQueue<>(qps);
        new Thread(()->{
            try {
                createToken();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public String getToken(){
        try {
            String take = blockingQueue.take();
            return take;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void changeQps(int qps){
        blockingQueue = new ArrayBlockingQueue<>(qps);
    }

    private void createToken() throws InterruptedException {
        while (true){
            Thread.sleep(1000 / hopeQps);
            blockingQueue.put(UUID.randomUUID().toString());
            System.out.println("current token count is " + blockingQueue.size());
        }
    }
}


class Limiter2{
    private int hopsQps;
    private volatile int currentQps;
    private Object lock = new Object();
    public Limiter2(int hopsQps){
        this.hopsQps = hopsQps;
        new Thread(()->{
            try {
                createToken();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createToken() throws Exception{
        while (true){
            Thread.sleep(1000 / hopsQps );
            synchronized (lock){
                if(currentQps >= hopsQps){
                    lock.wait();
                }
                currentQps++;
                System.out.println("current qps = "+ currentQps);
                lock.notifyAll();
            }
        }
    }
    public void getToken() throws Exception{
        synchronized (lock){
            if(currentQps <= 0){
                lock.wait();
            }
            currentQps--;
            System.out.println("get:"+currentQps);
            lock.notifyAll();
        }
    }
}

class Limiter3{
    Semaphore semaphore;
    public Limiter3(int hopeQps){
        this.semaphore = new Semaphore(hopeQps);
    }

    public void getToken() throws InterruptedException {
        semaphore.acquire();
    }
}
