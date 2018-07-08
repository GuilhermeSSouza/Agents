// Agent bomb in project agent

/* Initial beliefs and rules */



/* Initial goals*/ 
+apaga(X)
	[source(walker)]:true
	<- !apagarFogo.
		
		
+!apagarFogo:true
	<- .wait(300);
	   apFogo.
	
	
+pos(X) : true
	<- 	!mover.
	
+!mover : true
	<- 	.wait (500);
		andaBomb.
	