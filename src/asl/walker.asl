// Agent walker in project graficTest

+pos(L, X, Y) : true
	<- 	!mover (X, Y).
	

+fogo(X) : true 
	<-	.print ("CHAMANDO BOMBEIRO");
		.send(bomb, achieve, apaga).


//+fogo(X) : true
//	<- .wait(300);
//	 .print ("CHAMA O BOMBEIRO!");
//	 !chamar.	
	
	
//+!chamar:true 
//	<-	.print ("chamando bombeiro");
//		.send(bomb, achieve, apaga).
	

+!mover (X, Y) : X < 9
	<- 	.wait (300);
		proximaCasa.
		

+!mover (X, Y) : X == 9 & Y < 9
	<- 	.wait (300);
		proximaCasa.
		

+!mover (X, Y) : true.
		