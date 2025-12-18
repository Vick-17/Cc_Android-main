package com.example.dawanlocations.data.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Fournit une instance unique (singleton) de {@link Retrofit}
 * configurée pour accéder à l’API distante.
 *
 * <p>
 * Cette classe encapsule la création et la configuration de Retrofit
 * afin d’assurer une seule instance réutilisée dans toute l’application.
 * </p>
 */
public final class RetrofitClient {

    /** URL de base de l’API Dawan. */
    private static final String BASE_URL = "https://dawan.org/";

    /** Instance unique (singleton) de Retrofit. */
    private static Retrofit instance;

    /**
     * Constructeur privé pour empêcher l’instanciation directe.
     */
    private RetrofitClient() {}

    /**
     * Retourne une instance unique de {@link Retrofit}.
     * <p>
     * L’appel est synchronisé pour être sûr qu’une seule instance
     * est créée même en cas d’accès concurrent.
     * </p>
     *
     * @return l’instance {@link Retrofit} configurée avec {@code BASE_URL}
     *         et le convertisseur Gson pour (dé)sérialiser les JSON.
     */
    public static synchronized Retrofit getInstance() {
        if (instance == null) {
            instance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return instance;
    }
}
