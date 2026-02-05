package sn.ods.obi.application.services.implement.shared.file;


import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sn.ods.obi.domain.enums.FileCode;
import sn.ods.obi.infrastructure.config.exceptions.APIException;
import sn.ods.obi.presentation.dto.responses.APIMessage;
import sn.ods.obi.presentation.dto.responses.FileRspDTO;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FileService implements IFile {

    @Value("${app.file.regex-splitter}")
    private String regexSplitter;

    @Value("${upload.path}")
    private String uploadPath;

    /**
     * Initializes the upload directory.
     *
     * @throws APIException if the directory cannot be created
     */
    @PostConstruct
    public void init() throws APIException {
        try {
            Files.createDirectories(Paths.get(uploadPath));
        } catch (IOException e) {
            throw new APIException(APIMessage.WS_AMAZON_ERROR, "Could not create upload folder!");
        }
    }

    /**
     * Stores a single file in the specified directory.
     *
     * @param file              the file to store
     * @param directory         the directory to store the file in
     * @param checkRegexSplitter whether to validate the file name format
     * @return the file response DTO
     * @throws APIException if the file is invalid or cannot be stored
     */
    @Override
    public FileRspDTO storeFile(MultipartFile file, String directory, boolean checkRegexSplitter) throws APIException {
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new APIException(APIMessage.REQUIRED_FILE, "Attached document");
        }

        String fileName = file.getOriginalFilename();
        Path root = Paths.get(uploadPath);
        ensureDirectoryExists(root);

        String key = checkRegexSplitter ? validateAndExtractFileCode(fileName) : "";
        fileName = cleanFileName(fileName);

        String extension = FilenameUtils.getExtension(fileName);
        String generatedName = generateUniqueFileName(extension);
        Path targetLocation = resolveTargetLocation(root, directory, generatedName);

        return saveFile(file, targetLocation, fileName, key, checkRegexSplitter, generatedName);
    }

    /**
     * Vérifie et crée le dossier racine s'il n'existe pas.
     */
    private void ensureDirectoryExists(Path root) throws APIException {
        if (!Files.exists(root)) {
            try {
                Files.createDirectories(root);
            } catch (IOException e) {
                throw new APIException(APIMessage.FILE_FORMAT_INCORRECT, "Failed to create root directory");
            }
        }
    }

    /**
     * Valide le format du fichier si checkRegexSplitter est activé et extrait le FileCode.
     */
    private String validateAndExtractFileCode(String fileName) throws APIException {
        List<String> parts = Arrays.asList(fileName.split(regexSplitter));
        if (parts.size() != 2) {
            throw new APIException(APIMessage.FILE_FORMAT_INCORRECT,
                    String.format("The format (%s) of the attachment files is incorrect!", fileName));
        }

        String key = parts.get(0);
        if (!FileCode.findByName(key)) {
            throw new APIException(APIMessage.NOT_FOUND, "FileCode " + key);
        }

        return key;
    }

    /**
     * Nettoie et valide le nom de fichier.
     */
    private String cleanFileName(String fileName) throws APIException {
        String cleanedFileName = StringUtils.cleanPath(fileName);
        if (cleanedFileName.contains("..")) {
            throw new APIException(APIMessage.FILE_FORMAT_INCORRECT, "Filename contains invalid path sequence " + fileName);
        }
        return cleanedFileName;
    }

    /**
     * Génère un nom de fichier unique.
     */
    private String generateUniqueFileName(String extension) {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return UUID.randomUUID() + "_" + timestamp + "." + extension;
    }

    /**
     * Résout l'emplacement cible du fichier.
     */
    private Path resolveTargetLocation(Path root, String directory, String generatedName) throws APIException {
        try {
            if (StringUtils.hasText(directory)) {
                Path subDirPath = root.resolve(directory);
                Files.createDirectories(subDirPath);
                return subDirPath.resolve(generatedName);
            }
            return root.resolve(generatedName);
        } catch (IOException e) {
            throw new APIException(APIMessage.FILE_FORMAT_INCORRECT, "Failed to create target directory");
        }
    }

    /**
     * Sauvegarde le fichier et retourne les métadonnées associées.
     */
    private FileRspDTO saveFile(MultipartFile file, Path targetLocation, String fileName, String key,
                                boolean checkRegexSplitter, String generatedName) throws APIException {
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);

            FileRspDTO.FileRspDTOBuilder responseBuilder = FileRspDTO.builder()
                    .originalName(fileName)
                    .fileType(file.getContentType())
                    .fileSize(file.getSize())
                    .fileCode(RandomStringUtils.randomAlphanumeric(8))
                    .generatedName(targetLocation.toString());

            if (checkRegexSplitter) {
                responseBuilder.fileCode(FileCode.valueOf(key.toUpperCase()).name());
            }

            return responseBuilder.build();
        } catch (IOException e) {
            throw new APIException(APIMessage.FILE_FORMAT_INCORRECT, "Failed to store file: " + fileName);
        }
    }

    /**
     * Stores multiple files in the specified directory.
     *
     * @param files             the files to store
     * @param directory         the directory to store the files in
     * @param checkRegexSplitter whether to validate the file name format
     * @return a list of file response DTOs
     * @throws APIException if any file is invalid or cannot be stored
     */
    @Override
    public List<FileRspDTO> storeMultipleFiles(MultipartFile[] files, String directory, boolean checkRegexSplitter) throws APIException {
        if (files == null || files.length == 0) {
            throw new APIException(APIMessage.REQUIRED_FILE, "Attached documents");
        }

        return Arrays.stream(files)
                .map(file -> {
                    try {
                        return storeFile(file, directory, checkRegexSplitter);
                    } catch (APIException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Loads a file as a resource.
     *
     * @param filename the name of the file to load
     * @param request  the HTTP request
     * @return the file as a resource
     * @throws APIException if the file cannot be read or does not exist
     */
    @Override
    public Resource load(String filename, HttpServletRequest request) throws APIException {
        try {
            Path file = Paths.get(uploadPath).resolve(filename).normalize();
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new APIException(APIMessage.FILE_FORMAT_INCORRECT, "Could not read the file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new APIException(APIMessage.FILE_FORMAT_INCORRECT, "Error: " + e.getMessage());
        }
    }

    /**
     * Deletes all files in the upload directory.
     */
    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(Paths.get(uploadPath).toFile());
    }

    /**
     * Lists all files in the upload directory.
     *
     * @return a list of file paths
     * @throws APIException if the files cannot be listed
     */
    @Override
    public List<Path> loadAll() throws APIException {
        Path root = Paths.get(uploadPath);

        if (!Files.exists(root)) {
            return Collections.emptyList();
        }

        try (Stream<Path> paths = Files.walk(root, 1)) {
            return paths.filter(path -> !path.equals(root))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new APIException(APIMessage.FILE_FORMAT_INCORRECT, "Could not list the files!");
        }
    }
}