begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hl7
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hl7
package|;
end_package

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|channel
operator|.
name|ChannelHandler
import|;
end_import

begin_comment
comment|/**  * Netty MLLP Encoder factory  */
end_comment

begin_class
DECL|class|HL7MLLPNettyEncoderFactory
specifier|public
class|class
name|HL7MLLPNettyEncoderFactory
extends|extends
name|HL7MLLPConfigAwareChannelHandlerFactory
block|{
annotation|@
name|Override
DECL|method|newChannelHandler ()
specifier|public
name|ChannelHandler
name|newChannelHandler
parameter_list|()
block|{
return|return
operator|new
name|HL7MLLPNettyEncoder
argument_list|(
name|config
argument_list|)
return|;
block|}
block|}
end_class

end_unit

