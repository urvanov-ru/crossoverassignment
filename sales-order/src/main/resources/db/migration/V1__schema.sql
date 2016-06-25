-- CREATE SCHEMA `saleorder` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;

-- USE salesorder;

CREATE TABLE customer(
    id                int            NOT NULL AUTO_INCREMENT,
    code              varchar(200)   NOT NULL UNIQUE,
    name              varchar(200)   NOT NULL,
    phone1            varchar(50)    NOT NULL,
    phone2            varchar(50)    NOT NULL,
    address           varchar(200)   NOT NULL,
    current_credit    numeric(15,2)  NOT NULL,
    credit_limit      numeric(15,2)  NOT NULL,
    version           int            NOT NULL default 0,
    PRIMARY KEY(id)
) ;

CREATE TABLE product(
    id                int            NOT NULL AUTO_INCREMENT,
    code              varchar(200)   NOT NULL UNIQUE,
    description       varchar(200)   NOT NULL,
    price             numeric(15, 2) NOT NULL,
    quantity          int            NOT NULL,
    version           int            NOT NULL default 0,
    PRIMARY KEY (id)
);

CREATE TABLE sale_order(
    id                int            NOT NULL AUTO_INCREMENT,
    number            varchar(200)   UNIQUE,
    customer_id       int            NOT NULL,
    version           int            NOT NULL default 0 ,
    PRIMARY KEY(id)
);

ALTER TABLE sale_order ADD CONSTRAINT fk_sale_order_customer_id
FOREIGN KEY (customer_id) REFERENCES customer(id)
ON UPDATE NO ACTION ON DELETE NO ACTION;


CREATE TABLE order_line(
    id                int            NOT NULL AUTO_INCREMENT,
    sale_order_id     int            NOT NULL,
    product_id        int            NOT NULL,
    quantity          int            NOT NULL,
    price             numeric(15,2)  NOT NULL,
    version           int            NOT NULL default 0,
    PRIMARY KEY(id)
);

ALTER TABLE order_line ADD CONSTRAINT fk_order_line_sale_order_id
FOREIGN KEY  (sale_order_id) REFERENCES sale_order(id)
ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE order_line ADD CONSTRAINT fk_order_line_sales_product_id
FOREIGN KEY (product_id) REFERENCES product(id)
ON UPDATE NO ACTION ON DELETE NO ACTION;