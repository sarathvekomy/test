drop table if exists vb_user_defined_alerts_history;
drop table if exists vb_user_defined_alerts_notifications;
drop table if exists vb_excess_cash;
drop table if exists vb_trending;
drop table if exists vb_my_sales;
drop table if exists vb_user_defined_alerts;
drop table if exists vb_system_alerts_history;
drop table if exists vb_system_alerts_notifications;
drop table if exists vb_system_alerts;
drop table if exists vb_day_book_change_request_products;
drop table if exists vb_day_book_change_request_amount;
drop table if exists vb_day_book_change_request;
drop table if exists vb_day_book_products;
drop table if exists vb_day_book_amount;
drop table if exists vb_day_book;
drop table if exists vb_journal_change_request;
drop table if exists vb_journal;
drop table if exists vb_sales_return_change_request_products;
drop table if exists vb_sales_return_change_request;
drop table if exists vb_sales_return_products;
drop table if exists vb_sales_return;
drop table if exists vb_delivery_note_change_request_payments;
drop table if exists vb_delivery_note_change_request_products;
drop table if exists vb_delivery_note_change_request;
drop table if exists vb_delivery_note_payments;
drop table if exists vb_delivery_note_products;
drop table if exists vb_delivery_note;
drop table if exists vb_customer_advance_info;
drop table if exists vb_customer_credit_info;
drop table if exists vb_sales_book_products;
drop table if exists vb_sales_book;
drop table if exists vb_employee_customer;
drop table if exists vb_product_customer_cost;
drop table if exists vb_customer_change_request_details;
drop table if exists vb_customer_change_request;
drop table if exists vb_customer_detail;
drop table if exists vb_customer;
drop table if exists vb_product_inventory_transaction;
drop table if exists vb_product;
drop table if exists vb_assign_organizations;
drop table if exists vb_employee_address;
drop table if exists vb_employee_detail;
drop table if exists vb_employee;
drop table if exists vb_alert_type_my_sales_page;
drop table if exists vb_alert_type_my_sales;
drop table if exists vb_alert_type;
drop table if exists vb_alert_category;
drop table if exists vb_notifications;
drop table if exists vb_journal_types;
drop table if exists vb_address_types;
drop table if exists vb_payment_types;
drop table if exists vb_organization_mapping;
drop table if exists vb_organization;
drop table if exists vb_role_module_mapping;
drop table if exists vb_modules;
drop table if exists vb_authority;
drop table if exists vb_role;
drop table if exists vb_user_setting;
drop table if exists vb_login_track;
drop table if exists vb_login;

/*---------------------------  table : vb_login  ---------------------------*/

create table vb_login
(
  id int(7)not null auto_increment primary key,
  username varchar(20) unique not null,
  password varchar(100) not null,
  enabled char(1)  not null,
  first_time char(1),
  wrong_password_count int(7) 
);

create index vb_login_index on vb_login(id);

/*---------------------------  table : vb_login_track ---------------------------*/

create table vb_login_track
(
  id int(7)not null auto_increment primary key,
  username varchar(25) not null,
  last_login_time timestamp
);

create index vb_login_track_index on vb_login_track(id);

/*---------------------------  table : vb_user_setting  ---------------------------*/

create table vb_user_setting
(
  id int(7)not null auto_increment primary key,
  username varchar(20) not null,
  theme  varchar(20),
  favorite varchar(100)
);

create index vb_user_setting_index on vb_user_setting(id);

/*---------------------------  table : vb_role  ---------------------------*/

create table vb_role
(
  id int(7)not null auto_increment primary key,
  role_name varchar(20) not null,
  description text
);

create index vb_role_index on vb_role(id);

/*---------------------------  table : vb_authority  ---------------------------*/

create table vb_authority
(
  id int(7)not null auto_increment primary key,
  login_id int(7)not null,
  role_id int(7)not null,
  foreign key (login_id) references vb_login(id),
  foreign key (role_id) references vb_role(id)
);

create index vb_authority_index on vb_authority(id);

/*---------------------------  table : vb_modules  ---------------------------*/

create table vb_modules
(
  id int(7)not null auto_increment primary key,
  module_name varchar(60) not null
);

create index vb_modules_index on vb_modules(id);


/*---------------------------  table : vb_role_module_mapping  ---------------------------*/

create table vb_role_module_mapping
(
  id int(7)not null auto_increment primary key,
  module_id int(7)not null,
  role_id int(7)not null,
  foreign key (module_id) references vb_modules(id),
  foreign key (role_id) references vb_role(id)
);

create index vb_role_module_mapping_index on vb_role_module_mapping(id);

