package py.com.proceco.server.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.gridfs.GridFsResource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import py.com.proceco.server.service.SitioService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus


@RestController
open class DownloadController {
    @Autowired
    lateinit var sitioService: SitioService

    @RequestMapping(
            value = ["/server/download"],
            method = [RequestMethod.GET]
    )
    @ResponseBody
    fun processUpload(
            @RequestParam("codigoSitio") codigoSitio: String
    ): ResponseEntity<GridFsResource> {
        val gridFsResource = sitioService.obtenerArchivosSitio(codigoSitio)
        val responseHeaders = HttpHeaders()
        responseHeaders.add("content-disposition", "attachment; filename=" + gridFsResource.filename)
        return ResponseEntity(gridFsResource, responseHeaders, HttpStatus.OK)
    }

}