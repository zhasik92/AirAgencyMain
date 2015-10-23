package com.netcracker.edu.persist;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by Zhassulan on 23.10.2015.
 */
public class InMemoryStorageManager {
    private static InMemoryStorageManager instance;
    private InMemoryStorage storage;

    private InMemoryStorageManager() {
        storage=getStorage();
    }

    public synchronized InMemoryStorage getStorage() {
        try(FileInputStream fis = new FileInputStream("InMemoryStorage.out");
            ObjectInputStream oin = new ObjectInputStream(fis)) {
            if (storage == null) {
                storage = (InMemoryStorage) oin.readObject();
                return storage;
            }
            return storage;
        } catch (FileNotFoundException fnfe) {
            System.out.println("fnse");
           return storage=new InMemoryStorage();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException cne) {
            cne.printStackTrace();
        }
        return storage;
    }

    public static synchronized InMemoryStorageManager getInstance() {
        if (instance == null) {
            instance = new InMemoryStorageManager();
        }
        return instance;
    }
}
