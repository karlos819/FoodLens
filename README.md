# 🍽️ FoodLens
> AI 기반 음식 기록 & 건강 분석(Android) 앱  
> (Room DB + Calpal 스타일 분석 대시보드 + 확장 가능한 AI 구조)

---

## 📌 프로젝트 개요
FoodLens는 사용자가 촬영한 음식 이미지를 기반으로 결과를 확인하고, 식사 기록(게시글)을 저장하며,  
Calpal 스타일의 건강 분석 대시보드 UI를 제공하는 Android 네이티브 애플리케이션입니다.

- **문제 정의**: 바쁜 일상에서 식단 기록이 어렵고, 건강 상태를 직관적으로 보기 힘듦  
- **목표**: 기록 진입 장벽을 낮추고(메모/카드 UI), 시각화로 동기 부여(게이지/그래프) 제공  
- **현재 구현 범위**: 기능 흐름(Analyze → Post 저장) + 더미 기반 대시보드 완성

---

## 💡 핵심 가치 제안
- **기록 자동화(저장)**: 음식명/칼로리/메모를 게시글 형태로 저장하고 추후 조회 가능  
- **시각화 기반 분석 UI**: Calpal 스타일 카드/게이지/그래프로 건강 상태를 한 화면에 제공  
- **확장 가능한 AI 구조**: TFLite(assets) / ML Kit / Gemini API 연동 가능 구조 유지  
- **오프라인 우선**: Room 기반 로컬 DB 저장으로 네트워크 없이도 기록 유지

---

## 🧩 기술 아키텍처
아래는 FoodLens의 데이터 흐름과 책임 분리를 나타낸 구조입니다.

```mermaid
flowchart TB
  A[Presentation Layer\nActivities / Adapters / CustomView] --> B[Data Layer\nRoom DB (DAO/Entity)]
  A --> C[AI Layer (Optional)\nTFLite / ML Kit / Gemini]
  B --> D[(SQLite)]
  C --> E[External Services\nGemini API / ML Model]



3️⃣ 건강 분석 대시보드 

실제 분석 로직 대신 더미 데이터 기반 UI 완성도에 집중

체중 / 체지방률 / 키 / BMI 표시

체중 변화 추이 그래프 (Custom SparklineView)

하루 섭취 칼로리 원형 게이지

아침 / 점심 / 저녁 / 간식 식사 리스트 표시



🖥️ 화면 구성
HomeActivity	앱 메인 화면
AnalyzeActivity	분석 및 건강 대시보드 화면
PostActivity	게시글(식사 기록) 작성 화면
SettingsActivity	설정 화면
ChatActivity	(확장 기능) AI 채팅 UI



🧠 설계 특징
✔ 단계적 아키텍처 설계

UI / 데이터 / 로직 분리

Room(Entity / DAO / Database) 구조 적용

✔ 확장 가능한 AI 구조

현재는 더미 데이터 사용

ML Kit / TFLite / Gemini API 연동이 가능한 구조 유지

✔ 커스텀 UI 컴포넌트 구현

SparklineView: Canvas 기반 커스텀 그래프

Material Components 기반 원형/선형 게이지



🛠️ 사용 기술 스택
Android / Kotlin

Kotlin 기반 Activity 구성

ViewBinding 미사용(XML + findViewById 기반)

데이터 저장

Room Database

Entity / DAO / Singleton Database 패턴

Coroutine 기반 비동기 처리

UI / UX

Material Design Components

CardView 기반 레이아웃

Custom View(Canvas) 그래프 구현

RecyclerView

Coroutine (lifecycleScope)

확장 가능 AI API 구조(Gemini, ML Kit 등)



프로젝트 구조
com.example.foodlens
 ├─ HomeActivity.kt
 ├─ AnalyzeActivity.kt
 ├─ PostActivity.kt
 ├─ SettingsActivity.kt
 ├─ ChatActivity.kt
 ├─ ui/
 │   └─ SparklineView.kt
 └─ data/db/
     ├─ AppDatabase.kt
     ├─ FoodPostEntity.kt
     ├─ FoodPostDao.kt
     ├─ FoodRecordEntity.kt
     └─ FoodRecordDao.kt

res/layout
 ├─ activity_home.xml
 ├─ activity_analyze.xml
 ├─ activity_post.xml
 ├─ activity_settings.xml
 └─ ...
