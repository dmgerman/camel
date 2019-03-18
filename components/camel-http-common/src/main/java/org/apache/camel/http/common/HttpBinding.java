begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.http.common
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|http
operator|.
name|common
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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|Message
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
name|spi
operator|.
name|HeaderFilterStrategy
import|;
end_import

begin_comment
comment|/**  * A pluggable strategy for configuring the http binding so reading request and writing response  * can be customized using the Java Servlet API.  *<p/>  * This is also used by the<tt>camel-jetty</tt> component in the<tt>JettyHttpConsumer</tt> class.  */
end_comment

begin_interface
DECL|interface|HttpBinding
specifier|public
interface|interface
name|HttpBinding
block|{
comment|/**      * Strategy to read the given request and bindings it to the given message.      *      * @param request  the request      * @param message  the message to populate with data from request      */
DECL|method|readRequest (HttpServletRequest request, HttpMessage message)
name|void
name|readRequest
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpMessage
name|message
parameter_list|)
function_decl|;
comment|/**      * Parses the body from a {@link org.apache.camel.http.common.HttpMessage}      *      * @param httpMessage the http message      * @return the parsed body returned as either a {@link java.io.InputStream} or a {@link java.io.Reader}      * depending on the {@link #setUseReaderForPayload(boolean)} property.      * @throws java.io.IOException can be thrown      */
DECL|method|parseBody (HttpMessage httpMessage)
name|Object
name|parseBody
parameter_list|(
name|HttpMessage
name|httpMessage
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Writes the exchange to the servlet response.      *<p/>      * Default implementation will delegate to the following methods depending on the status of the exchange      *<ul>      *<li>doWriteResponse - processing returns a OUT message</li>      *<li>doWriteFaultResponse - processing returns a fault message</li>      *<li>doWriteResponse - processing returns an exception and status code 500</li>      *</ul>      *      * @param exchange the exchange      * @param response the http response      * @throws java.io.IOException can be thrown from http response      */
DECL|method|writeResponse (Exchange exchange, HttpServletResponse response)
name|void
name|writeResponse
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Strategy method that writes the response to the http response stream when an exception occurred      *      * @param exception  the exception occurred      * @param response   the http response      * @throws java.io.IOException can be thrown from http response      */
DECL|method|doWriteExceptionResponse (Throwable exception, HttpServletResponse response)
name|void
name|doWriteExceptionResponse
parameter_list|(
name|Throwable
name|exception
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Strategy method that writes the response to the http response stream for a fault message      *      * @param message  the fault message      * @param response the http response      * @param exchange the exchange to provide context for header filtering      * @throws java.io.IOException can be thrown from http response      */
DECL|method|doWriteFaultResponse (Message message, HttpServletResponse response, Exchange exchange)
name|void
name|doWriteFaultResponse
parameter_list|(
name|Message
name|message
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Strategy method that writes the response to the http response stream for an OUT message      *      * @param message  the OUT message      * @param response the http response      * @param exchange the exchange to provide context for header filtering      * @throws java.io.IOException can be thrown from http response      */
DECL|method|doWriteResponse (Message message, HttpServletResponse response, Exchange exchange)
name|void
name|doWriteResponse
parameter_list|(
name|Message
name|message
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Should reader by used instead of input stream.      *      * @see #setUseReaderForPayload(boolean) for more details      * @return<tt>true</tt> if reader should be used      */
DECL|method|isUseReaderForPayload ()
name|boolean
name|isUseReaderForPayload
parameter_list|()
function_decl|;
comment|/**      * Should the {@link javax.servlet.http.HttpServletRequest#getReader()} be exposed as the payload of input messages in the Camel      * {@link org.apache.camel.Message#getBody()} or not. If false then the {@link javax.servlet.http.HttpServletRequest#getInputStream()} will be exposed.      *<p/>      * Is default<tt>false</tt>.      *      * @param useReaderForPayload whether to use reader or not      */
DECL|method|setUseReaderForPayload (boolean useReaderForPayload)
name|void
name|setUseReaderForPayload
parameter_list|(
name|boolean
name|useReaderForPayload
parameter_list|)
function_decl|;
comment|/**      * If enabled and an Exchange failed processing on the consumer side, and if the caused Exception was send back      * serialized in the response as a application/x-java-serialized-object content type (for example using Jetty or      * Servlet Camel components). On the producer side the exception will be deserialized and thrown as is,      * instead of the HttpOperationFailedException. The caused exception is required to be serialized.      *<p/>      * This is by default turned off. If you enable this then be aware that Java will deserialize the incoming      * data from the request to Java and that can be a potential security risk.      */
DECL|method|isTransferException ()
name|boolean
name|isTransferException
parameter_list|()
function_decl|;
comment|/**      * Whether to allow java serialization when a request uses context-type=application/x-java-serialized-object      *<p/>      * This is by default turned off. If you enable this then be aware that Java will deserialize the incoming      * data from the request to Java and that can be a potential security risk.      */
DECL|method|isAllowJavaSerializedObject ()
name|boolean
name|isAllowJavaSerializedObject
parameter_list|()
function_decl|;
comment|/**      * Whether to eager check whether the HTTP requests has content if the content-length header is 0 or not present.      * This can be turned on in case HTTP clients do not send streamed data.      */
DECL|method|isEagerCheckContentAvailable ()
name|boolean
name|isEagerCheckContentAvailable
parameter_list|()
function_decl|;
comment|/**      * Whether to allow Exchange Body HTTP mapping      */
DECL|method|isMapHttpMessageBody ()
name|boolean
name|isMapHttpMessageBody
parameter_list|()
function_decl|;
comment|/**      * Whether to allow Exchange Headers HTTP mapping      */
DECL|method|isMapHttpMessageHeaders ()
name|boolean
name|isMapHttpMessageHeaders
parameter_list|()
function_decl|;
comment|/**      * Whether to allow Exchange Form URL Encoded Body HTTP mapping      */
DECL|method|isMapHttpMessageFormUrlEncodedBody ()
name|boolean
name|isMapHttpMessageFormUrlEncodedBody
parameter_list|()
function_decl|;
comment|/**      * Whether to eager check whether the HTTP requests has content if the content-length header is 0 or not present.      * This can be turned on in case HTTP clients do not send streamed data.      */
DECL|method|setEagerCheckContentAvailable (boolean eagerCheckContentAvailable)
name|void
name|setEagerCheckContentAvailable
parameter_list|(
name|boolean
name|eagerCheckContentAvailable
parameter_list|)
function_decl|;
comment|/**      * If enabled and an Exchange failed processing on the consumer side, and if the caused Exception was send back      * serialized in the response as a application/x-java-serialized-object content type (for example using Jetty or      * Servlet Camel components). On the producer side the exception will be deserialized and thrown as is,      * instead of the HttpOperationFailedException. The caused exception is required to be serialized.      *<p/>      * This is by default turned off. If you enable this then be aware that Java will deserialize the incoming      * data from the request to Java and that can be a potential security risk.      */
DECL|method|setTransferException (boolean transferException)
name|void
name|setTransferException
parameter_list|(
name|boolean
name|transferException
parameter_list|)
function_decl|;
comment|/**      * Whether to allow java serialization when a request uses context-type=application/x-java-serialized-object      *<p/>      * This is by default turned off. If you enable this then be aware that Java will deserialize the incoming      * data from the request to Java and that can be a potential security risk.      *      * @param allowJavaSerializedObject<tt>true</tt> to allow serializing java objects      */
DECL|method|setAllowJavaSerializedObject (boolean allowJavaSerializedObject)
name|void
name|setAllowJavaSerializedObject
parameter_list|(
name|boolean
name|allowJavaSerializedObject
parameter_list|)
function_decl|;
comment|/**      * Gets the header filter strategy      *      * @return the strategy      */
DECL|method|getHeaderFilterStrategy ()
name|HeaderFilterStrategy
name|getHeaderFilterStrategy
parameter_list|()
function_decl|;
comment|/**      * Sets the header filter strategy to use.      *<p/>      * Will default use {@link org.apache.camel.http.common.HttpHeaderFilterStrategy}      *      * @param headerFilterStrategy the custom strategy      */
DECL|method|setHeaderFilterStrategy (HeaderFilterStrategy headerFilterStrategy)
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
name|headerFilterStrategy
parameter_list|)
function_decl|;
comment|/**      * Whether to allow Exchange Body HTTP mapping      *<p/>      * This is by default turned on. If you disable this then be aware that the Exchange body won't be mapped to HTTP      */
DECL|method|setMapHttpMessageBody (boolean mapHttpMessageBody)
name|void
name|setMapHttpMessageBody
parameter_list|(
name|boolean
name|mapHttpMessageBody
parameter_list|)
function_decl|;
comment|/**      * Whether to allow Exchange Headers HTTP mapping      *<p/>      * This is by default turned on. If you disable this then be aware that the Exchange headers won't be mapped to HTTP      */
DECL|method|setMapHttpMessageHeaders (boolean mapHttpMessageHeaders)
name|void
name|setMapHttpMessageHeaders
parameter_list|(
name|boolean
name|mapHttpMessageHeaders
parameter_list|)
function_decl|;
comment|/**      * Whether to allow Exchange Form URL Encoded Body HTTP mapping      *<p/>      * This is by default turned on. If you disable this then be aware that the Exchange Form URL Encoded Body won't be mapped to HTTP      */
DECL|method|setMapHttpMessageFormUrlEncodedBody (boolean mapHttpMessageFormUrlEncodedBody)
name|void
name|setMapHttpMessageFormUrlEncodedBody
parameter_list|(
name|boolean
name|mapHttpMessageFormUrlEncodedBody
parameter_list|)
function_decl|;
comment|/**      * Whitelist of accepted filename extensions for accepting uploaded files.      *<p/>      * Multiple extensions can be separated by comma, such as txt,xml.      */
DECL|method|getFileNameExtWhitelist ()
name|String
name|getFileNameExtWhitelist
parameter_list|()
function_decl|;
comment|/**      * Whitelist of accepted filename extensions for accepting uploaded files.      *<p/>      * Multiple extensions can be separated by comma, such as txt,xml.      */
DECL|method|setFileNameExtWhitelist (String fileNameExtWhitelist)
name|void
name|setFileNameExtWhitelist
parameter_list|(
name|String
name|fileNameExtWhitelist
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

