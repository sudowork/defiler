package chaosmonkey;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import dfs.DFS;
import dfs.DFSSingleton;

import junit.framework.TestCase;

public class MonkeyTest extends TestCase {
	
	private DFS dfs;
	
	public void setUp() {
		dfs = DFSSingleton.getInstance("MONKEYTEST.dat", true);
	}
	
	public void testReleaseTheMonkeys() {
		int numMonkeys = 8;
		int numOpsPerMonkey = 128;
		
		ChaosMonkey[] monkeys = new ChaosMonkey[numMonkeys];
		for (int i = 0; i < numMonkeys; i++) {
			monkeys[i] = new ChaosMonkey(i, numOpsPerMonkey);
		}
		
		ExecutorService pool = Executors.newFixedThreadPool(numMonkeys);
		for (ChaosMonkey cm : monkeys) {
			pool.execute(cm);
		}
		pool.shutdown();
		
		try {
			pool.awaitTermination(180, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			for (ChaosMonkey cm : monkeys) {
				if (cm.getException() != null) cm.getException().printStackTrace();
				assertNull(cm.getException());
			}
		}
	}
	
	public void tearDown() {
		dfs.sync();
	}

}
