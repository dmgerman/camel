begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.websocket.jsr356
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|websocket
operator|.
name|jsr356
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
name|support
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

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.23.0"
argument_list|,
name|scheme
operator|=
literal|"websocket-jsr356"
argument_list|,
name|title
operator|=
literal|"Javax Websocket"
argument_list|,
name|syntax
operator|=
literal|"websocket-jsr356:/resourceUri"
argument_list|,
name|consumerClass
operator|=
name|JSR356Consumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"jsr356"
argument_list|)
DECL|class|JSR356Endpoint
specifier|public
class|class
name|JSR356Endpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"If a path (/foo) it will deploy locally the endpoint, "
operator|+
literal|"if an uri it will connect to the corresponding server"
argument_list|)
DECL|field|websocketPathOrUri
specifier|private
name|String
name|websocketPathOrUri
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Used when the endpoint is in client mode to populate a pool of sessions"
argument_list|)
DECL|field|sessionCount
specifier|private
name|int
name|sessionCount
init|=
literal|1
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"the servlet context to use (represented by its path)"
argument_list|)
DECL|field|context
specifier|private
name|String
name|context
decl_stmt|;
DECL|field|component
specifier|private
specifier|final
name|JSR356WebSocketComponent
name|component
decl_stmt|;
DECL|method|JSR356Endpoint (final JSR356WebSocketComponent component, final String uri)
specifier|public
name|JSR356Endpoint
parameter_list|(
specifier|final
name|JSR356WebSocketComponent
name|component
parameter_list|,
specifier|final
name|String
name|uri
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
name|component
operator|=
name|component
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|JSR356WebSocketComponent
name|getComponent
parameter_list|()
block|{
return|return
name|JSR356WebSocketComponent
operator|.
name|class
operator|.
name|cast
argument_list|(
name|super
operator|.
name|getComponent
argument_list|()
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
block|{
return|return
operator|new
name|JSR356Consumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|sessionCount
argument_list|,
name|context
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
block|{
return|return
operator|new
name|JSR356Producer
argument_list|(
name|this
argument_list|,
name|sessionCount
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
DECL|method|getSessionCount ()
specifier|public
name|int
name|getSessionCount
parameter_list|()
block|{
return|return
name|sessionCount
return|;
block|}
DECL|method|setSessionCount (final int sessionCount)
specifier|public
name|void
name|setSessionCount
parameter_list|(
specifier|final
name|int
name|sessionCount
parameter_list|)
block|{
name|this
operator|.
name|sessionCount
operator|=
name|sessionCount
expr_stmt|;
block|}
block|}
end_class

end_unit

