package org.acme;

import jakarta.ws.rs.Consumes;
// import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

class UserLogeado {
    private String username;
    private String userStatus;

    public UserLogeado(String user) {
        this.username = user;
        this.userStatus = "LoggedIn";
    }

    public String getUsername() {
        return username;
    }

    public String getUserStatus() {
        return userStatus;
    }
}

class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

// Clase usuario Request
class UserRequest {
    private String username;
    private String password;
    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
}

@Path("/app")
public class GreetingResource {

    //Lista de usuarios
    public List<User> Usuarios = new ArrayList<>();

    //Funcion para validar nombre de usuari
    public boolean ValidaUsername(String username) {
        for (int i=0; i<Usuarios.size();i++) {
            if (Usuarios.get(i).getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    //FuncionParacomparar contraseña
    public boolean ValidaContraseña(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    //Servicio para registrar
    @Path("/register")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response Registrar(UserRequest nuevoUsuario){
        //String username = nuevoUsuario.getUsername();
        //String pass = nuevoUsuario.getPassword();
        //String Confirm = nuevoUsuario.getConfirmPassword();


        //validar nombre de usuario

        if(ValidaUsername(nuevoUsuario.getUsername())){
            return  Response.status(Response.Status.BAD_REQUEST).
                    entity("{\"error\": \"El nombre de usuario se encuentra en uso.\"}").build();
        }

        //validar contraseñas

        if(!ValidaContraseña(nuevoUsuario.getPassword(),nuevoUsuario.getConfirmPassword())){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Las contraseñas no coinciden.\"}").build();
        }

        //guardar usuario en la lista

        User user = new User(nuevoUsuario.getUsername(), nuevoUsuario.getPassword());
        Usuarios.add(user);
        return Response.ok(Usuarios).build();
    }

    //Servicio de Login
    @Path("/login")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)

    public Response Login(UserRequest userRequest){

        String username = userRequest.getUsername();
        String pass = userRequest.getPassword();

        //Validar credenciales
        for(int i=0; i< Usuarios.size();i++){
            //Validas
            if(Usuarios.get(i).getUsername() == username && Usuarios.get(i).getPassword() == pass){
                return Response.ok(new UserLogeado(username)).build();
            }

            //Invalidas
        }

        return Response.status(Response.Status.UNAUTHORIZED)
                .entity("{\"error\": \"Credenciales inválidas.\"}").build();

    }

}
