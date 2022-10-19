package com.abp.botrunningsystem.service.impl.utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BotPool extends Thread {
    private final static ReentrantLock lock = new ReentrantLock();

    private final Condition condition = lock.newCondition();

    private Queue<Bot> bots = new LinkedList<>();

    // 生产者线程往 bots 中增加任务
    public void addBot(Integer userId, Integer oppoId, String botCode, String input) {
        lock.lock();
        try {
            bots.add(new Bot(userId, oppoId, botCode, input));
            condition.signalAll();              // 唤醒所有线程
        } finally {
            lock.unlock();
        }
    }

    // 目前实现使用 joor 只支持执行 java 代码，未来可以修改成 Docker 沙箱执行其他语言代码
    private void consume(Bot bot) {
        Consumer consumer = new Consumer();
        consumer.startTimeout(2000, bot);       // 一共 5 秒时间，一个 bot 2秒，1秒冗余
    }

    // 消费者进程消费任务
    @Override
    public void run() {
        while (true) {
            lock.lock();
            if (bots.isEmpty()) {       // 如果队列为空则阻塞
                try {
                    // 阻塞当前线程，直到线程被唤醒或者被中断
                    // await 中包含释放 lock 的操作
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    lock.unlock();
                    break;
                }
            } else {
                Bot bot = bots.remove();    // 返回并删除队列的队头
                lock.unlock();
                consume(bot);   // 函数比较耗时，需要几秒钟，放在解锁语句后
            }
        }
    }
}
