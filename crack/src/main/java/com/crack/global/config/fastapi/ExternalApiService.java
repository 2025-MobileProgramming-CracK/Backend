package com.crack.global.config.fastapi;


import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

@Service
public class ExternalApiService {

  private final WebClient webClient;

  public ExternalApiService(WebClient webClient) {
    this.webClient = webClient;
  }

  public Mono<Boolean> sendImageToFastApi(MultipartFile image) {
    try {
      ByteArrayResource resource = new ByteArrayResource(image.getBytes()) {
        @Override
        public String getFilename() {
          return image.getOriginalFilename();
        }
      };

      MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
      body.add("image", resource);

      return webClient.post()
          .uri("http://컨테이너이름:8000/api/image-check")
          .contentType(MediaType.MULTIPART_FORM_DATA)
          .body(BodyInserters.fromMultipartData(body))
          .retrieve()
          .bodyToMono(Boolean.class)
          .onErrorReturn(false);

    } catch (Exception e) {
      return Mono.just(false);
    }
  }
}
