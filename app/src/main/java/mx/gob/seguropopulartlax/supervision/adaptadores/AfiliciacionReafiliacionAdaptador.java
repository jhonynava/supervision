package mx.gob.seguropopulartlax.supervision.adaptadores;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import mx.gob.seguropopulartlax.supervision.R;
import mx.gob.seguropopulartlax.supervision.POJOs.VariablesPOJO;
import mx.gob.seguropopulartlax.supervision.db.BaseDatos;
import mx.gob.seguropopulartlax.supervision.db.ConstantesBaseDatos;
import mx.gob.seguropopulartlax.supervision.db.ConstructorVariables;

public class AfiliciacionReafiliacionAdaptador extends RecyclerView.Adapter<AfiliciacionReafiliacionAdaptador.VariablesViewHolder> {


    ArrayList<VariablesPOJO> variables;
    public Activity activty;
    ConstructorVariables constructorVariables;

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public AfiliciacionReafiliacionAdaptador(ArrayList<VariablesPOJO> variables, Activity activity) {
        this.variables = variables;
        this.activty = activity;
    }

    @NonNull
    @Override
    public AfiliciacionReafiliacionAdaptador.VariablesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_variables, parent, false);
        return new VariablesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final AfiliciacionReafiliacionAdaptador.VariablesViewHolder vaiablesViewHolder, final int position) {
        constructorVariables = new ConstructorVariables(activty);
        final VariablesPOJO variable = variables.get(position);
        final String respuesta = variable.getVariable();
        final String respuesraLike = variable.getRespuesta_like();
        final String respuetsaDislike = variable.getRespuesta_dislike();
        final String comentario = variable.getComentario();
        final int foto = variable.getFoto();
        final int[] valor_respuesta = {0};
        final String[] numero_respuesta = new String[variables.size()];
        final int img_like = variable.getImg_like();
        final int img_dislike = variable.getImg_dislike();

        vaiablesViewHolder.texto_like.setText(respuesraLike);
        vaiablesViewHolder.texto_dislike.setText(respuetsaDislike);
        vaiablesViewHolder.texto_variables.setText(respuesta);
        vaiablesViewHolder.img_comentario.setImageResource(R.drawable.icons8_speech_bubble_64);
        vaiablesViewHolder.img_like.setImageResource(R.drawable.icons_like);
        vaiablesViewHolder.img_dislike.setImageResource(R.drawable.icons_dislike);

        vaiablesViewHolder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String comentario = variable.getComentario();

                String valor1 = vaiablesViewHolder.texto_like.getText().toString();
                String valor2 = vaiablesViewHolder.texto_dislike.getText().toString();

                if (valor2.equals("1")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activty);

                    alertDialogBuilder.setMessage("¿Deseas cambia la respuesta?");
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            vaiablesViewHolder.img_like.setImageResource(R.drawable.icons_like_fill);
                            vaiablesViewHolder.img_dislike.setImageResource(R.drawable.icons_dislike);
                            variable.setRespuesta_dislike(null);
                            vaiablesViewHolder.texto_dislike.setText(null);
                            variable.setRespuesta_like("1");
                            final String respuestaLike = variable.getRespuesta_like();
                            vaiablesViewHolder.texto_like.setText(respuestaLike);

                            if (comentario == null) {
                                Toast.makeText(activty, "Respuesta cambiada", Toast.LENGTH_SHORT).show();
                            } else if (comentario.isEmpty()) {
                                Toast.makeText(activty, "Respuesta cambiada", Toast.LENGTH_SHORT).show();
                            } else {
                                variable.setComentario("");
                                Toast.makeText(activty, "La observación anterior ha sido eliminada", Toast.LENGTH_SHORT).show();
                            }
                            dialog.cancel();
                            valor_respuesta[0] = 1;
                            numero_respuesta[position] = respuesraLike;
                        }
                    });
                    alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = alertDialogBuilder.create();
                    alert.show();
                } else if (valor1.equals("1")) {
                    Toast.makeText(activty, "Ya haz seleccionado la respuesta", Toast.LENGTH_SHORT).show();
                } else if (valor1.isEmpty()) {
                    constructorVariables.insertarLike(variable, 1, true, 21, position, 1);
                    variable.setRespuesta_like("1");
                    vaiablesViewHolder.img_like.setImageResource(R.drawable.icons_like_fill);
                    final String respuestaLike = variable.getRespuesta_like();
                    vaiablesViewHolder.texto_like.setText(respuestaLike);
                    valor_respuesta[0] = 1;
                    numero_respuesta[position] = respuesraLike;
                }
            }
        });

        vaiablesViewHolder.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String comentario = variable.getComentario();
                String valor1 = vaiablesViewHolder.texto_like.getText().toString();
                String valor2 = vaiablesViewHolder.texto_dislike.getText().toString();
                if (valor1.equals("1")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activty);

                    alertDialogBuilder.setMessage("¿Deseas cambia la respuesta?")
                            .setCancelable(false)
                            .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    vaiablesViewHolder.img_dislike.setImageResource(R.drawable.icons_dislike_fill);
                                    vaiablesViewHolder.img_like.setImageResource(R.drawable.icons_like);
                                    variable.setRespuesta_like(null);
                                    vaiablesViewHolder.texto_like.setText(null);
                                    variable.setRespuesta_dislike("1");
                                    final String respuestaDislike = variable.getRespuesta_dislike();
                                    vaiablesViewHolder.texto_dislike.setText(respuestaDislike);

                                    if (comentario == null) {
                                        Toast.makeText(activty, "Respuesta cambiada", Toast.LENGTH_SHORT).show();
                                    } else if (comentario.isEmpty()) {
                                        Toast.makeText(activty, "Respuesta cambiada", Toast.LENGTH_SHORT).show();
                                    } else {
                                        variable.setComentario("");
                                        Toast.makeText(activty, "La observación anterior ha sido eliminada", Toast.LENGTH_SHORT).show();
                                    }
                                    dialog.cancel();
                                    valor_respuesta[0] = 1;
                                    numero_respuesta[position] = respuestaDislike;
                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = alertDialogBuilder.create();
                    alert.show();
                } else if (valor2.equals("1")) {
                    Toast.makeText(activty, "Ya haz seleccionado la respuesta", Toast.LENGTH_SHORT).show();
                } else if (valor2.isEmpty()) {
                    variable.setRespuesta_dislike("1");
                    vaiablesViewHolder.img_dislike.setImageResource(R.drawable.icons_dislike_fill);
                    final String respuestaDislike = variable.getRespuesta_dislike();
                    vaiablesViewHolder.texto_dislike.setText(respuestaDislike);
                    valor_respuesta[0] = 1;
                    numero_respuesta[position] = respuestaDislike;
                }
            }
        });

        vaiablesViewHolder.radioGroup_respuestas.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
        });

        vaiablesViewHolder.img_comentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comentario = variable.getComentario();

                String valor1 = vaiablesViewHolder.texto_like.getText().toString();
                String valor2 = vaiablesViewHolder.texto_dislike.getText().toString();
                int valor = valor_respuesta[0];

                if (valor == 0) {
                    Toast.makeText(activty, "Antes selecciona bien o mal", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(comentario)) {
                    cuadro_dialogo(respuesta, position, vaiablesViewHolder, variable);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activty);
                    final LayoutInflater inflater = activty.getLayoutInflater();
                    final View dialogLayout = inflater.inflate(R.layout.observacion, null);
                    builder.setView(dialogLayout);
                    final AlertDialog show = builder.show();

                    final EditText edt_observacion = dialogLayout.findViewById(R.id.edt_comentario);
                    final Button btn_editar_observacion = dialogLayout.findViewById(R.id.btn_editar_observacion);
                    Button btn_cancelar_observacion = dialogLayout.findViewById(R.id.btn_cancelar);
                    final Button btn_guardar_observacion = dialogLayout.findViewById(R.id.btn_guardar_observacion);

                    edt_observacion.setText(comentario);
                    edt_observacion.setEnabled(false);

                    btn_editar_observacion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            edt_observacion.setEnabled(true);
                            btn_editar_observacion.setVisibility(View.INVISIBLE);
                            btn_guardar_observacion.setVisibility(View.VISIBLE);
                        }
                    });

                    btn_guardar_observacion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String observacion = edt_observacion.getText().toString();
                            View focusView = null;

                            if (TextUtils.isEmpty(observacion)) {
                                edt_observacion.setError("Escriba la observación");
                                focusView = edt_observacion;
                            } else {
                                variable.setComentario(observacion);
                                final String respuesta = variable.getComentario();
                                variable.setFoto(R.drawable.icons8_speech_bubble_64);
                                vaiablesViewHolder.img_comentario.setImageResource(variable.getFoto());
                                Toast.makeText(activty, "Guardado con exito", Toast.LENGTH_SHORT).show();
                                InputMethodManager imm = (InputMethodManager) activty.getSystemService(Activity.INPUT_METHOD_SERVICE);
                                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                                show.dismiss();
                            }
                        }
                    });

                    btn_cancelar_observacion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            show.dismiss();
                        }
                    });
                }
            }
        });
    }

    private void cuadro_dialogo(String respuesta, int position, final VariablesViewHolder vaiablesViewHolder, final VariablesPOJO variable) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activty);
        final LayoutInflater inflater = activty.getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.cuadro_dialogo, null);
        builder.setView(dialogLayout).setCancelable(false);
        final AlertDialog show = builder.show();

        final EditText observacion_vaiable = dialogLayout.findViewById(R.id.obervación_vaiable);
        Button btn_guardar_observacion = dialogLayout.findViewById(R.id.btn_guardar_observacion);
        Button btn_cancelar_observacion = dialogLayout.findViewById(R.id.cancelar_observacion);


        btn_guardar_observacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String observacion = observacion_vaiable.getText().toString();
                View focusView = null;

                if (TextUtils.isEmpty(observacion)) {
                    observacion_vaiable.setError("Escriba la observación");
                    focusView = observacion_vaiable;
                } else {
                    variable.setComentario(observacion);
                    final String respuesta = variable.getComentario();
                    variable.setFoto(R.drawable.icons8_speech_bubble_64);
                    vaiablesViewHolder.img_comentario.setImageResource(variable.getFoto());
                    Toast.makeText(activty, "Guardado con exito", Toast.LENGTH_SHORT).show();
                    InputMethodManager imm = (InputMethodManager) activty.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    show.dismiss();
                }

            }
        });
        btn_cancelar_observacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) activty.getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(dialogLayout.getWindowToken(), 0);
                show.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return variables.size();
    }

    public class VariablesViewHolder extends RecyclerView.ViewHolder {

        private TextView texto_variables, texto_dislike, texto_like;
        private ImageView like, dislike;
        private ImageButton img_comentario;
        private ImageView img_like, img_dislike;
        private RadioGroup radioGroup_respuestas;

        public VariablesViewHolder(final View itemView) {
            super(itemView);
            texto_variables = itemView.findViewById(R.id.tv_variables);
            like = itemView.findViewById(R.id.img_like);
            dislike = itemView.findViewById(R.id.img_dislike);
            texto_dislike = itemView.findViewById(R.id.tv_dislike);
            texto_like = itemView.findViewById(R.id.tv_like);
            img_comentario = itemView.findViewById(R.id.img_comentario);
            img_like = itemView.findViewById(R.id.img_like);
            img_dislike = itemView.findViewById(R.id.img_dislike);
            radioGroup_respuestas = itemView.findViewById(R.id.radio_group_respuestas);
        }
    }
}