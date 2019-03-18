begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
package|;
end_package

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
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_class
DECL|class|CxfPayloadConsumerNamespaceOnEnvelopeStreamCacheTest
specifier|public
class|class
name|CxfPayloadConsumerNamespaceOnEnvelopeStreamCacheTest
extends|extends
name|CxfPayloadConsumerNamespaceOnEnvelopeTest
block|{
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:router"
argument_list|)
comment|// Use message mode to send the raw message
operator|.
name|to
argument_list|(
literal|"cxf:bean:serviceEndpoint?dataFormat=RAW"
argument_list|)
comment|// Convert to String to make testing the result easier
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// The consumer is running in payload mode
name|from
argument_list|(
literal|"cxf:bean:serviceEndpoint?dataFormat=PAYLOAD"
argument_list|)
comment|// Use stream caching to trigger the issue
operator|.
name|streamCaching
argument_list|()
comment|// Parse to DOM to make sure it's still valid XML
operator|.
name|convertBodyTo
argument_list|(
name|Document
operator|.
name|class
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
name|RESPONSE_PAYLOAD
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

