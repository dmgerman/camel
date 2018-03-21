begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.milo.client
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|milo
operator|.
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CompletableFuture
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|Boolean
operator|.
name|TRUE
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
name|AsyncCallback
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
name|impl
operator|.
name|DefaultAsyncProducer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|types
operator|.
name|builtin
operator|.
name|ExpandedNodeId
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|MiloClientProducer
specifier|public
class|class
name|MiloClientProducer
extends|extends
name|DefaultAsyncProducer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MiloClientProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|connection
specifier|private
specifier|final
name|MiloClientConnection
name|connection
decl_stmt|;
DECL|field|nodeId
specifier|private
specifier|final
name|ExpandedNodeId
name|nodeId
decl_stmt|;
DECL|field|methodId
specifier|private
specifier|final
name|ExpandedNodeId
name|methodId
decl_stmt|;
DECL|field|defaultAwaitWrites
specifier|private
specifier|final
name|boolean
name|defaultAwaitWrites
decl_stmt|;
DECL|method|MiloClientProducer (final MiloClientEndpoint endpoint, final MiloClientConnection connection, final boolean defaultAwaitWrites)
specifier|public
name|MiloClientProducer
parameter_list|(
specifier|final
name|MiloClientEndpoint
name|endpoint
parameter_list|,
specifier|final
name|MiloClientConnection
name|connection
parameter_list|,
specifier|final
name|boolean
name|defaultAwaitWrites
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|connection
operator|=
name|connection
expr_stmt|;
name|this
operator|.
name|defaultAwaitWrites
operator|=
name|defaultAwaitWrites
expr_stmt|;
name|this
operator|.
name|nodeId
operator|=
name|endpoint
operator|.
name|getNodeId
argument_list|()
expr_stmt|;
name|this
operator|.
name|methodId
operator|=
name|endpoint
operator|.
name|getMethodId
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange, AsyncCallback async)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|async
parameter_list|)
block|{
specifier|final
name|Message
name|msg
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
specifier|final
name|Object
name|value
init|=
name|msg
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Processing message: {}"
argument_list|,
name|value
argument_list|)
expr_stmt|;
specifier|final
name|CompletableFuture
argument_list|<
name|?
argument_list|>
name|future
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|methodId
operator|==
literal|null
condition|)
block|{
name|future
operator|=
name|this
operator|.
name|connection
operator|.
name|writeValue
argument_list|(
name|this
operator|.
name|nodeId
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|future
operator|=
name|this
operator|.
name|connection
operator|.
name|call
argument_list|(
name|this
operator|.
name|nodeId
argument_list|,
name|this
operator|.
name|methodId
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Boolean
name|await
init|=
name|msg
operator|.
name|getHeader
argument_list|(
literal|"await"
argument_list|,
name|this
operator|.
name|defaultAwaitWrites
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|TRUE
operator|.
name|equals
argument_list|(
name|await
argument_list|)
condition|)
block|{
name|future
operator|.
name|whenComplete
argument_list|(
parameter_list|(
name|v
parameter_list|,
name|ex
parameter_list|)
lambda|->
name|async
operator|.
name|done
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
else|else
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
end_class

end_unit

