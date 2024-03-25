package editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import clipboard.ClipboardStack;
import clipboard.CopyAction;
import clipboard.CutAction;
import clipboard.PasteAction;
import clipboard.PasteAndTakeAction;
import location.Location;
import observer.ClipboardObserver;
import observer.CursorObserver;
import observer.SelectionObserver;
import observer.TextObserver;

public class TextEditor extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final int WINDOW_WIDTH = 800;
	private static final int WINDOW_HEIGHT = 700;

	private TextEditorModel model;
	private TextEditorPanel panel;
	private ClipboardStack stack;
	
	private AbstractAction cut;
	private AbstractAction copy;
	private AbstractAction paste;
	private AbstractAction pasteAndTake;

	public TextEditor() {
		initTextEditorModel("Hello World!\nMy name is Lovro Srebacic!\nI hope you have a great day!\n");
		initGUI();
		createActions();
		createMenuBar();
		createToolbar();
	}

	private void initTextEditorModel(String text) {
		this.model = new TextEditorModel(text);
		this.stack = new ClipboardStack();
		
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
					if (e.isShiftDown()) {
						model.addSelectionLeft();
					} else {
						model.moveCursorLeft(false);
					}
					break;
				}

				case KeyEvent.VK_RIGHT: {
					if (e.isShiftDown()) {
						model.addSelectionRight();
					} else {
						model.moveCursorRight(false);
					}
					break;
				}

				case KeyEvent.VK_UP: {
					if (e.isShiftDown()) {
						model.addSelectionUp();
					} else {
						model.moveCursorUp(false);
					}
					break;
				}

				case KeyEvent.VK_DOWN: {
					if (e.isShiftDown()) {
						model.addSelectionDown();
					} else {
						model.moveCursorDown(false);
					}
					break;
				}
				case KeyEvent.VK_BACK_SPACE: {
					if (model.hasSelection()) {
						model.executeAction("DeleteRangeAction");
					} else {
						model.executeAction("DeleteBeforeAction");
					}
					break;
				}
				case KeyEvent.VK_DELETE: {
					if (model.hasSelection()) {
						model.executeAction("DeleteRangeAction");
					} else {
						model.executeAction("DeleteAfterAction");
					}
					break;
				}
				default: {
					if (e.getKeyCode() != KeyEvent.VK_SHIFT && !e.isActionKey() && e.getKeyCode() != KeyEvent.VK_ALT
							&& e.getKeyCode() != KeyEvent.VK_ALT_GRAPH && !e.isControlDown()
							&& e.getKeyCode() != KeyEvent.VK_ESCAPE) {
						model.insert(e.getKeyChar());
						model.executeAction("InsertTextAction");
					}
				}
				}
			}
		});
	}
	
	private void createActions() {
		cut = new CutAction(stack, model, panel);
		configureAction(cut, "Cut", KeyStroke.getKeyStroke("control X"), KeyEvent.VK_X, false);
		copy = new CopyAction(stack, model);
		configureAction(copy, "Copy", KeyStroke.getKeyStroke("control C"), KeyEvent.VK_C, false);
		paste = new PasteAction(stack, model);
		configureAction(paste, "Paste", KeyStroke.getKeyStroke("control V"), KeyEvent.VK_V, false);
		pasteAndTake = new PasteAndTakeAction(stack, model);
		configureAction(pasteAndTake, "Paste and Pop", KeyStroke.getKeyStroke("control shift V"), KeyEvent.VK_V, false);
		
		stack.addClipboardObserver((ClipboardObserver) paste);
		stack.addClipboardObserver((ClipboardObserver) pasteAndTake);
		model.addSelectionObserver((SelectionObserver) cut);
		model.addSelectionObserver((SelectionObserver) copy);
	}
	
	private void configureAction(AbstractAction action, String name, KeyStroke accelerator, int mnemoic, boolean enabled) {
		action.putValue(Action.NAME, name);
		action.putValue(Action.ACCELERATOR_KEY, accelerator);
		action.putValue(Action.MNEMONIC_KEY, mnemoic);
		action.setEnabled(enabled);
	}
	
	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu edit = new JMenu("Edit");
		edit.add(new JMenuItem(cut));
		edit.add(new JMenuItem(copy));
		edit.add(new JMenuItem(paste));
		edit.add(new JMenuItem(pasteAndTake));
		
		menuBar.add(edit);
		
		setJMenuBar(menuBar);
	}
	
	private void createToolbar() {
		JToolBar toolBar = new JToolBar();
		
		toolBar.add(new JButton(cut));
		toolBar.add(new JButton(copy));
		toolBar.add(new JButton(paste));
		toolBar.add(new JButton(pasteAndTake));
		
		toolBar.setFloatable(true);
		add(toolBar, BorderLayout.NORTH);
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
