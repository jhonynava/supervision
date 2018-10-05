package mx.gob.seguropopulartlax.supervision;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import mx.gob.seguropopulartlax.supervision.db.BaseDatos;
import mx.gob.seguropopulartlax.supervision.db.ConstantesBaseDatos;

public class LoginActivity extends AppCompatActivity {

    private EditText user;
    private EditText pass;
    private Button btnLogin;
    private ProgressDialog progressDialog;
    public Connection conexion = null;
    private View mProgressView;
    private View mLoginFormView;
    private UserLoginTask mAuthTask;
    private String nombre_usuario, aPaterno_usuario, aMaterno_usuario;
    Boolean saveLogin;
    CheckBox saveLoginCheckBox;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String PREF_FILE_NAME = "preferencia";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = findViewById(R.id.edt_user);
        pass = findViewById(R.id.edt_pass);
        btnLogin = findViewById(R.id.btn_login);
        progressDialog = new ProgressDialog(this);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress2);
        saveLoginCheckBox = findViewById(R.id.rContrasenia);


        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Boolean valor = sharedPreferences.getBoolean("savelogin", false);

        if (valor) {
            saveLoginCheckBox.setChecked(true);
            saveLogin = sharedPreferences.getBoolean("savelogin", true);
            user.setText(sharedPreferences.getString("username", null));
            pass.setText(sharedPreferences.getString("password", null));
            nombre_usuario = sharedPreferences.getString("nombre",null);


            Toast.makeText(getBaseContext(), getResources().getString(R.string.conexion_exito) + " " + nombre_usuario, Toast.LENGTH_SHORT).show();
            Intent layout_menu = new Intent(LoginActivity.this, MenuActivity.class);
            startActivity(layout_menu);
            finish();

        } else {
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View view = LoginActivity.this.getCurrentFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    Login();
                }
            });
        }
    }

    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUser;
        private final String mPassword;

        public UserLoginTask(String user, String password) {
            mUser = user;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ConnectionClass con = new ConnectionClass();
            Tools t = new Tools();
            ResultSet rst;
            Boolean rconexion = null;
            try {
                conexion = con.conexionBD();
                if (conexion == null) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.mensaje_error), Toast.LENGTH_SHORT).show();
                    rconexion = false;
                } else {
                    try {
                        String p = t.encode(mPassword);
                        String query = "SELECT A.NOMBRES,A.APELLIDO_PATERNO, A.APELLIDO_MATERNO FROM dbo.cat_user A where A.ID_USUARIO = ? AND A.PASSWORD = ?";
                        PreparedStatement pst = conexion.prepareStatement(query);
                        pst.setString(1, mUser.trim());
                        pst.setString(2, p.trim());
                        rst = pst.executeQuery();

                        if (rst.next()) {
                            nombre_usuario = rst.getString(getResources().getString(R.string.variable_nombres));
                            aPaterno_usuario = rst.getString("APELLIDO_PATERNO");
                            aMaterno_usuario = rst.getString("APELLIDO_MATERNO");
                            editor.putString("nombre",nombre_usuario);
                            editor.putString("apellido_paterno",aPaterno_usuario);
                            editor.putString("apellido_materno",aMaterno_usuario);
                            editor.commit();
                            conexion.close();
                            rconexion = true;
                            insertarUsuario(LoginActivity.this, mUser, p, nombre_usuario, aPaterno_usuario, aMaterno_usuario);

                        } else {
                            rconexion = false;
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            } catch (Exception e) {
                rconexion = false;
            }
            return rconexion;
        }

        @Override
        protected void onPostExecute(final Boolean succes) {
            mAuthTask = null;
            mostrarProgreso(false);
            if (succes) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.conexion_exito) + " " + nombre_usuario, Toast.LENGTH_SHORT).show();
                Intent layout_menu = new Intent(LoginActivity.this, MenuActivity.class);
                startActivity(layout_menu);
                finish();
            } else {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.error_userpass), Toast.LENGTH_SHORT).show();
                pass.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            mostrarProgreso(false);
        }
    }

    private void Login() {
        if (mAuthTask != null) {
            return;
        }

        String usuario = user.getText().toString().toUpperCase();
        String password = pass.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            pass.setError(getString(R.string.contraseña_vacia));
            focusView = pass;
            cancel = true;
        }

        if (TextUtils.isEmpty(usuario)) {
            user.setError(getString(R.string.usuario_vacio));
            focusView = user;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {

            if (saveLoginCheckBox.isChecked()) {
                editor.putBoolean("savelogin", true);
                editor.putString("username", usuario);
                editor.putString("password", password);
                editor.commit();
                mostrarProgreso(true);
                mAuthTask = new UserLoginTask(usuario, password);
                mAuthTask.execute((Void) null);
            } else {
                mostrarProgreso(true);
                mAuthTask = new UserLoginTask(usuario, password);
                mAuthTask.execute((Void) null);
            }
        }
    }

    public class cargarInformarcion extends AsyncTask<Void, Void, Boolean>{


        @Override
        protected Boolean doInBackground(Void... voids) {
            return null;
        }
    }

    private void mostrarProgreso(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    public void insertarUsuario(Context context, String curp, String pass, String nombre, String apaterno, String amaterno){
        BaseDatos db = new BaseDatos(context);
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantesBaseDatos.TABLE_CURP, curp);
        contentValues.put(ConstantesBaseDatos.TABLE_PASSWORD, pass);
        contentValues.put(ConstantesBaseDatos.TABLE_NOMBRE, nombre);
        contentValues.put(ConstantesBaseDatos.TABLE_APATERNO, apaterno);
        contentValues.put(ConstantesBaseDatos.TABLE_AMATERNO, amaterno);
        db.insertarValorUsuario(contentValues);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setTitle("Salir")
                    .setMessage("¿Estás seguro?")
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LoginActivity.this.finish();
                        }
                    })
                    .show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
