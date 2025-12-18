package com.example.dawanlocations.ui.map;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.dawanlocations.domain.model.Location;
import com.example.dawanlocations.repository.LocationRepository;
import com.example.dawanlocations.repository.LocationRepositoryImpl;

import java.util.List;

/**
 * ViewModel dédié à l’écran de la carte ({@link MapActivity}).
 *
 * <p>
 * Son rôle est d’exposer à l’UI les données nécessaires
 * (liste des {@link Location}) et de déléguer les opérations
 * métiers (rafraîchissement depuis l’API) au {@link LocationRepository}.
 * </p>
 *
 * <p>
 * Il suit le principe MVVM :
 * <ul>
 *     <li>L’UI observe un {@link LiveData} fourni par ce ViewModel.</li>
 *     <li>Le ViewModel va chercher les données via le Repository.</li>
 *     <li>L’UI reste ainsi découplée de la logique métier et de persistance.</li>
 * </ul>
 * </p>
 */
public class MapViewModel extends AndroidViewModel {
    /** Référence vers le repository (accès données locales + distantes) */
    private final LocationRepository repo;
    /** Liste observable des centres (utilisée pour afficher les markers) */
    private final LiveData<List<Location>> locations;

    /**
     * Constructeur.
     *
     * <p>
     * Initialise le repository et récupère directement
     * le flux {@link LiveData} des {@link Location}.
     * </p>
     *
     * @param app application Android, nécessaire car {@link AndroidViewModel} a besoin d’un contexte
     */
    public MapViewModel(@NonNull Application app) {
        super(app);
        repo = new LocationRepositoryImpl(app);
        locations = repo.getLocations();
    }

    /**
     * Retourne la liste observable des centres de formation.
     *
     * <p>
     * Cette donnée est connectée à la base Room et se met
     * automatiquement à jour lorsque la base change.
     * </p>
     *
     * @return {@link LiveData} des {@link Location}
     */
    public LiveData<List<Location>> getLocations() {
        return locations;
    }

    /**
     * Demande un rafraîchissement depuis l’API distante.
     *
     * <p>
     * Cette méthode déclenche l’appel réseau et la mise à jour
     * de la base locale. Le {@link LocationRepository.Callback} permet de notifier
     * l’UI du résultat (succès, erreur ou offline).
     * </p>
     *
     * @param cb callback du repository
     */
    public void refresh(LocationRepository.Callback cb) {
        repo.refreshAsync(cb);
    }
}
