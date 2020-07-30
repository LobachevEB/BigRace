import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class MainClass {
    public static final int CARS_COUNT = 4;
    public static final int TUNNEL_TRAFFIC = 2;
    private static CyclicBarrier control = new CyclicBarrier(CARS_COUNT);
    private static Semaphore tunnelControl = new Semaphore(TUNNEL_TRAFFIC);
    private static Semaphore startControl = new Semaphore (1);
    private static CountDownLatch finishControl = new CountDownLatch(CARS_COUNT);
    public static void main(String[] args) throws InterruptedException {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        startControl.acquire();
        Race race = new Race(new Road(60,false,finishControl), new Tunnel(tunnelControl), new Road(40,true,finishControl));
        Road r = ((Road)race.getStages().get(race.getStages().size()-1));

        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10),control,startControl);
        }
        for (int i = 0; i < cars.length; i++) {
            new Thread(cars[i]).start();
        }

        try {
            startControl.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
        CountDownLatch raceControl = Car.getRaceControl();
        raceControl.countDown();
        finishControl.await();
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}

