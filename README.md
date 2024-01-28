# README.md

### FLAT

---

- 시연 영상: https://www.youtube.com/watch?v=jPAP0503B1c

### 개요

---

- 부동산 매물의 다양한 서비스 제공을 목적으로 매물 위치 및 정보, 친구 추가 및 채팅 기능 등을 제공

### 기획 배경 및 목표

---

- 자취할 공간을 알아보면서 직접 느꼈던 니즈 해결
    - 관심 있는 지역의 매물 만을 찾아보고 싶다.
    - 이사 가는 지역 주변에 친구가 살고 있는지 알아보고 싶다.
    - 안전을 위해 알아보는 매물 주변에 경찰서가 있는지 알아보고 싶다.
    - 매물에 대한 문의를 실시간 온라인으로 하고 싶다.
    - 매물을 여러 개 동시에 알아보다 보니, 전달 받은 매물 정보가 섞여 기억하기 힘들다.
- 타겟 유저
    - 혼자 살 집을 알아보는 20~30대
    - 콜 포비아 (call phobia)가 있는 유저

### 차별화 전략

---

- 차별화 전략 1. 관심 지역 설정

| 니즈1 | 이사 가는 지역 주변에 친구가 살고 있는지 알아보고 싶다. |
| --- | --- |
| 니즈2 | 안전을 위해 알아보는 매물 주변에 경찰서가 있는지 알아보고 싶다. |
| 차별화 전략 | 친구가 살고 있는 위치와 경찰서 위치를 고려해서 관심 지역을 설정할 수 있다. |
- 차별화 전략 2. 관심 지역 매물

| 니즈1 | 관심 있는 지역의 매물 만을 찾아보고 싶다. |
| --- | --- |
| 차별화 전략 | 관심 지역 설정을 하고, 매물 검색 시 관심 지역에 포함된 매물 만을 필터링 하여 보여준다.  |
|  | 관심 지역 설정 범위를 조정한다. |
|  | 관심 지역에 매물에 등록되면 알림을 받는다. |

- 차별화 전략 3. 매물 문의

| 니즈1 | 매물에 대한 문의를 실시간 온라인으로 하고 싶다. |
| --- | --- |
| 니즈2 | 매물을 여러 개 동시에 알아보다 보니, 전달 받은 매물 정보가 섞여 기억하기 힘들다.  |
| 차별화 전략 | 매물 별로 문의를 위한 채팅방을 각각 개설할 수 있다. |
|  | 집주인과 전화하지 않고 온라인으로 문의를 남겨 놓을 수 있다. |
|  | 매물 별로 개설된 채팅방 대화 내역을 통해 매물에 대한 정보를 확인할 수 있다. |

### 프로젝트 기간

---

- 2023.10.17 ~ 2023.11.23

### 팀원

---

| 이름 | 구현 기능 |
| --- | --- |
| 김동훈 | 채팅, 친구, 마이페이지, 알림, 지도 기능 백엔드 및 프론트엔드 |
| 선수연 | 사용자 인증, 매물, 지도, 검색 기능 백엔드 및 프론트엔드 |
- 이후 상호 보완하여 페어 담당 기능 수정

### 기술 스택

---

- 백엔드
    - Spring, JPA, Spring Security
    - MySQL, AWS RDS 및 S3
- 프론트엔드
    - Vue.js, Quasar
- 활용 API
    - Naver Map API : [https://navermaps.github.io/maps.js.ncp/docs/tutorial-2-Getting-Started.html](https://navermaps.github.io/maps.js.ncp/docs/tutorial-2-Getting-Started.html)
    - Naver Geocode API : [https://api.ncloud-docs.com/docs/ai-naver-mapsgeocoding-geocode](https://api.ncloud-docs.com/docs/ai-naver-mapsgeocoding-geocode)
    - 경찰서 API : [https://www.data.go.kr/data/15054711/fileData.do#tab-layer-openapi](https://www.data.go.kr/data/15054711/fileData.do#tab-layer-openapi)

### 데이터베이스 설계

---
![image](https://github.com/ssafy-flat/flat/assets/77823761/33cf4ab4-1825-4895-87c8-931b0b363873)


### 기능

---
![image](https://github.com/ssafy-flat/flat/assets/77823761/bd074fed-6795-4079-9b80-d2686168d261)
