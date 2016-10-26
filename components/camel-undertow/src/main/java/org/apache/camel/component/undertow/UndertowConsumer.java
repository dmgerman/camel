begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.undertow
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|undertow
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
name|nio
operator|.
name|ByteBuffer
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|server
operator|.
name|HttpHandler
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|server
operator|.
name|HttpServerExchange
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|util
operator|.
name|Headers
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|util
operator|.
name|HttpString
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|util
operator|.
name|Methods
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|util
operator|.
name|MimeMappings
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|util
operator|.
name|StatusCodes
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
name|Exchange
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
name|Processor
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
name|TypeConverter
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
name|impl
operator|.
name|DefaultConsumer
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

begin_comment
comment|/**  * The Undertow consumer which is also an Undertow HttpHandler implementation to handle incoming request.  */
end_comment

begin_class
DECL|class|UndertowConsumer
specifier|public
class|class
name|UndertowConsumer
extends|extends
name|DefaultConsumer
implements|implements
name|HttpHandler
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
name|UndertowConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|registrationInfo
specifier|private
name|HttpHandlerRegistrationInfo
name|registrationInfo
decl_stmt|;
DECL|method|UndertowConsumer (UndertowEndpoint endpoint, Processor processor)
specifier|public
name|UndertowConsumer
parameter_list|(
name|UndertowEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|UndertowEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|UndertowEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|getEndpoint
argument_list|()
operator|.
name|getComponent
argument_list|()
operator|.
name|registerConsumer
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
name|getEndpoint
argument_list|()
operator|.
name|getComponent
argument_list|()
operator|.
name|unregisterConsumer
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
DECL|method|getHttpHandlerRegistrationInfo ()
specifier|public
name|HttpHandlerRegistrationInfo
name|getHttpHandlerRegistrationInfo
parameter_list|()
block|{
if|if
condition|(
name|registrationInfo
operator|==
literal|null
condition|)
block|{
name|UndertowEndpoint
name|endpoint
init|=
name|getEndpoint
argument_list|()
decl_stmt|;
name|registrationInfo
operator|=
operator|new
name|HttpHandlerRegistrationInfo
argument_list|()
expr_stmt|;
name|registrationInfo
operator|.
name|setUri
argument_list|(
name|endpoint
operator|.
name|getHttpURI
argument_list|()
argument_list|)
expr_stmt|;
name|registrationInfo
operator|.
name|setMethodRestrict
argument_list|(
name|endpoint
operator|.
name|getHttpMethodRestrict
argument_list|()
argument_list|)
expr_stmt|;
name|registrationInfo
operator|.
name|setMatchOnUriPrefix
argument_list|(
name|endpoint
operator|.
name|getMatchOnUriPrefix
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|registrationInfo
return|;
block|}
annotation|@
name|Override
DECL|method|handleRequest (HttpServerExchange httpExchange)
specifier|public
name|void
name|handleRequest
parameter_list|(
name|HttpServerExchange
name|httpExchange
parameter_list|)
throws|throws
name|Exception
block|{
name|HttpString
name|requestMethod
init|=
name|httpExchange
operator|.
name|getRequestMethod
argument_list|()
decl_stmt|;
if|if
condition|(
name|Methods
operator|.
name|OPTIONS
operator|.
name|equals
argument_list|(
name|requestMethod
argument_list|)
operator|&&
operator|!
name|getEndpoint
argument_list|()
operator|.
name|isOptionsEnabled
argument_list|()
condition|)
block|{
name|String
name|allowedMethods
decl_stmt|;
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getHttpMethodRestrict
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|allowedMethods
operator|=
literal|"OPTIONS,"
operator|+
name|getEndpoint
argument_list|()
operator|.
name|getHttpMethodRestrict
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|allowedMethods
operator|=
literal|"GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,CONNECT,PATCH"
expr_stmt|;
block|}
comment|//return list of allowed methods in response headers
name|httpExchange
operator|.
name|setStatusCode
argument_list|(
name|StatusCodes
operator|.
name|OK
argument_list|)
expr_stmt|;
name|httpExchange
operator|.
name|getResponseHeaders
argument_list|()
operator|.
name|put
argument_list|(
name|ExchangeHeaders
operator|.
name|CONTENT_TYPE
argument_list|,
name|MimeMappings
operator|.
name|DEFAULT_MIME_MAPPINGS
operator|.
name|get
argument_list|(
literal|"txt"
argument_list|)
argument_list|)
expr_stmt|;
name|httpExchange
operator|.
name|getResponseHeaders
argument_list|()
operator|.
name|put
argument_list|(
name|ExchangeHeaders
operator|.
name|CONTENT_LENGTH
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|httpExchange
operator|.
name|getResponseHeaders
argument_list|()
operator|.
name|put
argument_list|(
name|Headers
operator|.
name|ALLOW
argument_list|,
name|allowedMethods
argument_list|)
expr_stmt|;
name|httpExchange
operator|.
name|getResponseSender
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
return|return;
block|}
comment|//perform blocking operation on exchange
if|if
condition|(
name|httpExchange
operator|.
name|isInIoThread
argument_list|()
condition|)
block|{
name|httpExchange
operator|.
name|dispatch
argument_list|(
name|this
argument_list|)
expr_stmt|;
return|return;
block|}
comment|//create new Exchange
comment|//binding is used to extract header and payload(if available)
name|Exchange
name|camelExchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|(
name|httpExchange
argument_list|)
decl_stmt|;
comment|//Unit of Work to process the Exchange
name|createUoW
argument_list|(
name|camelExchange
argument_list|)
expr_stmt|;
try|try
block|{
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|camelExchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|doneUoW
argument_list|(
name|camelExchange
argument_list|)
expr_stmt|;
block|}
name|Object
name|body
init|=
name|getResponseBody
argument_list|(
name|httpExchange
argument_list|,
name|camelExchange
argument_list|)
decl_stmt|;
name|TypeConverter
name|tc
init|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
decl_stmt|;
if|if
condition|(
name|body
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"No payload to send as reply for exchange: "
operator|+
name|camelExchange
argument_list|)
expr_stmt|;
name|httpExchange
operator|.
name|getResponseHeaders
argument_list|()
operator|.
name|put
argument_list|(
name|ExchangeHeaders
operator|.
name|CONTENT_TYPE
argument_list|,
name|MimeMappings
operator|.
name|DEFAULT_MIME_MAPPINGS
operator|.
name|get
argument_list|(
literal|"txt"
argument_list|)
argument_list|)
expr_stmt|;
name|httpExchange
operator|.
name|getResponseSender
argument_list|()
operator|.
name|send
argument_list|(
literal|"No response available"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ByteBuffer
name|bodyAsByteBuffer
init|=
name|tc
operator|.
name|convertTo
argument_list|(
name|ByteBuffer
operator|.
name|class
argument_list|,
name|body
argument_list|)
decl_stmt|;
name|httpExchange
operator|.
name|getResponseSender
argument_list|()
operator|.
name|send
argument_list|(
name|bodyAsByteBuffer
argument_list|)
expr_stmt|;
block|}
name|httpExchange
operator|.
name|getResponseSender
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
DECL|method|getResponseBody (HttpServerExchange httpExchange, Exchange camelExchange)
specifier|private
name|Object
name|getResponseBody
parameter_list|(
name|HttpServerExchange
name|httpExchange
parameter_list|,
name|Exchange
name|camelExchange
parameter_list|)
throws|throws
name|IOException
block|{
name|Object
name|result
decl_stmt|;
if|if
condition|(
name|camelExchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|result
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getUndertowHttpBinding
argument_list|()
operator|.
name|toHttpResponse
argument_list|(
name|httpExchange
argument_list|,
name|camelExchange
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getUndertowHttpBinding
argument_list|()
operator|.
name|toHttpResponse
argument_list|(
name|httpExchange
argument_list|,
name|camelExchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

