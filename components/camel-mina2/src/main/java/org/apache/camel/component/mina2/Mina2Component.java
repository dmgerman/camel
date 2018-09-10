begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mina2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mina2
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
name|impl
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
name|mina
operator|.
name|core
operator|.
name|filterchain
operator|.
name|IoFilter
import|;
end_import

begin_comment
comment|/**  * Component for Apache MINA 2.x.  *  * @version   */
end_comment

begin_class
DECL|class|Mina2Component
specifier|public
class|class
name|Mina2Component
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
literal|"advanced"
argument_list|)
DECL|field|configuration
specifier|private
name|Mina2Configuration
name|configuration
decl_stmt|;
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
DECL|method|Mina2Component ()
specifier|public
name|Mina2Component
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
DECL|method|Mina2Component (CamelContext context)
specifier|public
name|Mina2Component
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
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
comment|// Using the configuration which set by the component as a default one
comment|// Since the configuration's properties will be set by the URI
comment|// we need to copy or create a new MinaConfiguration here
comment|// Using the configuration which set by the component as a default one
comment|// Since the configuration's properties will be set by the URI
comment|// we need to copy or create a new MinaConfiguration here
name|Mina2Configuration
name|config
decl_stmt|;
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|config
operator|=
name|configuration
operator|.
name|copy
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|config
operator|=
operator|new
name|Mina2Configuration
argument_list|()
expr_stmt|;
block|}
name|URI
name|u
init|=
operator|new
name|URI
argument_list|(
name|remaining
argument_list|)
decl_stmt|;
name|config
operator|.
name|setHost
argument_list|(
name|u
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setPort
argument_list|(
name|u
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setProtocol
argument_list|(
name|u
operator|.
name|getScheme
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setFilters
argument_list|(
name|resolveAndRemoveReferenceListParameter
argument_list|(
name|parameters
argument_list|,
literal|"filters"
argument_list|,
name|IoFilter
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|config
argument_list|,
name|parameters
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
return|return
name|createEndpoint
argument_list|(
name|uri
argument_list|,
name|config
argument_list|)
return|;
block|}
DECL|method|createEndpoint (Mina2Configuration config)
specifier|public
name|Endpoint
name|createEndpoint
parameter_list|(
name|Mina2Configuration
name|config
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|createEndpoint
argument_list|(
name|config
operator|.
name|getUriString
argument_list|()
argument_list|,
name|config
argument_list|)
return|;
block|}
DECL|method|createEndpoint (String uri, Mina2Configuration config)
specifier|private
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Mina2Configuration
name|config
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
name|String
name|protocol
init|=
name|config
operator|.
name|getProtocol
argument_list|()
decl_stmt|;
comment|// if mistyped uri then protocol can be null
name|Mina2Endpoint
name|endpoint
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|protocol
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|protocol
operator|.
name|equals
argument_list|(
literal|"tcp"
argument_list|)
operator|||
name|config
operator|.
name|isDatagramProtocol
argument_list|()
operator|||
name|protocol
operator|.
name|equals
argument_list|(
literal|"vm"
argument_list|)
condition|)
block|{
name|endpoint
operator|=
operator|new
name|Mina2Endpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|config
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
comment|// protocol not resolved so error
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unrecognised MINA protocol: "
operator|+
name|protocol
operator|+
literal|" for uri: "
operator|+
name|uri
argument_list|)
throw|;
block|}
comment|// set sync or async mode after endpoint is created
if|if
condition|(
name|config
operator|.
name|isSync
argument_list|()
condition|)
block|{
name|endpoint
operator|.
name|setExchangePattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|endpoint
operator|.
name|setExchangePattern
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
block|}
return|return
name|endpoint
return|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getConfiguration ()
specifier|public
name|Mina2Configuration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
comment|/**      * To use the shared mina configuration.      */
DECL|method|setConfiguration (Mina2Configuration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|Mina2Configuration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
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

