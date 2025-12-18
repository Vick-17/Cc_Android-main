package com.example.dawanlocations;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.dawanlocations.ui.map.MapActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test instrumenté simple (smoke test) pour vérifier
 * que l’écran principal {@link MapActivity} se lance correctement.
 *
 * <p>
 * Le but est de s’assurer que :
 * <ul>
 *     <li>l’activité démarre sans crash,</li>
 *     <li>le conteneur de la carte (View avec id {@code R.id.map}) est bien présent et visible.</li>
 * </ul>
 * </p>
 */
@RunWith(AndroidJUnit4.class)
public class MapActivitySmokeTest {

    /**
     * Règle JUnit qui démarre automatiquement {@link MapActivity}
     * avant chaque test et la ferme après.
     */
    @Rule
    public ActivityScenarioRule<MapActivity> rule =
            new ActivityScenarioRule<>(MapActivity.class);

    /**
     * Vérifie que l’application démarre bien et que
     * la vue contenant la carte est affichée à l’écran.
     */
    @Test
    public void appLaunch_showsMapContainer() {
        onView(withId(R.id.map))
                .check(matches(isDisplayed()));
    }
}
