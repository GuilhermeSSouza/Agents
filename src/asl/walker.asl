// Agent walker in project graficTest




!chamar.


+!chamar:true 
	<-.send(bomb, tell, apaga).

+fogo(L, X, Y) : true
	<- .wait(300);
	 !chamar.
	
+pos(L, X, Y) : true
	<- 	!mover (X, Y).

+!mover (X, Y) : X < 9
	<- 	.wait (300);
		proximaCasa.
		

+!mover (X, Y) : X == 9 & Y < 9
	<- 	.wait (300);
		proximaCasa.
		

+!mover (X, Y) : true.
		