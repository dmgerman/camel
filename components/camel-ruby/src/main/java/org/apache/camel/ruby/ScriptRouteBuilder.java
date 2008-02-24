begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.ruby
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ruby
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
name|builder
operator|.
name|RouteBuilder
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
name|ProcessorType
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
name|RouteType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * Provide some helper methods for building routes from scripting languages  * with a minimum amount of noise using state for the current node in the DSL  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ScriptRouteBuilder
specifier|public
specifier|abstract
class|class
name|ScriptRouteBuilder
extends|extends
name|RouteBuilder
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|ScriptRouteBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|node
specifier|protected
name|ProcessorType
name|node
decl_stmt|;
DECL|method|ScriptRouteBuilder ()
specifier|public
name|ScriptRouteBuilder
parameter_list|()
block|{     }
DECL|method|ScriptRouteBuilder (CamelContext context)
specifier|public
name|ScriptRouteBuilder
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
DECL|method|configureRoute (RouteType route)
specifier|protected
name|void
name|configureRoute
parameter_list|(
name|RouteType
name|route
parameter_list|)
block|{
name|super
operator|.
name|configureRoute
argument_list|(
name|route
argument_list|)
expr_stmt|;
name|this
operator|.
name|node
operator|=
name|route
expr_stmt|;
block|}
DECL|method|to (String uri)
specifier|public
name|ProcessorType
name|to
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|getNode
argument_list|()
operator|.
name|to
argument_list|(
name|uri
argument_list|)
return|;
block|}
DECL|method|to (Endpoint endpoint)
specifier|public
name|ProcessorType
name|to
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
return|return
name|getNode
argument_list|()
operator|.
name|to
argument_list|(
name|endpoint
argument_list|)
return|;
block|}
DECL|method|getNode ()
specifier|public
name|ProcessorType
name|getNode
parameter_list|()
block|{
if|if
condition|(
name|node
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"You must define a route first via the from() method"
argument_list|)
throw|;
block|}
return|return
name|node
return|;
block|}
DECL|method|setNode (ProcessorType node)
specifier|public
name|void
name|setNode
parameter_list|(
name|ProcessorType
name|node
parameter_list|)
block|{
name|this
operator|.
name|node
operator|=
name|node
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Node is now: "
operator|+
name|node
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

