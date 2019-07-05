begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pulsar
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pulsar
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
name|component
operator|.
name|pulsar
operator|.
name|configuration
operator|.
name|PulsarConfiguration
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
name|pulsar
operator|.
name|utils
operator|.
name|AutoConfiguration
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
name|pulsar
operator|.
name|utils
operator|.
name|PulsarPath
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
name|pulsar
operator|.
name|client
operator|.
name|api
operator|.
name|PulsarClient
import|;
end_import

begin_class
annotation|@
name|Component
argument_list|(
literal|"pulsar"
argument_list|)
DECL|class|PulsarComponent
specifier|public
class|class
name|PulsarComponent
extends|extends
name|DefaultComponent
block|{
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|autoConfiguration
specifier|private
name|AutoConfiguration
name|autoConfiguration
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|pulsarClient
specifier|private
name|PulsarClient
name|pulsarClient
decl_stmt|;
DECL|method|PulsarComponent ()
specifier|public
name|PulsarComponent
parameter_list|()
block|{     }
DECL|method|PulsarComponent (CamelContext context)
specifier|public
name|PulsarComponent
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
DECL|method|createEndpoint (final String uri, final String path, final Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|,
specifier|final
name|String
name|path
parameter_list|,
specifier|final
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
specifier|final
name|PulsarConfiguration
name|configuration
init|=
operator|new
name|PulsarConfiguration
argument_list|()
decl_stmt|;
name|setProperties
argument_list|(
name|configuration
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
if|if
condition|(
name|autoConfiguration
operator|!=
literal|null
condition|)
block|{
name|setProperties
argument_list|(
name|autoConfiguration
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
if|if
condition|(
name|autoConfiguration
operator|.
name|isAutoConfigurable
argument_list|()
condition|)
block|{
name|autoConfiguration
operator|.
name|ensureNameSpaceAndTenant
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
block|}
name|PulsarEndpoint
name|answer
init|=
operator|new
name|PulsarEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setPulsarConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setPulsarClient
argument_list|(
name|pulsarClient
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|answer
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|PulsarPath
name|pp
init|=
operator|new
name|PulsarPath
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|pp
operator|.
name|isAutoConfigurable
argument_list|()
condition|)
block|{
name|answer
operator|.
name|setPersistence
argument_list|(
name|pp
operator|.
name|getPersistence
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setTenant
argument_list|(
name|pp
operator|.
name|getTenant
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setNamespace
argument_list|(
name|pp
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setTopic
argument_list|(
name|pp
operator|.
name|getTopic
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Pulsar name structure is invalid: was "
operator|+
name|path
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|getAutoConfiguration ()
specifier|public
name|AutoConfiguration
name|getAutoConfiguration
parameter_list|()
block|{
return|return
name|autoConfiguration
return|;
block|}
comment|/**      * The pulsar auto configuration      */
DECL|method|setAutoConfiguration (AutoConfiguration autoConfiguration)
specifier|public
name|void
name|setAutoConfiguration
parameter_list|(
name|AutoConfiguration
name|autoConfiguration
parameter_list|)
block|{
name|this
operator|.
name|autoConfiguration
operator|=
name|autoConfiguration
expr_stmt|;
block|}
DECL|method|getPulsarClient ()
specifier|public
name|PulsarClient
name|getPulsarClient
parameter_list|()
block|{
return|return
name|pulsarClient
return|;
block|}
comment|/**      * The pulsar client      */
DECL|method|setPulsarClient (PulsarClient pulsarClient)
specifier|public
name|void
name|setPulsarClient
parameter_list|(
name|PulsarClient
name|pulsarClient
parameter_list|)
block|{
name|this
operator|.
name|pulsarClient
operator|=
name|pulsarClient
expr_stmt|;
block|}
block|}
end_class

end_unit

