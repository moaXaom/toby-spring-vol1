DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id        varchar(10) primary key,
    name      varchar(20) not null,
    password  varchar(10) not null,
    level     int         not null,
    login     int         not null,
    recommend int         not null,
    email     varchar(50) not null
);
