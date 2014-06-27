insert into vb_role values(1,'ROLE_USER','User');
insert into vb_role values(2,'ROLE_SITEADMIN','Site Administrator');
insert into vb_role values(3,'ROLE_MANAGEMENT','Management');
insert into vb_role values(4,'ROLE_ACCOUNTANT','Accountant');
insert into vb_role values(5,'ROLE_SALESEXECUTIVE','Sales Executive');
insert into vb_role values(6,'ROLE_GROUPHEAD','Group Head');

insert into vb_login values(1,'admin','21232f297a57a5a743894a0e4a801fc3', 1,'N', 0);
insert into vb_authority values(1,1,1);
insert into vb_authority values(2,1,2);
insert into vb_authority values(3,1,3);
insert into vb_authority values(4,1,4);
insert into vb_authority values(5,1,5);
insert into vb_authority values(6,1,6);

insert into vb_organization values(1,'User Group', 'User Group', 'UG', '', '', '', 'Organization Group', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ');

insert into vb_notifications(id,notification_type) values (1,'In System Alert');
insert into vb_notifications(id,notification_type) values (2,'SMS');
insert into vb_notifications(id,notification_type) values (3,'Emails');

insert into vb_alert_category(id,alert_category) values (1,'System Alerts');
insert into vb_alert_category(id,alert_category) values (2,'User Defined Alerts');

insert into vb_alert_type(id,alert_category_id,alert_type) values (1,1,'System Defaults');
insert into vb_alert_type(id,alert_category_id,alert_type) values (2,1,'Credit Overdue');
insert into vb_alert_type(id,alert_category_id,alert_type) values (3,1,'Stock Allotment');
insert into vb_alert_type(id,alert_category_id,alert_type) values (4,1,'Day Book closure');
insert into vb_alert_type(id,alert_category_id,alert_type) values (5,1,'Wrong login for 5 times');
insert into vb_alert_type(id,alert_category_id,alert_type) values (6,1,'Allowance Overlimit');
insert into vb_alert_type(id,alert_category_id,alert_type) values (7,2,'My Sales');
insert into vb_alert_type(id,alert_category_id,alert_type) values (8,2,'Trending');
insert into vb_alert_type(id,alert_category_id,alert_type) values (9,2,'Excess Amount');

insert into vb_alert_type_my_sales(id,alert_type_id,alert_my_sales) values (1,7,'Approvals');
insert into vb_alert_type_my_sales(id,alert_type_id,alert_my_sales) values (2,7,'Transaction CR');

insert into vb_alert_type_my_sales_page(id,alert_type_my_sales_id,alert_my_sales_page) values (1,1,'New Customer CR');
insert into vb_alert_type_my_sales_page(id,alert_type_my_sales_id,alert_my_sales_page) values (2,1,'Existing Customer CR');
insert into vb_alert_type_my_sales_page(id,alert_type_my_sales_id,alert_my_sales_page) values (3,1,'Sales Return');
insert into vb_alert_type_my_sales_page(id,alert_type_my_sales_id,alert_my_sales_page) values (4,1,'Journal');
insert into vb_alert_type_my_sales_page(id,alert_type_my_sales_id,alert_my_sales_page) values (5,2,'Delivery Note');
insert into vb_alert_type_my_sales_page(id,alert_type_my_sales_id,alert_my_sales_page) values (6,2,'Sales Return');
insert into vb_alert_type_my_sales_page(id,alert_type_my_sales_id,alert_my_sales_page) values (7,2,'Journal');
insert into vb_alert_type_my_sales_page(id,alert_type_my_sales_id,alert_my_sales_page) values (8,2,'Day Book');

