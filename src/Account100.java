import java.util.Scanner;

public class Account100 {
	private static String domain = "192.168.200.10";
	private int account_number;
	private int admin_number;
	private static String name_prefix = "test";
	
	private SmackCore core;
	
	public static void main (String[] args){
		Account100 main = new Account100();
		main.run();
	}
	public void run(){

		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Tester number : ");
		this.account_number = Integer.valueOf(scanner.next());
		
		System.out.print("Admin number : ");
		this.admin_number = Integer.valueOf(scanner.next());
		
		this.core = new SmackCore(
				domain ,
				account_number ,
				admin_number ,
				name_prefix
				);
		
		System.out.println( "Creating " + this.account_number + " accounts..." );
		this.core.createAccounts();
		System.out.println( account_number + " accounts created!" );
		
		System.out.println("Creating " + this.account_number + " listeners");
		this.core.createListeners();
		System.out.println( account_number + " listener created!");
		
		pause();
		
		System.out.println("Building " +
				this.account_number + "*" + this.admin_number + "=" +
				this.account_number * this.admin_number +
				" friendships...");
		this.core.createFriendships();
		System.out.println( account_number + " friendships built!");
		
		pause();
		
		System.out.println("Deleting " + this.account_number + " accounts..." );
		this.core.removeAccounts();
		System.out.println( account_number + " accounts deleted!");
		
		pause();
		
		System.out.println("Disconnecting " + this.account_number + " friendships..." );
		this.core.disconnectListeners();
		System.out.println( account_number + " friendships disconnected!");
		
		System.out.println("Exit!");
	}
	public static void pause(){
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		System.out.print("Press the enter key to continue");
		scanner.nextLine();
	}
}
