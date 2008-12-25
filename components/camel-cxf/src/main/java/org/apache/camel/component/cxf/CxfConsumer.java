begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
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
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceProvider
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
name|cxf
operator|.
name|feature
operator|.
name|MessageDataFormatFeature
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
name|feature
operator|.
name|PayLoadDataFormatFeature
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
name|spring
operator|.
name|CxfEndpointBean
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
name|CxfEndpointUtils
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
name|BusFactory
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
name|bus
operator|.
name|spring
operator|.
name|SpringBusFactory
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
name|common
operator|.
name|classloader
operator|.
name|ClassLoaderUtils
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
name|frontend
operator|.
name|ServerFactoryBean
import|;
end_import

begin_comment
comment|/**  * A consumer of exchanges for a service in CXF  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|CxfConsumer
specifier|public
class|class
name|CxfConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|field|endpoint
specifier|private
name|CxfEndpoint
name|endpoint
decl_stmt|;
DECL|field|server
specifier|private
name|Server
name|server
decl_stmt|;
DECL|method|CxfConsumer (CxfEndpoint endpoint, Processor processor)
specifier|public
name|CxfConsumer
parameter_list|(
name|CxfEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|Bus
name|bus
init|=
literal|null
decl_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|boolean
name|isWebServiceProvider
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getApplicationContext
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|(
name|endpoint
operator|.
name|getApplicationContext
argument_list|()
argument_list|)
decl_stmt|;
name|bus
operator|=
name|bf
operator|.
name|createBus
argument_list|()
expr_stmt|;
if|if
condition|(
name|CxfEndpointUtils
operator|.
name|getSetDefaultBus
argument_list|(
name|endpoint
argument_list|)
condition|)
block|{
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// now we just use the default bus here
name|bus
operator|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
expr_stmt|;
block|}
name|ServerFactoryBean
name|svrBean
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|isSpringContextEndpoint
argument_list|()
condition|)
block|{
name|CxfEndpointBean
name|endpointBean
init|=
name|endpoint
operator|.
name|getCxfEndpointBean
argument_list|()
decl_stmt|;
name|CxfEndpointUtils
operator|.
name|checkServiceClass
argument_list|(
name|endpointBean
operator|.
name|getServiceClass
argument_list|()
argument_list|)
expr_stmt|;
name|svrBean
operator|=
name|CxfEndpointUtils
operator|.
name|getServerFactoryBean
argument_list|(
name|endpointBean
operator|.
name|getServiceClass
argument_list|()
argument_list|)
expr_stmt|;
name|isWebServiceProvider
operator|=
name|CxfEndpointUtils
operator|.
name|hasAnnotation
argument_list|(
name|endpointBean
operator|.
name|getServiceClass
argument_list|()
argument_list|,
name|WebServiceProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|configure
argument_list|(
name|svrBean
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// setup the serverFactoryBean with the URI parameters
name|CxfEndpointUtils
operator|.
name|checkServiceClassName
argument_list|(
name|endpoint
operator|.
name|getServiceClass
argument_list|()
argument_list|)
expr_stmt|;
name|Class
name|serviceClass
init|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|endpoint
operator|.
name|getServiceClass
argument_list|()
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|svrBean
operator|=
name|CxfEndpointUtils
operator|.
name|getServerFactoryBean
argument_list|(
name|serviceClass
argument_list|)
expr_stmt|;
name|isWebServiceProvider
operator|=
name|CxfEndpointUtils
operator|.
name|hasAnnotation
argument_list|(
name|serviceClass
argument_list|,
name|WebServiceProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|svrBean
operator|.
name|setAddress
argument_list|(
name|endpoint
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|svrBean
operator|.
name|setServiceClass
argument_list|(
name|serviceClass
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getWsdlURL
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|svrBean
operator|.
name|setWsdlURL
argument_list|(
name|endpoint
operator|.
name|getWsdlURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|CxfEndpointUtils
operator|.
name|getServiceName
argument_list|(
name|endpoint
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|svrBean
operator|.
name|setServiceName
argument_list|(
name|CxfEndpointUtils
operator|.
name|getServiceName
argument_list|(
name|endpoint
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|CxfEndpointUtils
operator|.
name|getServiceName
argument_list|(
name|endpoint
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|svrBean
operator|.
name|setEndpointName
argument_list|(
name|CxfEndpointUtils
operator|.
name|getPortName
argument_list|(
name|endpoint
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|DataFormat
name|dataFormat
init|=
name|CxfEndpointUtils
operator|.
name|getDataFormat
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|svrBean
operator|.
name|setInvoker
argument_list|(
operator|new
name|CamelInvoker
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
comment|// apply feature here
if|if
condition|(
operator|!
name|dataFormat
operator|.
name|equals
argument_list|(
name|DataFormat
operator|.
name|POJO
argument_list|)
operator|&&
operator|!
name|isWebServiceProvider
condition|)
block|{
if|if
condition|(
name|dataFormat
operator|.
name|equals
argument_list|(
name|DataFormat
operator|.
name|PAYLOAD
argument_list|)
condition|)
block|{
name|svrBean
operator|.
name|getFeatures
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|PayLoadDataFormatFeature
argument_list|()
argument_list|)
expr_stmt|;
comment|// adding the logging feature here for debug
comment|//features.add(new LoggingFeature());
block|}
elseif|else
if|if
condition|(
name|dataFormat
operator|.
name|equals
argument_list|(
name|DataFormat
operator|.
name|MESSAGE
argument_list|)
condition|)
block|{
name|svrBean
operator|.
name|getFeatures
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|MessageDataFormatFeature
argument_list|()
argument_list|)
expr_stmt|;
comment|//features.add(new LoggingFeature());
block|}
block|}
name|svrBean
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|svrBean
operator|.
name|setStart
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|server
operator|=
name|svrBean
operator|.
name|create
argument_list|()
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
DECL|method|getEndpoint ()
specifier|public
name|CxfEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
block|}
end_class

end_unit

