delete from venta_detalle;
delete from venta;
delete from factura;

delete from compra_detalle ;
delete from compra;
delete from solicitud_compra;


select * from producto where stock <20
update producto set descripcion = 'Manzanita' where id = 419;

update proveedor set descripcion = 'AppleStore' where id = 172
update venta set factura_id = null;

select * from venta;

select vd from venta_detalle vd where venta_id = 951;

select * from compra;

update compra set id = 550 where id = 952

select proveedor_id from producto;


select * from proveedor where id = 172;


select count(*) from cliente;