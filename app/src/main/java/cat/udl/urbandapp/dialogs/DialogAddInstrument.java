package cat.udl.urbandapp.dialogs;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import cat.udl.urbandapp.R;
import cat.udl.urbandapp.models.Instrument;
import cat.udl.urbandapp.preferences.PreferencesProvider;

import cat.udl.urbandapp.recyclerview.InstrumentAdapter;
import cat.udl.urbandapp.recyclerview.InstrumentDiffCallback;
import cat.udl.urbandapp.viewmodel.TablesViewModel;
import cat.udl.urbandapp.viewmodel.UserViewModel;

public class DialogAddInstrument extends DialogFragment implements LifecycleOwner {

    public View rootView;
    private FragmentActivity activity;
    private TablesViewModel viewModel;
    private SharedPreferences mPreferences;

    private CardView cuerda;
    private CardView percusion;
    private CardView viento;
    private CardView otros;

    private Spinner choice_instrument;
    private FloatingActionButton addInstrument;
    private RatingBar experienceBar;


    public static DialogAddInstrument newInstance(FragmentActivity activity, TablesViewModel tablesViewModel) {
        DialogAddInstrument dialog = new DialogAddInstrument();
        dialog.activity = activity;
        dialog.viewModel = tablesViewModel;
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.mPreferences = PreferencesProvider.providePreferences();
        initView();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("Aplicar cambios", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                viewModel.saveInstrument();
                viewModel.resetInstruments();
            }
        });
        AlertDialog alertDialog = builder.setView(rootView)
                .setCancelable(true)
                .create();

        cuerda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayAdapter<CharSequence> adapter_cu = ArrayAdapter.createFromResource(getContext(),
                        R.array.spinnerStringInstruments, android.R.layout.simple_spinner_item);
                adapter_cu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                choice_instrument.setAdapter(adapter_cu);
            }
        });
        viento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayAdapter<CharSequence> adapter_ins = ArrayAdapter.createFromResource(getContext(),
                        R.array.spinnerWindInstruments, android.R.layout.simple_spinner_item);
                adapter_ins.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                choice_instrument.setAdapter(adapter_ins);
            }
        });
        percusion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayAdapter<CharSequence> adapter_per = ArrayAdapter.createFromResource(getContext(),
                        R.array.spinnerPercusionInstruments, android.R.layout.simple_spinner_item);
                adapter_per.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                choice_instrument.setAdapter(adapter_per);

            }
        });
        otros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayAdapter<CharSequence> adapter_oth = ArrayAdapter.createFromResource(getContext(),
                        R.array.spinnerOthers, android.R.layout.simple_spinner_item);
                adapter_oth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                choice_instrument.setAdapter(adapter_oth);
            }
        });

        //TODO: ICONO ELIMINR FALTA IMPLEMENTAR VISUALMENTE

        addInstrument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _instrument = (String) choice_instrument.getSelectedItem();
                float _expirience = experienceBar.getRating();
                viewModel.addInstrument(_instrument, _expirience);
            }
        });

       /* viewModel.getResponseAddedInstrument().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    Toast.makeText(getContext(), "Instrumento añadido correctamente", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "Error añadiendo el isntrumento", Toast.LENGTH_SHORT).show();
                }
            }
        });*/


        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;


    }

    private void initView() {
        rootView = LayoutInflater.from
                (getContext()).inflate(R.layout.dialog_add_instrument, null, false);

        cuerda = rootView.findViewById(R.id.family_cuerda);
        viento = rootView.findViewById(R.id.family_viento);
        percusion = rootView.findViewById(R.id.family_percusion);
        otros = rootView.findViewById(R.id.family_otros);

        choice_instrument = rootView.findViewById(R.id.spinner_instruments);

        experienceBar = rootView.findViewById(R.id.ratingBar_exp);
        addInstrument = rootView.findViewById(R.id.button_add_instrument);

    }




}
