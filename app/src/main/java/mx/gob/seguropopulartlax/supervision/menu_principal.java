package mx.gob.seguropopulartlax.supervision;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import mx.gob.seguropopulartlax.supervision.db.BaseDatos;
import mx.gob.seguropopulartlax.supervision.fragments.RV_VDomiciliaria;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class menu_principal extends Fragment{

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String PREF_FILE_NAME = "preferencia";

    private Button btn_iniciar_supervision, btn_continuar_supervision, btn_lista_supervisiones, btn_imprimir_cedula;

    public menu_principal() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences = this.getActivity().getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);

        View v = inflater.inflate(R.layout.fragment_menu_principal, container, false);

        btn_iniciar_supervision = v.findViewById(R.id.btn_iniciar_supervison);
        btn_continuar_supervision = v.findViewById(R.id.btn_continuar_supervison);
        btn_lista_supervisiones = v.findViewById(R.id.btn_lista_supervisiones);
        btn_imprimir_cedula = v.findViewById(R.id.btn_imprimir_cedula);

        Boolean texto =  sharedPreferences.getBoolean("fase3",false);
        if (texto==false){
        btn_iniciar_supervision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new modulo();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main,fragment).commit();
            }
        });} else {
            btn_iniciar_supervision.setEnabled(false);
            btn_continuar_supervision.setVisibility(View.VISIBLE);
            btn_continuar_supervision.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new RV_VDomiciliaria();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main,fragment).commit();
                }
            });
        }

        return v;
    }

}
