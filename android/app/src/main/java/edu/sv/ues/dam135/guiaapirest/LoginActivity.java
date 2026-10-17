package edu.sv.ues.dam135.guiaapirest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import edu.sv.ues.dam135.guiaapirest.api.RetrofitClient;
import edu.sv.ues.dam135.guiaapirest.api.SessionManager;
import edu.sv.ues.dam135.guiaapirest.models.LoginRequest;
import edu.sv.ues.dam135.guiaapirest.models.TokenResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Pantalla de inicio de sesión. Envía las credenciales a /auth/login y, si
 * son válidas, guarda el token y abre la lista de productos.
 */
public class LoginActivity extends AppCompatActivity {

    private TextInputEditText edtUsuario;
    private TextInputEditText edtClave;
    private Button buttonIngresar;
    private ProgressBar progressBar;
    private SessionManager sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        VentanaUtil.aplicarMargenesDelSistema(findViewById(R.id.raiz));

        edtUsuario = findViewById(R.id.edtUsuario);
        edtClave = findViewById(R.id.edtClave);
        buttonIngresar = findViewById(R.id.buttonIngresar);
        progressBar = findViewById(R.id.progressBar);
        sesion = new SessionManager(this);

        buttonIngresar.setOnClickListener(v -> iniciarSesion());
        edtClave.setOnEditorActionListener((vista, accion, evento) -> {
            if (accion == EditorInfo.IME_ACTION_DONE) {
                iniciarSesion();
                return true;
            }
            return false;
        });
    }

    private void iniciarSesion() {
        String usuario = textoDe(edtUsuario);
        String clave = textoDe(edtClave);

        if (usuario.isEmpty() || clave.isEmpty()) {
            mostrarMensaje(R.string.login_error_campos);
            return;
        }

        mostrarCarga(true);
        RetrofitClient.getApi(this)
                .login(new LoginRequest(usuario, clave))
                .enqueue(new Callback<TokenResponse>() {
                    @Override
                    public void onResponse(Call<TokenResponse> llamada,
                                           Response<TokenResponse> respuesta) {
                        mostrarCarga(false);
                        TokenResponse cuerpo = respuesta.body();
                        if (respuesta.isSuccessful() && cuerpo != null && cuerpo.getToken() != null) {
                            sesion.guardarToken(cuerpo.getToken());
                            abrirProductos();
                        } else if (respuesta.code() == 401 || respuesta.code() == 403) {
                            mostrarMensaje(R.string.login_error_credenciales);
                        } else {
                            mostrarMensaje(R.string.login_error_inesperado);
                        }
                    }

                    @Override
                    public void onFailure(Call<TokenResponse> llamada, Throwable error) {
                        mostrarCarga(false);
                        mostrarMensaje(R.string.login_error_red);
                    }
                });
    }

    private void abrirProductos() {
        startActivity(new Intent(this, ProductosActivity.class));
        finish();
    }

    private void mostrarCarga(boolean cargando) {
        progressBar.setVisibility(cargando ? View.VISIBLE : View.GONE);
        buttonIngresar.setEnabled(!cargando);
    }

    private void mostrarMensaje(int recurso) {
        Snackbar.make(findViewById(R.id.raiz), recurso, Snackbar.LENGTH_LONG).show();
    }

    private static String textoDe(TextInputEditText campo) {
        return campo.getText() == null ? "" : campo.getText().toString().trim();
    }
}
