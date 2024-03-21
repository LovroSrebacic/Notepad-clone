package editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import location.Location;
import observer.CursorObserver;
import observer.TextObserver;

public class TextEditor extends JFrame {

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
		this.model.addCursorObserver(new CursorObserver() {

			@Override
			public void updateCursorLocation(Location location) {
				panel.revalidate();
				panel.repaint();
			}
		});
		this.model.addTextObserver(new TextObserver() {
			@Override
			public void updateText() {
				panel.revalidate();
				panel.repaint();
			}
		});
	}

	private void initGUI() {
		this.panel = new TextEditorPanel(model);
		this.panel.setBackground(Color.WHITE);
		add(this.panel, BorderLayout.CENTER);

		addListeners();

		pack();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double screenWidth = screenSize.getWidth();
		double screenHeight = screenSize.getHeight();
		setLocation((int) screenWidth / 2 - (WINDOW_WIDTH / 2), (int) screenHeight / 2 - (WINDOW_HEIGHT / 2));
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setTitle("Notepad clone");
		this.panel.revalidate();
		this.panel.repaint();
	}

	private void addListeners() {
		this.panel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT: {
					model.moveCursorLeft();
					break;
				}

				case KeyEvent.VK_RIGHT: {
					model.moveCursorRight();
					break;
				}

				case KeyEvent.VK_UP: {
					model.moveCursorUp();
					break;
				}

				case KeyEvent.VK_DOWN: {
					model.moveCursorDown();
					break;
				}
				case KeyEvent.VK_BACK_SPACE: {
					model.executeAction("DeleteBeforeAction");
					break;
				}
				case KeyEvent.VK_DELETE: {
					model.executeAction("DeleteAfterAction");
					break;
				}
				}
			}
		});
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
