В данном сервисе разработал:
  1. RecommendationServiceImpl для возможности давать/удалять/обновлять (и др.) рекомендацию:
       https://github.com/Amirhan2/user_service/blob/phoenix-master-stream6/src/main/java/school/faang/user_service/service/recommendation/RecommendationServiceImpl.java
  2. RecommendationController без REST API (не предусмотрено):
       https://github.com/Amirhan2/user_service/blob/phoenix-master-stream6/src/main/java/school/faang/user_service/controller/recommendation/RecommendationController.java
  3. RecommendationMapper(MapStruct):
       https://github.com/Amirhan2/user_service/blob/phoenix-master-stream6/src/main/java/school/faang/user_service/mapper/RecommendationMapper.java
  4. Разработал отправку ивента запроса рекомендации с помощью класса EventPublisher предназначенный для всех ивентов:
       https://github.com/Amirhan2/user_service/blob/phoenix-master-stream6/src/main/java/school/faang/user_service/service/recommendation/RecommendationRequestServiceImpl.java
  5. UnitTest(JUnit, Mockito) юнит тест на сервис:
       https://github.com/Amirhan2/user_service/blob/phoenix-master-stream6/src/test/java/school/faang/user_service/service/recommendation/RecommendationServiceTest.java
     
     
