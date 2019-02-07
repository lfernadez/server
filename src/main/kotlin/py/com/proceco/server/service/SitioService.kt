package py.com.proceco.server.service

import com.mongodb.BasicDBObject
import org.apache.commons.io.IOUtils
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.MongoDbFactory
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.gridfs.GridFsResource
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import py.com.proceco.server.model.SitioArchivo
import py.com.proceco.server.model.SitioProperties
import py.com.proceco.server.repository.SitioRepository
import com.mongodb.client.gridfs.GridFSBuckets
import com.mongodb.client.MongoDatabase
import com.mongodb.client.gridfs.GridFSBucket
import py.com.proceco.server.config.MongoConfig


@Service
open class SitioService {

    @Autowired
    lateinit var gridFsTemplate: GridFsTemplate

    @Autowired
    lateinit var sitioRepository: SitioRepository

    @Autowired
    lateinit var mongoConfig: MongoConfig

    fun obtenerSitios(): MutableList<SitioArchivo> {
        return sitioRepository.findAll()
    }

    fun obtenerArchivosSitio(codigoSitio: String): GridFsResource {
        val sitioArchivo = sitioRepository.findByCodigoSitio(codigoSitio)
        val gridFsFile = gridFsTemplate.findOne(
                Query(Criteria.where("_id").`is`(sitioArchivo.archivos[1]))
        )
        return GridFsResource(gridFsFile, getGridFs().openDownloadStream(gridFsFile.getObjectId()))
    }

    fun guardarArchivosSitio(sitioProperties: SitioProperties, archivos: Array<MultipartFile>) {
        val sitioArchivo = SitioArchivo(sitioProperties.codigoSitio, sitioProperties.nombreSitio)
        val nuevosArchivos = procesarArchivos(archivos, sitioArchivo)
        val array = arrayOfNulls<ObjectId>(nuevosArchivos.size)
        sitioArchivo.archivos = nuevosArchivos.toArray(array)
        sitioRepository.save(sitioArchivo)
    }

    private fun procesarArchivos(archivos: Array<MultipartFile>, sitioArchivo: SitioArchivo): ArrayList<ObjectId> {
        var arrayListFile = ArrayList<ObjectId>()
        arrayListFile.addAll(sitioArchivo.archivos)
        for (multiparFile: MultipartFile in archivos) {
            arrayListFile.add(enlazarArchivo(multiparFile))
        }
        return arrayListFile;
    }

    private fun enlazarArchivo(archivoMultipartFile: MultipartFile): ObjectId {
        val inputStreamFile = archivoMultipartFile.inputStream
        val fileMetadata = BasicDBObject()
        fileMetadata.put("fileName", archivoMultipartFile.originalFilename)
        fileMetadata.put("type", archivoMultipartFile.contentType)
        return gridFsTemplate.store(inputStreamFile, archivoMultipartFile.originalFilename, fileMetadata)
    }

    private fun getGridFs(): GridFSBucket {
        val db = mongoConfig.mongoDbFactory().db
        return GridFSBuckets.create(db)
    }

}