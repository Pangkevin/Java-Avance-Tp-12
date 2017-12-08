import java.util.ArrayDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FifoB<E> {

	private ArrayDeque<E> ad;
	private final Object monitor = new Object();
	int taille;
	private Lock lock = new ReentrantLock();
	// Permet de liberer le lock sous une certaine condition
	final Condition notFull  = lock.newCondition(); 
	final Condition notEmpty  = lock.newCondition(); 

	public FifoB(int taille) {
		if (taille < 0) {
			throw new IllegalArgumentException();
		}
		ad = new ArrayDeque<E>();
		this.taille = taille;
	}

	public void add(E e) throws InterruptedException {

		synchronized (monitor) {
			// tant que ad vaut la taille max, alors on attend
			while (ad.size() == taille) {
				notFull.await();
			}
			ad.add(e);

			// On reveille tout le monde pour dire que ya a manger
			monitor.notifyAll();

		}
	}

	public E poll() throws InterruptedException {
		E monE = null;
		synchronized (monitor) {

			while (ad.size() == 0) {
				notEmpty.await();
			}
			monE = ad.poll();
			monitor.notifyAll();
			return monE;
		}
	}

	public void addLock(E e) throws InterruptedException {

		lock.lock();
		// tant que ad vaut la taille max, alors on attend
		while (ad.size() == taille) {
			lock.wait();
		}
		ad.add(e);
		lock.unlock();
		
		// On reveille tout le monde pour dire que ya a manger
		lock.notifyAll();

	}

	public E pollLock() throws InterruptedException {
		E monE = null;
		lock.lock();
		while (ad.size() == 0) {
			lock.wait();
		}
		monE = ad.poll();
		lock.unlock();
		lock.notifyAll();
		return monE;

	}

}
