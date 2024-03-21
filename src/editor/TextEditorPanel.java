package editor;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.LineMetrics;
import java.util.Iterator;

import javax.swing.JPanel;

import location.Location;

public class TextEditorPanel extends JPanel{

	private static final long serialVersionUID = 1L;

	private static final int MARGIN = 5;
	private static final int SPACE = 20;
	
	private TextEditorModel model;
	private int width;
	private int height;
	
	public TextEditorPanel(TextEditorModel model) {
		this.model = model;
		this.width = 0;
		this.height = 0;
		this.setFocusable(true);
		this.requestFocusInWindow();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setFont(new Font("Courier New", Font.PLAIN, 12));
		FontMetrics fm = g2d.getFontMetrics();
		
		int lineWidth = 0;
		Iterator<String> lineIterator = this.model.allLines();
		String line = "";
		int counter = 0;
		
		while(lineIterator.hasNext()) {
			line = lineIterator.next();
			lineWidth = (int) fm.getStringBounds(line, g2d).getWidth();
			if(lineWidth > width) {
				width = lineWidth;
			}
			
			g2d.drawString(line, MARGIN, counter * SPACE + fm.getHeight());
			counter++;
		}
		
		this.height = counter * SPACE + fm.getHeight();
		this.width += 3 * MARGIN;
		
		Location cursorLocation = this.model.getCursorLocation();
		
		if (!this.model.getLines().isEmpty() && this.model.getLine(cursorLocation.getY()).length() != 0) {
			line = this.model.getLine(cursorLocation.getY()).substring(0, cursorLocation.getX());
		} else {
			line = "";
		}

		LineMetrics lm = g2d.getFont().getLineMetrics(line, g2d.getFontRenderContext());

		int x1 = fm.stringWidth(line) + MARGIN;
		int y1 = cursorLocation.getY() * SPACE + fm.getHeight();
		int x2 = x1;
		int y2 = y1 - (int) lm.getHeight();

		g2d.drawLine(x1, y1, x2, y2);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}
}
