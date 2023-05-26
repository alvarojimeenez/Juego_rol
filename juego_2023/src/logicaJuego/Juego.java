package logicaJuego;

import static logicaJuego.JuegoUtils.crearSeparador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import elementos.Coordenada;
import elementos.Element;
import elementos.ElementType;
import elementos.Jugador;
import elementos.JugadorException;
import elementos.PlayerType;

public class Juego {

	private Map<Coordenada, Element> tablero;
	private List<Coordenada> coordenadaJugadores;
	private int jugadorJuega;
	private int dado; // Dado para ver los movimientos del jugador que juega
	private List<Jugador> jugadores;



	public Juego(PlayerType[] tipos) {
		super();
		this.tablero= new HashMap<>();
		this.coordenadaJugadores = new ArrayList<>();
		this.jugadores = new ArrayList<>();
		this.jugadorJuega = 0;
		for (PlayerType p : tipos) {
			Jugador j = new Jugador(p);
			this.jugadores.add(j);
		}
		annadirCoordenadasEnTablero();
		asignarCoordenadasAJugadores();
		annadirJugadoresTablero();
		annadirElementosRandomTablero();
	}
	
	/*
    
	elementosrandom();
	lo mismo pero metiendo elementos(comprobar que en la coordenada no haya jugadores)
	*/
	
	public void annadirCoordenadasEnTablero() {
		Coordenada aux = null; 
		for (int i = 0; i<10; i++) {
			for (int j = 0; j<10; j++) {
				aux = new Coordenada(i, j);
				this.tablero.put(aux, null);
			}
		}
	}
	
	public void asignarCoordenadasAJugadores() {
		Coordenada aux = null;
		int cont = 0;
		while (this.coordenadaJugadores.size()!=4) {
			aux = new Coordenada(new Random().nextInt(0,10),new Random().nextInt(0,10));
			if (!this.coordenadaJugadores.contains(aux)) {
				this.coordenadaJugadores.add(aux);
				this.jugadores.get(cont).setCoordenada(aux);
				cont++;
			}
		}
	}
	
	public void annadirJugadoresTablero() {
		for (Jugador j : this.jugadores) {
			this.tablero.put(j.getCoordenada(),j);
		}
	}
	
	public void annadirElementosRandomTablero() {
		Coordenada gema = null;
		Coordenada roca = null;
		Coordenada pocion = null;
		
		int contGemas = 0;
		int contRocas = 0;
		int contPociones = 0;
		
		while (contGemas!=2 && contRocas!=2 && contPociones!=2) {
			gema = new Coordenada(new Random().nextInt(0,10),new Random().nextInt(0,10));
			roca = new Coordenada(new Random().nextInt(0,10),new Random().nextInt(0,10));
			pocion = new Coordenada(new Random().nextInt(0,10),new Random().nextInt(0,10));
			
			if (this.tablero.get(gema) == null) {
				this.tablero.put(gema, new Element(ElementType.GEMA));
				contGemas++;
			}
			if (this.tablero.get(roca) == null) {
				this.tablero.put(roca, new Element(ElementType.ROCA));
				contRocas++;
			}
			if (this.tablero.get(pocion) == null) {
				this.tablero.put(pocion, new Element(ElementType.POCION));
				contPociones++;
			}
		}
	}
	
	
	/**
	 * Mueve el jugador en el tablero
	 * 
	 * @param direccion
	 * @return resultado de la operación
	 * @throws JuegoException
	 * @throws JugadorException
	 */
	public String moverJugador(char direccion) throws JuegoException, JugadorException {

		String resultado = "";
		Jugador jugador = (Jugador) this.tablero.get(this.coordenadaJugadores.get(jugadorJuega));

		Coordenada coordDestino = getNextPosition(direccion);

		// Tengo que ver que hay en la nueva casilla
		Element elemento = this.tablero.get(coordDestino);

		if (elemento != null) { // Hay algo en la casilla
			if (elemento instanceof Jugador) {
				// Después de la lucha los jugadores no se mueven
				resultado = luchar(resultado, jugador, coordDestino, elemento);
				
			} else if (elemento.getType() == ElementType.ROCA) {
				resultado = encuentraRoca(resultado, jugador, coordDestino);
				
			} else if (elemento.getType() == ElementType.GEMA) {
				jugador.encuentraGema();
				this.cambiaJugadorAPosicion(coordDestino);

			} else if (elemento.getType() == ElementType.DINERO) {
				jugador.encuentraDinero();
				this.cambiaJugadorAPosicion(coordDestino);

			} else if (elemento.getType() == ElementType.POCION) {
				jugador.encuentraPocion();
				this.cambiaJugadorAPosicion(coordDestino);
				
			}

		} else {
			this.cambiaJugadorAPosicion(coordDestino);
		}

		return resultado;
	}

