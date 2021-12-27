package SGCRLogicLayer;

import SGCRDataLayer.Servicos.ServicosFacade;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Timer extends Thread {
	ServicosFacade sf;
	ReentrantLock rlock = new ReentrantLock();

	public ReentrantLock getLock() { return rlock; }

	public Timer(ServicosFacade sf){
		this.sf = sf;
	}

	public void run(){
		while (true){
			try{
				rlock.lock();
				sf.arquiva_e_sinalizaExpirados();
			}finally { rlock.unlock(); }
			try { TimeUnit.DAYS.sleep(1); }
			catch (InterruptedException e) { return; }
		}
	}

}