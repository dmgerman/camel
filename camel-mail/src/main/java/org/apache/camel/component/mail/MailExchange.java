begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mail
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mail
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
name|CamelContext
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
name|DefaultExchange
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|Message
import|;
end_import

begin_comment
comment|/**  * Represents an {@ilnk Exchange} for working with Mail  *  * @version $Revision:520964 $  */
end_comment

begin_class
DECL|class|MailExchange
specifier|public
class|class
name|MailExchange
extends|extends
name|DefaultExchange
block|{
DECL|field|binding
specifier|private
name|MailBinding
name|binding
decl_stmt|;
DECL|method|MailExchange (CamelContext context, MailBinding binding)
specifier|public
name|MailExchange
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|MailBinding
name|binding
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
block|}
DECL|method|MailExchange (CamelContext context, MailBinding binding, Message message)
specifier|public
name|MailExchange
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|MailBinding
name|binding
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|this
argument_list|(
name|context
argument_list|,
name|binding
argument_list|)
expr_stmt|;
name|setIn
argument_list|(
operator|new
name|MailMessage
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getIn ()
specifier|public
name|MailMessage
name|getIn
parameter_list|()
block|{
return|return
operator|(
name|MailMessage
operator|)
name|super
operator|.
name|getIn
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getOut ()
specifier|public
name|MailMessage
name|getOut
parameter_list|()
block|{
return|return
operator|(
name|MailMessage
operator|)
name|super
operator|.
name|getOut
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getFault ()
specifier|public
name|MailMessage
name|getFault
parameter_list|()
block|{
return|return
operator|(
name|MailMessage
operator|)
name|super
operator|.
name|getFault
argument_list|()
return|;
block|}
DECL|method|getBinding ()
specifier|public
name|MailBinding
name|getBinding
parameter_list|()
block|{
return|return
name|binding
return|;
block|}
annotation|@
name|Override
DECL|method|newInstance ()
specifier|public
name|Exchange
name|newInstance
parameter_list|()
block|{
return|return
operator|new
name|MailExchange
argument_list|(
name|getContext
argument_list|()
argument_list|,
name|binding
argument_list|)
return|;
block|}
comment|// Expose Email APIs
comment|//-------------------------------------------------------------------------
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
annotation|@
name|Override
DECL|method|createInMessage ()
specifier|protected
name|MailMessage
name|createInMessage
parameter_list|()
block|{
return|return
operator|new
name|MailMessage
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createOutMessage ()
specifier|protected
name|MailMessage
name|createOutMessage
parameter_list|()
block|{
return|return
operator|new
name|MailMessage
argument_list|()
return|;
block|}
block|}
end_class

end_unit

