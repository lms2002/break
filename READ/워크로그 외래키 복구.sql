-- 워크로그와 routine_exercise_manager 외래키 복구
ALTER TABLE WorkoutLog 
ADD CONSTRAINT fk_routine_exercise_manager FOREIGN KEY (routine_exercise_manager_id)
REFERENCES routine_exercise_manager(routine_exercise_manager_id) ON DELETE CASCADE;
