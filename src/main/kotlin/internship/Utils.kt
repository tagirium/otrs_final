package internship

import internship.plugins.UserSession
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import java.io.File
import java.io.InputStream


fun File.copyInputStreamToFile(inputstream: InputStream) {
    this.outputStream().use { fileOut ->
        inputstream.copyTo(fileOut)
    }
}

fun getTicketsByIds(session: UserSession, ids: List<String>): List<TicketResponse> {
    val str1 = "<?xml version='1.0' encoding='UTF-8'?>" +
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
            "xmlns:tic=\"http://www.otrs.org/TicketConnector/\">" +
            "<soapenv:Body>\n" +
            "   <tic:TicketGet>\n" +
            "       <SessionID>${session.sessionId}</SessionID>\n" +
            "       <TicketID>${ids.toString().replace("[", "").replace("]", "")}</TicketID>\n" +
            "       <Extended>1</Extended>\n" +
            "       <AllArticles>1</AllArticles>\n" +
            "       <ArticleLimit>10</ArticleLimit>\n" +
            "   </tic:TicketGet>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>"

    val doc = Jsoup
        .connect("http://10.90.138.10/otrs/nph-genericinterface.pl/Webservice/webservice_soap")
        .method(Connection.Method.POST)
        .header("SOAPAction", "http://www.otrs.org/TicketConnector/TicketGet")
        .data("ContentType", "text/xml; charset=\"utf-8\"")
        .data("Accept", "text/xml")
        .requestBody(str1)
        .execute()

    val xmlbody = Jsoup.parse(doc.body(), "", Parser.xmlParser())

    val tickets = xmlbody.select("Ticket").map { ticket ->
        TicketResponse(
            age = ticket.selectFirst("Age").text(),
            archiveFlag = ticket.selectFirst("ArchiveFlag").text(),
            articles = ticket.select("Article").map {
                ArticleResponse(
                    articleId = it.selectFirst("ArticleID")?.text(),
                    articleNumber = it.selectFirst("ArticleNumber")?.text(),
                    bcc = it.selectFirst("Bcc")?.text(),
                    body = it.selectFirst("Body")?.text(),
                    cc = it.selectFirst("Cc")?.text(),
                    changeBy = it.selectFirst("ChangeBy")?.text(),
                    changeTime = it.selectFirst("ChangeTime")?.text(),
                    createBy = it.selectFirst("CreateBy")?.text(),
                    createTime = it.selectFirst("CreateTime")?.text(),
                    from = it.selectFirst("From")?.text(),
                    incomingTime = it.selectFirst("IncomingTime")?.text(),
                    messageId = it.selectFirst("MessageID")?.text(),
                    subject = it.selectFirst("Subject")?.text(),
                    ticketId = it.selectFirst("TicketID")?.text(),
                    to = it.selectFirst("To")?.text()
                )
            },
            changeBy = ticket.selectFirst("ChangeBy")?.text(),
            changed = ticket.selectFirst("Changed")?.text(),
            createBy = ticket.selectFirst("CreateBy")?.text(),
            created = ticket.selectFirst("Created")?.text(),
            customerId = ticket.selectFirst("CustomerID")?.text(),
            customerUserId = ticket.selectFirst("CustomerUserID")?.text(),
            firstLock = ticket.selectFirst("FirstLock")?.text(),
            firstResponse = ticket.selectFirst("FirstResponse")?.text(),
            groupId = ticket.selectFirst("GroupID")?.text(),
            lock = ticket.selectFirst("Lock")?.text(),
            lockId = ticket.selectFirst("LockedID")?.text(),
            owner = ticket.selectFirst("Owner")?.text(),
            ownerId = ticket.selectFirst("OwnerID")?.text(),
            responsible = ticket.selectFirst("Responsible")?.text(),
            responsibleId = ticket.selectFirst("ResponsibleID")?.text(),
            state = ticket.selectFirst("State")?.text(),
            stateId = ticket.selectFirst("StateID")?.text(),
            stateType = ticket.selectFirst("StateType")?.text(),
            ticketId = ticket.selectFirst("TicketID")?.text(),
            ticketNumber = ticket.selectFirst("TicketNumber")?.text(),
            title = ticket.selectFirst("Title")?.text(),
            type = ticket.selectFirst("Type")?.text(),
            typeId = ticket.selectFirst("TypeID")?.text()
        )
    }
    return tickets
}

