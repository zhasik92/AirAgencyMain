package com.netcracker.edu.session;

import com.netcracker.edu.bobjects.User;

/**
 * Created by Zhassulan on 24.11.2015.
 */
public class SecurityContextHolder {
    private static final ThreadLocal<User> threadLocalScope=new ThreadLocal<>();

    public final static User getLoggedHolder(){
        return threadLocalScope.get();
    }

    public final static void setLoggedUser(User user) {
        threadLocalScope.set(user);
    }
}
