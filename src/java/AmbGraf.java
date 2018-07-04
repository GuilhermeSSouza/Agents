
// Environment code for project graficTest

import jason.asSyntax.*;

import jason.environment.*;
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.GridWorldView;
import jason.environment.grid.Location;

import java.util.Random;
import java.util.logging.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.Serializable;

public class AmbGraf extends Environment {

	public static final int gridSize = 10;
	public static final int randFire = 16;

	/**
	 * Crenças iniais dos agentes
	 */
	public static final Literal civil = Literal.parseLiteral("proximaCasa");
	public static final Term piro = Literal.parseLiteral("andaPiro");

	private Logger logger = Logger.getLogger("graficTest." + AmbGraf.class.getName());

	private ModeloAmbGraf model;
	private VisaoAmbGraf view;

	/** Called before the MAS execution with the args informed in .mas2j */
	@Override
	public void init(String[] args) {
		// super.init(args);

		model = new ModeloAmbGraf();

		view = new VisaoAmbGraf(model);

		model.setView(view);

		updatePercepts();

	}

	@Override
	public boolean executeAction(String agName, Structure action) {
		logger.info(agName + ": Está fazendo :" + action);

		try {

			if (action.equals(civil)) {
				model.proximaCasa();
			} else if (action.equals(piro)) {						
				
				model.andaPiro();
			} else {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		updatePercepts();

		try {
			Thread.sleep(200);

		} catch (Exception e) {
		}
		informAgsEnvironmentChanged();
		return true;
	}

	
//	 /** Called before the end of MAS execution */
//	 @Override
//	 public void stop() {
//	 super.stop();
//	 }

	void updatePercepts() {
		clearPercepts();

		Location walkerLoc = model.getAgPos(0);
		Location piroLoc = model.getAgPos(1);
		Location poliLoc = model.getAgPos(2);
		Location bombLoc = model.getAgPos(3);
		
		

		Literal pos1 = Literal.parseLiteral("pos(walker," + walkerLoc.x + "," + walkerLoc.y + ")");
		Literal pos2 = Literal.parseLiteral("pos(piro," + piroLoc.x + "," + piroLoc.y + ")");
		Literal pos3 = Literal.parseLiteral("pos(policia," + poliLoc.x + "," + poliLoc.y + ")");
		Literal pos4 = Literal.parseLiteral("pos(bombeiro," + bombLoc.x + "," + bombLoc.y + ")");

		addPercept(pos1);
		addPercept(pos2);
		addPercept(pos3);
		addPercept(pos4);
	}

	class ModeloAmbGraf extends GridWorldModel {

		public ModeloAmbGraf() {
			super(gridSize, gridSize, 4);

			try {
				setAgPos(0, 0, 0);

				Location piro = new Location(gridSize / 2, gridSize / 2);
				Location pL = new Location(2, 5);
				Location bomb = new Location (7,3);

				setAgPos(1, piro);
				
				setAgPos(2,pL);
				
				setAgPos(3, bomb);
				

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		void proximaCasa() throws Exception {

			Location walkerLoc = getAgPos(0);

			walkerLoc.x++;

			if (walkerLoc.x == getWidth()) {
				walkerLoc.x = 0;
				walkerLoc.y++;
			}

			if (walkerLoc.y == getHeight()) {
				walkerLoc.x = 0;
				walkerLoc.y=0;
			}

			setAgPos(0, walkerLoc);
			
		}

		void andaPiro() {
			Random r = new Random();
			Location pL = getAgPos(1);
			int x = r.nextInt(10);
			int y = r.nextInt(10);
			if(pL.x<x) {
				pL.x++;
			}else if (pL.x>x) {
				pL.x--;
			}
			
			if(pL.y<y) {
				pL.y++;
			}else if(pL.y>y) {
				pL.y--;
			}
			
			setAgPos(1, pL);
		}
		
		
		/**
		 * Ir ate onde o piro foi visto.
		 * @param x
		 * @param y
		 */
		void andaPolice(int x, int y) {
			
			Location poliLoc = getAgPos(2);
			setAgPos(2,  new Location(x, poliLoc.y));
			Location poliLoc2 = getAgPos(2);
			setAgPos(2,  new Location(poliLoc2.x, y));
			
		}
		
		
		/**
		 * Ir ate o incedio.
		 * @param x
		 * @param y
		 */
		void andaBomb(int x, int y) {
			
			Location bombLoc = getAgPos(3);
			setAgPos(2,  new Location(x, bombLoc.y));
			Location bombLoc2 = getAgPos(2);
			setAgPos(2,  new Location(bombLoc2.x, y));
			
		}
		
		
		
	}

	class VisaoAmbGraf extends GridWorldView implements Serializable {

		static final long serialVersionUID = 10L;
		public VisaoAmbGraf(ModeloAmbGraf model) {

			super(model, "Mundo Andarilho", 1000);

			defaultFont = new Font("Arial", Font.BOLD, 18); // change default font

			setVisible(true);

			repaint();
		}

		@Override
		public void draw(Graphics g, int x, int y, int object) {

			switch (object) {

			case AmbGraf.randFire:
				DesenhaIncendio(g, x, y);
				break;
			}
		}

		public void DesenhaIncendio(Graphics g, int x, int y) {

			super.drawObstacle(g, x, y);
			g.setColor(Color.yellow);
			drawString(g, x, y, defaultFont, "FOGO!");
		}

		@Override
		public void drawAgent(Graphics g, int x, int y, Color c, int id) {
			
			if (id == 0) {

				String label = "Walker";
				c = Color.BLUE;
				super.drawAgent(g, x, y, c, -1);
				g.setColor(Color.black);
				super.drawString(g, x, y, defaultFont, label);
				setVisible(true);
			}

			if (id == 1) {

				String label = "Piro";
				c = Color.RED;
				super.drawAgent(g, x, y, c, -1);
				g.setColor(Color.black);
				super.drawString(g, x, y, defaultFont, label);
				setVisible(true);
			}
			if (id == 2) {

				String label = "Police";
				c = Color.YELLOW;
				super.drawAgent(g, x, y, c, -1);
				g.setColor(Color.black);
				super.drawString(g, x, y, defaultFont, label);
				setVisible(true);
			}
			
			if (id == 3) {

				String label = "Bomb";
				c = Color.ORANGE;
				super.drawAgent(g, x, y, c, -1);
				g.setColor(Color.black);
				super.drawString(g, x, y, defaultFont, label);
				setVisible(true);
			}
		}

	}
}