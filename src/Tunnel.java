import java.util.concurrent.Semaphore;

public class Tunnel extends Stage {
    private Semaphore tunnelControl;
    public Tunnel(Semaphore cntl) {
        this.length = 80;
        this.description = "Тоннель " + length + " метров";
        this.tunnelControl = cntl;
    }
    @Override
    public void go(Car c) {
        try {
            try {
                tunnelControl.acquire();
                System.out.println(c.getName() + " готовится к этапу(ждет): " + description);
                System.out.println(c.getName() + " начал этап: " + description);
                Thread.sleep(length / c.getSpeed() * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(c.getName() + " закончил этап: " + description);
                tunnelControl.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
