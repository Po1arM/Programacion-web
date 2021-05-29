package edu.pucmm.eitc;

import org.apache.http.client.HttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException {
        //System.out.println("Ingrese un URL: ");
        //Scanner scanner = new Scanner(System.in);
        //String url =  scanner.nextLine();
        String url = "https://raw.githubusercontent.com/FGuzman29/Universidad/main/prueba.html?token=ALXFEXZ5FZZZU3FQOTTIKWTAXPDTG";
        Document doc = Jsoup.connect(url).get();

        //Cantidad de lineas del recurso
        long lines = doc.html().lines().count();
        System.out.print("La cantidad de lineas en el documento es de: " + lines + " lineas");

        //Cantidad de parrafos
        Elements para = doc.select("p");
        System.out.println("\nLa cantidad de parrafos es de: " + para.size());

        //Cantidad de imagenes dentro de parrafos
        if (para.size() != 0) {
            Elements img = doc.select("p img");
            System.out.println("La cantidad de imagenes dentro de parrafos es de: " + img.size());
        }

        Elements postForms = doc.select("form[method$=post]");
        System.out.println("La cantidad de formularios con metodo POST es de: " + postForms.size());

        Elements getForms = doc.select("form[method$=get]");
        System.out.println("La cantidad de formularios con metodo GET es de: " + getForms.size()+"\n");

        inputs(postForms);
        inputs(getForms);
    }

    public static void inputs(Elements forms){
        int cont = 1;
        for (Element form: forms) {
            if(form.childrenSize() != 0) {
                System.out.println("Los inputs del formulario " + form.attr("method").toLowerCase() + " #"+ cont+ " son:");
                for (Element child : form.children()) {
                    System.out.println("\t"+ child);
                }
            }else{
                System.out.println("El formulario " + form.attr("method").toLowerCase() +" #"+ cont +" no tiene campos input como hijos");
            }
            cont++;
        }
    }
}