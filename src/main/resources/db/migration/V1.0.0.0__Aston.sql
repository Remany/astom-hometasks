CREATE EXTENSION "uuid-ossp";

CREATE TABLE Consumer(
    id         UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    name       varchar   NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL
);

CREATE TABLE Product(
    id          UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    name        varchar   NOT NULL,
    description varchar   NOT NULL,
    created_at  timestamp NOT NULL,
    updated_at  timestamp NOT NULL
);

CREATE TABLE Price(
    id         UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    product_id UUID REFERENCES Product (id) ON DELETE CASCADE,
    value      decimal   NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL
);

CREATE TABLE Orders(
    consumer_id UUID REFERENCES Consumer (id) ON DELETE CASCADE,
    product_id  UUID REFERENCES Product (id) ON DELETE CASCADE
);