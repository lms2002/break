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
    password VARCHAR(255) NOT NULL,  -- 비밀번호, 해시된 형태로 저장 64자리까지 입력 가능
    PRIMARY KEY (userId),  -- userId를 기본 키로 설정
   -- -- CONSTRAINT : 외래키 제약조건 이름, ON DELETE CASCADE :  User 테이블의 userId 삭제될 경우, PasswordManager도 자동으로 삭제
    CONSTRAINT fk_user_password FOREIGN KEY (userId) REFERENCES User(userId) ON DELETE CASCADE
);

-- 5. 루틴(Routine) 테이블
CREATE TABLE Routine (
    routineId INT AUTO_INCREMENT PRIMARY KEY,  -- 루틴 ID, 기본 키
    userId INT,  -- 외래 키로 User 테이블의 userId 참조
    name VARCHAR(30) NOT NULL,  -- 루틴 이름, 최대 30자
    creationDate DATE NOT NULL,  -- 생성 날짜
    CONSTRAINT fk_user_routine FOREIGN KEY (userId) REFERENCES User(userId) ON DELETE CASCADE
);

-- 6. 인바디(InBody) 테이블 (먼저 생성해야 함)
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

-- 7. 운동 로그(WorkoutLog) 테이블 (InBody 테이블이 먼저 생성된 후 생성)
CREATE TABLE WorkoutLog (
    logId INT AUTO_INCREMENT PRIMARY KEY,  -- 로그 ID, 기본 키
    exerciseType ENUM('StandardExercise', 'CustomExercise') NOT NULL,  -- 운동 유형 (표준, 커스텀)
    userId INT,  -- 외래 키로 User 테이블의 userId 참조
    inBodyId INT,  -- 외래 키로 InBody 테이블의 inBodyId 참조 (nullable)
    date DATE NOT NULL,  -- 운동 날짜
    duration INT NOT NULL,  -- 운동 시간 (분 단위)
    calories INT NOT NULL,  -- 소모 칼로리
    CONSTRAINT fk_user_workout FOREIGN KEY (userId) REFERENCES User(userId) ON DELETE CASCADE,
    CONSTRAINT fk_inbody_workout FOREIGN KEY (inBodyId) REFERENCES InBody(inBodyId) ON DELETE SET NULL
);

-- 8. 알림(Notification) 테이블
CREATE TABLE Notification (
    notificationId INT AUTO_INCREMENT PRIMARY KEY,  -- 알림 ID, 기본 키
    userId INT,  -- 외래 키로 User 테이블의 userId 참조
    message TEXT NOT NULL,  -- 알림 메시지
    /*
    'Routine': 일상적인 알림, 정기적인 알림 등
	'Workout': 운동과 관련된 알림입니다. 예를 들어 운동 일정에 대한 알림
	'General': 일반적인 알림을 의미합니다. 사용자의 활동이나 앱 업데이트 등 다양한 종류의 알림
    */
    type ENUM('Routine', 'Workout', 'General') NOT NULL,  -- 알림 유형
    creationDate DATE NOT NULL,  -- 생성 날짜
    status BOOLEAN NOT NULL DEFAULT FALSE,  -- 읽음 상태 (false: 읽지 않음, true: 읽음)
    CONSTRAINT fk_user_notification FOREIGN KEY (userId) REFERENCES User(userId) ON DELETE CASCADE
);
