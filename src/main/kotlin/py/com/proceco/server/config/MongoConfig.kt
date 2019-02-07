package py.com.proceco.server.config

import com.mongodb.MongoClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDbFactory
import org.springframework.data.mongodb.config.AbstractMongoConfiguration
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories


@Configuration
@EnableMongoRepositories(basePackages = arrayOf("py.com.proceco.server.repository"))
open class MongoConfig : AbstractMongoConfiguration() {
    /**
     *
     */
    override fun mongoClient(): MongoClient {
        return MongoClient("127.0.0.1", 27017)
    }

    /**
     *
     */
    override fun getDatabaseName(): String {
        return "server_files"
    }

    override fun getMappingBasePackages(): MutableCollection<String> {
        return arrayListOf("py.com.proceco.server.model")
    }

    /**
     *
     */
    @Bean
    @Throws(Exception::class)
    open fun gridFsTemplate(): GridFsTemplate {
        return GridFsTemplate(mongoDbFactory(), mappingMongoConverter())
    }
}