package Bot;

import bwapi.Position;
import bwapi.Unit;




public class Kiter {
Unit me;
Unit target;
Data data;
int state;
Position tpos;
int id;

Kiter(Unit m, Unit t, Data d, int sta){
	this.me = m;
	this.target = t;
	this.data = d;
	this.tpos = t.getPosition();
	this.id = t.getID();
	this.state = sta;
}





}


