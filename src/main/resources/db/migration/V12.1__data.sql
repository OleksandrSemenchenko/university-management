insert into university.users(first_name, last_name, email, password, enabled) 
    values('admin', 'admin', 'a', '{noop}a', 'true');
insert into university.users(first_name, last_name, email, password, enabled)
    values('staff', 'staff', 'staff', '{noop}a', 'true');
insert into university.users(first_name, last_name, email, password, enabled) 
    values('teacher', 'teacher', 'teacher', '{noop}a', 'true');
insert into university.users(first_name, last_name, email, password, enabled) 
    values('student', 'student', 'student', '{noop}a', 'true');
insert into university.users(first_name, last_name, email, password) 
    values('person', 'person', 'person', '{noop}a');
    
insert into university.authorities(authority, user_id) values('ROLE_ADMIN', 1);
insert into university.authorities(authority, user_id) values('ROLE_STAFF', 2);
insert into university.authorities(authority, user_id) values('ROLE_TEACHER', 3);
insert into university.authorities(authority, user_id) values('ROLE_STUDENT', 4);

insert into university.users(first_name, last_name, email) 
    values('Linus', 'Torvalds', 'email@domain');
insert into university.users(first_name, last_name) values('Dennis', 'Ritchie');
insert into university.users(first_name, last_name) values('Isaac', 'Barrow');

insert into university.teachers(user_id) values(2);
insert into university.teachers(user_id) values(3);
insert into university.teachers(user_id) values(4);

insert into university.courses(name) values('Programming');
insert into university.courses(name) values('Mathematics');
insert into university.courses(name, description) values('Electrodynamics', 
                                                         'It is a branch of theoretical physics');

insert into university.teacher_course(teacher_id, course_id) values(1, 1);
insert into university.teacher_course(teacher_id, course_id) values(2, 1);
insert into university.teacher_course(teacher_id, course_id) values(3, 2);
insert into university.teacher_course(teacher_id, course_id) values(1, 3);

insert into university.groups(name) values('lt-58');
insert into university.groups(name) values('ua-77');

insert into university.users(first_name, last_name) values('Adam', 'Fox');
insert into university.users(first_name, last_name) values('Stiven', 'Parker');
insert into university.users(first_name, last_name) values('Oliver', 'Stoun');
insert into university.users(first_name, last_name) values('Luise', 'Key');
insert into university.users(first_name, last_name) values('Jon', 'Snow');
insert into university.users(first_name, last_name) values('Margaret', 'Thatcher');
insert into university.users(first_name, last_name) values('Sansa', 'Stark');
insert into university.users(first_name, last_name) values('Rhaenyra', 'Targaryen');

insert into university.students(user_id, group_id) values(6, 1);
insert into university.students(user_id, group_id) values(7, 1);
insert into university.students(user_id, group_id) values(8, 1);
insert into university.students(user_id, group_id) values(9, 1);
insert into university.students(user_id, group_id) values(10, 2);
insert into university.students(user_id, group_id) values(11, 2);
insert into university.students(user_id, group_id) values(12, 2);
insert into university.students(user_id, group_id) values(13, 2);

insert into university.timetables(start_time, break_duration, week_day, group_id, course_id)
    values('08:00', 15,'MONDAY', 1, 1);
insert into university.timetables(start_time, break_duration, week_day, group_id, course_id)
    values('10:00', 45, 'TUESDAY', 1, 2);
insert into university.timetables(start_time, break_duration, week_day, group_id, course_id)
    values('12:30', 15, 'FRIDAY', 1, 3);
insert into university.timetables(start_time, break_duration, week_day, group_id, course_id)
    values('14:30', 15, 'TUESDAY', 2, 2);  
insert into university.timetables(start_time, break_duration, week_day, group_id, course_id)
    values('16:30', 15, 'WEDNESDAY', 2, 3); 
    

    