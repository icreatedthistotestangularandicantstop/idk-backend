package app.services;

import app.core.repos.ImageRepository;
import app.http.pojos.ImageUploadedResponse;
import app.pojo.Image;
import app.pojo.ImageSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

@Service
public class ImageService {
    private final ImageRepository imageRepository;

    @Autowired
    public ImageService(final ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Transactional
    public ImageUploadedResponse create(final String mimeType, final Integer userId, final byte[] data) {
        final Image image = new Image();
        image.setMimeType(mimeType);
        image.setUserId(userId);

        final int imageId = imageRepository.add(image);
        createFile(imageId, data);
        createImageScaledCopies(imageId);

        return new ImageUploadedResponse(imageId);
    }

    private void createFile(final int imageId, final byte[] data) {
        try {
            final File file = new File(getImageFile(imageId));
            file.createNewFile();
            final FileOutputStream out = new FileOutputStream(file);
            out.write(data);
            out.close();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createImageScaledCopies(final int imageId) {
        resize(getImageFile(imageId), getImageFile(imageId) + "_small", 100, 100);
        resize(getImageFile(imageId), getImageFile(imageId) + "_medium", 250, 250);
        resize(getImageFile(imageId), getImageFile(imageId) + "_big", 500, 500);

    }

    private String getImageFile(final int imageId) {
        return "/home/velizar/images/" + imageId;
    }

    public Image readById(final int id) {
        return readById(id, null);
    }

    public Image readById(final int id, final ImageSize size) {
        final String filename;
        switch (size) {
            case small:
                filename = getImageFile(id) + "_small";
                break;
            case medium:
                filename = getImageFile(id) + "_medium";
                break;
            case big:
                filename = getImageFile(id) + "_big";
                break;
            default:
                filename = getImageFile(id);
                break;
        }
        final Image image = imageRepository.findById(id);
        image.setData(getFile(filename));

        return image;
    }

    private byte[] getFile(final String filename) {
        try {
            // TODO remove hardcode
            final File file = new File(filename);
            if (!file.exists()) {
                return null;
            }

            final FileInputStream in = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            in.read(data);
            in.close();

            return data;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void resize(String inputImagePath, String outputImagePath, int scaledWidth, int scaledHeight) {
        try {
            File inputFile = new File(inputImagePath);
            BufferedImage inputImage = ImageIO.read(inputFile);

            BufferedImage outputImage = new BufferedImage(scaledWidth,
                    scaledHeight, inputImage.getType());

            Graphics2D g2d = outputImage.createGraphics();
            g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
            g2d.dispose();

            String formatName = "png";

            ImageIO.write(outputImage, formatName, new File(outputImagePath));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<Integer, Image> getImageForUsers(final Set<Integer> userIds) {
        final java.util.List<Image> images = imageRepository.findForUsers(userIds);
        final Map<Integer, Image> result = new HashMap<>();
        final Set<Integer> rUserIds = new HashSet<>();
        for (final Image image : images) {
            rUserIds.add(image.getUserId());
        }
        for (final Integer userId : userIds) {
            final java.util.List<Image> userImages = new ArrayList<>();
            for (final Image image : images) {
                if (image.getUserId() == userId) {
                    userImages.add(image);
                }
            }
            Image tImage = null;
            for (final Image image : userImages) {
                if (null == tImage || image.getCreatedAt() > tImage.getCreatedAt()) {
                    tImage = image;
                }
            }
            if (null == tImage) {
                continue;
            }

            result.put(userId, tImage);
        }

        return result;
    }

}
