package com.example.dawanlocations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.number.OrderingComparison.greaterThan;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.dawanlocations.data.remote.DawanApi;
import com.example.dawanlocations.data.remote.model.LocationDto;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Test instrumenté "live" qui interroge directement l’API REST Dawan.
 *
 * <p>
 * Le but de ce test est de vérifier :
 * <ul>
 *     <li>que l’API est joignable,</li>
 *     <li>que la réponse HTTP est un succès (code 2xx),</li>
 *     <li>que le corps de la réponse n’est pas null,</li>
 *     <li>et que la liste retournée contient au moins un élément.</li>
 * </ul>
 * </p>
 */
@RunWith(AndroidJUnit4.class)
public class DawanApiLiveTest {

    /**
     * Vérifie que l’appel à {@link DawanApi#getLocations()} fonctionne
     * et renvoie une liste non vide.
     */
    @Test
    public void api_returnsLocations_success() throws Exception {
        // Initialisation d’un client Retrofit pointant sur https://dawan.org/
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dawan.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Création d’un proxy de l’interface DawanApi
        DawanApi api = retrofit.create(DawanApi.class);

        // Appel synchrone à l’API
        Response<List<LocationDto>> res = api.getLocations().execute();

        // Vérifications avec Hamcrest
        assertThat("HTTP doit être 2xx", res.isSuccessful(), is(true));
        assertThat("Body ne doit pas être null", res.body(), notNullValue());
        assertThat("Liste non vide", res.body().size(), greaterThan(0));
    }
}
