package com.example.philosophers_demo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * 创建时间：2024/7/28 13:57
 * 作者：Jun bu jian
 */
@Data
@Slf4j
public class PhilosopherMeal extends Thread{
    /**
     * 哲学家前缀
     */
    private static String NAME_PREFIX = "哲学家";
    /**
     * 哲学家编号
     */
    private int philosopherNum;

    /**
     * 模拟筷子资源信号量
     */
    private volatile List<Semaphore> semaphores;

    public PhilosopherMeal(int philosopherNum, List<Semaphore> semaphores) {
        super(PhilosopherMeal.NAME_PREFIX+String.valueOf(philosopherNum));
        this.philosopherNum = philosopherNum;
        this.semaphores = semaphores;
    }

    @Override
    public void run() {
        try {
            //哲学家先拿起左手的筷子
            semaphores.get(this.philosopherNum).acquire();
            System.out.println(this.getName()+"拿到了筷子： " + String.valueOf(this.philosopherNum) +
                    " 正在试图拿筷子： " + String.valueOf(this.getRight(this.philosopherNum)));
//            sleep(1000);
            //哲学家拿起右手的筷子
            semaphores.get(this.getRight(this.philosopherNum)).acquire();
            // 哲学家进餐
            System.out.println(this.getName()+"进餐中...使用筷子： " + String.valueOf(this.philosopherNum) +
                    " 和使用筷子： " + String.valueOf(this.getRight(this.philosopherNum)));;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放筷子资源
            semaphores.get(philosopherNum).release();
            semaphores.get((philosopherNum+1)%semaphores.size()).release();
        }
    }

    public int getRight(int philosopherNum) {
        return (philosopherNum + 1 == semaphores.size()) ? 0: philosopherNum + 1;
    }

    public static void main(String[] args) throws InterruptedException {
        List<Semaphore> semaphores1 = new ArrayList<>();
        int counter = 5;

        for (int i = 0; i < counter; i++) {

            Semaphore semaphore = new Semaphore(1);
            semaphores1.add(semaphore);
        }
        for (int i = 0; i < counter; i++) {
            PhilosopherMeal philosopherMeal = new PhilosopherMeal(i, semaphores1);
            philosopherMeal.start();
        }
    }
}
