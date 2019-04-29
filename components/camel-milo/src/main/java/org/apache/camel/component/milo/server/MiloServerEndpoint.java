begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.milo.server
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
name|server
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
name|Component
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
name|server
operator|.
name|internal
operator|.
name|CamelNamespace
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
name|server
operator|.
name|internal
operator|.
name|CamelServerItem
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
name|DefaultEndpoint
import|;
end_import

begin_comment
comment|/**  * Make telemetry data available as an OPC UA server  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.19.0"
argument_list|,
name|scheme
operator|=
literal|"milo-server"
argument_list|,
name|syntax
operator|=
literal|"milo-server:itemId"
argument_list|,
name|title
operator|=
literal|"OPC UA Server"
argument_list|,
name|label
operator|=
literal|"iot"
argument_list|)
DECL|class|MiloServerEndpoint
specifier|public
class|class
name|MiloServerEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|itemId
specifier|private
name|String
name|itemId
decl_stmt|;
DECL|field|namespace
specifier|private
specifier|final
name|CamelNamespace
name|namespace
decl_stmt|;
DECL|field|item
specifier|private
name|CamelServerItem
name|item
decl_stmt|;
DECL|method|MiloServerEndpoint (final String uri, final String itemId, final CamelNamespace namespace, final Component component)
specifier|public
name|MiloServerEndpoint
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|,
specifier|final
name|String
name|itemId
parameter_list|,
specifier|final
name|CamelNamespace
name|namespace
parameter_list|,
specifier|final
name|Component
name|component
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
name|itemId
operator|=
name|itemId
expr_stmt|;
name|this
operator|.
name|namespace
operator|=
name|namespace
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
name|this
operator|.
name|item
operator|=
name|this
operator|.
name|namespace
operator|.
name|getOrAddItem
argument_list|(
name|this
operator|.
name|itemId
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
if|if
condition|(
name|this
operator|.
name|item
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|item
operator|.
name|dispose
argument_list|()
expr_stmt|;
name|this
operator|.
name|item
operator|=
literal|null
expr_stmt|;
block|}
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
name|MiloServerProducer
argument_list|(
name|this
argument_list|,
name|this
operator|.
name|item
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
name|MiloServerConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|this
operator|.
name|item
argument_list|)
return|;
block|}
comment|/**      * ID of the item      *      * @param itemId the new ID of the item      */
DECL|method|setItemId (final String itemId)
specifier|public
name|void
name|setItemId
parameter_list|(
specifier|final
name|String
name|itemId
parameter_list|)
block|{
name|this
operator|.
name|itemId
operator|=
name|itemId
expr_stmt|;
block|}
comment|/**      * Get the ID of the item      *      * @return the ID of the item      */
DECL|method|getItemId ()
specifier|public
name|String
name|getItemId
parameter_list|()
block|{
return|return
name|this
operator|.
name|itemId
return|;
block|}
block|}
end_class

end_unit

