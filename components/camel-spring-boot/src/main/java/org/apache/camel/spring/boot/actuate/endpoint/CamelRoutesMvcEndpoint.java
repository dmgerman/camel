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
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Supplier
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
name|HttpStatus
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
name|GetMapping
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
name|PostMapping
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
name|RequestAttribute
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
name|ResponseStatus
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
comment|/**      * Default path      */
DECL|field|PATH
specifier|public
specifier|static
specifier|final
name|String
name|PATH
init|=
literal|"/camel/routes"
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
name|setPath
argument_list|(
name|PATH
argument_list|)
expr_stmt|;
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
comment|// ********************************************
comment|// Endpoints
comment|// ********************************************
annotation|@
name|ResponseBody
annotation|@
name|GetMapping
argument_list|(
name|value
operator|=
literal|"/{id}/detail"
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
DECL|method|detail ( @athVariable String id)
specifier|public
name|Object
name|detail
parameter_list|(
annotation|@
name|PathVariable
name|String
name|id
parameter_list|)
block|{
return|return
name|doIfEnabled
argument_list|(
parameter_list|()
lambda|->
block|{
name|Object
name|result
init|=
name|delegate
operator|.
name|getRouteDetailsInfo
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
throw|throw
operator|new
name|NoSuchRouteException
argument_list|(
literal|"No such route "
operator|+
name|id
argument_list|)
throw|;
block|}
return|return
name|result
return|;
block|}
argument_list|)
return|;
block|}
annotation|@
name|ResponseBody
annotation|@
name|GetMapping
argument_list|(
name|value
operator|=
literal|"/{id}/info"
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
DECL|method|info ( @athVariable String id)
specifier|public
name|Object
name|info
parameter_list|(
annotation|@
name|PathVariable
name|String
name|id
parameter_list|)
block|{
return|return
name|doIfEnabled
argument_list|(
parameter_list|()
lambda|->
block|{
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
throw|throw
operator|new
name|NoSuchRouteException
argument_list|(
literal|"No such route "
operator|+
name|id
argument_list|)
throw|;
block|}
return|return
name|result
return|;
block|}
argument_list|)
return|;
block|}
annotation|@
name|ResponseBody
annotation|@
name|PostMapping
argument_list|(
name|value
operator|=
literal|"/{id}/stop"
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
DECL|method|stop ( @athVariable String id, @RequestAttribute(required = false) Long timeout, @RequestAttribute(required = false) Boolean abortAfterTimeout)
specifier|public
name|Object
name|stop
parameter_list|(
annotation|@
name|PathVariable
name|String
name|id
parameter_list|,
annotation|@
name|RequestAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
name|Long
name|timeout
parameter_list|,
annotation|@
name|RequestAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
name|Boolean
name|abortAfterTimeout
parameter_list|)
block|{
return|return
name|doIfEnabled
argument_list|(
parameter_list|()
lambda|->
block|{
try|try
block|{
name|delegate
operator|.
name|stopRoute
argument_list|(
name|id
argument_list|,
name|Optional
operator|.
name|ofNullable
argument_list|(
name|timeout
argument_list|)
argument_list|,
name|Optional
operator|.
name|of
argument_list|(
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|,
name|Optional
operator|.
name|ofNullable
argument_list|(
name|abortAfterTimeout
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
name|GenericException
argument_list|(
literal|"Error stopping route "
operator|+
name|id
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|ResponseEntity
operator|.
name|ok
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
argument_list|)
return|;
block|}
annotation|@
name|ResponseBody
annotation|@
name|PostMapping
argument_list|(
name|value
operator|=
literal|"/{id}/start"
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
DECL|method|start ( @athVariable String id)
specifier|public
name|Object
name|start
parameter_list|(
annotation|@
name|PathVariable
name|String
name|id
parameter_list|)
block|{
return|return
name|doIfEnabled
argument_list|(
parameter_list|()
lambda|->
block|{
try|try
block|{
name|delegate
operator|.
name|startRoute
argument_list|(
name|id
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
name|GenericException
argument_list|(
literal|"Error starting route "
operator|+
name|id
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|ResponseEntity
operator|.
name|ok
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
argument_list|)
return|;
block|}
annotation|@
name|ResponseBody
annotation|@
name|PostMapping
argument_list|(
name|value
operator|=
literal|"/{id}/suspend"
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
DECL|method|suspend ( @athVariable String id, @RequestAttribute(required = false) Long timeout)
specifier|public
name|Object
name|suspend
parameter_list|(
annotation|@
name|PathVariable
name|String
name|id
parameter_list|,
annotation|@
name|RequestAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
name|Long
name|timeout
parameter_list|)
block|{
return|return
name|doIfEnabled
argument_list|(
parameter_list|()
lambda|->
block|{
try|try
block|{
name|delegate
operator|.
name|suspendRoute
argument_list|(
name|id
argument_list|,
name|Optional
operator|.
name|ofNullable
argument_list|(
name|timeout
argument_list|)
argument_list|,
name|Optional
operator|.
name|of
argument_list|(
name|TimeUnit
operator|.
name|SECONDS
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
name|GenericException
argument_list|(
literal|"Error suspending route "
operator|+
name|id
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|ResponseEntity
operator|.
name|ok
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
argument_list|)
return|;
block|}
annotation|@
name|ResponseBody
annotation|@
name|PostMapping
argument_list|(
name|value
operator|=
literal|"/{id}/resume"
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
DECL|method|resume ( @athVariable String id)
specifier|public
name|Object
name|resume
parameter_list|(
annotation|@
name|PathVariable
name|String
name|id
parameter_list|)
block|{
return|return
name|doIfEnabled
argument_list|(
parameter_list|()
lambda|->
block|{
try|try
block|{
name|delegate
operator|.
name|resumeRoute
argument_list|(
name|id
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
name|GenericException
argument_list|(
literal|"Error resuming route "
operator|+
name|id
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|ResponseEntity
operator|.
name|ok
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
argument_list|)
return|;
block|}
comment|// ********************************************
comment|// Helpers
comment|// ********************************************
DECL|method|doIfEnabled (Supplier<Object> supplier)
specifier|private
name|Object
name|doIfEnabled
parameter_list|(
name|Supplier
argument_list|<
name|Object
argument_list|>
name|supplier
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
return|return
name|supplier
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
annotation|@
name|ResponseStatus
argument_list|(
name|value
operator|=
name|HttpStatus
operator|.
name|INTERNAL_SERVER_ERROR
argument_list|)
DECL|class|GenericException
specifier|public
specifier|static
class|class
name|GenericException
extends|extends
name|RuntimeException
block|{
DECL|method|GenericException (String message, Throwable cause)
specifier|public
name|GenericException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
annotation|@
name|ResponseStatus
argument_list|(
name|value
operator|=
name|HttpStatus
operator|.
name|NOT_FOUND
argument_list|,
name|reason
operator|=
literal|"No such route"
argument_list|)
DECL|class|NoSuchRouteException
specifier|public
specifier|static
class|class
name|NoSuchRouteException
extends|extends
name|RuntimeException
block|{
DECL|method|NoSuchRouteException (String message)
specifier|public
name|NoSuchRouteException
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

