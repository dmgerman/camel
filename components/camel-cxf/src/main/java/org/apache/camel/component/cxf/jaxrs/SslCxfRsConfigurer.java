begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|component
operator|.
name|cxf
operator|.
name|common
operator|.
name|AbstractSslEndpointConfigurer
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
name|jsse
operator|.
name|SSLContextParameters
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
name|AbstractJAXRSFactoryBean
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
name|client
operator|.
name|Client
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
name|client
operator|.
name|WebClient
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
name|http
operator|.
name|HTTPConduit
import|;
end_import

begin_class
DECL|class|SslCxfRsConfigurer
specifier|public
specifier|final
class|class
name|SslCxfRsConfigurer
extends|extends
name|AbstractSslEndpointConfigurer
implements|implements
name|CxfRsConfigurer
block|{
DECL|method|SslCxfRsConfigurer (SSLContextParameters sslContextParameters, CamelContext camelContext)
specifier|private
name|SslCxfRsConfigurer
parameter_list|(
name|SSLContextParameters
name|sslContextParameters
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|super
argument_list|(
name|sslContextParameters
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
block|}
DECL|method|create (SSLContextParameters sslContextParameters, CamelContext camelContext)
specifier|public
specifier|static
name|CxfRsConfigurer
name|create
parameter_list|(
name|SSLContextParameters
name|sslContextParameters
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
if|if
condition|(
name|sslContextParameters
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|ChainedCxfRsConfigurer
operator|.
name|NullCxfRsConfigurer
argument_list|()
return|;
block|}
else|else
block|{
return|return
operator|new
name|SslCxfRsConfigurer
argument_list|(
name|sslContextParameters
argument_list|,
name|camelContext
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|configure (AbstractJAXRSFactoryBean factoryBean)
specifier|public
name|void
name|configure
parameter_list|(
name|AbstractJAXRSFactoryBean
name|factoryBean
parameter_list|)
block|{     }
annotation|@
name|Override
DECL|method|configureClient (Client client)
specifier|public
name|void
name|configureClient
parameter_list|(
name|Client
name|client
parameter_list|)
block|{
name|HTTPConduit
name|httpConduit
init|=
operator|(
name|HTTPConduit
operator|)
name|WebClient
operator|.
name|getConfig
argument_list|(
name|client
argument_list|)
operator|.
name|getConduit
argument_list|()
decl_stmt|;
name|setupHttpConduit
argument_list|(
name|httpConduit
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configureServer (Server server)
specifier|public
name|void
name|configureServer
parameter_list|(
name|Server
name|server
parameter_list|)
block|{     }
block|}
end_class

end_unit

