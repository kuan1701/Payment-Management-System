insert into `user`
(
    activation_code,
    active,
    email,
    email_verified,
    name,
    password,
    phone,
    registration_date,
    reset_password_token,
    surname,
    username,
    role_id
)
values
(
    null,
    true,
    'paymentmanagementsystem@gmail.com',
    true,
    'PMS',
    '$2a$08$FsyzJ2.hxuAvcfF9D32MfuVo9y2Qo/J0/8ALn8B6YaZiymb6d6eu.',
    '+375-29-111-11-11',
    '04-08-2021 15:50:30',
    null,
    'Admin',
    'admin',
    2
);