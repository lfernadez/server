package py.com.proceco.server.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class SitioArchivo(
        @Id
        val codigoSitio: String,
        val nombreSitio: String) {
    var archivos: Array<ObjectId> = emptyArray()
}