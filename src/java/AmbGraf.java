
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
	public static final Literal walker = Literal.parseLiteral("proximaCasa");
	/* public static final Literal civilFogo = Literal.parseLiteral("apFogo"); */
	public static final Term piro = Literal.parseLiteral("andaPiro");
	public static final Literal bomb = Literal.parseLiteral("apFogo");
	public static final Literal policia = Literal.parseLiteral("andaPolice");

	private int contapiro = 0;
	private int contaBomb = 0;
	private int contaPoli = 0;
	public Location fogoLocal = new Location(0, 0);
	public Location piroLocal = new Location(2, 5);

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

			if (action.equals(walker)) {

				model.proximaCasa();

			} else if (action.equals(piro)) {

				model.andaPiro();

			} else if (action.equals(bomb)) {

				model.apFogo();
			} else if (action.equals(policia)) {
				model.andaPolice();
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

	@Override
	public void stop() {
		super.stop();
	}

	void updatePercepts() {
		clearPercepts();

		Location walkerLoc = model.getAgPos(0);

		Literal pos1 = Literal.parseLiteral("local(walker," + walkerLoc.x + "," + walkerLoc.y + ")");
		Literal pos2 = Literal.parseLiteral("incendio(" + contapiro + ")");
		Literal pos3 = Literal.parseLiteral("prendendo(" + contaPoli + ")");

		// + "piro," + piroLoc.x + "," + piroLoc.y + ")");
		// Literal pos3 = Literal.parseLiteral("police(" + contaPoli + ")");
		// Literal pos4 = Literal.parseLiteral("bombeiro("+ contaBomb + ")");

		addPercept(pos1);
		addPercept(pos2);
		addPercept(pos3);
		// addPercept(pos4);

	}

	class ModeloAmbGraf extends GridWorldModel {

		public ModeloAmbGraf() {
			super(gridSize, gridSize, 4);

			try {
				setAgPos(0, 1, 0);

				Location piro = new Location(gridSize / 2, gridSize / 2);
				Location pL = new Location(2, 5);
				Location bomb = new Location(0, 0);

				setAgPos(1, piro);

				setAgPos(2, pL);

				setAgPos(3, bomb);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		void proximaCasa() {

			Location walkerLoc = getAgPos(0);

			walkerLoc.x++;

			if (walkerLoc.x == getWidth()) {
				walkerLoc.x = 0;
				walkerLoc.y++;
			}

			if (walkerLoc.y == getHeight()) {
				walkerLoc.x = 0;
				walkerLoc.y = 0;
			}

			if (!hasObject(randFire, walkerLoc)) {

				setAgPos(0, walkerLoc);
				Literal novoWalker = Literal.parseLiteral("local(walker," + walkerLoc.x + "," + walkerLoc.y + ")");
				System.out.println(novoWalker);
				addPercept(novoWalker);
			} else {

				
				Literal fogo = Literal.parseLiteral("incendio(" + contaBomb + ")");
				fogoLocal = walkerLoc;
				addPercept(fogo);

			}

			System.out.print("AVISTADO");
			Literal pegado = Literal.parseLiteral("prendendo(" + contaPoli + ")");
			contaPoli++;
			addPercept(pegado);
			if (testPosi(getAgPos(0))) {
				piroLocal = walkerLoc;

			}

		}

		boolean testPosi(Location posi) {

			Location piroLoc = getAgPos(1);
			if (piroLoc.x == posi.x & piroLoc.y == posi.y) {

				System.out.println("ESTÃO NO MESMO LUGAR");

				return true;
			}

			System.out.println("ESTÂO EM LUGARES DIFERENTES");
			return false;

		}

		void andaPiro() {
			Random r = new Random();
			Location pL = getAgPos(1);
			int x = r.nextInt(10);
			int y = r.nextInt(10);

			if (pL.x < x) {
				pL.x++;
			} else if (pL.x > x) {
				pL.x--;
			}

			if (pL.y < y) {
				pL.y++;
			} else if (pL.y > y) {
				pL.y--;
			}
			if (randFire <= r.nextInt(100)) {
				if (!model.hasObject(randFire, pL)) {
					add(randFire, pL.x, pL.y);
				}
			} else {

			}

			setAgPos(1, pL);
			contapiro++;
			Literal pos10 = Literal.parseLiteral("incendio(" + contapiro + ")");
			System.out.println("\nPos2 = " + pos10 + "\n");
			addPercept(pos10);

		}

		/**
		 * Ir ate onde o piro foi visto.
		 * 
		 * @param x
		 * @param y
		 */
		void andaPolice() throws Exception {

			
			Location poliLoc = getAgPos(2);
			if(hasObject(randFire, poliLoc)) {
				

				Literal fogo2 = Literal.parseLiteral("incendio(" + contaBomb + ")");
				fogoLocal = poliLoc;
				addPercept(fogo2);

			}
			setAgPos(2, piroLocal);
			if (testPosi(getAgPos(2))) {

				System.out.println("PEGUEI O SAFADO");
				stop();
			}

		}

		/**
		 * Ir ate o incendio.
		 * 
		 * @param x
		 * @param y
		 */

		void apFogo() {

			
			try {
				andaBomb();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			remove(randFire, fogoLocal);

			proximaCasa();

		}

		void andaBomb() throws InterruptedException {

			setAgPos(3, fogoLocal);
		}

	
	
	
	    void andar(Location posi, int i) {

			Location Loc = getAgPos(i);

			Loc.x++;

			if (Loc.x == getWidth()) {
				Loc.x = 0;
				Loc.y++;
			}

			if (Loc.y == getHeight()) {
				Loc.x = 0;
				Loc.y = 0;
			}
			
			setAgPos(i, Loc);
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	
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
				c = Color.LIGHT_GRAY;
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
				c = Color.BLUE;
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