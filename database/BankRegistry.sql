create database if not exists Banco;
use banco;

create table if not exists Clientes(
	id int not null auto_increment,
	passwd varchar(10),
	nombre varchar(50),
	ApPat varchar(50),
	ApMat varchar(50),
	Telefono varchar(50),
	Calle varchar(50),
	Colonia varchar(50),
	Numero varchar(10),
	primary key(id)
);

create table if not exists Cuenta(
	id int not null auto_increment,
	balance double,
	id_cliente int not null,
	primary key(id),
	foreign key(id_cliente) references Clientes(id)
);

create table if not exists OperacionesCuenta(
	id int not null auto_increment,
	id_cuenta int not null,
	fechaOp date,
	tipoMovimiento varchar(50),
	cantidad double,
	primary key(id),
	foreign key(id_cuenta) references Cuenta(id)
);

create table if not exists Usuarios(
	id int not null auto_increment,
	passwd varchar(10) not null,
	ApPat varchar(50) not null,
	ApMat varchar(50) not null,
	nombre varchar(50) not null,
	email varchar(50) not null,
	tipo varchar(20) not null,
	primary key(id)
);