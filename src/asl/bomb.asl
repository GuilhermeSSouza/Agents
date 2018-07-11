// Agent bomb in project agent

/* Initial beliefs and rules */




/* Initial goals*/ 
+apaga[source(walker)] : true
	<- 	.print ("CHAMANDO OBJETIVO APAGAR FOGO");
		!apagarFogo.
		
		
+!apagarFogo:true
	<- .wait(300);
		.print("APAGANDO INCENDIO !!!!!");
		apFogo.
	
	
//+pos(X) : true
//	<- 	!mover.
	
//+!mover : true
//	<- 	.wait (500);
//		andaBomb.
	