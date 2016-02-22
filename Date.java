/*CS340 FL15 Project 2
 * Wang, Stacy 23062813
 * M/W Class
 * Professor Fluture
 * **/

import java.util.*;
import java.util.concurrent.*;

public class Date extends Thread {
	Semaphore taken = new Semaphore(1);
	Random r = new Random();
	public boolean decision; 
	static Semaphore allDone = new Semaphore(Game.num_date);
	
	public Date(int id){
		setName("Date " + id);
	}
	
	public void run(){
		while(SmartPants.welcomed.availablePermits()>0){
			try{
				sleep(100);
			}
			catch(InterruptedException ie){
				System.out.println("problem with approached() in " + getName());
			}
		}
		while(allDone.tryAcquire()==false){
			waiting();
		}
		goHome();
	}
	
	private void waiting(){
			try{
				sleep(r.nextInt(2000-1000+1)+1000);
				if (allDone.availablePermits()==0) { System.out.println(getName() + " is waiting to be approached.");}
			}
			catch(InterruptedException ie){;}
	}
	
	public void approached(int ID){
		int priority = getPriority();
		setPriority(priority+1);
		System.out.println(getName() + " is speaking to " + Game.Contestant[ID-1].getName());
		try{
			sleep(r.nextInt(2000-1000+1)+1000);
		}
		catch(InterruptedException ie){
			System.out.println("problem with approached() in " + getName());
		}
		decision=r.nextBoolean();
		setPriority(priority);
	}
	
	public void goHome(){
			System.out.println(getName() + " has left the club.");
	}
	
	public static long time = System.currentTimeMillis();
	public void msg(String m){
		System.out.println("[" + (System.currentTimeMillis()-time)+ "]" + getName() + ": " + m);
	}
}
