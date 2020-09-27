import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class demonstrates how multiple threads can safely access a map. 
 * @author Juliane Schut
 *
 */
public class MultithreadingMap {
	
	private final static int THREAD_POOL_SIZE = 10; // The number of threads to be created.
	private final static CyclicBarrier gate = new CyclicBarrier(11); // Barrier that ensures all 10 thread + the main thread start at the same time.
	private static ConcurrentHashMap<Integer, Long> safeMapMultithread = new ConcurrentHashMap<Integer, Long>(); // The map all threads will add an entry to. 
	
	/**
	 * The program's point of entry. 10 threads are created, started and each add one entry to the ConcurrentHashMap.
	 * @param args
	 * @throws InterruptedException
	 * @throws BrokenBarrierException
	 */
	public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
	
		 for(int i = 0; i < THREAD_POOL_SIZE; i++){
			 Thread th = new Thread(() -> {
				 try {
					gate.await();
				} catch (InterruptedException | BrokenBarrierException e) {
					System.out.println("Gate await was unsuccessful");
					e.printStackTrace();
				}
				 addEntryToMap(safeMapMultithread);});
			 th.start();
			 }
		
		 gate.await(); // The Main thread awaits, now all threads will start running. 
		 
	}
	
	/**
	 * Generates a random integer to serve as the hashmap key and captures the current time to be added as the value.
	 * ThreadLocalRandom minimizes overhead compared to Random.
	 * @param map - The map to which an entry needs to be added. 
	 */
	private static void addEntryToMap(ConcurrentHashMap<Integer,Long> map){
			
		 	Integer randomInt = ThreadLocalRandom.current().nextInt();
			long timestamp = System.nanoTime();
			map.put(randomInt, timestamp);
			
		}
}


