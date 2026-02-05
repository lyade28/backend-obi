package sn.ods.obi.presentation.dto.favori;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriDTO {
    private Long id;
    private String type;
    private String entityId;
    private String label;
    private String url;
    private Long userId;
}
