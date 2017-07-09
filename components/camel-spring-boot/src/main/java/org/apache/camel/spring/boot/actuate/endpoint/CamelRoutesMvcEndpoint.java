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
name|springframework
operator|.
name|boot
operator|.
name|actuate
operator|.
name|endpoint
operator|.
name|mvc
operator|.
name|ActuatorMediaTypes
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
name|endpoint
operator|.
name|mvc
operator|.
name|EndpointMvcAdapter
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
name|endpoint
operator|.
name|mvc
operator|.
name|MvcEndpoint
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
name|ConfigurationProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|http
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|http
operator|.
name|ResponseEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|web
operator|.
name|bind
operator|.
name|annotation
operator|.
name|PathVariable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|web
operator|.
name|bind
operator|.
name|annotation
operator|.
name|RequestMapping
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|web
operator|.
name|bind
operator|.
name|annotation
operator|.
name|RequestMethod
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|web
operator|.
name|bind
operator|.
name|annotation
operator|.
name|ResponseBody
import|;
end_import

begin_comment
comment|/**  * Adapter to expose {@link CamelRoutesEndpoint} as an {@link MvcEndpoint}.  */
end_comment

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"endpoints."
operator|+
name|CamelRoutesEndpoint
operator|.
name|ENDPOINT_ID
argument_list|)
DECL|class|CamelRoutesMvcEndpoint
specifier|public
class|class
name|CamelRoutesMvcEndpoint
extends|extends
name|EndpointMvcAdapter
block|{
DECL|field|NOT_FOUND
specifier|private
specifier|static
specifier|final
name|ResponseEntity
argument_list|<
name|?
argument_list|>
name|NOT_FOUND
init|=
name|ResponseEntity
operator|.
name|notFound
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
DECL|field|delegate
specifier|private
specifier|final
name|CamelRoutesEndpoint
name|delegate
decl_stmt|;
DECL|method|CamelRoutesMvcEndpoint (CamelRoutesEndpoint delegate)
specifier|public
name|CamelRoutesMvcEndpoint
parameter_list|(
name|CamelRoutesEndpoint
name|delegate
parameter_list|)
block|{
name|super
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
annotation|@
name|RequestMapping
argument_list|(
name|method
operator|=
name|RequestMethod
operator|.
name|GET
argument_list|,
name|value
operator|=
literal|"/{id}"
argument_list|,
name|produces
operator|=
block|{
name|ActuatorMediaTypes
operator|.
name|APPLICATION_ACTUATOR_V1_JSON_VALUE
block|,
name|MediaType
operator|.
name|APPLICATION_JSON_VALUE
block|}
argument_list|)
annotation|@
name|ResponseBody
DECL|method|get (@athVariable String id)
specifier|public
name|Object
name|get
parameter_list|(
annotation|@
name|PathVariable
name|String
name|id
parameter_list|)
block|{
if|if
condition|(
operator|!
name|delegate
operator|.
name|isEnabled
argument_list|()
condition|)
block|{
return|return
name|getDisabledResponse
argument_list|()
return|;
block|}
name|Object
name|result
init|=
name|delegate
operator|.
name|getRouteInfo
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|result
operator|=
name|NOT_FOUND
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

