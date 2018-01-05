package org.jasig.cas.zk.asyn;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 带缓存的线程池 异步处理器
 * @author syf
 *
 */
public class AsynProcessor {

		private AsynProcessor(){
				exec = Executors.newCachedThreadPool();
		}
	
		private static class AsynProcessorHolder {
		      	private final static AsynProcessor INSTANCE = new AsynProcessor();
		}
		 
		public static AsynProcessor getInstance() {
			
				return AsynProcessorHolder.INSTANCE;
		}
		//------------------以上为单例实现----------------------
	
		private ExecutorService exec;
		
		
		private void process(Runnable command){
				
				exec.execute(command);
		}
		
		public static void asynProcess(Runnable command){
				AsynProcessor processor = AsynProcessor.getInstance();
				processor.process(command);
		}
		
}