fun getTicket(session: UserSession, id: String): TicketResponse {
    val str = "<?xml version='1.0' encoding='UTF-8'?>" +
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
            "xmlns:tic=\"http://www.otrs.org/TicketConnector/\">" +
            "<soapenv:Body>\n" +
            "   <tic:TicketGet>\n" +
            "       <SessionID>${session.sessionId}</SessionID>\n" +
            "       <TicketID>$id</TicketID>\n" +
            "       <Extended>1</Extended>\n" +
            "       <AllArticles>1</AllArticles>\n" +
            "       <ArticleLimit>10</ArticleLimit>\n" +
            "   </tic:TicketGet>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>"

    val req = Jsoup
        .connect("http://10.90.138.10/otrs/nph-genericinterface.pl/Webservice/webservice_soap")
        .method(Connection.Method.POST)
        .header("SOAPAction", "http://www.otrs.org/TicketConnector/TicketGet")
        .data("ContentType", "text/xml; charset=\"utf-8\"")
        .data("Accept", "text/xml")
        .requestBody(str)
        .execute()

    val xmlbody = Jsoup.parse(req.body(), "", Parser.xmlParser())
    val ticket = xmlbody.select("Ticket").first()
    val ticketResponse = TicketResponse(
        age = ticket.selectFirst("Age").text(),
        archiveFlag = ticket.selectFirst("ArchiveFlag").text(),
        articles = ticket.select("Article").map {
            ArticleResponse(
                articleId = it.selectFirst("ArticleID")?.text(),
                articleNumber = it.selectFirst("ArticleNumber")?.text(),
                bcc = it.selectFirst("Bcc")?.text(),
                body = it.selectFirst("Body")?.text(),
                cc = it.selectFirst("Cc")?.text(),
                changeBy = it.selectFirst("ChangeBy")?.text(),
                changeTime = it.selectFirst("ChangeTime")?.text(),
                createBy = it.selectFirst("CreateBy")?.text(),
                createTime = it.selectFirst("CreateTime")?.text(),
                from = it.selectFirst("From")?.text(),
                incomingTime = it.selectFirst("IncomingTime")?.text(),
                messageId = it.selectFirst("MessageID")?.text(),
                subject = it.selectFirst("Subject")?.text(),
                ticketId = it.selectFirst("TicketID")?.text(),
                to = it.selectFirst("To")?.text()
            )
        },
        changeBy = ticket.selectFirst("ChangeBy")?.text(),
        changed = ticket.selectFirst("Changed")?.text(),
        createBy = ticket.selectFirst("CreateBy")?.text(),
        created = ticket.selectFirst("Created")?.text(),
        customerId = ticket.selectFirst("CustomerID")?.text(),
        customerUserId = ticket.selectFirst("CustomerUserID")?.text(),
        firstLock = ticket.selectFirst("FirstLock")?.text(),
        firstResponse = ticket.selectFirst("FirstResponse")?.text(),
        groupId = ticket.selectFirst("GroupID")?.text(),
        lock = ticket.selectFirst("Lock")?.text(),
        lockId = ticket.selectFirst("LockedID")?.text(),
        owner = ticket.selectFirst("Owner")?.text(),
        ownerId = ticket.selectFirst("OwnerID")?.text(),
        responsible = ticket.selectFirst("Responsible")?.text(),
        responsibleId = ticket.selectFirst("ResponsibleID")?.text(),
        state = ticket.selectFirst("State")?.text(),
        stateId = ticket.selectFirst("StateID")?.text(),
        stateType = ticket.selectFirst("StateType")?.text(),
        ticketId = ticket.selectFirst("TicketID")?.text(),
        ticketNumber = ticket.selectFirst("TicketNumber")?.text(),
        title = ticket.selectFirst("Title")?.text(),
        type = ticket.selectFirst("Type")?.text(),
        typeId = ticket.selectFirst("TypeID")?.text()
    )
    return ticketResponse
}

