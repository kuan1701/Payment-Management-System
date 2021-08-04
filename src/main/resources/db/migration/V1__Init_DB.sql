create table account
(
    account_id bigint not null auto_increment,
    balance decimal(19,2) not null, currency varchar(255),
    is_blocked bit, is_deleted bit,
    number varchar(255), user_user_id bigint,
    primary key (account_id)
) engine = InnoDB;

create table bank_card
(
    card_id bigint not null auto_increment,
    cvv varchar(3),
    is_active bit,
    month varchar(2),
    number varchar(19),
    validity varchar(255),
    year varchar(2),
    account_account_id bigint,
    primary key (card_id)
) engine = InnoDB;

create table letter
(
    letter_id bigint not null auto_increment,
    date varchar(255),
    description varchar(2048),
    is_processed bit,
    type_question integer,
    user_user_id bigint,
    primary key (letter_id)
) engine = InnoDB;

create table log_entry
(
    log_entry_id bigint not null auto_increment,
    date varchar(255),
    description varchar(2048),
    user_user_id bigint,
    primary key (log_entry_id)
) engine = InnoDB;

create table payment
(
    payment_id bigint not null auto_increment,
    appointment varchar(255),
    date varchar(255),
    exchange_rate decimal(19,2),
    is_outgoing bit,
    new_balance decimal(19,2),
    recipient_amount decimal(19,2),
    recipient_currency varchar(255),
    recipient_number varchar(20),
    sender_amount decimal(19,2),
    sender_currency varchar(255),
    sender_number varchar(20),
    status bit,
    user_id bigint,
    account_account_id bigint,
    primary key (payment_id)
) engine = InnoDB;

create table role
(
    id bigint not null,
    name varchar(255),
    primary key (id)
) engine = InnoDB;

create table user
(
    user_id bigint not null auto_increment,
    activation_code varchar(255),
    active bit, email varchar(255),
    email_verified bit,
    name varchar(255),
    password varchar(255),
    phone varchar(255),
    registration_date varchar(255),
    reset_password_token varchar(255),
    surname varchar(255),
    username varchar(255),
    role_id bigint,
    primary key (user_id)
) engine = InnoDB;

alter table account
    add constraint FKjiqrvmmf18phbftjiwc1nsobl foreign key (user_user_id) references user (user_id);

alter table bank_card
    add constraint FK6j7048q6auijf41vans0lj5qw foreign key (account_account_id) references account (account_id);

alter table letter
    add constraint FK3dvaokahvyovfcfg1biemguwd foreign key (user_user_id) references user (user_id);

alter table log_entry
    add constraint FKtf9sags1uipf70tqyq3knvh3a foreign key (user_user_id) references user (user_id);

alter table payment
    add constraint FK9ntaamy420q595njxwhin8o70 foreign key (account_account_id) references account (account_id);

alter table user
    add constraint FKn82ha3ccdebhokx3a8fgdqeyy foreign key (role_id) references role (id);