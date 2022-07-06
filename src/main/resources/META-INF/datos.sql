update permiso set tag_menu='#{LoginManager.doCambiarContrasenhaForm}' where id_permiso=2
update permiso set tag_menu='#{LoginManager.doLogout}' where id_permiso=5

INSERT INTO configuracion (id_configuracion, titulo_sistema, sub_titulo_sistema) VALUES (1, 'SISTEMA DE SEGUIMIENTO DE DOCUMENTOS Y REPOSITORIO DE ARCHIVOS', 'Institución')

ALTER TABLE public.usuario ADD secure_password varchar(255) NULL

ALTER TABLE public.documento ADD numero_expediente int4 NULL;
ALTER TABLE public.configuracion ADD tiempo_alerta integer DEFAULT 60;


ALTER TABLE public.estado_tramitacion ADD insignia varchar(255) NULL;
ALTER TABLE public.estado_tramitacion ADD info_estado varchar(255) NULL;
ALTER TABLE public.estado_tramitacion ADD orden integer NULL;

INSERT INTO public.estado_tramitacion (id_estado, descripcion_estado, insignia) VALUES(5, 'Ingresado', 'rojo');
INSERT INTO public.estado_tramitacion (id_estado, descripcion_estado, insignia) VALUES(4, 'Derivado', 'lila');

ALTER TABLE public.estado_tramitacion ALTER COLUMN info_estado TYPE varchar(500);
UPDATE public.estado_tramitacion SET orden=3, descripcion_estado='Pendiente', insignia='amarillo',info_estado='Un trámite o mensaje recibido en los pendientes de una dependencia (actualmente seleccionada) y que no ha sido confirmado aún.' WHERE id_estado=1;
UPDATE public.estado_tramitacion SET orden=6, descripcion_estado='Archivado', insignia='azul',info_estado='Un trámite o mensaje que ha terminado en una dependencia (actualmente seleccionada) debido a que ha sido archivado con/sin nota.' WHERE id_estado=100;
UPDATE public.estado_tramitacion SET orden=5,descripcion_estado='Rechazado', insignia='naranja',info_estado='Un trámite o mensaje que ha sido rechazado en una dependencia (actualmente seleccionada).' WHERE id_estado=2;
UPDATE public.estado_tramitacion SET orden=4,descripcion_estado='Confirmado', insignia='verde', info_estado='Un trámite o mensaje recibido en los pendientes de una dependencia (actualmente seleccionada) y que ya ha sido confirmado.' WHERE id_estado=3;
UPDATE public.estado_tramitacion SET orden=2,info_estado='Un trámite o mensaje que ha sido derivado a otras dependencias desde una dependencia (actualmente seleccionada).' WHERE id_estado=4;
UPDATE public.estado_tramitacion SET orden=1,info_estado='Un trámite o mensaje que ha sido ingresado en los pendientes de una dependencia (actualmente seleccionada) y que espera su proceso (derivación). El alta de un documento, automáticamente genera un trámite de INGRESADO en los pendientes de la dependencia y espera su proceso.' WHERE id_estado=5;


ALTER TABLE public.tramitacion ADD id_tramitacion_padre integer NULL;
ALTER TABLE public.tramitacion ADD CONSTRAINT fk_tramitacion_id_padre FOREIGN KEY (id_tramitacion_padre) REFERENCES public.tramitacion(id_tramitacion);

ALTER TABLE public.tramitacion ADD leido boolean null;
update tramitacion set leido=false where leido is null and id_estado =1;
update tramitacion set leido=true where leido is null and id_estado >1;

ALTER TABLE public.tramitacion RENAME COLUMN id_usuario TO id_creador;

ALTER TABLE public.tramitacion ADD id_prioridad integer NULL;
ALTER TABLE public.tramitacion ADD CONSTRAINT fk_tramitacion_id_prioridad FOREIGN KEY (id_prioridad) REFERENCES public.prioridad(id_prioridad);

INSERT INTO public.prioridad (id_prioridad, descripcion_prioridad, insignia, orden) VALUES(1, 'Alta', 'rojo', 1);
INSERT INTO public.prioridad (id_prioridad, descripcion_prioridad, insignia, orden) VALUES(2, 'Normal', 'gris', 2);

update public.tramitacion set id_prioridad =2 where id_prioridad is null;

ALTER TABLE public.permiso ADD nivel_acceso varchar(255) NULL;
ALTER TABLE public.permiso ADD con_separador boolean NULL;
INSERT INTO public.permiso (id_permiso, descripcion_permiso, nivel, orden, tag_menu, url_imagen) VALUES(21, 'Registro Autoḿatico de Expedientes', '2.4', NULL, '#{DocumentoController.doCrearRegistroAutomatico()}', 'fa fa-file-contract');

