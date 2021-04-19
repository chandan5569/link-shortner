package shortner.io.plugins

import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.util.*
import io.ktor.velocity.*
import org.apache.velocity.runtime.RuntimeConstants
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader
import shortner.io.Datastore
import java.util.*



fun getShortCode(url:String){

}
fun Application.configureRouting() {
    install(Locations) {
    }
    install(ContentNegotiation) {
        gson()
    }
    install(Velocity){
        setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath")
        setProperty("classpath.resource.loader.class", ClasspathResourceLoader::class.java.name)
    }


    val rootUrl = Datastore["rootUrl"]

    routing {
        post<Generate> {
            val urlLink = call.receive<LinkRequest>().link
            val longCode = Base64.getEncoder().encodeToString(urlLink.toByteArray())
            if (Datastore.allKeys.contains(longCode)) {
                val shortCode =
                    Datastore[longCode] ?: throw IllegalArgumentException("this should happen")
                return@post call.respond(HttpStatusCode.OK, Data("$rootUrl/$shortCode"))
            } else {
                val shortCode = System.currentTimeMillis().toString(35)
                Datastore[longCode] = shortCode
                Datastore[shortCode] = longCode
                return@post call.respond(HttpStatusCode.OK, Data("$rootUrl/$shortCode"))
            }

        }

        get<LoadUrl>{

            if (it.code.isNullOrBlank())
            {
                return@get call.respond("Hello there!!")

            }
            if (Datastore.allKeys.contains(it.code)){
                val longcode=Datastore[it.code!!]
                val urlLink = String(Base64.getDecoder().decode(longcode))

              return@get  call.respondRedirect(urlLink)
            }
          return@get  call.respond(HttpStatusCode.BadRequest,"Bad Url")
        }
        get("/")
        {
            return@get call.respond(VelocityContent("templates/index.vl",mapOf()))
        }

        post("/form_generate"){
            val parameters = call.receive<Parameters>()
            val urlLink = parameters["link"]?: return@post call.respondRedirect("/")
            val longCode = Base64.getEncoder().encodeToString(urlLink.toByteArray())
            if (Datastore.allKeys.contains(longCode)) {
                val shortCode =
                    Datastore[longCode] ?: throw IllegalArgumentException("this should happen")
            return@post call.respond(VelocityContent("templates/shortened.vl",mapOf("link" to "$rootUrl/$shortCode","root" to rootUrl.toString())))
            } else {
                val shortCode = System.currentTimeMillis().toString(35)
                Datastore[longCode] = shortCode
                Datastore[shortCode] = longCode
                return@post call.respond(VelocityContent("templates/shortened.vl",mapOf("link" to "$rootUrl/$shortCode","root" to rootUrl.toString())))
            }
        }
    }
}

data class Data(val shortUrl: String)
data class LinkRequest(val link: String)


@Location("/generate")
class Generate

@Location("/{code}")
data class LoadUrl(val code:String?)



