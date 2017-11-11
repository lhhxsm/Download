package com.android.download.test;

/**
 * 作者:lhh
 * 描述:线程的中断
 * 时间:2017/11/10.
 */
public class ThreadPoolTest {
  public static void main(String args[]) throws InterruptedException {
    MyRunnable runnable = new MyRunnable();
    Thread thread = new Thread(runnable);
    thread.start();
    Thread.sleep(1000L);
    runnable.flag = false;
    thread.interrupt();
  }

  private static class MyRunnable implements Runnable {
    public volatile boolean flag = true;

    @Override public void run() {
      while (flag && !Thread.interrupted()) {
        try {
          System.out.println("running");
          Thread.sleep(2000L);
        } catch (InterruptedException e) {
          e.printStackTrace();
          return;
        }
      }
    }
  }
}
