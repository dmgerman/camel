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
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Consumer
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
name|milo
operator|.
name|client
operator|.
name|internal
operator|.
name|SubscriptionManager
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
name|Stack
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
name|DataValue
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
name|StatusCode
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
name|Variant
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
name|unsigned
operator|.
name|UInteger
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
name|structured
operator|.
name|CallMethodResult
import|;
end_import

begin_class
DECL|class|MiloClientConnection
specifier|public
class|class
name|MiloClientConnection
implements|implements
name|AutoCloseable
block|{
DECL|field|configuration
specifier|private
specifier|final
name|MiloClientConfiguration
name|configuration
decl_stmt|;
DECL|field|manager
specifier|private
name|SubscriptionManager
name|manager
decl_stmt|;
DECL|field|initialized
specifier|private
name|boolean
name|initialized
decl_stmt|;
DECL|method|MiloClientConnection (final MiloClientConfiguration configuration)
specifier|public
name|MiloClientConnection
parameter_list|(
specifier|final
name|MiloClientConfiguration
name|configuration
parameter_list|)
block|{
name|requireNonNull
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
comment|// make a copy since the configuration is mutable
name|this
operator|.
name|configuration
operator|=
name|configuration
operator|.
name|clone
argument_list|()
expr_stmt|;
block|}
DECL|method|init ()
specifier|protected
name|void
name|init
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|manager
operator|=
operator|new
name|SubscriptionManager
argument_list|(
name|this
operator|.
name|configuration
argument_list|,
name|Stack
operator|.
name|sharedScheduledExecutor
argument_list|()
argument_list|,
literal|10_000
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|this
operator|.
name|manager
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|manager
operator|.
name|dispose
argument_list|()
expr_stmt|;
name|this
operator|.
name|manager
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|checkInit ()
specifier|protected
specifier|synchronized
name|void
name|checkInit
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|initialized
condition|)
block|{
return|return;
block|}
try|try
block|{
name|init
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|this
operator|.
name|initialized
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|FunctionalInterface
DECL|interface|MonitorHandle
specifier|public
interface|interface
name|MonitorHandle
block|{
DECL|method|unregister ()
name|void
name|unregister
parameter_list|()
function_decl|;
block|}
DECL|method|monitorValue (final ExpandedNodeId nodeId, Double samplingInterval, final Consumer<DataValue> valueConsumer)
specifier|public
name|MonitorHandle
name|monitorValue
parameter_list|(
specifier|final
name|ExpandedNodeId
name|nodeId
parameter_list|,
name|Double
name|samplingInterval
parameter_list|,
specifier|final
name|Consumer
argument_list|<
name|DataValue
argument_list|>
name|valueConsumer
parameter_list|)
block|{
name|requireNonNull
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|requireNonNull
argument_list|(
name|valueConsumer
argument_list|)
expr_stmt|;
name|checkInit
argument_list|()
expr_stmt|;
specifier|final
name|UInteger
name|handle
init|=
name|this
operator|.
name|manager
operator|.
name|registerItem
argument_list|(
name|nodeId
argument_list|,
name|samplingInterval
argument_list|,
name|valueConsumer
argument_list|)
decl_stmt|;
return|return
parameter_list|()
lambda|->
name|MiloClientConnection
operator|.
name|this
operator|.
name|manager
operator|.
name|unregisterItem
argument_list|(
name|handle
argument_list|)
return|;
block|}
DECL|method|getConnectionId ()
specifier|public
name|String
name|getConnectionId
parameter_list|()
block|{
return|return
name|this
operator|.
name|configuration
operator|.
name|toCacheId
argument_list|()
return|;
block|}
DECL|method|writeValue (final ExpandedNodeId nodeId, final Object value)
specifier|public
name|CompletableFuture
argument_list|<
name|?
argument_list|>
name|writeValue
parameter_list|(
specifier|final
name|ExpandedNodeId
name|nodeId
parameter_list|,
specifier|final
name|Object
name|value
parameter_list|)
block|{
name|checkInit
argument_list|()
expr_stmt|;
return|return
name|this
operator|.
name|manager
operator|.
name|write
argument_list|(
name|nodeId
argument_list|,
name|mapWriteValue
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
DECL|method|call (final ExpandedNodeId nodeId, final ExpandedNodeId methodId, final Object value)
specifier|public
name|CompletableFuture
argument_list|<
name|CallMethodResult
argument_list|>
name|call
parameter_list|(
specifier|final
name|ExpandedNodeId
name|nodeId
parameter_list|,
specifier|final
name|ExpandedNodeId
name|methodId
parameter_list|,
specifier|final
name|Object
name|value
parameter_list|)
block|{
name|checkInit
argument_list|()
expr_stmt|;
return|return
name|this
operator|.
name|manager
operator|.
name|call
argument_list|(
name|nodeId
argument_list|,
name|methodId
argument_list|,
name|mapCallValue
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Map the incoming value to some value callable to the milo client      *      * @param value the incoming value      * @return the outgoing call request      */
DECL|method|mapCallValue (final Object value)
specifier|private
name|Variant
index|[]
name|mapCallValue
parameter_list|(
specifier|final
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|Variant
index|[
literal|0
index|]
return|;
block|}
if|if
condition|(
name|value
operator|instanceof
name|Variant
index|[]
condition|)
block|{
return|return
operator|(
name|Variant
index|[]
operator|)
name|value
return|;
block|}
if|if
condition|(
name|value
operator|instanceof
name|Variant
condition|)
block|{
return|return
operator|new
name|Variant
index|[]
block|{
operator|(
name|Variant
operator|)
name|value
block|}
return|;
block|}
return|return
operator|new
name|Variant
index|[]
block|{
operator|new
name|Variant
argument_list|(
name|value
argument_list|)
block|}
return|;
block|}
comment|/**      * Map the incoming value to some value writable to the milo client      *      * @param value the incoming value      * @return the outgoing value      */
DECL|method|mapWriteValue (final Object value)
specifier|private
name|DataValue
name|mapWriteValue
parameter_list|(
specifier|final
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|DataValue
condition|)
block|{
return|return
operator|(
name|DataValue
operator|)
name|value
return|;
block|}
if|if
condition|(
name|value
operator|instanceof
name|Variant
condition|)
block|{
return|return
operator|new
name|DataValue
argument_list|(
operator|(
name|Variant
operator|)
name|value
argument_list|,
name|StatusCode
operator|.
name|GOOD
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
return|return
operator|new
name|DataValue
argument_list|(
operator|new
name|Variant
argument_list|(
name|value
argument_list|)
argument_list|,
name|StatusCode
operator|.
name|GOOD
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
end_class

end_unit

