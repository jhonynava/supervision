package mx.gob.seguropopulartlax.supervision;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import mx.gob.seguropopulartlax.supervision.POJOs.VariablesPOJO;

import static android.content.Context.MODE_PRIVATE;

public class informe_hallazgos extends Fragment {

    private EditText edt_fecha_cumplimiento, edt_hallazgo, edt_compromisoMejora, edt_responsable, edt_supervisor, edt_rubro, edt_variable;
    private Calendar myCalendar = Calendar.getInstance();
    private cargarDatosTask mAuthTask;
    Connection conexion = null;
    ConnectionClass conn = new ConnectionClass();
    ArrayList<String> rubros = new ArrayList<>();
    SpinnerDialog spinnerDialog_Rubro, spinnerDialog_Variable;
    ResultSet rst;
    ArrayList<VariablesPOJO> variables = new ArrayList<>();
    ArrayList<String> variablesList = new ArrayList<>();
    VariablesPOJO variable = null;
    int pos1 = -1, pos2 = -1;
    private ProgressDialog proceso;
    private int posicion_rubro = 0;
    private long min;
    View focusView = null;
    private String full_name;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String PREF_FILE_NAME = "preferencia";


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences = this.getActivity().getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);

        View v = inflater.inflate(R.layout.fragment_informe_hallazgos, container, false);
        edt_rubro = v.findViewById(R.id.edt_rubro);
        edt_variable = v.findViewById(R.id.edt_variable);
        edt_hallazgo = v.findViewById(R.id.edt_hallazgo);
        edt_compromisoMejora = v.findViewById(R.id.edt_compromisoMejora);
        edt_responsable = v.findViewById(R.id.edt_responsable);
        edt_supervisor = v.findViewById(R.id.edt_supervisor);
        Button btn_guarda_hallazgos = v.findViewById(R.id.btn_guardar_hallazgos);
        Button btn_sin_hallazgo = v.findViewById(R.id.btn_sinHallazgos);

        full_name = sharedPreferences.getString("nombre",null) + " " + sharedPreferences.getString("apellido_paterno",null) + " " + sharedPreferences.getString("apellido_materno", null);
        edt_supervisor.setText(full_name);
        edt_supervisor.setTextColor(getResources().getColor(R.color.negro));
        edt_responsable.setText(sharedPreferences.getString("encargado",null));
        edt_responsable.setTextColor(getResources().getColor(R.color.negro));

        proceso = new ProgressDialog(getActivity());
        cargarRubros();

        //Calendario
        min = myCalendar.getTimeInMillis() + (60 * (1440 + 60 * (24 + 24 * (30 + 64 * (6 + 12)))));
        edt_fecha_cumplimiento = v.findViewById(R.id.edt_fecha_cumplimiento);

        edt_fecha_cumplimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(min);
                dialog.show();
            }
        });

        edt_rubro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialog_Rubro.showSpinerDialog();
                spinnerDialog_Rubro.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String s, int i) {
                        edt_rubro.setText(s);
                        edt_rubro.setTextColor(getResources().getColor(R.color.negro));
                        pos1 = i;
                        posicion_rubro = i + 1;
                        variablesList.clear();
                        cargarDatosVariable();
                    }
                });
            }
        });

        edt_variable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = edt_variable.getText().toString();
                if (a.equals("")) {
                    Toast.makeText(getActivity(), "Primero pulsa 'Rubro' para ingresar los datos de la variable", Toast.LENGTH_LONG).show();
                }
                else{
                    spinnerDialog_Variable.showSpinerDialog();
                    spinnerDialog_Variable.bindOnSpinerListener(new OnSpinerItemClick() {
                        @Override
                        public void onClick(String s, int i) {
                            pos2 = i;
                            edt_variable.setText(s);
                            edt_variable.setTextColor(getResources().getColor(R.color.negro));
                        }
                    });

                }
            }
        });

        btn_guarda_hallazgos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto_hallazgo = edt_hallazgo.getText().toString();
                String texto_compromiso = edt_compromisoMejora.getText().toString();
                String fecha = edt_fecha_cumplimiento.getText().toString();

                if (pos1 != -1 && pos2 != -1 && !texto_hallazgo.isEmpty() && !texto_compromiso.isEmpty() && !fecha.isEmpty()) {
                    cuadroDialogo_denuevo();
                }
                if (pos1 == -1 && pos2 == -1 && texto_hallazgo.isEmpty() && texto_compromiso.isEmpty()) {
                    edt_rubro.setError("Selecciona el rubro");
                    edt_variable.setError("Selecciona la variable");
                    edt_hallazgo.setError("Ingresa el hallazgo");
                    edt_compromisoMejora.setError("Ingresa el compromiso de mejora");
                    edt_fecha_cumplimiento.setError("Ingresa la fecha de cumplimiento");
                    focusView = edt_rubro;
                    focusView = edt_variable;
                    focusView = edt_hallazgo;
                    focusView = edt_compromisoMejora;
                    focusView = edt_fecha_cumplimiento;
                    Toast.makeText(getContext(), "Te faltan campos por ingresar", Toast.LENGTH_SHORT).show();
                } else if (pos1 == -1) {
                    edt_rubro.setError("Selecciona el Rubro");
                    focusView = edt_rubro;
                    Toast.makeText(getContext(), "Selecciona el Rubro", Toast.LENGTH_SHORT).show();
                } else if (pos2 == -1){
                    edt_variable.setError("Selecciona la Variable");
                    focusView = edt_variable;
                    Toast.makeText(getContext(), "Selecciona la Variable", Toast.LENGTH_SHORT).show();
                } else if (texto_hallazgo.isEmpty()){
                    edt_hallazgo.setError("Ingresa el hallazgo");
                    focusView = edt_hallazgo;
                } else if (texto_compromiso.isEmpty()){
                    edt_compromisoMejora.setError("Ingresa el compromiso de mejora");
                    focusView = edt_compromisoMejora;
                } else if (fecha.isEmpty()){
                    edt_fecha_cumplimiento.setError("Ingresa la fecha de cumplimiento");
                    focusView = edt_fecha_cumplimiento;
                    Toast.makeText(getContext(), "Ingresa la fecha de cumplimiento", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_sin_hallazgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cuadroDialogo_terminar();
            }
        });

        return v;
    }

    private void cuadroDialogo_denuevo() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("¡Listo!");
        alertDialogBuilder.setMessage("¿Deseas agregar otro hallazgo?")
                .setCancelable(false)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        edt_rubro.setText(null);
                        edt_variable.setText(null);
                        edt_hallazgo.setText(null);
                        edt_compromisoMejora.setText(null);
                        edt_fecha_cumplimiento.setText(null);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cuadroDialogo_terminar();
                    }
                });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    private void cuadroDialogo_terminar() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        alertDialogBuilder.setTitle("¡Primera fase terminada!");
        alertDialogBuilder.setMessage("Los datos han sido guardados con exito.\n" +
                "¿Deseas iniciar con una nueva supervisión?")
                .setCancelable(false)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), MenuActivity.class);
                        startActivity(intent);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void cargarRubros() {
        rubros.add("Revisión del Procedimiento de Afiliación o Reafiliación");
        rubros.add("Revisión de Expedientes");
        rubros.add("Visitas Domiciliarias");
        rubros.add("Ejercicio del Gasto de Operación");

        spinnerDialog_Rubro = new SpinnerDialog(getActivity(), rubros, "Selecciona el Rubro", "Cerrar");
    }

    private void cargarDatosVariable() {
        mAuthTask = new cargarDatosTask();
        mAuthTask.execute((Void) null);
    }

    public class cargarDatosTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                conexion = conn.conexionBD();
                String position = String.valueOf(posicion_rubro);
                if (conexion == null) {
                    Toast.makeText(getContext(), "Error en la conexión.\nRevisa tu conexión a internet", Toast.LENGTH_SHORT).show();
                } else {
                    variables.clear();
                    variablesList.clear();
                    String query = "SELECT Variables FROM Desarrollo.dbo.cedula WHERE Id_rubro=?";
                    PreparedStatement pst = conexion.prepareStatement(query);
                    pst.setString(1, position);
                    rst = pst.executeQuery();

                    while (rst.next()) {
                        variable = new VariablesPOJO();
                        variable.setVariable(rst.getString("Variables"));
                        variables.add(variable);
                    }
                    for (int i = 0; i < variables.size(); i++)
                        variablesList.add(variables.get(i).getVariable());

                    conexion.close();

                }
            } catch (Exception e) {
                Toast.makeText(getContext(), "Error al obtener la informaicón", Toast.LENGTH_SHORT).show();
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
            edt_variable.setText(null);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            spinnerDialog_Variable = new SpinnerDialog(getActivity(), variablesList, "Selecciona la Variable", "Cancelar");
            proceso.dismiss();
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            proceso.dismiss();
        }
    }

    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateLabel();
        }
    };

    private void updateLabel() {
        String myFormat = "dd/MM/YYYY";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edt_fecha_cumplimiento.setText(sdf.format(myCalendar.getTime()));
    }


}