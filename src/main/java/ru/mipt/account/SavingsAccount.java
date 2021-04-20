package ru.mipt.account;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SavingsAccount {
    AtomicInteger balance;
    //AtomicInteger numberOfPreferredWithdrawals;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition moneyCondition = lock.newCondition();

    public SavingsAccount(int balance) {
        this.balance = new AtomicInteger(balance);
        //numberOfPreferredWithdrawals = new AtomicInteger(0);
    }

    public void deposit(int k) {
        lock.lock();
        try {
            balance.getAndAdd(k);
            moneyCondition.signalAll();
        } finally {
            lock.unlock();
        }

    }

    public void withdraw(int k) {
        lock.lock();
        try {
            awaitForMoney(k);
            balance.getAndAdd(-1 * k);
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }



    private void awaitForMoney(int k) throws InterruptedException {
        while(balance.get() < k) {
            moneyCondition.await();
        }
    }
}
