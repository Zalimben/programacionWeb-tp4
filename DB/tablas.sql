/* 	Sequencias para los ID, este numero comienza a partir de 1000
	de manera a evitar conflictos con los datos semillas
*/

CREATE SEQUENCE seq_proveedor
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 99999
    MINVALUE 1;

CREATE SEQUENCE seq_cliente
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 99999
    MINVALUE 1;

CREATE SEQUENCE seq_factura
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 99999
    MINVALUE 1;

CREATE SEQUENCE seq_producto
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 99999
    MINVALUE 1;

CREATE SEQUENCE seq_venta
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 99999
    MINVALUE 1;

CREATE SEQUENCE seq_compra
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 99999
    MINVALUE 1;

CREATE SEQUENCE seq_compra_detalle
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 99999
    MINVALUE 1;

CREATE SEQUENCE seq_venta_detalle
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 99999
    MINVALUE 1;

CREATE SEQUENCE seq_solicitud_compra
    START WITH 1000
    INCREMENT BY 1
    MAXVALUE 99999
    MINVALUE 1;

/* Tablas */
CREATE TABLE proveedorId (
  id          BIGINT PRIMARY KEY DEFAULT nextval('seq_proveedor'),
  descripcion VARCHAR(100)
);

CREATE TABLE cliente (
  id               BIGINT PRIMARY KEY   DEFAULT nextval('seq_cliente'),
  nombre           VARCHAR(100),
  cedula_identidad VARCHAR(50) NOT NULL DEFAULT nextval('seq_cliente'::regclass),
  CONSTRAINT unique_cedula UNIQUE (cedula_identidad)
);

CREATE TABLE factura (
  id    BIGINT PRIMARY KEY DEFAULT nextval('seq_factura'),
  monto VARCHAR(50),
  fecha VARCHAR(50) WITHOUT TIME ZONE NOT NULL DEFAULT now()
);

CREATE TABLE producto (
  id          BIGINT PRIMARY KEY DEFAULT nextval('seq_producto'),
  descripcion VARCHAR(100) NOT NULL,
  stock       BIGINT,
  precio      BIGINT
);

CREATE TABLE venta (
  id         BIGINT DEFAULT nextval('seq_venta'),
  cliente_id BIGINT,
  fecha      VARCHAR(50) WITHOUT TIME ZONE NOT NULL DEFAULT now(),
  factura_id BIGINT,
  monto      BIGINT,
  CONSTRAINT venta_pkey PRIMARY KEY (id),
  CONSTRAINT cliente_fkey FOREIGN KEY (cliente_id)
  REFERENCES cliente (id)
    MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
  CONSTRAINT factura_fkey FOREIGN KEY (factura_id)
  REFERENCES factura (id)
    MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE RESTRICT
);

CREATE TABLE venta_detalle (
  id          BIGINT DEFAULT nextval('seq_venta_detalle'),
  venta_id    BIGINT,
  producto_id BIGINT,
  cantidad    BIGINT CHECK (cantidad > 0),
  CONSTRAINT venta_detalle_pkey PRIMARY KEY (id),
  CONSTRAINT venta_fkey FOREIGN KEY (venta_id)
  REFERENCES venta (id)
    MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
  CONSTRAINT producto_fkey FOREIGN KEY (producto_id)
  REFERENCES producto (id)
    MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE RESTRICT
);


CREATE TABLE compra (
  id           BIGINT DEFAULT nextval('seq_compra'),
  proveedor_id BIGINT,
  fecha        VARCHAR(50) WITHOUT TIME ZONE NOT NULL DEFAULT now(),
  monto        BIGINT,
  CONSTRAINT compra_pkey PRIMARY KEY (id),
  CONSTRAINT proveedor_fkey FOREIGN KEY (proveedor_id)
  REFERENCES proveedorId (id)
    MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE RESTRICT
);

CREATE TABLE compra_detalle (
  id          BIGINT DEFAULT nextval('seq_compra_detalle'),
  compra_id   BIGINT,
  producto_id BIGINT,
  cantidad    BIGINT CHECK (cantidad > 0),
  CONSTRAINT compra_detalle_pkey PRIMARY KEY (id),
  CONSTRAINT compra_fkey FOREIGN KEY (compra_id)
  REFERENCES compra (id)
    MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
  CONSTRAINT producto_fkey FOREIGN KEY (producto_id)
  REFERENCES producto (id)
    MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE RESTRICT
);

CREATE TABLE solicitud_compra (
  id          BIGINT  DEFAULT nextval('seq_compra_detalle'),
  fecha       VARCHAR(50) WITHOUT TIME ZONE NOT NULL DEFAULT now(),
  producto_id BIGINT,
  atendido    BOOLEAN DEFAULT FALSE,
  CONSTRAINT solicitud_compra_pkey PRIMARY KEY (id),
  CONSTRAINT producto_fkey FOREIGN KEY (producto_id)
  REFERENCES producto (id)
    MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE RESTRICT
);