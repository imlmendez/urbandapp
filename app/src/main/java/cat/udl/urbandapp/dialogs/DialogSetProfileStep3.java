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
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import cat.udl.urbandapp.R;
import cat.udl.urbandapp.models.MusicalGenere;
import cat.udl.urbandapp.recyclerview.GenereDiffCallback;
import cat.udl.urbandapp.recyclerview.GeneresAdapter;
import cat.udl.urbandapp.viewmodel.MusicalGenreViewModel;
import cat.udl.urbandapp.viewmodel.UserViewModel;

    public class DialogSetProfileStep3 extends DialogFragment {

        public View rootView;
        private FragmentActivity activity;
        private FloatingActionButton add_generes;
        private RecyclerView recyclerView_generes;
        private MusicalGenreViewModel musicalGenreViewModel;
        private UserViewModel userViewModel;

        public static DialogSetProfileStep3 newInstance(FragmentActivity activity) {
            DialogSetProfileStep3 dialog = new DialogSetProfileStep3();
            dialog.activity = activity;
            return dialog;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            musicalGenreViewModel = new MusicalGenreViewModel(getActivity().getApplication());
            userViewModel = new UserViewModel(getActivity().getApplication());
            initView();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setPositiveButton("Finish the set up", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                        userViewModel.firstSetUpDone();
                }
            });
            builder.setNegativeButton("Prevous Step", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DialogSetProfileStep2 dialog2 = DialogSetProfileStep2.newInstance(getActivity());
                    dialog2.show(getParentFragmentManager(),"probando");
                }
            });
            // Set other dialog properties

            final GeneresAdapter genereAdapter = new GeneresAdapter(new GenereDiffCallback(), musicalGenreViewModel, this.getActivity());
            recyclerView_generes.setAdapter(genereAdapter);

            musicalGenreViewModel.getListGeneres();

            musicalGenreViewModel.getGeneres().observe(this, new Observer<List<MusicalGenere>>() {
                @Override
                public void onChanged(List<MusicalGenere> i) {
                    genereAdapter.submitList(i);
                }
            });

            musicalGenreViewModel.getResponseChangedList().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean added) {
                    Log.d("Genres", "List actualized: " + added);
                    if (added) {
                        musicalGenreViewModel.getListGeneres();
                    }
                }
            });

            AlertDialog alertDialog = builder.setView(rootView)
                    .setCancelable(true)
                    .create();

            alertDialog.setCanceledOnTouchOutside(false);
            return alertDialog;
        }

        //inicialtizar la vista del dialog
        private void initView() {
            rootView = LayoutInflater.from
                    (getContext()).inflate(R.layout.dialog_set_profile_step3, null, false);
            recyclerView_generes = rootView.findViewById(R.id.recyclerView_generes);
            add_generes = rootView.findViewById(R.id.imageView_addGenere);

            recyclerView_generes.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView_generes.setHasFixedSize(true);

            add_generes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogAddGenere dialogAddGenere = DialogAddGenere.newInstance(getActivity(), musicalGenreViewModel);
                    dialogAddGenere.show(getParentFragmentManager(), "probando");
                }
            });
        }
}
