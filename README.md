
# 미래 DX 생활가전 리빙솔루션+ PROJECT


> **✅ 프로젝트 참여 기간**: 2024.07.21 ~ 2024.12.27
> 
> **✅ 프로젝트 개발 참가자**: 정유빈, 김서영
>
> **🎉 대상 수상🎉**

<br>

<p align = "center"><img src="https://github.com/user-attachments/assets/ecaf438e-e51d-475c-a286-c7785fcabe93" width="60%"/></p>

<br>
<br>

## 📌 어플리케이션 UI 

<p align = "center"><img src="https://github.com/user-attachments/assets/2fac3faf-d43e-4c9e-ba1f-aada707b5273" width="80%"/></p>


<br>

전기공학과 3명, 컴퓨터공학과 2명이서 진행한 **[배터리 모사 시스템 구현]** 프로젝트였습니다.

<br>
<br>

## 📌 프로젝트 요약

- I2C 통신 방법으로 청소기 모사 시스템으로 데이터 값이 JSON 형태로 넘어옴
- 청소기로부터 받은 값을 WIFI로 받아 어플리케이션에 시각화함

<br>
<br>

### 💡 주제의 필요성

- **경제적 이점**
  - 장기 비용 절감
      - 배터리 교체 주기가 짧아짐에 따라 사용자의 경제적 부담감 증가
- **환경 보호**
  - 전자 폐기물 감소
      - 배터리 수명을 연장하면 배터리 폐기물의 감소로 이어져 환경에 미치는 영향을 최소화할 수 있음, 자원 고갈 문제 완화, 환경보호에 큰 기여
  - 자원 사용 효율화
      - 배터리에 필요한 리튬, 코발트 등의 원료는 한정자원이므로 배터리 수명 연장을 도모하여 자원의 사용 효율 높임
- **사용자 경험 개선**
  - 사용 편의성
      - 배터리 수명이 길어지면 사용자는 장비를 더 오래 사용할 수 있으며, 충전 빈도를 줄여 사용자 편의성을 크게 향상시킴
  - 제품 신뢰성 향상
      - 길어진 배터리 수명 제품은 신뢰성이 높아져 사용자 만족도가 증가함

<br>
<br>


### 💡 기대 효과

- **비용 절감**
  - 개인 사용자
      - 배터리 교체 주기가 길어짐에 따라 스마트폰, 노트북 등의 기기를 더 오래 사용할 수 있음
- **환경적 이점**
  - 폐기물 감소
      - 사용 수명이 길어진 배터리는 전자 폐기물의 양을 줄이는 데 기여함
  - 탄소 발자국 감소
      - 배터리 생산과 폐기에 따른 탄소 배출이 감소하여 전반적인 탄소 발자국을 줄이는데 도움이 됨
- **사회적 이익**
  - 지속 가능한 기술
      - 배터리 수명 연장 기술은 지속 가능한 기술 개발을 촉진하여 긍정적인 영향
- **산업 및 경제 발전**
  - 기술 경쟁력 강화, 신시장 창출
- **사용자 만족도 증가**
  - 제품 신뢰도, 사용 경험 개선


<br>
<br>

### 💡 목적, 개발 동기 및 필요성

- **경제적 이점**
    - 장기 비용 절감
        - 배터리 교체 주기가 짧아짐에 따라 사용자의 경제적 부담감 증가
- **환경 보호**
    - 전자 폐기물 감소
        - 배터리 수명을 연장하면 배터리 폐기물의 감소로 이어져 환경에 미치는 영향을 최소화할 수 있음, 자원 고갈 문제 완화, 환경보호에 큰 기여
    - 자원 사용 효율화
        - 배터리에 필요한 리튬, 코발트 등의 원료는 한정자원이므로 배터리 수명 연장을 도모하여 자원의 사용 효율 높임
        
        ![image](https://github.com/user-attachments/assets/7228fa12-854e-4f2e-ab00-372aee06bf9c)

        [사진출처: 대한환경공학회지, '전기자동차에서 발생하는 폐배터리의 재활용 및 재사용: 리뷰']
        
        - 수산화 리튬의 공급과 수요 그래프
            - 해를 거듭할수록 공급과 수요는 크게 증가하는데, 수요량을 따라가지 못하는 공급량 → 그러므로 배터리 수명을 연장시켜 한정자원의 사용을 최소화해야 함
            - https://www.jksee.or.kr/journal/view.php?viewtype=pubreader&number=4468#!po=16.6667
            - 출처: ‘전기자동차에서 발생하는 폐배터리의 재활용 및 재사용: 리뷰’, 정지우 외 4인, 전남대학교 환경에너지공학과 광주캠퍼스
- **사용자 경험 개선**
    - 사용 편의성
        - 배터리 수명이 길어지면 사용자는 장비를 더 오래 사용할 수 있으며, 충전 빈도를 줄여 사용자 편의성을 크게 향상시킴
    - 제품 신뢰성 향상
        - 길어진 배터리 수명 제품은 신뢰성이 높아져 사용자 만족도가 증가함


<br>
<br>

### 💡 해결방안 및 수행 과정

- **요약**
   - 데이터베이스에 시간별 온도, 전류, 전압, 충전량을 저장
   - 어플리케이션으로 시각화
 

<br>

- **Fragment 화면**
   - Fragment 1 (Graph): 시간당 전류, 전압, 충전량 그래프
   - ![image](https://github.com/user-attachments/assets/83a1a195-72c1-43f3-bd05-172047ea83e3)

   - Fragment 2 (Data): 현재 배터리 + 충전기 상태 -> 온도, 전류, 전압, 충전량
   - Fragment 3 (Goal): 현재 충전 방식

<br>


