package com.example.dawanlocations.repository;

import androidx.lifecycle.LiveData;
import com.example.dawanlocations.domain.model.Location;

import java.util.List;

/**
 * Contrat du dépôt (Repository) pour accéder aux données des {@link Location}.
 *
 * <p>
 * Le repository agit comme une couche d’abstraction entre :
 * <ul>
 *     <li>la source locale (base de données Room)</li>
 *     <li>et la source distante (API REST via Retrofit)</li>
 * </ul>
 *
 * L’UI (ViewModel, Activity, Fragment) utilise uniquement cette interface
 * sans connaître les détails d’implémentation.
 * </p>
 */
public interface LocationRepository {

    /**
     * Retourne un flux observable des {@link Location}.
     *
     * <p>
     * Cette méthode renvoie un {@link LiveData} connecté à la base locale.
     * Ainsi, toute mise à jour (via {@link #refreshAsync(Callback)}) sera automatiquement
     * reflétée dans l’UI qui observe ce flux.
     * </p>
     *
     * @return un {@link LiveData} contenant la liste des {@link Location}
     */
    LiveData<List<Location>> getLocations();

    /**
     * Rafraîchit les données en lançant une récupération asynchrone
     * depuis l’API distante, puis en mettant à jour la base locale.
     *
     * @param callback un callback optionnel pour être notifié
     *                 du succès, d’une erreur, ou d’un état hors-ligne
     */
    void refreshAsync(Callback callback);

    /**
     * Interface de rappel pour notifier l’état du rafraîchissement.
     */
    interface Callback {

        /**
         * Appelé si la mise à jour a réussi.
         */
        void onSuccess();

        /**
         * Appelé si une erreur est survenue lors de la récupération
         * ou du traitement des données.
         *
         * @param t l’exception ou la cause de l’erreur
         */
        void onError(Throwable t);

        /**
         * Appelé si aucune connexion réseau n’est disponible.
         */
        void onOffline();
    }
}
