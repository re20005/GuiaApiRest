package edu.sv.ues.dam135.guiaapirest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import edu.sv.ues.dam135.guiaapirest.api.ApiService;
import edu.sv.ues.dam135.guiaapirest.api.RetrofitClient;
import edu.sv.ues.dam135.guiaapirest.api.SessionManager;
import edu.sv.ues.dam135.guiaapirest.models.Producto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Consulta y mantiene los productos de la API (crear, editar y eliminar).
 * Todos los endpoints están protegidos con el token JWT de la sesión.
 */
public class ProductosActivity extends AppCompatActivity implements ProductoAdapter.Acciones {

    private ApiService api;
    private ProductoAdapter adaptador;
    private ProgressBar progressBar;
    private TextView tvVacio;
    private SessionManager sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);
        VentanaUtil.aplicarMargenesDelSistema(findViewById(R.id.raiz));

        api = RetrofitClient.getApi(this);
        progressBar = findViewById(R.id.progressBar);
        tvVacio = findViewById(R.id.tvVacio);
        sesion = new SessionManager(this);

        adaptador = new ProductoAdapter(this);
        RecyclerView rvProductos = findViewById(R.id.rvProductos);
        rvProductos.setLayoutManager(new LinearLayoutManager(this));
        rvProductos.setAdapter(adaptador);

        findViewById(R.id.buttonCerrarSesion).setOnClickListener(v -> cerrarSesion());
        findViewById(R.id.fabAgregar).setOnClickListener(v -> mostrarFormulario(null));

        cargarProductos();
    }

    // ---------------------------------------------------------------- consultar

    private void cargarProductos() {
        progressBar.setVisibility(View.VISIBLE);
        tvVacio.setVisibility(View.GONE);

        api.obtenerProductos().enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> llamada,
                                   Response<List<Producto>> respuesta) {
                progressBar.setVisibility(View.GONE);
                if (sesionExpirada(respuesta.code())) {
                    return;
                }
                if (respuesta.isSuccessful()) {
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

    // ------------------------------------------------------------ crear / editar

    @Override
    public void alEditar(Producto producto) {
        mostrarFormulario(producto);
    }

    /** Muestra el formulario para crear un producto (existente nulo) o editar uno. */
    private void mostrarFormulario(Producto existente) {
        View vista = getLayoutInflater().inflate(R.layout.dialog_producto, null);
        TextInputLayout tilNombre = vista.findViewById(R.id.tilNombreProducto);
        TextInputEditText edtNombre = vista.findViewById(R.id.edtNombreProducto);
        MaterialSwitch swActivo = vista.findViewById(R.id.swActivo);

        if (existente != null) {
            edtNombre.setText(existente.getName());
            swActivo.setChecked(existente.isStatus());
        }

        AlertDialog dialogo = new MaterialAlertDialogBuilder(this)
                .setTitle(existente == null ? R.string.producto_nuevo : R.string.producto_editar)
                .setView(vista)
                .setPositiveButton(R.string.accion_guardar, null)
                .setNegativeButton(R.string.accion_cancelar, null)
                .create();

        // El botón se enlaza después de mostrar el diálogo para que no se cierre
        // cuando el nombre está vacío.
        dialogo.setOnShowListener(d -> dialogo.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(v -> {
                    String nombre = edtNombre.getText() == null
                            ? "" : edtNombre.getText().toString().trim();
                    if (nombre.isEmpty()) {
                        tilNombre.setError(getString(R.string.producto_error_nombre));
                        return;
                    }
                    tilNombre.setError(null);
                    guardarProducto(existente, nombre, swActivo.isChecked());
                    dialogo.dismiss();
                }));
        dialogo.show();
    }

    private void guardarProducto(Producto existente, String nombre, boolean activo) {
        progressBar.setVisibility(View.VISIBLE);
        Callback<Producto> alTerminar = new Callback<Producto>() {
            @Override
            public void onResponse(Call<Producto> llamada, Response<Producto> respuesta) {
                progressBar.setVisibility(View.GONE);
                if (sesionExpirada(respuesta.code())) {
                    return;
                }
                if (respuesta.isSuccessful()) {
                    mostrarMensaje(R.string.producto_guardado);
                    cargarProductos();
                } else {
                    mostrarMensaje(R.string.producto_error_guardar);
                }
            }

            @Override
            public void onFailure(Call<Producto> llamada, Throwable error) {
                progressBar.setVisibility(View.GONE);
                mostrarMensaje(R.string.producto_error_guardar);
            }
        };

        if (existente == null) {
            api.crearProducto(new Producto(null, nombre, activo)).enqueue(alTerminar);
        } else {
            api.actualizarProducto(existente.getCode(),
                    new Producto(existente.getCode(), nombre, activo)).enqueue(alTerminar);
        }
    }

    // ------------------------------------------------------------------ eliminar

    @Override
    public void alEliminar(Producto producto) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.producto_eliminar_titulo)
                .setMessage(getString(R.string.producto_eliminar_mensaje, producto.getName()))
                .setPositiveButton(R.string.accion_eliminar, (d, which) -> eliminarProducto(producto))
                .setNegativeButton(R.string.accion_cancelar, null)
                .show();
    }

    private void eliminarProducto(Producto producto) {
        progressBar.setVisibility(View.VISIBLE);
        api.eliminarProducto(producto.getCode()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> llamada, Response<Void> respuesta) {
                progressBar.setVisibility(View.GONE);
                if (sesionExpirada(respuesta.code())) {
                    return;
                }
                if (respuesta.isSuccessful()) {
                    mostrarMensaje(R.string.producto_eliminado);
                    cargarProductos();
                } else {
                    mostrarMensaje(R.string.producto_error_eliminar);
                }
            }

            @Override
            public void onFailure(Call<Void> llamada, Throwable error) {
                progressBar.setVisibility(View.GONE);
                mostrarMensaje(R.string.producto_error_eliminar);
            }
        });
    }

    // ------------------------------------------------------------ sesión y avisos

    /** Cierra la sesión en la API (revoca el token) y vuelve al login. */
    private void cerrarSesion() {
        progressBar.setVisibility(View.VISIBLE);
        api.cerrarSesion().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> llamada, Response<Void> respuesta) {
                volverAlLogin();
            }

            @Override
            public void onFailure(Call<Void> llamada, Throwable error) {
                // Sin conexión: igual se cierra la sesión en el dispositivo.
                volverAlLogin();
            }
        });
    }

    private void volverAlLogin() {
        sesion.cerrarSesion();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /** Si el token venció o fue revocado, vuelve al login y devuelve true. */
    private boolean sesionExpirada(int codigoHttp) {
        if (codigoHttp == 401 || codigoHttp == 403) {
            mostrarMensaje(R.string.productos_sesion_expirada);
            volverAlLogin();
            return true;
        }
        return false;
    }

    private void mostrarErrorDeCarga() {
        Snackbar.make(findViewById(R.id.raiz), R.string.productos_error_red, Snackbar.LENGTH_INDEFINITE)
                .setAnchorView(R.id.fabAgregar)
                .setAction(R.string.productos_reintentar, v -> cargarProductos())
                .show();
    }

    private void mostrarMensaje(int recurso) {
        // Anclado al botón flotante para que el aviso no lo tape.
        Snackbar.make(findViewById(R.id.raiz), recurso, Snackbar.LENGTH_SHORT)
                .setAnchorView(R.id.fabAgregar)
                .show();
    }
}
