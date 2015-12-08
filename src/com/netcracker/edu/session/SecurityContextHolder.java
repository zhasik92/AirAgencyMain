package com.netcracker.edu.session;

import com.netcracker.edu.bobjects.User;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Zhassulan on 24.11.2015.
 */
public class SecurityContextHolder {
    private static final ThreadLocal<User> threadLocalScope = new ThreadLocal<>();
    private static final Set<User> activeUsers = new HashSet<>();

    public final static User getLoggedHolder() {
        return threadLocalScope.get();
    }

    public synchronized static void setLoggedUser(User user) throws IOException {
        if (user != null) {
            if (activeUsers.contains(user)) {
                throw new AccessDeniedException("User already signed in");
            }
            activeUsers.add(user);
        }
        threadLocalScope.set(user);
    }

    public synchronized final static void removeUserFromSignedUsers() {
        activeUsers.remove(threadLocalScope.get());
    }
}
