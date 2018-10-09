begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.jaxrs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|jaxrs
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
name|Suspendable
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
name|cxf
operator|.
name|interceptors
operator|.
name|UnitOfWorkCloserInterceptor
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
name|cxf
operator|.
name|util
operator|.
name|CxfUtils
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
name|DefaultConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Server
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|JAXRSServerFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|phase
operator|.
name|Phase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|MessageObserver
import|;
end_import

begin_comment
comment|/**  * A Consumer of exchanges for a JAXRS service in CXF.  CxfRsConsumer acts a CXF  * service to receive REST requests, convert them to a normal java object invocation,  * and forward them to Camel route for processing.   * It is also responsible for converting and sending back responses to CXF client.   */
end_comment

begin_class
DECL|class|CxfRsConsumer
specifier|public
class|class
name|CxfRsConsumer
extends|extends
name|DefaultConsumer
implements|implements
name|Suspendable
block|{
DECL|field|server
specifier|private
name|Server
name|server
decl_stmt|;
DECL|method|CxfRsConsumer (CxfRsEndpoint endpoint, Processor processor)
specifier|public
name|CxfRsConsumer
parameter_list|(
name|CxfRsEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|server
operator|=
name|createServer
argument_list|()
expr_stmt|;
block|}
DECL|method|createServer ()
specifier|protected
name|Server
name|createServer
parameter_list|()
block|{
name|CxfRsEndpoint
name|endpoint
init|=
operator|(
name|CxfRsEndpoint
operator|)
name|getEndpoint
argument_list|()
decl_stmt|;
name|CxfRsInvoker
name|cxfRsInvoker
init|=
operator|new
name|CxfRsInvoker
argument_list|(
name|endpoint
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|JAXRSServerFactoryBean
name|svrBean
init|=
name|endpoint
operator|.
name|createJAXRSServerFactoryBean
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|endpoint
operator|.
name|getBus
argument_list|()
decl_stmt|;
comment|// We need to apply the bus setting from the CxfRsEndpoint which does not use the default bus
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|svrBean
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
name|svrBean
operator|.
name|setInvoker
argument_list|(
name|cxfRsInvoker
argument_list|)
expr_stmt|;
comment|// setup the UnitOfWorkCloserInterceptor for OneWayMessageProcessor
name|svrBean
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|UnitOfWorkCloserInterceptor
argument_list|(
name|Phase
operator|.
name|POST_INVOKE
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
comment|// close the UnitOfWork normally
name|svrBean
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|UnitOfWorkCloserInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|Server
name|server
init|=
name|svrBean
operator|.
name|create
argument_list|()
decl_stmt|;
specifier|final
name|MessageObserver
name|originalOutFaultObserver
init|=
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getOutFaultObserver
argument_list|()
decl_stmt|;
comment|//proxy OutFaultObserver so we can close org.apache.camel.spi.UnitOfWork in case of error
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|setOutFaultObserver
argument_list|(
name|message
lambda|->
block|{
name|CxfUtils
operator|.
name|closeCamelUnitOfWork
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|originalOutFaultObserver
operator|.
name|onMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
return|return
name|server
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
if|if
condition|(
name|server
operator|==
literal|null
condition|)
block|{
name|server
operator|=
name|createServer
argument_list|()
expr_stmt|;
block|}
name|server
operator|.
name|start
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
if|if
condition|(
name|server
operator|!=
literal|null
condition|)
block|{
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
name|server
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|server
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
DECL|method|getServer ()
specifier|public
name|Server
name|getServer
parameter_list|()
block|{
return|return
name|server
return|;
block|}
block|}
end_class

end_unit

