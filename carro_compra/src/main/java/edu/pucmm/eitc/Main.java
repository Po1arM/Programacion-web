package edu.pucmm.eitc;

import edu.pucmm.eitc.encapsulaciones.CarroCompra;
import edu.pucmm.eitc.encapsulaciones.Producto;
import edu.pucmm.eitc.encapsulaciones.VentasProductos;
import io.javalin.Javalin;

import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinVelocity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main {
    public static void main(String[] args){

        Javalin app = Javalin.create().start(5000);
        JavalinRenderer.register(JavalinVelocity.INSTANCE, ".vm");

        Service service = Service.getInstance();

        app.before(ctx -> {
            CarroCompra carrito = ctx.sessionAttribute("carrito");
            if(carrito == null){
                carrito = new CarroCompra(service.getCarrito());
            }
            ctx.sessionAttribute("carrito",carrito);

        });

        app.get("/", ctx -> {
            CarroCompra carrito = ctx.sessionAttribute("carrito");

            List<Producto> productos = service.getProductos();
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("productos",productos);
            modelo.put("cantidad",carrito.getProductos().size());
            ctx.render("/publico/listadoProductos.vm", modelo);
        });
        app.get("/comprar", ctx -> {
            ctx.redirect("/");
        });

        app.get("/ventas", ctx -> {

            if( ctx.cookie("usuario") == null || ctx.cookie("password") == null || !ctx.cookie("usuario").equalsIgnoreCase("admin") || !ctx.cookie("password").equalsIgnoreCase("admin")) {
                ctx.redirect("/autenti/ventas");
                return;
            }
            CarroCompra carrito = ctx.sessionAttribute("carrito");
            List<VentasProductos> ventas = service.getVentas();
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("ventas",ventas);
            modelo.put("cantidad",carrito.getProductos().size());

            ctx.render("/publico/ventas.vm",modelo);
        });

        app.get("/carrito", ctx -> {
            CarroCompra carrito = ctx.sessionAttribute("carrito");
            if(carrito == null){
                carrito = new CarroCompra(service.getCarrito());
            }
            ctx.sessionAttribute("carrito",carrito);
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("productos",carrito.getProductos());
            modelo.put("cantidad",carrito.getProductos().size());
            ctx.render("/publico/carrito.vm",modelo);
        });

        app.get("/registrar", ctx -> {
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("accion","/registrar");
            CarroCompra carrito = ctx.sessionAttribute("carrito");
            modelo.put("cantidad",carrito.getProductos().size());
            ctx.render("/publico/productoCE.vm",modelo);
        });

        app.get("/productos", ctx -> {
            if( ctx.cookie("usuario") == null || ctx.cookie("password") == null || !ctx.cookie("usuario").equalsIgnoreCase("admin") || !ctx.cookie("password").equalsIgnoreCase("admin")) {
                ctx.redirect("/autenti/productos");
                return;
            }
            List<Producto> productos = service.getProductos();
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("productos",productos);
            CarroCompra carrito = ctx.sessionAttribute("carrito");
            modelo.put("cantidad",carrito.getProductos().size());
            ctx.render("/publico/productos.vm",modelo);
        });

        app.post("/registrar", ctx -> {
            String nombre = ctx.formParam("nombre");
            int precio = ctx.formParam("precio",Integer.class).get();

            Producto temp = new Producto(nombre,precio);
            service.registrarProducto(temp);
            ctx.redirect("/productos");
        });

        app.get("/remover/:id", ctx -> {
            service.eliminarProducto(ctx.pathParam("id",Integer.class).get());
            ctx.redirect("/productos");
        });

        app.get("/editar/:id", ctx -> {
            Producto temp = service.getProductosPorID(ctx.pathParam("id", Integer.class).get());
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("producto",temp);
            modelo.put("accion","/editar/"+ctx.pathParam("id", Integer.class).get());

            CarroCompra carrito = ctx.sessionAttribute("carrito");
            modelo.put("cantidad",carrito.getProductos().size());
            ctx.render("/publico/productoCE.vm",modelo);
        });

        app.post("/editar/:id", ctx -> {
            String nombre = ctx.formParam("nombre");
            int precio = ctx.formParam("precio",Integer.class).get();

            Producto temp = new Producto(nombre,precio);
            temp.setId(ctx.pathParam("id",Integer.class).get());
            service.actualizarProducto(temp);

            ctx.redirect("/productos");
        });

        app.get("/autenti/:direc", ctx -> {
            String direc = ctx.pathParam("direc");
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("direc",direc);
            ctx.render("/publico/autentificacion.vm",modelo);
        });

        app.post("/autenti/:direc",ctx -> {
            String usuario = ctx.formParam("usuario");
            String pass = ctx.formParam("password");
            String temp = ctx.pathParam("direc");

            if(usuario == null || pass == null){
                ctx.redirect("/autenti/"+temp);
            }
            ctx.cookie("usuario", usuario);
            ctx.cookie("password",pass);

            ctx.redirect("/"+temp);

        });

        app.post("/comprar", ctx -> {
            CarroCompra carrito = ctx.sessionAttribute("carrito");

            Producto temp = carrito.getProductosPorID(ctx.formParam("id",Integer.class).get());
            if(temp == null){
                temp = service.getProductosPorID(ctx.formParam("id",Integer.class).get());
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

        app.get("/eliminar/:id", ctx -> {
            int id = ctx.pathParam("id", Integer.class).get();
            CarroCompra carrito = ctx.sessionAttribute("carrito");
            carrito.eliminarProductoPorId(id);

            ctx.sessionAttribute("carrito",carrito);
            ctx.redirect("/carrito");
        });

        app.post("/procesar",ctx -> {
           CarroCompra carrito = ctx.sessionAttribute("carrito");
           if(carrito.getProductos().size() < 1){
               ctx.redirect("/carrito");
           }
           String nombre = ctx.formParam("nombre");
           VentasProductos venta = new VentasProductos(service.getVentas().size()+1,nombre,carrito.productos);
           service.addVentas(venta);
           carrito.borrarProductos();
           ctx.sessionAttribute("carrito",carrito);
           ctx.redirect("/comprar");
        });

        app.get("/limpiar", ctx -> {
            CarroCompra carrito = ctx.sessionAttribute("carrito");
            carrito.borrarProductos();

            ctx.redirect("/comprar");
        });
    }

}
