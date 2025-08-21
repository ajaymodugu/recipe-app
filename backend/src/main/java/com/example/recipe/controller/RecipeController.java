import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeRepository repository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllRecipes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Recipe> recipesPage = repository.findAllByOrderByRatingDesc(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("page", page);
        response.put("limit", limit);
        response.put("total", recipesPage.getTotalElements());
        response.put("data", recipesPage.getContent());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, List<Recipe>>> searchRecipes(
            @RequestParam(required = false) String calories,  // Parse operator e.g., <=400
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String cuisine,
            @RequestParam(required = false) String total_time,
            @RequestParam(required = false) String rating) {
        // Parse parameters with operators (e.g., for calories, split <=400 into operator and value)
        // For simplicity, assume >= for rating, <= for total_time; extend logic for full operators
        Float ratingVal = rating != null ? Float.parseFloat(rating.replace(">=", "")) : null;  // Basic parsing
        Integer totalTimeVal = total_time != null ? Integer.parseInt(total_time.replace("<=", "")) : null;

        List<Recipe> recipes = repository.searchRecipes(title, cuisine, ratingVal, totalTimeVal);
        // For calories, filter in memory or extend query

        return ResponseEntity.ok(Map.of("data", recipes));
    }
}