/*---------------------------  table : vb_organization  ---------------------------*/

create table vb_organization
(
  id int(7)not null auto_increment primary key,
  super_user_name varchar(30) not null,
  name varchar(100) not null,
  organization_code varchar(30) not null,
  full_name varchar(100) not null,
  branch_name varchar(100) not null,
  main_branch varchar(5) not null,
  description text,
  address_line_1 varchar(200) not null,
  address_line_2 varchar(200),
  locality varchar(60)  not null,
  landmark varchar(60),
  city varchar(60) not null,
  state varchar(60) not null,
  country varchar(50) not null,
  currency_format varchar(20) not null,
  zipcode varchar(10)not null,
  phone1 varchar(20),
  phone2 varchar(20),
  phone3 varchar(20),
  email varchar(100) not null,
  created_on datetime,
  created_by varchar(50),
  modified_on timestamp,
  modified_by varchar(50)
);

create index vb_organization_index on vb_organization(id);

/*---------------------------  table : vb_organization_mapping  ---------------------------*/

create table vb_organization_mapping
(
  id int(7)not null auto_increment primary key,
  main_branch_id int(7),
  sub_branch_id int(7),
  foreign key (main_branch_id) references vb_organization(id),
  foreign key (sub_branch_id) references vb_organization(id)
);

create index vb_organization_mapping_index on vb_organization_mapping(id);

/*--------------------------- table : vb_payment_types--------------------------*/

create table vb_payment_types
(
  id int(7)not null auto_increment primary key,
  created_on datetime,
  created_by varchar(50),
  modified_on timestamp,
  modified_by varchar(50),
  payment_type varchar(30) not null,
  description text,
  organization_id int(7) not null,
  foreign key (organization_id) references vb_organization(id)
);
create index vb_payment_types_index on vb_payment_types(id);

/*--------------------------- table : vb_address_types--------------------------*/

create table vb_address_types
(
  id int(7)not null auto_increment primary key,
  created_on datetime,
  created_by varchar(50),
  modified_on timestamp,
  modified_by varchar(50),
  address_type varchar(30) not null,
  description text,
  organization_id int(7) not null,
  foreign key (organization_id) references vb_organization(id)
);
create index vb_address_types_index on vb_address_types(id);

/*--------------------------- table : vb_journal_types--------------------------*/

create table vb_journal_types
(
  id int(7)not null auto_increment primary key,
  created_on datetime,
  created_by varchar(50),
  modified_on timestamp,
  modified_by varchar(50),
  journal_type varchar(30) not null,
  invoice_no varchar(30),
  description text,
  organization_id int(7) not null,
  foreign key (organization_id) references vb_organization(id)
);
create index vb_journal_types_index on vb_journal_types(id);

/*---------------------------  table : vb_notifications  ---------------------------*/

create table vb_notifications
(
  id int(7) not null auto_increment primary key,
  notification_type varchar(80) not null
);

create index vb_notifications_index on vb_notifications(id);


/*---------------------------  table : vb_alert_category  ---------------------------*/

create table vb_alert_category
(
  id int(7) not null auto_increment primary key,
  alert_category varchar(80) not null
);

create index vb_alert_category_index on vb_alert_category(id);


/*---------------------------  table : vb_alert_type  ---------------------------*/

create table vb_alert_type
(
  id int(7) not null auto_increment primary key,
  alert_category_id int(7) not null,
  alert_type varchar(80) not null,
  foreign key (alert_category_id) references vb_alert_category(id)
);

create index vb_alert_type_index on vb_alert_type(id);


/*---------------------------  table : vb_alert_type_my_sales  ---------------------------*/

create table vb_alert_type_my_sales
(
  id int(7) not null auto_increment primary key,
  alert_type_id int(7) not null,
  alert_my_sales varchar(80) not null,
  foreign key (alert_type_id) references vb_alert_type(id)
);

create index vb_alert_type_my_sales_index on vb_alert_type_my_sales(id);


/*---------------------------  table : vb_alert_type_my_sales_page  ---------------------------*/

create table vb_alert_type_my_sales_page
(
  id int(7) not null auto_increment primary key,
  alert_type_my_sales_id int(7) not null,
  alert_my_sales_page varchar(80) not null,
  foreign key (alert_type_my_sales_id) references vb_alert_type_my_sales(id)
);

create index vb_alert_type_my_sales_page_index on vb_alert_type_my_sales_page(id);


/*---------------------------  table : vb_employee  ---------------------------*/

