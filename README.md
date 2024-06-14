# Backend Readme

## 기술 스택

Java, Springboot, Mysql, Docker, AWS, Naver-Cloud-Platform

## ERD

[https://www.erdcloud.com/d/pQ8SunX3mpNENeD9R](https://www.erdcloud.com/d/pQ8SunX3mpNENeD9R)

![title](https://private-user-images.githubusercontent.com/100981076/339589798-b27b1fb2-92cb-4984-8084-6e0437780ae7.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MTgzMjU0NjgsIm5iZiI6MTcxODMyNTE2OCwicGF0aCI6Ii8xMDA5ODEwNzYvMzM5NTg5Nzk4LWIyN2IxZmIyLTkyY2ItNDk4NC04MDg0LTZlMDQzNzc4MGFlNy5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQwNjE0JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MDYxNFQwMDMyNDhaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT04NGI5MWM3NzQzNjQ3YzYxNDA4ZGExYzIxZmNjYTAzMTE2YzA0YjMyZjVjY2IzZjEwMWI5YTAxNTUxODU4MmUxJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCZhY3Rvcl9pZD0wJmtleV9pZD0wJnJlcG9faWQ9MCJ9.uCJXd80DQaa-3iCbRSkee8loBowiMp7Q5xOmpeHPwBM)   


## API 명세서

[https://handy-lycra-4c6.notion.site/API-571c137e7e0a4a16b90b32560792cfda?pvs=4](https://www.notion.so/API-571c137e7e0a4a16b90b32560792cfda?pvs=21)

![title](https://private-user-images.githubusercontent.com/100981076/339589823-32860547-187a-45c7-b905-de2ade2c83fd.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MTgzMjU0NzQsIm5iZiI6MTcxODMyNTE3NCwicGF0aCI6Ii8xMDA5ODEwNzYvMzM5NTg5ODIzLTMyODYwNTQ3LTE4N2EtNDVjNy1iOTA1LWRlMmFkZTJjODNmZC5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQwNjE0JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MDYxNFQwMDMyNTRaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT0xNzM1YmE5Mjc2ZDkxZDAzMzY5ZWFlYWVkZDdjZjQ5Nzk2N2NhZTcyYzkyMGQyNjk3MmI3NGFiNWUxMWExMzUwJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCZhY3Rvcl9pZD0wJmtleV9pZD0wJnJlcG9faWQ9MCJ9.7ZsDGXmCS3nv2g0KPqlxOlDERZTtxR-TwVNvFs5xEsc)   


## 역할 분담

- 장욱
    - Restful 기반 API 명세서 작성.
    - JWT를 사용한 회원 관리 기능 개발 (Access Token, Refresh Token).
    - 회원 관리 기능 구현 (회원 가입, 로그인, 사용자 정보 수정 등)
    - Docker 파일 관리 및 Docker Compose를 사용한 컨테이너 관리.
    - AWS EC2, Naver-Cloud-Platfom를 사용한 서버 배포
- 김동욱
    - ERD 작성
    - 게스트하우스 관리 기능 구현 (상세 페이지, 추천 게스트하우스, 리뷰, 평점 등)
    - 이미지 DB에 저장 및 불러오기 구현

## 신경 쓴 부분

- **보안성 강화를 위한 JWT 토큰 사용** : 사용자 인증과 인증 처리에 JWT를 사용하여 보안성을 강화하였습니다. 토큰 기능 구현 시, 다중 토큰 관리 및 서버 측에서 Refresh 토큰의 제어권을 갖도록함으로써 보안 강도를 높였습니다.
- **컨테이너화를 통한 환경 일관성 및 배포 용이성 보장**: Docker를 사용하여 개발 환경과 운영 환경의 일관성을 보장하고, 배포 프로세스를 간소화했습니다. Docker Compose를 사용하여 여러 컨테이너를 관리하고, 필요한 서비스를 쉽게 배포 및 확장할 수 있게 만들었습니다.
- **클라우드 플랫폼 활용**: AWS EC2와 Naver Cloud Platform을 통해 서비스를 배포하였습니다.클라우드 서비스를 사용함으로써, 서버 운영의 유연성을 향상시키고 비용을 최적화할 수 있었습니다.
- **ERD 작성** : 기획 단계에서 ERD를 설계할 때 유연한 api 처리를 고려하며 작성하였습니다.
- **이미지 처리 :** 이미지를 저장하고 불러올 때 Base64 인코딩을 통해 이미지 데이터 처리하였습니다.

## 트러블 슈팅

- **한글 파일 이미지 저장 및 find** : 한글 입력 방식은 완성형과 조합형 두 가지가 있습니다. 한글에 대해서 DB에 저장하고 불러오려면 UTF-8로 인코딩하기 전에 완성형 또는 조합형으로 정규화를 해주는 작업이 필요합니다. Postman에서 테스트를 진행할 때는 조합형으로 진행했습니다. (postman에선 저장할 때 기본적으로 조합형인듯 합니다.)
- 클라우드 환경 배포 시 사진 파일 불러오기 : Springboot 코드 빌드 후 .jar에서 이미지를 불러오는 과정에서 IOException 문제가 발생하였습니다. 이 문제를 해결하기 위해 Docker image build 시 COPY 명령어를 통해 Docker 서버 내부에 사진 파일들을 따로 저장한 후 코드 상에서 상대 경로를 설정하여 Docker 파일 내부를 조회하도록 하였습니다.

## 시작 가이드

<aside>
💡 AWS EC2(Springboot), Naver-Cloud-Platform(DB)에 서버를 배포 했었으나 프리티어 만료로 인해 배포를 중단하였습니다. Backed 서버를 실행시킬 수 있도록 Docker hub에 Springboot Image를 업로드 하였습니다. 서버를 실행시키기 위해서는 Image를 pull 받아 docker 컨테이너를 실행시켜야합니다.

</aside>
</br>


1. **Springboot image pull 받기**
    
    docker image를 다운 받기 위해서는 터미널에서 아래 명령어를 입력해야합니다.
    
    ```docker
    docker pull ukjang/hansumproject-spring-boot-app:latest
    ```
    

image를 pull 받으셨다면 `docker images`  명령어를 통해 `ukjang/hansumproject-spring-boot-app` image가 잘 받아왔는지 확인해주세요. 

![title](https://private-user-images.githubusercontent.com/100981076/339589852-1cfc4ccb-ad08-4ec7-86ad-73e9f1effe47.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MTgzMjU0OTEsIm5iZiI6MTcxODMyNTE5MSwicGF0aCI6Ii8xMDA5ODEwNzYvMzM5NTg5ODUyLTFjZmM0Y2NiLWFkMDgtNGVjNy04NmFkLTczZTlmMWVmZmU0Ny5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQwNjE0JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MDYxNFQwMDMzMTFaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT0xMGFkNzBmNWNlM2YyZjFmNTY5N2QyNjhiMTI4N2Q4YzAzNWNlOGIwZWI0YTllZjI3NDg2OWVjNmU4MTZkNzg0JlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCZhY3Rvcl9pZD0wJmtleV9pZD0wJnJlcG9faWQ9MCJ9.KLQbkU-i170XWKqgpaZ1X05vwU1Hwg5ULccButi_Qdk)   


1. **mysql image pull 받기**
    
    DB로 mysql를 사용하고 있으므로 mysql image도 pull 받아야합니다.
    
    아래 명령어를 통해 image를 pull 받아주세요.
    
    ```docker
    docker pull mysql
    ```
    

image를 pull 받았다면 `docker images` 명령어를 통해 mysql image가 잘 받아와졌는지 확인해주세요.

![title](https://private-user-images.githubusercontent.com/100981076/339589867-28da6fae-e155-4551-b1fe-57ffd3ec0d46.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MTgzMjU1MDAsIm5iZiI6MTcxODMyNTIwMCwicGF0aCI6Ii8xMDA5ODEwNzYvMzM5NTg5ODY3LTI4ZGE2ZmFlLWUxNTUtNDU1MS1iMWZlLTU3ZmZkM2VjMGQ0Ni5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQwNjE0JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MDYxNFQwMDMzMjBaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT0wYmI1ODRkYThmZDRhZmZhNjAxYjU0YjhmYjQ1ZTRhZjNjNGJiMGQwMWNiY2UyZGZmOGUwN2E5NWFhMGQyZmY1JlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCZhY3Rvcl9pZD0wJmtleV9pZD0wJnJlcG9faWQ9MCJ9.ARoMdL3p8HjIwjXmSIB-pS1cdhKtbbZVCCNqWx_K6JY)   


1. **Docker Compose로 컨테이너 실행시키기**
    
    Backend 실행시키기 위한 image를 모두 pull 받았으니 이제 docker compose를 Image들을 실행시켜야합니다.
    

임의의 디렉토리(폴더)를 하나 만든 후 `docker-compose.yml` 생성하여 아래 코드를 입력해주세요.

```docker
version: '3'
services:
  mysql:
    container_name: mysql_db
    image: mysql:latest
    healthcheck:
      test: [ "CMD", "mysqladmin", "ping", "-h", "localhost" ]
      interval: 10s
      timeout: 5s
      retries: 5
    environment:
      MYSQL_ROOT_HOST: '%'
      MYSQL_ROOT_PASSWORD: 1234
      MYSQL_DATABASE: hansumDB
    ports:
      - "3306:3306"
    volumes:
      - ./my-custom.cnf:/etc/mysql/conf.d/my-custom.cnf
    networks:
      - test_network

  spring-boot-app:
    container_name: hansumproject0522
    image : ukjang/hansumproject-spring-boot-app:latest
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/hansumDB?useSSL=false&allowPublicKeyRetrieval=true
      SPRING_DATASOURCE_USERNAME: "root"
      SPRING_DATASOURCE_PASSWORD: "1234"
    ports:
      - "8080:8080"
    depends_on:
      mysql:
        condition: service_healthy
    networks:
      - test_network
networks:
  test_network:
```

1. **`docker-compose.yml` 파일을 만드셨다면 터미널로 `docker-compose.yml` 파일이 있는 디렉토리(폴더)의 위치로 이동시켜주세요.**

2. **아래 명령어를 입력해주세요.**
    
    ```docker
    docker compose up -d
    ```
    

1. **docker dash board를 통해 docker container가 잘 작동되는지 확인해주세요.**
    
![title](https://private-user-images.githubusercontent.com/100981076/339589892-326aa9e0-b011-4589-acc0-eb297630f699.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MTgzMjU1MDQsIm5iZiI6MTcxODMyNTIwNCwicGF0aCI6Ii8xMDA5ODEwNzYvMzM5NTg5ODkyLTMyNmFhOWUwLWIwMTEtNDU4OS1hY2MwLWViMjk3NjMwZjY5OS5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQwNjE0JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MDYxNFQwMDMzMjRaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT00OTk2ZjYwMTcxZjI2N2ExNjk4YTk5MzgyM2IxN2E4ZTE4OTYxNWE4NzkzZDBlYjViZWMwMmJhZjcxOGZhNGZmJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCZhY3Rvcl9pZD0wJmtleV9pZD0wJnJlcG9faWQ9MCJ9.Fl4lkpV7L3_dwHRyOoB3XLtfwElvO5SHA3mPeXNhSBw)   
