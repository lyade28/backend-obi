package sn.ods.obi.presentation.dto.multitenant;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

/**
 * DTO aligné sur le frontend (ThemeConfig) : palette et logo par organisation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ThemeConfigDTO {

    private String primary;
    private String secondary;
    private String accent;
    private String background;
    private String surface;
    private String text;
    private String success;
    private String warning;
    private String danger;
    private List<String> chartColors;
    private String logoUrl;
}
