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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Definition
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|factory
operator|.
name|WSDLFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|xml
operator|.
name|WSDLReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|CamelContext
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
name|Endpoint
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
name|PollingConsumer
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
name|helpers
operator|.
name|DOMUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|util
operator|.
name|Assert
import|;
end_import

begin_comment
comment|/**  * A CXF based SOAP endpoint which wraps an existing  * endpoint with SOAP processing.  */
end_comment

begin_class
DECL|class|CxfSoapEndpoint
specifier|public
class|class
name|CxfSoapEndpoint
implements|implements
name|Endpoint
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|Endpoint
name|endpoint
decl_stmt|;
DECL|field|wsdl
specifier|private
name|Resource
name|wsdl
decl_stmt|;
DECL|field|serviceClass
specifier|private
name|String
name|serviceClass
decl_stmt|;
DECL|field|description
specifier|private
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
name|description
decl_stmt|;
DECL|field|definition
specifier|private
name|Definition
name|definition
decl_stmt|;
DECL|field|serviceName
specifier|private
name|QName
name|serviceName
decl_stmt|;
DECL|field|endpointName
specifier|private
name|QName
name|endpointName
decl_stmt|;
DECL|field|bus
specifier|private
name|Bus
name|bus
decl_stmt|;
DECL|method|CxfSoapEndpoint (Endpoint endpoint)
specifier|public
name|CxfSoapEndpoint
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
DECL|method|getInnerEndpoint ()
specifier|protected
name|Endpoint
name|getInnerEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
name|endpoint
operator|.
name|isSingleton
argument_list|()
return|;
block|}
DECL|method|getEndpointUri ()
specifier|public
name|String
name|getEndpointUri
parameter_list|()
block|{
return|return
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
return|;
block|}
DECL|method|createExchange ()
specifier|public
name|Exchange
name|createExchange
parameter_list|()
block|{
return|return
name|endpoint
operator|.
name|createExchange
argument_list|()
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
name|endpoint
operator|.
name|createExchange
argument_list|(
name|pattern
argument_list|)
return|;
block|}
DECL|method|createExchange (Exchange exchange)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|endpoint
operator|.
name|createExchange
argument_list|(
name|exchange
argument_list|)
return|;
block|}
DECL|method|getContext ()
specifier|public
name|CamelContext
name|getContext
parameter_list|()
block|{
return|return
name|endpoint
operator|.
name|getContext
argument_list|()
return|;
block|}
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
name|CxfSoapProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
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
name|CxfSoapConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
DECL|method|createPollingConsumer ()
specifier|public
name|PollingConsumer
name|createPollingConsumer
parameter_list|()
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
DECL|method|configureProperties (Map options)
specifier|public
name|void
name|configureProperties
parameter_list|(
name|Map
name|options
parameter_list|)
block|{     }
DECL|method|getWsdl ()
specifier|public
name|Resource
name|getWsdl
parameter_list|()
block|{
return|return
name|wsdl
return|;
block|}
DECL|method|setWsdl (Resource wsdl)
specifier|public
name|void
name|setWsdl
parameter_list|(
name|Resource
name|wsdl
parameter_list|)
block|{
name|this
operator|.
name|wsdl
operator|=
name|wsdl
expr_stmt|;
block|}
DECL|method|setServiceClass (String serviceClass)
specifier|public
name|void
name|setServiceClass
parameter_list|(
name|String
name|serviceClass
parameter_list|)
block|{
name|this
operator|.
name|serviceClass
operator|=
name|serviceClass
expr_stmt|;
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
DECL|method|setServiceName (String serviceName)
specifier|public
name|void
name|setServiceName
parameter_list|(
name|String
name|serviceName
parameter_list|)
block|{
name|this
operator|.
name|serviceName
operator|=
name|QName
operator|.
name|valueOf
argument_list|(
name|serviceName
argument_list|)
expr_stmt|;
block|}
DECL|method|setEndpointName (String endpointName)
specifier|public
name|void
name|setEndpointName
parameter_list|(
name|String
name|endpointName
parameter_list|)
block|{
name|this
operator|.
name|endpointName
operator|=
name|QName
operator|.
name|valueOf
argument_list|(
name|endpointName
argument_list|)
expr_stmt|;
block|}
DECL|method|getEndpointName ()
specifier|public
name|QName
name|getEndpointName
parameter_list|()
block|{
return|return
name|endpointName
return|;
block|}
DECL|method|init ()
specifier|public
name|void
name|init
parameter_list|()
throws|throws
name|Exception
block|{
name|Assert
operator|.
name|notNull
argument_list|(
name|wsdl
argument_list|,
literal|"soap.wsdl parameter must be set on the uri"
argument_list|)
expr_stmt|;
if|if
condition|(
name|serviceName
operator|==
literal|null
condition|)
block|{
name|description
operator|=
name|DOMUtils
operator|.
name|readXml
argument_list|(
name|wsdl
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
name|WSDLFactory
name|wsdlFactory
init|=
name|WSDLFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|WSDLReader
name|reader
init|=
name|wsdlFactory
operator|.
name|newWSDLReader
argument_list|()
decl_stmt|;
name|reader
operator|.
name|setFeature
argument_list|(
literal|"javax.wsdl.verbose"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|definition
operator|=
name|reader
operator|.
name|readWSDL
argument_list|(
name|wsdl
operator|.
name|getURL
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|description
argument_list|)
expr_stmt|;
name|serviceName
operator|=
operator|(
name|QName
operator|)
name|definition
operator|.
name|getServices
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getBus ()
specifier|protected
name|Bus
name|getBus
parameter_list|()
block|{
if|if
condition|(
name|bus
operator|==
literal|null
condition|)
block|{
name|bus
operator|=
name|BusFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createBus
argument_list|()
expr_stmt|;
block|}
return|return
name|bus
return|;
block|}
DECL|method|getDefinition ()
specifier|public
name|Definition
name|getDefinition
parameter_list|()
block|{
return|return
name|definition
return|;
block|}
DECL|method|getServiceName ()
specifier|public
name|QName
name|getServiceName
parameter_list|()
block|{
return|return
name|serviceName
return|;
block|}
block|}
end_class

end_unit

