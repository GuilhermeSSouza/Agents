// Agent piro in project grafico

+pos(L, X, Y) : true
	<- 	!mover (X, Y).
	
+!mover (X, Y) : X < 9
	<- 	.wait (3000);
		andaPiro.

+!mover (X, Y) : X == 9 & Y < 9
	<- 	.wait (3000);
		andaPiro.

+!mover (X, Y) : true.
