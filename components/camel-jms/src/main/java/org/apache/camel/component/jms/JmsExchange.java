begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Message
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
name|ExchangePattern
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

begin_comment
comment|/**  * Represents an {@link Exchange} for working with JMS messages while exposing the inbound and outbound JMS {@link Message}  * objects via {@link #getInMessage()} and {@link #getOutMessage()}  *  * @version $Revision:520964 $  */
end_comment

begin_class
DECL|class|JmsExchange
specifier|public
class|class
name|JmsExchange
extends|extends
name|DefaultExchange
block|{
DECL|field|binding
specifier|private
name|JmsBinding
name|binding
decl_stmt|;
DECL|method|JmsExchange (Endpoint endpoint, ExchangePattern pattern, JmsBinding binding)
specifier|public
name|JmsExchange
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|,
name|JmsBinding
name|binding
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
block|}
DECL|method|JmsExchange (Endpoint endpoint, ExchangePattern pattern, JmsBinding binding, Message message)
specifier|public
name|JmsExchange
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|,
name|JmsBinding
name|binding
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|this
argument_list|(
name|endpoint
argument_list|,
name|pattern
argument_list|,
name|binding
argument_list|)
expr_stmt|;
name|setIn
argument_list|(
operator|new
name|JmsMessage
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|JmsExchange (DefaultExchange parent, JmsBinding binding)
specifier|public
name|JmsExchange
parameter_list|(
name|DefaultExchange
name|parent
parameter_list|,
name|JmsBinding
name|binding
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getIn ()
specifier|public
name|JmsMessage
name|getIn
parameter_list|()
block|{
return|return
operator|(
name|JmsMessage
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
name|JmsMessage
name|getOut
parameter_list|()
block|{
return|return
operator|(
name|JmsMessage
operator|)
name|super
operator|.
name|getOut
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getOut (boolean lazyCreate)
specifier|public
name|JmsMessage
name|getOut
parameter_list|(
name|boolean
name|lazyCreate
parameter_list|)
block|{
return|return
operator|(
name|JmsMessage
operator|)
name|super
operator|.
name|getOut
argument_list|(
name|lazyCreate
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getFault ()
specifier|public
name|JmsMessage
name|getFault
parameter_list|()
block|{
return|return
operator|(
name|JmsMessage
operator|)
name|super
operator|.
name|getFault
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getFault (boolean lazyCreate)
specifier|public
name|JmsMessage
name|getFault
parameter_list|(
name|boolean
name|lazyCreate
parameter_list|)
block|{
return|return
operator|(
name|JmsMessage
operator|)
name|super
operator|.
name|getFault
argument_list|(
name|lazyCreate
argument_list|)
return|;
block|}
DECL|method|getBinding ()
specifier|public
name|JmsBinding
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
name|JmsExchange
argument_list|(
name|this
argument_list|,
name|binding
argument_list|)
return|;
block|}
comment|// Expose JMS APIs
comment|//-------------------------------------------------------------------------
comment|/**      * Return the underlying JMS In message      *      * @return the JMS In message      */
DECL|method|getInMessage ()
specifier|public
name|Message
name|getInMessage
parameter_list|()
block|{
return|return
name|getIn
argument_list|()
operator|.
name|getJmsMessage
argument_list|()
return|;
block|}
comment|/**      * Return the underlying JMS Out message      *      * @return the JMS out message      */
DECL|method|getOutMessage ()
specifier|public
name|Message
name|getOutMessage
parameter_list|()
block|{
return|return
name|getOut
argument_list|()
operator|.
name|getJmsMessage
argument_list|()
return|;
block|}
comment|/**      * Return the underlying JMS Fault message      *      * @return the JMS fault message      */
DECL|method|getFaultMessage ()
specifier|public
name|Message
name|getFaultMessage
parameter_list|()
block|{
return|return
name|getFault
argument_list|()
operator|.
name|getJmsMessage
argument_list|()
return|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
annotation|@
name|Override
DECL|method|createInMessage ()
specifier|protected
name|JmsMessage
name|createInMessage
parameter_list|()
block|{
return|return
operator|new
name|JmsMessage
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createOutMessage ()
specifier|protected
name|JmsMessage
name|createOutMessage
parameter_list|()
block|{
return|return
operator|new
name|JmsMessage
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createFaultMessage ()
specifier|protected
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|createFaultMessage
parameter_list|()
block|{
return|return
operator|new
name|JmsMessage
argument_list|()
return|;
block|}
block|}
end_class

end_unit

