package com.example.dawanlocations.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dawanlocations.R;
import com.example.dawanlocations.domain.model.Location;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * Fragment affiché en bas de l’écran (BottomSheet)
 * pour présenter les détails d’un {@link Location}.
 *
 * <p>
 * Il est déclenché lorsqu’un utilisateur clique sur un repère
 * (Marker) sur la Google Map. Les informations (nom, adresse,
 * code postal, ville) sont passées via un {@link Bundle}.
 * </p>
 */
public class LocationDetailsBottomSheet extends BottomSheetDialogFragment {

    // --- Clés des arguments transmis au fragment ---
    private static final String ARG_ID = "arg_id";
    private static final String ARG_NAME = "arg_name";
    private static final String ARG_ADDR = "arg_addr";
    private static final String ARG_CITY = "arg_city";
    private static final String ARG_PC = "arg_pc";

    /**
     * Fabrique une nouvelle instance du fragment avec les données d’un {@link Location}.
     *
     * <p>
     * On utilise un {@link Bundle} pour encapsuler les champs utiles
     * (id, nom, adresse, ville, code postal) et les transmettre
     * en arguments au fragment.
     * </p>
     *
     * @param loc objet {@link Location} dont on veut afficher les détails
     * @return une instance de {@link LocationDetailsBottomSheet} initialisée
     */
    public static LocationDetailsBottomSheet newInstance(Location loc) {
        Bundle b = new Bundle();
        b.putInt(ARG_ID, loc.id);
        b.putString(ARG_NAME, loc.name);
        b.putString(ARG_ADDR, loc.address);
        b.putString(ARG_CITY, loc.city);
        b.putString(ARG_PC, loc.postalCode);

        LocationDetailsBottomSheet f = new LocationDetailsBottomSheet();
        f.setArguments(b);
        return f;
    }

    /**
     * Création et initialisation de la vue du fragment.
     *
     * <p>
     * On récupère les arguments (via {@link #getArguments()}),
     * puis on remplit les TextView du layout
     * {@code fragment_location_details.xml}.
     * </p>
     *
     * <ul>
     *     <li>{@code title} : affiche le nom du centre (par défaut "Centre")</li>
     *     <li>{@code addr} : affiche l’adresse, suivie du code postal et de la ville</li>
     * </ul>
     *
     * @param inflater  utilisé pour "gonfler" le layout XML
     * @param container parent du fragment
     * @param savedInstanceState état sauvegardé si recréation
     * @return la vue racine du fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_location_details, container, false);

        Bundle args = getArguments();
        TextView title = v.findViewById(R.id.title);
        TextView addr = v.findViewById(R.id.address);

        if (args != null) {
            // Nom du centre
            title.setText(args.getString(ARG_NAME, "Centre"));

            // Construction de l’adresse complète
            String a = args.getString(ARG_ADDR, "");
            String pc = args.getString(ARG_PC, "");
            String c = args.getString(ARG_CITY, "");

            addr.setText(a + (pc.isEmpty() && c.isEmpty() ? "" : "\n" + pc + " " + c));
        }

        return v;
    }
}
