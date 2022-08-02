/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import sedra.modelo.Rol;
import sedra.modelo.Usuario;
//import senado.gov.py.entity.Usuario;

/**
 * Conjunto de utilidades necesarias para el app-web
 *
 * @author jmferreira
 */
public class JSFutil implements Serializable {

    ResourceBundle bundle = ResourceBundle.getBundle("propiedades.bundle", JSFutil.getmyLocale());
    public static final String folderRoot = "/upload/";
    public static String folderDocumento = folderRoot;
    public static String folderTramite = folderRoot + "tramite/";

    private Integer tiempoDespacho = 15;

    public static enum StatusMessage {
        INFORMATION, WARNING, ERROR, FATAL
    }

    public Integer getTiempoDespacho() {
        return tiempoDespacho;
    }

    public void setTiempoDespacho(Integer tiempoDespacho) {
        this.tiempoDespacho = tiempoDespacho;
    }

    /**
     * Estructurar un SelectItems a partir de un conjunto de registros
     *
     * @param entities
     * @param selectOne
     * @return
     */
    public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem(null, "------ Opciones ------");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }

    public static void addMessage(String msg, StatusMessage estado) {
        FacesMessage facesMsg;
        switch (estado) {
            case INFORMATION:
                facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", msg);
                break;
            case ERROR:
                facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg);
                break;
            case FATAL:
                facesMsg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal", msg);
                break;
            case WARNING:
                facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Información", msg);
                break;
            default:
                facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", msg);
        }
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    /**
     * Recuperar un parametro de la sesión del usuario
     *
     * @param key
     * @return
     */
    public static String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    /**
     * getter local del usuario
     *
     * @return
     */
    public static Locale getmyLocale() {
        FacesContext miContexto = FacesContext.getCurrentInstance();
        Locale miLocal = miContexto.getViewRoot().getLocale();
        return miLocal;
    }

    /**
     * Cambiar la zona horaria del usuario
     *
     * @param date
     * @param zone
     * @return
     */
    public static Date changeTimeZone(Date date, TimeZone zone) {
        Calendar first = Calendar.getInstance(zone);
        first.setTimeInMillis(date.getTime());

        Calendar output = Calendar.getInstance();
        output.set(Calendar.YEAR, first.get(Calendar.YEAR));
        output.set(Calendar.MONTH, first.get(Calendar.MONTH));
        output.set(Calendar.DAY_OF_MONTH, first.get(Calendar.DAY_OF_MONTH));
        output.set(Calendar.HOUR_OF_DAY, first.get(Calendar.HOUR_OF_DAY));
        output.set(Calendar.MINUTE, first.get(Calendar.MINUTE));
        output.set(Calendar.SECOND, first.get(Calendar.SECOND));
        output.set(Calendar.MILLISECOND, first.get(Calendar.MILLISECOND));

        return output.getTime();
    }

    /**
     * Calcula la diferencia de tiempo entre dos fechas-horas
     *
     * @param finicio
     * @param hinicio
     * @param ffin
     * @param hfin
     * @return
     */
    public static String diferenciaTime(Date finicio, Date hinicio, Date ffin, Date hfin) {

        Date fechaMayor = null;
        Date fechaMenor = null;
        /* Verificamos cual es la mayor de las dos fechas, para no tener sorpresas al momento
         * de realizar la resta.
         */
        try {
            Calendar first = Calendar.getInstance();
            first.setTimeInMillis(finicio.getTime() + hinicio.getTime());
            Date fecha1 = first.getTime();
            first.setTimeInMillis(ffin.getTime() + hfin.getTime());
            Date fecha2 = first.getTime();

            if (fecha1.compareTo(fecha2) > 0) {
                fechaMayor = fecha1;
                fechaMenor = fecha2;
            } else {
                fechaMayor = fecha2;
                fechaMenor = fecha1;
            }

            //los milisegundos
            long diferenciaMils = fechaMayor.getTime() - fechaMenor.getTime();

            //obtenemos los segundos
            long segundos = diferenciaMils / 1000;
            //obtenemos los días (24*3600)
            long dias = segundos / 86400;
            //restamos las días para continuar con horas
            segundos -= dias * 86400;
            //obtenemos las horas
            long horas = segundos / 3600;
            //restamos las horas para continuar con minutos
            segundos -= horas * 3600;
            //igual que el paso anterior
            long minutos = segundos / 60;
            segundos -= minutos * 60;
            //retornamos
            return dias + " dias " + horas + " hs " + minutos + " min.";
        } catch (Exception e) {
            return "No estimado";
        }

    }

    /**
     * getter TimeZone del usuario
     *
     * @return La zona horaria de Paraguay
     */
    public static TimeZone getMyTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Asuncion"));
        return TimeZone.getDefault();
    }

    /**
     * getter FechaHoraActual
     *
     * @return La FechaHora actual en formato DATE
     */
    public static Date getFechaHoraActual() {
        Calendar c = Calendar.getInstance();
        return c.getTime();
    }

    /**
     * getter Calendar
     *
     * @return Una instancia del calendario
     */
    public static Calendar getCalendar() {
        Calendar c = Calendar.getInstance();
        return c;
    }

    /**
     * setter una variable en la sesión del usuario
     *
     * @param variable
     * @param valor
     */
    public static void putSessionVariable(String variable, Object valor) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put(variable, valor);
    }

    /**
     * getter una variable en la sesión del usuario
     *
     * @param variable
     * @return La variable Objeto de la sesion
     */
    public static Object takeSessionVariable(String variable) {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        return session.getAttribute(variable);
    }

    /**
     * Prepara la cadena de texto para realizar una consulta SQL reemplazando
     * caracteres especiales por %
     *
     * @param input
     * @return El texto sin caracteres especiales
     */
    public static String limpiarTexto(String input) {
        // Cadena de caracteres original a sustituir.
        String original = "àáâãäçèéêëìíîïñòóôõöùúûüýÿÀÁÂÃÄÇÈÉÊËÌÍÎÏÑÒÓÔÕÖÙÚÛÜÝ`^´çÇ";
        // Cadena de caracteres ASCII que reemplazarán los originales.
        //String ascii = "aaaeeeiiiooouunAAAEEEIIIOOOUUUNcC";
        String output = input;
        for (int i = 0; i < original.length(); i++) {
            // Reemplazamos los caracteres especiales.
            output = output.replace(original.charAt(i), '%');
        }//for i
        return output;
    }

    /**
     * Extraer solo las palabras que se consideran KeyWord de una cadena de
     * caracteres
     *
     * @param input
     * @return Array de String considerados KeyWord
     */
    public static String[] extraerKeyword(String input) {
        String[] output;
        String[] noKeyword = {" a ", " al ", " ante ", " antes ", " asi ", " aunque ", " bajo ", " bien ", " cabe ", " como ", " con ", " contra ", " cuando ", " de ", " del ", " desde ", " despues ", " durante ", " e ", " el ", " empero ", " en ", " entre ", " esta ", " hacia ", " hasta ", " la ", " las ", " los ", " luego ", " mas ", " mediante ", " muy ", " ni ", " o ", " ora ", " para ", " pero ", " por ", " porque ", " pues ", " que ", " se ", " sea ", " segun ", " si ", " sin ", " sino ", " siquiera ", " sobre ", " tal ", " toda ", " tras ", " u ", " un ", " una ", " uno ", " unos ", " y ", " ya "};
        String temp = input;
        for (String x : noKeyword) {
            temp = temp.replaceAll(x, " ");
        }
        temp = quitaEspacios(temp);
        output = temp.split(" ");
        return output;
    }

    /**
     * Sacar todos los blancos de una cadena de texto
     *
     * @param texto
     * @return La cadena sin blancos
     */
    public static String quitaEspacios(String texto) {
        java.util.StringTokenizer tokens = new java.util.StringTokenizer(texto);
        StringBuilder buff = new StringBuilder();
        while (tokens.hasMoreTokens()) {
            buff.append(" ").append(tokens.nextToken());
        }
        return buff.toString().trim();
    }

    /**
     * getter FechaHoraActual en formato largo
     *
     * @return La FechaHora en formato largo
     */
    public static String getFechaHoraActualLargo() {
        return getFechaHoraLargo(getFechaHoraActual());
    }

    /**
     * getter FechaHora en formato largo
     *
     * @param fechaActual
     * @return La fecha en formato largo
     */
    public static String getFechaHoraLargo(Date fechaActual) {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("'Hoy es' EEEEEEEEE, dd 'de' MMMMM 'de' yyyy");
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
        formatoFecha.setTimeZone(TimeZone.getTimeZone("America/Asuncion"));
        formatoHora.setTimeZone(TimeZone.getTimeZone("America/Asuncion"));
        return formatoFecha.format(fechaActual) + " " + formatoHora.format(fechaActual);
    }

    /**
     * getter FechaHora en formato corto
     *
     * @param fechaActual
     * @return La fecha en formato corto
     */
    public static String getFechaHoraCorto(Date fechaActual) {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        formatoFecha.setTimeZone(TimeZone.getTimeZone("America/Asuncion"));
        return formatoFecha.format(fechaActual);
    }

    public static Usuario getUsuarioConectado() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        Usuario user = (Usuario) session.getAttribute("user");
        return user;
    }

    public static String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * Grabar un archivo a disco
     *
     * @param is2
     * @param nombreArchivo
     * @return Un indicador de si el archivo ha sido escrito en el disco
     */
    public static Integer fileToDisk(InputStream is2, String nombreArchivo) {
        checkFolders();
        File file;
        InputStream is = is2;
        file = new File(nombreArchivo);
        //is = new ByteArrayInputStream(d.getArchivo());
        try {
            OutputStream out = new FileOutputStream(file);
            byte buf[] = new byte[1024];
            int len;
            while ((len = is.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            is.close();
            out.close();
            return 0;
        } catch (IOException ex) {
            Logger.getLogger(JSFutil.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(JSFutil.class.getName()).log(Level.SEVERE, null, ex);
                return -1;
            }
        }
    }

    private static void checkFolders() {
        try {
            Files.createDirectories(Paths.get(folderDocumento));
            Files.createDirectories(Paths.get(folderTramite));
        } catch (IOException ex) {
            System.out.println("The directory already exist");
        }
    }

    public static Date convertirFecha(String date) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return df.parse(date);
        } catch (ParseException ex) {
        }
        return null;
    }

    /**
     * Generar un password usando Secure password hash using bcrypt and scrypt
     * algorithms
     * http://howtodoinjava.com/2013/07/22/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
     *
     * @param cadena
     * @return
     */
    public static String getSecurePassword(String cadena) {
        /**
         * <dependency>
         * <groupId>org.mindrot</groupId>
         * <artifactId>jbcrypt</artifactId>
         * <version>0.3m</version>
         * </dependency>
         *
         */
        String generatedSecuredPasswordHash = BCrypt.hashpw(cadena, BCrypt.gensalt(12));
        return generatedSecuredPasswordHash;
    }

    public static boolean checkSecurePassword(String original, String generado) {
        boolean matched = BCrypt.checkpw(original, generado);
        return matched;
    }

    public static enum PersistAction {

        CREATE,
        DELETE,
        UPDATE
    }

    public static String getMes(int month) {
        String[] monthNames = {"ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SETIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
        return monthNames[month];
    }

    /**
     * Verifica si el IP inicia con un rango de intranet
     *
     * @param ip
     * @return
     */
    public static Boolean esValidoIPIntranet(String ip) {
        if (!ip.startsWith("0:0:0:0:0:0:0:") && !ip.startsWith("96.") && !ip.startsWith("192.168.")) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    public static String getFileDescription(String nombreArchivo) {
        int i = nombreArchivo.lastIndexOf('.');
        if (i > 0) {
            return nombreArchivo.substring(0, i).toUpperCase();
        } else {
            return nombreArchivo;
        }
    }

    public static String sanitizeFilename(String inputName) {
        return inputName.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
    }

    public static Boolean deleteFileFromDisk(String nombreArchivo) {
        File file;
        file = new File(nombreArchivo);
        //is = new ByteArrayInputStream(d.getArchivo());
        try {
            return file.delete();
        } catch (Exception ex) {
            Logger.getLogger(JSFutil.class.getName()).log(Level.SEVERE, null, ex);
            return Boolean.FALSE;
        }
    }

    public static List<?> arrayToList(Object[] list) {
        List<Object> miLista = Arrays.asList(list);
        return miLista;
    }

    public static String getAbsoluteApplicationUrl() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            URL url = new URL(request.getRequestURL().toString());
            URL newUrl = new URL(url.getProtocol(),
                    url.getHost(),
                    url.getPort(),
                    request.getContextPath());
            return newUrl.toString();
        } catch (MalformedURLException ex) {
            Logger.getLogger(JSFutil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static Rol getRolSesion() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        Rol rol = (Rol) session.getAttribute("rol");
        return rol;
    }

    public static Boolean strmatch(String str, String pattern) {
        int n = str.length();
        int m = pattern.length();

        // empty pattern can only match with
        // empty string
        if (m == 0) {
            return (n == 0);
        }

        // lookup table for storing results of
        // subproblems
        boolean[][] lookup = new boolean[n + 1][m + 1];

        // initialize lookup table to false
        for (int i = 0; i < n + 1; i++) {
            Arrays.fill(lookup[i], false);
        }

        // empty pattern can match with empty string
        lookup[0][0] = true;

        // Only '*' can match with empty string
        for (int j = 1; j <= m; j++) {
            if (pattern.charAt(j - 1) == '*') {
                lookup[0][j] = lookup[0][j - 1];
            }
        }

        // fill the table in bottom-up fashion
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                // Two cases if we see a '*'
                // a) We ignore '*'' character and move
                //    to next  character in the pattern,
                //     i.e., '*' indicates an empty
                //     sequence.
                // b) '*' character matches with ith
                //     character in input
                if (pattern.charAt(j - 1) == '*') {
                    lookup[i][j] = lookup[i][j - 1]
                            || lookup[i - 1][j];
                } // Current characters are considered as
                // matching in two cases
                // (a) current character of pattern is '?'
                // (b) characters actually match
                else if (pattern.charAt(j - 1) == '?'
                        || str.charAt(i - 1)
                        == pattern.charAt(j - 1)) {
                    lookup[i][j] = lookup[i - 1][j - 1];
                } // If characters don't match
                else {
                    lookup[i][j] = false;
                }
            }
        }

        return lookup[n][m];
    }

    public static String replaceWildcardRegex(String texto) {
        String cadena = texto.replaceAll("%", "*");
        cadena = cadena.replaceAll("_", "?");
        return cadena;
    }
}
