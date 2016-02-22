/*CS340 FL15 Project 2
 * Wang, Stacy 23062813
 * M/W Class
 * Professor Fluture
 * **/

import java.util.concurrent.*;

public class SmartPants extends Thread {
	static Semaphore available = new Semaphore(1); //SmartPants availability, can speak to one contestant at a time
	static Semaphore showEnds = new Semaphore(1); //indicates whether or not the show has ended
	static Semaphore welcomed = new Semaphore(Game.num_contestant); //number of contestants welcomed by smartpants
	
	public SmartPants(){
		setName("SmartPants");
	}
	
	public void run(){
		welcome(); 
		waitingToEndShow();
	}
	
	/**welcomes each contestant ensures that SmartPants is first ready by taking available semaphore and then releasing it*/
	private void welcome(){
		try{
			available.acquire();
			System.out.println("SmartPants is ready to go and waiting for contestants to arrive.");
			available.release();
			showEnds.acquire(); //show has not ended
		}
		catch (InterruptedException ie){
			System.out.println("Issue in welcome() SmartPants.");
		}
	}
	
	/**waiting for show to end by waiting for the final contestant to finish and release the showEnds mutex*/
	private void waitingToEndShow(){
		try{
			showEnds.acquire();
			System.out.println("Show has ended"); 
			System.out.println("SmartPants has left the club.");
		}
		catch(InterruptedException ie){ };
	}
	
	public static long time = System.currentTimeMillis();
	public void msg(String m){
		System.out.println("[" + (System.currentTimeMillis()-time)+ "]" + getName() + ": " + m);
	}
	
}