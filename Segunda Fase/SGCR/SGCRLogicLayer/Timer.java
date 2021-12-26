package SGCRLogicLayer;

import SGCRDataLayer.Servicos.ServicosFacade;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Timer extends Thread implements Serializable {
	ServicosFacade sf;
	ReentrantLock rlock = new ReentrantLock();
	Condition cond = rlock.newCondition();
	boolean running;

	public boolean isRunning() {
		return running;
	}

	public Condition getCond() {
		return cond;
	}

	public Timer(ServicosFacade sf){
		this.sf=sf;
	}
	public void run(){
		while (true){
			running = true;
			sf.arquiva_e_sinalizaExpirados();
			running=false;
			try {
				TimeUnit.DAYS.sleep(1);
			} catch (InterruptedException e) {
				break;
			}
			cond.signal();
		}
	}

}