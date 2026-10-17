package edu.sv.ues.dam135.guiaapirest.api;

import java.util.List;

import edu.sv.ues.dam135.guiaapirest.models.LoginRequest;
import edu.sv.ues.dam135.guiaapirest.models.Producto;
import edu.sv.ues.dam135.guiaapirest.models.TokenResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Endpoints de la API REST. Las rutas son relativas a la URL base, que
 * incluye el context-path de la API (/api/v1/apirest/).
 */
public interface ApiService {

    @POST("auth/login")
    Call<TokenResponse> login(@Body LoginRequest solicitud);

    @GET("products")
    Call<List<Producto>> obtenerProductos();
}
