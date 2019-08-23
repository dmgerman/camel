begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.iec60870
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|iec60870
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
name|atomic
operator|.
name|AtomicReference
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
name|iec60870
operator|.
name|AbstractConnectionMultiplexor
operator|.
name|Handle
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
name|iec60870
operator|.
name|client
operator|.
name|ClientOptions
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
name|spi
operator|.
name|Metadata
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
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriPath
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
name|DefaultComponent
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
name|DefaultEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|neoscada
operator|.
name|protocol
operator|.
name|iec60870
operator|.
name|ProtocolOptions
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|neoscada
operator|.
name|protocol
operator|.
name|iec60870
operator|.
name|client
operator|.
name|data
operator|.
name|DataModuleOptions
import|;
end_import

begin_class
DECL|class|AbstractIecEndpoint
specifier|public
specifier|abstract
class|class
name|AbstractIecEndpoint
parameter_list|<
name|T
extends|extends
name|AbstractConnectionMultiplexor
parameter_list|>
extends|extends
name|DefaultEndpoint
block|{
comment|/**      * The object information address      */
annotation|@
name|UriPath
argument_list|(
name|name
operator|=
literal|"uriPath"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|address
specifier|private
specifier|final
name|ObjectAddress
name|address
decl_stmt|;
comment|// dummy for doc generation
comment|/**      * A full set of connection options      */
annotation|@
name|UriParam
DECL|field|connectionOptions
specifier|private
name|ClientOptions
name|connectionOptions
decl_stmt|;
comment|// dummy for doc generation
comment|/**      * A set of protocol options      */
annotation|@
name|UriParam
DECL|field|protocolOptions
specifier|private
name|ProtocolOptions
name|protocolOptions
decl_stmt|;
comment|// dummy for doc generation
comment|/**      * A set of data module options      */
annotation|@
name|UriParam
DECL|field|dataModuleOptions
specifier|private
name|DataModuleOptions
name|dataModuleOptions
decl_stmt|;
comment|// dummy for doc generation
comment|/**      * An identifier grouping connection instances      */
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"id"
argument_list|)
DECL|field|connectionId
specifier|private
name|String
name|connectionId
decl_stmt|;
DECL|field|connection
specifier|private
specifier|final
name|T
name|connection
decl_stmt|;
DECL|field|connectionHandle
specifier|private
specifier|final
name|AtomicReference
argument_list|<
name|Handle
argument_list|>
name|connectionHandle
init|=
operator|new
name|AtomicReference
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|AbstractIecEndpoint (final String uri, final DefaultComponent component, final T connection, final ObjectAddress address)
specifier|public
name|AbstractIecEndpoint
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|,
specifier|final
name|DefaultComponent
name|component
parameter_list|,
specifier|final
name|T
name|connection
parameter_list|,
specifier|final
name|ObjectAddress
name|address
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|connection
operator|=
name|requireNonNull
argument_list|(
name|connection
argument_list|)
expr_stmt|;
name|this
operator|.
name|address
operator|=
name|requireNonNull
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
DECL|method|getConnectionOptions ()
specifier|public
name|ClientOptions
name|getConnectionOptions
parameter_list|()
block|{
return|return
name|connectionOptions
return|;
block|}
DECL|method|getProtocolOptions ()
specifier|public
name|ProtocolOptions
name|getProtocolOptions
parameter_list|()
block|{
return|return
name|protocolOptions
return|;
block|}
DECL|method|getDataModuleOptions ()
specifier|public
name|DataModuleOptions
name|getDataModuleOptions
parameter_list|()
block|{
return|return
name|dataModuleOptions
return|;
block|}
DECL|method|getConnectionId ()
specifier|public
name|String
name|getConnectionId
parameter_list|()
block|{
return|return
name|connectionId
return|;
block|}
DECL|method|setConnectionId (String connectionId)
specifier|public
name|void
name|setConnectionId
parameter_list|(
name|String
name|connectionId
parameter_list|)
block|{
name|this
operator|.
name|connectionId
operator|=
name|connectionId
expr_stmt|;
block|}
DECL|method|getAddress ()
specifier|public
name|ObjectAddress
name|getAddress
parameter_list|()
block|{
return|return
name|this
operator|.
name|address
return|;
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
name|this
operator|.
name|connectionHandle
operator|.
name|set
argument_list|(
name|this
operator|.
name|connection
operator|.
name|register
argument_list|()
argument_list|)
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
specifier|final
name|Handle
name|connectionHandle
init|=
name|this
operator|.
name|connectionHandle
operator|.
name|getAndSet
argument_list|(
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|connectionHandle
operator|!=
literal|null
condition|)
block|{
name|connectionHandle
operator|.
name|unregister
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|getConnection ()
specifier|protected
name|T
name|getConnection
parameter_list|()
block|{
return|return
name|this
operator|.
name|connection
return|;
block|}
block|}
end_class

end_unit

