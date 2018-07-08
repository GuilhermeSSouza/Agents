// Agent piro in project grafico

+pos(X) : true
	<- 	!mover.
	
+!mover : true
	<- 	.wait (500);
		andaPiro.