	private String encuentraRoca(String resultado, Jugador jugador, Coordenada coordDestino) {
		int resultadoRoca = jugador.encuentraRoca();
		switch (resultadoRoca) {
				case Constantes.ROMPE_ROCA_CON_GEMA:
					resultado = String.format("El jugador %s rompe la roca con una gema.", jugador.getNombre());
					this.cambiaJugadorAPosicion(coordDestino);
					break;
		
				case Constantes.GANA_A_LA_ROCA:
					resultado = String.format("El jugador %s gana a la roca.", jugador.getNombre());
					this.cambiaJugadorAPosicion(coordDestino);
					break;
		
				case Constantes.PIERDE_A_LA_ROCA:
					resultado = String.format("El jugador %s pierde. No se mueve.", jugador.getNombre());
					break;
		}
		return resultado;
	}

	private String luchar(String resultado, Jugador jugador, Coordenada coordDestino, Element elemento) throws JugadorException {
		Jugador enemigo = (Jugador) elemento;
		int resultadoLucha = jugador.lucha(enemigo);
		switch (resultadoLucha) {
		case Constantes.EMPATE:
			resultado = "Empate entre los jugadores";
			break;
		case Constantes.GANA_USA_POCIMA:
			resultado = "El jugador " + jugador.getNombre() + " gana. Le quita una pócima al enemigo";
			break;
		case Constantes.GANA_DINERO:
			resultado = "El jugador " + jugador.getNombre() + " gana. Le quita el dinero al enemigo";
			break;
		case Constantes.GANA_MUERE:
			resultado = "El jugador " + jugador.getNombre() + " gana. El enemigo muere";
			this.eliminarJugador(coordDestino);
			// Si se elimina el jugador que juega el dado se pone a 0 para que no siga
			// tirando
			break;
		case Constantes.PIERDE_USA_POCIMA:
			resultado = "El enemigo " + enemigo.getNombre() + " gana. Le quita una pócima al jugador";
			break;
		case Constantes.PIERDE_DINERO:
			resultado = "El enemigo " + enemigo.getNombre() + " gana. Le quita el dinero al jugador";
			break;
		case Constantes.PIERDE_MUERE:
			resultado = "El enemigo " + enemigo.getNombre() + " gana. El jugador muere";
			this.eliminarJugador(this.coordenadaJugadores.get(jugadorJuega));
			dado = 0;
			// Decrementamos en uno el jugador, para que no se salte al siguiente
			// ya que al borrarlo el siguiente apunta al siguiente, y al incrementarlo
			// se le salta
			this.jugadorJuega--;
			break;
		}
		return resultado;
	}

	private Coordenada getNextPosition(char direction) throws JuegoException {
		Coordenada c = new Coordenada();
		this.tablero.put(this.jugadores.get(jugadorJuega).getCoordenada(), null);
		if (direction=='N' && obtenerCoordenadaJugadorJuega().goUp()) {
			c = obtenerCoordenadaJugadorJuega();
		}else if (direction=='S' && obtenerCoordenadaJugadorJuega().goDown()) {
			c = obtenerCoordenadaJugadorJuega();
		}else if (direction=='E' && obtenerCoordenadaJugadorJuega().goRight()) {
			c = obtenerCoordenadaJugadorJuega();
		}else if (direction=='O' && obtenerCoordenadaJugadorJuega().goLeft()){
			c = obtenerCoordenadaJugadorJuega();
		}else {
			throw new JuegoException("La dirección no es correcta");
		}
		return c;
	}

