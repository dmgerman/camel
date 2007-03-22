begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jbi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jbi
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
name|jbi
operator|.
name|messaging
operator|.
name|MessageExchange
import|;
end_import

begin_comment
comment|/**  * An {@link Exchange} working with JBI which exposes the underlying JBI features such as the   * JBI {@link #getMessageExchange()}  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|JbiExchange
specifier|public
class|class
name|JbiExchange
extends|extends
name|DefaultExchange
block|{
DECL|field|binding
specifier|private
specifier|final
name|JbiBinding
name|binding
decl_stmt|;
DECL|field|messageExchange
specifier|private
name|MessageExchange
name|messageExchange
decl_stmt|;
DECL|method|JbiExchange (CamelContext context, JbiBinding binding)
specifier|public
name|JbiExchange
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|JbiBinding
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
DECL|method|JbiExchange (CamelContext context, JbiBinding binding, MessageExchange messageExchange)
specifier|public
name|JbiExchange
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|JbiBinding
name|binding
parameter_list|,
name|MessageExchange
name|messageExchange
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
name|this
operator|.
name|messageExchange
operator|=
name|messageExchange
expr_stmt|;
comment|// TODO we could maybe use the typesafe APIs of different derived APIs from JBI
name|setIn
argument_list|(
operator|new
name|JbiMessage
argument_list|(
name|messageExchange
operator|.
name|getMessage
argument_list|(
literal|"in"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|setOut
argument_list|(
operator|new
name|JbiMessage
argument_list|(
name|messageExchange
operator|.
name|getMessage
argument_list|(
literal|"out"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|setFault
argument_list|(
operator|new
name|JbiMessage
argument_list|(
name|messageExchange
operator|.
name|getMessage
argument_list|(
literal|"fault"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the underlying JBI message exchange for an inbound exchange      * or null for outbound messages      *      * @return the inbound message exchange      */
DECL|method|getMessageExchange ()
specifier|public
name|MessageExchange
name|getMessageExchange
parameter_list|()
block|{
return|return
name|messageExchange
return|;
block|}
annotation|@
name|Override
DECL|method|getIn ()
specifier|public
name|JbiMessage
name|getIn
parameter_list|()
block|{
return|return
operator|(
name|JbiMessage
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
name|JbiMessage
name|getOut
parameter_list|()
block|{
return|return
operator|(
name|JbiMessage
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
name|JbiMessage
name|getFault
parameter_list|()
block|{
return|return
operator|(
name|JbiMessage
operator|)
name|super
operator|.
name|getFault
argument_list|()
return|;
block|}
comment|/**      * @return the Camel<-> JBI binding      */
DECL|method|getBinding ()
specifier|public
name|JbiBinding
name|getBinding
parameter_list|()
block|{
return|return
name|binding
return|;
block|}
annotation|@
name|Override
DECL|method|createInMessage ()
specifier|protected
name|JbiMessage
name|createInMessage
parameter_list|()
block|{
return|return
operator|new
name|JbiMessage
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createOutMessage ()
specifier|protected
name|JbiMessage
name|createOutMessage
parameter_list|()
block|{
return|return
operator|new
name|JbiMessage
argument_list|()
return|;
block|}
block|}
end_class

end_unit