fun getTicketIds(session: UserSession): List<String> {
    val str = "<?xml version='1.0' encoding='UTF-8'?>" +
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
            "xmlns:tic=\"http://www.otrs.org/TicketConnector/\">" +
            "<soapenv:Body>\n" +
            "   <tic:TicketSearch>\n" +
            "       <SessionID>${session.sessionId}</SessionID>\n" +
            "       <Result>ARRAY</Result>\n" +
            "   </tic:TicketSearch>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>"

    val doc = Jsoup
        .connect("http://10.90.138.10/otrs/nph-genericinterface.pl/Webservice/webservice_soap")
        .method(Connection.Method.POST)
        .header("SOAPAction", "http://www.otrs.org/TicketConnector/TicketSearch")
        .data("ContentType", "text/xml; charset=\"utf-8\"")
        .data("Accept", "text/xml")
        .requestBody(str)
        .execute()

    val els = Jsoup.parse(doc.body(), "", Parser.xmlParser())
    val ids = els.select("TicketID")

    val list = ids.map { it.text() }
    return list
}

fun formTicketCreate(ticket: Ticket, sessionId: String): CreatedTicketResp {
    /*val dynaFields = StringBuilder()
    if (ticket.dynamicFields != null)
        ticket.dynamicFields.forEach { field ->
            dynaFields
                .append("<DynamicField>\n")
                .append("   <Name>${field?.name}</Name>\n")
                .append("   <Value>${field?.value}</Value>\n")
                .append("</DynamicField>\n")
        }*/
    val str = "<?xml version='1.0' encoding='UTF-8'?>" +
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
            "xmlns:tic=\"http://www.otrs.org/TicketConnector/\">" +
            "<soapenv:Body>\n" +
            "      <tic:TicketCreate>\n" +
            "      <SessionID>${sessionId}</SessionID>\n" +
            "      <Ticket>\n" +
            "                <Title>${ticket.title}</Title>\n" +
            "                <CustomerUser>${ticket.customerUser}</CustomerUser>\n" +
            "                <QueueID>1</QueueID>\n" +
            "                <StateID>4</StateID>\n" +
            "                <PriorityID>3</PriorityID>\n" +
            "         </Ticket>\n" +
            "         <Article>\n" +
            "             <Subject>${ticket.article.subject}</Subject>\n" +
            "             <Body>${ticket.article.body}</Body>\n" +
            "             <ContentType>${ticket.article.contentType}</ContentType>\n" +
            "         </Article>\n" +
           // "${if (ticket.dynamicFields != null) dynaFields.toString() else ""}" +
            "      </tic:TicketCreate>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>"

    println("soap create Ticket: $str")
    var doc: Connection.Response? = null
    try {
        doc = Jsoup
            .connect("http://10.90.138.10/otrs/nph-genericinterface.pl/Webservice/webservice_soap")
            .method(Connection.Method.POST)
            .header("SOAPAction", "http://www.otrs.org/TicketConnector/TicketCreate")
            .data("ContentType", "text/xml; charset=\"utf-8\"")
            .data("Accept", "text/xml")
            .requestBody(str)
            .execute()
    } catch (e: Error){
        println(e.message)
        e.printStackTrace()
    }

    println("response: ${doc?.body()}")

    val soap = Jsoup.parse(doc?.body(), "", Parser.xmlParser())
    val artId = soap.select("ArticleID").text()
    val ticketId = soap.select("TicketID").text()
    val ticketNum = soap.select("TicketNumber").text()
    val created = CreatedTicketResp(artId, ticketId, ticketNum)
    return created
}


fun sessionCreate(username: String, password: String): SessionResponse {
    val str = "<?xml version='1.0' encoding='UTF-8'?>" +
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
            "xmlns:tic=\"http://www.otrs.org/TicketConnector/\">" +
            "<soapenv:Body>\n" +
            "   <tic:SessionCreate>\n" +
            "       <CustomerUserLogin>$username</CustomerUserLogin>\n" +
            "       <Password>$password</Password>\n" +
            "   </tic:SessionCreate>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>"
    val resp = Jsoup
        .connect("http://10.90.138.10/otrs/nph-genericinterface.pl/Webservice/webservice_soap")
        .method(Connection.Method.POST)
        .header("SOAPAction", "http://www.otrs.org/TicketConnector/SessionCreate")
        .data("ContentType", "text/xml; charset=\"utf-8\"")
        .data("Accept", "text/xml")
        .requestBody(str)
        .execute()

    val doc = Jsoup.parse(resp.body(), "", Parser.xmlParser())
    //println("createSess: ${doc.html()}")
    val msg = doc.select("SessionCreateResponse")
    if (msg.html().contains("Error"))
        return SessionResponse(
            isOk = false,
            errorMsg = msg.select("ErrorMessage").text()
        )
    else
        return SessionResponse(
            sessionID = msg.select("SessionID").text()
        )
}