create table vb_employee
(
  id int(7)not null auto_increment primary key,
  username varchar(20) not null,
  first_name varchar(35) not null,
  middle_name varchar(35),
  last_name varchar(35) not null,
  employee_number varchar(100),
  employee_type varchar(30) not null,
  employee_email varchar(100) not null,
  gender char(1) default 'M',
  organization_id int(7),
  created_on datetime,
  created_by varchar(50),
  modified_on timestamp,
  modified_by varchar(50),
  foreign key (organization_id) references vb_organization(id)
);

create index vb_employee_index on vb_employee(id);

/*---------------------------  table : vb_employee_detail  ---------------------------*/

create table vb_employee_detail
(
  id int(7)not null auto_increment primary key,
  employee_id int(7)not null,
  mobile varchar(80) not null,
  alternate_mobile varchar(80),
  direct_line varchar(80),
  blood_group varchar(20) not null,
  nationality varchar(50) not null,
  passport_number varchar(50) not null,
  foreign key (employee_id) references vb_employee(id)
);

create index vb_employee_detail_index on vb_employee_detail(id);

/*---------------------------  table : vb_employee_address  ---------------------------*/

create table vb_employee_address
(
  id int(7)not null auto_increment primary key,
  employee_id int(7)not null,
  address_line_1 varchar(200) not null,
  address_line_2 varchar(200),
  locality varchar(60)  not null,
  landmark varchar(60),
  city varchar(60)not null,
  state varchar(60)not null,
  zipcode varchar(10)not null,
  address_type varchar(30) not null,
  foreign key (employee_id) references vb_employee(id)
);

create index vb_employee_address_index on vb_employee_address(id);

/*---------------------------  table : vb_assign_organizations  ---------------------------*/

create table vb_assign_organizations
(
  id int(7) not null auto_increment primary key,
  employee_id int(7) not null,
  created_on datetime,
  created_by varchar(50),
  modified_on timestamp,
  modified_by varchar(50),
  organization_id int(7)not null,
  foreign key (employee_id) references vb_employee(id),
  foreign key (organization_id) references vb_organization(id)
);

create index vb_assign_organizations_index on vb_assign_organizations(id);

/*---------------------------  table : vb_product  ---------------------------*/

create table vb_product
(
  id int(7)not null auto_increment primary key,
  product_name varchar(200) not null,
  batch_number varchar(100) not null,
  product_category varchar(100),
  brand varchar(100) not null,
  model varchar(100) not null,
  description text,
  cost_per_quantity float(12,2) not null,
  quantity_arrived int(10),
  quantity_at_warehouse int(10),
  available_quantity int(10),
  total_quantity int(10),
  organization_id int(7)not null,
  created_on datetime,
  created_by varchar(50),
  modified_on timestamp,
  modified_by varchar(50),
  foreign key (organization_id) references vb_organization(id)
);

create index vb_product_index on vb_product(id);

/*---------------------------  table : vb_product_inventory_transaction  ---------------------------*/

create table vb_product_inventory_transaction
(
  id int(7)not null auto_increment primary key,
  product_name varchar(100) not null,
  batch_number varchar(100) not null,
  created_on datetime,
  created_by varchar(50),
  modified_on timestamp,
  modified_by varchar(50),
  sales_executive varchar(50),
  inwards_qty int(10),
  outwards_qty int(10),
  organization_id int(7)not null,
  foreign key (organization_id) references vb_organization(id)
);

create index vb_product_inventory_transaction_index on vb_product_inventory_transaction(id);


/*---------------------------  table : vb_customer  ---------------------------*/

create table vb_customer
(
  id int(7)not null auto_increment primary key,
  business_name varchar(100) not null,
  invoice_name varchar(100) not null,
  customer_name varchar(100) not null,
  gender char(1) default 'M',
  organization_id int(7)not null,
  created_on datetime,
  created_by varchar(50),
  modified_on timestamp,
  modified_by varchar(50),
  credit_limit float(12,2),
  credit_overdue_days int(3),
  foreign key (organization_id) references vb_organization(id)
);

create index vb_customer_index on vb_customer(id);

/*---------------------------  table : vb_customer_detail  ---------------------------*/

create table vb_customer_detail
(
  id int(7)not null auto_increment primary key,
  customer_id int(7)not null,
  address_line_1 varchar(200),
  address_line_2 varchar(200),
  region varchar(50) not null,
  locality varchar(60) not null,
  landmark varchar(60),
  city varchar(60),
  state varchar(60),
  zipcode varchar(10),
  mobile varchar(80) not null,
  alternate_mobile varchar(80),
  direct_line varchar(80),
  email varchar(100) not null,
  foreign key (customer_id) references vb_customer(id)
);

