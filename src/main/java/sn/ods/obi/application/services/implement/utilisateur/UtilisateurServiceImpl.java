package sn.ods.obi.application.services.implement.utilisateur;

import com.querydsl.core.BooleanBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import sn.ods.obi.application.services.interfaces.utilisateur.UtilisateurService;
import sn.ods.obi.application.services.interfaces.utilisateur.ValidationUserService;
import sn.ods.obi.application.services.implement.shared.file.INotificationService;
import sn.ods.obi.domain.model.utilisateur.Profile;
import sn.ods.obi.domain.model.utilisateur.QUtilisateur;
import sn.ods.obi.domain.model.utilisateur.Utilisateur;
import sn.ods.obi.domain.model.utilisateur.ValidationUser;
import sn.ods.obi.domain.repository.utilisateur.ProfilRepository;
import sn.ods.obi.domain.repository.utilisateur.UtilisateurRepository;
import sn.ods.obi.infrastructure.config.exceptions.APIException;
import sn.ods.obi.infrastructure.config.password.PasswordGenerator;
import sn.ods.obi.infrastructure.config.utils.UtilityClass;
import sn.ods.obi.presentation.dto.requests.authencation.LoginFormDTO;
import sn.ods.obi.presentation.dto.requests.utilisateur.UserReqForAdminDTO;
import sn.ods.obi.presentation.dto.requests.utilisateur.UserReqForUserDTO;
import sn.ods.obi.presentation.dto.responses.APIMessage;
import sn.ods.obi.presentation.dto.responses.Response;
import sn.ods.obi.presentation.dto.responses.utilisateur.UserResForAdminDTO;
import sn.ods.obi.presentation.mappers.utilisateur.UserMapperForAdminMapper;
import sn.ods.obi.presentation.mappers.utilisateur.UserMapperForUserMapper;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class  UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final INotificationService notificationService;
    private final UserMapperForAdminMapper userMapperForAdminMapper;
    private final UserMapperForUserMapper userMapperForUserMapper;

    private final ValidationUserService validationUserService;

    private final ProfilRepository profilRepository;

    public static final String FIRST_CONNEXION = "FIRST_CONNEXION";

    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public Utilisateur createUserFromAdmin(UserReqForAdminDTO dto) {

        try {
            Utilisateur utilisateur = userMapperForAdminMapper.toEntity(dto);

            Set<Profile> profiles = new HashSet<>();

            if (  utilisateurRepository.findUtilisateurByEmail(utilisateur.getEmail()).isPresent()) {
                throw   new APIException(APIMessage.EMAIL_ALREADY_EXISTS);
            }

            if(   utilisateurRepository.findByTelephone(utilisateur.getTelephone()).isPresent()) {
                throw new APIException(APIMessage.PHONE_NUMBER_ALREADY_EXIST);
            }

            dto.getProfiles().forEach(profile -> {
                Optional<Profile> profileDB = profilRepository.findByCode(profile.getCode());
                profileDB.ifPresent(profiles::add);
            });

            utilisateur.setProfiles(profiles);

            utilisateur.setFirstLog(true);
            utilisateur.setStatus(true);

            if (new UtilityClass.EmailUtility().validate(dto.getEmail())) {

                utilisateur.setEmail(dto.getEmail());
            } else {
                throw new APIException(APIMessage.EMAIL_NOT_VALID);
            }

            String password = PasswordGenerator.generateRandomString();
            log.info("................password: = {}", password);

            utilisateur.setPassword(passwordEncoder.encode(password));


            var userSaved =  utilisateurRepository.save(utilisateur);

                notificationService.sendNotificationToNewUserRegistred(
                        new LoginFormDTO(userSaved.getEmail(), password), FIRST_CONNEXION);

            return userSaved;
        }catch (Exception e) {
            throw new RuntimeException("Exception: " + e.getMessage());
        }


    }

    @Override
    public Utilisateur createUserFromUser(UserReqForUserDTO dto) {
       try {
           Utilisateur utilisateur = userMapperForUserMapper.toEntity(dto);

           if (  utilisateurRepository.findUtilisateurByEmail(utilisateur.getEmail()).isPresent()) {
               throw   new APIException(APIMessage.EMAIL_ALREADY_EXISTS);
           }

           if(   utilisateurRepository.findByTelephone(utilisateur.getTelephone()).isPresent()) {
               throw new APIException(APIMessage.PHONE_NUMBER_ALREADY_EXIST);
           }


           Set<Profile> profiles = new HashSet<>();

           dto.getProfiles().forEach(profile -> {
               Optional<Profile> profileDB = profilRepository.findByCode(profile.getCode());
               profileDB.ifPresent(profiles::add);
           });

           utilisateur.setProfiles(profiles);

           utilisateur.setFirstLog(true);
           utilisateur.setStatus(false);



           if (new UtilityClass.EmailUtility().validate(dto.getEmail())) {

               utilisateur.setEmail(dto.getEmail());
           } else {
               throw new APIException(APIMessage.EMAIL_NOT_VALID);
           }



          validationUserService.validateUser(utilisateur);

          return utilisateurRepository.save(utilisateur);
       }catch (Exception e) {
         throw new RuntimeException("Exception: " + e.getMessage());
       }
    }

    @Override
    public Utilisateur updateUser(Long id, UserReqForAdminDTO dto) {

       try {
           Utilisateur utilisateur = utilisateurRepository.findById(id)
                   .orElseThrow(() -> new APIException(APIMessage.ACCOUNT_NOT_FOUND));


           utilisateur.setNom(dto.getNom());
           utilisateur.setPrenom(dto.getPrenom());
           utilisateur.setAdresse(dto.getAdresse());
           utilisateur.setTelephone(dto.getTelephone());
           utilisateur.setDateNaissance(dto.getDateNaissance());
           utilisateur.setSexe(dto.getSexe());
           utilisateur.setLieuDeNaissance(dto.getLieuDeNaissance());


           Set<Profile> profiles = new HashSet<>();
           dto.getProfiles().forEach(profile -> {
               Optional<Profile> profileDB = profilRepository.findByCode(profile.getCode());
               profileDB.ifPresent(profiles::add);
           });

           utilisateur.setProfiles(profiles);

           return utilisateurRepository.save(utilisateur);
       }catch (Exception e) {
           throw new RuntimeException("Exception: " + e.getMessage());
       }
    }

    @Override
    public Utilisateur getUser(Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new APIException(APIMessage.ACCOUNT_NOT_FOUND));
    }

    @Override
    public Response<Object> getUserPage(int page, int size, String filter) {

        Page<UserResForAdminDTO> utilisateurResDTOPage;
        BooleanBuilder builder = new BooleanBuilder();


        if (StringUtils.isNotBlank(filter)) {

            builder.andAnyOf(
                    QUtilisateur.utilisateur.email.containsIgnoreCase(filter),
                    QUtilisateur.utilisateur.prenom.containsIgnoreCase(filter),
                    QUtilisateur.utilisateur.nom.containsIgnoreCase(filter),
                    QUtilisateur.utilisateur.telephone.containsIgnoreCase(filter),
                    QUtilisateur.utilisateur.adresse.containsIgnoreCase(filter),
                    QUtilisateur.utilisateur.sexe.containsIgnoreCase(filter),
                    QUtilisateur.utilisateur.lieuDeNaissance.containsIgnoreCase(filter));

        }

        utilisateurResDTOPage = Objects.nonNull(builder.getValue()) ? utilisateurRepository
                .findAll(builder.getValue(), PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")))
                .map(userMapperForAdminMapper::toDto)
                : utilisateurRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")))
                .map(userMapperForAdminMapper::toDto);

        Response.PageMetadata pageMetadata = Response.PageMetadata.builder()
                .size(utilisateurResDTOPage.getSize())
                .number(utilisateurResDTOPage.getNumber())
                .totalElements(utilisateurResDTOPage.getTotalElements())
                .totalPages(utilisateurResDTOPage.getTotalPages())
                .build();

        return Response.ok().setPayload(utilisateurResDTOPage.getContent()).setMetadata(pageMetadata)
                .setMessage("Liste des utilisateurs");

    }

    @Override
    public void activation(Map<String, String> activation) {


            ValidationUser validationUser =validationUserService.readCode(activation.get("code"));

            if(Instant.now().isAfter(validationUser.getExpiration())){

                throw new APIException(APIMessage.INVALID_CODE_EXPIRED);
            }

            Utilisateur utilisateurActivate =   utilisateurRepository.findById(validationUser.getUser().getId())
                    .orElseThrow(() -> new APIException(APIMessage.ACCOUNT_ALREADY_EXIST));

            if(Boolean.TRUE.equals(utilisateurActivate.getStatus())){

                throw new APIException(APIMessage.ACCOUNT_ALREADY_ACTIVATED);
            }
            utilisateurActivate.setStatus(true);

            String password = PasswordGenerator.generateRandomString();
            log.info("................password: = {}", password);

            utilisateurActivate.setPassword(passwordEncoder.encode(password));

            Utilisateur activatedUser = utilisateurRepository.save(utilisateurActivate);

            notificationService.sendNotificationToNewUserRegistred(
                    new LoginFormDTO(activatedUser.getEmail(), password), FIRST_CONNEXION);



    }
}
