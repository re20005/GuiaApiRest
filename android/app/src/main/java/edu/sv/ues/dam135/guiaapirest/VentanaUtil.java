package edu.sv.ues.dam135.guiaapirest;

import android.view.View;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Evita que el contenido quede bajo las barras del sistema (borde a borde).
 */
final class VentanaUtil {

    private VentanaUtil() {
    }

    /** Suma el alto de las barras del sistema al relleno original de la vista. */
    static void aplicarMargenesDelSistema(View raiz) {
        final int izquierda = raiz.getPaddingLeft();
        final int arriba = raiz.getPaddingTop();
        final int derecha = raiz.getPaddingRight();
        final int abajo = raiz.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(raiz, (vista, ventana) -> {
            Insets barras = ventana.getInsets(
                    WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            vista.setPadding(izquierda + barras.left, arriba + barras.top,
                    derecha + barras.right, abajo + barras.bottom);
            return ventana;
        });
    }
}