create index vb_customer_detail_index on vb_customer_detail(id);

/*---------------------------  table : vb_customer_change_request  ---------------------------*/

create table vb_customer_change_request
(
  id int(7)not null auto_increment primary key,
  business_name varchar(200) not null,
  gender char(1) default 'M',
  created_on datetime,
  created_by varchar(50),
  modified_on timestamp,
  modified_by varchar(50),
  organization_id int(7)not null,
  cr_type bit,
  status varchar(20),
  foreign key (organization_id) references vb_organization(id)
);

create index vb_customer_change_request_index on vb_customer_change_request(id);

/*---------------------------  table : vb_customer_change_request_details  ---------------------------*/

create table vb_customer_change_request_details
(
  id int(7)not null auto_increment primary key,
  customer_change_request_id int(7) not null,
  customer_name varchar(100) not null,
  invoice_name varchar(100) not null,
  address_line_1 varchar(200),
  address_line_2 varchar(200),
  region varchar(50) not null,
  locality varchar(60) not null,
  landmark varchar(60),
  city varchar(60)not null,
  state varchar(60)not null,
  zipcode varchar(10)not null,
  address_type varchar(30) not null,
  mobile varchar(80) not null,
  alternate_mobile varchar(80),
  email varchar(100) not null,
  direct_line varchar(80),
  created_by varchar(50),
  created_date datetime,
  modified_by varchar(50),
  modified_date timestamp,
  foreign key (customer_change_request_id) references vb_customer_change_request(id)
);

create index vb_customer_change_request_details_index on vb_customer_change_request_details(id);

/*---------------------------  table : vb_product_customer_cost  ---------------------------*/

create table vb_product_customer_cost
(
  id int(7)not null auto_increment primary key,
  customer_id int(7) not null,
  product_id int(7) not null,
  cost float(12,2) not null,
  created_on datetime,
  created_by varchar(50),
  modified_on timestamp,
  modified_by varchar(50),
  organization_id int(7)not null,
  foreign key (product_id) references vb_product(id),
  foreign key (customer_id) references vb_customer(id),
  foreign key (organization_id) references vb_organization(id)
);

create index vb_product_customer_cost_index on vb_product_customer_cost(id);

/*---------------------------  table : vb_employee_customer  ---------------------------*/

create table vb_employee_customer
(
  id int(7)not null auto_increment primary key,
  employee_id int(7) not null,
  customer_id int(7) not null,
  created_on datetime,
  created_by varchar(50),
  modified_on timestamp,
  modified_by varchar(50),
  organization_id int(7)not null,
  foreign key (employee_id) references vb_employee(id),
  foreign key (customer_id) references vb_customer(id),
  foreign key (organization_id) references vb_organization(id)
);

create index vb_employee_customer_index on vb_employee_customer(id);

/*---------------------------  table : vb_sales_book  ---------------------------*/

create table vb_sales_book
(
  id int(7)not null auto_increment primary key,
  created_on datetime,
  created_by varchar(50),
  modified_on timestamp,
  modified_by varchar(50),
  sales_executive varchar(50) not null,
  advance float(20,2),
  opening_balance float(20,2),
  closing_balance float(20,2),
  flag int(7),
  cycle_id int(7),
  allotment_type varchar(20) not null,
  organization_id int(7)not null,
  foreign key (organization_id) references vb_organization(id)
);

create index vb_sales_index on vb_sales_book(id);

/*---------------------------  table : vb_sales_book_products  ---------------------------*/

create table vb_sales_book_products
(
  id int(7)not null auto_increment primary key,
  sales_id int(7) not null,
  product_name varchar(100) not null,
  batch_number varchar(100) not null,
  qty_allotted int(10),
  qty_opening_balance int(10),
  qty_sold int(10),
  qty_to_factory int(10),
  qty_closing_balance int(10),
  remarks text,
  foreign key (sales_id) references vb_sales_book(id)
);

create index vb_sales_book_products_index on vb_sales_book_products(id);
/*---------------------------  table : vb_customer_credit_info  ---------------------------*/
/*---------------------------  table : table in DB to save previous credit info  ---------------------------*/
create table vb_customer_credit_info
(
  id int(7)not null auto_increment primary key,
  business_name varchar(200) not null,
  credit_from text not null,
  debit_to text,
  balance float(12,2) not null,
  due float(12,2),
  created_on datetime,
  created_by varchar(50),
  modified_on timestamp,
  modified_by varchar(50),
  organization_id int(7)not null,
  foreign key (organization_id) references vb_organization(id)
);

create index vb_customer_credit_info_index on vb_customer_credit_info(id);

