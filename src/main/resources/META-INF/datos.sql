update permiso set tag_menu='#{LoginManager.doCambiarContrasenhaForm}' where id_permiso=2
update permiso set tag_menu='#{LoginManager.doLogout}' where id_permiso=5

INSERT INTO configuracion (id_configuracion, titulo_sistema, sub_titulo_sistema) VALUES (1, 'SISTEMA DE SEGUIMIENTO DE DOCUMENTOS Y REPOSITORIO DE ARCHIVOS', 'Institución')

ALTER TABLE public.usuario ADD secure_password varchar(255) NULL

UPDATE public.permiso SET descripcion_permiso='Archivo', nivel='1', orden=NULL, tag_menu=NULL, url_imagen='fa fa-folder-open' WHERE id_permiso=1;
UPDATE public.permiso SET descripcion_permiso='Usuarios', nivel='1.2', orden=NULL, tag_menu='#{UsuarioController.listUsuarioSetup}', url_imagen='fa fa-users' WHERE id_permiso=3;
UPDATE public.permiso SET descripcion_permiso='Roles', nivel='1.3', orden=NULL, tag_menu='#{RolController.listRolSetup}', url_imagen='fa fa-user-circle' WHERE id_permiso=4;
UPDATE public.permiso SET descripcion_permiso='Documentos', nivel='2.1', orden=NULL, tag_menu='#{DocumentoController.listDocumentoSetup}', url_imagen='fa fa-file' WHERE id_permiso=7;
UPDATE public.permiso SET descripcion_permiso='Pendientes', nivel='3.1', orden=NULL, tag_menu='#{TramitacionController.listPendientesSetup}', url_imagen='fa fa-list-ul' WHERE id_permiso=9;
UPDATE public.permiso SET descripcion_permiso='Administración', nivel='2', orden=NULL, tag_menu=NULL, url_imagen='fa fa-tools' WHERE id_permiso=6;
UPDATE public.permiso SET descripcion_permiso='Tramitación', nivel='3', orden=NULL, tag_menu=NULL, url_imagen='fa fa-share-square' WHERE id_permiso=8;
UPDATE public.permiso SET descripcion_permiso='Plan de Archivo', nivel='2.2', orden=NULL, tag_menu='#{ClasificadorController.listPlanArchivoSetup}', url_imagen='fa fa-folder-plus' WHERE id_permiso=10;
UPDATE public.permiso SET descripcion_permiso='Delantal de Documento', nivel='4.1', orden=NULL, tag_menu='#{ReporteController.imprimirDelantalSetup}', url_imagen='fa fa-scroll' WHERE id_permiso=14;
UPDATE public.permiso SET descripcion_permiso='Informes', nivel='4', orden=NULL, tag_menu=NULL, url_imagen='fa fa-list-alt' WHERE id_permiso=13;
UPDATE public.permiso SET descripcion_permiso='Seguimiento Doc.', nivel='3.4', orden=NULL, tag_menu='#{TramitacionController.listSeguimientoSetup}', url_imagen='fa fa-file-contract' WHERE id_permiso=15;
UPDATE public.permiso SET descripcion_permiso='Agregador Doc.', nivel='2.3', orden=NULL, tag_menu='#{DocumentoController.crearDocumentoFromClasificadorSetup}', url_imagen='fa fa-file-medical' WHERE id_permiso=12;
UPDATE public.permiso SET descripcion_permiso='Documentos en Dependencias', nivel='4.2', orden=NULL, tag_menu='#{ReporteController.listTramitacionOficinaSetup}', url_imagen='fa fa-building' WHERE id_permiso=16;
UPDATE public.permiso SET descripcion_permiso='Archivar Doc.', nivel='3.2', orden=NULL, tag_menu='#{DocumentoController.listAdjuntaDocumentoSetup}', url_imagen='fa fa-file-archive' WHERE id_permiso=11;
UPDATE public.permiso SET descripcion_permiso='Operaciones Especiales', nivel='5', orden=NULL, tag_menu=NULL, url_imagen='fa fa-user-tag' WHERE id_permiso=17;
UPDATE public.permiso SET descripcion_permiso='Desbloquear Doc.', nivel='5.1', orden=NULL, tag_menu='#{TramitacionController.listDesbloqueoSetup}', url_imagen='fa fa-unlock' WHERE id_permiso=18;
UPDATE public.permiso SET descripcion_permiso='Localizar Doc.', nivel='4.3', orden=NULL, tag_menu='#{DocumentoController.listLocalizarDocumentoSetup}', url_imagen='fa fa-search' WHERE id_permiso=19;
UPDATE public.permiso SET descripcion_permiso='Nota de Salida/STR', nivel='3.5', orden=NULL, tag_menu='#{NotaSalidaController.listNotaSalidaSetup}', url_imagen='fa fa-sign' WHERE id_permiso=20;
UPDATE public.permiso SET descripcion_permiso='Cambiar Contraseña', nivel='1.1', orden=NULL, tag_menu='#{LoginManager.doCambiarContrasenhaForm}', url_imagen='fa fa-key' WHERE id_permiso=2;
UPDATE public.permiso SET descripcion_permiso='Salir', nivel='1.4', orden=NULL, tag_menu='#{LoginManager.doLogout}', url_imagen='fa fa-sign-out' WHERE id_permiso=5;
INSERT INTO public.permiso (id_permiso, descripcion_permiso, nivel, orden, tag_menu, url_imagen) VALUES(21, 'Registro Autoḿatico de Expedientes', '2.4', NULL, '#{DocumentoController.doCrearRegistroAutomatico()}', 'fa fa-file-contract');

ALTER TABLE public.documento ADD numero_expediente int4 NULL;
ALTER TABLE public.configuracion ADD tiempo_alerta integer DEFAULT 60;


ALTER TABLE public.estado_tramitacion ADD insignia varchar(255) NULL;

UPDATE public.estado_tramitacion SET descripcion_estado='Pendiente', insignia='amarillo' WHERE id_estado=1;
UPDATE public.estado_tramitacion SET descripcion_estado='Terminado', insignia='azul' WHERE id_estado=100;
UPDATE public.estado_tramitacion SET descripcion_estado='Rechazado', insignia='naranja' WHERE id_estado=2;
UPDATE public.estado_tramitacion SET descripcion_estado='Recbido', insignia='verde' WHERE id_estado=3;
UPDATE public.estado_tramitacion SET descripcion_estado='Derivado', insignia='lila' WHERE id_estado=4;
INSERT INTO public.estado_tramitacion (id_estado, descripcion_estado, insignia) VALUES(5, 'Ingresado', 'rojo');
UPDATE public.estado_tramitacion SET descripcion_estado='Ingresado', insignia='rojo' WHERE id_estado=5;

ALTER TABLE public.tramitacion ADD id_tramitacion_padre integer NULL;
ALTER TABLE public.tramitacion ADD CONSTRAINT fk_tramitacion_id_padre FOREIGN KEY (id_tramitacion_padre) REFERENCES public.tramitacion(id_tramitacion);

ALTER TABLE public.tramitacion ADD leido boolean null;
update tramitacion set leido=false where leido is null and id_estado =1;
update tramitacion set leido=true where leido is null and id_estado >1;

ALTER TABLE public.tramitacion RENAME COLUMN id_usuario TO id_creador;
