/*CS340 FL15 Project 2
 * Wang, Stacy 23062813
 * M/W Class
 * Professor Fluture
 * **/

import java.util.*;
import java.util.concurrent.*;

public class Contestant extends Thread {
	private int ID; //id of contestant
	static Semaphore doneDating = new Semaphore(Game.num_contestant); //checks to make sure that each contestant is finished
	private Random r = new Random(); 
	private int rounds = Game.num_rounds; //rounds allowed per contestant
	private static int grouped = 0; //number of contestants grouped thus far
	private static int groupnumber = 1; //identification to distinguish which contestant left with which group
	private Vector<String> Contacts = new Vector<String>(); //stores dates who provide contact info to contestant
	static Semaphore leave = new Semaphore (Game.group_size); //permits the contestant to leave in said group
	
	public Contestant (int id){
		setName("Contestant " + id);
		ID=id;
	}
	
	public void run(){
		enteringClub();
		dating();
		waiting();
		linger();
		grouped();
	}
	
	/**Contesant travels to club, welcomed by Smartpants*/
	private void enteringClub(){
		try{
			SmartPants.showEnds.tryAcquire(); //allows for the first contestant to say that the showEnds has not ended by taking it...preceding contestants does nothing to mutex
			Date.allDone.tryAcquire(Game.num_date);
			System.out.println(getName() + " is traveling to club.");
			sleep(r.nextInt(2000-1000+1)+1000); 
			System.out.println(getName() + " has arrived at the club.");
			SmartPants.available.acquire(); //so can speak to smartpants
			System.out.println("SmartPants welcoming " + getName());
			SmartPants.welcomed.acquire(); //marks that it has been welcomed
			SmartPants.available.release(); //so others can speak to smartpants
			System.out.println(getName() + " enters club."); 
			if (SmartPants.welcomed.availablePermits()==0){ //so that we know all contestants have spoken to smartpants...smartpants can standby
				System.out.println("All contestants have entered the club. SmartPants will now wait for Contestants to finish");
			}
		}
		catch (InterruptedException ie){
			System.out.println("In enteringclub(), issue with " + getName()); 
		}
	}
	
	/**Interacting with dates*/
	private void dating(){
		while (rounds!=0){
			for (int i=0; i<Game.num_date; i++){
				if (Game.Date[i].taken.tryAcquire()) { //can approach date
					System.out.println(getName() + " has approached " + Game.Date[i].getName());
					Game.Date[i].approached(ID);
					if (Game.Date[i].decision&&!Contacts.contains(Game.Date[i].getName())){
						Contacts.add(Game.Date[i].getName());
					}
					rounds--;
					Game.Date[i].taken.release(); //allows date to speak to another contestant
					if (rounds==0){ break; }
				}
			}
		}
	}
	
	private void waiting(){
		System.out.println(getName() + " waiting for show to end.");
		try{
			doneDating.acquire();
			if (doneDating.availablePermits()==0){
				SmartPants.showEnds.release();
				Date.allDone.release(Game.num_date);
			}
		}
		catch(InterruptedException ie){
			System.out.println("in waiting() of " + getName());
		}
	}
	
	private void linger(){
		System.out.println(getName() + " lingering outside bragging.");
		try{
			leave.acquire();
		}
		catch(InterruptedException ie){;}
	}

	private void grouped(){
		grouped++;
		System.out.println(getName() + " grouped in " + groupnumber);
		System.out.println(getName() + " has left.");
		System.out.println(getName() + " has received contact info from: ");
		if (Contacts.size()==0){
			System.out.println("No one.");
		}
		for (int i=0; i<Contacts.size(); i++){
			System.out.println(Contacts.get(i));
		}
		if (grouped%Game.group_size==0){
			leave.release(Game.group_size);
			groupnumber++;
		}
	}
	public static long time = System.currentTimeMillis();
	public void msg(String m){
		System.out.println("[" + (System.currentTimeMillis()-time)+ "]" + getName() + ": " + m);
	}
}