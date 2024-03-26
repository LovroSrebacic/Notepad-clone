package status_bar;

import javax.swing.JLabel;

import observer.StatusBarObserver;

public class StatusLabel extends JLabel implements StatusBarObserver{

	private static final long serialVersionUID = 1L;
	
	public StatusLabel(String text) {
		this.setText(text);
	}

	@Override
	public void updateStatusBar(String text) {
		this.setText(text);
	}

}
