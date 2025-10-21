create table users (
  id varchar(24) primary key not null,
  email varchar(255) not null,
  source varchar(32) not null,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

create table cards (
  id varchar(24) primary key not null,
  body text,
  body_tsv tsvector generated always as (to_tsvector('english', body)) stored,
  creator_id varchar(24) not null references users(id),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

create table authors (
  id serial primary key,
  card_id varchar(24) not null references cards(id),
  user_id varchar(24) not null references users(id)
);