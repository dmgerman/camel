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
name|CxfConstants
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
name|CxfSpringEndpoint
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
name|frontend
operator|.
name|ClientProxyFactoryBean
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
name|JaxWsProxyFactoryBean
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

begin_class
DECL|class|CxfEndpointUtils
specifier|public
specifier|final
class|class
name|CxfEndpointUtils
block|{
DECL|method|CxfEndpointUtils ()
specifier|private
name|CxfEndpointUtils
parameter_list|()
block|{
comment|// not constructed
block|}
DECL|method|getQName (final String name)
specifier|public
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
comment|// only used by test currently
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
if|if
condition|(
name|endpoint
operator|.
name|getPortName
argument_list|()
operator|!=
literal|null
condition|)
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
else|else
block|{
name|String
name|portLocalName
init|=
name|getCxfEndpointPropertyValue
argument_list|(
operator|(
name|CxfSpringEndpoint
operator|)
name|endpoint
argument_list|,
name|CxfConstants
operator|.
name|PORT_LOCALNAME
argument_list|)
decl_stmt|;
name|String
name|portNamespace
init|=
name|getCxfEndpointPropertyValue
argument_list|(
operator|(
name|CxfSpringEndpoint
operator|)
name|endpoint
argument_list|,
name|CxfConstants
operator|.
name|PORT_NAMESPACE
argument_list|)
decl_stmt|;
if|if
condition|(
name|portLocalName
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|QName
argument_list|(
name|portNamespace
argument_list|,
name|portLocalName
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
comment|// only used by test currently
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
if|if
condition|(
name|endpoint
operator|.
name|getServiceName
argument_list|()
operator|!=
literal|null
condition|)
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
else|else
block|{
name|String
name|serviceLocalName
init|=
name|getCxfEndpointPropertyValue
argument_list|(
operator|(
name|CxfSpringEndpoint
operator|)
name|endpoint
argument_list|,
name|CxfConstants
operator|.
name|SERVICE_LOCALNAME
argument_list|)
decl_stmt|;
name|String
name|serviceNamespace
init|=
name|getCxfEndpointPropertyValue
argument_list|(
operator|(
name|CxfSpringEndpoint
operator|)
name|endpoint
argument_list|,
name|CxfConstants
operator|.
name|SERVICE_NAMESPACE
argument_list|)
decl_stmt|;
if|if
condition|(
name|serviceLocalName
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|QName
argument_list|(
name|serviceNamespace
argument_list|,
name|serviceLocalName
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
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
name|ClientProxyFactoryBean
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
name|ClientProxyFactoryBean
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
name|ClientProxyFactoryBean
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
name|JaxWsProxyFactoryBean
argument_list|()
else|:
operator|new
name|ClientProxyFactoryBean
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
comment|// only used by test currently
DECL|method|checkServiceClassName (String className)
specifier|public
specifier|static
name|void
name|checkServiceClassName
parameter_list|(
name|String
name|className
parameter_list|)
throws|throws
name|CamelException
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|className
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
literal|"serviceClass is required for CXF endpoint configuration"
argument_list|)
throw|;
block|}
block|}
comment|// only used by test currently
DECL|method|getCxfEndpointPropertyValue (CxfSpringEndpoint endpoint, String property)
specifier|public
specifier|static
name|String
name|getCxfEndpointPropertyValue
parameter_list|(
name|CxfSpringEndpoint
name|endpoint
parameter_list|,
name|String
name|property
parameter_list|)
block|{
name|String
name|result
init|=
literal|null
decl_stmt|;
name|CxfEndpointBean
name|cxfEndpointBean
init|=
name|endpoint
operator|.
name|getBean
argument_list|()
decl_stmt|;
if|if
condition|(
name|cxfEndpointBean
operator|!=
literal|null
operator|&&
name|cxfEndpointBean
operator|.
name|getProperties
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
operator|(
name|String
operator|)
name|cxfEndpointBean
operator|.
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
name|property
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

