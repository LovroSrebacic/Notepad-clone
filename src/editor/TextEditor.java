package editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class TextEditor extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private static final int WINDOW_WIDTH = 800;
	private static final int WINDOW_HEIGHT = 700;

	private TextEditorModel model;
	private JPanel panel;
	
	public TextEditor() {
		initTextEditorModel("Hello World!\nMy name is Lovro Srebacic!\nI hope you have a great day!\n");
		initGUI();
	}
	
	private void initTextEditorModel(String text) {
		this.model = new TextEditorModel(text);
	}
	
	private void initGUI() {
		this.panel = new TextEditorPanel(model);
		this.panel.setBackground(Color.WHITE);
		add(this.panel, BorderLayout.CENTER);
		
		pack();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double screenWidth = screenSize.getWidth();
		double screenHeight = screenSize.getHeight();
		setLocation((int)screenWidth / 2 - (WINDOW_WIDTH / 2), (int)screenHeight / 2 - (WINDOW_HEIGHT / 2));
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setTitle("Notepad clone");
		this.panel.revalidate();
		this.panel.repaint();
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new TextEditor().setVisible(true);
			}
		});
	}
	
}
