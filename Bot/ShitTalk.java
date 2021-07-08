package Bot;

import java.util.ArrayList;

import bwapi.Color;
import bwapi.Game;
import bwapi.Position;
import bwapi.Text;
import bwapi.Unit;

public class ShitTalk {
	Unit u;
	String message;
	bwapi.Text color;
	int drawTill;
	Game game;
	Position draw;
	int startAt = 0;
	
	public ShitTalk(Unit a, String s, Text d, Integer f, Game g, Position h){
		this.u = a;
		this.message = s;
		this.color = d;
		this.drawTill = f;
		this.game = g;
		this.draw = h;
	}
	
	public ShitTalk(Unit a, String s, Text d, Integer f, Game g, Position h, Integer j){
		this.u = a;
		this.message = s;
		this.color = d;
		this.drawTill = f;
		this.game = g;
		this.startAt = j;
		this.draw = h;
	}
	
	
	void goAhead(){
		game.drawTextMap(this.u.getX(), this.u.getY() + 10, Text.formatText(this.message, this.color));
	}
	
	

	
}
