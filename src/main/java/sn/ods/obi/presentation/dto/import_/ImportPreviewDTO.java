package sn.ods.obi.presentation.dto.import_;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportPreviewDTO {
    private Long importId;
    private List<String> columns;
    private List<Map<String, Object>> rows;
}
