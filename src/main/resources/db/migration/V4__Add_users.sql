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
    'kuan0077@mail.com',
    true,
    'Kuan',
    '$2a$08$FsyzJ2.hxuAvcfF9D32MfuVo9y2Qo/J0/8ALn8B6YaZiymb6d6eu.',
    '+375-29-285-27-75',
    '04-08-2021 15:50:30',
    null,
    'Chin',
    'kuan1701',
    1
),
(
    null,
    true,
    'billGates@mail.com',
    true,
    'Bill',
    '$2a$08$FsyzJ2.hxuAvcfF9D32MfuVo9y2Qo/J0/8ALn8B6YaZiymb6d6eu.',
    '+375-29-111-11-12',
    '04-08-2021 15:50:30',
    null,
    'Gates',
    'billGates',
    1
),
(
    null,
    true,
    'markZuckerberg@mail.com',
    true,
    'Mark',
    '$2a$08$FsyzJ2.hxuAvcfF9D32MfuVo9y2Qo/J0/8ALn8B6YaZiymb6d6eu.',
    '+375-29-111-11-13',
    '04-08-2021 15:50:30',
    null,
    'Zuckerberg',
    'markZuckerberg',
    1
);