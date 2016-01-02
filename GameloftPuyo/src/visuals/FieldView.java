package visuals;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

import engine.Options;
import game.GameField;
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
		if ( GameField.cells!=null)
		for (int i=0; i<6; i++)
			for (int j=0; j<12; j++) {
				GameObject obj = GameField.cells[i][j].gameObject;
				if (obj!=null)
				switch (obj.getType()) {
				case Puyo.RED|GameObject.PUYO_TYPE_MASK: 
					g.drawImage(images[0],200+i*32, j*32, this); break;
				case Puyo.GREEN|GameObject.PUYO_TYPE_MASK: 
					g.drawImage(images[1],200+i*32, j*32, this); break;
				case Puyo.BLUE|GameObject.PUYO_TYPE_MASK: 
					g.drawImage(images[2],200+i*32, j*32, this); break;
				case Puyo.YELLOW|GameObject.PUYO_TYPE_MASK: 
					g.drawImage(images[3],200+i*32, j*32, this); break;
				default: g.drawImage(images[4],200+i*32, j*32, this);
			};
			}
		for (GameObject obj:drawObjects) 
			switch (obj.getType()) {
				case Puyo.RED|GameObject.PUYO_TYPE_MASK: 
					g.drawImage(images[0],obj.getDrawX(), obj.getDrawY(), this); break;
				case Puyo.GREEN|GameObject.PUYO_TYPE_MASK: 
					g.drawImage(images[1],obj.getDrawX(), obj.getDrawY(), this); break;
				case Puyo.BLUE|GameObject.PUYO_TYPE_MASK: 
					g.drawImage(images[2],obj.getDrawX(), obj.getDrawY(), this); break;
				case Puyo.YELLOW|GameObject.PUYO_TYPE_MASK: 
					g.drawImage(images[3],obj.getDrawX(), obj.getDrawY(), this); break;
				default: g.drawImage(images[4],obj.getDrawX(), obj.getDrawY(), this);
			};
		drawObjects = null;
	}
}
