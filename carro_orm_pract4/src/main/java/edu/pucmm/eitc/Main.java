package edu.pucmm.eitc;

import edu.pucmm.eitc.encapsulaciones.CarroCompra;
import edu.pucmm.eitc.encapsulaciones.Producto;
import edu.pucmm.eitc.encapsulaciones.Usuario;
import edu.pucmm.eitc.encapsulaciones.VentasProductos;
import edu.pucmm.eitc.servicios.ServiceProduct;
import edu.pucmm.eitc.servicios.ServiceVentas;
import io.javalin.Javalin;

import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinVelocity;
import org.jasypt.util.text.AES256TextEncryptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main {

    private static String modoConexion = "";

    public static void main(String[] args){

        //Creacion de la controladora
        //Service service = Service.getInstance();

        //Inicializacion del servidor
        Javalin app = Javalin.create().start(5000);
        //Instanciacion del motor de plantillas a utilizar
        JavalinRenderer.register(JavalinVelocity.INSTANCE, ".vm");

        //Si el carrito no existe dentro de la sesion entonces se crea y se agrega como un atributo
        app.before(ctx -> {
            CarroCompra carrito = ctx.sessionAttribute("carrito");
            if(carrito == null){
                carrito = new CarroCompra();
            }
            ctx.sessionAttribute("carrito",carrito);

        });
        /*Ruta raiz
        * Muesta los productos disponibles para agragar al carrito*/
        app.get("/", ctx -> {
            CarroCompra carrito = ctx.sessionAttribute("carrito");

            List<Producto> productos = ServiceProduct.getInstance().findAll();
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("productos",productos);
            modelo.put("cantidad",carrito.getProductos().size());
            ctx.render("/publico/listadoProductos.vm", modelo);
        });

        /*Peticion que agrega un producto al carrito del usuario
        * Si el producto ya está en el carrito entonces se aumenta la cantidad que se quiere*/
        app.post("/comprar", ctx -> {
            CarroCompra carrito = ctx.sessionAttribute("carrito");

            Producto temp = carrito.getProductosPorID(ctx.formParam("id",Integer.class).get());
            if(temp == null){
                temp = ServiceProduct.getInstance().find(ctx.formParam("id", Integer.class).get());
                temp.setCantidad(ctx.formParam("cantidad",Integer.class).get() );
                carrito.addProducto(temp);
                ctx.sessionAttribute("carrito",carrito);
                ctx.redirect("/comprar");
            }else{
                int pos = carrito.getPos(ctx.formParam("id",Integer.class).get());
                temp.setCantidad(ctx.formParam("cantidad",Integer.class).get() + temp.getCantidad());
                carrito.cambiarProducto(temp,pos);
            }

            ctx.sessionAttribute("carrito",carrito);
            ctx.redirect("/comprar");
        });

        app.get("/comprar", ctx -> {
            ctx.redirect("/");
        });

        /*Carga la pestaña con todas las ventas realizadas
        * Si el usuario no se ha logeado entonces se redirige al log-in*/
        app.get("/ventas", ctx -> {
            if( ctx.cookie("usuario") == null || ctx.cookie("mist")== null){
                ctx.redirect("/autenti/ventas");
                return;
            } else{
                AES256TextEncryptor textEncryptor = new AES256TextEncryptor();
                textEncryptor.setPassword("myEncryptionPassword");
                String mist = textEncryptor.decrypt(ctx.cookie("mist"));
                Usuario aux = new Usuario(ctx.cookie("usuario"),mist);
                /*if(!service.autentificarUsuario(aux).equalsIgnoreCase("ADM")){
                    ctx.redirect("/autenti/ventas");
                    return;
                }*/
            }
            CarroCompra carrito = ctx.sessionAttribute("carrito");
            List<VentasProductos> ventas = ServiceVentas.getInstance().findAll();
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("ventas",ventas);
            modelo.put("cantidad",carrito.getProductos().size());

            ctx.render("/publico/ventas.vm",modelo);
        });



        /*Carga la ventana para hacer crud de los productos*/
        app.get("/productos", ctx -> {
            if( ctx.cookie("usuario") == null || ctx.cookie("mist")== null){
                ctx.redirect("/autenti/productos");
                return;
            } else{
                AES256TextEncryptor textEncryptor = new AES256TextEncryptor();
                textEncryptor.setPassword("myEncryptionPassword");
                String mist = textEncryptor.decrypt(ctx.cookie("mist"));
                Usuario aux = new Usuario(ctx.cookie("usuario"),mist);
                /*if(!service.autentificarUsuario(aux).equalsIgnoreCase("ADM")){
                    ctx.redirect("/autenti/ventas");
                    return;
                }*/
            }
            List<Producto> productos = ServiceProduct.getInstance().findAll();
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("productos",productos);
            CarroCompra carrito = ctx.sessionAttribute("carrito");
            modelo.put("cantidad",carrito.getProductos().size());
            ctx.render("/publico/productos.vm",modelo);
        });

        /*Carga la ventana para registrar un nuevo producto en el sistema
        * Envia un string accion para poder especificar lo que se va a realizar al momento de hacer post en el formulario
        * ya que se utiliza la misma vista que para editar un producto*/
        app.get("/registrar", ctx -> {
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("accion","/registrar");
            CarroCompra carrito = ctx.sessionAttribute("carrito");
            modelo.put("cantidad",carrito.getProductos().size());
            ctx.render("/publico/productoCE.vm",modelo);
        });

        /*Registra un producto en el sistema a partir de los valores del formulario*/
        app.post("/registrar", ctx -> {
            String nombre = ctx.formParam("nombre");
            int precio = ctx.formParam("precio",Integer.class).get();

            Producto temp = new Producto(nombre,precio);
            ServiceProduct.getInstance().create(temp);
            ctx.redirect("/productos");
        });

        /*Remueve un articulo de los disponibles a partir de su id*/
        app.get("/remover/:id", ctx -> {
            ServiceProduct.getInstance().delete(ctx.pathParam("id",Integer.class).get());
            ctx.redirect("/productos");
        });

        /*Permite editar un producto ya agregado
        * Se envia un string para determinar que se realizará una modificación luego del post*/
        app.get("/editar/:id", ctx -> {
            Producto temp = ServiceProduct.getInstance().find(ctx.pathParam("id", Integer.class).get());
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("producto",temp);
            modelo.put("accion","/editar/"+ctx.pathParam("id", Integer.class).get());

            CarroCompra carrito = ctx.sessionAttribute("carrito");
            modelo.put("cantidad",carrito.getProductos().size());
            ctx.render("/publico/productoCE.vm",modelo);
        });

        /*Post luego del formulario de modificar producto
        * Actualiza los valores a partir de lo enviado en el formulario*/
        app.post("/editar/:id", ctx -> {
            String nombre = ctx.formParam("nombre");
            int precio = ctx.formParam("precio",Integer.class).get();

            Producto temp = new Producto(nombre,precio);
            temp.setId(ctx.pathParam("id",Integer.class).get());
            ServiceProduct.getInstance().edit(temp);

            ctx.redirect("/productos");
        });

        /*Hace render al log-in
        * direc determina a que vista será rediccionado luego de autentificarse correctamente*/
        app.get("/autenti/:direc", ctx -> {
            String direc = ctx.pathParam("direc");
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("direc",direc);
            ctx.render("/publico/autentificacion.vm",modelo);
        });

        /*Post de autentificacion
        * redirige a la ventana especificada en el get*/
        app.post("/autenti/:direc",ctx -> {
            String usuario = ctx.formParam("usuario");
            String pass = ctx.formParam("password");
            String temp = ctx.pathParam("direc");
            String recordar = ctx.formParam("recordar");

            if(usuario == null || pass == null){
                ctx.redirect("/autenti/"+temp);
            }
            Usuario user = new Usuario(usuario,pass);
            //service.autentificarUsuario(user);
            AES256TextEncryptor textEncryptor = new AES256TextEncryptor();
            textEncryptor.setPassword("myEncryptionPassword");
            pass = textEncryptor.encrypt(pass);
            if(recordar != null){
                ctx.cookie("usuario", usuario,(3600*24*7));//Una semana en segundos
                ctx.cookie("mist", pass,(3600*24*7));
            }
            //Encriptar cookie
            ctx.cookie("usuario", usuario);
            ctx.cookie("mist", pass);

            ctx.redirect("/"+temp);

        });

        /*Carga el carrito pasando la lista de productos que se tiene dentro del carro*/
        app.get("/carrito", ctx -> {
            CarroCompra carrito = ctx.sessionAttribute("carrito");
            if(carrito == null){
                carrito = new CarroCompra();
            }
            ctx.sessionAttribute("carrito",carrito);
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("productos",carrito.getProductos());
            modelo.put("cantidad",carrito.getProductos().size());
            ctx.render("/publico/carrito.vm",modelo);
        });
        /*Elimina un producto del carrito a partir de su id*/
        app.get("/eliminar/:id", ctx -> {
            int id = ctx.pathParam("id", Integer.class).get();
            CarroCompra carrito = ctx.sessionAttribute("carrito");
            carrito.eliminarProductoPorId(id);

            ctx.sessionAttribute("carrito",carrito);
            ctx.redirect("/carrito");
        });

        /*Procesa la compra
        * crea un objeto venta
        * Limpia el carrito del usuario*/
        app.post("/procesar",ctx -> {
           CarroCompra carrito = ctx.sessionAttribute("carrito");
           if(carrito.getProductos().size() < 1){
               ctx.redirect("/carrito");
           }
           String nombre = ctx.formParam("nombre");
           VentasProductos venta = new VentasProductos(nombre,carrito.productos);
           ServiceVentas.getInstance().create(venta);
           carrito.borrarProductos();
           ctx.sessionAttribute("carrito",carrito);
           ctx.redirect("/comprar");
        });

        /*Limpia el carrito del usuario*/
        app.get("/limpiar", ctx -> {
            CarroCompra carrito = ctx.sessionAttribute("carrito");
            carrito.borrarProductos();

            ctx.redirect("/comprar");
        });

        app.get("/logout", ctx -> {
            if(ctx.cookie("usuario")!= null && ctx.cookie("mist")!= null){
                ctx.removeCookie("usuario");
                ctx.removeCookie("mist");
            }
            ctx.redirect("/");
        });
    }

    public static String getConnection(){
        return modoConexion;
    }

}
