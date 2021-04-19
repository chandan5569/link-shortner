package shortner.io

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import shortner.io.plugins.*

class Application {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            embeddedServer(Netty, port = Datastore["port"]!!.toInt()) {
                configureRouting()
                configureMonitoring()
            }.start(wait = true)
        }
    }
}

