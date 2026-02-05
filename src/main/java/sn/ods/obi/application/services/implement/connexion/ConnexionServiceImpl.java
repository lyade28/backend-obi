package sn.ods.obi.application.services.implement.connexion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ods.obi.domain.model.connexion.Connexion;
import sn.ods.obi.domain.repository.connexion.ConnexionRepository;
import sn.ods.obi.application.services.interfaces.connexion.ConnexionService;
import sn.ods.obi.presentation.dto.connexion.ConnexionDTO;
import sn.ods.obi.presentation.dto.connexion.ConnexionSafeDTO;
import sn.ods.obi.presentation.dto.connexion.SchemaTableDTO;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnexionServiceImpl implements ConnexionService {

    private final ConnexionRepository connexionRepository;

    @Override
    public List<ConnexionSafeDTO> getConnexions(Long tenantId) {
        return connexionRepository.findByTenantId(tenantId).stream()
                .map(this::toSafeDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ConnexionSafeDTO> getConnexion(Long id) {
        return connexionRepository.findById(id).map(this::toSafeDTO);
    }

    @Override
    @Transactional
    public ConnexionSafeDTO createConnexion(ConnexionDTO dto) {
        Connexion c = Connexion.builder()
                .nom(dto.getNom())
                .type(dto.getType() != null ? dto.getType() : "PostgreSQL")
                .host(dto.getHost())
                .port(dto.getPort() != null ? dto.getPort() : 5432)
                .database(dto.getDatabase())
                .schema(dto.getSchema() != null ? dto.getSchema() : "public")
                .username(dto.getUsername())
                .password(encodePassword(dto.getPassword()))
                .tenantId(dto.getTenantId())
                .statutDb("Non vérifié")
                .build();
        c = connexionRepository.save(c);
        return toSafeDTO(c);
    }

    @Override
    @Transactional
    public ConnexionSafeDTO updateConnexion(Long id, ConnexionDTO dto) {
        Connexion c = connexionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Connexion non trouvée"));
        if (dto.getNom() != null) c.setNom(dto.getNom());
        if (dto.getType() != null) c.setType(dto.getType());
        if (dto.getHost() != null) c.setHost(dto.getHost());
        if (dto.getPort() != null) c.setPort(dto.getPort());
        if (dto.getDatabase() != null) c.setDatabase(dto.getDatabase());
        if (dto.getSchema() != null) c.setSchema(dto.getSchema());
        if (dto.getUsername() != null) c.setUsername(dto.getUsername());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) c.setPassword(encodePassword(dto.getPassword()));
        c = connexionRepository.save(c);
        return toSafeDTO(c);
    }

    @Override
    @Transactional
    public void deleteConnexion(Long id) {
        connexionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public boolean testConnexion(Long id) {
        Connexion c = connexionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Connexion non trouvée"));
        boolean ok = false;
        if ("PostgreSQL".equals(c.getType())) {
            String url = buildJdbcUrl(c);
            try (Connection conn = java.sql.DriverManager.getConnection(url, c.getUsername(), decodePassword(c.getPassword()))) {
                ok = conn.isValid(3);
            } catch (Exception e) {
                log.warn("Test connexion failed: {}", e.getMessage());
            }
        }
        c.setStatutDb(ok ? "OK" : "Erreur");
        c.setDerniereVerifDb(java.time.LocalDateTime.now());
        connexionRepository.save(c);
        return ok;
    }

    @Override
    public SchemaTableDTO getSchemas(Long id) {
        Connexion c = connexionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Connexion non trouvée"));
        if (!"PostgreSQL".equals(c.getType())) {
            return SchemaTableDTO.builder().connexion(c.getNom()).schema(c.getSchema()).tables(0).tableNames(List.of()).build();
        }
        String url = buildJdbcUrl(c);
        List<String> tables = new ArrayList<>();
        try (Connection conn = java.sql.DriverManager.getConnection(url, c.getUsername(), decodePassword(c.getPassword()))) {
            DatabaseMetaData meta = conn.getMetaData();
            try (ResultSet rs = meta.getTables(null, c.getSchema(), "%", new String[]{"TABLE", "VIEW"})) {
                while (rs.next()) {
                    tables.add(rs.getString("TABLE_NAME"));
                }
            }
        } catch (Exception e) {
            log.warn("Schema exploration failed: {}", e.getMessage());
        }
        return SchemaTableDTO.builder()
                .connexion(c.getNom())
                .schema(c.getSchema())
                .tables(tables.size())
                .tableNames(tables)
                .build();
    }

    private String buildJdbcUrl(Connexion c) {
        return switch (c.getType()) {
            case "MySQL" -> String.format("jdbc:mysql://%s:%d/%s", c.getHost(), c.getPort(), c.getDatabase());
            case "SQL Server" -> String.format("jdbc:sqlserver://%s:%d;databaseName=%s", c.getHost(), c.getPort(), c.getDatabase());
            case "Oracle" -> String.format("jdbc:oracle:thin:@%s:%d:%s", c.getHost(), c.getPort(), c.getDatabase());
            default -> String.format("jdbc:postgresql://%s:%d/%s?currentSchema=%s", c.getHost(), c.getPort(), c.getDatabase(), c.getSchema());
        };
    }

    private ConnexionSafeDTO toSafeDTO(Connexion c) {
        String statutDb = c.getStatutDb() != null ? c.getStatutDb() : "Non vérifié";
        String derniereVerif = c.getDerniereVerifDb() != null
                ? c.getDerniereVerifDb().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                : null;
        return ConnexionSafeDTO.builder()
                .id(c.getId())
                .nom(c.getNom())
                .type(c.getType())
                .host(c.getHost())
                .port(c.getPort())
                .database(c.getDatabase())
                .schema(c.getSchema())
                .username(c.getUsername())
                .tenantId(c.getTenantId())
                .statutDb(statutDb)
                .derniereVerifDb(derniereVerif)
                .build();
    }

    private String encodePassword(String pwd) {
        return pwd == null ? null : Base64.getEncoder().encodeToString(pwd.getBytes(StandardCharsets.UTF_8));
    }

    private String decodePassword(String encoded) {
        return encoded == null ? null : new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
    }
}