/*---------------------------  table : vb_customer_advance_info  ---------------------------*/
/*---------------------------  table : table in DB to save advance info  ---------------------------*/
create table vb_customer_advance_info
(
  id int(7)not null auto_increment primary key,
  business_name varchar(200) not null,
  credit_from text not null,
  debit_to text,
  advance float(12,2) not null,
  balance float(12,2) not null,
  created_on datetime,
  created_by varchar(50),
  modified_on timestamp,
  modified_by varchar(50),
  organization_id int(7)not null,
  foreign key (organization_id) references vb_organization(id)
);

create index vb_customer_advance_info_index on vb_customer_advance_info(id);

/*---------------------------  table : vb_delivery_note  ---------------------------*/

create table vb_delivery_note
(
  id int(7)not null auto_increment primary key,
  invoice_no text not null,
  created_on datetime,
  created_by varchar(50),
  modified_on timestamp,
  modified_by varchar(50),
  business_name varchar(200) not null,
  invoice_name varchar(200),
  sales_id int(7) not null,
  organization_id int(7) not null,
  foreign key (sales_id) references vb_sales_book(id),
  foreign key (organization_id) references vb_organization(id)
);

create index vb_delivery_note_index on vb_delivery_note(id);

/*---------------------------  table : vb_delivery_note_products  ---------------------------*/

create table vb_delivery_note_products
(
  id int(7)not null auto_increment primary key,
  delivery_note_id int(7) not null,
  product_name varchar(100) not null,
  batch_number varchar(100) not null,
  product_qty int(10) not null,
  bonus_qty int(10),
  bonus_reason text,
  product_cost float(20,2) not null,
  total_cost float(20,2) not null,
  foreign key (delivery_note_id) references vb_delivery_note(id)
);

create index vb_delivery_note_products_index on vb_delivery_note_products(id);

/*---------------------------  table : vb_delivery_note_payments  ---------------------------*/

create table vb_delivery_note_payments
(
  id int(7)not null auto_increment primary key,
  delivery_note_id int(7) not null,
  present_payable float(20,2) not null,
  previous_credit float(29,2) not null,
  present_advance float(20,2) not null,
  total_payable float(20,2) not null,
  present_payment float(20,2) not null,
  balance float(20,2) not null,
  payment_type varchar(20) not null,
  cheque_no varchar(50),
  bank_name varchar(100),
  branch_name varchar(100),
  foreign key (delivery_note_id) references vb_delivery_note(id)
);

create index vb_delivery_note_payments_index on vb_delivery_note_payments(id);

/*--------------------------- table : vb_delivery_note_change_request--------------------------*/


create table vb_delivery_note_change_request
(
  id int(7)not null auto_increment primary key,
  invoice_no text not null,
  created_on datetime,
  created_by varchar(50),
  modified_on timestamp,
  modified_by varchar(50),
  business_name varchar(200) not null,
  invoice_name varchar(200),
  sales_id int(7) not null,
  status varchar(20),
  cr_description text,
  organization_id int(7) not null,
  foreign key (sales_id) references vb_sales_book(id),
  foreign key (organization_id) references vb_organization(id)
);

create index vb_delivery_note_change_requestindex on vb_delivery_note_change_request(id);

/*---------------------------  table : vb_delivery_note_change_request_products  ---------------------------*/

create table vb_delivery_note_change_request_products
(
  id int(7)not null auto_increment primary key,
  delivery_note_change_request_id int(7) not null,
  product_name varchar(100) not null,
  batch_number varchar(100) not null,
  product_qty varchar(10) not null,
  bonus_qty varchar(10),
  bonus_reason text,
  product_cost varchar(20) not null,
  total_cost varchar(20) not null,
  foreign key (delivery_note_change_request_id) references vb_delivery_note_change_request(id)
);

create index vb_delivery_note_change_request_products_index on vb_delivery_note_change_request_products(id);

/*---------------------------  table : vb_delivery_note_change_request_payments  ---------------------------*/

create table vb_delivery_note_change_request_payments
(
  id int(7)not null auto_increment primary key,
  delivery_note_change_request_id int(7) not null,
  present_payable varchar(20) not null,
  previous_credit varchar(20) not null,
  present_advance varchar(20) not null,
  total_payable varchar(20) not null,
  present_payment varchar(20) not null,
  balance varchar(20) not null,
  payment_type varchar(20) not null,
  cheque_no varchar(50),
  bank_name varchar(100),
  branch_name varchar(100),
  foreign key (delivery_note_change_request_id) references vb_delivery_note_change_request(id)
);

create index delivery_note_change_request_payments_index on vb_delivery_note_change_request(id);

