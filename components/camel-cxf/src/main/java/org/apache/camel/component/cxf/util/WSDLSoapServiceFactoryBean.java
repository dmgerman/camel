begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.util
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
name|util
package|;
end_package

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
name|cxf
operator|.
name|binding
operator|.
name|soap
operator|.
name|interceptor
operator|.
name|CheckFaultInterceptor
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
name|binding
operator|.
name|soap
operator|.
name|interceptor
operator|.
name|MustUnderstandInterceptor
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
name|binding
operator|.
name|soap
operator|.
name|interceptor
operator|.
name|ReadHeadersInterceptor
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
name|binding
operator|.
name|soap
operator|.
name|interceptor
operator|.
name|SoapActionInInterceptor
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
name|binding
operator|.
name|soap
operator|.
name|interceptor
operator|.
name|SoapHeaderInterceptor
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
name|binding
operator|.
name|soap
operator|.
name|interceptor
operator|.
name|SoapHeaderOutFilterInterceptor
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
name|binding
operator|.
name|soap
operator|.
name|interceptor
operator|.
name|SoapOutInterceptor
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
name|binding
operator|.
name|soap
operator|.
name|interceptor
operator|.
name|SoapPreProtocolOutInterceptor
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
name|interceptor
operator|.
name|AttachmentInInterceptor
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
name|interceptor
operator|.
name|AttachmentOutInterceptor
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
name|interceptor
operator|.
name|StaxInInterceptor
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
name|interceptor
operator|.
name|StaxOutInterceptor
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
name|interceptor
operator|.
name|URIMappingInterceptor
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
name|Service
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
name|factory
operator|.
name|ReflectionServiceFactoryBean
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
name|service
operator|.
name|model
operator|.
name|ServiceInfo
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
name|wsdl11
operator|.
name|WSDLServiceFactory
import|;
end_import

begin_comment
comment|//The service factory bean which is used for the service without SEI
end_comment

begin_class
DECL|class|WSDLSoapServiceFactoryBean
specifier|public
class|class
name|WSDLSoapServiceFactoryBean
extends|extends
name|ReflectionServiceFactoryBean
block|{
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
annotation|@
name|Override
DECL|method|create ()
specifier|public
name|Service
name|create
parameter_list|()
block|{
name|WSDLServiceFactory
name|factory
init|=
operator|new
name|WSDLServiceFactory
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|getWsdlURL
argument_list|()
argument_list|,
name|getServiceQName
argument_list|()
argument_list|)
decl_stmt|;
name|setService
argument_list|(
name|factory
operator|.
name|create
argument_list|()
argument_list|)
expr_stmt|;
name|initializeSoapInterceptors
argument_list|()
expr_stmt|;
comment|//disable the date interceptors
name|updateEndpointInfors
argument_list|()
expr_stmt|;
name|createEndpoints
argument_list|()
expr_stmt|;
return|return
name|getService
argument_list|()
return|;
block|}
DECL|method|updateEndpointInfors ()
specifier|private
name|void
name|updateEndpointInfors
parameter_list|()
block|{
name|Service
name|service
init|=
name|getService
argument_list|()
decl_stmt|;
for|for
control|(
name|ServiceInfo
name|inf
range|:
name|service
operator|.
name|getServiceInfos
argument_list|()
control|)
block|{
for|for
control|(
name|EndpointInfo
name|ei
range|:
name|inf
operator|.
name|getEndpoints
argument_list|()
control|)
block|{
comment|//setup the endpoint address
name|ei
operator|.
name|setAddress
argument_list|(
literal|"local://"
operator|+
name|ei
operator|.
name|getService
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|"/"
operator|+
name|ei
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
comment|// working as the dispatch mode, the binding factory will not add interceptor
comment|//ei.getBinding().setProperty(AbstractBindingFactory.DATABINDING_DISABLED, Boolean.TRUE);
block|}
block|}
block|}
DECL|method|checkServiceClassAnnotations (Class<?> sc)
specifier|protected
name|void
name|checkServiceClassAnnotations
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|sc
parameter_list|)
block|{
comment|// do nothing here
block|}
comment|// do not handle any payload information here
DECL|method|initializeSoapInterceptors ()
specifier|private
name|void
name|initializeSoapInterceptors
parameter_list|()
block|{
name|getService
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|DataInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|getService
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|ReadHeadersInterceptor
argument_list|(
name|getBus
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|getService
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|MustUnderstandInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|getService
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|AttachmentInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|getService
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|SoapHeaderInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|getService
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|CheckFaultInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|getService
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|URIMappingInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|getService
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|StaxInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|getService
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|SoapActionInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|getService
argument_list|()
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|DataOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|getService
argument_list|()
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|AttachmentOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|getService
argument_list|()
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|StaxOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|getService
argument_list|()
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|SoapHeaderOutFilterInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|getService
argument_list|()
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|SoapPreProtocolOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|getService
argument_list|()
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|SoapOutInterceptor
argument_list|(
name|getBus
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|getService
argument_list|()
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|SoapOutInterceptor
argument_list|(
name|getBus
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|setServiceName (QName name)
specifier|public
name|void
name|setServiceName
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
name|serviceName
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getServiceName ()
specifier|public
name|String
name|getServiceName
parameter_list|()
block|{
return|return
name|serviceName
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getServiceQName ()
specifier|public
name|QName
name|getServiceQName
parameter_list|()
block|{
return|return
name|serviceName
return|;
block|}
DECL|method|getEndpointName ()
specifier|public
name|QName
name|getEndpointName
parameter_list|()
block|{
comment|// get the endpoint name if it is not set
if|if
condition|(
name|endpointName
operator|==
literal|null
condition|)
block|{
name|endpointName
operator|=
name|getService
argument_list|()
operator|.
name|getEndpoints
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
return|return
name|endpointName
return|;
block|}
DECL|method|setEndpointName (QName name)
specifier|public
name|void
name|setEndpointName
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
name|endpointName
operator|=
name|name
expr_stmt|;
block|}
block|}
end_class

end_unit

