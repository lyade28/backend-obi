package sn.ods.obi.presentation.web;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ods.obi.application.services.interfaces.utilisateur.UtilisateurService;
import sn.ods.obi.domain.model.utilisateur.Utilisateur;
import sn.ods.obi.presentation.dto.requests.utilisateur.UserReqForAdminDTO;
import sn.ods.obi.presentation.dto.requests.utilisateur.UserReqForUserDTO;
import sn.ods.obi.presentation.dto.responses.APIResponse;
import sn.ods.obi.presentation.dto.responses.Response;
import sn.ods.obi.presentation.mappers.utilisateur.UserMapperForAdminMapper;
import sn.ods.obi.presentation.mappers.utilisateur.UserMapperForUserMapper;

import java.util.Map;

/**
 * @author Abdou Karim CISSOKHO
 * @created 07/01/2025-13:04
 * @project obi
 */

@RestController
@RequestMapping("/utilisateur")

@RequiredArgsConstructor
public class UtilisateurResource {

    private  final UtilisateurService utilisateurService;

    private final UserMapperForAdminMapper userMapperForAdminMapper;
    private final UserMapperForUserMapper userMapperForUserMapper;



    @Operation(summary = "Endpoint permetant de faire l'ajout  d'un  nouveau  utilisateur")
    @PostMapping("/admin/add-user")
    public ResponseEntity<APIResponse> createUserFromAdmin(@RequestBody @Valid UserReqForAdminDTO dto) {



        Utilisateur user = utilisateurService.createUserFromAdmin(dto);

        APIResponse response = APIResponse.success(userMapperForAdminMapper.toDto(user));

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Endpoint permetant de faire l'ajout  d'un  nouveau  utilisateur")
    @PostMapping("/user/add-user")
    public ResponseEntity<APIResponse> createUserFromUser(@RequestBody @Valid UserReqForUserDTO dto) {


        System.out.println("dto " + dto);
        Utilisateur user = utilisateurService.createUserFromUser(dto);



        APIResponse response = APIResponse.success(userMapperForUserMapper.toDto(user));

        return ResponseEntity.ok(response);
    }




    @Operation(summary = "Endpoint permetant de faire une  modification  d'un   utilisateur à partir de  son id fourni")
    @PutMapping("/update-user/{id}")
    public ResponseEntity<APIResponse> updateUser(@PathVariable("id")Long id, @RequestBody @Valid UserReqForAdminDTO dto) {

        Utilisateur user = utilisateurService.updateUser(id, dto);

        APIResponse response = APIResponse.success(userMapperForAdminMapper.toDto(user));

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Endpoint pour récupérer  un   utilisateur à partir de  son id fourni")
    @GetMapping("/user-id/{id}")
    public ResponseEntity<APIResponse> getUser(@PathVariable("id")Long id) {

        Utilisateur user = utilisateurService.getUser(id);

        APIResponse response = APIResponse.success(userMapperForAdminMapper.toDto(user));

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Endpoint pour lister les  utilisateur avec filtre")
    @GetMapping(path = "/user/page")
    public Response<Object> getUserPage(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "filter", defaultValue = "") String filter
           ) {
        return utilisateurService.getUserPage(page, size, filter);
    }


    @Operation(summary = "Endpoint pour activer un compte creer par un utilisateur - Ajouter 'code'= 'chiffre' ")
    @PostMapping(path = "/activation")
    public ResponseEntity<APIResponse> activationUser(@RequestBody Map<String, String> activation) {

        utilisateurService.activation(activation);

        APIResponse response = APIResponse.success("Activation OK");

        return ResponseEntity.ok(response);
    }
}
