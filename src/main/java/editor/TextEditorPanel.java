package main.java.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import javax.swing.JPanel;

import main.java.location.Location;
import main.java.location.LocationRange;

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
		
		if (this.model.hasSelection()) {
			LocationRange range = this.model.getSelectionRange();
			Location start = null;
			Location end = null;

			if (range.getEnd().getY() > range.getStart().getY()) {
				start = range.getStart();
				end = range.getEnd();
			} else if (range.getEnd().getY() == range.getStart().getY()) {
				if (range.getEnd().getX() > range.getStart().getX()) {
					start = range.getStart();
					end = range.getEnd();
				} else {
					start = range.getEnd();
					end = range.getStart();
				}
			} else {
				start = range.getEnd();
				end = range.getStart();
			}

			g2d.setColor(Color.LIGHT_GRAY);
			Rectangle2D rectangle = null;
			String line = null;

			if (end.getY() - start.getY() == 0) {
				line = this.model.getLine(start.getY());
				rectangle = fm.getStringBounds(line.substring(start.getX(), end.getX()), g2d);
				g2d.fillRect(MARGIN + fm.stringWidth(line.substring(0, start.getX())), start.getY() * SPACE,
						(int) rectangle.getWidth(), fm.getHeight());
			} else {
				line = this.model.getLine(start.getY());
				rectangle = fm.getStringBounds(line.substring(start.getX()), g2d);
				g2d.fillRect(MARGIN + fm.stringWidth(line.substring(0, start.getX())), start.getY() * SPACE,
						(int) rectangle.getWidth(), fm.getHeight());
				for (int i = start.getY() + 1; i < end.getY(); i++) {
					line = model.getLine(i);
					rectangle = fm.getStringBounds(line, g2d);
					g2d.fillRect(MARGIN, i * SPACE, (int) rectangle.getWidth(), fm.getHeight());
				}

				line = model.getLine(end.getY());
				rectangle = fm.getStringBounds(line.substring(0, end.getX()), g2d);
				g2d.fillRect(MARGIN, end.getY() * SPACE, (int) rectangle.getWidth(), fm.getHeight());
			}
			
			g2d.setColor(Color.BLACK);
		}
		
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
	
	public void setModel(TextEditorModel model) {
		this.model = model;
	}
}
