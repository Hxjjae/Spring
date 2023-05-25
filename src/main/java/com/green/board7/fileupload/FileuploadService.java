package com.green.board7.fileupload;

import com.green.board7.fileupload.model.FileEntity;
import com.green.board7.fileupload.model.FileLoadDto;
import com.green.board7.fileupload.model.FileuploadInsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;

@Service
public class FileuploadService {
    private final FileuploadMapper mapper;

    @Autowired
    public FileuploadService(FileuploadMapper mapper) {
        this.mapper = mapper;
    }
    public Resource fileLoad(FileLoadDto dto) {
        FileEntity entity = mapper.selFileById(dto);
        try{
            File file = new File(fileDir + entity.getPath());
            Resource resource = new UrlResource(file.toURI());
            if(resource.exists()) {
                return resource;
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Value("${file.dir}")
    private String fileDir;

    public void fileUpload(FileuploadInsDto dto ,MultipartFile img) {
        System.out.println("fileDir : " + fileDir);

        //원래 파일 이름 추출
        String originFileName = img.getOriginalFilename();

        //파일 이름으로 사용할 uuid 생성
        String uuid = UUID.randomUUID().toString();

        int dotIdx = originFileName.lastIndexOf(".");
        String ext = originFileName.substring(dotIdx);

        String savedFileName = uuid + ext;
        String savedFilePath = fileDir + savedFileName;

        File file = new File(savedFilePath);
        try {
            img.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileEntity entity = FileEntity.builder()
                .path(savedFileName)
                .uploader(dto.getUploader())
                .levelValue(dto.getLevelValue())
                .build();
        mapper.insFile(entity);
    }
}
