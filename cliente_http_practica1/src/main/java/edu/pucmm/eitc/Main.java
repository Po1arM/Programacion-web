package edu.pucmm.eitc;


import org.jsoup.Connection.Response;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Ingrese un URL: ");

        Scanner scanner = new Scanner(System.in);
        String url =  scanner.nextLine();
        try {
            //Pedir el html del url proporcionado
            Document doc = Jsoup.connect(url).timeout(15000)
                                .followRedirects(true)
                                .get();

            //Cantidad de lineas del recurso
            long lines = doc.html().lines().count();
            System.out.print("\nLa cantidad de lineas en el documento es de: " + lines + " lineas");

            //Cantidad de parrafos
            Elements para = doc.select("p");
            System.out.println("\nLa cantidad de parrafos es de: " + para.size());

            //Cantidad de imagenes dentro de parrafos
            if (para.size() != 0) {
Elements img = doc.select("p img");
System.out.println("La cantidad de imagenes dentro de parrafos es de: " + img.size());
            }

            //Buscar los formularios tipo post
            Elements postForms = doc.select("form[method$=post]");
            System.out.println("La cantidad de formularios con metodo POST es de: " + postForms.size());

            //Buscar los formularios tipo get
            Elements getForms = doc.select("form[method$=get]");
            System.out.println("La cantidad de formularios con metodo GET es de: " + getForms.size() + "\n");

            //Imprimir los hijos de cada formulario
            inputs(postForms);
            inputs(getForms);

            //Solo entrar a la funcion si existen formularios de tipo post
            if (postForms.size() != 0) {
                post(postForms, url);
            }

        }catch (UnknownHostException error){
            System.out.println("La url es invalida");
            System.out.println(error.toString());
        }

    }

    public static void inputs(Elements forms) {
        int cont = 1;
        for (Element form : forms) {
            if (form.children().select("input").size() != 0) {
                System.out.println("Los inputs del formulario " + form.attr("method").toLowerCase() + " #" + cont + " son:");
                for (Element child : form.select("input")) {
                    System.out.println("\t" + child);
                }
            } else {
                System.out.println("El formulario " + form.attr("method").toLowerCase() + " #" + cont + " no tiene campos input como hijos");
            }
            cont++;
        }
    }


    public static void post(Elements forms,String url){
        int cont = 1;
        for (Element form: forms) {
            try {
                String postURL = form.attr("action");
                Response response;

                //Revisar si la direccion a la que se hace post es interna
                if(!postURL.contains("https")) {
                    postURL = url.concat(postURL);
                }
                response = Jsoup.connect(postURL)
                        .method(Method.POST)
                        .data("asignatura","practica1")
                        .header("matricula","20180520")
                        .execute();

                System.out.println("\nRespuesta del formulario POST #"+cont+": \n Status code=" + response.statusCode() + " ,URL=" + response.url());
                System.out.println(" Headers: " + response.headers());

            } catch (IOException error) {
                System.out.println("\nRespuesta: " + error);
            }
        }
    }
}