begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.nats
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|nats
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
name|spi
operator|.
name|Metadata
import|;
end_import

begin_class
DECL|class|NatsComponent
specifier|public
class|class
name|NatsComponent
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
annotation|@
name|Override
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
name|NatsConfiguration
name|config
init|=
operator|new
name|NatsConfiguration
argument_list|()
decl_stmt|;
name|setProperties
argument_list|(
name|config
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|config
operator|.
name|setServers
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
if|if
condition|(
name|config
operator|.
name|getSslContextParameters
argument_list|()
operator|==
literal|null
condition|)
block|{
name|config
operator|.
name|setSslContextParameters
argument_list|(
name|retrieveGlobalSslContextParameters
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|NatsEndpoint
name|endpoint
init|=
operator|new
name|NatsEndpoint
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
annotation|@
name|Override
DECL|method|isUseGlobalSslContextParameters ()
specifier|public
name|boolean
name|isUseGlobalSslContextParameters
parameter_list|()
block|{
return|return
name|this
operator|.
name|useGlobalSslContextParameters
return|;
block|}
comment|/**      * Enable usage of global SSL context parameters.      */
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

