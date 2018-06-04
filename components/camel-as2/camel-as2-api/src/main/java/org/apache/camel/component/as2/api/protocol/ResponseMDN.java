begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.as2.api.protocol
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|protocol
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivateKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|Certificate
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
name|as2
operator|.
name|api
operator|.
name|AS2Charset
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
name|as2
operator|.
name|api
operator|.
name|AS2Constants
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
name|as2
operator|.
name|api
operator|.
name|AS2Header
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
name|as2
operator|.
name|api
operator|.
name|AS2ServerManager
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
name|as2
operator|.
name|api
operator|.
name|AS2SignedDataGenerator
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
name|as2
operator|.
name|api
operator|.
name|AS2TransferEncoding
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
name|as2
operator|.
name|api
operator|.
name|InvalidAS2NameException
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
name|as2
operator|.
name|api
operator|.
name|Util
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
name|as2
operator|.
name|api
operator|.
name|entity
operator|.
name|AS2DispositionType
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
name|as2
operator|.
name|api
operator|.
name|entity
operator|.
name|DispositionMode
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
name|as2
operator|.
name|api
operator|.
name|entity
operator|.
name|DispositionNotificationMultipartReportEntity
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
name|as2
operator|.
name|api
operator|.
name|entity
operator|.
name|DispositionNotificationOptions
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
name|as2
operator|.
name|api
operator|.
name|entity
operator|.
name|DispositionNotificationOptionsParser
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
name|as2
operator|.
name|api
operator|.
name|entity
operator|.
name|MultipartSignedEntity
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
name|as2
operator|.
name|api
operator|.
name|util
operator|.
name|EntityUtils
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
name|as2
operator|.
name|api
operator|.
name|util
operator|.
name|HttpMessageUtils
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
name|as2
operator|.
name|api
operator|.
name|util
operator|.
name|SigningUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpEntityEnclosingRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpResponseInterceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|protocol
operator|.
name|HttpContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|protocol
operator|.
name|HttpCoreContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|ResponseMDN
specifier|public
class|class
name|ResponseMDN
implements|implements
name|HttpResponseInterceptor
block|{
DECL|field|BOUNDARY_PARAM_NAME
specifier|public
specifier|static
specifier|final
name|String
name|BOUNDARY_PARAM_NAME
init|=
literal|"boundary"
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ResponseMDN
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|as2Version
specifier|private
specifier|final
name|String
name|as2Version
decl_stmt|;
DECL|field|serverFQDN
specifier|private
specifier|final
name|String
name|serverFQDN
decl_stmt|;
DECL|field|signingCertificateChain
specifier|private
name|Certificate
index|[]
name|signingCertificateChain
decl_stmt|;
DECL|field|signingPrivateKey
specifier|private
name|PrivateKey
name|signingPrivateKey
decl_stmt|;
DECL|method|ResponseMDN (String as2Version, String serverFQDN, Certificate[] signingCertificateChain, PrivateKey signingPrivateKey)
specifier|public
name|ResponseMDN
parameter_list|(
name|String
name|as2Version
parameter_list|,
name|String
name|serverFQDN
parameter_list|,
name|Certificate
index|[]
name|signingCertificateChain
parameter_list|,
name|PrivateKey
name|signingPrivateKey
parameter_list|)
block|{
name|this
operator|.
name|as2Version
operator|=
name|as2Version
expr_stmt|;
name|this
operator|.
name|serverFQDN
operator|=
name|serverFQDN
expr_stmt|;
name|this
operator|.
name|signingCertificateChain
operator|=
name|signingCertificateChain
expr_stmt|;
name|this
operator|.
name|signingPrivateKey
operator|=
name|signingPrivateKey
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (HttpResponse response, HttpContext context)
specifier|public
name|void
name|process
parameter_list|(
name|HttpResponse
name|response
parameter_list|,
name|HttpContext
name|context
parameter_list|)
throws|throws
name|HttpException
throws|,
name|IOException
block|{
name|int
name|statusCode
init|=
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
decl_stmt|;
if|if
condition|(
name|statusCode
operator|<
literal|200
operator|||
name|statusCode
operator|>=
literal|300
condition|)
block|{
comment|// RFC4130 - 7.6 - Status codes in the 200 range SHOULD also be used when an entity is returned
comment|// (a signed receipt in a multipart/signed content type or an unsigned
comment|// receipt in a multipart/report)
name|LOG
operator|.
name|debug
argument_list|(
literal|"MDN not return due to response status code: "
operator|+
name|statusCode
argument_list|)
expr_stmt|;
return|return;
block|}
name|HttpCoreContext
name|coreContext
init|=
name|HttpCoreContext
operator|.
name|adapt
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|HttpEntityEnclosingRequest
name|request
init|=
name|coreContext
operator|.
name|getAttribute
argument_list|(
name|HttpCoreContext
operator|.
name|HTTP_REQUEST
argument_list|,
name|HttpEntityEnclosingRequest
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|request
operator|==
literal|null
condition|)
block|{
comment|// Should never happen; but you never know
name|LOG
operator|.
name|debug
argument_list|(
literal|"MDN not returned due to null request"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|HttpException
argument_list|(
literal|"request missing from HTTP context"
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Processing MDN for request: "
operator|+
name|request
argument_list|)
expr_stmt|;
if|if
condition|(
name|HttpMessageUtils
operator|.
name|getHeaderValue
argument_list|(
name|request
argument_list|,
name|AS2Header
operator|.
name|DISPOSITION_NOTIFICATION_TO
argument_list|)
operator|==
literal|null
condition|)
block|{
comment|// no receipt requested by sender
name|LOG
operator|.
name|debug
argument_list|(
literal|"MDN not returned: no receipt requested"
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// Return a Message Disposition Notification Receipt in response body
name|String
name|boundary
init|=
name|EntityUtils
operator|.
name|createBoundaryValue
argument_list|()
decl_stmt|;
name|DispositionNotificationMultipartReportEntity
name|multipartReportEntity
init|=
operator|new
name|DispositionNotificationMultipartReportEntity
argument_list|(
name|request
argument_list|,
name|response
argument_list|,
name|DispositionMode
operator|.
name|AUTOMATIC_ACTION_MDN_SENT_AUTOMATICALLY
argument_list|,
name|AS2DispositionType
operator|.
name|PROCESSED
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|AS2Charset
operator|.
name|US_ASCII
argument_list|,
name|boundary
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|DispositionNotificationOptions
name|dispositionNotificationOptions
init|=
name|DispositionNotificationOptionsParser
operator|.
name|parseDispositionNotificationOptions
argument_list|(
name|HttpMessageUtils
operator|.
name|getHeaderValue
argument_list|(
name|request
argument_list|,
name|AS2Header
operator|.
name|DISPOSITION_NOTIFICATION_OPTIONS
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|String
name|receiptAddress
init|=
name|HttpMessageUtils
operator|.
name|getHeaderValue
argument_list|(
name|request
argument_list|,
name|AS2Header
operator|.
name|RECEIPT_DELIVERY_OPTION
argument_list|)
decl_stmt|;
if|if
condition|(
name|receiptAddress
operator|!=
literal|null
condition|)
block|{
comment|// Asynchronous Delivery
comment|// TODO Implement
block|}
else|else
block|{
comment|// Synchronous Delivery
comment|/* MIME header */
name|response
operator|.
name|addHeader
argument_list|(
name|AS2Header
operator|.
name|MIME_VERSION
argument_list|,
name|AS2Constants
operator|.
name|MIME_VERSION
argument_list|)
expr_stmt|;
comment|/* AS2-Version header */
name|response
operator|.
name|addHeader
argument_list|(
name|AS2Header
operator|.
name|AS2_VERSION
argument_list|,
name|as2Version
argument_list|)
expr_stmt|;
comment|/* Subject header */
comment|// RFC4130 - 7.3 -  Subject header SHOULD be supplied
name|String
name|subjectPrefix
init|=
name|coreContext
operator|.
name|getAttribute
argument_list|(
name|AS2ServerManager
operator|.
name|SUBJECT
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|subject
init|=
name|HttpMessageUtils
operator|.
name|getHeaderValue
argument_list|(
name|request
argument_list|,
name|AS2Header
operator|.
name|SUBJECT
argument_list|)
decl_stmt|;
if|if
condition|(
name|subjectPrefix
operator|!=
literal|null
operator|&&
name|subject
operator|!=
literal|null
condition|)
block|{
name|subject
operator|=
name|subjectPrefix
operator|+
name|subject
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|subject
operator|!=
literal|null
condition|)
block|{
name|subject
operator|=
literal|"MDN Response To:"
operator|+
name|subject
expr_stmt|;
block|}
else|else
block|{
name|subject
operator|=
literal|"Your Requested MDN Response"
expr_stmt|;
block|}
name|response
operator|.
name|addHeader
argument_list|(
name|AS2Header
operator|.
name|SUBJECT
argument_list|,
name|subject
argument_list|)
expr_stmt|;
comment|/* From header */
name|String
name|from
init|=
name|coreContext
operator|.
name|getAttribute
argument_list|(
name|AS2ServerManager
operator|.
name|FROM
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|response
operator|.
name|addHeader
argument_list|(
name|AS2Header
operator|.
name|FROM
argument_list|,
name|from
argument_list|)
expr_stmt|;
comment|/* AS2-From header */
name|String
name|as2From
init|=
name|HttpMessageUtils
operator|.
name|getHeaderValue
argument_list|(
name|request
argument_list|,
name|AS2Header
operator|.
name|AS2_TO
argument_list|)
decl_stmt|;
try|try
block|{
name|Util
operator|.
name|validateAS2Name
argument_list|(
name|as2From
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidAS2NameException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|HttpException
argument_list|(
literal|"Invalid AS-From name"
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|response
operator|.
name|addHeader
argument_list|(
name|AS2Header
operator|.
name|AS2_FROM
argument_list|,
name|as2From
argument_list|)
expr_stmt|;
comment|/* AS2-To header */
name|String
name|as2To
init|=
name|HttpMessageUtils
operator|.
name|getHeaderValue
argument_list|(
name|request
argument_list|,
name|AS2Header
operator|.
name|AS2_FROM
argument_list|)
decl_stmt|;
try|try
block|{
name|Util
operator|.
name|validateAS2Name
argument_list|(
name|as2To
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidAS2NameException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|HttpException
argument_list|(
literal|"Invalid AS-To name"
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|response
operator|.
name|addHeader
argument_list|(
name|AS2Header
operator|.
name|AS2_TO
argument_list|,
name|as2To
argument_list|)
expr_stmt|;
comment|/* Message-Id header*/
comment|// RFC4130 - 7.3 -  A Message-ID header is added to support message reconciliation
name|response
operator|.
name|addHeader
argument_list|(
name|AS2Header
operator|.
name|MESSAGE_ID
argument_list|,
name|Util
operator|.
name|createMessageId
argument_list|(
name|serverFQDN
argument_list|)
argument_list|)
expr_stmt|;
name|AS2SignedDataGenerator
name|gen
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|dispositionNotificationOptions
operator|.
name|getSignedReceiptProtocol
argument_list|()
operator|!=
literal|null
operator|&&
name|signingCertificateChain
operator|!=
literal|null
operator|&&
name|signingPrivateKey
operator|!=
literal|null
condition|)
block|{
name|gen
operator|=
name|SigningUtils
operator|.
name|createSigningGenerator
argument_list|(
name|signingCertificateChain
argument_list|,
name|signingPrivateKey
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|gen
operator|!=
literal|null
condition|)
block|{
comment|// Create signed receipt
try|try
block|{
name|multipartReportEntity
operator|.
name|setMainBody
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|MultipartSignedEntity
name|multipartSignedEntity
init|=
operator|new
name|MultipartSignedEntity
argument_list|(
name|multipartReportEntity
argument_list|,
name|gen
argument_list|,
name|AS2Charset
operator|.
name|US_ASCII
argument_list|,
name|AS2TransferEncoding
operator|.
name|BASE64
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|response
operator|.
name|setHeader
argument_list|(
name|multipartSignedEntity
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|EntityUtils
operator|.
name|setMessageEntity
argument_list|(
name|response
argument_list|,
name|multipartSignedEntity
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"failed to sign receipt"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// Create unsigned receipt
name|response
operator|.
name|setHeader
argument_list|(
name|multipartReportEntity
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|EntityUtils
operator|.
name|setMessageEntity
argument_list|(
name|response
argument_list|,
name|multipartReportEntity
argument_list|)
expr_stmt|;
block|}
block|}
name|LOG
operator|.
name|debug
argument_list|(
name|Util
operator|.
name|printMessage
argument_list|(
name|response
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

