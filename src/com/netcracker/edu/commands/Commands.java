package com.netcracker.edu.commands;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Zhassulan on 23.10.2015.
 */
public final class Commands implements Serializable {
    private static HashMap<String,String> map;
    private Commands() {
    }
    public static HashMap<String,String> getAllCommands(){
        if(map==null){
            map=new HashMap<>();
            map.put("hello", "com.netcracker.edu.commands.HelloWorldCommand");
            map.put("addpassenger","com.netcracker.edu.commands.AddPassengerCommand");
            map.put("exit","com.netcracker.edu.commands.ExitCommand");
        }
        return map;
    }
}
