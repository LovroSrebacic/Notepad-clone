package editor;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import javax.swing.JPanel;

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
		
		List<String> lines = this.model.getLines();
		
		int lineWidth;
		for(int i = 0; i < lines.size(); i++){
			lineWidth = (int) fm.getStringBounds(lines.get(i), g2d).getWidth();
			if(lineWidth > width) {
				width = lineWidth;
			}
			
			g2d.drawString(lines.get(i), MARGIN, i * SPACE + fm.getHeight());
		}
		
		this.height = lines.size() * SPACE + fm.getHeight();
		this.width += 3 * MARGIN;
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}
}
