package metodosimplex;

import java.util.ArrayList;

/**
 * @author Wargosh
 */
public class Ecuacion {
    public ArrayList<Double> incognita = new ArrayList(); // numero de incognitas de la ecuacion
    public int condicion = 0; // [-1 = menor_igual, 0 = igual, 1 = mayor_igual]
    public Double igualdad = 0d; // parte derecha de la ecuacion (despues del igual)
    public int pivote = 0; // almacena el indice del pivote actual
}