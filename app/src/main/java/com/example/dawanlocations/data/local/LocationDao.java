package com.example.dawanlocations.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * DAO (Data Access Object) pour accéder aux données de la table {@link LocationEntity}.
 *
 * <p>
 * Cette interface définit les opérations possibles sur la base locale Room
 * concernant les entités {@link LocationEntity}.
 * Room génère automatiquement l’implémentation à la compilation.
 * </p>
 */
@Dao
public interface LocationDao {

    /**
     * Récupère toutes les locations présentes dans la base locale.
     *
     * <p>
     * L’utilisation de {@link LiveData} permet d’observer automatiquement
     * les changements (insertion, suppression, mise à jour) dans la table,
     * et de mettre à jour l’UI en conséquence.
     * </p>
     *
     * @return une {@link LiveData} contenant la liste des {@link LocationEntity}.
     */
    @Query("SELECT * FROM locations")
    LiveData<List<LocationEntity>> getAllLive();

    /**
     * Insère une liste de {@link LocationEntity} dans la base.
     *
     * <p>
     * Si une entité existe déjà (même clé primaire), elle est remplacée grâce à
     * {@link OnConflictStrategy#REPLACE}.
     * </p>
     *
     * @param items liste d’objets {@link LocationEntity} à insérer.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<LocationEntity> items);

    /**
     * Supprime toutes les entrées de la table {@code locations}.
     */
    @Query("DELETE FROM locations")
    void clear();
}
