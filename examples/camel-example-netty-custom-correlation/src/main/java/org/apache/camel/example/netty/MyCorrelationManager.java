begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.netty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|netty
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
name|component
operator|.
name|netty4
operator|.
name|TimeoutCorrelationManagerSupport
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
name|StringHelper
import|;
end_import

begin_comment
comment|/**  * Implement a timeout aware {@link org.apache.camel.component.netty4.NettyCamelStateCorrelationManager}  * that handles all the complexities for us, so we only need to implement how to extract the correlation id.  */
end_comment

begin_class
DECL|class|MyCorrelationManager
specifier|public
class|class
name|MyCorrelationManager
extends|extends
name|TimeoutCorrelationManagerSupport
block|{
annotation|@
name|Override
DECL|method|getRequestCorrelationId (Object request)
specifier|public
name|String
name|getRequestCorrelationId
parameter_list|(
name|Object
name|request
parameter_list|)
block|{
comment|// correlation id is before the first colon
return|return
name|StringHelper
operator|.
name|before
argument_list|(
name|request
operator|.
name|toString
argument_list|()
argument_list|,
literal|":"
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getResponseCorrelationId (Object response)
specifier|public
name|String
name|getResponseCorrelationId
parameter_list|(
name|Object
name|response
parameter_list|)
block|{
comment|// correlation id is before the first colon
return|return
name|StringHelper
operator|.
name|before
argument_list|(
name|response
operator|.
name|toString
argument_list|()
argument_list|,
literal|":"
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getTimeoutResponse (String correlationId, Object request)
specifier|public
name|String
name|getTimeoutResponse
parameter_list|(
name|String
name|correlationId
parameter_list|,
name|Object
name|request
parameter_list|)
block|{
comment|// here we can build a custom response message on timeout, instead
comment|// of having an exception being thrown, however we only have access
comment|// to the correlation id and the request message that was sent over the wire
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

