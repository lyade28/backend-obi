package sn.ods.obi.presentation.web.shared.file;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import sn.ods.obi.application.services.implement.shared.file.IFile;
import sn.ods.obi.infrastructure.config.exceptions.APIException;
import sn.ods.obi.presentation.dto.responses.APIResponse;
import sn.ods.obi.presentation.dto.responses.FileRspDTO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;


@RestController

@Slf4j
//@Tag(name = "FILE - MANAGEMENT - API", description = "Permet de charger et de télécharger des fichiers")
@Validated
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileResource {
    private final IFile iFile;

    @PostMapping(name = "/single", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE})
public ResponseEntity<APIResponse> uploadSingleFile(@RequestPart(name = "file") MultipartFile file, @RequestParam(required = false) String directory) throws APIException {

        FileRspDTO result = iFile.storeFile(file, directory, false);

        APIResponse response = APIResponse.success(result);
        return ResponseEntity.ok().body(response);
    }

/*    @PostMapping(name = "/multiple", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<SmartCareResponse> uploadMultipleFile(@RequestPart(name = "files") MultipartFile[] files, @RequestParam(required = false) String directory) throws SmartCareException {

        List<FileRspDTO> result = iFile.storeMultipleFiles(files, directory, false);

        SmartCareResponse response = SmartCareResponse.success(result);
        return ResponseEntity.ok().body(response);
    }*/

    @GetMapping("")
    public ResponseEntity<APIResponse> getListFiles() throws APIException {
        List<FileRspDTO> fileInfos = iFile.loadAll()
                .stream()
                .map(this::pathToFileData)
                .collect(Collectors.toList());

        APIResponse response = APIResponse.success(fileInfos);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public void delete() {
        iFile.deleteAll();
    }

    @GetMapping("/download")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@RequestParam String filename, HttpServletRequest request) throws APIException, IOException {
        Resource file = iFile.load(filename, request);

        String contentType = request.getServletContext().getMimeType(file.getFile().getAbsolutePath());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    private FileRspDTO pathToFileData(Path path) {
        FileRspDTO fileData = new FileRspDTO();
        String filename = path.getFileName()
                .toString();
        fileData.setGeneratedName(filename);
        fileData.setDownloadUrl(MvcUriComponentsBuilder.fromMethodName(FileResource.class, "getFile", filename)
                .build()
                .toString());
        try {
            fileData.setFileSize(Files.size(path));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error: " + e.getMessage());
        }

        return fileData;
    }
}
