import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Clase Juego que simula el juego del Julepe.
 * 
 * @author Miguel Bayon
 */
public class Juego
{
    private Jugador[] jugadores;
    private Mazo mazo;
    private int paloQuePinta;


    /**
     * Constructor de la clase Juego
     *
     * @param numeroJugadores El número de jugadores que van a jugar
     * @param nombreJugadorHumano El nombre del jugador humano
     */
    public Juego(int numeroJugadores, String nombreJugadorHumano)
    {
        mazo = new Mazo();
        jugadores = new Jugador[numeroJugadores];

        ArrayList<String> posiblesNombres = new ArrayList<String>();
        posiblesNombres.add("Pepe");
        posiblesNombres.add("Maria");
        posiblesNombres.add("Juan");
        posiblesNombres.add("Luis");
        posiblesNombres.add("Marcos");
        posiblesNombres.add("Omar"); 
        posiblesNombres.add("Carlos");
        posiblesNombres.add("Azahara");  

        Jugador jugadorHumano = new Jugador(nombreJugadorHumano);
        jugadores[0] = jugadorHumano;
        System.out.println("Bienvenido a esta partida de julepe, " + nombreJugadorHumano);

        Random aleatorio = new Random();
        for (int i = 1; i < numeroJugadores; i++) {
            int posicionNombreElegido = aleatorio.nextInt(posiblesNombres.size());
            String nombreAleatorioElegido = posiblesNombres.get(posicionNombreElegido);
            posiblesNombres.remove(posicionNombreElegido);

            Jugador jugador = new Jugador(nombreAleatorioElegido);
            jugadores[i] = jugador;

        }

        System.out.println("Tus rivales son: ");
        for (int i = 1; i < jugadores.length; i++) {
            System.out.println(jugadores[i].getNombre());
        }
        System.out.println();
        
        jugar();
    }
    
    
    /**
     * Método que reparte 5 cartas a cada uno de los jugadores presentes en
     * la partida y elige un palo para que pinte.
     *
     * @return El palo que pinta tras repartir
     */
    private int repartir() 
    {
        mazo.barajar();

        Carta nuevaCarta = null;
        for (int cartaARepartir = 0; cartaARepartir < 5; cartaARepartir++) {            
            for (int jugadorActual = 0; jugadorActual < jugadores.length; jugadorActual++) {
                nuevaCarta = mazo.sacarCarta();
                jugadores[jugadorActual].recibirCarta(nuevaCarta);
            }
        }

        paloQuePinta = nuevaCarta.getPalo();
        switch (paloQuePinta) {
            case 0:
            System.out.println("Pintan oros");
            break;
            case 1:
            System.out.println("Pintan copas");
            break;
            case 2:
            System.out.println("Pintan espadas");
            break;
            case 3:
            System.out.println("Pintan bastos");
            break;
        }

        return paloQuePinta;           
    }
   


    /**
     * Devuelve la posición del jugador cuyo nombre se especifica como
     * parámetro.
     *
     * @param nombre El nombre del jugador a buscar
     * @return La posición del jugador buscado o -1 en caso de no hallarlo.
     */
    private int encontrarPosicionJugadorPorNombre(String nombre)
    {
        int posicionDelJugador = -1;
        int contador = 0;
        boolean buscando = true;
        while (contador < jugadores.length && buscando) {
            if (jugadores[contador].getNombre().equals(nombre)) {
                buscando = false;
                posicionDelJugador = contador;
            }
            contador++;
        }
        return posicionDelJugador;
    }
    
        
    /**
     * Desarrolla una partida de julepe teniendo en cuenta que el mazo y los
     * jugadores ya han sido creados. 
     * 
     * La partida se desarrolla conforme a las normas del julepe con la
     * excepción de que es el usuario humano quien lanza cada vez la primera
     * carta, independientemente de qué usuario haya ganado la baza anterior y,
     * además, los jugadores no humanos no siguen ningún criterio a la hora
     * de lanzar sus cartas, haciéndolo de manera aleatoria.
     * 
     * En concreto, el método de se encarga de:
     * 1. Repartir las cartas a los jugadores.
     * 2. Solicitar por teclado la carta que quiere lanzar el jugador humano.
     * 3. Lanzar una carta por cada jugador no humano.
     * 4. Darle la baza al jugador que la ha ganado.
     * 5. Informar de qué jugador ha ganado la baza.
     * 6. Repetir el proceso desde el punto 2 hasta que los jugadores hayan
     *    tirado todas sus cartas.
     * 7. Informar de cuántas bazas ha ganado el jugador humano.
     * 8. Indicar si el jugador humano "es julepe" (ha ganado menos de dos
     *    bazas) o "no es julepe".
     *
     */
    private void jugar()
    {
        repartir();
        Scanner entrada = new Scanner(System.in);
        boolean cartaTirada = false;
        int contadorLento = 0;
        while (contadorLento < 5) {
            Baza baza = new Baza(jugadores.length, paloQuePinta);
            while (!cartaTirada) {
                System.out.println("");
                jugadores[0].verCartasJugador();
                System.out.println("");
                System.out.println("�Que carta quieres tirar?");
                System.out.println("");
                String nombreCartaATirar = entrada.nextLine();
                System.out.println("");
                Carta cartaActual = jugadores[0].tirarCarta(nombreCartaATirar);
                if (cartaActual != null) {
                    baza.addCarta(cartaActual, jugadores[0].getNombre());
                    cartaTirada = true;
                }
                else {
                    System.out.println("");
                    System.out.println("No tienes esa carta, por favor tira una carta de la que dispongas");
                    System.out.println("");
                }
            }
            int contador = 1;
            while (contador < jugadores.length) {
                baza.addCarta(jugadores[contador].tirarCartaInteligentemente(baza.getPaloPrimeraCartaDeLaBaza(), baza.cartaQueVaGanandoLaBaza(), paloQuePinta), jugadores[contador].getNombre());
                contador++;
            }
            jugadores[encontrarPosicionJugadorPorNombre(baza.nombreJugadorQueVaGanandoLaBaza())].addBaza(baza);
            System.out.println("El jugador " + baza.nombreJugadorQueVaGanandoLaBaza() + " ha ganado esta baza.");
            contadorLento++;
            cartaTirada = false;
        }
        System.out.println("El jugador " + jugadores[0].getNombre() + " ha ganado " + jugadores[0].getNumeroBazasGanadas() + " bazas.");
        if (jugadores[0].getNumeroBazasGanadas() < 2) {
            System.out.println("");
            System.out.println("El jugador " + jugadores[0].getNombre() + " es julepe.");
            System.out.println("");
        }
        else {
            System.out.println("");
            System.out.println("El jugador " + jugadores[0].getNombre() + " no es julepe.");
            System.out.println(""); 
        }
    }    
}













