package com.crack.global.config.fastapi;


import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ExternalApiService {

  private final WebClient webClient;

  public ExternalApiService(WebClient webClient) {
    this.webClient = webClient;
  }

  public String sendImageToFlask(MultipartFile image) throws IOException {
    log.info("[1] Flask API 전송 시작");

    // MultipartFile 정보를 로그로 출력
    log.info("[2] 업로드된 파일 이름: {}", image.getOriginalFilename());
    log.info("[3] 업로드된 파일 크기: {} bytes", image.getSize());

    // 파일을 ByteArrayResource로 변환
    ByteArrayResource resource = new ByteArrayResource(image.getBytes()) {
      @Override
      public String getFilename() {
        return image.getOriginalFilename();
      }
    };
    log.info("[4] ByteArrayResource 생성 완료");

    // 요청 바디 생성
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("image", resource);
    log.info("[5] Multipart 요청 바디 생성 완료");

    // Flask API 요청
    String response = webClient.post()
        .uri("http://sad_keldysh:5000/model/events")
        .contentType(MediaType.MULTIPART_FORM_DATA)
        .body(BodyInserters.fromMultipartData(body))
        .retrieve()
        .bodyToMono(String.class)
        .block();

    log.info("[6] Flask API 응답 수신 완료: {}", response);
    return response;
  }
}

