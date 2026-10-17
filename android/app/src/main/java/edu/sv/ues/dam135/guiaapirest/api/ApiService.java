package edu.sv.ues.dam135.guiaapirest.api;

import java.util.List;

import edu.sv.ues.dam135.guiaapirest.models.LoginRequest;
import edu.sv.ues.dam135.guiaapirest.models.Producto;
import edu.sv.ues.dam135.guiaapirest.models.RegistroRequest;
import edu.sv.ues.dam135.guiaapirest.models.TokenResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Endpoints de la API REST. Las rutas son relativas a la URL base, que
 * incluye el context-path de la API (/api/v1/apirest/).
 */
public interface ApiService {

    @POST("auth/login")
    Call<TokenResponse> login(@Body LoginRequest solicitud);

    /** Responde 201 al crear la cuenta y 409 si el correo ya existe. */
    @POST("auth/register")
    Call<ResponseBody> registrar(@Body RegistroRequest solicitud);

    /** Invalida el token enviado en la cabecera Authorization. Responde 204. */
    @POST("auth/logout")
    Call<Void> cerrarSesion();

    @GET("products")
    Call<List<Producto>> obtenerProductos();

    @POST("products")
    Call<Producto> crearProducto(@Body Producto producto);

    @PUT("products/{code}")
    Call<Producto> actualizarProducto(@Path("code") int code, @Body Producto producto);

    @DELETE("products/{code}")
    Call<Void> eliminarProducto(@Path("code") int code);
}
