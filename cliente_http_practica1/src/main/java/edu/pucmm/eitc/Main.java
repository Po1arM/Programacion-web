package edu.pucmm.eitc;

import org.apache.http.client.HttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException {
        //System.out.println("Ingrese un URL: ");
        //Scanner scanner = new Scanner(System.in);
        //String url =  scanner.nextLine();
        String url = "https://acento.com.do/el-financiero/fase-para-trabajadores-del-sector-turismo-otorgara-subsidios-hasta-julio-8949165.html";
        Document doc = Jsoup.connect(url).get();

        //Cantidad de lineas del recurso
        long lines = doc.html().lines().count();
        System.out.print("La cantidad de lineas en el documento es de: " + lines + " lineas");

        //Cantidad de parrafos
        Elements para = doc.getElementsByTag("p");
        System.out.println("\nLa cantidad de parrafos es de: " + para.size());

        //Cantidad de imagenes dentro de parrafos
        if (para.size() != 0) {
            Elements img = doc.select("p img");
            System.out.println("La cantidad de imagenes dentro de parrafos es de: " + img.size());
        }

    }
}