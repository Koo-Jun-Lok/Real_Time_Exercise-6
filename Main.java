package Week_08;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

// Corrected Class from the PDF
class BankAccountWithLock {
    private double balance;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public BankAccountWithLock(double initialBalance) {
        this.balance = initialBalance;
    }

    // Read balance (shared lock)
    public double getBalance() {
        readLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " reads balance: " + balance);
            return balance;
        } finally {
            readLock.unlock();
        }
    }

    // Deposit money (exclusive lock)
    public void deposit(double amount) {
        writeLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " deposits: " + amount);
            balance += amount; // Corrected from "balance + amount"
        } finally {
            writeLock.unlock();
        }
    }

    // Withdraw money (exclusive lock)
    public void withdraw(double amount) {
        writeLock.lock();
        try {
            if (balance >= amount) { // Corrected logic to allow withdrawing exact balance
                System.out.println(Thread.currentThread().getName() + " withdraws: " + amount);
                balance -= amount; // Corrected from "balance amount"
            } else {
                System.out.println(Thread.currentThread().getName() + " insufficient funds for: " + amount);
            }
        } finally {
            writeLock.unlock();
        }
    }
}


public class Main {
    public static void main(String[] args) {
        // Initialize account with 1000
        BankAccountWithLock account = new BankAccountWithLock(1000);

        // Create threads for  (readers)
        Thread reader1 = new Thread(() -> account.getBalance(), "Reader-1");
        Thread reader2 = new Thread(() -> account.getBalance(), "Reader-2");

        // Create threads for  (writers)
        Thread writer1 = new Thread(() -> account.deposit(500), "Writer-Deposit");
        Thread writer2 = new Thread(() -> account.withdraw(200), "Writer-Withdraw");
        Thread writer3 = new Thread(() -> account.withdraw(2000), "Writer-Overdraw");

        // Start threads
        reader1.start();
        writer1.start();
        reader2.start();
        writer2.start();
        writer3.start();
    }
}