package edu.sv.ues.dam135.guiaapirest.api;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import edu.sv.ues.dam135.guiaapirest.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Crea una única instancia de Retrofit con Gson. Agrega el token JWT de la
 * sesión en la cabecera Authorization de cada petición.
 */
public final class RetrofitClient {
    private static ApiService api;

    private RetrofitClient() {
    }

    public static synchronized ApiService getApi(Context contexto) {
        if (api == null) {
            SessionManager sesion = new SessionManager(contexto);

            OkHttpClient.Builder cliente = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .addInterceptor(cadena -> {
                        Request original = cadena.request();
                        String token = sesion.obtenerToken();
                        if (token == null || original.header("Authorization") != null) {
                            return cadena.proceed(original);
                        }
                        Request conToken = original.newBuilder()
                                .header("Authorization", "Bearer " + token)
                                .build();
                        return cadena.proceed(conToken);
                    });

            if (BuildConfig.DEBUG) {
                // Nivel BASIC: no registra cuerpos ni cabeceras con credenciales.
                cliente.addInterceptor(
                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC));
            }

            api = new Retrofit.Builder()
                    .baseUrl(BuildConfig.API_BASE_URL)
                    .client(cliente.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiService.class);
        }
        return api;
    }
}
