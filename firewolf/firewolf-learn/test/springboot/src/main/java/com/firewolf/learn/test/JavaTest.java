package com.firewolf.learn.test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 描述：
 * Author：liuxing
 * Date：2020-07-24
 */
public class JavaTest {

    public static void main(String[] args) throws Exception{
        UserService userService = new UserService();
        User zhangsan = userService.findByName("zhangsan");
        System.out.println(zhangsan);
        deathLock();
    }

    public static void deathLock() {
        Lock lock1 = new ReentrantLock();
        Lock lock2 = new ReentrantLock();
        Thread t1 = new Thread(() -> {
            try {
                lock1.lock();
                TimeUnit.SECONDS.sleep(1);
                lock2.lock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                lock2.lock();
                TimeUnit.SECONDS.sleep(1);
                lock1.lock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.setName("mythread1");
        t2.setName("mythread2");
        t1.start();
        t2.start();
    }
}
