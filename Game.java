/*CS340 FL15 Project 2
 * Wang, Stacy 23062813
 * M/W Class
 * Professor Fluture
 * **/

public class Game extends Thread {
	public static int num_contestant; // number of contestants
	public static int num_date; //number of dates
	public static int num_rounds; //number of chances contestants have to attempt to obtain contact info
	public static int group_size; //size which group of contestants must be before leaving
	public static Contestant[] Contestant;
	public static Date[] Date;
	
	public static void main(String args[]){
		//prompt user to enter values
		System.out.println("Please enter four values in order as follows for number of contestants, number of dates, number of rounds, and group size" );
		num_contestant=Integer.parseInt(args[0]);
		num_date=Integer.parseInt(args[1]);
		num_rounds=Integer.parseInt(args[2]);
		group_size=Integer.parseInt(args[3]);
		System.out.println("Contestants: " + num_contestant + " Dates: " + num_date + " Rounds: " + num_rounds + " Group size: " + group_size);
		//start up all characters in the game show
		SmartPants SmartPants = new SmartPants();
		SmartPants.start();
		Date= new Date[num_date];
		for (int i=0; i<num_date; i++){
			Date[i]= new Date(i+1);
			Date[i].start();
		}
		Contestant = new Contestant[num_contestant];
		for (int i=0; i<num_contestant; i++){
			Contestant[i]= new Contestant(i+1);
			Contestant[i].start();
		}
	}
}