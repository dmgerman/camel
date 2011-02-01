begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|blueprint
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
name|RoutesBuilder
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
name|PackageScanFilter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|container
operator|.
name|BlueprintContainer
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
comment|/**  * A helper class which will find all {@link org.apache.camel.builder.RouteBuilder} instances in the  *  {@link org.osgi.service.blueprint.container.BlueprintContainer}.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ContextScanRouteBuilderFinder
specifier|public
class|class
name|ContextScanRouteBuilderFinder
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ContextScanRouteBuilderFinder
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|blueprintContainer
specifier|private
specifier|final
name|BlueprintContainer
name|blueprintContainer
decl_stmt|;
DECL|field|filter
specifier|private
specifier|final
name|PackageScanFilter
name|filter
decl_stmt|;
DECL|method|ContextScanRouteBuilderFinder (BlueprintCamelContext camelContext, PackageScanFilter filter)
specifier|public
name|ContextScanRouteBuilderFinder
parameter_list|(
name|BlueprintCamelContext
name|camelContext
parameter_list|,
name|PackageScanFilter
name|filter
parameter_list|)
block|{
name|this
operator|.
name|blueprintContainer
operator|=
name|camelContext
operator|.
name|getBlueprintContainer
argument_list|()
expr_stmt|;
name|this
operator|.
name|filter
operator|=
name|filter
expr_stmt|;
block|}
comment|/**      * Appends all the {@link org.apache.camel.builder.RouteBuilder} instances that can be found in the context      */
DECL|method|appendBuilders (List<RoutesBuilder> list)
specifier|public
name|void
name|appendBuilders
parameter_list|(
name|List
argument_list|<
name|RoutesBuilder
argument_list|>
name|list
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|RoutesBuilder
argument_list|>
name|beans
init|=
name|BlueprintContainerRegistry
operator|.
name|lookupByType
argument_list|(
name|blueprintContainer
argument_list|,
name|RoutesBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|key
range|:
name|beans
operator|.
name|keySet
argument_list|()
control|)
block|{
name|Object
name|bean
init|=
name|beans
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Found RouteBuilder with id: "
operator|+
name|key
operator|+
literal|" -> "
operator|+
name|bean
argument_list|)
expr_stmt|;
block|}
comment|// certain beans should be ignored
if|if
condition|(
name|shouldIgnoreBean
argument_list|(
name|bean
argument_list|)
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Ignoring RouteBuilder id: "
operator|+
name|key
argument_list|)
expr_stmt|;
block|}
continue|continue;
block|}
if|if
condition|(
operator|!
name|isFilteredClass
argument_list|(
name|bean
argument_list|)
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Ignoring filtered RouteBuilder id: "
operator|+
name|key
operator|+
literal|" as class: "
operator|+
name|bean
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
continue|continue;
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Adding instantiated RouteBuilder id: "
operator|+
name|key
operator|+
literal|" as class: "
operator|+
name|bean
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|list
operator|.
name|add
argument_list|(
operator|(
name|RoutesBuilder
operator|)
name|bean
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|shouldIgnoreBean (Object bean)
specifier|protected
name|boolean
name|shouldIgnoreBean
parameter_list|(
name|Object
name|bean
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
DECL|method|isFilteredClass (Object bean)
specifier|protected
name|boolean
name|isFilteredClass
parameter_list|(
name|Object
name|bean
parameter_list|)
block|{
if|if
condition|(
name|filter
operator|!=
literal|null
condition|)
block|{
return|return
name|filter
operator|.
name|matches
argument_list|(
name|bean
operator|.
name|getClass
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

