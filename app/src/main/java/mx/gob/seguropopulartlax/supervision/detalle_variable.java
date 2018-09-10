package mx.gob.seguropopulartlax.supervision;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class detalle_variable extends AppCompatActivity {

    private TextView tvVariable;
    private EditText et_observavcion;
    private ImageButton btn_yes, btn_no;
    private Button btn_guardar;
    public int visibility = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_variable);

        Bundle parametros = getIntent().getExtras();
        String varible = parametros.getString(getResources().getString(R.string.nombre_variable));

        tvVariable = findViewById(R.id.tv_Nombre_Variable);
        btn_guardar = findViewById(R.id.btn_guardar);
        btn_no = findViewById(R.id.btn_no);
        btn_yes = findViewById(R.id.btn_yes);
        et_observavcion = findViewById(R.id.et_observacion);

        tvVariable.setText(varible);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accion_btn_yes();
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accion_btn_no();
            }
        });

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accion_btn_guardar(v);
            }
        });
    }

    private void accion_btn_guardar(View v) {
        String observacion = et_observavcion.getText().toString();

        View focusView = null;

        if (TextUtils.isEmpty(observacion)) {
            et_observavcion.setError(getString(R.string.obsercavion_vacia));
            focusView = et_observavcion;
        } else {
            Snackbar.make(v,"Guardado con exito",Snackbar.LENGTH_LONG).show();

        }
    }

    public void accion_btn_yes() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(detalle_variable.this);

        alertDialogBuilder.setTitle("Registro Guardado");
        alertDialogBuilder.setMessage("¿Deseas agregar alguna observación?")
                .setCancelable(false)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvVariable.getLayoutParams();
                        params.setMargins(0, 0, 0, 20);
                        tvVariable.setLayoutParams(params);
                        et_observavcion.setVisibility(View.VISIBLE);
                        btn_guardar.setVisibility(View.VISIBLE);
                        btn_yes.setEnabled(false);
                        btn_no.setEnabled(false);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        onBackPressed();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void accion_btn_no() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(detalle_variable.this);

        alertDialogBuilder.setTitle("Registro Guardado");
        alertDialogBuilder.setMessage("¿Deseas agregar alguna observación?")
                .setCancelable(false)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvVariable.getLayoutParams();
                        params.setMargins(0, 0, 0, 20);
                        tvVariable.setLayoutParams(params);
                        et_observavcion.setVisibility(View.VISIBLE);
                        btn_guardar.setVisibility(View.VISIBLE);
                        btn_yes.setEnabled(false);
                        btn_no.setEnabled(false);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        onBackPressed();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Salir")
                    .setMessage("¿Estás seguro?")
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            detalle_variable.this.finish();
                        }
                    })
                    .show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
