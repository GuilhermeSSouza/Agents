// Agent piro in project grafico

+incendio(X) : true
	<- 	!mover.
	
+!mover : true
	<- 	.wait (800);
		andaPiro.

