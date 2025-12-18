package com.example.dawanlocations.repository;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.dawanlocations.data.local.AppDatabase;
import com.example.dawanlocations.data.local.LocationDao;
import com.example.dawanlocations.data.local.LocationEntity;
import com.example.dawanlocations.data.remote.DawanApi;
import com.example.dawanlocations.data.remote.RetrofitClient;
import com.example.dawanlocations.data.remote.model.LocationDto;
import com.example.dawanlocations.domain.mapper.LocationMappers;
import com.example.dawanlocations.domain.model.Location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

/**
 * Implémentation concrète du {@link LocationRepository}.
 *
 * <p>
 * Cette classe combine :
 * <ul>
 *     <li>une source locale (base Room via {@link LocationDao})</li>
 *     <li>une source distante (API REST via {@link DawanApi})</li>
 * </ul>
 *
 * Elle orchestre la récupération, la persistance et l’exposition
 * des données de {@link Location} au reste de l’application.
 * </p>
 */
public class LocationRepositoryImpl implements LocationRepository {
    private final LocationDao dao;
    private final DawanApi api;
    private final ExecutorService io = Executors.newSingleThreadExecutor();
    private final Context appContext;

    /**
     * Construit un repository en initialisant la base locale et l’API distante.
     *
     * @param context contexte Android, utilisé pour obtenir la DB et vérifier la connectivité
     */
    public LocationRepositoryImpl(Context context) {
        this.appContext = context.getApplicationContext();
        this.dao = AppDatabase.get(appContext).locationDao();
        this.api = RetrofitClient.getInstance().create(DawanApi.class);
    }

    /**
     * Retourne un flux {@link LiveData} observant les entités locales.
     *
     * <p>
     * Les entités sont transformées en objets de domaine {@link Location}
     * grâce au mapper {@link LocationMappers}.
     * </p>
     *
     * @return un {@link LiveData} contenant la liste des {@link Location}
     */
    @Override
    public LiveData<List<Location>> getLocations() {
        return Transformations.map(dao.getAllLive(), LocationMappers::toDomainList);
    }

    /**
     * Rafraîchit les données depuis l’API distante et met à jour la base locale.
     *
     * <p>
     * Fonctionnement :
     * <ol>
     *     <li>Vérifie d’abord la connectivité avec {@link #isOnline()}.</li>
     *     <li>Si offline → callback.onOffline().</li>
     *     <li>Sinon, exécute un appel Retrofit synchrone à {@link DawanApi#getLocations()}.</li>
     *     <li>Si succès → mappe les DTO en entités Room, puis insère en transaction.</li>
     *     <li>Sinon → signale l’erreur au callback.</li>
     * </ol>
     * </p>
     *
     * @param callback interface de rappel notifiant succès, erreur ou hors-ligne
     */
    @Override
    public void refreshAsync(Callback callback) {
        io.execute(() -> {
            if (!isOnline()) {
                if (callback != null) callback.onOffline();
                return;
            }
            try {
                Response<List<LocationDto>> res = api.getLocations().execute();
                if (res.isSuccessful() && res.body() != null) {
                    List<LocationEntity> entities = new ArrayList<>();
                    for (LocationDto dto : res.body()) {
                        entities.add(LocationMappers.toEntity(dto));
                    }
                    AppDatabase.get(appContext).runInTransaction(() -> {
                        dao.clear();
                        dao.insertAll(entities);
                    });
                    if (callback != null) callback.onSuccess();
                } else {
                    if (callback != null) callback.onError(new IOException("HTTP " + res.code()));
                }
            } catch (Exception e) {
                if (callback != null) callback.onError(e);
            }
        });
    }

    /**
     * Vérifie si l’appareil est actuellement connecté à Internet.
     *
     * @return {@code true} si une connexion valide est détectée, {@code false} sinon
     */
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        Network active = cm.getActiveNetwork();
        if (active == null) return false;

        NetworkCapabilities caps = cm.getNetworkCapabilities(active);
        return caps != null
                && caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                && caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }
}
