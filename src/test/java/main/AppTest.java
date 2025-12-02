package main;

import helperClasses.RateDrink;
import helperClasses.RecipeSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    private Liquid vodka;
    private Liquid oj;
    private Liquid syrup;
    private Mix testMix;
    private final String TEST_FILE_PATH = "test_recipes.ser";

    @BeforeEach
    public void setUp() {
        // Setup dummy data before each test (actually nice cocktail btw)
        vodka = new Liquid("vodka", "SPIRIT", 0.40, 0.1, 0.0, 5.0, 1.0);
        oj = new Liquid("orange_juice", "MIXER", 0.0, 0.1, 0.6, 2.0, 1.0);
        syrup = new Liquid("syrup", "MIXER", 0.0, 0.0, 1.0, 1.0, 0.5);
        testMix = new Mix();
    }

    @AfterEach
    public void tearDown() {
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }

    // ==========================================
    // CLASS 1 TEST: main.Mix
    // ==========================================

    @Test
    public void testMixAddLiquidAndGetVolume() {
        // Function 1: put()
        testMix.put(vodka, 0.05); // 50ml
        testMix.put(oj, 0.20);    // 200ml

        // Function 2: getCurrentVolume()
        assertEquals(0.25, testMix.getCurrentVolume(), 0.001, "Total volume should be sum of ingredients");
    }

    @Test
    public void testMixGetSpecificLiquidAmount() {
        testMix.put(vodka, 0.1);

        // Function 3: get(Liquid l)
        assertEquals(0.1, testMix.get(vodka), 0.001);
        assertEquals(0.0, testMix.get(oj), 0.001, "Should return 0 for liquid not in mix");
    }

    @Test
    public void testMixReset() {
        testMix.put(vodka, 0.1);
        assertFalse(testMix.getMix().isEmpty());

        // Function 4: reset()
        testMix.reset();

        assertEquals(0.0, testMix.getCurrentVolume(), 0.001);
        assertTrue(testMix.getMix().isEmpty(), "Mix should be empty after reset");
    }

    // ==========================================
    // CLASS 2 TEST: helperClasses.RateDrink
    // ==========================================

    @Test
    public void testRateDrinkCalculation() {
        // Create a basic drink
        testMix.put(vodka, 0.05);
        testMix.put(oj, 0.15);

        // Function 5: Constructor calculates the rates instantly
        RateDrink rater = new RateDrink(testMix);

        // Function 6: getRating()
        double rating = rater.getRating();
        assertTrue(rating >= 0 && rating <= 5.0, "Rating should be between 0 and 5");

        // Function 7: getAttributes()
        List<Double> stats = rater.getAttributes();
        assertEquals(4, stats.size(), "Should return 4 attributes (Taste, Fun, Creative, Crazy)");
    }

    @Test
    public void testRateDrinkReaction() {
        // Create a "Crazy" drink (Pure Alcohol) to force a specific reaction
        testMix.put(vodka, 0.5);

        RateDrink rater = new RateDrink(testMix);

        // Function 8: getReaction()
        String reaction = rater.getReaction();
        assertNotNull(reaction);
        // Based on logic: drink is very crazy, but not that creative and definately NOT tasty, so it should get a neutral reaction
        assertEquals("not_that_great_drink", reaction);
    }

    // ==========================================
    // CLASS 3 TEST: helperClasses.RecipeSerializer
    // ==========================================

    @Test
    public void testRecipeSerializationRoundTrip() throws IOException, ClassNotFoundException {
        // Prepare a recipe
        testMix.put(vodka, 0.05);
        testMix.put(syrup, 0.02);

        Recipe originalRecipe = new Recipe();
        originalRecipe.mix = testMix;
        originalRecipe.rating = 4.5;
        originalRecipe.reaction = "Tasty";

        // Function 9: saveRecipeToFile()
        RecipeSerializer.saveRecipeToFile(originalRecipe, TEST_FILE_PATH);

        File file = new File(TEST_FILE_PATH);
        assertTrue(file.exists(), "File should be created");

        // Function 10: getRecipeFromFile()
        List<Recipe> loadedRecipes = RecipeSerializer.getRecipeFromFile(TEST_FILE_PATH);

        assertNotNull(loadedRecipes);
        assertEquals(1, loadedRecipes.size());

        Recipe loadedRecipe = loadedRecipes.getFirst();

        // Verify contents
        assertEquals(originalRecipe.rating, loadedRecipe.rating, 0.001);
        assertEquals(originalRecipe.reaction, loadedRecipe.reaction);
        assertEquals(originalRecipe.mix.getCurrentVolume(), loadedRecipe.mix.getCurrentVolume(), 0.001);
    }
}