begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|Connector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|Server
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|nio
operator|.
name|SelectChannelConnector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|security
operator|.
name|SslSocketConnector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|servlet
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|servlet
operator|.
name|ServletHolder
import|;
end_import

begin_comment
comment|/**  * An HttpComponent which starts an embedded Jetty for to handle consuming from  * http endpoints.  *   * @version $Revision: 525142 $  */
end_comment

begin_class
DECL|class|JettyHttpComponent
specifier|public
class|class
name|JettyHttpComponent
extends|extends
name|HttpComponent
block|{
DECL|field|server
name|Server
name|server
decl_stmt|;
DECL|class|ConnectorRef
class|class
name|ConnectorRef
block|{
DECL|field|connector
name|Connector
name|connector
decl_stmt|;
DECL|field|refCount
name|int
name|refCount
decl_stmt|;
DECL|method|ConnectorRef (Connector connector)
specifier|public
name|ConnectorRef
parameter_list|(
name|Connector
name|connector
parameter_list|)
block|{
name|this
operator|.
name|connector
operator|=
name|connector
expr_stmt|;
name|increment
argument_list|()
expr_stmt|;
block|}
DECL|method|increment ()
specifier|public
name|int
name|increment
parameter_list|()
block|{
return|return
operator|++
name|refCount
return|;
block|}
DECL|method|decrement ()
specifier|public
name|int
name|decrement
parameter_list|()
block|{
return|return
operator|--
name|refCount
return|;
block|}
block|}
DECL|field|connectors
specifier|final
name|HashMap
argument_list|<
name|String
argument_list|,
name|ConnectorRef
argument_list|>
name|connectors
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|ConnectorRef
argument_list|>
argument_list|()
decl_stmt|;
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
name|server
operator|=
name|createServer
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
DECL|method|createServer ()
specifier|private
name|Server
name|createServer
parameter_list|()
throws|throws
name|Exception
block|{
name|setCamelServlet
argument_list|(
operator|new
name|CamelServlet
argument_list|()
argument_list|)
expr_stmt|;
name|Server
name|server
init|=
operator|new
name|Server
argument_list|()
decl_stmt|;
name|Context
name|context
init|=
operator|new
name|Context
argument_list|(
name|Context
operator|.
name|NO_SECURITY
operator||
name|Context
operator|.
name|NO_SESSIONS
argument_list|)
decl_stmt|;
name|context
operator|.
name|setContextPath
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
name|ServletHolder
name|holder
init|=
operator|new
name|ServletHolder
argument_list|()
decl_stmt|;
name|holder
operator|.
name|setServlet
argument_list|(
name|getCamelServlet
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addServlet
argument_list|(
name|holder
argument_list|,
literal|"/*"
argument_list|)
expr_stmt|;
name|server
operator|.
name|setHandler
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
return|return
name|server
return|;
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
for|for
control|(
name|ConnectorRef
name|connectorRef
range|:
name|connectors
operator|.
name|values
argument_list|()
control|)
block|{
name|connectorRef
operator|.
name|connector
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
name|connectors
operator|.
name|clear
argument_list|()
expr_stmt|;
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|connect (HttpConsumer consumer)
specifier|public
name|void
name|connect
parameter_list|(
name|HttpConsumer
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Make sure that there is a connector for the requested endpoint.
name|HttpEndpoint
name|endpoint
init|=
operator|(
name|HttpEndpoint
operator|)
name|consumer
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|String
name|connectorKey
init|=
name|endpoint
operator|.
name|getProtocol
argument_list|()
operator|+
literal|":"
operator|+
name|endpoint
operator|.
name|getPort
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|connectors
init|)
block|{
name|ConnectorRef
name|connectorRef
init|=
name|connectors
operator|.
name|get
argument_list|(
name|connectorKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|connectorRef
operator|==
literal|null
condition|)
block|{
name|Connector
name|connector
decl_stmt|;
if|if
condition|(
literal|"https"
operator|.
name|equals
argument_list|(
name|endpoint
operator|.
name|getProtocol
argument_list|()
argument_list|)
condition|)
block|{
name|connector
operator|=
operator|new
name|SslSocketConnector
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|connector
operator|=
operator|new
name|SelectChannelConnector
argument_list|()
expr_stmt|;
block|}
name|connector
operator|.
name|setPort
argument_list|(
name|endpoint
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|server
operator|.
name|addConnector
argument_list|(
name|connector
argument_list|)
expr_stmt|;
name|connector
operator|.
name|start
argument_list|()
expr_stmt|;
name|connectorRef
operator|=
operator|new
name|ConnectorRef
argument_list|(
name|connector
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// ref track the connector
name|connectorRef
operator|.
name|increment
argument_list|()
expr_stmt|;
block|}
block|}
name|super
operator|.
name|connect
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|disconnect (HttpConsumer consumer)
specifier|public
name|void
name|disconnect
parameter_list|(
name|HttpConsumer
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{
name|super
operator|.
name|disconnect
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
comment|// If the connector is not needed anymore.. then stop it.
name|HttpEndpoint
name|endpoint
init|=
operator|(
name|HttpEndpoint
operator|)
name|consumer
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|String
name|connectorKey
init|=
name|endpoint
operator|.
name|getProtocol
argument_list|()
operator|+
literal|":"
operator|+
name|endpoint
operator|.
name|getPort
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|connectors
init|)
block|{
name|ConnectorRef
name|connectorRef
init|=
name|connectors
operator|.
name|get
argument_list|(
name|connectorKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|connectorRef
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|connectorRef
operator|.
name|decrement
argument_list|()
operator|==
literal|0
condition|)
block|{
name|server
operator|.
name|removeConnector
argument_list|(
name|connectorRef
operator|.
name|connector
argument_list|)
expr_stmt|;
name|connectorRef
operator|.
name|connector
operator|.
name|stop
argument_list|()
expr_stmt|;
name|connectors
operator|.
name|remove
argument_list|(
name|connectorKey
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

