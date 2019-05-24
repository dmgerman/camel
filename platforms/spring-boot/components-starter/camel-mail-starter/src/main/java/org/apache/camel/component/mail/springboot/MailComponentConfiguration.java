begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mail.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mail
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mail
operator|.
name|AttachmentsContentTransferEncodingResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mail
operator|.
name|JavaMailSender
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|jsse
operator|.
name|SSLContextParameters
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * To send or receive emails using imap/pop3 or smtp protocols.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.mail"
argument_list|)
DECL|class|MailComponentConfiguration
specifier|public
class|class
name|MailComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the mail component. This is      * enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * Sets the Mail configuration      */
DECL|field|configuration
specifier|private
name|MailConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * Resolver to determine Content-Type for file attachments. The option is a      * org.apache.camel.component.mail.ContentTypeResolver type.      */
DECL|field|contentTypeResolver
specifier|private
name|String
name|contentTypeResolver
decl_stmt|;
comment|/**      * Enable usage of global SSL context parameters.      */
DECL|field|useGlobalSslContextParameters
specifier|private
name|Boolean
name|useGlobalSslContextParameters
init|=
literal|false
decl_stmt|;
comment|/**      * To use a custom org.apache.camel.spi.HeaderFilterStrategy to filter      * header to and from Camel message. The option is a      * org.apache.camel.spi.HeaderFilterStrategy type.      */
DECL|field|headerFilterStrategy
specifier|private
name|String
name|headerFilterStrategy
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
comment|/**      * Whether the component should use basic property binding (Camel 2.x) or      * the newer property binding with additional capabilities      */
DECL|field|basicPropertyBinding
specifier|private
name|Boolean
name|basicPropertyBinding
init|=
literal|false
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|MailConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( MailConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|MailConfigurationNestedConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getContentTypeResolver ()
specifier|public
name|String
name|getContentTypeResolver
parameter_list|()
block|{
return|return
name|contentTypeResolver
return|;
block|}
DECL|method|setContentTypeResolver (String contentTypeResolver)
specifier|public
name|void
name|setContentTypeResolver
parameter_list|(
name|String
name|contentTypeResolver
parameter_list|)
block|{
name|this
operator|.
name|contentTypeResolver
operator|=
name|contentTypeResolver
expr_stmt|;
block|}
DECL|method|getUseGlobalSslContextParameters ()
specifier|public
name|Boolean
name|getUseGlobalSslContextParameters
parameter_list|()
block|{
return|return
name|useGlobalSslContextParameters
return|;
block|}
DECL|method|setUseGlobalSslContextParameters ( Boolean useGlobalSslContextParameters)
specifier|public
name|void
name|setUseGlobalSslContextParameters
parameter_list|(
name|Boolean
name|useGlobalSslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|useGlobalSslContextParameters
operator|=
name|useGlobalSslContextParameters
expr_stmt|;
block|}
DECL|method|getHeaderFilterStrategy ()
specifier|public
name|String
name|getHeaderFilterStrategy
parameter_list|()
block|{
return|return
name|headerFilterStrategy
return|;
block|}
DECL|method|setHeaderFilterStrategy (String headerFilterStrategy)
specifier|public
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|String
name|headerFilterStrategy
parameter_list|)
block|{
name|this
operator|.
name|headerFilterStrategy
operator|=
name|headerFilterStrategy
expr_stmt|;
block|}
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
DECL|method|getBasicPropertyBinding ()
specifier|public
name|Boolean
name|getBasicPropertyBinding
parameter_list|()
block|{
return|return
name|basicPropertyBinding
return|;
block|}
DECL|method|setBasicPropertyBinding (Boolean basicPropertyBinding)
specifier|public
name|void
name|setBasicPropertyBinding
parameter_list|(
name|Boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|this
operator|.
name|basicPropertyBinding
operator|=
name|basicPropertyBinding
expr_stmt|;
block|}
DECL|class|MailConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|MailConfigurationNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mail
operator|.
name|MailConfiguration
operator|.
name|class
decl_stmt|;
comment|/**          * To use a custom {@link          * org.apache.camel.component.mail.JavaMailSender} for sending emails.          */
DECL|field|javaMailSender
specifier|private
name|JavaMailSender
name|javaMailSender
decl_stmt|;
comment|/**          * The mail server host name          */
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
comment|/**          * Sets the java mail options. Will clear any default properties and          * only use the properties provided for this method.          */
DECL|field|javaMailProperties
specifier|private
name|Properties
name|javaMailProperties
decl_stmt|;
comment|/**          * Sets additional java mail properties, that will append/override any          * default properties that is set based on all the other options. This          * is useful if you need to add some special options but want to keep          * the others as is.          */
DECL|field|additionalJavaMailProperties
specifier|private
name|Properties
name|additionalJavaMailProperties
decl_stmt|;
comment|/**          * The password for login          */
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
comment|/**          * The Subject of the message being sent. Note: Setting the subject in          * the header takes precedence over this option.          */
DECL|field|subject
specifier|private
name|String
name|subject
decl_stmt|;
comment|/**          * The port number of the mail server          */
DECL|field|port
specifier|private
name|Integer
name|port
decl_stmt|;
comment|/**          * The protocol for communicating with the mail server          */
DECL|field|protocol
specifier|private
name|String
name|protocol
decl_stmt|;
comment|/**          * Specifies the mail session that camel should use for all mail          * interactions. Useful in scenarios where mail sessions are created and          * managed by some other resource, such as a JavaEE container. If this          * is not specified, Camel automatically creates the mail session for          * you.          */
DECL|field|session
specifier|private
name|Session
name|session
decl_stmt|;
comment|/**          * The username for login          */
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
comment|/**          * The from email address          */
DECL|field|from
specifier|private
name|String
name|from
init|=
literal|"camel@localhost"
decl_stmt|;
comment|/**          * Deletes the messages after they have been processed. This is done by          * setting the DELETED flag on the mail message. If false, the SEEN flag          * is set instead. As of Camel 2.10 you can override this configuration          * option by setting a header with the key delete to determine if the          * mail should be deleted or not.          */
DECL|field|delete
specifier|private
name|Boolean
name|delete
init|=
literal|false
decl_stmt|;
comment|/**          * Specifies whether Camel should map the received mail message to Camel          * body/headers. If set to true, the body of the mail message is mapped          * to the body of the Camel IN message and the mail headers are mapped          * to IN headers. If this option is set to false then the IN message          * contains a raw javax.mail.Message. You can retrieve this raw message          * by calling exchange.getIn().getBody(javax.mail.Message.class).          */
DECL|field|mapMailMessage
specifier|private
name|Boolean
name|mapMailMessage
init|=
literal|true
decl_stmt|;
comment|/**          * The folder to poll.          */
DECL|field|folderName
specifier|private
name|String
name|folderName
init|=
literal|"INBOX"
decl_stmt|;
comment|/**          * Option to let Camel ignore unsupported charset in the local JVM when          * sending mails. If the charset is unsupported then charset=XXX (where          * XXX represents the unsupported charset) is removed from the          * content-type and it relies on the platform default instead.          */
DECL|field|ignoreUriScheme
specifier|private
name|Boolean
name|ignoreUriScheme
init|=
literal|false
decl_stmt|;
comment|/**          * Whether to limit by unseen mails only.          */
DECL|field|unseen
specifier|private
name|Boolean
name|unseen
init|=
literal|true
decl_stmt|;
comment|/**          * Sets the To email address. Separate multiple email addresses with          * comma.          */
DECL|field|to
specifier|private
name|String
name|to
decl_stmt|;
comment|/**          * Sets the CC email address. Separate multiple email addresses with          * comma.          */
DECL|field|cc
specifier|private
name|String
name|cc
decl_stmt|;
comment|/**          * Sets the BCC email address. Separate multiple email addresses with          * comma.          */
DECL|field|bcc
specifier|private
name|String
name|bcc
decl_stmt|;
comment|/**          * The Reply-To recipients (the receivers of the response mail).          * Separate multiple email addresses with a comma.          */
DECL|field|replyTo
specifier|private
name|String
name|replyTo
decl_stmt|;
comment|/**          * Sets the maximum number of messages to consume during a poll. This          * can be used to avoid overloading a mail server, if a mailbox folder          * contains a lot of messages. Default value of -1 means no fetch size          * and all messages will be consumed. Setting the value to 0 is a          * special corner case, where Camel will not consume any messages at          * all.          */
DECL|field|fetchSize
specifier|private
name|Integer
name|fetchSize
init|=
operator|-
literal|1
decl_stmt|;
comment|/**          * Enable debug mode on the underlying mail framework. The SUN Mail          * framework logs the debug messages to System.out by default.          */
DECL|field|debugMode
specifier|private
name|Boolean
name|debugMode
init|=
literal|false
decl_stmt|;
comment|/**          * The connection timeout in milliseconds.          */
DECL|field|connectionTimeout
specifier|private
name|Integer
name|connectionTimeout
init|=
literal|30000
decl_stmt|;
comment|/**          * To use a dummy security setting for trusting all certificates. Should          * only be used for development mode, and not production.          */
DECL|field|dummyTrustManager
specifier|private
name|Boolean
name|dummyTrustManager
init|=
literal|false
decl_stmt|;
comment|/**          * The mail message content type. Use text/html for HTML mails.          */
DECL|field|contentType
specifier|private
name|String
name|contentType
init|=
literal|"text/plain"
decl_stmt|;
comment|/**          * Specifies the key to an IN message header that contains an          * alternative email body. For example, if you send emails in text/html          * format and want to provide an alternative mail body for non-HTML          * email clients, set the alternative mail body with this key as a          * header.          */
DECL|field|alternativeBodyHeader
specifier|private
name|String
name|alternativeBodyHeader
init|=
literal|"CamelMailAlternativeBody"
decl_stmt|;
comment|/**          * Whether to use disposition inline or attachment.          */
DECL|field|useInlineAttachments
specifier|private
name|Boolean
name|useInlineAttachments
init|=
literal|false
decl_stmt|;
comment|/**          * Option to let Camel ignore unsupported charset in the local JVM when          * sending mails. If the charset is unsupported then charset=XXX (where          * XXX represents the unsupported charset) is removed from the          * content-type and it relies on the platform default instead.          */
DECL|field|ignoreUnsupportedCharset
specifier|private
name|Boolean
name|ignoreUnsupportedCharset
init|=
literal|false
decl_stmt|;
comment|/**          * Whether the consumer should disconnect after polling. If enabled this          * forces Camel to connect on each poll.          */
DECL|field|disconnect
specifier|private
name|Boolean
name|disconnect
init|=
literal|false
decl_stmt|;
comment|/**          * Whether the consumer should close the folder after polling. Setting          * this option to false and having disconnect=false as well, then the          * consumer keep the folder open between polls.          */
DECL|field|closeFolder
specifier|private
name|Boolean
name|closeFolder
init|=
literal|true
decl_stmt|;
comment|/**          * To configure security using SSLContextParameters.          */
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
comment|/**          * After processing a mail message, it can be copied to a mail folder          * with the given name. You can override this configuration value, with          * a header with the key copyTo, allowing you to copy messages to folder          * names configured at runtime.          */
DECL|field|copyTo
specifier|private
name|String
name|copyTo
decl_stmt|;
comment|/**          * Will mark the javax.mail.Message as peeked before processing the mail          * message. This applies to IMAPMessage messages types only. By using          * peek the mail will not be eager marked as SEEN on the mail server,          * which allows us to rollback the mail message if there is an error          * processing in Camel.          */
DECL|field|peek
specifier|private
name|Boolean
name|peek
init|=
literal|true
decl_stmt|;
comment|/**          * If the mail consumer cannot retrieve a given mail message, then this          * option allows to skip the message and move on to retrieve the next          * mail message.<p/> The default behavior would be the consumer throws          * an exception and no mails from the batch would be able to be routed          * by Camel.          */
DECL|field|skipFailedMessage
specifier|private
name|Boolean
name|skipFailedMessage
init|=
literal|false
decl_stmt|;
comment|/**          * If the mail consumer cannot retrieve a given mail message, then this          * option allows to handle the caused exception by the consumer's error          * handler. By enable the bridge error handler on the consumer, then the          * Camel routing error handler can handle the exception instead.<p/>          * The default behavior would be the consumer throws an exception and no          * mails from the batch would be able to be routed by Camel.          */
DECL|field|handleFailedMessage
specifier|private
name|Boolean
name|handleFailedMessage
init|=
literal|false
decl_stmt|;
comment|/**          * To use a custom AttachmentsContentTransferEncodingResolver to resolve          * what content-type-encoding to use for attachments.          */
DECL|field|attachmentsContentTransferEncodingResolver
specifier|private
name|AttachmentsContentTransferEncodingResolver
name|attachmentsContentTransferEncodingResolver
decl_stmt|;
comment|/**          * This option enables transparent MIME decoding and unfolding for mail          * headers.          */
DECL|field|mimeDecodeHeaders
specifier|private
name|Boolean
name|mimeDecodeHeaders
init|=
literal|false
decl_stmt|;
DECL|method|getJavaMailSender ()
specifier|public
name|JavaMailSender
name|getJavaMailSender
parameter_list|()
block|{
return|return
name|javaMailSender
return|;
block|}
DECL|method|setJavaMailSender (JavaMailSender javaMailSender)
specifier|public
name|void
name|setJavaMailSender
parameter_list|(
name|JavaMailSender
name|javaMailSender
parameter_list|)
block|{
name|this
operator|.
name|javaMailSender
operator|=
name|javaMailSender
expr_stmt|;
block|}
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
DECL|method|getJavaMailProperties ()
specifier|public
name|Properties
name|getJavaMailProperties
parameter_list|()
block|{
return|return
name|javaMailProperties
return|;
block|}
DECL|method|setJavaMailProperties (Properties javaMailProperties)
specifier|public
name|void
name|setJavaMailProperties
parameter_list|(
name|Properties
name|javaMailProperties
parameter_list|)
block|{
name|this
operator|.
name|javaMailProperties
operator|=
name|javaMailProperties
expr_stmt|;
block|}
DECL|method|getAdditionalJavaMailProperties ()
specifier|public
name|Properties
name|getAdditionalJavaMailProperties
parameter_list|()
block|{
return|return
name|additionalJavaMailProperties
return|;
block|}
DECL|method|setAdditionalJavaMailProperties ( Properties additionalJavaMailProperties)
specifier|public
name|void
name|setAdditionalJavaMailProperties
parameter_list|(
name|Properties
name|additionalJavaMailProperties
parameter_list|)
block|{
name|this
operator|.
name|additionalJavaMailProperties
operator|=
name|additionalJavaMailProperties
expr_stmt|;
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
DECL|method|getSubject ()
specifier|public
name|String
name|getSubject
parameter_list|()
block|{
return|return
name|subject
return|;
block|}
DECL|method|setSubject (String subject)
specifier|public
name|void
name|setSubject
parameter_list|(
name|String
name|subject
parameter_list|)
block|{
name|this
operator|.
name|subject
operator|=
name|subject
expr_stmt|;
block|}
DECL|method|getPort ()
specifier|public
name|Integer
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|setPort (Integer port)
specifier|public
name|void
name|setPort
parameter_list|(
name|Integer
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
DECL|method|getProtocol ()
specifier|public
name|String
name|getProtocol
parameter_list|()
block|{
return|return
name|protocol
return|;
block|}
DECL|method|setProtocol (String protocol)
specifier|public
name|void
name|setProtocol
parameter_list|(
name|String
name|protocol
parameter_list|)
block|{
name|this
operator|.
name|protocol
operator|=
name|protocol
expr_stmt|;
block|}
DECL|method|getSession ()
specifier|public
name|Session
name|getSession
parameter_list|()
block|{
return|return
name|session
return|;
block|}
DECL|method|setSession (Session session)
specifier|public
name|void
name|setSession
parameter_list|(
name|Session
name|session
parameter_list|)
block|{
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
block|}
DECL|method|getUsername ()
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
DECL|method|setUsername (String username)
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
block|}
DECL|method|getFrom ()
specifier|public
name|String
name|getFrom
parameter_list|()
block|{
return|return
name|from
return|;
block|}
DECL|method|setFrom (String from)
specifier|public
name|void
name|setFrom
parameter_list|(
name|String
name|from
parameter_list|)
block|{
name|this
operator|.
name|from
operator|=
name|from
expr_stmt|;
block|}
DECL|method|getDelete ()
specifier|public
name|Boolean
name|getDelete
parameter_list|()
block|{
return|return
name|delete
return|;
block|}
DECL|method|setDelete (Boolean delete)
specifier|public
name|void
name|setDelete
parameter_list|(
name|Boolean
name|delete
parameter_list|)
block|{
name|this
operator|.
name|delete
operator|=
name|delete
expr_stmt|;
block|}
DECL|method|getMapMailMessage ()
specifier|public
name|Boolean
name|getMapMailMessage
parameter_list|()
block|{
return|return
name|mapMailMessage
return|;
block|}
DECL|method|setMapMailMessage (Boolean mapMailMessage)
specifier|public
name|void
name|setMapMailMessage
parameter_list|(
name|Boolean
name|mapMailMessage
parameter_list|)
block|{
name|this
operator|.
name|mapMailMessage
operator|=
name|mapMailMessage
expr_stmt|;
block|}
DECL|method|getFolderName ()
specifier|public
name|String
name|getFolderName
parameter_list|()
block|{
return|return
name|folderName
return|;
block|}
DECL|method|setFolderName (String folderName)
specifier|public
name|void
name|setFolderName
parameter_list|(
name|String
name|folderName
parameter_list|)
block|{
name|this
operator|.
name|folderName
operator|=
name|folderName
expr_stmt|;
block|}
DECL|method|getIgnoreUriScheme ()
specifier|public
name|Boolean
name|getIgnoreUriScheme
parameter_list|()
block|{
return|return
name|ignoreUriScheme
return|;
block|}
DECL|method|setIgnoreUriScheme (Boolean ignoreUriScheme)
specifier|public
name|void
name|setIgnoreUriScheme
parameter_list|(
name|Boolean
name|ignoreUriScheme
parameter_list|)
block|{
name|this
operator|.
name|ignoreUriScheme
operator|=
name|ignoreUriScheme
expr_stmt|;
block|}
DECL|method|getUnseen ()
specifier|public
name|Boolean
name|getUnseen
parameter_list|()
block|{
return|return
name|unseen
return|;
block|}
DECL|method|setUnseen (Boolean unseen)
specifier|public
name|void
name|setUnseen
parameter_list|(
name|Boolean
name|unseen
parameter_list|)
block|{
name|this
operator|.
name|unseen
operator|=
name|unseen
expr_stmt|;
block|}
DECL|method|getTo ()
specifier|public
name|String
name|getTo
parameter_list|()
block|{
return|return
name|to
return|;
block|}
DECL|method|setTo (String to)
specifier|public
name|void
name|setTo
parameter_list|(
name|String
name|to
parameter_list|)
block|{
name|this
operator|.
name|to
operator|=
name|to
expr_stmt|;
block|}
DECL|method|getCc ()
specifier|public
name|String
name|getCc
parameter_list|()
block|{
return|return
name|cc
return|;
block|}
DECL|method|setCc (String cc)
specifier|public
name|void
name|setCc
parameter_list|(
name|String
name|cc
parameter_list|)
block|{
name|this
operator|.
name|cc
operator|=
name|cc
expr_stmt|;
block|}
DECL|method|getBcc ()
specifier|public
name|String
name|getBcc
parameter_list|()
block|{
return|return
name|bcc
return|;
block|}
DECL|method|setBcc (String bcc)
specifier|public
name|void
name|setBcc
parameter_list|(
name|String
name|bcc
parameter_list|)
block|{
name|this
operator|.
name|bcc
operator|=
name|bcc
expr_stmt|;
block|}
DECL|method|getReplyTo ()
specifier|public
name|String
name|getReplyTo
parameter_list|()
block|{
return|return
name|replyTo
return|;
block|}
DECL|method|setReplyTo (String replyTo)
specifier|public
name|void
name|setReplyTo
parameter_list|(
name|String
name|replyTo
parameter_list|)
block|{
name|this
operator|.
name|replyTo
operator|=
name|replyTo
expr_stmt|;
block|}
DECL|method|getFetchSize ()
specifier|public
name|Integer
name|getFetchSize
parameter_list|()
block|{
return|return
name|fetchSize
return|;
block|}
DECL|method|setFetchSize (Integer fetchSize)
specifier|public
name|void
name|setFetchSize
parameter_list|(
name|Integer
name|fetchSize
parameter_list|)
block|{
name|this
operator|.
name|fetchSize
operator|=
name|fetchSize
expr_stmt|;
block|}
DECL|method|getDebugMode ()
specifier|public
name|Boolean
name|getDebugMode
parameter_list|()
block|{
return|return
name|debugMode
return|;
block|}
DECL|method|setDebugMode (Boolean debugMode)
specifier|public
name|void
name|setDebugMode
parameter_list|(
name|Boolean
name|debugMode
parameter_list|)
block|{
name|this
operator|.
name|debugMode
operator|=
name|debugMode
expr_stmt|;
block|}
DECL|method|getConnectionTimeout ()
specifier|public
name|Integer
name|getConnectionTimeout
parameter_list|()
block|{
return|return
name|connectionTimeout
return|;
block|}
DECL|method|setConnectionTimeout (Integer connectionTimeout)
specifier|public
name|void
name|setConnectionTimeout
parameter_list|(
name|Integer
name|connectionTimeout
parameter_list|)
block|{
name|this
operator|.
name|connectionTimeout
operator|=
name|connectionTimeout
expr_stmt|;
block|}
DECL|method|getDummyTrustManager ()
specifier|public
name|Boolean
name|getDummyTrustManager
parameter_list|()
block|{
return|return
name|dummyTrustManager
return|;
block|}
DECL|method|setDummyTrustManager (Boolean dummyTrustManager)
specifier|public
name|void
name|setDummyTrustManager
parameter_list|(
name|Boolean
name|dummyTrustManager
parameter_list|)
block|{
name|this
operator|.
name|dummyTrustManager
operator|=
name|dummyTrustManager
expr_stmt|;
block|}
DECL|method|getContentType ()
specifier|public
name|String
name|getContentType
parameter_list|()
block|{
return|return
name|contentType
return|;
block|}
DECL|method|setContentType (String contentType)
specifier|public
name|void
name|setContentType
parameter_list|(
name|String
name|contentType
parameter_list|)
block|{
name|this
operator|.
name|contentType
operator|=
name|contentType
expr_stmt|;
block|}
DECL|method|getAlternativeBodyHeader ()
specifier|public
name|String
name|getAlternativeBodyHeader
parameter_list|()
block|{
return|return
name|alternativeBodyHeader
return|;
block|}
DECL|method|setAlternativeBodyHeader (String alternativeBodyHeader)
specifier|public
name|void
name|setAlternativeBodyHeader
parameter_list|(
name|String
name|alternativeBodyHeader
parameter_list|)
block|{
name|this
operator|.
name|alternativeBodyHeader
operator|=
name|alternativeBodyHeader
expr_stmt|;
block|}
DECL|method|getUseInlineAttachments ()
specifier|public
name|Boolean
name|getUseInlineAttachments
parameter_list|()
block|{
return|return
name|useInlineAttachments
return|;
block|}
DECL|method|setUseInlineAttachments (Boolean useInlineAttachments)
specifier|public
name|void
name|setUseInlineAttachments
parameter_list|(
name|Boolean
name|useInlineAttachments
parameter_list|)
block|{
name|this
operator|.
name|useInlineAttachments
operator|=
name|useInlineAttachments
expr_stmt|;
block|}
DECL|method|getIgnoreUnsupportedCharset ()
specifier|public
name|Boolean
name|getIgnoreUnsupportedCharset
parameter_list|()
block|{
return|return
name|ignoreUnsupportedCharset
return|;
block|}
DECL|method|setIgnoreUnsupportedCharset (Boolean ignoreUnsupportedCharset)
specifier|public
name|void
name|setIgnoreUnsupportedCharset
parameter_list|(
name|Boolean
name|ignoreUnsupportedCharset
parameter_list|)
block|{
name|this
operator|.
name|ignoreUnsupportedCharset
operator|=
name|ignoreUnsupportedCharset
expr_stmt|;
block|}
DECL|method|getDisconnect ()
specifier|public
name|Boolean
name|getDisconnect
parameter_list|()
block|{
return|return
name|disconnect
return|;
block|}
DECL|method|setDisconnect (Boolean disconnect)
specifier|public
name|void
name|setDisconnect
parameter_list|(
name|Boolean
name|disconnect
parameter_list|)
block|{
name|this
operator|.
name|disconnect
operator|=
name|disconnect
expr_stmt|;
block|}
DECL|method|getCloseFolder ()
specifier|public
name|Boolean
name|getCloseFolder
parameter_list|()
block|{
return|return
name|closeFolder
return|;
block|}
DECL|method|setCloseFolder (Boolean closeFolder)
specifier|public
name|void
name|setCloseFolder
parameter_list|(
name|Boolean
name|closeFolder
parameter_list|)
block|{
name|this
operator|.
name|closeFolder
operator|=
name|closeFolder
expr_stmt|;
block|}
DECL|method|getSslContextParameters ()
specifier|public
name|SSLContextParameters
name|getSslContextParameters
parameter_list|()
block|{
return|return
name|sslContextParameters
return|;
block|}
DECL|method|setSslContextParameters ( SSLContextParameters sslContextParameters)
specifier|public
name|void
name|setSslContextParameters
parameter_list|(
name|SSLContextParameters
name|sslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|sslContextParameters
operator|=
name|sslContextParameters
expr_stmt|;
block|}
DECL|method|getCopyTo ()
specifier|public
name|String
name|getCopyTo
parameter_list|()
block|{
return|return
name|copyTo
return|;
block|}
DECL|method|setCopyTo (String copyTo)
specifier|public
name|void
name|setCopyTo
parameter_list|(
name|String
name|copyTo
parameter_list|)
block|{
name|this
operator|.
name|copyTo
operator|=
name|copyTo
expr_stmt|;
block|}
DECL|method|getPeek ()
specifier|public
name|Boolean
name|getPeek
parameter_list|()
block|{
return|return
name|peek
return|;
block|}
DECL|method|setPeek (Boolean peek)
specifier|public
name|void
name|setPeek
parameter_list|(
name|Boolean
name|peek
parameter_list|)
block|{
name|this
operator|.
name|peek
operator|=
name|peek
expr_stmt|;
block|}
DECL|method|getSkipFailedMessage ()
specifier|public
name|Boolean
name|getSkipFailedMessage
parameter_list|()
block|{
return|return
name|skipFailedMessage
return|;
block|}
DECL|method|setSkipFailedMessage (Boolean skipFailedMessage)
specifier|public
name|void
name|setSkipFailedMessage
parameter_list|(
name|Boolean
name|skipFailedMessage
parameter_list|)
block|{
name|this
operator|.
name|skipFailedMessage
operator|=
name|skipFailedMessage
expr_stmt|;
block|}
DECL|method|getHandleFailedMessage ()
specifier|public
name|Boolean
name|getHandleFailedMessage
parameter_list|()
block|{
return|return
name|handleFailedMessage
return|;
block|}
DECL|method|setHandleFailedMessage (Boolean handleFailedMessage)
specifier|public
name|void
name|setHandleFailedMessage
parameter_list|(
name|Boolean
name|handleFailedMessage
parameter_list|)
block|{
name|this
operator|.
name|handleFailedMessage
operator|=
name|handleFailedMessage
expr_stmt|;
block|}
DECL|method|getAttachmentsContentTransferEncodingResolver ()
specifier|public
name|AttachmentsContentTransferEncodingResolver
name|getAttachmentsContentTransferEncodingResolver
parameter_list|()
block|{
return|return
name|attachmentsContentTransferEncodingResolver
return|;
block|}
DECL|method|setAttachmentsContentTransferEncodingResolver ( AttachmentsContentTransferEncodingResolver attachmentsContentTransferEncodingResolver)
specifier|public
name|void
name|setAttachmentsContentTransferEncodingResolver
parameter_list|(
name|AttachmentsContentTransferEncodingResolver
name|attachmentsContentTransferEncodingResolver
parameter_list|)
block|{
name|this
operator|.
name|attachmentsContentTransferEncodingResolver
operator|=
name|attachmentsContentTransferEncodingResolver
expr_stmt|;
block|}
DECL|method|getMimeDecodeHeaders ()
specifier|public
name|Boolean
name|getMimeDecodeHeaders
parameter_list|()
block|{
return|return
name|mimeDecodeHeaders
return|;
block|}
DECL|method|setMimeDecodeHeaders (Boolean mimeDecodeHeaders)
specifier|public
name|void
name|setMimeDecodeHeaders
parameter_list|(
name|Boolean
name|mimeDecodeHeaders
parameter_list|)
block|{
name|this
operator|.
name|mimeDecodeHeaders
operator|=
name|mimeDecodeHeaders
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

