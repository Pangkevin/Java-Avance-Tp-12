import java.util.ArrayDeque;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Restaurant {

	//private ArrayDeque<Cake> ad = new ArrayDeque<Cake>();
	private FifoB<Cake> ad = new FifoB<Cake>(10);
//	private Lock vigile = new ReentrantLock();
	private Object videur = new Object();

	public Runnable createGateau(String typeGateau, int tempsPrepa) {
		return new Runnable() {

			@Override
			public void run() {

				while (true) {
					try {
//						vigile.lock();
						ad.addLock(new Cake(typeGateau));
						System.out.println("je crée " + typeGateau);
					}
					catch(Exception e){
						e.printStackTrace();
					}
					finally {
//						vigile.unlock();

					}
					try {
						Thread.sleep(tempsPrepa * 1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}

		};

	}

	public Runnable manger(String nomMangeur, int tempsMange) {
		return new Runnable() {

			@Override
			public void run() {
				
				while (true) {
					try {
//						vigile.lock();
						Cake a = ad.pollLock();
						if (!Objects.isNull(a)) {
							System.out.println(nomMangeur + " mange " + a.getGateau());
						}
					}
					catch(Exception e) {
						e.printStackTrace();
					}
					finally {
//						vigile.unlock();

					}
					
					try {
						Thread.sleep(tempsMange * 1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}

		};

	}

	public static void main(String[] args) {

		Restaurant resto = new Restaurant();
		// On cree un producteur
		new Thread(resto.createGateau("eclair", 2)).start();
		new Thread(resto.createGateau("eclair", 2)).start();
		new Thread(resto.createGateau("eclair", 2)).start();
		new Thread(resto.createGateau("eclair", 2)).start();
		new Thread(resto.createGateau("eclair", 2)).start();

		// On cree 5 consommateur
		new Thread(resto.manger("Toto", 2)).start();
		new Thread(resto.manger("Tata", 2)).start();
		new Thread(resto.manger("Trotro", 2)).start();
		new Thread(resto.manger("Coco", 2)).start();
		new Thread(resto.manger("Lolo", 2)).start();

	}

}
