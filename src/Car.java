import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Car implements Runnable {
    private static int CARS_COUNT;
    private Semaphore startControl;

    static private final CountDownLatch raceControl;
    static {
        CARS_COUNT = 0;
        raceControl = new CountDownLatch(1);
    }
    private CyclicBarrier control;
    public static CountDownLatch getRaceControl() {
        return raceControl;
    }


    private Race race;
    private int speed;
    private String name;
    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }
    public Car(Race race, int speed,CyclicBarrier cntl,Semaphore startCtl) {
        this.control = cntl;
        this.startControl = startCtl;
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }
    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            try {
                control.await();
                control.reset();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println(this.name + " готов");
            try {
                control.await();
                control.reset();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            startControl.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            raceControl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
    }
}
