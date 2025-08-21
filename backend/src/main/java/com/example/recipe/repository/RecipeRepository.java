import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    // Paginated and sorted by rating DESC
    Page<Recipe> findAllByOrderByRatingDesc(Pageable pageable);

    // Search query (dynamic based on params)
    @Query("SELECT r FROM Recipe r WHERE "
            + "(:title IS NULL OR r.title LIKE %:title%) "
            + "AND (:cuisine IS NULL OR r.cuisine = :cuisine) "
            + "AND (:rating IS NULL OR r.rating >= :rating) "  // Example for >=, adjust for operators
            + "AND (:totalTime IS NULL OR r.totalTime <= :totalTime)")  // Adjust operators as needed
    List<Recipe> searchRecipes(@Param("title") String title, @Param("cuisine") String cuisine,
                               @Param("rating") Float rating, @Param("totalTime") Integer totalTime);
    // Add more for calories (parse nutrients JSON if needed, e.g., using JSON_EXTRACT in query)
}
