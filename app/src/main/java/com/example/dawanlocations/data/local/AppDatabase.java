package com.example.dawanlocations.data.local;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Base de données locale de l’application utilisant Room.
 *
 * <p>
 * Cette classe définit la base SQLite qui stocke les entités (ici {@link LocationEntity}).
 * Elle fournit un accès centralisé aux DAO pour effectuer les opérations CRUD.
 * </p>
 *
 * <p>
 * Le pattern Singleton est utilisé pour s'assurer qu'une seule instance de la base
 * de données est créée et partagée dans toute l'application.
 * </p>
 */
@Database(entities = {LocationEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    /**
     * Fournit l'accès au DAO {@link LocationDao}, qui permet
     * d'interagir avec la table des {@link LocationEntity}.
     *
     * @return une instance de {@link LocationDao}.
     */
    public abstract LocationDao locationDao();

    /**
     * Instance unique de la base de données (pattern Singleton).
     * <p>
     * Utilise le mot-clé {@code volatile} pour garantir la visibilité des changements
     * entre threads.
     * </p>
     */
    private static volatile AppDatabase INSTANCE;

    /**
     * Récupère l’instance unique de la base de données.
     *
     * <p>
     * Si l’instance n’existe pas encore, elle est créée en utilisant
     * {@link Room#databaseBuilder(Context, Class, String)}.
     * L’accès est synchronisé pour éviter la création multiple
     * en environnement multi-threadé (double-checked locking).
     * </p>
     *
     * @param context contexte de l’application, utilisé pour accéder au système Android.
     * @return l’instance unique de {@link AppDatabase}.
     */
    public static AppDatabase get(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "dawan_locations.db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
