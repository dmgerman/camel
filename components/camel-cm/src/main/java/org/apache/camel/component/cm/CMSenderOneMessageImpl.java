begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cm
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cm
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

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
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|ParserConfigurationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|OutputKeys
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Result
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Transformer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|DOMImplementation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Text
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
name|cm
operator|.
name|exceptions
operator|.
name|CMDirectException
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
name|cm
operator|.
name|exceptions
operator|.
name|XMLConstructionException
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
name|cm
operator|.
name|exceptions
operator|.
name|cmresponse
operator|.
name|CMResponseException
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
name|cm
operator|.
name|exceptions
operator|.
name|cmresponse
operator|.
name|InsufficientBalanceException
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
name|cm
operator|.
name|exceptions
operator|.
name|cmresponse
operator|.
name|InvalidProductTokenException
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
name|cm
operator|.
name|exceptions
operator|.
name|cmresponse
operator|.
name|NoAccountFoundForProductTokenException
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
name|cm
operator|.
name|exceptions
operator|.
name|cmresponse
operator|.
name|UnknownErrorException
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
name|cm
operator|.
name|exceptions
operator|.
name|cmresponse
operator|.
name|UnroutableMessageException
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
name|client
operator|.
name|HttpClient
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
name|client
operator|.
name|methods
operator|.
name|HttpPost
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
name|entity
operator|.
name|StringEntity
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
name|impl
operator|.
name|client
operator|.
name|HttpClientBuilder
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
DECL|class|CMSenderOneMessageImpl
specifier|public
class|class
name|CMSenderOneMessageImpl
implements|implements
name|CMSender
block|{
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
name|CMSenderOneMessageImpl
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|url
specifier|private
specifier|final
name|String
name|url
decl_stmt|;
DECL|field|productToken
specifier|private
specifier|final
name|UUID
name|productToken
decl_stmt|;
DECL|method|CMSenderOneMessageImpl (final String url, final UUID productToken)
specifier|public
name|CMSenderOneMessageImpl
parameter_list|(
specifier|final
name|String
name|url
parameter_list|,
specifier|final
name|UUID
name|productToken
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
name|this
operator|.
name|productToken
operator|=
name|productToken
expr_stmt|;
block|}
comment|/**      * Sends a message to CM endpoints. 1. CMMessage instance is going to be marshalled to xml. 2. Post request xml string to CMEndpoint.      */
annotation|@
name|Override
DECL|method|send (final CMMessage cmMessage)
specifier|public
name|void
name|send
parameter_list|(
specifier|final
name|CMMessage
name|cmMessage
parameter_list|)
block|{
comment|// See: Check https://dashboard.onlinesmsgateway.com/docs for responses
comment|// 1.Construct XML. Throws XMLConstructionException
specifier|final
name|String
name|xml
init|=
name|createXml
argument_list|(
name|cmMessage
argument_list|)
decl_stmt|;
comment|// 2. Try to send to CM SMS Provider ...Throws CMResponseException
name|doHttpPost
argument_list|(
name|url
argument_list|,
name|xml
argument_list|)
expr_stmt|;
block|}
DECL|method|createXml (final CMMessage message)
specifier|private
name|String
name|createXml
parameter_list|(
specifier|final
name|CMMessage
name|message
parameter_list|)
block|{
try|try
block|{
specifier|final
name|ByteArrayOutputStream
name|xml
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
specifier|final
name|DocumentBuilderFactory
name|factory
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// Get the DocumentBuilder
specifier|final
name|DocumentBuilder
name|docBuilder
init|=
name|factory
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
comment|// Create blank DOM Document
specifier|final
name|DOMImplementation
name|impl
init|=
name|docBuilder
operator|.
name|getDOMImplementation
argument_list|()
decl_stmt|;
specifier|final
name|Document
name|doc
init|=
name|impl
operator|.
name|createDocument
argument_list|(
literal|null
argument_list|,
literal|"MESSAGES"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
comment|// ROOT Element es MESSAGES
specifier|final
name|Element
name|root
init|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
comment|// AUTHENTICATION element
specifier|final
name|Element
name|authenticationElement
init|=
name|doc
operator|.
name|createElement
argument_list|(
literal|"AUTHENTICATION"
argument_list|)
decl_stmt|;
specifier|final
name|Element
name|productTokenElement
init|=
name|doc
operator|.
name|createElement
argument_list|(
literal|"PRODUCTTOKEN"
argument_list|)
decl_stmt|;
name|authenticationElement
operator|.
name|appendChild
argument_list|(
name|productTokenElement
argument_list|)
expr_stmt|;
specifier|final
name|Text
name|productTokenValue
init|=
name|doc
operator|.
name|createTextNode
argument_list|(
literal|""
operator|+
name|productToken
argument_list|)
decl_stmt|;
name|productTokenElement
operator|.
name|appendChild
argument_list|(
name|productTokenValue
argument_list|)
expr_stmt|;
name|root
operator|.
name|appendChild
argument_list|(
name|authenticationElement
argument_list|)
expr_stmt|;
comment|// MSG Element
specifier|final
name|Element
name|msgElement
init|=
name|doc
operator|.
name|createElement
argument_list|(
literal|"MSG"
argument_list|)
decl_stmt|;
name|root
operator|.
name|appendChild
argument_list|(
name|msgElement
argument_list|)
expr_stmt|;
comment|//<FROM>VALUE</FROM>
specifier|final
name|Element
name|fromElement
init|=
name|doc
operator|.
name|createElement
argument_list|(
literal|"FROM"
argument_list|)
decl_stmt|;
name|fromElement
operator|.
name|appendChild
argument_list|(
name|doc
operator|.
name|createTextNode
argument_list|(
name|message
operator|.
name|getSender
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|msgElement
operator|.
name|appendChild
argument_list|(
name|fromElement
argument_list|)
expr_stmt|;
comment|//<BODY>VALUE</BODY>
specifier|final
name|Element
name|bodyElement
init|=
name|doc
operator|.
name|createElement
argument_list|(
literal|"BODY"
argument_list|)
decl_stmt|;
name|bodyElement
operator|.
name|appendChild
argument_list|(
name|doc
operator|.
name|createTextNode
argument_list|(
name|message
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|msgElement
operator|.
name|appendChild
argument_list|(
name|bodyElement
argument_list|)
expr_stmt|;
comment|//<TO>VALUE</TO>
specifier|final
name|Element
name|toElement
init|=
name|doc
operator|.
name|createElement
argument_list|(
literal|"TO"
argument_list|)
decl_stmt|;
name|toElement
operator|.
name|appendChild
argument_list|(
name|doc
operator|.
name|createTextNode
argument_list|(
name|message
operator|.
name|getPhoneNumber
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|msgElement
operator|.
name|appendChild
argument_list|(
name|toElement
argument_list|)
expr_stmt|;
comment|//<DCS>VALUE</DCS> - if UNICODE - messageOut.isGSM338Enc
comment|// false
if|if
condition|(
name|message
operator|.
name|isUnicode
argument_list|()
condition|)
block|{
specifier|final
name|Element
name|dcsElement
init|=
name|doc
operator|.
name|createElement
argument_list|(
literal|"DCS"
argument_list|)
decl_stmt|;
name|dcsElement
operator|.
name|appendChild
argument_list|(
name|doc
operator|.
name|createTextNode
argument_list|(
literal|"8"
argument_list|)
argument_list|)
expr_stmt|;
name|msgElement
operator|.
name|appendChild
argument_list|(
name|dcsElement
argument_list|)
expr_stmt|;
block|}
comment|//<REFERENCE>VALUE</REFERENCE> -Alfanum
specifier|final
name|String
name|id
init|=
name|message
operator|.
name|getIdAsString
argument_list|()
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
operator|&&
operator|!
name|id
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
specifier|final
name|Element
name|refElement
init|=
name|doc
operator|.
name|createElement
argument_list|(
literal|"REFERENCE"
argument_list|)
decl_stmt|;
name|refElement
operator|.
name|appendChild
argument_list|(
name|doc
operator|.
name|createTextNode
argument_list|(
literal|""
operator|+
name|message
operator|.
name|getIdAsString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|msgElement
operator|.
name|appendChild
argument_list|(
name|refElement
argument_list|)
expr_stmt|;
block|}
comment|//<MINIMUMNUMBEROFMESSAGEPARTS>1</MINIMUMNUMBEROFMESSAGEPARTS>
comment|//<MAXIMUMNUMBEROFMESSAGEPARTS>8</MAXIMUMNUMBEROFMESSAGEPARTS>
if|if
condition|(
name|message
operator|.
name|isMultipart
argument_list|()
condition|)
block|{
specifier|final
name|Element
name|minMessagePartsElement
init|=
name|doc
operator|.
name|createElement
argument_list|(
literal|"MINIMUMNUMBEROFMESSAGEPARTS"
argument_list|)
decl_stmt|;
name|minMessagePartsElement
operator|.
name|appendChild
argument_list|(
name|doc
operator|.
name|createTextNode
argument_list|(
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|msgElement
operator|.
name|appendChild
argument_list|(
name|minMessagePartsElement
argument_list|)
expr_stmt|;
specifier|final
name|Element
name|maxMessagePartsElement
init|=
name|doc
operator|.
name|createElement
argument_list|(
literal|"MAXIMUMNUMBEROFMESSAGEPARTS"
argument_list|)
decl_stmt|;
name|maxMessagePartsElement
operator|.
name|appendChild
argument_list|(
name|doc
operator|.
name|createTextNode
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|message
operator|.
name|getMultiparts
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|msgElement
operator|.
name|appendChild
argument_list|(
name|maxMessagePartsElement
argument_list|)
expr_stmt|;
block|}
comment|// Creatate XML as String
specifier|final
name|Transformer
name|aTransformer
init|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newTransformer
argument_list|()
decl_stmt|;
name|aTransformer
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|INDENT
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
specifier|final
name|Source
name|src
init|=
operator|new
name|DOMSource
argument_list|(
name|doc
argument_list|)
decl_stmt|;
specifier|final
name|Result
name|dest
init|=
operator|new
name|StreamResult
argument_list|(
name|xml
argument_list|)
decl_stmt|;
name|aTransformer
operator|.
name|transform
argument_list|(
name|src
argument_list|,
name|dest
argument_list|)
expr_stmt|;
return|return
name|xml
operator|.
name|toString
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
specifier|final
name|TransformerException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|XMLConstructionException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cant serialize CMMessage %s"
argument_list|,
name|message
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
specifier|final
name|ParserConfigurationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|XMLConstructionException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cant serialize CMMessage %s"
argument_list|,
name|message
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|doHttpPost (final String urlString, final String requestString)
specifier|private
name|void
name|doHttpPost
parameter_list|(
specifier|final
name|String
name|urlString
parameter_list|,
specifier|final
name|String
name|requestString
parameter_list|)
block|{
specifier|final
name|HttpClient
name|client
init|=
name|HttpClientBuilder
operator|.
name|create
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
specifier|final
name|HttpPost
name|post
init|=
operator|new
name|HttpPost
argument_list|(
name|urlString
argument_list|)
decl_stmt|;
name|post
operator|.
name|setEntity
argument_list|(
operator|new
name|StringEntity
argument_list|(
name|requestString
argument_list|,
name|Charset
operator|.
name|forName
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
specifier|final
name|HttpResponse
name|response
init|=
name|client
operator|.
name|execute
argument_list|(
name|post
argument_list|)
decl_stmt|;
specifier|final
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Response Code : {}"
argument_list|,
name|statusCode
argument_list|)
expr_stmt|;
if|if
condition|(
name|statusCode
operator|==
literal|400
condition|)
block|{
throw|throw
operator|new
name|CMDirectException
argument_list|(
literal|"CM Component and CM API show some kind of inconsistency. "
operator|+
literal|"CM is complaining about not using a post method for the request. And this component only uses POST requests. What happens?"
argument_list|)
throw|;
block|}
if|if
condition|(
name|statusCode
operator|!=
literal|200
condition|)
block|{
throw|throw
operator|new
name|CMDirectException
argument_list|(
literal|"CM Component and CM API show some kind of inconsistency. The component expects the status code to be 200 or 400. New api released? "
argument_list|)
throw|;
block|}
comment|// So we have 200 status code...
comment|// The response type is 'text/plain' and contains the actual
comment|// result of the request processing.
comment|// We obtaing the result text
specifier|final
name|BufferedReader
name|rd
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|response
operator|.
name|getEntity
argument_list|()
operator|.
name|getContent
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|StringBuffer
name|result
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|String
name|line
init|=
literal|null
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|rd
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|result
operator|.
name|append
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
comment|// ... and process it
name|line
operator|=
name|result
operator|.
name|toString
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|line
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// Line is not empty = error
name|LOG
operator|.
name|debug
argument_list|(
literal|"Result of the request processing: FAILED\n{}"
argument_list|,
name|line
argument_list|)
expr_stmt|;
comment|// The response text contains the error description. We will
comment|// throw a custom exception for each.
if|if
condition|(
name|line
operator|.
name|contains
argument_list|(
name|CMConstants
operator|.
name|ERROR_UNKNOWN
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|UnknownErrorException
argument_list|()
throw|;
block|}
elseif|else
if|if
condition|(
name|line
operator|.
name|contains
argument_list|(
name|CMConstants
operator|.
name|ERROR_NO_ACCOUNT
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|NoAccountFoundForProductTokenException
argument_list|()
throw|;
block|}
elseif|else
if|if
condition|(
name|line
operator|.
name|contains
argument_list|(
name|CMConstants
operator|.
name|ERROR_INSUFICIENT_BALANCE
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|InsufficientBalanceException
argument_list|()
throw|;
block|}
elseif|else
if|if
condition|(
name|line
operator|.
name|contains
argument_list|(
name|CMConstants
operator|.
name|ERROR_UNROUTABLE_MESSAGE
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|UnroutableMessageException
argument_list|()
throw|;
block|}
elseif|else
if|if
condition|(
name|line
operator|.
name|contains
argument_list|(
name|CMConstants
operator|.
name|ERROR_INVALID_PRODUCT_TOKEN
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|InvalidProductTokenException
argument_list|()
throw|;
block|}
else|else
block|{
comment|// SO FAR i would expect other kind of ERROR.
comment|// MSISDN correctness and message validity is client
comment|// responsibility
throw|throw
operator|new
name|CMResponseException
argument_list|(
literal|"CHECK ME. I am not expecting this. "
argument_list|)
throw|;
block|}
block|}
comment|// Ok. Line is EMPTY - successfully submitted
name|LOG
operator|.
name|debug
argument_list|(
literal|"Result of the request processing: Successfully submited"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|IOException
name|io
parameter_list|)
block|{
throw|throw
operator|new
name|CMDirectException
argument_list|(
name|io
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|t
operator|instanceof
name|CMDirectException
operator|)
condition|)
block|{
comment|// Chain it
name|t
operator|=
operator|new
name|CMDirectException
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|(
name|CMDirectException
operator|)
name|t
throw|;
block|}
block|}
block|}
end_class

end_unit

