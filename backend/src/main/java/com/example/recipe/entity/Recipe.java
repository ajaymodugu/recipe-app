import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cuisine;
    private String title;
    private Float rating;
    private Integer prepTime;
    private Integer cookTime;
    private Integer totalTime;
    private String description;

    @Column(columnDefinition = "JSON")
    private String nutrients;  // Store as JSON string

    private String serves;
}
