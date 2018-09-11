package mx.gob.seguropopulartlax.supervision;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import mx.gob.seguropopulartlax.supervision.POJOs.UsuarioPOJO;
import mx.gob.seguropopulartlax.supervision.vista.fragments.fragment_RV_Afiliacion;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class modulo extends Fragment {

    EditText edt_modulo, edt_encargado;
    Button btnIngresar;
    ArrayList<String> listaPersonas;
    ArrayList<UsuarioPOJO> personasList = new ArrayList<>();
    ArrayList<String> listaModulos;
    ArrayList<Mao> maoList = new ArrayList<>();
    Connection conexion = null;
    ResultSet rst;
    ConnectionClass conn = new ConnectionClass();
    private cargarDatosTask mAuthTask;
    Mao modulos = null;
    UsuarioPOJO persona = null;
    private ProgressDialog proceso;
    int pos1 = -1;
    int pos2 = -1;
    SpinnerDialog spinnerDialog_modulo, spinnerDialog_encargado;
    View focusView = null;
    private String modulo, encargado;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String PREF_FILE_NAME = "preferencia";

    public modulo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences = this.getActivity().getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        View v = inflater.inflate(R.layout.fragment_modulo, container, false);

        btnIngresar = v.findViewById(R.id.btn_ingresar);
        edt_modulo = v.findViewById(R.id.edt_modulo);
        edt_encargado = v.findViewById(R.id.edt_encargado);
        proceso = new ProgressDialog(getActivity());

        cargarDatos();

        edt_modulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialog_modulo.showSpinerDialog();
                spinnerDialog_modulo.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String s, int i) {
                        pos1 = i;
                        edt_modulo.setText(s);
                        edt_modulo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        modulo = s;
                    }
                });
            }
        });

        edt_encargado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialog_encargado.showSpinerDialog();
                spinnerDialog_encargado.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String s, int i) {
                        pos2 = i;
                        edt_encargado.setText(s);
                        edt_encargado.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        encargado = s;
                    }
                });
            }
        });

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pos1 != -1 && pos2 != -1) {
                    editor.putString("encargado", encargado);
                    editor.putString("modulo", modulo);
                    editor.commit();
                    Fragment fragment = new fragment_RV_Afiliacion();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main,fragment).commit();
                }
                if (pos1 == -1 && pos2 == -1) {
                    edt_encargado.setError("Selecciona el Encargado");
                    edt_modulo.setError("Selecciona el Modulo");
                    focusView = edt_encargado;
                    focusView = edt_modulo;
                    Toast.makeText(getActivity(), "Selecciona el Modulo y el Encargado", Toast.LENGTH_SHORT).show();
                } else if (pos1 == -1) {
                    edt_modulo.setError("Selecciona el modulo");
                    focusView = edt_modulo;
                    Toast.makeText(getActivity(), "Selecciona el Modulo", Toast.LENGTH_SHORT).show();
                } else if (pos2 == -1) {
                    edt_encargado.setError("Selecciona el encargado");
                    focusView = edt_encargado;
                    Toast.makeText(getActivity(), "Selecciona el Encargado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    public class cargarDatosTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                conexion = conn.conexionBD();
                if (conexion == null) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.mensaje_error), Toast.LENGTH_SHORT).show();
                } else {
                    String query =
                            "SELECT MAO, ALIAS " +
                                    "FROM Desarrollo.dbo.CAT_MAO " +
                                    "WHERE ALIAS IS NOT NULL AND ACTIVO=1 ORDER BY ALIAS";
                    PreparedStatement pst = conexion.prepareStatement(query);
                    rst = pst.executeQuery();

                    while (rst.next()) {
                        modulos = new Mao();
                        modulos.setMao(rst.getString("MAO"));
                        modulos.setAlias(rst.getString("ALIAS"));
                        maoList.add(modulos);
                    }
                    listaModulos = new ArrayList<>();

                    for (int i = 0; i < maoList.size(); i++) {
                        listaModulos.add(maoList.get(i).getAlias());
                    }
                    conexion.close();
                }
            } catch (Exception e) {
            }

            try {
                conexion = conn.conexionBD();
                if (conexion == null) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.mensaje_error), Toast.LENGTH_SHORT).show();
                } else {
                    String query = "SELECT APELLIDO_PATERNO, APELLIDO_MATERNO, NOMBRES FROM cat_user WHERE SUPERVISOR != 1 ORDER BY APELLIDO_PATERNO";
                    PreparedStatement pst = conexion.prepareStatement(query);
                    rst = pst.executeQuery();

                    while (rst.next()) {
                        persona = new UsuarioPOJO();
                        persona.setaPaterno(rst.getString("APELLIDO_PATERNO"));
                        persona.setaMaterno(rst.getString("APELLIDO_MATERNO"));
                        persona.setNombre(rst.getString("NOMBRES"));
                        personasList.add(persona);
                    }
                    listaPersonas = new ArrayList<>();

                    for (int i = 0; i < personasList.size(); i++) {
                        listaPersonas.add(personasList.get(i).getaPaterno() + " " + personasList.get(i).getaMaterno() + " " +
                                personasList.get(i).getNombre());
                    }
                    conexion.close();
                }
            } catch (Exception e) {
            }

            return true;
        }

        @Override
        protected void onPreExecute() {
            proceso.setMessage("Cargando...");
            proceso.setTitle("Obteniendo información");
            proceso.show();
            proceso.setCancelable(false);
            proceso.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            spinnerDialog_modulo = new SpinnerDialog(getActivity(), listaModulos, "Selecciona el Modulo", "Cancelar");
            spinnerDialog_encargado = new SpinnerDialog(getActivity(), listaPersonas, "Selecciona el Encargado", "Cancelar");
            //ArrayAdapter<CharSequence> adaptadorModulo = new ArrayAdapter(ModuloActivity.this, android.R.layout.simple_spinner_dropdown_item, listaModulos);
            //ArrayAdapter<CharSequence> adaptadorEncargado = new ArrayAdapter(ModuloActivity.this, android. R.layout.simple_spinner_dropdown_item, listaPersonas);
            //spinModulo.setAdapter(adaptadorModulo);
            //spinEncargado.setAdapter(adaptadorEncargado);
            proceso.dismiss();

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            proceso.dismiss();
        }
    }

    private void cargarDatos() {
        mAuthTask = new cargarDatosTask();
        mAuthTask.execute((Void) null);
    }


}
