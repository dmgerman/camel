begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * Helper for {@link RouteDefinition}  *<p/>  * Utility methods to help preparing {@link RouteDefinition} before they are added to  * {@link org.apache.camel.CamelContext}.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|RouteDefinitionHelper
specifier|public
specifier|final
class|class
name|RouteDefinitionHelper
block|{
DECL|method|RouteDefinitionHelper ()
specifier|private
name|RouteDefinitionHelper
parameter_list|()
block|{     }
DECL|method|initParent (RouteDefinition route)
specifier|public
specifier|static
name|void
name|initParent
parameter_list|(
name|RouteDefinition
name|route
parameter_list|)
block|{
for|for
control|(
name|ProcessorDefinition
name|output
range|:
name|route
operator|.
name|getOutputs
argument_list|()
control|)
block|{
name|output
operator|.
name|setParent
argument_list|(
name|route
argument_list|)
expr_stmt|;
if|if
condition|(
name|output
operator|.
name|getOutputs
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// recursive the outputs
name|initParent
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|initParent (ProcessorDefinition parent)
specifier|public
specifier|static
name|void
name|initParent
parameter_list|(
name|ProcessorDefinition
name|parent
parameter_list|)
block|{
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|children
init|=
name|parent
operator|.
name|getOutputs
argument_list|()
decl_stmt|;
for|for
control|(
name|ProcessorDefinition
name|child
range|:
name|children
control|)
block|{
name|child
operator|.
name|setParent
argument_list|(
name|parent
argument_list|)
expr_stmt|;
if|if
condition|(
name|child
operator|.
name|getOutputs
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// recursive the children
name|initParent
argument_list|(
name|child
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|prepareRouteForInit (RouteDefinition route, List<ProcessorDefinition> abstracts, List<ProcessorDefinition> lower)
specifier|public
specifier|static
name|void
name|prepareRouteForInit
parameter_list|(
name|RouteDefinition
name|route
parameter_list|,
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|abstracts
parameter_list|,
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|lower
parameter_list|)
block|{
comment|// filter the route into abstracts and lower
for|for
control|(
name|ProcessorDefinition
name|output
range|:
name|route
operator|.
name|getOutputs
argument_list|()
control|)
block|{
if|if
condition|(
name|output
operator|.
name|isAbstract
argument_list|()
condition|)
block|{
name|abstracts
operator|.
name|add
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|lower
operator|.
name|add
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

