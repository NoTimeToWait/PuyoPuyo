package visuals;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

import engine.Options;
import game.GameObject;
import game.Puyo;

public class FieldView extends JPanel {
	private Image[] images;
	private GameObject[] drawObjects;
	public FieldView() {
		super();
		images = new Image[Options.getImageLinks().length];
		for (int i=0; i<images.length; i++)
			images[i] = Toolkit.getDefaultToolkit().getImage(Options.getImageLinks()[i]);
		
	}
	public void drawObjects(GameObject[] objects) {
		drawObjects = objects;
		repaint();
	}
	
	public void paint(Graphics g)
	{
		if (drawObjects==null) return;
		g.clearRect(0, 0, getWidth(), getHeight());
		for (GameObject obj:drawObjects) 
			switch (obj.getType()) {
				case Puyo.RED|GameObject.PUYO_TYPE_MASK: 
					g.drawImage(images[0],obj.getX(), obj.getY(), this); break;
				case Puyo.GREEN|GameObject.PUYO_TYPE_MASK: 
					g.drawImage(images[1],obj.getX(), obj.getY(), this); break;
				case Puyo.BLUE|GameObject.PUYO_TYPE_MASK: 
					g.drawImage(images[2],obj.getX(), obj.getY(), this); break;
				case Puyo.YELLOW|GameObject.PUYO_TYPE_MASK: 
					g.drawImage(images[3],obj.getX(), obj.getY(), this); break;
				default: g.drawImage(images[4],obj.getX(), obj.getY(), this);
			};
		drawObjects = null;
	}
}
