package app.feedback.post.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    @Override
    public String saveImage(MultipartFile multipartFile) {

        if (multipartFile.isEmpty()) {
            return null;
        }

        String userHome = System.getProperty("user.dir");
        if (userHome.contains("/build/libs")) {
            userHome = userHome.replace("/build/libs", "");
        } else if (userHome.contains("\\build\\libs")) {
            userHome = userHome.replace("\\build\\libs", "");
        }

        Path path = Paths.get("image");

        File targetFolder = new File(userHome + File.separator + path);

        if (!targetFolder.exists()) {
            targetFolder.mkdirs();
        }

        String fileName = UUID.randomUUID() + "_" + System.currentTimeMillis();
        String originalFileName = multipartFile.getOriginalFilename();
        String ext = originalFileName.substring(originalFileName.lastIndexOf("."));

        String saveFileName = targetFolder + File.separator + originalFileName.substring(0, originalFileName.lastIndexOf(".")) + fileName + ext;
        String returnPath = "/image/" + originalFileName.substring(0, originalFileName.lastIndexOf(".")) + fileName + ext;

        try {
            multipartFile.transferTo(new File(saveFileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        returnPath = returnPath.replaceAll("\\\\", "/");
        log.info("Saved file path: {}", saveFileName);
        log.info("Return path: {}", returnPath);
        return returnPath;
    }
}
