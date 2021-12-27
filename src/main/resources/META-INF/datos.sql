update permiso set tag_menu='#{LoginManager.doCambiarContrasenhaForm}' where id_permiso=2
update permiso set tag_menu='#{LoginManager.doLogout}' where id_permiso=5

INSERT INTO configuracion (id_configuracion, titulo_sistema, sub_titulo_sistema) VALUES (1, 'SISTEMA DE SEGUIMIENTO DE DOCUMENTOS Y REPOSITORIO DE ARCHIVOS', 'Institución')

ALTER TABLE public.usuario ADD secure_password varchar(255) NULL
