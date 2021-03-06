package cat.udl.urbandapp.dialogs;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import cat.udl.urbandapp.R;
import cat.udl.urbandapp.models.Instrument;
import cat.udl.urbandapp.preferences.PreferencesProvider;
import cat.udl.urbandapp.recyclerview.InstrumentAdapter;
import cat.udl.urbandapp.recyclerview.InstrumentDiffCallback;
import cat.udl.urbandapp.viewmodel.InstrumentsViewModel;

public class DialogSetProfileStep2 extends DialogFragment {

    private String TAG = "DialogSetProfileStep2";

    public View rootView;
    private FragmentActivity activity;
    private FloatingActionButton addInstrument;
    private SharedPreferences mPreferences;
    private InstrumentsViewModel instrumentsViewModel;
    private RecyclerView recyclerInstruments;

    public static DialogSetProfileStep2 newInstance(FragmentActivity activity) {
        DialogSetProfileStep2 dialog = new DialogSetProfileStep2();
        dialog.activity = activity;
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        instrumentsViewModel = new InstrumentsViewModel(getActivity().getApplication());
        mPreferences = PreferencesProvider.providePreferences();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("Next Step", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DialogSetProfileStep3 dialog3 = DialogSetProfileStep3.newInstance(getActivity());
                dialog3.show(getParentFragmentManager(), "probando");
            }
        });
        builder.setNegativeButton("Prevous Step", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DialogSetProfileStep1 dialog1 = DialogSetProfileStep1.newInstance(getActivity());
                dialog1.show(getParentFragmentManager(), "probando");
            }
        });
        initView();

        AlertDialog alertDialog = builder.setView(rootView)
                .setCancelable(true)
                .create();

        final InstrumentAdapter instrumentAdapter = new InstrumentAdapter(new InstrumentDiffCallback(), instrumentsViewModel, this.getActivity());
        recyclerInstruments.setAdapter(instrumentAdapter);

        instrumentsViewModel.getInstruments().observe(this, new Observer<List<Instrument>>() {
            @Override
            public void onChanged(List<Instrument> i) {
                instrumentAdapter.submitList(i);
            }
        });

        instrumentsViewModel.getResponseChangedList().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean added) {
                if (added) {
                    instrumentsViewModel.getListInstruments();
                }
            }
        });

        addInstrument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAddInstrument dialogAddInstrument = DialogAddInstrument.newInstance(getActivity(), instrumentsViewModel);
                dialogAddInstrument.show(getParentFragmentManager(), "probando");
            }
        });

        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }

    private void initView() {
        rootView = LayoutInflater.from
                (getContext()).inflate(R.layout.dialog_set_profile_step2, null, false);
        addInstrument = rootView.findViewById(R.id.addInstrument);
        recyclerInstruments = rootView.findViewById(R.id.recyclerView_instruments);

        recyclerInstruments.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerInstruments.setHasFixedSize(true);

    }

}