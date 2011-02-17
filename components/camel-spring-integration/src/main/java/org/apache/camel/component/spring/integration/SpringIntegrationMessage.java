begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.integration
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
name|integration
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|impl
operator|.
name|DefaultMessage
import|;
end_import

begin_comment
comment|/**  * The Message {@link DefaultMessage} implementation  * for accessing the SpringIntegrationMessage  *  * @version   */
end_comment

begin_class
DECL|class|SpringIntegrationMessage
specifier|public
class|class
name|SpringIntegrationMessage
extends|extends
name|DefaultMessage
block|{
DECL|field|siMessage
specifier|private
name|org
operator|.
name|springframework
operator|.
name|integration
operator|.
name|Message
name|siMessage
decl_stmt|;
DECL|method|SpringIntegrationMessage ()
specifier|public
name|SpringIntegrationMessage
parameter_list|()
block|{     }
DECL|method|SpringIntegrationMessage (org.springframework.integration.Message message)
specifier|public
name|SpringIntegrationMessage
parameter_list|(
name|org
operator|.
name|springframework
operator|.
name|integration
operator|.
name|Message
name|message
parameter_list|)
block|{
name|this
operator|.
name|siMessage
operator|=
name|message
expr_stmt|;
block|}
DECL|method|setMessage (org.springframework.integration.Message message)
specifier|public
name|void
name|setMessage
parameter_list|(
name|org
operator|.
name|springframework
operator|.
name|integration
operator|.
name|Message
name|message
parameter_list|)
block|{
name|this
operator|.
name|siMessage
operator|=
name|message
expr_stmt|;
block|}
DECL|method|getMessage ()
specifier|public
name|org
operator|.
name|springframework
operator|.
name|integration
operator|.
name|Message
name|getMessage
parameter_list|()
block|{
return|return
name|siMessage
return|;
block|}
annotation|@
name|Override
DECL|method|copyFrom (org.apache.camel.Message that)
specifier|public
name|void
name|copyFrom
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|that
parameter_list|)
block|{
name|setMessageId
argument_list|(
name|that
operator|.
name|getMessageId
argument_list|()
argument_list|)
expr_stmt|;
name|setBody
argument_list|(
name|that
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|getHeaders
argument_list|()
operator|.
name|putAll
argument_list|(
name|that
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|that
operator|instanceof
name|SpringIntegrationMessage
condition|)
block|{
name|SpringIntegrationMessage
name|orig
init|=
operator|(
name|SpringIntegrationMessage
operator|)
name|that
decl_stmt|;
name|setMessage
argument_list|(
name|orig
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|getAttachments
argument_list|()
operator|.
name|putAll
argument_list|(
name|that
operator|.
name|getAttachments
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|siMessage
operator|!=
literal|null
condition|)
block|{
return|return
literal|"SpringIntegrationMessage: "
operator|+
name|siMessage
return|;
block|}
else|else
block|{
return|return
literal|"SpringIntegrationMessage: "
operator|+
name|getBody
argument_list|()
return|;
block|}
block|}
DECL|method|getHeader (String name)
specifier|public
name|Object
name|getHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|siMessage
operator|!=
literal|null
condition|)
block|{
return|return
name|siMessage
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|super
operator|.
name|getHeader
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getHeaders ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getHeaders
parameter_list|()
block|{
if|if
condition|(
name|siMessage
operator|!=
literal|null
condition|)
block|{
return|return
name|siMessage
operator|.
name|getHeaders
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|super
operator|.
name|getHeaders
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|newInstance ()
specifier|public
name|SpringIntegrationMessage
name|newInstance
parameter_list|()
block|{
return|return
operator|new
name|SpringIntegrationMessage
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createBody ()
specifier|protected
name|Object
name|createBody
parameter_list|()
block|{
return|return
name|siMessage
operator|.
name|getPayload
argument_list|()
return|;
block|}
block|}
end_class

end_unit

