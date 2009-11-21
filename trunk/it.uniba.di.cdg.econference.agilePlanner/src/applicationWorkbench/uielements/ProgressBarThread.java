package applicationWorkbench.uielements;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class ProgressBarThread extends Thread{
	
	
		private  Shell loadingWindow;
		private  Display display;
		private  Label loadingLabel;
		private  boolean isLoading;
		private static ProgressBarThread pgth=null;
		
		private ProgressBarThread(){
			
		}
		
		public static ProgressBarThread  getInstatnce(){
			if(pgth==null)
				pgth = new ProgressBarThread();
		
			return pgth;
		}
		
		public void run()
		{
			this.display = new Display();
			this.loadingWindow = new Shell(this.display, SWT.NONE);
			this.loadingWindow.setSize(200, 100);
			
			this.loadingWindow.setLocation(400, 300);
			
			this.loadingLabel = new Label(this.loadingWindow, SWT.NONE);
			this.loadingLabel.setLocation(80, 40);
			this.loadingLabel.setSize(100, 20);
			this.loadingLabel.setText("Loading");
			
			this.loadingWindow.open();
			
			this.isLoading = true;
			
			
			while(this.isLoading)
			{
				System.out.println("asdfasdfasdfasdfasdfasdfasdfasdfsadfadsfadfadfasdf");
				try {
					this.loadingLabel.setText("Loading.");
					sleep(1000);
					this.loadingLabel.setText("Loading..");
					sleep(1000);
					this.loadingLabel.setText("Loading...");
					sleep(1000);
				} catch (InterruptedException e) {
					System.out.println("Loading thread is being stupid!");
				}
			}
			
		}

		public static ProgressBarThread getPgth() {
			return pgth;
		}

		public static void setPgth(ProgressBarThread pgth) {
			ProgressBarThread.pgth = pgth;
		}
	

}
