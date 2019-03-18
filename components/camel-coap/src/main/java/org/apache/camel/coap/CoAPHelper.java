begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.coap
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|coap
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|californium
operator|.
name|core
operator|.
name|CoapClient
import|;
end_import

begin_comment
comment|/**  * Various helper methods for CoAP  */
end_comment

begin_class
DECL|class|CoAPHelper
specifier|public
specifier|final
class|class
name|CoAPHelper
block|{
DECL|method|CoAPHelper ()
specifier|private
name|CoAPHelper
parameter_list|()
block|{     }
comment|/**      * Determines which CoAP request method to use based on the content of the target      * request URI, the message body or value from the CamelCoapMethod header.      *      * @param exchange the exchange      * @param client the CoAP client      * @return the CoAP request method      */
DECL|method|getDefaultMethod (Exchange exchange, CoapClient client)
specifier|public
specifier|static
name|String
name|getDefaultMethod
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|CoapClient
name|client
parameter_list|)
block|{
name|String
name|method
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|CoAPConstants
operator|.
name|COAP_METHOD
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|method
operator|==
literal|null
condition|)
block|{
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|body
operator|==
literal|null
operator|||
name|client
operator|.
name|getURI
argument_list|()
operator|.
name|contains
argument_list|(
literal|"?"
argument_list|)
condition|)
block|{
name|method
operator|=
name|CoAPConstants
operator|.
name|METHOD_GET
expr_stmt|;
block|}
else|else
block|{
name|method
operator|=
name|CoAPConstants
operator|.
name|METHOD_POST
expr_stmt|;
block|}
block|}
return|return
name|method
return|;
block|}
comment|/**      * Determines which method verbs the CoAP server should be restricted to handling.      *      * @param methodRestrict      * @return      */
DECL|method|getDefaultMethodRestrict (String methodRestrict)
specifier|public
specifier|static
name|String
name|getDefaultMethodRestrict
parameter_list|(
name|String
name|methodRestrict
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|methodRestrict
argument_list|)
condition|)
block|{
return|return
name|methodRestrict
return|;
block|}
return|return
name|CoAPConstants
operator|.
name|METHOD_RESTRICT_ALL
return|;
block|}
block|}
end_class

end_unit

