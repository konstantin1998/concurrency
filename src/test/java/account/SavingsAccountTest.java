package account;

import org.junit.Test;
import ru.mipt.account.SavingsAccount;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

public class SavingsAccountTest {
    @Test
    public void withdrawTest(){
        int balance = 100;
        SavingsAccount account = new SavingsAccount(balance);
        Runnable runnable = () -> {
            int n = 10;
            account.withdraw(n);
        };
        runThreads(runnable);

        int expectedBalance = 70;
        assertEquals(expectedBalance, getBalance(account));
    }

    private int getBalance(SavingsAccount account) {
        int balance = 0;
        try {
            Field field = account.getClass().getDeclaredField("balance");
            field.setAccessible(true);
            balance = ((AtomicInteger) field.get(account)).get();
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return balance;
    }

    @Test
    public void depositTest() {
        int balance = 100;
        SavingsAccount account = new SavingsAccount(balance);
        Runnable runnable = () -> {
            int n = 10;
            account.deposit(n);
        };
        runThreads(runnable);

        int expectedBalance = 130;
        assertEquals(expectedBalance, getBalance(account));
    }

    private void runThreads(Runnable runnable) {
        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        Thread thread3 = new Thread(runnable);

        thread1.start();
        thread2.start();
        thread3.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void withdrawAndDepositTest() {
        int balance = 100;
        SavingsAccount account = new SavingsAccount(balance);
        Runnable withdraw = () -> {
            int n = 10;
            account.withdraw(n);
        };
        Runnable deposit = () -> {
            int n = 10;
            account.deposit(n);
        };
        Thread withdrawThread1 = new Thread(withdraw);
        Thread withdrawThread2 = new Thread(withdraw);
        Thread depositThread1 = new Thread(deposit);
        Thread depositThread2 = new Thread(deposit);

        withdrawThread1.start();
        withdrawThread2.start();
        depositThread1.start();
        depositThread2.start();

        try {
            withdrawThread1.join();
            withdrawThread2.join();
            depositThread1.join();
            depositThread2.join();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(balance, getBalance(account));
    }
}
