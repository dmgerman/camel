begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.ws.utils
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
name|utils
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|context
operator|.
name|MessageContext
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
name|transport
operator|.
name|WebServiceMessageReceiver
import|;
end_import

begin_comment
comment|/**  * Used for test to extract the message that was sent  */
end_comment

begin_class
DECL|class|OutputChannelReceiver
specifier|public
class|class
name|OutputChannelReceiver
implements|implements
name|WebServiceMessageReceiver
block|{
DECL|field|messageContext
specifier|private
name|MessageContext
name|messageContext
decl_stmt|;
annotation|@
name|Override
DECL|method|receive (MessageContext messageContext)
specifier|public
name|void
name|receive
parameter_list|(
name|MessageContext
name|messageContext
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|messageContext
operator|=
name|messageContext
expr_stmt|;
block|}
DECL|method|getMessageContext ()
specifier|public
name|MessageContext
name|getMessageContext
parameter_list|()
block|{
return|return
name|messageContext
return|;
block|}
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|this
operator|.
name|messageContext
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

