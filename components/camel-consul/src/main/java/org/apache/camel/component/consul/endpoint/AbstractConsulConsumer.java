begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.consul.endpoint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|consul
operator|.
name|endpoint
package|;
end_package

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigInteger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicReference
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|Consul
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
name|Processor
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
name|component
operator|.
name|consul
operator|.
name|ConsulConfiguration
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
name|component
operator|.
name|consul
operator|.
name|ConsulConstants
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
name|component
operator|.
name|consul
operator|.
name|ConsulEndpoint
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
name|support
operator|.
name|DefaultConsumer
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
name|ObjectHelper
import|;
end_import

begin_class
DECL|class|AbstractConsulConsumer
specifier|abstract
class|class
name|AbstractConsulConsumer
parameter_list|<
name|C
parameter_list|>
extends|extends
name|DefaultConsumer
block|{
DECL|field|endpoint
specifier|protected
specifier|final
name|ConsulEndpoint
name|endpoint
decl_stmt|;
DECL|field|configuration
specifier|protected
specifier|final
name|ConsulConfiguration
name|configuration
decl_stmt|;
DECL|field|key
specifier|protected
specifier|final
name|String
name|key
decl_stmt|;
DECL|field|index
specifier|protected
specifier|final
name|AtomicReference
argument_list|<
name|BigInteger
argument_list|>
name|index
decl_stmt|;
DECL|field|clientSupplier
specifier|private
specifier|final
name|Function
argument_list|<
name|Consul
argument_list|,
name|C
argument_list|>
name|clientSupplier
decl_stmt|;
DECL|field|watcher
specifier|private
name|Runnable
name|watcher
decl_stmt|;
DECL|method|AbstractConsulConsumer (ConsulEndpoint endpoint, ConsulConfiguration configuration, Processor processor, Function<Consul, C> clientSupplier)
specifier|protected
name|AbstractConsulConsumer
parameter_list|(
name|ConsulEndpoint
name|endpoint
parameter_list|,
name|ConsulConfiguration
name|configuration
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|Function
argument_list|<
name|Consul
argument_list|,
name|C
argument_list|>
name|clientSupplier
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|key
operator|=
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|configuration
operator|.
name|getKey
argument_list|()
argument_list|,
name|ConsulConstants
operator|.
name|CONSUL_KEY
argument_list|)
expr_stmt|;
name|this
operator|.
name|index
operator|=
operator|new
name|AtomicReference
argument_list|<>
argument_list|(
name|configuration
operator|.
name|getFirstIndex
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|clientSupplier
operator|=
name|clientSupplier
expr_stmt|;
name|this
operator|.
name|watcher
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|watcher
operator|=
name|createWatcher
argument_list|(
name|clientSupplier
operator|.
name|apply
argument_list|(
name|endpoint
operator|.
name|getConsul
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|watcher
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|watcher
operator|=
literal|null
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
comment|// *************************************************************************
comment|//
comment|// *************************************************************************
DECL|method|createWatcher (C client)
specifier|protected
specifier|abstract
name|Runnable
name|createWatcher
parameter_list|(
name|C
name|client
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|// *************************************************************************
comment|// Handlers
comment|// *************************************************************************
DECL|class|AbstractWatcher
specifier|protected
specifier|abstract
class|class
name|AbstractWatcher
implements|implements
name|Runnable
block|{
DECL|field|client
specifier|private
specifier|final
name|C
name|client
decl_stmt|;
DECL|method|AbstractWatcher (C client)
specifier|public
name|AbstractWatcher
parameter_list|(
name|C
name|client
parameter_list|)
block|{
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
block|}
DECL|method|onError (Throwable throwable)
specifier|protected
name|void
name|onError
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
if|if
condition|(
name|isRunAllowed
argument_list|()
condition|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error watching for event "
operator|+
name|key
argument_list|,
name|throwable
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setIndex (BigInteger responseIndex)
specifier|protected
specifier|final
name|void
name|setIndex
parameter_list|(
name|BigInteger
name|responseIndex
parameter_list|)
block|{
name|index
operator|.
name|set
argument_list|(
name|responseIndex
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
specifier|final
name|void
name|run
parameter_list|()
block|{
if|if
condition|(
name|isRunAllowed
argument_list|()
condition|)
block|{
name|watch
argument_list|(
name|client
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|client ()
specifier|protected
specifier|final
name|C
name|client
parameter_list|()
block|{
return|return
name|client
return|;
block|}
DECL|method|watch ()
specifier|protected
specifier|final
name|void
name|watch
parameter_list|()
block|{
name|watch
argument_list|(
name|client
argument_list|)
expr_stmt|;
block|}
DECL|method|watch (C client)
specifier|protected
specifier|abstract
name|void
name|watch
parameter_list|(
name|C
name|client
parameter_list|)
function_decl|;
block|}
block|}
end_class

end_unit

