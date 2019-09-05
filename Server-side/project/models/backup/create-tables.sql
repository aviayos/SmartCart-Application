SET search_path = cart_scheme, pg_catalog;

-- Application user table
DROP TABLE IF EXISTS app_users CASCADE;
create table app_users (

	userid   UUID  not null PRIMARY KEY,
	first_name   varchar (20) not null,
	last_name   varchar (20) not null,
     email       varchar (30) not null,
     password    varchar (15) not null);

ALTER TABLE app_users OWNER TO smartcartuser;

-- Application Orders table
DROP TABLE IF EXISTS app_orders CASCADE;
create table app_orders (

	orderid   UUID  not null PRIMARY KEY,
	userid   UUID  not null,
	supermarketid  UUID  not null,
     price    real not null,
     order_list  text[] not null,
     location real[] not null);

ALTER TABLE app_orders OWNER TO smartcartuser;

-- Web users table
DROP TABLE IF EXISTS web_users CASCADE;
create table web_users (

	business_id   UUID  not null PRIMARY KEY,
	first_name   varchar (20) not null,
	last_name   varchar (20) not null,
     email       varchar (30) not null UNIQUE,
     password    varchar (15) not null,
     business_name varchar (20) not null,
     phone varchar (15) not null,
     location varchar (100) not null);

ALTER TABLE web_users OWNER TO smartcartuser;

-- ENUM type for product category.
CREATE TYPE cart_scheme.category_enum AS ENUM (
     'FRUITS AND VEGS',
     'DAIRY',
     'MEAT',
     'OTHER'
);

ALTER TYPE cart_scheme.category_enum OWNER TO smartcartuser;
-- Products table
DROP TABLE IF EXISTS products CASCADE;
create table products (

	productid   UUID  not null PRIMARY KEY,
     createdby   UUID  not null,
	product_name   varchar (20) not null,
     price       real not null,
     category cart_scheme.category_enum,
     createdon    timestamp not null);

ALTER TABLE products OWNER TO smartcartuser;

/*=====================================================================
====================== application users ==============================
=====================================================================*/

insert into app_users (userid, first_name, last_name, email, password) 
values ('f61f03a3-c548-45ad-a1df-08b9bd1b5453',
        'client-Yoram',
        'Biberman', 
        'smartcart@gmail.com',
        '123456');

insert into app_users (userid, first_name, last_name, email, password) 
values ('ca865ffd-50bd-49e2-9f15-a0b928a4cee5',
        'client-Yosi',
        'Cohen', 
        'yosi_mail@gmail.com',
        '123456');

insert into app_users (userid, first_name, last_name, email, password) 
values ('722f0964-52db-4d60-8062-0db73876c87a',
        'client-Dana',
        'Levi', 
        'dana_mail@gmail.com',
        '123456');

/*=====================================================================
=========================== web users =================================
=====================================================================*/


insert into web_users (business_id,first_name, last_name, email, password,business_name,phone,location) 
values ('0f731813-dba3-4bf4-bbda-220aaf493666',
        'guy',
        'moshe', 
        'guymosh5@gmail.com',
        '123456',
        'super-guy',
        '0547777001',
        'Aluf Simhoni 12, jerusalem');

insert into web_users (business_id,first_name, last_name, email, password,business_name,phone,location) 
values ('cd914d6a-0c4f-43c5-9efa-421875a5f1a4',
        'naor',
        'Levi', 
        'naor846@gmail.com',
        '123456',
        'super-naor',
        '0527898476',
        'Yefe Rom 86, jerusalem');

insert into web_users (business_id,first_name, last_name, email, password,business_name,phone,location) 
values ('8250292d-7bd9-4d3d-869c-0c54ebcf87c3',
        'or',
        'ella', 
        'or7ella@gmail.com',
        '123456',
        'super-or',
        '0548376458',
        'hillel 12, jerusalem');


