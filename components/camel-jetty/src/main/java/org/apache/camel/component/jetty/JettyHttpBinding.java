begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty
package|;
end_package

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
name|spi
operator|.
name|HeaderFilterStrategy
import|;
end_import

begin_comment
comment|/**  * Jetty specific binding to parse the response when using {@link org.apache.camel.component.jetty.JettyHttpProducer}  *  * @version   */
end_comment

begin_interface
DECL|interface|JettyHttpBinding
specifier|public
interface|interface
name|JettyHttpBinding
block|{
comment|/**      * Parses the response from the Jetty client.      *      * @param exchange  the Exchange which to populate with the response      * @param httpExchange  the response from the Jetty client      * @throws Exception is thrown if error parsing response      */
DECL|method|populateResponse (Exchange exchange, JettyContentExchange httpExchange)
name|void
name|populateResponse
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|JettyContentExchange
name|httpExchange
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Gets the header filter strategy      *      * @return the strategy      */
DECL|method|getHeaderFilterStrategy ()
name|HeaderFilterStrategy
name|getHeaderFilterStrategy
parameter_list|()
function_decl|;
comment|/**      * Sets the header filter strategy to use.      *<p/>      * Will default use {@link org.apache.camel.component.http.HttpHeaderFilterStrategy}      *      * @param headerFilterStrategy the custom strategy      */
DECL|method|setHeaderFilterStrategy (HeaderFilterStrategy headerFilterStrategy)
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
name|headerFilterStrategy
parameter_list|)
function_decl|;
comment|/**      * Whether to throw {@link org.apache.camel.component.http.HttpOperationFailedException}      * in case of response code != 200.      *      * @param throwExceptionOnFailure<tt>true</tt> to throw exception      */
DECL|method|setThrowExceptionOnFailure (boolean throwExceptionOnFailure)
name|void
name|setThrowExceptionOnFailure
parameter_list|(
name|boolean
name|throwExceptionOnFailure
parameter_list|)
function_decl|;
comment|/**      * Whether to throw {@link org.apache.camel.component.http.HttpOperationFailedException}      * in case of response code != 200.      *      * @return<tt>true</tt> to throw exception      */
DECL|method|isThrowExceptionOnFailure ()
name|boolean
name|isThrowExceptionOnFailure
parameter_list|()
function_decl|;
comment|/**      * Whether to transfer exception back as a serialized java object      * if processing failed due to an exception      *      * @param transferException<tt>true</tt> to transfer exception      */
DECL|method|setTransferException (boolean transferException)
name|void
name|setTransferException
parameter_list|(
name|boolean
name|transferException
parameter_list|)
function_decl|;
comment|/**      * Whether to transfer exception back as a serialized java object      * if processing failed due to an exception      *      * @return<tt>true</tt> to transfer exception      */
DECL|method|isTransferException ()
name|boolean
name|isTransferException
parameter_list|()
function_decl|;
DECL|method|setSupportRedirect (boolean supportRedirect)
name|void
name|setSupportRedirect
parameter_list|(
name|boolean
name|supportRedirect
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

