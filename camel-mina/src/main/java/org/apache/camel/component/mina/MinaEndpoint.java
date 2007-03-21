begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mina
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mina
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
name|impl
operator|.
name|DefaultEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|common
operator|.
name|IoHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|common
operator|.
name|IoHandlerAdapter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|common
operator|.
name|IoSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|common
operator|.
name|IoAcceptor
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|MinaEndpoint
specifier|public
class|class
name|MinaEndpoint
extends|extends
name|DefaultEndpoint
argument_list|<
name|MinaExchange
argument_list|>
block|{
DECL|field|session
specifier|private
name|IoSession
name|session
decl_stmt|;
DECL|field|serverHandler
specifier|private
name|IoHandler
name|serverHandler
decl_stmt|;
DECL|field|clientHandler
specifier|private
name|IoHandler
name|clientHandler
decl_stmt|;
DECL|field|acceptor
specifier|private
specifier|final
name|IoAcceptor
name|acceptor
decl_stmt|;
DECL|method|MinaEndpoint (String endpointUri, CamelContext container, IoAcceptor acceptor)
specifier|public
name|MinaEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|CamelContext
name|container
parameter_list|,
name|IoAcceptor
name|acceptor
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|container
argument_list|)
expr_stmt|;
name|this
operator|.
name|acceptor
operator|=
name|acceptor
expr_stmt|;
block|}
DECL|method|onExchange (MinaExchange minaExchange)
specifier|public
name|void
name|onExchange
parameter_list|(
name|MinaExchange
name|minaExchange
parameter_list|)
block|{
name|session
operator|.
name|write
argument_list|(
name|minaExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createExchange ()
specifier|public
name|MinaExchange
name|createExchange
parameter_list|()
block|{
return|return
operator|new
name|MinaExchange
argument_list|(
name|getContext
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getServerHandler ()
specifier|public
name|IoHandler
name|getServerHandler
parameter_list|()
block|{
if|if
condition|(
name|serverHandler
operator|==
literal|null
condition|)
block|{
name|serverHandler
operator|=
name|createServerHandler
argument_list|()
expr_stmt|;
block|}
return|return
name|serverHandler
return|;
block|}
DECL|method|getClientHandler ()
specifier|public
name|IoHandler
name|getClientHandler
parameter_list|()
block|{
if|if
condition|(
name|clientHandler
operator|==
literal|null
condition|)
block|{
name|clientHandler
operator|=
name|createClientHandler
argument_list|()
expr_stmt|;
block|}
return|return
name|clientHandler
return|;
block|}
DECL|method|getSession ()
specifier|public
name|IoSession
name|getSession
parameter_list|()
block|{
return|return
name|session
return|;
block|}
DECL|method|setSession (IoSession session)
specifier|public
name|void
name|setSession
parameter_list|(
name|IoSession
name|session
parameter_list|)
block|{
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
annotation|@
name|Override
DECL|method|doDeactivate ()
specifier|protected
name|void
name|doDeactivate
parameter_list|()
block|{
name|acceptor
operator|.
name|unbindAll
argument_list|()
expr_stmt|;
block|}
DECL|method|createClientHandler ()
specifier|protected
name|IoHandler
name|createClientHandler
parameter_list|()
block|{
return|return
operator|new
name|IoHandlerAdapter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|messageReceived
parameter_list|(
name|IoSession
name|ioSession
parameter_list|,
name|Object
name|object
parameter_list|)
throws|throws
name|Exception
block|{
name|super
operator|.
name|messageReceived
argument_list|(
name|ioSession
argument_list|,
name|object
argument_list|)
expr_stmt|;
comment|/** TODO */
block|}
block|}
return|;
block|}
DECL|method|createServerHandler ()
specifier|protected
name|IoHandler
name|createServerHandler
parameter_list|()
block|{
return|return
operator|new
name|IoHandlerAdapter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|messageReceived
parameter_list|(
name|IoSession
name|ioSession
parameter_list|,
name|Object
name|object
parameter_list|)
throws|throws
name|Exception
block|{
name|super
operator|.
name|messageReceived
argument_list|(
name|ioSession
argument_list|,
name|object
argument_list|)
expr_stmt|;
comment|/** TODO */
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

