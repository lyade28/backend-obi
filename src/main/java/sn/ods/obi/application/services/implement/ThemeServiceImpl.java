package sn.ods.obi.application.services.implement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ods.obi.application.services.interfaces.ThemeService;
import sn.ods.obi.domain.model.multitenant.TenantTheme;
import sn.ods.obi.domain.repository.multitenant.TenantRepository;
import sn.ods.obi.domain.repository.multitenant.TenantThemeRepository;
import sn.ods.obi.presentation.dto.multitenant.ThemeConfigDTO;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ThemeServiceImpl implements ThemeService {

    private final TenantThemeRepository themeRepository;
    private final TenantRepository tenantRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Optional<ThemeConfigDTO> getTheme(Long tenantId) {
        return themeRepository.findByTenantId(tenantId)
                .map(TenantTheme::getThemeJson)
                .filter(json -> json != null && !json.isBlank())
                .flatMap(this::parseThemeJson);
    }

    @Override
    @Transactional
    public ThemeConfigDTO saveTheme(Long tenantId, ThemeConfigDTO dto) {
        tenantRepository.findById(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant non trouvé"));
        String json = serializeTheme(dto);
        TenantTheme entity = themeRepository.findByTenantId(tenantId).orElse(null);
        if (entity == null) {
            entity = TenantTheme.builder()
                    .tenantId(tenantId)
                    .themeJson(json)
                    .build();
        } else {
            entity.setThemeJson(json);
        }
        themeRepository.save(entity);
        return dto;
    }

    private Optional<ThemeConfigDTO> parseThemeJson(String json) {
        try {
            ThemeConfigDTO dto = objectMapper.readValue(json, ThemeConfigDTO.class);
            return Optional.ofNullable(dto);
        } catch (JsonProcessingException e) {
            log.warn("Invalid theme JSON for tenant: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private String serializeTheme(ThemeConfigDTO dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Theme serialization failed", e);
        }
    }
}
