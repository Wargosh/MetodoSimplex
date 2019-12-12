package metodosimplex;

import java.util.ArrayList;

/**
 * @author Wargosh
 */
public class MetodoSimplex {

    public static String opcion = "MINIMIZAR"; // MAXIMIZAR o MINIMIZAR
    public static ArrayList<Ecuacion> ecuaciones = new ArrayList<>();

    public static void main(String[] args) {
        Interfaz form = new Interfaz();
        form.setVisible(true);
//        Ecuacion funObj = new Ecuacion(); // Funcion Objetivo
//        funObj.incognita.add(2d);
//        funObj.incognita.add(3d);
//        funObj.incognita.add(3d);
//
//        Ecuacion r1 = new Ecuacion(); // Restriccion 1
//        r1.incognita.add(3d);
//        r1.incognita.add(2d);
//        r1.incognita.add(0d);
//        r1.condicion = -1; // menor igual
//        r1.igualdad = 60d;
//
//        Ecuacion r2 = new Ecuacion(); // Restriccion 2
//        r2.incognita.add(-1d);
//        r2.incognita.add(1d);
//        r2.incognita.add(4d);
//        r2.condicion = -1; // menor igual
//        r2.igualdad = 10d;
//
//        Ecuacion r3 = new Ecuacion(); // Restriccion 3
//        r3.incognita.add(2d);
//        r3.incognita.add(-2d);
//        r3.incognita.add(5d);
//        r3.condicion = -1; // menor igual
//        r3.igualdad = 50d;
        
        Ecuacion funObj = new Ecuacion(); // Funcion Objetivo
        funObj.incognita.add(2d);
        funObj.incognita.add(4d);
        funObj.incognita.add(-4d);
        funObj.incognita.add(7d);

        Ecuacion r1 = new Ecuacion(); // Restriccion 1
        r1.incognita.add(8d);
        r1.incognita.add(-2d);
        r1.incognita.add(1d);
        r1.incognita.add(-1d);
        r1.condicion = -1; // menor igual
        r1.igualdad = 50d;

        Ecuacion r2 = new Ecuacion(); // Restriccion 2
        r2.incognita.add(3d);
        r2.incognita.add(5d);
        r2.incognita.add(0d);
        r2.incognita.add(2d);
        r2.condicion = -1; // menor igual
        r2.igualdad = 150d;
        
        Ecuacion r3 = new Ecuacion(); // Restriccion 3
        r3.incognita.add(1d);
        r3.incognita.add(-1d);
        r3.incognita.add(2d);
        r3.incognita.add(-4d);
        r3.condicion = -1; // menor igual
        r3.igualdad = 100d;

        // agregar funcion objetivo y restricciones a la lista
        ecuaciones.add(funObj);
        ecuaciones.add(r1);
        ecuaciones.add(r2);
        ecuaciones.add(r3);

        Presentar();
        EstandarizarEcuaciones();
        FormatFuncObjetivo();
        PresentarMatrix();
        ResolverEcuaciones();
    }

    public static void ResolverEcuaciones() {
        while (VerificarFinal()) {
            int[] array = EncontrarPivoteEcuacion();
            // verifica si el nuevo pivote ya esta en 1, sino lo simplifica a 1
            if (ecuaciones.get(array[1]).incognita.get(array[0]) != 1) {
                // dividir la ecuacion para el pivote (de esta manera hacer el pivote a 1) 
                Double auxPivot = ecuaciones.get(array[1]).incognita.get(array[0]);
                ecuaciones.get(array[1]).igualdad = ecuaciones.get(array[1]).igualdad / auxPivot;
                for (int i = 0; i < ecuaciones.get(array[1]).incognita.size(); i++) {
                    ecuaciones.get(array[1]).incognita.set(i, ecuaciones.get(array[1]).incognita.get(i) / auxPivot);
                }
            }
            // simplificar las otras ecuaciones
            for (int i = 0; i < ecuaciones.size(); i++) { // recorre todas las ecuaciones
                if (i != array[1]) { // evitar la ecuación del pivote actual
                    Double auxPivot = ecuaciones.get(i).incognita.get(array[0]);
                    auxPivot *= -1;
                    ecuaciones.get(i).igualdad = (ecuaciones.get(array[1]).igualdad * auxPivot) + ecuaciones.get(i).igualdad;
                    for (int j = 0; j < ecuaciones.get(i).incognita.size(); j++) {
                        ecuaciones.get(i).incognita.set(j, (ecuaciones.get(array[1]).incognita.get(j) * auxPivot) + ecuaciones.get(i).incognita.get(j));
                    }
                }
            }
            PresentarMatrix();
        }
    }
    
    public static boolean VerificarFinal() {
        for (int i = 0; i < ecuaciones.get(0).incognita.size(); i++) {
            if (ecuaciones.get(0).incognita.get(i) < 0) { // si hay almenos un negativo en la Función Objetivo
                return true; // Continua el algoritmo
            }
        }
        
        return false; // Termina algoritmo
    }

