package edu.sv.ues.dam135.guiaapirest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import edu.sv.ues.dam135.guiaapirest.api.RetrofitClient;
import edu.sv.ues.dam135.guiaapirest.api.SessionManager;
import edu.sv.ues.dam135.guiaapirest.models.Producto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Consulta los productos de la API (endpoint protegido con el token JWT).
 */
public class ProductosActivity extends AppCompatActivity {

    private ProductoAdapter adaptador;
    private ProgressBar progressBar;
    private TextView tvVacio;
    private SessionManager sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);
        VentanaUtil.aplicarMargenesDelSistema(findViewById(R.id.raiz));

        progressBar = findViewById(R.id.progressBar);
        tvVacio = findViewById(R.id.tvVacio);
        sesion = new SessionManager(this);

        adaptador = new ProductoAdapter();
        RecyclerView rvProductos = findViewById(R.id.rvProductos);
        rvProductos.setLayoutManager(new LinearLayoutManager(this));
        rvProductos.setAdapter(adaptador);

        findViewById(R.id.buttonCerrarSesion).setOnClickListener(v -> cerrarSesion());

        cargarProductos();
    }

    private void cargarProductos() {
        progressBar.setVisibility(View.VISIBLE);
        tvVacio.setVisibility(View.GONE);

        RetrofitClient.getApi(this).obtenerProductos().enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> llamada,
                                   Response<List<Producto>> respuesta) {
                progressBar.setVisibility(View.GONE);
                if (respuesta.code() == 401 || respuesta.code() == 403) {
                    Snackbar.make(findViewById(R.id.raiz),
                            R.string.productos_sesion_expirada, Snackbar.LENGTH_SHORT).show();
                    cerrarSesion();
                } else if (respuesta.isSuccessful()) {
                    // La API responde 204 sin cuerpo cuando no hay productos.
                    List<Producto> productos = respuesta.body();
                    adaptador.setProductos(productos);
                    boolean hayProductos = productos != null && !productos.isEmpty();
                    tvVacio.setVisibility(hayProductos ? View.GONE : View.VISIBLE);
                } else {
                    mostrarErrorDeCarga();
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> llamada, Throwable error) {
                progressBar.setVisibility(View.GONE);
                mostrarErrorDeCarga();
            }
        });
    }

    private void mostrarErrorDeCarga() {
        Snackbar.make(findViewById(R.id.raiz), R.string.productos_error_red, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.productos_reintentar, v -> cargarProductos())
                .show();
    }

    private void cerrarSesion() {
        sesion.cerrarSesion();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
