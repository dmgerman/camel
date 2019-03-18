begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.opentracing.decorators
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|opentracing
operator|.
name|decorators
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|Endpoint
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

begin_class
DECL|class|CometdSpanDecorator
specifier|public
class|class
name|CometdSpanDecorator
extends|extends
name|AbstractMessagingSpanDecorator
block|{
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|String
name|getComponent
parameter_list|()
block|{
return|return
literal|"cometd"
return|;
block|}
annotation|@
name|Override
DECL|method|getDestination (Exchange exchange, Endpoint endpoint)
specifier|protected
name|String
name|getDestination
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
comment|// Extract path, and remove leading '/'
return|return
name|URI
operator|.
name|create
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
operator|.
name|getPath
argument_list|()
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
return|;
block|}
block|}
end_class

end_unit

