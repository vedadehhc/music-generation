package geneticAlgorithm;

public class RandomNumberGenerator extends Thread {

	private int r;
	private int limit;
	private boolean running;
	
	public static void main(String[] args) {
		RandomNumberGenerator rng = new RandomNumberGenerator(100);
		rng.start();
		for(int i =0; i < 100; i ++) {
			System.out.println(rng.r);
		}
		rng.exit();
	}

	public RandomNumberGenerator(int limit) {
		this.limit = limit;
//		r = new AtomicInteger((int) (Math.random() * limit));
		r = (int) (Math.random() * limit);
		running = true;
	}

	@Override
	public void run() {
		while (running) {
//			r.incrementAndGet();
//			r.compareAndSet(limit, 0);
			
			r++;
			if(r >= limit) {
				r = 0;
			}
		}
	}
	
	public int getRand() {
		return r;
	}

	public void exit() {
		running = false;
	}
}
