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
name|ExchangePattern
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
name|HeaderFilterStrategy
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
name|spring
operator|.
name|SpringCamelContext
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
name|configuration
operator|.
name|spring
operator|.
name|ConfigurerImpl
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
name|message
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_comment
comment|/**  * Defines the<a href="http://activemq.apache.org/camel/cxf.html">CXF Endpoint</a>  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|CxfEndpoint
specifier|public
class|class
name|CxfEndpoint
extends|extends
name|DefaultEndpoint
argument_list|<
name|CxfExchange
argument_list|>
block|{
DECL|field|component
specifier|private
specifier|final
name|CxfComponent
name|component
decl_stmt|;
DECL|field|address
specifier|private
specifier|final
name|String
name|address
decl_stmt|;
DECL|field|wsdlURL
specifier|private
name|String
name|wsdlURL
decl_stmt|;
DECL|field|serviceClass
specifier|private
name|String
name|serviceClass
decl_stmt|;
DECL|field|portName
specifier|private
name|String
name|portName
decl_stmt|;
DECL|field|serviceName
specifier|private
name|String
name|serviceName
decl_stmt|;
DECL|field|dataFormat
specifier|private
name|String
name|dataFormat
decl_stmt|;
DECL|field|beanId
specifier|private
name|String
name|beanId
decl_stmt|;
DECL|field|isWrapped
specifier|private
name|boolean
name|isWrapped
decl_stmt|;
DECL|field|isSpringContextEndpoint
specifier|private
name|boolean
name|isSpringContextEndpoint
decl_stmt|;
DECL|field|inOut
specifier|private
name|boolean
name|inOut
init|=
literal|true
decl_stmt|;
DECL|field|isSetDefaultBus
specifier|private
name|Boolean
name|isSetDefaultBus
decl_stmt|;
DECL|field|configurer
specifier|private
name|ConfigurerImpl
name|configurer
decl_stmt|;
DECL|field|cxfEndpointBean
specifier|private
name|CxfEndpointBean
name|cxfEndpointBean
decl_stmt|;
DECL|method|CxfEndpoint (String uri, String address, CxfComponent component)
specifier|public
name|CxfEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|address
parameter_list|,
name|CxfComponent
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
name|component
operator|=
name|component
expr_stmt|;
name|this
operator|.
name|address
operator|=
name|address
expr_stmt|;
if|if
condition|(
name|address
operator|.
name|startsWith
argument_list|(
name|CxfConstants
operator|.
name|SPRING_CONTEXT_ENDPOINT
argument_list|)
condition|)
block|{
name|isSpringContextEndpoint
operator|=
literal|true
expr_stmt|;
comment|// Get the bean from the Spring context
name|beanId
operator|=
name|address
operator|.
name|substring
argument_list|(
name|CxfConstants
operator|.
name|SPRING_CONTEXT_ENDPOINT
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|beanId
operator|.
name|startsWith
argument_list|(
literal|"//"
argument_list|)
condition|)
block|{
name|beanId
operator|=
name|beanId
operator|.
name|substring
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
name|SpringCamelContext
name|context
init|=
operator|(
name|SpringCamelContext
operator|)
name|this
operator|.
name|getCamelContext
argument_list|()
decl_stmt|;
name|configurer
operator|=
operator|new
name|ConfigurerImpl
argument_list|(
name|context
operator|.
name|getApplicationContext
argument_list|()
argument_list|)
expr_stmt|;
name|cxfEndpointBean
operator|=
operator|(
name|CxfEndpointBean
operator|)
name|context
operator|.
name|getApplicationContext
argument_list|()
operator|.
name|getBean
argument_list|(
name|beanId
argument_list|)
expr_stmt|;
assert|assert
name|cxfEndpointBean
operator|!=
literal|null
assert|;
block|}
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
argument_list|<
name|CxfExchange
argument_list|>
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|CxfProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
argument_list|<
name|CxfExchange
argument_list|>
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|CxfConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
DECL|method|createExchange ()
specifier|public
name|Exchange
name|createExchange
parameter_list|()
block|{
return|return
operator|new
name|CxfExchange
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|getExchangePattern
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createExchange (ExchangePattern pattern)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
block|{
return|return
operator|new
name|CxfExchange
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|pattern
argument_list|)
return|;
block|}
DECL|method|createExchange (Message inMessage)
specifier|public
name|CxfExchange
name|createExchange
parameter_list|(
name|Message
name|inMessage
parameter_list|)
block|{
return|return
operator|new
name|CxfExchange
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|getExchangePattern
argument_list|()
argument_list|,
name|inMessage
argument_list|)
return|;
block|}
DECL|method|getDataFormat ()
specifier|public
name|String
name|getDataFormat
parameter_list|()
block|{
return|return
name|dataFormat
return|;
block|}
DECL|method|setDataFormat (String format)
specifier|public
name|void
name|setDataFormat
parameter_list|(
name|String
name|format
parameter_list|)
block|{
name|dataFormat
operator|=
name|format
expr_stmt|;
block|}
DECL|method|isSpringContextEndpoint ()
specifier|public
name|boolean
name|isSpringContextEndpoint
parameter_list|()
block|{
return|return
name|isSpringContextEndpoint
return|;
block|}
DECL|method|getAddress ()
specifier|public
name|String
name|getAddress
parameter_list|()
block|{
return|return
name|address
return|;
block|}
DECL|method|getWsdlURL ()
specifier|public
name|String
name|getWsdlURL
parameter_list|()
block|{
return|return
name|wsdlURL
return|;
block|}
DECL|method|setWsdlURL (String url)
specifier|public
name|void
name|setWsdlURL
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|wsdlURL
operator|=
name|url
expr_stmt|;
block|}
DECL|method|setSetDefaultBus (Boolean set)
specifier|public
name|void
name|setSetDefaultBus
parameter_list|(
name|Boolean
name|set
parameter_list|)
block|{
name|isSetDefaultBus
operator|=
name|set
expr_stmt|;
block|}
DECL|method|isSetDefaultBus ()
specifier|public
name|Boolean
name|isSetDefaultBus
parameter_list|()
block|{
return|return
name|isSetDefaultBus
return|;
block|}
DECL|method|getServiceClass ()
specifier|public
name|String
name|getServiceClass
parameter_list|()
block|{
return|return
name|serviceClass
return|;
block|}
DECL|method|setServiceClass (String className)
specifier|public
name|void
name|setServiceClass
parameter_list|(
name|String
name|className
parameter_list|)
block|{
name|serviceClass
operator|=
name|className
expr_stmt|;
block|}
DECL|method|setPortName (String port)
specifier|public
name|void
name|setPortName
parameter_list|(
name|String
name|port
parameter_list|)
block|{
name|portName
operator|=
name|port
expr_stmt|;
block|}
DECL|method|setServiceName (String service)
specifier|public
name|void
name|setServiceName
parameter_list|(
name|String
name|service
parameter_list|)
block|{
name|serviceName
operator|=
name|service
expr_stmt|;
block|}
DECL|method|getPortName ()
specifier|public
name|String
name|getPortName
parameter_list|()
block|{
return|return
name|portName
return|;
block|}
DECL|method|getServiceName ()
specifier|public
name|String
name|getServiceName
parameter_list|()
block|{
return|return
name|serviceName
return|;
block|}
DECL|method|isInOut ()
specifier|public
name|boolean
name|isInOut
parameter_list|()
block|{
return|return
name|inOut
return|;
block|}
DECL|method|setInOut (boolean inOut)
specifier|public
name|void
name|setInOut
parameter_list|(
name|boolean
name|inOut
parameter_list|)
block|{
name|this
operator|.
name|inOut
operator|=
name|inOut
expr_stmt|;
block|}
DECL|method|isWrapped ()
specifier|public
name|boolean
name|isWrapped
parameter_list|()
block|{
return|return
name|isWrapped
return|;
block|}
DECL|method|setWrapped (boolean wrapped)
specifier|public
name|void
name|setWrapped
parameter_list|(
name|boolean
name|wrapped
parameter_list|)
block|{
name|isWrapped
operator|=
name|wrapped
expr_stmt|;
block|}
DECL|method|getComponent ()
specifier|public
name|CxfComponent
name|getComponent
parameter_list|()
block|{
return|return
name|component
return|;
block|}
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
DECL|method|getBeanId ()
specifier|public
name|String
name|getBeanId
parameter_list|()
block|{
return|return
name|beanId
return|;
block|}
DECL|method|getCxfEndpointBean ()
specifier|public
name|CxfEndpointBean
name|getCxfEndpointBean
parameter_list|()
block|{
return|return
name|cxfEndpointBean
return|;
block|}
DECL|method|configure (Object beanInstance)
specifier|public
name|void
name|configure
parameter_list|(
name|Object
name|beanInstance
parameter_list|)
block|{
name|configurer
operator|.
name|configureBean
argument_list|(
name|beanId
argument_list|,
name|beanInstance
argument_list|)
expr_stmt|;
block|}
DECL|method|getApplicationContext ()
specifier|public
name|ApplicationContext
name|getApplicationContext
parameter_list|()
block|{
if|if
condition|(
name|getCamelContext
argument_list|()
operator|instanceof
name|SpringCamelContext
condition|)
block|{
name|SpringCamelContext
name|context
init|=
operator|(
name|SpringCamelContext
operator|)
name|getCamelContext
argument_list|()
decl_stmt|;
return|return
name|context
operator|.
name|getApplicationContext
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
DECL|method|getHeaderFilterStrategy ()
specifier|public
name|HeaderFilterStrategy
name|getHeaderFilterStrategy
parameter_list|()
block|{
return|return
name|component
operator|.
name|getHeaderFilterStrategy
argument_list|()
return|;
block|}
block|}
end_class

end_unit

