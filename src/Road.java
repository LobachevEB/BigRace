import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Road extends Stage {

    private  CountDownLatch finishControl;

    private boolean finalStage;

    public Road(int length,boolean finalStage,CountDownLatch finishControl) {
        this.length = length;
        this.description = "Дорога " + length + " метров";
        this.finalStage = finalStage;
        this.finishControl = finishControl;
    }
    @Override
    public void go(Car c) {
        try {
            System.out.println(c.getName() + " начал этап: " + description);
            Thread.sleep(length / c.getSpeed() * 1000);
            System.out.println(c.getName() + " закончил этап: " + description);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(finalStage){
            finishControl.countDown();
        }
    }
}
