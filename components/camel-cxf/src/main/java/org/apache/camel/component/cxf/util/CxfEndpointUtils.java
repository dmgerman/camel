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
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
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
name|CamelException
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
name|CxfEndpoint
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
name|DataFormat
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
name|common
operator|.
name|i18n
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
name|frontend
operator|.
name|ClientFactoryBean
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|JaxWsClientFactoryBean
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
name|jaxws
operator|.
name|JaxWsServerFactoryBean
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
name|jaxws
operator|.
name|support
operator|.
name|JaxWsServiceFactoryBean
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
name|AbstractServiceFactoryBean
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
name|wsdl11
operator|.
name|WSDLServiceFactory
import|;
end_import

begin_class
DECL|class|CxfEndpointUtils
specifier|public
specifier|final
class|class
name|CxfEndpointUtils
block|{
DECL|field|PROP_NAME_PORT
specifier|public
specifier|static
specifier|final
name|String
name|PROP_NAME_PORT
init|=
literal|"port"
decl_stmt|;
DECL|field|PROP_NAME_SERVICE
specifier|public
specifier|static
specifier|final
name|String
name|PROP_NAME_SERVICE
init|=
literal|"service"
decl_stmt|;
DECL|field|PROP_NAME_SERVICECLASS
specifier|public
specifier|static
specifier|final
name|String
name|PROP_NAME_SERVICECLASS
init|=
literal|"serviceClass"
decl_stmt|;
DECL|field|PROP_NAME_DATAFORMAT
specifier|public
specifier|static
specifier|final
name|String
name|PROP_NAME_DATAFORMAT
init|=
literal|"dataFormat"
decl_stmt|;
DECL|field|DATAFORMAT_POJO
specifier|public
specifier|static
specifier|final
name|String
name|DATAFORMAT_POJO
init|=
literal|"pojo"
decl_stmt|;
DECL|field|DATAFORMAT_MESSAGE
specifier|public
specifier|static
specifier|final
name|String
name|DATAFORMAT_MESSAGE
init|=
literal|"message"
decl_stmt|;
DECL|field|DATAFORMAT_PAYLOAD
specifier|public
specifier|static
specifier|final
name|String
name|DATAFORMAT_PAYLOAD
init|=
literal|"payload"
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|CxfEndpointUtils
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|CxfEndpointUtils ()
specifier|private
name|CxfEndpointUtils
parameter_list|()
block|{
comment|// not constructed
block|}
DECL|method|getQName (final String name)
specifier|static
name|QName
name|getQName
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
name|QName
name|qName
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|qName
operator|=
name|QName
operator|.
name|valueOf
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|qName
return|;
block|}
DECL|method|getPortName (final CxfEndpoint endpoint)
specifier|public
specifier|static
name|QName
name|getPortName
parameter_list|(
specifier|final
name|CxfEndpoint
name|endpoint
parameter_list|)
block|{
return|return
name|getQName
argument_list|(
name|endpoint
operator|.
name|getPortName
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getServiceName (final CxfEndpoint endpoint)
specifier|public
specifier|static
name|QName
name|getServiceName
parameter_list|(
specifier|final
name|CxfEndpoint
name|endpoint
parameter_list|)
block|{
return|return
name|getQName
argument_list|(
name|endpoint
operator|.
name|getServiceName
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getEndpointInfo (final Service service, final CxfEndpoint endpoint)
specifier|public
specifier|static
name|EndpointInfo
name|getEndpointInfo
parameter_list|(
specifier|final
name|Service
name|service
parameter_list|,
specifier|final
name|CxfEndpoint
name|endpoint
parameter_list|)
block|{
name|EndpointInfo
name|endpointInfo
init|=
literal|null
decl_stmt|;
specifier|final
name|java
operator|.
name|util
operator|.
name|Collection
argument_list|<
name|EndpointInfo
argument_list|>
name|endpoints
init|=
name|service
operator|.
name|getServiceInfos
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getEndpoints
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoints
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|endpointInfo
operator|=
name|endpoints
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
else|else
block|{
specifier|final
name|String
name|port
init|=
name|endpoint
operator|.
name|getPortName
argument_list|()
decl_stmt|;
if|if
condition|(
name|port
operator|!=
literal|null
condition|)
block|{
specifier|final
name|QName
name|endpointName
init|=
name|QName
operator|.
name|valueOf
argument_list|(
name|port
argument_list|)
decl_stmt|;
name|endpointInfo
operator|=
name|service
operator|.
name|getServiceInfos
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getEndpoint
argument_list|(
name|endpointName
argument_list|)
expr_stmt|;
block|}
comment|//TBD may be delegate to the EndpointUri params.
block|}
return|return
name|endpointInfo
return|;
block|}
DECL|method|getSEIClass (String className)
specifier|public
specifier|static
name|Class
name|getSEIClass
parameter_list|(
name|String
name|className
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
if|if
condition|(
name|className
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
return|return
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|className
argument_list|,
name|CxfEndpointUtils
operator|.
name|class
argument_list|)
return|;
block|}
block|}
DECL|method|hasWebServiceAnnotation (Class<?> cls)
specifier|public
specifier|static
name|boolean
name|hasWebServiceAnnotation
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|hasAnnotation
argument_list|(
name|cls
argument_list|,
name|WebService
operator|.
name|class
argument_list|)
operator|||
name|hasAnnotation
argument_list|(
name|cls
argument_list|,
name|WebServiceProvider
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|hasAnnotation (Class<?> cls, Class<? extends Annotation> annotation)
specifier|public
specifier|static
name|boolean
name|hasAnnotation
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|annotation
parameter_list|)
block|{
if|if
condition|(
name|cls
operator|==
literal|null
operator|||
name|cls
operator|==
name|Object
operator|.
name|class
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
literal|null
operator|!=
name|cls
operator|.
name|getAnnotation
argument_list|(
name|annotation
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|interfaceClass
range|:
name|cls
operator|.
name|getInterfaces
argument_list|()
control|)
block|{
if|if
condition|(
literal|null
operator|!=
name|interfaceClass
operator|.
name|getAnnotation
argument_list|(
name|annotation
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
name|hasAnnotation
argument_list|(
name|cls
operator|.
name|getSuperclass
argument_list|()
argument_list|,
name|annotation
argument_list|)
return|;
block|}
DECL|method|getServerFactoryBean (Class<?> cls)
specifier|public
specifier|static
name|ServerFactoryBean
name|getServerFactoryBean
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
throws|throws
name|CamelException
block|{
name|ServerFactoryBean
name|serverFactory
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|cls
operator|==
literal|null
condition|)
block|{
name|serverFactory
operator|=
operator|new
name|ServerFactoryBean
argument_list|()
expr_stmt|;
name|serverFactory
operator|.
name|setServiceFactory
argument_list|(
operator|new
name|WSDLSoapServiceFactoryBean
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|boolean
name|isJSR181SEnabled
init|=
name|CxfEndpointUtils
operator|.
name|hasWebServiceAnnotation
argument_list|(
name|cls
argument_list|)
decl_stmt|;
name|serverFactory
operator|=
name|isJSR181SEnabled
condition|?
operator|new
name|JaxWsServerFactoryBean
argument_list|()
else|:
operator|new
name|ServerFactoryBean
argument_list|()
expr_stmt|;
block|}
return|return
name|serverFactory
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getClientFactoryBean (Class<?> cls)
specifier|public
specifier|static
name|ClientFactoryBean
name|getClientFactoryBean
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
throws|throws
name|CamelException
block|{
name|ClientFactoryBean
name|clientFactory
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|cls
operator|==
literal|null
condition|)
block|{
name|clientFactory
operator|=
operator|new
name|ClientFactoryBean
argument_list|()
expr_stmt|;
name|clientFactory
operator|.
name|setServiceFactory
argument_list|(
operator|new
name|WSDLSoapServiceFactoryBean
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|boolean
name|isJSR181SEnabled
init|=
name|CxfEndpointUtils
operator|.
name|hasWebServiceAnnotation
argument_list|(
name|cls
argument_list|)
decl_stmt|;
name|clientFactory
operator|=
name|isJSR181SEnabled
condition|?
operator|new
name|JaxWsClientFactoryBean
argument_list|()
else|:
operator|new
name|ClientFactoryBean
argument_list|()
expr_stmt|;
block|}
return|return
name|clientFactory
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|//TODO check the CxfEndpoint information integration
DECL|method|checkEndpiontIntegration (CxfEndpoint endpoint, Bus bus)
specifier|public
specifier|static
name|void
name|checkEndpiontIntegration
parameter_list|(
name|CxfEndpoint
name|endpoint
parameter_list|,
name|Bus
name|bus
parameter_list|)
throws|throws
name|CamelException
block|{
name|String
name|wsdlLocation
init|=
name|endpoint
operator|.
name|getWsdlURL
argument_list|()
decl_stmt|;
name|QName
name|serviceQName
init|=
name|CxfEndpointUtils
operator|.
name|getQName
argument_list|(
name|endpoint
operator|.
name|getServiceName
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|serviceClassName
init|=
name|endpoint
operator|.
name|getServiceClass
argument_list|()
decl_stmt|;
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
name|URL
name|wsdlUrl
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|wsdlLocation
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|wsdlUrl
operator|=
name|UriUtils
operator|.
name|getWsdlUrl
argument_list|(
operator|new
name|URI
argument_list|(
name|wsdlLocation
argument_list|)
argument_list|)
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
name|CamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|serviceQName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"SVC_QNAME_NOT_FOUND_X"
argument_list|,
name|LOG
argument_list|,
name|endpoint
operator|.
name|getServiceName
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|serviceClassName
operator|==
literal|null
operator|&&
name|dataFormat
operator|==
name|DataFormat
operator|.
name|POJO
condition|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"SVC_CLASS_PROP_IS_REQUIRED_X"
argument_list|,
name|LOG
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
name|AbstractServiceFactoryBean
name|serviceFactory
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|serviceClassName
operator|!=
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|serviceClassName
argument_list|,
name|CxfEndpointUtils
operator|.
name|class
argument_list|)
decl_stmt|;
name|boolean
name|isJSR181SEnabled
init|=
name|CxfEndpointUtils
operator|.
name|hasWebServiceAnnotation
argument_list|(
name|cls
argument_list|)
decl_stmt|;
name|serviceFactory
operator|=
name|isJSR181SEnabled
condition|?
operator|new
name|JaxWsServiceFactoryBean
argument_list|()
else|:
operator|new
name|ReflectionServiceFactoryBean
argument_list|()
expr_stmt|;
name|serviceFactory
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
if|if
condition|(
name|wsdlUrl
operator|!=
literal|null
condition|)
block|{
operator|(
operator|(
name|ReflectionServiceFactoryBean
operator|)
name|serviceFactory
operator|)
operator|.
name|setWsdlURL
argument_list|(
name|wsdlUrl
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|serviceQName
operator|!=
literal|null
condition|)
block|{
operator|(
operator|(
name|ReflectionServiceFactoryBean
operator|)
name|serviceFactory
operator|)
operator|.
name|setServiceName
argument_list|(
name|serviceQName
argument_list|)
expr_stmt|;
block|}
operator|(
operator|(
name|ReflectionServiceFactoryBean
operator|)
name|serviceFactory
operator|)
operator|.
name|setServiceClass
argument_list|(
name|cls
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|wsdlUrl
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"SVC_WSDL_URL_IS_NULL_X"
argument_list|,
name|LOG
argument_list|,
name|wsdlLocation
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
name|serviceFactory
operator|=
operator|new
name|WSDLServiceFactory
argument_list|(
name|bus
argument_list|,
name|wsdlUrl
argument_list|,
name|serviceQName
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|cnfe
parameter_list|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"CLASS_X_NOT_FOUND "
argument_list|,
name|LOG
argument_list|,
name|serviceClassName
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|,
name|cnfe
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getDataFormat (CxfEndpoint endpoint)
specifier|public
specifier|static
name|DataFormat
name|getDataFormat
parameter_list|(
name|CxfEndpoint
name|endpoint
parameter_list|)
throws|throws
name|CamelException
block|{
name|String
name|dataFormatString
init|=
name|endpoint
operator|.
name|getDataFormat
argument_list|()
decl_stmt|;
if|if
condition|(
name|dataFormatString
operator|==
literal|null
condition|)
block|{
return|return
name|DataFormat
operator|.
name|POJO
return|;
block|}
name|DataFormat
name|retval
init|=
name|DataFormat
operator|.
name|asEnum
argument_list|(
name|dataFormatString
argument_list|)
decl_stmt|;
if|if
condition|(
name|retval
operator|==
name|DataFormat
operator|.
name|UNKNOWN
condition|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"INVALID_MESSAGE_FORMAT_XXXX"
argument_list|,
name|LOG
argument_list|,
name|dataFormatString
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|retval
return|;
block|}
block|}
end_class

end_unit