UPDATE public.permiso SET descripcion_permiso='Archivo', nivel='1', orden=NULL, tag_menu=NULL, url_imagen='fa fa-folder-open', nivel_acceso='general', con_separador=NULL WHERE id_permiso=1;
UPDATE public.permiso SET descripcion_permiso='Cambiar Contraseña', nivel='1.1', orden=NULL, tag_menu='#{LoginManager.doCambiarContrasenhaForm}', url_imagen='fa fa-key', nivel_acceso='usuario', con_separador=NULL WHERE id_permiso=2;
UPDATE public.permiso SET descripcion_permiso='Usuarios', nivel='1.2', orden=NULL, tag_menu='#{UsuarioController.listUsuarioSetup}', url_imagen='fa fa-users', nivel_acceso='admin', con_separador=NULL WHERE id_permiso=3;
UPDATE public.permiso SET descripcion_permiso='Roles', nivel='1.3', orden=NULL, tag_menu='#{RolController.listRolSetup}', url_imagen='fa fa-user-circle', nivel_acceso='admin', con_separador=NULL WHERE id_permiso=4;
UPDATE public.permiso SET descripcion_permiso='Salir', nivel='1.4', orden=NULL, tag_menu='#{LoginManager.doLogout}', url_imagen='fa fa-sign-out', nivel_acceso='general', con_separador=NULL WHERE id_permiso=5;
UPDATE public.permiso SET descripcion_permiso='Administración', nivel='2', orden=NULL, tag_menu=NULL, url_imagen='fa fa-tools', nivel_acceso='general', con_separador=NULL WHERE id_permiso=6;
UPDATE public.permiso SET descripcion_permiso='Documentos', nivel='2.1', orden=NULL, tag_menu='#{DocumentoController.listDocumentoSetup}', url_imagen='fa fa-file', nivel_acceso='usuario', con_separador=NULL WHERE id_permiso=7;
UPDATE public.permiso SET descripcion_permiso='Plan de Archivo', nivel='2.2', orden=NULL, tag_menu='#{ClasificadorController.listPlanArchivoSetup}', url_imagen='fa fa-folder-plus', nivel_acceso='admin', con_separador=NULL WHERE id_permiso=10;
UPDATE public.permiso SET descripcion_permiso='Agregador Doc.', nivel='2.3', orden=NULL, tag_menu='#{DocumentoController.crearDocumentoFromClasificadorSetup}', url_imagen='fa fa-file-medical', nivel_acceso='usuario', con_separador=NULL WHERE id_permiso=12;
UPDATE public.permiso SET descripcion_permiso='Registro Autoḿatico de Expedientes', nivel='2.4', orden=NULL, tag_menu='#{DocumentoController.doCrearRegistroAutomatico()}', url_imagen='fa fa-file-contract', nivel_acceso='usuario', con_separador=NULL WHERE id_permiso=21;
UPDATE public.permiso SET descripcion_permiso='Tramitación', nivel='3', orden=NULL, tag_menu=NULL, url_imagen='fa fa-share-square', nivel_acceso='general', con_separador=NULL WHERE id_permiso=8;
UPDATE public.permiso SET descripcion_permiso='Pendientes', nivel='3.1', orden=NULL, tag_menu='#{TramitacionController.listPendientesSetup}', url_imagen='fa fa-list-ul', nivel_acceso='usuario', con_separador=NULL WHERE id_permiso=9;
UPDATE public.permiso SET descripcion_permiso='Archivar Doc.', nivel='3.2', orden=NULL, tag_menu='#{DocumentoController.listAdjuntaDocumentoSetup}', url_imagen='fa fa-file-archive', nivel_acceso='admin', con_separador=NULL WHERE id_permiso=11;
UPDATE public.permiso SET descripcion_permiso='Nota de Salida/STR', nivel='3.4', orden=NULL, tag_menu='#{NotaSalidaController.listNotaSalidaSetup}', url_imagen='fa fa-sign', nivel_acceso='admin', con_separador=NULL WHERE id_permiso=20;
UPDATE public.permiso SET descripcion_permiso='Seguimiento Doc.', nivel='3.5', orden=NULL, tag_menu='#{TramitacionController.listSeguimientoSetup}', url_imagen='fa fa-file-contract', nivel_acceso='admin', con_separador=true WHERE id_permiso=15;
UPDATE public.permiso SET descripcion_permiso='Informes', nivel='4', orden=NULL, tag_menu=NULL, url_imagen='fa fa-list-alt', nivel_acceso='general', con_separador=NULL WHERE id_permiso=13;
UPDATE public.permiso SET descripcion_permiso='Delantal de Documento', nivel='4.1', orden=NULL, tag_menu='#{ReporteController.imprimirDelantalSetup}', url_imagen='fa fa-scroll', nivel_acceso='admin', con_separador=NULL WHERE id_permiso=14;
UPDATE public.permiso SET descripcion_permiso='Documentos en Dependencias', nivel='4.2', orden=NULL, tag_menu='#{ReporteController.listTramitacionOficinaSetup}', url_imagen='fa fa-building', nivel_acceso='usuario', con_separador=NULL WHERE id_permiso=16;
UPDATE public.permiso SET descripcion_permiso='Localizar Doc.', nivel='4.3', orden=NULL, tag_menu='#{DocumentoController.listLocalizarDocumentoSetup}', url_imagen='fa fa-search', nivel_acceso='admin', con_separador=NULL WHERE id_permiso=19;
UPDATE public.permiso SET descripcion_permiso='Operaciones Especiales', nivel='5', orden=NULL, tag_menu=NULL, url_imagen='fa fa-user-tag', nivel_acceso='general', con_separador=NULL WHERE id_permiso=17;
UPDATE public.permiso SET descripcion_permiso='Desbloquear Doc.', nivel='5.1', orden=NULL, tag_menu='#{TramitacionController.listDesbloqueoSetup}', url_imagen='fa fa-unlock', nivel_acceso='admin', con_separador=NULL WHERE id_permiso=18;

ALTER TABLE public.nota_salida ADD asunto varchar(255) NULL;
ALTER TABLE public.nota_salida ADD fecha_nota date NULL;
