package top.ashman.wx.interfaces.controller;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.ashman.wx.application.AuthenticationService;
import top.ashman.wx.application.MessageService;
import top.ashman.wx.config.property.WxProperties;
import top.ashman.wx.infrastructure.util.WxConsts;
import top.ashman.wx.interfaces.AuthenticationQuery;
import top.ashman.wx.interfaces.WxRequestProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.concurrent.ExecutorService;

/**
 * @author singoasher
 * @date 2018/1/30
 */
@RestController
@RequestMapping("/wx")
public class WxController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WxController.class);

    private AuthenticationService authenticationService;
    private MessageService messageService;
    private ExecutorService executorService;
    private WxProperties wxProperties;

    @Autowired
    public WxController(AuthenticationService authenticationService,
                        MessageService messageService,
                        ExecutorService executorService,
                        WxProperties wxProperties) {
        this.authenticationService = authenticationService;
        this.messageService = messageService;
        this.executorService = executorService;
        this.wxProperties = wxProperties;
    }

    @GetMapping
    public ResponseEntity<String> authorization(AuthenticationQuery authenticationQuery) {
        LOGGER.info("AuthenticationQuery: {}", authenticationQuery.toString());
        return ResponseEntity.ok(authenticationService.authenticate(authenticationQuery));
    }

    @PostMapping
    public ResponseEntity<String> post(@RequestBody String xmlString)
            throws IOException, DocumentException, ParseException {
        LOGGER.info("Xml String: {}", xmlString);
        Document document = DocumentHelper.parseText(xmlString);
        return ResponseEntity.ok(WxRequestProcessor
                .response(wxProperties.getServer().getMode(), document, messageService, executorService));
    }
}
