import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.io.File;
import java.util.Iterator;

@Component
public class JsonParserRunner implements CommandLineRunner {

    private final RecipeRepository repository;

    public JsonParserRunner(RecipeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File("US_recipes.json"));

        Iterator<JsonNode> recipes = root.elements();
        while (recipes.hasNext()) {
            JsonNode recipeJson = recipes.next();

            Recipe recipe = new Recipe();
            recipe.setCuisine(recipeJson.get("cuisine").asText());
            recipe.setTitle(recipeJson.get("title").asText());

            // Handle NaN
            double rating = recipeJson.get("rating").asDouble();
            recipe.setRating(Double.isNaN(rating) ? null : (float) rating);

            double prepTime = recipeJson.get("prep_time").asDouble();
            recipe.setPrepTime(Double.isNaN(prepTime) ? null : (int) prepTime);

            double cookTime = recipeJson.get("cook_time").asDouble();
            recipe.setCookTime(Double.isNaN(cookTime) ? null : (int) cookTime);

            double totalTime = recipeJson.get("total_time").asDouble();
            recipe.setTotalTime(Double.isNaN(totalTime) ? null : (int) totalTime);

            recipe.setDescription(recipeJson.get("description").asText());
            recipe.setNutrients(mapper.writeValueAsString(recipeJson.get("nutrients")));  // As JSON string
            recipe.setServes(recipeJson.get("serves").asText());

            repository.save(recipe);
        }
    }
}
