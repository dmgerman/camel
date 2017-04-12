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
name|Objects
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
name|Consumer
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
name|Producer
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
name|NamespaceId
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
name|PartialNodeId
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
name|UriEndpoint
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

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"milo-client"
argument_list|,
name|syntax
operator|=
literal|"milo-client:tcp://user:password@host:port/path/to/service?itemId=item.id&namespaceUri=urn:foo:bar"
argument_list|,
name|title
operator|=
literal|"Milo based OPC UA Client"
argument_list|,
name|consumerClass
operator|=
name|MiloClientConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"iot"
argument_list|)
DECL|class|MiloClientEndpoint
specifier|public
class|class
name|MiloClientEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|MiloClientItemConfiguration
block|{
comment|/** 	 * The OPC UA server endpoint 	 */
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|endpointUri
specifier|private
specifier|final
name|String
name|endpointUri
decl_stmt|;
comment|/** 	 * The node definition (see Node ID) 	 */
annotation|@
name|UriParam
DECL|field|node
specifier|private
name|ExpandedNodeId
name|node
decl_stmt|;
comment|/** 	 * The sampling interval in milliseconds 	 */
annotation|@
name|UriParam
DECL|field|samplingInterval
specifier|private
name|Double
name|samplingInterval
decl_stmt|;
comment|/** 	 * The client configuration 	 */
annotation|@
name|UriParam
DECL|field|client
specifier|private
name|MiloClientConfiguration
name|client
decl_stmt|;
comment|/** 	 * Default "await" setting for writes 	 */
annotation|@
name|UriParam
DECL|field|defaultAwaitWrites
specifier|private
name|boolean
name|defaultAwaitWrites
init|=
literal|false
decl_stmt|;
DECL|field|connection
specifier|private
specifier|final
name|MiloClientConnection
name|connection
decl_stmt|;
DECL|field|component
specifier|private
specifier|final
name|MiloClientComponent
name|component
decl_stmt|;
DECL|method|MiloClientEndpoint (final String uri, final MiloClientComponent component, final MiloClientConnection connection, final String endpointUri)
specifier|public
name|MiloClientEndpoint
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|,
specifier|final
name|MiloClientComponent
name|component
parameter_list|,
specifier|final
name|MiloClientConnection
name|connection
parameter_list|,
specifier|final
name|String
name|endpointUri
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|component
argument_list|)
expr_stmt|;
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|connection
argument_list|)
expr_stmt|;
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpointUri
operator|=
name|endpointUri
expr_stmt|;
name|this
operator|.
name|component
operator|=
name|component
expr_stmt|;
name|this
operator|.
name|connection
operator|=
name|connection
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
name|this
operator|.
name|component
operator|.
name|disposed
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|MiloClientProducer
argument_list|(
name|this
argument_list|,
name|this
operator|.
name|connection
argument_list|,
name|this
argument_list|,
name|this
operator|.
name|defaultAwaitWrites
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (final Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
specifier|final
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|MiloClientConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|this
operator|.
name|connection
argument_list|,
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getConnection ()
specifier|public
name|MiloClientConnection
name|getConnection
parameter_list|()
block|{
return|return
name|this
operator|.
name|connection
return|;
block|}
comment|// item configuration
annotation|@
name|Override
DECL|method|makePartialNodeId ()
specifier|public
name|PartialNodeId
name|makePartialNodeId
parameter_list|()
block|{
name|PartialNodeId
name|result
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|node
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
name|PartialNodeId
operator|.
name|fromExpandedNodeId
argument_list|(
name|this
operator|.
name|node
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Missing or invalid node id configuration"
argument_list|)
throw|;
block|}
else|else
block|{
return|return
name|result
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|makeNamespaceId ()
specifier|public
name|NamespaceId
name|makeNamespaceId
parameter_list|()
block|{
name|NamespaceId
name|result
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|node
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
name|NamespaceId
operator|.
name|fromExpandedNodeId
argument_list|(
name|this
operator|.
name|node
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Missing or invalid node id configuration"
argument_list|)
throw|;
block|}
else|else
block|{
return|return
name|result
return|;
block|}
block|}
DECL|method|setNode (final String node)
specifier|public
name|void
name|setNode
parameter_list|(
specifier|final
name|String
name|node
parameter_list|)
block|{
if|if
condition|(
name|node
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|node
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|node
operator|=
name|ExpandedNodeId
operator|.
name|parse
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getNode ()
specifier|public
name|String
name|getNode
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|node
operator|!=
literal|null
condition|)
block|{
return|return
name|this
operator|.
name|node
operator|.
name|toParseableString
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getSamplingInterval ()
specifier|public
name|Double
name|getSamplingInterval
parameter_list|()
block|{
return|return
name|this
operator|.
name|samplingInterval
return|;
block|}
DECL|method|setSamplingInterval (final Double samplingInterval)
specifier|public
name|void
name|setSamplingInterval
parameter_list|(
specifier|final
name|Double
name|samplingInterval
parameter_list|)
block|{
name|this
operator|.
name|samplingInterval
operator|=
name|samplingInterval
expr_stmt|;
block|}
DECL|method|isDefaultAwaitWrites ()
specifier|public
name|boolean
name|isDefaultAwaitWrites
parameter_list|()
block|{
return|return
name|this
operator|.
name|defaultAwaitWrites
return|;
block|}
DECL|method|setDefaultAwaitWrites (final boolean defaultAwaitWrites)
specifier|public
name|void
name|setDefaultAwaitWrites
parameter_list|(
specifier|final
name|boolean
name|defaultAwaitWrites
parameter_list|)
block|{
name|this
operator|.
name|defaultAwaitWrites
operator|=
name|defaultAwaitWrites
expr_stmt|;
block|}
block|}
end_class

end_unit

