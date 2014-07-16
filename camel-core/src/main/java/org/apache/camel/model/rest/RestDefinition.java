begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.rest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|rest
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElementRef
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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
name|ModelCamelContext
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
comment|/**  * Represents an XML&lt;rest/&gt; element  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"rest"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|RestDefinition
specifier|public
class|class
name|RestDefinition
block|{
annotation|@
name|XmlAttribute
DECL|field|component
specifier|private
name|String
name|component
decl_stmt|;
annotation|@
name|XmlElementRef
DECL|field|paths
specifier|private
name|List
argument_list|<
name|PathDefinition
argument_list|>
name|paths
init|=
operator|new
name|ArrayList
argument_list|<
name|PathDefinition
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|getComponent ()
specifier|public
name|String
name|getComponent
parameter_list|()
block|{
return|return
name|component
return|;
block|}
DECL|method|setComponent (String component)
specifier|public
name|void
name|setComponent
parameter_list|(
name|String
name|component
parameter_list|)
block|{
name|this
operator|.
name|component
operator|=
name|component
expr_stmt|;
block|}
DECL|method|getPaths ()
specifier|public
name|List
argument_list|<
name|PathDefinition
argument_list|>
name|getPaths
parameter_list|()
block|{
return|return
name|paths
return|;
block|}
DECL|method|setPaths (List<PathDefinition> paths)
specifier|public
name|void
name|setPaths
parameter_list|(
name|List
argument_list|<
name|PathDefinition
argument_list|>
name|paths
parameter_list|)
block|{
name|this
operator|.
name|paths
operator|=
name|paths
expr_stmt|;
block|}
comment|// Fluent API
comment|//-------------------------------------------------------------------------
comment|/**      * To use a specific Camel rest component      */
DECL|method|component (String componentId)
specifier|public
name|RestDefinition
name|component
parameter_list|(
name|String
name|componentId
parameter_list|)
block|{
name|setComponent
argument_list|(
name|componentId
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Defines the rest path to use      */
DECL|method|path (String uri)
specifier|public
name|PathDefinition
name|path
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|PathDefinition
name|answer
init|=
operator|new
name|PathDefinition
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setRest
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|getPaths
argument_list|()
operator|.
name|add
argument_list|(
name|answer
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Transforms this REST definition into a list of {@link org.apache.camel.model.RouteDefinition} which      * Camel routing engine can add and run. This allows us to define REST services using this      * REST DSL and turn those into regular Camel routes.      */
DECL|method|asRouteDefinition (ModelCamelContext camelContext)
specifier|public
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|asRouteDefinition
parameter_list|(
name|ModelCamelContext
name|camelContext
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|RouteDefinition
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|PathDefinition
name|path
range|:
name|getPaths
argument_list|()
control|)
block|{
name|String
name|uri
init|=
name|path
operator|.
name|getUri
argument_list|()
decl_stmt|;
for|for
control|(
name|VerbDefinition
name|verb
range|:
name|path
operator|.
name|getVerbs
argument_list|()
control|)
block|{
name|String
name|from
init|=
literal|"rest:"
operator|+
name|verb
operator|.
name|asVerb
argument_list|()
operator|+
literal|":"
operator|+
name|uri
operator|+
operator|(
name|getComponent
argument_list|()
operator|!=
literal|null
condition|?
literal|"?componentName="
operator|+
name|getComponent
argument_list|()
else|:
literal|""
operator|)
decl_stmt|;
name|RouteDefinition
name|route
init|=
operator|new
name|RouteDefinition
argument_list|()
decl_stmt|;
name|route
operator|.
name|fromRest
argument_list|(
name|from
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|route
argument_list|)
expr_stmt|;
name|route
operator|.
name|getOutputs
argument_list|()
operator|.
name|addAll
argument_list|(
name|verb
operator|.
name|getOutputs
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