/*---------------------------  table : vb_sales_return  ---------------------------*/

create table vb_sales_return
(
  id int(7)not null auto_increment primary key,
  invoice_no text not null,
  created_on datetime,
  created_by varchar(50),
  modified_on timestamp,
  modified_by varchar(50),
  business_name varchar(100) not null,
  invoice_name varchar(100) not null,
  products_grand_total float(20,2) not null,
  sales_id int(7) not null,
  status varchar(20),
  organization_id int(7)not null,
  foreign key (sales_id) references vb_sales_book(id),
  foreign key (organization_id) references vb_organization(id)
);

create index vb_sales_return_index on vb_sales_return(id);

/*---------------------------  table : vb_sales_return_products  ---------------------------*/

create table vb_sales_return_products
(
  id int(7)not null auto_increment primary key,
  sales_return_id int(7)not null,
  product_name varchar(100) not null,
  batch_number varchar(100) not null,
  damaged int(10),
  resalable int(10),
  total_qty int(10),
  cost float(20,2) not null,
  total_cost float(20,2) not null,
  foreign key (sales_return_id) references vb_sales_return(id)
);

create index vb_sales_return_products_index on vb_sales_return_products(id);

/*---------------------------  table : vb_sales_return_change_request  ---------------------------*/

create table vb_sales_return_change_request
(
  id int(7)not null auto_increment primary key,
  invoice_no varchar(100) not null,
  created_on datetime,
  created_by varchar(50),
  modified_on timestamp,
  modified_by varchar(50),
  business_name varchar(100) not null,
  invoice_name varchar(100) not null,
  products_grand_total varchar(20) not null,
  sales_id int(7) not null,
  status varchar(20),
  cr_description text,
  organization_id int(7)not null,
  foreign key (sales_id) references vb_sales_book(id),
  foreign key (organization_id) references vb_organization(id)
);

create index vb_sales_return_change_request_index on vb_sales_return_change_request(id);

/*---------------------------  table : vb_sales_return_change_request_products  ---------------------------*/

create table vb_sales_return_change_request_products
(
  id int(7)not null auto_increment primary key,
  sales_return_change_request_id int(7)not null,
  product_name varchar(100) not null,
  batch_number varchar(100) not null,
  damaged varchar(10),
  resalable varchar(10),
  total_qty varchar(10),
  cost varchar(20) not null,
  total_cost varchar(20) not null,
  foreign key (sales_return_change_request_id) references vb_sales_return_change_request(id)
);

create index vb_sales_return_change_request_products_index on vb_sales_return_change_request_products(id);

/*---------------------------  table : vb_journal  ---------------------------*/

create table vb_journal
(
  id int(7)not null auto_increment primary key,
  created_on datetime,
  created_by varchar(50),
  modified_on timestamp,
  modified_by varchar(50),
  business_name varchar(100) not null,
  invoice_name varchar(100) not null,
  invoice_no varchar(100) not null,
  status varchar(20),
  journal_type varchar(30) not null,
  amount float(20,2) not null,
  description text,
  sales_id int(7) not null,
  organization_id int(7) not null,
  foreign key (sales_id) references vb_sales_book(id),
  foreign key (organization_id) references vb_organization(id)
);

create index vb_journal_index on vb_journal(id);

/*---------------------------  table : vb_journal_change_request  ---------------------------*/

create table vb_journal_change_request
(
  id int(7)not null auto_increment primary key,
  created_on datetime,
  created_by varchar(50),
  modified_on timestamp,
  modified_by varchar(50),
  business_name varchar(100) not null,
  invoice_name varchar(100) not null,
  invoice_no varchar(100) not null,
  status varchar(20),
  journal_type varchar(30) not null,
  amount varchar(20) not null,
  description text,
  cr_description text,
  sales_id int(7) not null,
  organization_id int(7) not null,
  foreign key (sales_id) references vb_sales_book(id),
  foreign key (organization_id) references vb_organization(id)
);

create index vb_journal_change_request_index on vb_journal_change_request(id);

/*---------------------------  table : vb_day_book  ---------------------------*/

create table vb_day_book
(
  id int(7)not null auto_increment primary key,
  created_on datetime,
  created_by varchar(50),
  modified_on timestamp,
  modified_by varchar(50),
  sales_executive varchar(50) not null,
  sales_id int(7) not null,
  is_return bit,
  driver_name varchar(50),
  vehicle_no varchar(20) not null,
  starting_reading float(20,2) not null,
  ending_reading float(20,2) not null,
  organization_id int(7) not null,
  remarks varchar(255),
  foreign key (sales_id) references vb_sales_book(id),
  foreign key (organization_id) references vb_organization(id)
);

