package sn.ods.obi.presentation.dto.dataset;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryResultDTO {
    private List<String> columns;
    private List<List<Object>> rows;
}
