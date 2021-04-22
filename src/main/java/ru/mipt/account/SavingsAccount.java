package ru.mipt.account;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SavingsAccount {
    AtomicInteger balance;
    AtomicInteger numberOfPreferredWithdrawals;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition moneyCondition = lock.newCondition();
    private final Condition preferredCondition = lock.newCondition();

    public SavingsAccount(int balance) {
        this.balance = new AtomicInteger(balance);
        numberOfPreferredWithdrawals = new AtomicInteger(0);
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
            waitForPreferred();
            awaitForMoney(k);
            balance.getAndAdd(-1 * k);
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public void withdrawPreferred(int k) {
        numberOfPreferredWithdrawals.getAndIncrement();
        lock.lock();
        try {
            awaitForMoney(k);
            balance.getAndAdd(-1 * k);
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
            numberOfPreferredWithdrawals.getAndDecrement();
        }
    }

    private void awaitForMoney(int k) throws InterruptedException {
        while(balance.get() < k) {
            moneyCondition.await();
        }
    }

    private void waitForPreferred() throws InterruptedException {
        while(numberOfPreferredWithdrawals.get() != 0) {
            preferredCondition.await();
        }
    }

    /*Ответ на вопрос пункта 3: может быть ситуация, когда какие-то потоки будут не завершат свою работу. Допустим,
    *что n > 20 и из них 15 потоков будут пытаться списать деньги с одного итого же аккаунта, на котором изначально 0$,
    * тогда сначала все эти потоки будут ждать до 2.00 пока на этот аккаунт не положат деньги, после этого 10 потоков
    * смогут завершить свою работу, а остальные пять будут ждать бесконечно, пока снова кто-нибудь не положит деньги
    * на счет. */
    void transfer(int k, SavingsAccount reserve) {
        lock.lock();
        try {
            reserve.withdraw(k);
            deposit(k);
        } finally {
            lock.unlock();
        }
    }
}
