package sn.ods.obi.application.services.implement.shared.file;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import sn.ods.obi.infrastructure.config.exceptions.APIException;
import sn.ods.obi.presentation.dto.responses.FileRspDTO;

import java.nio.file.Path;
import java.util.List;


public interface IFile {
    FileRspDTO storeFile(MultipartFile file, String directory, boolean checkRegexSplitter) throws APIException;

    List<FileRspDTO> storeMultipleFiles(MultipartFile[] files, String directory, boolean checkRegexSplitter) throws APIException;

    Resource load(String filename, HttpServletRequest request) throws APIException;

    void deleteAll();

    List<Path> loadAll() throws APIException;
}
