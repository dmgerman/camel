begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.ws.addressing
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|ws
operator|.
name|addressing
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractXmlApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|soap
operator|.
name|addressing
operator|.
name|client
operator|.
name|ActionCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|soap
operator|.
name|addressing
operator|.
name|core
operator|.
name|MessageAddressingProperties
import|;
end_import

begin_class
DECL|class|ConsumerWSANewChannelParamsActionTests
specifier|public
class|class
name|ConsumerWSANewChannelParamsActionTests
extends|extends
name|AbstractConsumerTests
block|{
DECL|method|channelIn (String actionUri)
specifier|public
name|ActionCallback
name|channelIn
parameter_list|(
name|String
name|actionUri
parameter_list|)
throws|throws
name|URISyntaxException
block|{
comment|// new channel
return|return
name|actionAndReplyTo
argument_list|(
name|actionUri
argument_list|,
literal|"mailto:reply-to-trigger@new-channel.com"
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|channelOut ()
specifier|public
name|MessageAddressingProperties
name|channelOut
parameter_list|()
block|{
return|return
name|newChannelParams
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"org/apache/camel/component/spring/ws/addresing/ConsumerWSAParamsActionTests-context.xml"
block|}
argument_list|)
return|;
block|}
block|}
end_class

end_unit

