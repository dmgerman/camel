begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.transport
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
name|transport
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|ProducerTemplate
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
name|RuntimeCamelException
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
name|util
operator|.
name|ObjectHelper
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|Configurable
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
name|Configurer
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
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|EndpointInfo
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
name|AbstractConduit
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
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|CamelConduit
specifier|public
class|class
name|CamelConduit
extends|extends
name|AbstractConduit
implements|implements
name|Configurable
block|{
DECL|field|BASE_BEAN_NAME_SUFFIX
specifier|protected
specifier|static
specifier|final
name|String
name|BASE_BEAN_NAME_SUFFIX
init|=
literal|".camel-conduit"
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CamelConduit
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// used for places where CXF requires JUL
DECL|field|JUL_LOG
specifier|private
specifier|static
specifier|final
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
name|JUL_LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|CamelConduit
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|endpointInfo
specifier|private
name|EndpointInfo
name|endpointInfo
decl_stmt|;
DECL|field|targetCamelEndpointUri
specifier|private
name|String
name|targetCamelEndpointUri
decl_stmt|;
DECL|field|producer
specifier|private
name|Producer
name|producer
decl_stmt|;
DECL|field|camelTemplate
specifier|private
name|ProducerTemplate
name|camelTemplate
decl_stmt|;
DECL|field|bus
specifier|private
name|Bus
name|bus
decl_stmt|;
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
decl_stmt|;
DECL|method|CamelConduit (CamelContext context, Bus b, EndpointInfo endpointInfo)
specifier|public
name|CamelConduit
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Bus
name|b
parameter_list|,
name|EndpointInfo
name|endpointInfo
parameter_list|)
block|{
name|this
argument_list|(
name|context
argument_list|,
name|b
argument_list|,
name|endpointInfo
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|CamelConduit (CamelContext context, Bus b, EndpointInfo epInfo, EndpointReferenceType targetReference)
specifier|public
name|CamelConduit
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Bus
name|b
parameter_list|,
name|EndpointInfo
name|epInfo
parameter_list|,
name|EndpointReferenceType
name|targetReference
parameter_list|)
block|{
name|this
argument_list|(
name|context
argument_list|,
name|b
argument_list|,
name|epInfo
argument_list|,
name|targetReference
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|CamelConduit (CamelContext context, Bus b, EndpointInfo epInfo, EndpointReferenceType targetReference, HeaderFilterStrategy headerFilterStrategy)
specifier|public
name|CamelConduit
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Bus
name|b
parameter_list|,
name|EndpointInfo
name|epInfo
parameter_list|,
name|EndpointReferenceType
name|targetReference
parameter_list|,
name|HeaderFilterStrategy
name|headerFilterStrategy
parameter_list|)
block|{
name|super
argument_list|(
name|getTargetReference
argument_list|(
name|epInfo
argument_list|,
name|targetReference
argument_list|,
name|b
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|address
init|=
name|epInfo
operator|.
name|getAddress
argument_list|()
decl_stmt|;
if|if
condition|(
name|address
operator|!=
literal|null
condition|)
block|{
name|targetCamelEndpointUri
operator|=
name|address
operator|.
name|substring
argument_list|(
name|CamelTransportConstants
operator|.
name|CAMEL_TRANSPORT_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|targetCamelEndpointUri
operator|.
name|startsWith
argument_list|(
literal|"//"
argument_list|)
condition|)
block|{
name|targetCamelEndpointUri
operator|=
name|targetCamelEndpointUri
operator|.
name|substring
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
block|}
name|camelContext
operator|=
name|context
expr_stmt|;
name|endpointInfo
operator|=
name|epInfo
expr_stmt|;
name|bus
operator|=
name|b
expr_stmt|;
name|initConfig
argument_list|()
expr_stmt|;
name|this
operator|.
name|headerFilterStrategy
operator|=
name|headerFilterStrategy
expr_stmt|;
name|Endpoint
name|target
init|=
name|getCamelContext
argument_list|()
operator|.
name|getEndpoint
argument_list|(
name|targetCamelEndpointUri
argument_list|)
decl_stmt|;
try|try
block|{
name|producer
operator|=
name|target
operator|.
name|createProducer
argument_list|()
expr_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Cannot create the producer rightly"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|setCamelContext (CamelContext context)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|camelContext
operator|=
name|context
expr_stmt|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"CamelContext"
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
name|camelContext
return|;
block|}
comment|// prepare the message for send out , not actually send out the message
annotation|@
name|Override
DECL|method|prepare (Message message)
specifier|public
name|void
name|prepare
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|IOException
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"CamelConduit send message"
argument_list|)
expr_stmt|;
name|CamelOutputStream
name|os
init|=
operator|new
name|CamelOutputStream
argument_list|(
name|this
operator|.
name|targetCamelEndpointUri
argument_list|,
name|this
operator|.
name|producer
argument_list|,
name|this
operator|.
name|headerFilterStrategy
argument_list|,
name|this
operator|.
name|getMessageObserver
argument_list|()
argument_list|,
name|message
argument_list|)
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
name|os
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
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"CamelConduit closed "
argument_list|)
expr_stmt|;
comment|// shutdown the producer
try|try
block|{
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"CamelConduit producer stop with the exception"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getLogger ()
specifier|protected
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
name|getLogger
parameter_list|()
block|{
return|return
name|JUL_LOG
return|;
block|}
annotation|@
name|Override
DECL|method|getBeanName ()
specifier|public
name|String
name|getBeanName
parameter_list|()
block|{
if|if
condition|(
name|endpointInfo
operator|==
literal|null
operator|||
name|endpointInfo
operator|.
name|getName
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|"default"
operator|+
name|BASE_BEAN_NAME_SUFFIX
return|;
block|}
return|return
name|endpointInfo
operator|.
name|getName
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
name|BASE_BEAN_NAME_SUFFIX
return|;
block|}
DECL|method|initConfig ()
specifier|private
name|void
name|initConfig
parameter_list|()
block|{
comment|// we could configure the camel context here
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|Configurer
name|configurer
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|Configurer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|configurer
condition|)
block|{
name|configurer
operator|.
name|configureBean
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Deprecated
DECL|method|getCamelTemplate ()
specifier|public
name|ProducerTemplate
name|getCamelTemplate
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|camelTemplate
operator|==
literal|null
condition|)
block|{
name|camelTemplate
operator|=
name|getCamelContext
argument_list|()
operator|.
name|createProducerTemplate
argument_list|()
expr_stmt|;
block|}
return|return
name|camelTemplate
return|;
block|}
annotation|@
name|Deprecated
DECL|method|setCamelTemplate (ProducerTemplate template)
specifier|public
name|void
name|setCamelTemplate
parameter_list|(
name|ProducerTemplate
name|template
parameter_list|)
block|{
name|camelTemplate
operator|=
name|template
expr_stmt|;
block|}
block|}
end_class

end_unit

