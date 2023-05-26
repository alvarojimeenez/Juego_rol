package elementos;

import java.util.Random;

import logicaJuego.Constantes;

public class Jugador extends Element{
	
	private static ElementType elemento;
	private int dinero; 
	private int pociones; 
	private int gemas; 
	private PlayerType player;
	private Coordenada coordenada;
	
	
	public Coordenada getCoordenada() {
		return coordenada;
	}

	public void setCoordenada(Coordenada coordenada) {
		this.coordenada = coordenada;
	}

	public Jugador(PlayerType player) {
		super(ElementType.valueOf(player.name()));
		this.player = player;
		this.elemento= ElementType.valueOf(player.name());
	}

	public String getNombre() {
		return this.player.toString();
	}
	
	public int getFuerzaParaLuchar() {
		return new Random().nextInt(0,getFuerza());
	}
	
	private int getFuerza() {
		return this.player.getFuerza();
	}
	
	public int getMagiaParaLuchar() {
		return new Random().nextInt(0,getMagia());
	}
	
	private int getMagia() {
		return this.player.getMagia();
	}
	
	public int getVelocidadParaLuchar() {
		return new Random().nextInt(1,getVelocidad());
	}
	
	private int getVelocidad() {
		return this.player.getVelocidad();
	}

	//Getters and setters
	
	public int getDinero() {
		return dinero;
	}

	public void setDinero(int dinero) throws JugadorException {
		if (dinero<0) {
			throw new JugadorException("No puede introducir dinero negativo");
		}else {
			this.dinero = dinero;
		}
	}

	public int getPociones() {
		return pociones;
	}

	public void setPociones(int pociones) throws JugadorException {
		if (pociones<0) {
			throw new JugadorException("No puede introducir dinero negativo");
		}else {
			this.pociones = pociones;
		}
	}

	public int getGemas() {
		return gemas;
	}

	public void setGemas(int gemas) throws JugadorException {
		if (gemas<0) {
			throw new JugadorException("No puede introducir dinero negativo");
		}else {
			this.gemas = gemas;
		}
	}

	public PlayerType getPlayer() {
		return player;
	}
	
	public int lucha(Jugador jugador) throws JugadorException {
		int aux = Constantes.EMPATE;

        if(this.getFuerzaParaLuchar()>jugador.getFuerzaParaLuchar()) {
            if(jugador.getPociones()>0) {
                if(jugador.getDinero()>0) {
                	aux=Constantes.GANA_DINERO;
                }else {
                	aux=Constantes.GANA_USA_POCIMA;
                    jugador.setPociones(jugador.getPociones()-1);
                }

                this.setDinero(jugador.getDinero());
                jugador.setDinero(0);

            }else {
            	aux=Constantes.GANA_MUERE;
                this.setDinero(jugador.getDinero());
                jugador.setDinero(0);

            }

        }else {
            if(this.getPociones()>0) {
                if(this.getDinero()>0) {
                	aux=Constantes.GANA_DINERO;
                }else {
                	aux=Constantes.GANA_USA_POCIMA;
                    this.setPociones(this.getPociones()-1);
                }

                jugador.setDinero(this.getDinero());
                this.setDinero(0);

            }else {
            	aux=Constantes.GANA_MUERE;
                jugador.setDinero(this.getDinero());
                this.setDinero(0);

            }
        }

        return aux;
	}
	
	public int encuentraRoca() {
		int tmp = 0;
		if (this.gemas>0) {
			tmp = Constantes.ROMPE_ROCA_CON_GEMA;
		}else {
			if (this.getMagia()>=4) {
				tmp = Constantes.GANA_A_LA_ROCA;
			}else {
				tmp = Constantes.PIERDE_A_LA_ROCA;
			}
		}
		return tmp;
	}
	
	public void encuentraDinero() {
		this.dinero++;
	}
	
	public void encuentraPocion() {
		this.pociones++;
	}

	public void encuentraGema() {
		this.gemas++;
	}

	@Override
	public String toString() {
		return "Jugador: "+ this.player + " Dinero:" + dinero + " Pociones:" + pociones + " Gemas:" + gemas +"\n";
	}
	
	
	
	
	
	
	
	
}
