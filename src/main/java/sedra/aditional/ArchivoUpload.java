/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedra.aditional;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Data;
import org.primefaces.model.file.UploadedFile;
import sedra.util.JSFutil;

/**
 *
 * @author jmferreira
 */
@Data
public class ArchivoUpload {

    private Integer idArchivo;
    private String fileName;
    private BigInteger fileSize;
    private String mimeType;
    private byte[] data;
    private String folder;
    private Date timestamp;
    private String tipoAdjunto;
    private String checksum;

    public ArchivoUpload(UploadedFile up, String folder) {
        this.idArchivo = null;
        this.fileName = JSFutil.sanitizeFilename(up.getFileName());
        this.mimeType = up.getContentType();
        this.data = up.getContent();
        this.fileSize = BigInteger.valueOf(up.getSize());
        this.timestamp = new Date();
        this.folder = folder;
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(data);
            this.checksum = new BigInteger(1, hash).toString(16);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ArchivoUpload.class.getName()).log(Level.SEVERE, null, ex);
            this.checksum = "none";
        }

    }

    public ArchivoUpload(Integer id, String fileName, String mimeType, byte[] data, BigInteger fileSize, String folder) {
        this.idArchivo = id;
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.data = data;
        this.fileSize = fileSize;
        this.timestamp = new Date();
        this.folder = folder;
    }

    public String toNameFileSystem() {
        return this.getFolder() + this.getIdArchivo() + "-" + this.getFileName();
    }

}