create index vb_day_book_index on vb_day_book(id);

/*---------------------------  table : vb_day_book_amount  ---------------------------*/

create table vb_day_book_amount
(
  id int(7) not null auto_increment primary key,
  day_book_id int(7) not null,
  executive_allowances float(12,2),
  driver_allowances float(12,2),
  vehicle_fuel_expenses float(12,2),
  vehicle_meter_reading float(20,2),
  vehicle_maintenance_expenses float(12,2),
  offloading_loading_charges float(12,2),
  dealer_party_expenses float(12,2),
  municipal_city_council float(12,2),
  miscellaneous_expenses float(12,2),
  reason_miscellaneous_expenses text,
  total_allowances float(20,2),
  customer_total_payable float(20,2),
  customer_total_received float(20,2),
  customer_total_credit float(20,2),
  amount_to_bank float(20,2),
  reason_amount_to_bank text,
  amount_to_factory float(20,2),
  closing_balance float(20,2),
  foreign key (day_book_id) references vb_day_book(id)
);

create index vb_day_book_amount_index on vb_day_book_amount(id);

/*---------------------------  table : vb_day_book_products  ---------------------------*/

create table vb_day_book_products
(
  id int(7) not null auto_increment primary key,
  day_book_id int(7) not null,
  product_name varchar(100),
  batch_number varchar(100) not null,
  opening_stock int(10),
  products_to_customer int(10),
  products_to_factory int(10),
  closing_stock int(10),
  foreign key (day_book_id) references vb_day_book(id)
);

create index vb_day_book_products_index on vb_day_book_products(id);



/*---------------------------  table : vb_day_book_change_request  ---------------------------*/

create table vb_day_book_change_request
(
  id int(7)not null auto_increment primary key,
  created_on datetime,
  created_by varchar(50),
  modified_on timestamp,
  modified_by varchar(50),
  sales_executive varchar(50) not null,
  sales_id int(7) not null,
  is_return bit,
  status varchar(20),
  driver_name varchar(50),
  vehicle_no varchar(20) not null,
  starting_reading varchar(20) not null,
  ending_reading varchar(20) not null,
  organization_id int(7) not null,
  remarks text,
  cr_description text,
  foreign key (sales_id) references vb_sales_book(id),
  foreign key (organization_id) references vb_organization(id)
);

create index vb_day_book_change_request_index on vb_day_book_change_request(id);

/*---------------------------  table : vb_day_book_change_request_amount  ---------------------------*/

create table vb_day_book_change_request_amount
(
  id int(7) not null auto_increment primary key,
  day_book_change_request_id int(7) not null,
  executive_allowances varchar(12),
  driver_allowances varchar(12),
  vehicle_fuel_expenses varchar(12),
  vehicle_meter_reading varchar(20),
  vehicle_maintenance_expenses varchar(12),
  offloading_loading_charges varchar(12),
  dealer_party_expenses varchar(12),
  municipal_city_council varchar(12),
  miscellaneous_expenses varchar(12),
  reason_miscellaneous_expenses text,
  total_allowances varchar(20),
  customer_total_payable varchar(20),
  customer_total_received varchar(20),
  customer_total_credit varchar(20),
  amount_to_bank varchar(20),
  reason_amount_to_bank text,
  amount_to_factory varchar(20),
  closing_balance varchar(20),
  foreign key (day_book_change_request_id) references vb_day_book_change_request(id)
);

create index vb_day_book_change_request_amount_index on vb_day_book_change_request(id);

/*---------------------------  table : vb_day_book_change_request_products  ---------------------------*/

create table vb_day_book_change_request_products
(
  id int(7) not null auto_increment primary key,
  day_book_change_request_id int(7) not null,
  product_name varchar(100),
  batch_number varchar(100) not null,
  opening_stock varchar(10),
  products_to_customer varchar(10),
  products_to_factory varchar(10),
  closing_stock varchar(10),
  foreign key (day_book_change_request_id) references vb_day_book_change_request(id)
);

create index vb_day_book_change_request_products_index on vb_day_book_change_request_products(id);

/*---------------------------  table : vb_system_alerts  ---------------------------*/

create table vb_system_alerts
(
  id int(7) not null auto_increment primary key,
  alert_name varchar(80) not null,
  alert_type_id int(7) not null,
  description text,
  active_inactive bit,
  created_on datetime,
  created_by varchar(50),
  modified_on timestamp,
  modified_by varchar(50),
  organization_id int(7),
  foreign key (organization_id) references vb_organization(id),
  foreign key (alert_type_id) references vb_alert_type(id)
);

