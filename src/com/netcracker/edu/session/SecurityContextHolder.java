package com.netcracker.edu.session;

import com.netcracker.edu.bobjects.User;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Zhassulan on 24.11.2015.
 */
public class SecurityContextHolder {
    private static final ThreadLocal<User> threadLocalScope = new ThreadLocal<>();
    private static final Set<User> activeUsers = new HashSet<>();
    private static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private static Lock readLock = rwl.readLock();
    private static Lock writeLock = rwl.writeLock();

    public static User getLoggedHolder() {
        return threadLocalScope.get();
    }

    public  static void setLoggedUser(User user) throws IOException {
        if (user != null) {
            readLock.lock();
            try {
                if (activeUsers.contains(user)) {
                    throw new AccessDeniedException("User already signed in");
                }
            } finally {
                readLock.unlock();
            }
            writeLock.lock();
            try {
                activeUsers.add(user);
            } finally {
                writeLock.unlock();
            }
        }
        threadLocalScope.set(user);
    }

    public /*synchronized*/  static void removeUserFromSignedUsers() {
        activeUsers.remove(threadLocalScope.get());
    }
}
