package py.com.proceco.server.repository

import org.springframework.data.mongodb.repository.MongoRepository
import py.com.proceco.server.model.SitioArchivo

/**
 *
 */
interface SitioRepository : MongoRepository<SitioArchivo, String> {

    /**
     *
     */
    fun findByCodigoSitio(codigoSitio: String): SitioArchivo
}