/*=====================================================================
====================== products of super-guy ==========================
=====================================================================*/

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('b692cd3a-cc3e-46cf-ac15-191d4789e01b',
        '0f731813-dba3-4bf4-bbda-220aaf493666',
        'Apple', 
        '10',
        'FRUITS AND VEGS',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('e52f6741-97fc-4a66-ac83-23082fc00ebd',
        '0f731813-dba3-4bf4-bbda-220aaf493666',
        'Banana', 
        '12',
        'FRUITS AND VEGS',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('036aaee7-7aa1-4819-964d-4b4f99136cad',
        '0f731813-dba3-4bf4-bbda-220aaf493666',
        'Cherry', 
        '15',
        'FRUITS AND VEGS',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('7f29e8eb-1c06-48d2-8025-df150161b80f',
        '0f731813-dba3-4bf4-bbda-220aaf493666',
        'Pineapple', 
        '18',
        'FRUITS AND VEGS',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('9b86807d-beab-46b3-b518-83de2ef70bfe',
        '0f731813-dba3-4bf4-bbda-220aaf493666',
        'Milk', 
        '20',
        'DAIRY',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('c74717a1-696e-4c68-989d-da6026b8ad62',
        '0f731813-dba3-4bf4-bbda-220aaf493666',
        'Yogurt', 
        '24',
        'DAIRY',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('bfe3ac48-dab2-4540-bab0-0ac144a2fece',
        '0f731813-dba3-4bf4-bbda-220aaf493666',
        'Beef', 
        '54',
        'MEAT',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('7ff24651-1819-4967-8257-0621a129451a',
        '0f731813-dba3-4bf4-bbda-220aaf493666',
        'Chicken', 
        '65',
        'MEAT',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('e7afbee5-961b-420f-aefe-4914d39cc75a',
        '0f731813-dba3-4bf4-bbda-220aaf493666',
        'Pasta', 
        '33',
        'OTHER',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('c31b3f54-2280-4ecb-8853-149b1b9f2122',
        '0f731813-dba3-4bf4-bbda-220aaf493666',
        'Tuna', 
        '24',
        'MEAT',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('2380df74-7974-4e8d-bbec-eec619467c82',
        '0f731813-dba3-4bf4-bbda-220aaf493666',
        'Persil', 
        '15',
        'OTHER',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('9bce571b-91a6-4a61-aa2b-47ce77819893',
        '0f731813-dba3-4bf4-bbda-220aaf493666',
        'Salmon', 
        '97',
        'MEAT',
        'now()');

/*=====================================================================
====================== products of super-naor ==========================
=====================================================================*/

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('ef9e573c-d703-4605-a0b6-9aa9ff985939',
        'cd914d6a-0c4f-43c5-9efa-421875a5f1a4',
        'Apple', 
        '11',
        'FRUITS AND VEGS',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('3fb95e50-18d2-44be-8822-c3ff4b086e5d',
        'cd914d6a-0c4f-43c5-9efa-421875a5f1a4',
        'Banana', 
        '32',
        'FRUITS AND VEGS',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('f0c36853-4ec7-4e25-a274-05aa0bc03f90',
        'cd914d6a-0c4f-43c5-9efa-421875a5f1a4',
        'Cherry', 
        '35',
        'FRUITS AND VEGS',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('71fdb570-e7cf-42ce-8509-417b0f6abd33',
        'cd914d6a-0c4f-43c5-9efa-421875a5f1a4',
        'Pineapple', 
        '8',
        'FRUITS AND VEGS',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('7fd6f001-2026-467b-88c3-41c13b6280b6',
        'cd914d6a-0c4f-43c5-9efa-421875a5f1a4',
        'Milk', 
        '10',
        'DAIRY',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('1446bb27-9165-4abb-aefa-b5b332d0923c',
        'cd914d6a-0c4f-43c5-9efa-421875a5f1a4',
        'Yogurt', 
        '14',
        'DAIRY',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('c73365a2-1bcb-45ab-8de0-ad4ce5dbc468',
        'cd914d6a-0c4f-43c5-9efa-421875a5f1a4',
        'Beef', 
        '44',
        'MEAT',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('603e6ccd-ded1-43aa-a3b0-bceb82986865',
        'cd914d6a-0c4f-43c5-9efa-421875a5f1a4',
        'Chicken', 
        '35',
        'MEAT',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('c1830404-949c-4c33-964a-88e5d50d44ee',
        'cd914d6a-0c4f-43c5-9efa-421875a5f1a4',
        'Pasta', 
        '23',
        'OTHER',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('204fa13d-4b22-4665-b154-dcf7b0f9c39f',
        'cd914d6a-0c4f-43c5-9efa-421875a5f1a4',
        'Tuna', 
        '17',
        'OTHER',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('abcd6fc2-47a3-47ad-89c9-f935c75088ad',
        'cd914d6a-0c4f-43c5-9efa-421875a5f1a4',
        'Persil', 
        '15',
        'OTHER',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('8712c675-c9ff-4b5d-81dd-0cf573be58f6',
        'cd914d6a-0c4f-43c5-9efa-421875a5f1a4',
        'Salmon', 
        '97',
        'MEAT',
        'now()');

/*=====================================================================
====================== products of super-or ==========================
=====================================================================*/

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('5b567c9e-3102-40da-8644-dbcdb34636c1',
        '8250292d-7bd9-4d3d-869c-0c54ebcf87c3',
        'Apple', 
        '15',
        'FRUITS AND VEGS',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('8bc3a688-7215-4fb1-a856-ae3317f82dc1',
        '8250292d-7bd9-4d3d-869c-0c54ebcf87c3',
        'Banana', 
        '12',
        'FRUITS AND VEGS',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('cf6b29d6-1902-4d4f-9541-de82c0ebaaa6',
        '8250292d-7bd9-4d3d-869c-0c54ebcf87c3',
        'Cherry', 
        '25',
        'FRUITS AND VEGS',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('15a1848a-fbfb-4dba-827b-ebdec07b98a9',
        '8250292d-7bd9-4d3d-869c-0c54ebcf87c3',
        'Pineapple', 
        '38',
        'FRUITS AND VEGS',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('cf78f8a1-f13e-460e-bf02-af31dd2e235a',
        '8250292d-7bd9-4d3d-869c-0c54ebcf87c3',
        'Milk', 
        '30',
        'DAIRY',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('99f68cff-23c7-457f-b7b3-040a34d91eed',
        '8250292d-7bd9-4d3d-869c-0c54ebcf87c3',
        'Yogurt', 
        '44',
        'DAIRY',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('96f96212-c8cf-4e2d-bd33-7f158f4be842',
        '8250292d-7bd9-4d3d-869c-0c54ebcf87c3',
        'Beef', 
        '54',
        'MEAT',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('2ff74802-825c-4a56-b300-ecf70a04f259',
        '8250292d-7bd9-4d3d-869c-0c54ebcf87c3',
        'Chicken', 
        '25',
        'MEAT',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('a2bce055-02d0-4192-8a23-8d9cf5dfa251',
        '8250292d-7bd9-4d3d-869c-0c54ebcf87c3',
        'Pasta', 
        '13',
        'OTHER',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('4d379ce1-82e9-4146-830d-8ef42ae82fc5',
        '8250292d-7bd9-4d3d-869c-0c54ebcf87c3',
        'Tuna', 
        '27',
        'OTHER',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('08617963-3b0f-4ca7-ae5b-092d9861a1fe',
        '8250292d-7bd9-4d3d-869c-0c54ebcf87c3',
        'Persil', 
        '15',
        'OTHER',
        'now()');

insert into products (productid,createdby,product_name,price,category,createdon) 
values ('1e28b01e-289b-4aae-ade8-18c32c639e32',
        '8250292d-7bd9-4d3d-869c-0c54ebcf87c3',
        'Salmon', 
        '97',
        'MEAT',
        'now()');
