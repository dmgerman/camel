begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot.actuate.endpoint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
operator|.
name|actuate
operator|.
name|endpoint
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
name|spring
operator|.
name|boot
operator|.
name|CamelAutoConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|actuate
operator|.
name|autoconfigure
operator|.
name|endpoint
operator|.
name|EndpointAutoConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|actuate
operator|.
name|autoconfigure
operator|.
name|endpoint
operator|.
name|condition
operator|.
name|ConditionalOnEnabledEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|actuate
operator|.
name|autoconfigure
operator|.
name|endpoint
operator|.
name|web
operator|.
name|WebEndpointAutoConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|AutoConfigureAfter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|condition
operator|.
name|ConditionalOnBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|condition
operator|.
name|ConditionalOnClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|condition
operator|.
name|ConditionalOnMissingBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|EnableConfigurationProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Bean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Configuration
import|;
end_import

begin_comment
comment|/**  * Auto configuration for the {@link CamelRoutesEndpoint}.  */
end_comment

begin_class
annotation|@
name|Configuration
annotation|@
name|EnableConfigurationProperties
argument_list|(
block|{
name|CamelRoutesEndpointProperties
operator|.
name|class
block|}
argument_list|)
annotation|@
name|ConditionalOnClass
argument_list|(
block|{
name|CamelRoutesEndpoint
operator|.
name|class
block|}
argument_list|)
annotation|@
name|ConditionalOnBean
argument_list|(
name|CamelAutoConfiguration
operator|.
name|class
argument_list|)
annotation|@
name|AutoConfigureAfter
argument_list|(
name|CamelAutoConfiguration
operator|.
name|class
argument_list|)
DECL|class|CamelRoutesEndpointAutoConfiguration
specifier|public
class|class
name|CamelRoutesEndpointAutoConfiguration
block|{
DECL|field|properties
specifier|private
name|CamelRoutesEndpointProperties
name|properties
decl_stmt|;
DECL|method|CamelRoutesEndpointAutoConfiguration (CamelRoutesEndpointProperties properties)
specifier|public
name|CamelRoutesEndpointAutoConfiguration
parameter_list|(
name|CamelRoutesEndpointProperties
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
annotation|@
name|Bean
annotation|@
name|ConditionalOnClass
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|)
annotation|@
name|ConditionalOnMissingBean
annotation|@
name|ConditionalOnEnabledEndpoint
DECL|method|camelEndpoint (CamelContext camelContext)
specifier|public
name|CamelRoutesEndpoint
name|camelEndpoint
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
return|return
operator|new
name|CamelRoutesEndpoint
argument_list|(
name|camelContext
argument_list|,
name|properties
argument_list|)
return|;
block|}
block|}
end_class

end_unit

