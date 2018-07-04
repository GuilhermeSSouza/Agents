// Agent walker in project graficTest

/* Initial beliefs and rules */

/* Initial goals */


+pos(L, X, Y) : true
	<- 	!mover (X, Y).

+!mover (X, Y) : X < 9
	<- 	.wait (300);
		proximaCasa.

+!mover (X, Y) : X == 9 & Y < 9
	<- 	.wait (300);
		proximaCasa.

+!mover (X, Y) : true.
		