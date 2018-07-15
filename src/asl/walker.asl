// Agent walker in project graficTest

+local(L, X, Y) : true
	<- 	!mover (X, Y)
		.print ("ANDANDO CIVIL").
		
+incendio (X) : true
	<- .print("INCENDIO");
	   .send(bomb, achieve, apaga).
	   
	   
	   
+prendendo (X) : true
	<- .print("PULICAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
	.send(policia,achieve, pegaPi).


+pega (X) : true
	<- .print("PULICAA");
	   .send(policia, achieve, pegaPi).


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
		