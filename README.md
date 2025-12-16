🍽️ FoodLens – AI 기반 음식 분석 & 건강 관리 Android 앱

FoodLens는 사용자가 촬영한 음식 이미지를 기반으로 분석 결과를 확인하고,
식사 기록을 저장하며, 건강 분석 대시보드를 제공하는 Android 애플리케이션입니다.

본 프로젝트는 Android 앱 구조 설계, 로컬 데이터 저장(Room), 커스텀 UI 구성,
확장 가능한 AI 연동 구조를 중심으로 구현되었습니다.



📌 프로젝트 개요

프로젝트명: FoodLens

플랫폼: Android

개발 언어: Kotlin

개발 목적

음식 섭취 기록을 직관적으로 관리

분석 결과 및 건강 상태를 시각적으로 제공

실제 AI 모델 연동이 가능한 구조 설계




🎯 핵심 기능 요약

1️⃣ 음식 분석 흐름 (Analyze → Post)

사용자가 음식 이미지를 입력

분석 화면에서 결과 확인

게시글 형태로 기록 저장


2️⃣ 게시글(식사 기록) 관리

음식명 / 칼로리 / 메모 / 생성 시간 저장

Room Database 기반 로컬 저장

앱 재실행 후에도 데이터 유지


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
