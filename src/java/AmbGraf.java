
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
	public Location piroLocal = new Location(2, 8);

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

	
	/**
	 * Finaliza a simulação quando o policial captura o piro
	 */
	@Override
	public void stop() {
		super.stop();
	}

	
	
	/**
	 * Iniciando as percepções dos agentes
	 */
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
			
			/**
			 * Passamos o tamanho do ambiente e a quantidade maxima de agentes que existiram dentro dele
			 */
			super(gridSize, gridSize, 4);        

			try {
				setAgPos(0, 1, 0);                                                          //Posição inical do civil ao inicar a simulação

				Location piro = new Location(gridSize / 2, gridSize / 2);                   //Localização inial do piro
				Location pL = new Location(2, 8);											//Localização inicail do Policial
				Location bomb = new Location(0, 0);											//Localização inicaial do Bomb

				
				/**
				 * Colocando cada um deles dentro do gridWorld
				 */
				setAgPos(1, piro);
				setAgPos(2, pL);
				setAgPos(3, bomb);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		
		
		/**
		 * Método de movimentação do civil
		 */
		void proximaCasa() {

			Location walkerLoc = getAgPos(0);												//obtem a posição do civil no grid

			walkerLoc.x++;

			if (walkerLoc.x == getWidth()) {
				walkerLoc.x = 0;
				walkerLoc.y++;
			}

			if (walkerLoc.y == getHeight()) {
				walkerLoc.x = 0;
				walkerLoc.y = 0;
			}
			
			
			/**
			 * Verifica se não tem fogo na proxima casa para onde o civil esta tentando ir:
			 *  Se true -> Chama bombeiro, se tiver fogo (se false o !hasObject ele chama o bomb para o local)
			 */
			if (!hasObject(randFire, walkerLoc)) {                                        
				
																														//parametros para que o agente acredite na nova crença
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
		
		
		/**
		 * Teste que verifica se existe mais de um agente na mesma casa; testa se o agente é um piro. Quem usa esse metodo é o civil, bomb e Policia.
		 * @param posi
		 * @return
		 */
		boolean testPosi(Location posi) {

			Location piroLoc = getAgPos(1);
			if (piroLoc.x == posi.x & piroLoc.y == posi.y) {

				System.out.println("ESTÃO NO MESMO LUGAR");

				return true;
			}

			System.out.println("ESTÂO EM LUGARES DIFERENTES");
			return false;

		}

		
		/**
		 * Metodo de movimentação do piro, escolhe aleatoriamente a proxima casa a ir
		 */
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
																	
				
				if (!model.hasObject(randFire, pL)) {							  //Se a casa já tiver fogo ele segue procurando outras casas para colocar fogo
					if (randFire <= r.nextInt(100)) {                             //Testa se aquela casa ira ou não incediar            
					add(randFire, getAgPos(1));									  //Add fogo,					
					setAgPos(1, pL);											  // Vai para proxima casa sem fogo escolida
				}
			} else {

			}

					/**
					 * Passando dados para que o agente passe a acreditar na nova crença 															    
					 */
			contapiro++;            
			Literal pos10 = Literal.parseLiteral("incendio(" + contapiro + ")");
			System.out.println("\nPos2 = " + pos10 + "\n");
			addPercept(pos10);

		}
 
		
		
		/**
		 * O andaPolice é o conjunto de ações do policial, ele vai ate o local onde foi chamado
		 * @throws Exception
		 */
		void andaPolice() throws Exception {

			
			Location poliLoc = getAgPos(2);
			if(hasObject(randFire, poliLoc)) {                                             
				

				Literal fogo2 = Literal.parseLiteral("incendio(" + contaBomb + ")");
				fogoLocal = poliLoc;
				addPercept(fogo2);

			}
			andar(piroLocal, 2);                                                          //Recebe o local do chamado seja do civil, seja do bomb
			if (testPosi(getAgPos(2))) {					
				
				System.out.println("PEGUEI O SAFADO");									 //Grito de euforia ao comprir seu trabalho! ;)
				 
				stop();																	 //Finaliza a aplicação
			}

		}

		/**
		 * Apgar o incendio quando soliciado
		 */

		void apFogo() {		
		
			andar(fogoLocal, 3);	                                       //Anda ate o local
			if (testPosi(getAgPos(2))) {								   // Teste se o piro foi visto ou não para chamar a policia

				piroLocal = getAgPos(3);
			}
			
			remove(randFire, fogoLocal);                                  //remove o fogo do local solicitado e do local onde está, caso o piro coloque fogo na casa

			
			if(!hasObject(randFire, fogoLocal)) {                          //Diz ao civil que o fogo foi removido e ele pode andar
			proximaCasa();
			}                                                

		}

		void andaBomb() throws InterruptedException {

			setAgPos(3, fogoLocal);
		}

	
	
	
		
		/**
		 * Metodo generico que é usado pelo Bomb, e pelo Policia para se locomver pelo mundo
		 * @param posi possição onde foi chamado, fogo ou piro avistado pelo civil ou pelo bomb
		 * @param i, é o agent que ira relaizar a ação de se locomover
		 */
	    void andar(Location posi, int i) {

			Location Loc = getAgPos(i);                                                             //posição original de onde o agente está antes de se mover
			Location LocLinha = new Location(posi.x, Loc.y);                                        //posição intermediaria dno caminho que ele ira percorrer.

			setAgPos(i, LocLinha);                                                                  //ele se move até a possição intermediaria
			
			try {
				Thread.sleep(500);                                                                  // sem o sleep a ação ocorrer muito rapida, impossivél ver ele se movendo
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Loc = getAgPos(i);                                                                      //posição do agente apos primeiro movimento
			LocLinha = new Location(Loc.x, posi.y);                                                 //posição final no grid
			setAgPos(i, LocLinha);   	                                                            //chegando na possição onde foi chamado
	    	
	    	
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