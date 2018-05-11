package app.http.controllers;

import app.http.pojos.CustomUserDetails;
import app.http.pojos.ImageUploadedResponse;
import app.pojo.Image;
import app.pojo.ImageSize;
import app.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@SuppressWarnings("unused")
@RestController
@RequestMapping(value = "/api/image")
public class ImageController {
    private final ImageService imageService;

    @Autowired
    public ImageController(final ImageService imageService) {
        this.imageService = imageService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ImageUploadedResponse uploadImage(
            final @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("file") MultipartFile file) throws IOException {
        return imageService.create(file.getContentType(), userDetails.getId(), file.getBytes());
    }

    @RequestMapping(value = "/{imageId}/{size}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getImageSize(@PathVariable(name = "imageId") final int imageId,
                                               @PathVariable(name = "size") final ImageSize size) {
        return getImageData(imageId, size);
    }

    @RequestMapping(value = "/{imageId}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getImageSize(@PathVariable(name = "imageId") final int imageId) {
        return getImageData(imageId, null);
    }


    private ResponseEntity<byte[]> getImageData(final int imageId, final ImageSize size) {
        final Image image = imageService.readById(imageId, size);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(image.getMimeType()));

        return new ResponseEntity<>(image.getData(), headers, HttpStatus.OK);
    }

}