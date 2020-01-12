insert into users (login, password)
values ('Ania', '123'),
    ('Maks', '666'),
    ('Loha', 'lubov'),
    ('pacan', 'fontan');

insert into groups( name, description)
values ('name1', 'description1'),
('name2', 'description2'),
('name3', 'description3'),
('name4', 'description4');


insert into users_groups(user_id, group_id)
values (1, 15),
(1, 18),
(2, 15),
(3, 25),
(4, 19);