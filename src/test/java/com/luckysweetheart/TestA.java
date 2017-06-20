package com.luckysweetheart;

/**
 * Created by yangxin on 2017/6/20.
 */
public class TestA {

    public static void main(String[] args) {
        MyMoney myMoney = new MyMoney(1000);
        Thread t1 = new Thread(myMoney);
        Thread t2 = new Thread(myMoney);
        Thread t3 = new Thread(myMoney);
        Thread t4 = new Thread(myMoney);

        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }

}

class MyMoney implements Runnable {

    private int money = 3000;

    private int qu = 0;
    public MyMoney(int qu) {
        this.qu = qu;
    }

    @Override
    public void run() {
        try {
            getMoney();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized int getMoney() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + "准备取" + qu + "元");
        if (qu > this.money) {
            System.out.println("你没这么多钱了！");
            return this.money - qu;
        }
        Thread.sleep(500);
        this.money = this.money - qu;
        System.out.println(Thread.currentThread().getName() + "取了" + qu + "元，你还剩" + this.money + "元");
        return this.money;
    }
}