	private void cambiaJugadorAPosicion(Coordenada coordDestino) {
		this.jugadores.get(jugadorJuega).setCoordenada(coordDestino);
		this.tablero.put(coordDestino, this.jugadores.get(jugadorJuega));
		
	}


	private void eliminarJugador(Coordenada coordDestino) {
		for (Coordenada c : this.tablero.keySet()) {
			if (c.equals(coordDestino)) {
				this.jugadores.remove(this.tablero.get(c));
				this.coordenadaJugadores.remove(c);
				this.tablero.replace(c, null);
			}
		}
	}


	/**
	 * Escribe el tablero en formato no gráfico. Devuelve el string con la
	 * información
	 */
	@Override
	public String toString() {
		StringBuilder resul = new StringBuilder();
		resul.append(crearSeparador());
		resul.append("     |");

		for (int fila = 0; fila < Constantes.TAMANNO; fila++) {
			for (int columna = 0; columna < Constantes.TAMANNO; columna++) {
				Coordenada coor = new Coordenada(columna, fila);

				if (this.tablero.get(coor) != null) {
					/*
					 if (this.tablero.get(coor).getType() != null) {
						 resul.append(" "+ this.tablero.get(coor).getType().getSymbol()+ " ");
					 }
					*/ 
					resul.append(" " + this.tablero.get(coor).getType().getSymbol() + " "); // hay que quitar esto
				} else {
					resul.append("   ");
				}

				resul.append("|");
			}
			resul.append(System.lineSeparator()).append(crearSeparador()).append("     |");
		}
		resul.delete(resul.length() - 5, resul.length());
		return resul.toString();
	}


	public String imprimeValoresJugadores() {
		StringBuilder sb = new StringBuilder();
		for (Jugador j : this.jugadores) {
			sb.append(j.toString());
		}
		return sb.toString();
	}


	public String imprimeNombreJugadores() {
		StringBuilder sb = new StringBuilder();
		for (Jugador j : this.jugadores) {
			sb.append(j.getNombre());
		}
		return sb.toString();
	}

	public boolean isTerminado() {
		boolean tmp = false; 
		if (this.jugadores.size()==1) {
			tmp = true;
		}else {
			for (Jugador j : this.jugadores) {
				if (j.getDinero()==Constantes.NUM_DINERO) {
					tmp = true;
				}
			}
		}
		return tmp;
	}

	public int getValorDado() {
		return this.dado;
	}

	public void setDado() {
		for (Jugador j : this.jugadores) {
			if (j.getPlayer().equals(ElementType.GUERRERO)) {
				this.dado = new Random().nextInt(1,Constantes.GUERRERO_VELOCIDAD);
			}else if (j.getPlayer().equals(ElementType.OGRO)) {
				this.dado = new Random().nextInt(1,Constantes.OGRO_VELOCIDAD);
			}else if (j.getPlayer().equals(ElementType.ELFO)) {
				this.dado = new Random().nextInt(1,Constantes.ELFO_VELOCIDAD);
			}else {
				this.dado = new Random().nextInt(1,Constantes.MAGO_VELOCIDAD);
			}
		}
	}

	public String getNombreJugadorQueJuega() {
		return this.jugadores.get(jugadorJuega).getNombre();
	}

	public void proximoJugador() {
		this.jugadorJuega=(++jugadorJuega%this.jugadores.size());
	}

	public String getGanador() {
		StringBuilder sb = new StringBuilder();
		int mayorDinero = 0;
		if (isTerminado()) {
			for (Jugador j : this.jugadores) {
				if (j.getDinero()>mayorDinero) {
					mayorDinero = j.getDinero();
					sb.append(j.getNombre());
				}
			}
		}
		return sb.toString();
	}


	public Element obtenerElementoTablero(Coordenada coordenada) {
		return this.tablero.get(coordenada);
	}


	public Coordenada obtenerCoordenadaJugadorJuega() {
		return this.coordenadaJugadores.get(this.jugadorJuega);
		
	}


	public void decrementaDado() {
		this.dado--;
	}
}
