package internship

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import internship.plugins.*
import io.ktor.application.*
import java.io.File

/*fun main() {
    embeddedServer(Netty, port = 8080, host = "") {
        configureRouting()
        configureMonitoring()
        configureSerialization()
        configCORS()
    }.start(wait = true)
}*/


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")// Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module() {
    configureRouting()
    configureMonitoring()
    configureSession()
    configureSerialization()
    configCORS()

}


