begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.thrift
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|thrift
package|;
end_package

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
name|util
operator|.
name|Map
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
name|SSLContextParametersAware
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
name|Metadata
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
name|annotations
operator|.
name|Component
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
name|DefaultComponent
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

begin_comment
comment|/**  * Represents the component that manages {@link ThriftEndpoint}.  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"thrift"
argument_list|)
DECL|class|ThriftComponent
specifier|public
class|class
name|ThriftComponent
extends|extends
name|DefaultComponent
implements|implements
name|SSLContextParametersAware
block|{
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|useGlobalSslContextParameters
specifier|private
name|boolean
name|useGlobalSslContextParameters
decl_stmt|;
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|ThriftConfiguration
name|config
init|=
operator|new
name|ThriftConfiguration
argument_list|()
decl_stmt|;
name|config
operator|=
name|parseConfiguration
argument_list|(
name|config
argument_list|,
name|uri
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|SSLContextParameters
name|sslParameters
init|=
name|config
operator|.
name|getSslParameters
argument_list|()
decl_stmt|;
if|if
condition|(
name|config
operator|.
name|getNegotiationType
argument_list|()
operator|==
name|ThriftNegotiationType
operator|.
name|SSL
operator|&&
name|sslParameters
operator|==
literal|null
condition|)
block|{
name|sslParameters
operator|=
name|retrieveGlobalSslContextParameters
argument_list|()
expr_stmt|;
name|config
operator|.
name|setSslParameters
argument_list|(
name|sslParameters
argument_list|)
expr_stmt|;
block|}
name|setProperties
argument_list|(
name|config
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|Endpoint
name|endpoint
init|=
operator|new
name|ThriftEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|config
argument_list|)
decl_stmt|;
return|return
name|endpoint
return|;
block|}
comment|/**      * Parses the configuration      *       * @return the parsed and valid configuration to use      */
DECL|method|parseConfiguration (ThriftConfiguration configuration, String remaining, Map<String, Object> parameters)
specifier|protected
name|ThriftConfiguration
name|parseConfiguration
parameter_list|(
name|ThriftConfiguration
name|configuration
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|configuration
operator|.
name|parseURI
argument_list|(
operator|new
name|URI
argument_list|(
name|remaining
argument_list|)
argument_list|,
name|parameters
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
name|configuration
return|;
block|}
comment|/**      * Determine if the thrift component is using global SSL context parameters      */
annotation|@
name|Override
DECL|method|isUseGlobalSslContextParameters ()
specifier|public
name|boolean
name|isUseGlobalSslContextParameters
parameter_list|()
block|{
return|return
name|useGlobalSslContextParameters
return|;
block|}
annotation|@
name|Override
DECL|method|setUseGlobalSslContextParameters (boolean useGlobalSslContextParameters)
specifier|public
name|void
name|setUseGlobalSslContextParameters
parameter_list|(
name|boolean
name|useGlobalSslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|useGlobalSslContextParameters
operator|=
name|useGlobalSslContextParameters
expr_stmt|;
block|}
block|}
end_class

end_unit