create index vb_system_alerts_index on vb_system_alerts(id);

/*---------------------------  table : vb_system_alerts_notifications  ---------------------------*/

create table vb_system_alerts_notifications
(
  id int(7) not null auto_increment primary key,
  system_alert_id int(7) not null,
  notification_id int(7) not null,
  role_id int(7) not null,
  user_id int(7) not null,
  mails_to bit,
  mails_cc bit,
  mails_bcc bit,
  foreign key (system_alert_id) references vb_system_alerts(id),
  foreign key (notification_id) references vb_notifications(id),
  foreign key (role_id) references vb_role(id),
  foreign key (user_id) references vb_employee(id)
);

create index vb_system_alerts_notifications_index on vb_system_alerts_notifications(id);

/*---------------------------  table : vb_system_alerts_history  ---------------------------*/

create table vb_system_alerts_history
(
  id int(7)not null auto_increment primary key,
  system_alert_id int(7) not null,
  created_on datetime,
  created_by varchar(50),
  modified_on datetime,
  modified_by varchar(50),
  reference varchar(50),
  foreign key (system_alert_id) references vb_system_alerts(id)
);

create index vb_system_alerts_history_index on vb_system_alerts_history(id);

/*---------------------------  table : vb_user_defined_alerts  ---------------------------*/

create table vb_user_defined_alerts
(
  id int(7) not null auto_increment primary key,
  alert_name varchar(80) not null, 
  alert_type_id int(7) not null,
  active_inactive bit,
  created_on datetime,
  created_by varchar(50),
  modified_on timestamp,
  modified_by varchar(50),
  description text,
  organization_id int(7),
  foreign key (alert_type_id) references vb_alert_type(id),
  foreign key (organization_id) references vb_organization(id)
);

create index vb_user_defined_alerts_index on vb_user_defined_alerts(id);

/*---------------------------  table : vb_my_sales  ---------------------------*/

create table vb_my_sales
(
  id int(7) not null auto_increment primary key,
  user_defined_alerts_id int(7) not null,
  alert_type_my_sales_id int(7) not null,
  alert_type_my_sales_page_id int(7) not null,
  description text,
  foreign key (user_defined_alerts_id) references vb_user_defined_alerts(id),
  foreign key (alert_type_my_sales_id) references vb_alert_type_my_sales(id),
  foreign key (alert_type_my_sales_page_id) references vb_alert_type_my_sales_page(id)
);

create index vb_my_sales on vb_my_sales(id);

/*---------------------------  table : vb_trending  ---------------------------*/

create table vb_trending
(
  id int(7) not null auto_increment primary key,
  user_defined_alerts_id int(7) not null,
  amount_persentage numeric(10),
  product_percentage numeric(10),
  description text,
  foreign key (user_defined_alerts_id) references vb_user_defined_alerts(id)
);

create index vb_my_sales on vb_trending(id);

/*---------------------------  table : vb_excess_cash  ---------------------------*/

create table vb_excess_cash
(
  id int(7) not null auto_increment primary key,
  user_defined_alerts_id int(7) not null,
  amount float(20,2) not null,
  description text,
  foreign key (user_defined_alerts_id) references vb_user_defined_alerts(id)
);

create index vb_excess_cash on vb_excess_cash(id);

/*---------------------------  table : vb_user_defined_alerts_notifications  ---------------------------*/

create table vb_user_defined_alerts_notifications
(
  id int(7) not null auto_increment primary key,
  user_defined_alerts_id int(7) not null,
  notification_id int(7) not null,
  role_id int(7) not null,
  user_id int(7) not null,
  mails_to bit,
  mails_cc bit,
  mails_bcc bit,
  foreign key (user_defined_alerts_id) references vb_user_defined_alerts(id),
  foreign key (notification_id) references vb_notifications(id),
  foreign key (role_id) references vb_role(id),
  foreign key (user_id) references vb_employee(id)
);

create index vb_user_defined_alerts_notifications on vb_user_defined_alerts_notifications(id);

/*---------------------------  table : vb_user_defined_alerts_history  ---------------------------*/

create table vb_user_defined_alerts_history
(
  id int(7)not null auto_increment primary key,
  user_defined_alerts_id int(7) not null,
  created_on datetime,
  created_by varchar(50),
  modified_on datetime,
  modified_by varchar(50),
  reference varchar(50),
  foreign key (user_defined_alerts_id) references vb_user_defined_alerts(id)
);

create index vb_user_defined_alerts_history_index on vb_user_defined_alerts_history(id);
