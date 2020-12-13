
public class MonicAndClementThread implements Runnable {
	//bank account instance variable of the job class "MonichAndClementThread"
	public Bank bank = new Bank();
	public static void main(String[] args) {
		//create instance of the job class
		MonicAndClementThread job = new MonicAndClementThread();
		//hock the job to the threads
		Thread monic = new Thread(job);
		Thread clement = new Thread(job);
		//set names for the threads
		monic.setName("Monic");
		clement.setName("Clement");
		//start the threads
		monic.start();
		clement.start();
	}
	
	public void run() {
       //while the account amount is greater then zero we process to the withdrawl
		//here the two threads will alternate the withdrawl of 10 dollars from the account (containes 100$) till the account is equal to zero
		while(bank.getAccount()>0) {
			System.out.println(Thread.currentThread().getName()+" Thread is entering the withdrawl method");
			withDrawlAmount(10);
			if(bank.getAccount()<0) {
				System.out.println(Thread.currentThread().getName()+" Thread you overdraw from the account you are now in dept of"+Math.abs(bank.getAccount())+"$");
			}
		}

		
    }
	
	//this method is synchronized and will be enterd by the thread that hold the lock key 
	//the lock key is class based key it means and thread holds it will lock all the synchronized methods in this class
	private synchronized void withDrawlAmount(int amount) {
		//if the amount is less then what we have in the account we proceed to the withdraw
		if(bank.getAccount()>=amount) {
			System.out.println(Thread.currentThread().getName()+" Thread will process to the withdrawl of "+amount+"$");
			System.out.println(Thread.currentThread().getName()+" Thread will sleep now");
			//the thread will sleep for half second before withdrawing
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//the thread proceed to the withdrawl
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName()+" Thread woke up now");
			//
			bank.withdraw(amount);
			System.out.println(Thread.currentThread().getName()+" Thread finished with the withdrawl of "+amount+"$\nYou have "+bank.getAccount()+"$ in your account");
		}else {
			System.out.println(Thread.currentThread().getName()+" Thread you cant take out money your account budget is "+bank.getAccount()+"$");
		}
		
	}
}
