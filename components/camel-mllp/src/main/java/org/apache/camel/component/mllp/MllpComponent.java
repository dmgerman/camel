begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mllp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mllp
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
name|impl
operator|.
name|UriEndpointComponent
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

begin_comment
comment|/**  * Represents the component that manages {@link MllpEndpoint}.  */
end_comment

begin_class
DECL|class|MllpComponent
specifier|public
class|class
name|MllpComponent
extends|extends
name|UriEndpointComponent
block|{
DECL|field|MLLP_LOG_PHI_PROPERTY
specifier|public
specifier|static
specifier|final
name|String
name|MLLP_LOG_PHI_PROPERTY
init|=
literal|"org.apache.camel.component.mllp.logPHI"
decl_stmt|;
DECL|field|MLLP_LOG_PHI_MAX_BYTES_PROPERTY
specifier|public
specifier|static
specifier|final
name|String
name|MLLP_LOG_PHI_MAX_BYTES_PROPERTY
init|=
literal|"org.apache.camel.component.mllp.logPHI.maxBytes"
decl_stmt|;
DECL|field|DEFAULT_LOG_PHI
specifier|public
specifier|static
specifier|final
name|boolean
name|DEFAULT_LOG_PHI
init|=
literal|true
decl_stmt|;
DECL|field|DEFAULT_LOG_PHI_MAX_BYTES
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_LOG_PHI_MAX_BYTES
init|=
literal|5120
decl_stmt|;
DECL|field|log
specifier|static
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MllpComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|logPhi
specifier|static
name|Boolean
name|logPhi
decl_stmt|;
DECL|field|logPhiMaxBytes
specifier|static
name|Integer
name|logPhiMaxBytes
decl_stmt|;
DECL|field|configuration
name|MllpConfiguration
name|configuration
decl_stmt|;
DECL|method|MllpComponent ()
specifier|public
name|MllpComponent
parameter_list|()
block|{
name|super
argument_list|(
name|MllpEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|MllpComponent (CamelContext context)
specifier|public
name|MllpComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|MllpEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uriString, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uriString
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
name|MllpEndpoint
name|endpoint
init|=
operator|new
name|MllpEndpoint
argument_list|(
name|uriString
argument_list|,
name|this
argument_list|,
name|hasConfiguration
argument_list|()
condition|?
name|configuration
operator|.
name|copy
argument_list|()
else|:
operator|new
name|MllpConfiguration
argument_list|()
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setBridgeErrorHandler
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
comment|// Make sure it has a host - may just be a port
name|int
name|colonIndex
init|=
name|remaining
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
if|if
condition|(
operator|-
literal|1
operator|!=
name|colonIndex
condition|)
block|{
name|endpoint
operator|.
name|setHostname
argument_list|(
name|remaining
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|colonIndex
argument_list|)
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setPort
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|remaining
operator|.
name|substring
argument_list|(
name|colonIndex
operator|+
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// No host specified - leave the default host and set the port
name|endpoint
operator|.
name|setPort
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|remaining
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|endpoint
return|;
block|}
DECL|method|hasLogPhi ()
specifier|public
specifier|static
name|boolean
name|hasLogPhi
parameter_list|()
block|{
return|return
name|logPhi
operator|!=
literal|null
return|;
block|}
DECL|method|isLogPhi ()
specifier|public
specifier|static
name|boolean
name|isLogPhi
parameter_list|()
block|{
if|if
condition|(
name|hasLogPhi
argument_list|()
condition|)
block|{
return|return
name|logPhi
return|;
block|}
name|boolean
name|answer
init|=
name|DEFAULT_LOG_PHI
decl_stmt|;
name|String
name|logPhiProperty
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|MllpComponent
operator|.
name|MLLP_LOG_PHI_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|logPhiProperty
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|logPhiProperty
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Set the component to log PHI data.      *      * @param logPhi true enables PHI logging; false disables it.      */
DECL|method|setLogPhi (Boolean logPhi)
specifier|public
specifier|static
name|void
name|setLogPhi
parameter_list|(
name|Boolean
name|logPhi
parameter_list|)
block|{
name|MllpComponent
operator|.
name|logPhi
operator|=
name|logPhi
expr_stmt|;
block|}
DECL|method|hasLogPhiMaxBytes ()
specifier|public
specifier|static
name|boolean
name|hasLogPhiMaxBytes
parameter_list|()
block|{
return|return
name|logPhiMaxBytes
operator|!=
literal|null
return|;
block|}
DECL|method|getLogPhiMaxBytes ()
specifier|public
specifier|static
name|int
name|getLogPhiMaxBytes
parameter_list|()
block|{
if|if
condition|(
name|hasLogPhiMaxBytes
argument_list|()
condition|)
block|{
return|return
name|logPhiMaxBytes
return|;
block|}
name|int
name|answer
init|=
name|DEFAULT_LOG_PHI_MAX_BYTES
decl_stmt|;
name|String
name|logPhiProperty
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|MllpComponent
operator|.
name|MLLP_LOG_PHI_MAX_BYTES_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|logPhiProperty
operator|!=
literal|null
operator|&&
operator|!
name|logPhiProperty
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
try|try
block|{
name|answer
operator|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|logPhiProperty
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|numberFormatException
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Invalid Interger value '{}' for system property {} - using default value of {}"
argument_list|,
name|logPhiProperty
argument_list|,
name|MllpComponent
operator|.
name|MLLP_LOG_PHI_MAX_BYTES_PROPERTY
argument_list|,
name|answer
argument_list|)
expr_stmt|;
comment|// use DEFAULT_LOG_PHI_MAX_BYTES for a invalid entry
block|}
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Set the maximum number of bytes of PHI that will be logged in a log entry.      *      * @param logPhiMaxBytes the maximum number of bytes to log.      */
DECL|method|setLogPhiMaxBytes (Integer logPhiMaxBytes)
specifier|public
specifier|static
name|void
name|setLogPhiMaxBytes
parameter_list|(
name|Integer
name|logPhiMaxBytes
parameter_list|)
block|{
name|MllpComponent
operator|.
name|logPhiMaxBytes
operator|=
name|logPhiMaxBytes
expr_stmt|;
block|}
DECL|method|hasConfiguration ()
specifier|public
name|boolean
name|hasConfiguration
parameter_list|()
block|{
return|return
name|configuration
operator|!=
literal|null
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|MllpConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
comment|/**      * Sets the default configuration to use when creating MLLP endpoints.      *      * @param configuration the default configuration.      */
DECL|method|setConfiguration (MllpConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|MllpConfiguration
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
block|}
end_class

end_unit

