package top.ashman.wx.interfaces.controller;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.ashman.wx.application.service.AuthenticationService;
import top.ashman.wx.application.service.MessageService;
import top.ashman.wx.config.property.WxProperties;
import top.ashman.wx.interfaces.AuthenticationQuery;
import top.ashman.wx.interfaces.XmlMessageAssembler;

import java.io.IOException;
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

        // 认证成功，将 echostr 返回，供微信后台认证 token，否则返回 null
        boolean isAuthenticated = authenticationService.authenticate(
                authenticationQuery.getSignature(),
                authenticationQuery.getTimestamp(),
                authenticationQuery.getNonce());
        return ResponseEntity.ok(isAuthenticated ? authenticationQuery.getEchostr() : null);
    }

    @PostMapping
    public ResponseEntity<String> post(AuthenticationQuery authenticationQuery, @RequestBody String xmlString)
            throws IOException, DocumentException, ParseException {
        LOGGER.info("AuthenticationQuery: {}", authenticationQuery.toString());
        LOGGER.info("Xml String: {}", xmlString);

        boolean isAuthenticated = authenticationService.authenticate(
                authenticationQuery.getSignature(),
                authenticationQuery.getTimestamp(),
                authenticationQuery.getNonce());
        return ResponseEntity.ok(isAuthenticated ?
                messageService.handle(XmlMessageAssembler.assemble(xmlString)) : null);
    }
}
