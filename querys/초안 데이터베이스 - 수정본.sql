-- 1. 데이터베이스 생성
CREATE DATABASE break_database;

-- 2. 데이터베이스 사용
USE break_database;

-- 3. 사용자(User) 테이블
CREATE TABLE User (
    userId INT AUTO_INCREMENT PRIMARY KEY,  -- 기본 키, 자동 증가
    name VARCHAR(20) NOT NULL,  -- 이름, 최대 20자
    email VARCHAR(30) NOT NULL UNIQUE,  -- 이메일, 최대 30자, 유일값
    age INT NOT NULL,  -- 나이
    gender ENUM('Male', 'Female', 'Other')  -- 성별, ENUM으로 설정 (남성, 여성, 기타)
);

-- 4. 비밀번호 관리자(PasswordManager) 테이블
CREATE TABLE PasswordManager (
    userId INT,  -- 외래 키로 User 테이블의 userId 참조
    password VARCHAR(255) NOT NULL,  -- 비밀번호, 해시된 형태로 저장
    PRIMARY KEY (userId),  -- userId를 기본 키로 설정
    CONSTRAINT fk_user_password FOREIGN KEY (userId) REFERENCES User(userId) ON DELETE CASCADE
);

-- 5. 운동(Exercise) 테이블 (카테고리와 타겟 부위를 ENUM으로 처리)
CREATE TABLE Exercise (
    exerciseId INT AUTO_INCREMENT PRIMARY KEY,  -- 운동 ID
    name VARCHAR(50) NOT NULL,  -- 운동 이름
    description TEXT,  -- 운동 설명
    category ENUM('Bodyweight', 'Cardio', 'Stretching', 'Dumbbell', 'Barbell') NOT NULL,  -- 카테고리
    targetArea ENUM('Chest', 'Back', 'Shoulders', 'Triceps', 'Biceps') NOT NULL  -- 타겟 부위
);

-- 6. 표준 운동(StandardExercise) 테이블 (Exercise와 상속 관계)
CREATE TABLE StandardExercise (
    standardExerciseId INT AUTO_INCREMENT PRIMARY KEY,  -- 표준 운동 ID
    exerciseId INT,  -- Exercise 테이블과 연결
    difficultyLevel VARCHAR(20),  -- 난이도
    CONSTRAINT fk_exercise_standard FOREIGN KEY (exerciseId) REFERENCES Exercise(exerciseId) ON DELETE CASCADE
);

-- 7. 커스텀 운동(CustomExercise) 테이블 (Exercise와 상속 관계)
CREATE TABLE CustomExercise (
    customExerciseId INT AUTO_INCREMENT PRIMARY KEY,  -- 커스텀 운동 ID
    exerciseId INT,  -- Exercise 테이블과 연결
    creatorUserId INT,  -- 외래 키로 User 테이블의 userId 참조 (사용자가 만든 운동)
    CONSTRAINT fk_exercise_custom FOREIGN KEY (exerciseId) REFERENCES Exercise(exerciseId) ON DELETE CASCADE,
    CONSTRAINT fk_user_creator FOREIGN KEY (creatorUserId) REFERENCES User(userId) ON DELETE CASCADE
);

-- 8. 루틴(Routine) 테이블
CREATE TABLE Routine (
    routineId INT AUTO_INCREMENT PRIMARY KEY,  -- 루틴 ID, 기본 키
    userId INT,  -- 외래 키로 User 테이블의 userId 참조
    name VARCHAR(30) NOT NULL,  -- 루틴 이름, 최대 30자
    creationDate DATE NOT NULL,  -- 생성 날짜
    CONSTRAINT fk_user_routine FOREIGN KEY (userId) REFERENCES User(userId) ON DELETE CASCADE
);

-- 9. 루틴-운동 관리자(RoutineExerciseManager) 테이블 (다대다 관계를 관리)
CREATE TABLE RoutineExerciseManager (
    routineExerciseManagerId INT AUTO_INCREMENT PRIMARY KEY,  -- 고유 ID
    routineId INT,  -- 외래 키로 Routine 테이블의 routineId 참조
    exerciseId INT,  -- Exercise 테이블의 exerciseId와 연결
    CONSTRAINT fk_routine FOREIGN KEY (routineId) REFERENCES Routine(routineId) ON DELETE CASCADE,
    CONSTRAINT fk_exercise FOREIGN KEY (exerciseId) REFERENCES Exercise(exerciseId) ON DELETE CASCADE
);

-- 10. 인바디(InBody) 테이블
CREATE TABLE InBody (
    inBodyId INT AUTO_INCREMENT PRIMARY KEY,  -- 인바디 ID, 기본 키
    userId INT,  -- 외래 키로 User 테이블의 userId 참조
    measurementDate DATE NOT NULL,  -- 측정 날짜
    weight FLOAT NOT NULL,  -- 체중
    bodyFatPercentage FLOAT NOT NULL,  -- 체지방률
    muscleMass FLOAT NOT NULL,  -- 근육량
    bmi FLOAT NOT NULL,  -- BMI
    visceralFatLevel FLOAT NOT NULL,  -- 내장 지방 수치
    basalMetabolicRate FLOAT NOT NULL,  -- 기초 대사량
    CONSTRAINT fk_user_inbody FOREIGN KEY (userId) REFERENCES User(userId) ON DELETE CASCADE
);

-- 11. 운동 로그(WorkoutLog) 테이블
CREATE TABLE WorkoutLog (
    logId INT AUTO_INCREMENT PRIMARY KEY,  -- 로그 ID, 기본 키
    exerciseId INT,  -- 외래 키로 Exercise 테이블의 exerciseId 참조
    userId INT,  -- 외래 키로 User 테이블의 userId 참조
    startTime DATETIME NOT NULL,  -- 운동 시작 시간
    endTime DATETIME NOT NULL,  -- 운동 종료 시간
    duration INT NOT NULL,  -- 운동 지속 시간 (분 단위)
    CONSTRAINT fk_exercise_workout FOREIGN KEY (exerciseId) REFERENCES Exercise(exerciseId) ON DELETE CASCADE,
    CONSTRAINT fk_user_workout FOREIGN KEY (userId) REFERENCES User(userId) ON DELETE CASCADE
);

-- 12. 알림(Notification) 테이블
CREATE TABLE Notification (
    notificationId INT AUTO_INCREMENT PRIMARY KEY,  -- 알림 ID, 기본 키
    userId INT,  -- 외래 키로 User 테이블의 userId 참조
    message TEXT NOT NULL,  -- 알림 메시지
    dateTime DATETIME NOT NULL,  -- 알림 시간
    CONSTRAINT fk_user_notification FOREIGN KEY (userId) REFERENCES User(userId) ON DELETE CASCADE
);
