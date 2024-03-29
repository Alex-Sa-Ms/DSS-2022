package SGCRLogicLayer;

import SGCRDataLayer.Servicos.iServico;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Timer extends Thread {
	iServico sf;
	ReentrantLock rlock = new ReentrantLock();

	/** @return  lock associada ao timer. */
	public ReentrantLock getLock() { return rlock; }

	/**
	 * Construtor de um Orcamento
	 * @param sf Serviços a que o Timer tera acesso
	 */
	public Timer(iServico sf){
		this.sf = sf;
	}

	/** Run do timer, arquiva os serviços finalizados. */
	public void run(){
		while (true){
			try{
				rlock.lock();
				sf.arquiva_e_sinalizaExpirados();
			}finally { rlock.unlock(); }
			try { TimeUnit.DAYS.sleep(1); }
			catch (InterruptedException e) {
				return;
			}
		}
	}

}