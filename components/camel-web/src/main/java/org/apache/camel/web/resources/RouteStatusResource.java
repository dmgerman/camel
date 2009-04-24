begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.web.resources
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|web
operator|.
name|resources
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Consumes
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|GET
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|POST
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Produces
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|jersey
operator|.
name|api
operator|.
name|representation
operator|.
name|Form
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
name|ServiceStatus
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
name|model
operator|.
name|RouteDefinition
import|;
end_import

begin_comment
comment|/**  * Represents the status of a single single Camel Route which is used to implement one or more  *<a href="http://camel.apache.org/enterprise-integration-patterns.html">Enterprise Integration Paterns</a>  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|RouteStatusResource
specifier|public
class|class
name|RouteStatusResource
block|{
DECL|field|routeResource
specifier|private
name|RouteResource
name|routeResource
decl_stmt|;
DECL|method|RouteStatusResource (RouteResource routeResource)
specifier|public
name|RouteStatusResource
parameter_list|(
name|RouteResource
name|routeResource
parameter_list|)
block|{
name|this
operator|.
name|routeResource
operator|=
name|routeResource
expr_stmt|;
block|}
DECL|method|getRoute ()
specifier|public
name|RouteDefinition
name|getRoute
parameter_list|()
block|{
return|return
name|routeResource
operator|.
name|getRoute
argument_list|()
return|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|routeResource
operator|.
name|getCamelContext
argument_list|()
return|;
block|}
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|TEXT_PLAIN
argument_list|)
DECL|method|getStatusText ()
specifier|public
name|String
name|getStatusText
parameter_list|()
block|{
name|ServiceStatus
name|status
init|=
name|getStatus
argument_list|()
decl_stmt|;
if|if
condition|(
name|status
operator|!=
literal|null
condition|)
block|{
return|return
name|status
operator|.
name|toString
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|getStatus ()
specifier|public
name|ServiceStatus
name|getStatus
parameter_list|()
block|{
return|return
name|getRoute
argument_list|()
operator|.
name|getStatus
argument_list|()
return|;
block|}
annotation|@
name|POST
annotation|@
name|Consumes
argument_list|(
name|MediaType
operator|.
name|TEXT_PLAIN
argument_list|)
DECL|method|setStatus (String status)
specifier|public
name|Response
name|setStatus
parameter_list|(
name|String
name|status
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|status
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|status
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"start"
argument_list|)
condition|)
block|{
name|getCamelContext
argument_list|()
operator|.
name|startRoute
argument_list|(
name|getRoute
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|status
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"stop"
argument_list|)
condition|)
block|{
name|getCamelContext
argument_list|()
operator|.
name|stopRoute
argument_list|(
name|getRoute
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
block|}
return|return
name|Response
operator|.
name|noContent
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
comment|/**      * Sets the status of this route to either "start" or "stop"      *      * @param formData is the form data POSTed typically from a HTML form with the<code>status</code> field      *                 set to either "start" or "stop"      */
annotation|@
name|POST
annotation|@
name|Consumes
argument_list|(
literal|"application/x-www-form-urlencoded"
argument_list|)
DECL|method|setStatus (Form formData)
specifier|public
name|Response
name|setStatus
parameter_list|(
name|Form
name|formData
parameter_list|)
throws|throws
name|Exception
block|{
comment|// TODO replace the Form class with an injected bean?
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Received form! "
operator|+
name|formData
argument_list|)
expr_stmt|;
name|String
name|status
init|=
name|formData
operator|.
name|getFirst
argument_list|(
literal|"status"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|setStatus
argument_list|(
name|status
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|seeOther
argument_list|(
operator|new
name|URI
argument_list|(
literal|"/routes"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit

