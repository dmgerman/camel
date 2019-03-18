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

begin_comment
comment|/**  * CoAP component constants  */
end_comment

begin_interface
DECL|interface|CoAPConstants
specifier|public
interface|interface
name|CoAPConstants
block|{
comment|/**      * Supported request methods      */
DECL|field|METHOD_DELETE
name|String
name|METHOD_DELETE
init|=
literal|"DELETE"
decl_stmt|;
DECL|field|METHOD_GET
name|String
name|METHOD_GET
init|=
literal|"GET"
decl_stmt|;
DECL|field|METHOD_PING
name|String
name|METHOD_PING
init|=
literal|"PING"
decl_stmt|;
DECL|field|METHOD_POST
name|String
name|METHOD_POST
init|=
literal|"POST"
decl_stmt|;
DECL|field|METHOD_PUT
name|String
name|METHOD_PUT
init|=
literal|"PUT"
decl_stmt|;
comment|/**      * Supported CoAP server methods      */
DECL|field|METHOD_RESTRICT_ALL
name|String
name|METHOD_RESTRICT_ALL
init|=
name|String
operator|.
name|format
argument_list|(
literal|"%s,%s,%s,%s"
argument_list|,
name|METHOD_DELETE
argument_list|,
name|METHOD_GET
argument_list|,
name|METHOD_POST
argument_list|,
name|METHOD_PUT
argument_list|)
decl_stmt|;
comment|/**      * CoAP exchange header names      */
DECL|field|COAP_METHOD
name|String
name|COAP_METHOD
init|=
literal|"CamelCoapMethod"
decl_stmt|;
DECL|field|COAP_RESPONSE_CODE
name|String
name|COAP_RESPONSE_CODE
init|=
literal|"CamelCoapResponseCode"
decl_stmt|;
DECL|field|COAP_URI
name|String
name|COAP_URI
init|=
literal|"CamelCoapUri"
decl_stmt|;
block|}
end_interface

end_unit

