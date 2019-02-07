package py.com.proceco.server.controller

import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import py.com.proceco.server.model.SitioArchivo
import py.com.proceco.server.model.SitioProperties
import py.com.proceco.server.service.SitioService

/**
 *
 */
@RestController
open class FileUploadController {

    @Autowired
    lateinit var sitioService: SitioService

    @RequestMapping(
            value = ["/server/upload"],
            method = [RequestMethod.POST],
            consumes = ["multipart/form-data"]
    )
    fun processUpload(
            @RequestParam("datosSitio") datos: String,
            @RequestParam("archivos") archivos: Array<MultipartFile>
    ): ResponseEntity<MutableList<SitioArchivo>> {
        val gson = Gson()
        val sitioProperties = gson.fromJson<SitioProperties>(datos, SitioProperties::class.java)
        sitioService.guardarArchivosSitio(sitioProperties, archivos)
        return ResponseEntity(sitioService.obtenerSitios(), HttpStatus.OK)
    }
}