
public class MonicAndClementThread implements Runnable {
	public Bank bank = new Bank();
	public static void main(String[] args) {
		MonicAndClementThread job = new MonicAndClementThread();
		Thread monic = new Thread(job);
		Thread clement = new Thread(job);
		monic.setName("Monic");
		clement.setName("Clement");
		monic.start();
		clement.start();
	}
	
	public void run() {
       
		while(bank.getAccount()>0) {
			System.out.println(Thread.currentThread().getName()+" Thread is entering the withdrawl method");
			withDrawlAmount(10);
			if(bank.getAccount()<0) {
				System.out.println(Thread.currentThread().getName()+" Thread you overdraw from the account you are now in dept of"+Math.abs(bank.getAccount())+"$");
			}
		}

		
    }

	private synchronized void withDrawlAmount(int amount) {
		if(bank.getAccount()>=amount) {
			System.out.println(Thread.currentThread().getName()+" Thread will process to the withdrawl of "+amount+"$");
			System.out.println(Thread.currentThread().getName()+" Thread will sleep now");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName()+" Thread woke up now");
			bank.withdraw(amount);
			System.out.println(Thread.currentThread().getName()+" Thread finished with the withdrawl of "+amount+"$\nYou have "+bank.getAccount()+"$ in your account");
		}else {
			System.out.println(Thread.currentThread().getName()+" Thread you cant take out money your account budget is "+bank.getAccount()+"$");
		}
		
	}
}
