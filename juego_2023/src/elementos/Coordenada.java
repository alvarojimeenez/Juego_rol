package elementos;

import java.util.Objects;

import logicaJuego.Constantes;

public class Coordenada {
	
	private int x; 
	private int y;
	
	public Coordenada() {
		super();
	}
	public Coordenada(int x, int y) {
		if (this.x<0 || this.x>Constantes.TAMANNO || this.y<0 || this.y>Constantes.TAMANNO) {
			this.x = 0; 
			this.y = 0;
		}else {
			this.x = x;
			this.y = y;
		}
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}
	@Override
	public boolean equals(Object obj) {
		return this==obj || obj!=null && obj instanceof Coordenada && obj.hashCode()==this.hashCode();
	}
	
	public boolean goRight() {
		boolean derecha = true; 
		if (this.x==Constantes.TAMANNO) {
			derecha = false;
		}else {
			this.x++;
		}
		return derecha;
	}
	
	public boolean goLeft() {
		boolean izquierda = true;
		if (this.x == -(Constantes.TAMANNO)) {
			izquierda = false;
		}else {
			this.x--;
		}
		return izquierda;
	}
	
	public boolean goUp() {
		boolean arriba = true;
		if (this.y==-(Constantes.TAMANNO)) {
			arriba = false;
		}else {
			this.y--;
		}
		return arriba;
	}
	
	public boolean goDown() {
		boolean abajo = true; 
		if (this.y==(Constantes.TAMANNO)) {
			abajo = false;
		}else {
			this.y++;		
		}
		return abajo;
	}
	
	public Coordenada clone() {
		Coordenada c = new Coordenada();
		c=this;
		return c;
	}
	
	
}
