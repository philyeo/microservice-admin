CREATE TABLE currency
(
    id         char(36)     NOT NULL,
    version    INT          NULL,
    created_at datetime     NULL,
    updated_at datetime     NULL,
    `key`      VARCHAR(255) NULL,
    label      VARCHAR(255) NULL,
    CONSTRAINT pk_currency PRIMARY KEY (id)
);

CREATE TABLE locations
(
    id               char(36)     NOT NULL,
    version          INT          NULL,
    created_at       datetime     NULL,
    updated_at       datetime     NULL,
    `key`            VARCHAR(255) NULL,
    label            VARCHAR(255) NULL,
    country_code     VARCHAR(255) NULL,
    is_team_location BIT(1)       NULL,
    CONSTRAINT pk_locations PRIMARY KEY (id)
);