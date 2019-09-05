/*=====================================================================
================================= orders ==============================
=====================================================================*/

insert into app_orders (orderid,userid,supermarketid,price,order_list,location) 
values ('d3980a29-5960-4b7a-a4c5-f396b8c5c573',
        'f61f03a3-c548-45ad-a1df-08b9bd1b5453', /* Yoram Biberman */
        '8250292d-7bd9-4d3d-869c-0c54ebcf87c3', /* super or */
        '27',
        '{apple,banana,cherry}',
        '{31.770937,35.216932}');

insert into app_orders (orderid,userid,supermarketid,price,order_list,location) 
values ('f2520141-3e16-4151-abda-56393807d233',
        'ca865ffd-50bd-49e2-9f15-a0b928a4cee5', /* Yosi Cohen */
        'cd914d6a-0c4f-43c5-9efa-421875a5f1a4', /* super naor */
        '27',
        '{apple,banana,cherry}',
        '{31.769350,35.224227}');

insert into app_orders (orderid,userid,supermarketid,price,order_list,location) 
values ('17b631fe-4cd8-427a-8a56-4203ff701117',
        '722f0964-52db-4d60-8062-0db73876c87a', /* Dana Levi */
        '0f731813-dba3-4bf4-bbda-220aaf493666', /* super guy */
        '27',
        '{apple,banana,cherry}',
        '{31.761158,35.209207}');