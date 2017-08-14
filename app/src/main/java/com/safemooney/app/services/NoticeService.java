package com.safemooney.app.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.safemooney.http.TransactionClient;
import com.safemooney.http.models.Transaction;
import com.safemooney.http.models.TransactionPreview;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class NoticeService
{
    private static final Queue<TransactionPreview> transactionQueue = new ArrayDeque<>();
    private static final boolean[] isActive = new boolean[1];
    private static int userId;
    private static String tokenkey;
    private static Thread thread;

    public static void putUserData(int id, String token)
    {
        userId = id;
        tokenkey = token;
    }

    static
    {
        isActive[0] = false;
    }


    public static void runProcess()
    {
        isActive[0] = true;
        thread = new Thread(new Runner());
        thread.start();
    }

    public static Queue<TransactionPreview> getQueue()
    {
        isActive[0] = false;
        try
        {
            thread.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            return null;
        }

        Queue<TransactionPreview> transactions = new ArrayDeque<>(transactionQueue);
        runProcess();
        return transactions;
    }


    private static class Runner implements Runnable
    {
        private int sleepTime = 60000;
        @Override
        public void run()
        {
            while (isActive[0])
            {
                try
                {
                    Thread.sleep(sleepTime);
                    TransactionClient transactionClient = new TransactionClient(userId, tokenkey);
                    List<TransactionPreview> queue = transactionClient.checkQueue();
                    if(queue == null)
                        continue;

                    transactionQueue.addAll(queue);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }
}
