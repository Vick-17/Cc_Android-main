package com.example.dawanlocations.ui.map;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.dawanlocations.R;
import com.example.dawanlocations.domain.model.Location;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.File;
import java.util.List;

/**
 * Activité principale affichant la carte OpenStreetMap via OSMDroid.
 *
 * <p>
 * Cette activité :
 * <ul>
 *     <li>Initialise une {@link MapView} OSMDroid.</li>
 *     <li>Observe la liste des {@link Location} depuis le {@link MapViewModel}.</li>
 *     <li>Affiche les repères (markers) correspondant aux centres de formation Dawan.</li>
 *     <li>Ouvre un {@link LocationDetailsBottomSheet} quand un utilisateur clique sur un marker.</li>
 * </ul>
 * </p>
 */
public class MapActivity extends AppCompatActivity {

    /** Référence vers la MapView OSMDroid */
    private MapView map;
    /** ViewModel exposant les données métier (liste des centres) */
    private MapViewModel vm;
    /** Bandeau affiché si l’application est hors ligne */
    private TextView offlineBanner;

    /**
     * Méthode de cycle de vie Android, appelée lors de la création de l’activité.
     *
     * <p>
     * Elle configure :
     * <ul>
     *     <li>Le layout {@code activity_map.xml}.</li>
     *     <li>La {@link MapView} OSMDroid.</li>
     *     <li>L’observation des données exposées par le {@link MapViewModel}.</li>
     *     <li>Un rafraîchissement initial via {@link MapViewModel#refresh}.</li>
     * </ul>
     * </p>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        offlineBanner = findViewById(R.id.offline_banner);

        // Configuration obligatoire pour OSMDroid (Android 9+ : User-Agent requis)
        Configuration.getInstance().setUserAgentValue(getPackageName());

        // Évite les soucis de permissions stockage en forçant le cache dans le répertoire interne
        File osmdroidBase = new File(getCacheDir(), "osmdroid");
        //noinspection ResultOfMethodCallIgnored
        osmdroidBase.mkdirs();
        Configuration.getInstance().setOsmdroidBasePath(osmdroidBase);
        Configuration.getInstance().setOsmdroidTileCache(new File(osmdroidBase, "tiles"));

        map = findViewById(R.id.map);
        map.setMultiTouchControls(true);
        map.setBuiltInZoomControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(5.0);
        mapController.setCenter(new GeoPoint(48.8566, 2.3522)); // France (Paris)

        // Initialisation du ViewModel (lié au cycle de vie de l’activité)
        vm = new ViewModelProvider(this).get(MapViewModel.class);

        // Observation des données LiveData : mise à jour de la carte à chaque changement
        vm.getLocations().observe(this, this::renderMarkers);

        // Déclenche un rafraîchissement initial (appel réseau + mise à jour DB)
        vm.refresh(new LocationRepositoryCallback());
    }

    /**
     * Ajoute des markers sur la carte pour chaque {@link Location}.
     *
     * @param list liste des centres de formation à afficher
     */
    private void renderMarkers(List<Location> list) {
        if (map == null || list == null) return;
        map.getOverlays().removeIf(overlay -> overlay instanceof Marker);

        for (Location loc : list) {
            GeoPoint point = new GeoPoint(loc.latitude, loc.longitude);
            Marker marker = new Marker(map);
            marker.setPosition(point);
            marker.setTitle(loc.name);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setRelatedObject(loc);
            marker.setOnMarkerClickListener((clickedMarker, mapView) -> {
                Object related = clickedMarker.getRelatedObject();
                if (related instanceof Location) {
                    LocationDetailsBottomSheet.newInstance((Location) related)
                            .show(getSupportFragmentManager(), "details");
                }
                return true;
            });
            map.getOverlays().add(marker);
        }
        map.invalidate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (map != null) map.onResume();
    }

    @Override
    protected void onPause() {
        if (map != null) map.onPause();
        super.onPause();
    }

    /**
     * Implémentation du callback du {@link com.example.dawanlocations.repository.LocationRepository}.
     *
     * <p>
     * Sert à afficher/masquer le bandeau "offline" en fonction :
     * <ul>
     *     <li>Succès du rafraîchissement → bandeau masqué.</li>
     *     <li>Erreur réseau ou API → bandeau affiché.</li>
     *     <li>Pas de connexion → bandeau affiché.</li>
     * </ul>
     * </p>
     */
    private class LocationRepositoryCallback implements com.example.dawanlocations.repository.LocationRepository.Callback {
        @Override public void onSuccess() { runOnUiThread(() -> offlineBanner.setVisibility(View.GONE)); }
        @Override public void onError(Throwable t) { runOnUiThread(() -> offlineBanner.setVisibility(View.VISIBLE)); }
        @Override public void onOffline() { runOnUiThread(() -> offlineBanner.setVisibility(View.VISIBLE)); }
    }
}
