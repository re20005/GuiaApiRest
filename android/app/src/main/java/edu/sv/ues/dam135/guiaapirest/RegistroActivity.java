package edu.sv.ues.dam135.guiaapirest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import edu.sv.ues.dam135.guiaapirest.api.RetrofitClient;
import edu.sv.ues.dam135.guiaapirest.models.RegistroRequest;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Registro de una cuenta nueva mediante /auth/register. Si la cuenta se crea,
 * devuelve el correo a la pantalla de inicio de sesión.
 */
public class RegistroActivity extends AppCompatActivity {

    static final String EXTRA_CORREO = "correo";
    private static final int CLAVE_MINIMA = 6;

    private TextInputEditText edtNombre;
    private TextInputEditText edtApellido;
    private TextInputEditText edtCorreo;
    private TextInputEditText edtClave;
    private TextInputEditText edtConfirmar;
    private Button buttonRegistrar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        VentanaUtil.aplicarMargenesDelSistema(findViewById(R.id.raiz));

        edtNombre = findViewById(R.id.edtNombre);
        edtApellido = findViewById(R.id.edtApellido);
        edtCorreo = findViewById(R.id.edtCorreo);
        edtClave = findViewById(R.id.edtClave);
        edtConfirmar = findViewById(R.id.edtConfirmar);
        buttonRegistrar = findViewById(R.id.buttonRegistrar);
        progressBar = findViewById(R.id.progressBar);

        buttonRegistrar.setOnClickListener(v -> registrar());
        edtConfirmar.setOnEditorActionListener((vista, accion, evento) -> {
            if (accion == EditorInfo.IME_ACTION_DONE) {
                registrar();
                return true;
            }
            return false;
        });
    }

    private void registrar() {
        String nombre = textoDe(edtNombre);
        String apellido = textoDe(edtApellido);
        String correo = textoDe(edtCorreo);
        String clave = textoDe(edtClave);
        String confirmar = textoDe(edtConfirmar);

        if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty()
                || clave.isEmpty() || confirmar.isEmpty()) {
            mostrarMensaje(R.string.registro_error_campos);
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            mostrarMensaje(R.string.registro_error_correo);
            return;
        }
        if (clave.length() < CLAVE_MINIMA) {
            mostrarMensaje(R.string.registro_error_clave_corta);
            return;
        }
        if (!clave.equals(confirmar)) {
            mostrarMensaje(R.string.registro_error_clave_distinta);
            return;
        }

        mostrarCarga(true);
        RetrofitClient.getApi(this)
                .registrar(new RegistroRequest(nombre, apellido, correo, clave))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> llamada,
                                           Response<ResponseBody> respuesta) {
                        mostrarCarga(false);
                        if (respuesta.code() == 201) {
                            Intent resultado = new Intent().putExtra(EXTRA_CORREO, correo);
                            setResult(RESULT_OK, resultado);
                            finish();
                        } else if (respuesta.code() == 409) {
                            mostrarMensaje(R.string.registro_error_existe);
                        } else {
                            mostrarMensaje(R.string.registro_error_servidor);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> llamada, Throwable error) {
                        mostrarCarga(false);
                        mostrarMensaje(R.string.login_error_red);
                    }
                });
    }

    private void mostrarCarga(boolean cargando) {
        progressBar.setVisibility(cargando ? View.VISIBLE : View.GONE);
        buttonRegistrar.setEnabled(!cargando);
    }

    private void mostrarMensaje(int recurso) {
        Snackbar.make(findViewById(R.id.raiz), recurso, Snackbar.LENGTH_LONG).show();
    }

    private static String textoDe(TextInputEditText campo) {
        return campo.getText() == null ? "" : campo.getText().toString().trim();
    }
}