    public static int[] EncontrarPivoteEcuacion() {
        // verifica si existen negativos en la funcion objetivo
        Double menor = 0d;
        int indice = 0;
        for (int i = 0; i < ecuaciones.get(0).incognita.size(); i++) {
            if (ecuaciones.get(0).incognita.get(i) < menor) {
                menor = ecuaciones.get(0).incognita.get(i);
                indice = i;
            }
        }

        // Encontrar la razón de la columna
        Double razon = 0d;
                int ind = 0;
        menor = Double.MAX_VALUE;
        for (int i = 1; i < ecuaciones.size(); i++) { // Recorrer restricciones
            if (ecuaciones.get(i).incognita.get(indice) > 0) { // solo si es positivo
                razon = ecuaciones.get(i).igualdad / ecuaciones.get(i).incognita.get(indice);
                System.out.println("razón ecuación (" + i + ") es: " + razon);
                if (razon < menor) {
                    menor = razon;
                    ind = i;
                }
            }
        }
        ecuaciones.get(ind).pivote = indice;
        //System.out.println("ind: " + ind + " indice: " + indice + " Pivot: " + indice);
        System.out.println("Pivote elegido es: X" + (indice + 1) + "  en la ecuación: (" + ind + ")");
        int[] array = new int[2];
        array[0] = indice;
        array[1] = ind;
        return array;
    }

    public static void FormatFuncObjetivo() {
        if (opcion == "MAXIMIZAR") { // Multiplica toda la ecuacion por -1
            ecuaciones.get(0).igualdad *= -1;
            for (int i = 0; i < ecuaciones.get(0).incognita.size(); i++) {
                ecuaciones.get(0).incognita.set(i, ecuaciones.get(0).incognita.get(i) * -1);
            }
        }
    }

    public static void EstandarizarEcuaciones() {
        System.out.println("");
        for (int i = 1; i < ecuaciones.size(); i++) { // recorrer restricciones
            if (ecuaciones.get(i).condicion == -1) {
                ecuaciones.get(i).condicion = 0; // cambia la condicion (De menor_igual a igual)
                ecuaciones.get(i).incognita.add(1d); // agrega una incognita auxiliar
                ecuaciones.get(i).pivote = ecuaciones.get(i).incognita.size() - 1; // obtiene la incognita creada
                for (int j = 0; j < ecuaciones.size(); j++) { // agregar incognita en las demas ecuaciones
                    if (j != i) { // que no sea la misma ecuacion...
                        ecuaciones.get(j).incognita.add(0d);
                    }
                }
            } else if (ecuaciones.get(i).condicion == 1) {
                ecuaciones.get(i).condicion = 0; // cambia la condicion (De menor_igual a igual)
                ecuaciones.get(i).incognita.add(-1d); // agrega una incognita auxiliar
                for (int j = 0; j < ecuaciones.size(); j++) { // agregar incognita en las demas ecuaciones
                    if (j != i) { // que no sea la misma ecuacion...
                        ecuaciones.get(j).incognita.add(0d);
                    }
                }
            } else {
                continue;
            }
        }
        Presentar();
    }

    public static void Presentar() {
        System.out.println("MAXIMIZAR:");
        String msj = "";
        for (int i = 0; i < ecuaciones.get(0).incognita.size(); i++) {
            if (i == 0) {
                msj += ecuaciones.get(0).incognita.get(i) + "X1 ";
            } else {
                msj += "+ " + ecuaciones.get(0).incognita.get(i) + "X" + (i + 1) + " ";
            }
        }
        System.out.println(msj);
        System.out.println("RESTRICCIONES:");
        for (int i = 1; i < ecuaciones.size(); i++) { // imprimir restricciones
            msj = "";
            for (int j = 0; j < ecuaciones.get(i).incognita.size(); j++) {
                if (j == 0) {
                    msj += ecuaciones.get(i).incognita.get(j) + "X1 ";
                } else {
                    msj += "+ " + ecuaciones.get(i).incognita.get(j) + "X" + (j + 1) + " ";
                }
            }
            if (ecuaciones.get(i).condicion == -1) {
                msj += "<= ";
            } else if (ecuaciones.get(i).condicion == 0) {
                msj += "= ";
            } else {
                msj += ">= ";
            }
            msj += ecuaciones.get(i).igualdad;
            System.out.println(msj);
        }
        System.out.println("");
    }

    public static void PresentarMatrix() {
        String msj = "|||";
        for (int i = 0; i < ecuaciones.get(0).incognita.size(); i++) {
            msj += "\tX" + (i + 1);
        }
        System.out.println(msj);
        for (int i = 1; i < ecuaciones.size(); i++) { // imprimir restricciones
            msj = "X" + (ecuaciones.get(i).pivote + 1);
            for (int j = 0; j < ecuaciones.get(i).incognita.size(); j++) {
                if (j == 0) {
                    msj += "\t" + ecuaciones.get(i).incognita.get(j);
                } else {
                    msj += "\t" + ecuaciones.get(i).incognita.get(j);
                }
            }
            msj += "\t" + ecuaciones.get(i).igualdad + "\t(" + i + ")";
            System.out.println(msj);
        }
        msj = " Z";
        for (int i = 0; i < ecuaciones.get(0).incognita.size(); i++) {
            msj += "\t" + ecuaciones.get(0).incognita.get(i);
        }
        msj += "\t" + ecuaciones.get(0).igualdad;
        System.out.println(msj);
        System.out.println("");
    }
}
