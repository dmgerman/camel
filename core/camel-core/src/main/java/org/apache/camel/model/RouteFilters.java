begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *<p>  * http://www.apache.org/licenses/LICENSE-2.0  *<p>  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|function
operator|.
name|Function
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
name|PatternHelper
import|;
end_import

begin_comment
comment|/**  * Used for filtering routes to only include routes matching a function.  */
end_comment

begin_class
DECL|class|RouteFilters
specifier|public
class|class
name|RouteFilters
implements|implements
name|Function
argument_list|<
name|RouteDefinition
argument_list|,
name|Boolean
argument_list|>
block|{
DECL|field|pattern
specifier|public
specifier|final
name|String
name|pattern
decl_stmt|;
comment|/**      * Used for filtering routes to only include routes matching the given pattern, which follows the following rules:      *      * - Match by route id      * - Match by route input endpoint uri      *      * The matching is using exact match, by wildcard and regular expression as documented by {@link PatternHelper#matchPattern(String, String)}.      *      * For example to only include routes which starts with foo in their route id's, use: foo&#42;      * And to only include routes which starts from JMS endpoints, use: jms:&#42;      */
DECL|method|filterByPattern (String pattern)
specifier|public
specifier|static
name|RouteFilters
name|filterByPattern
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
return|return
operator|new
name|RouteFilters
argument_list|(
name|pattern
argument_list|)
return|;
block|}
DECL|method|RouteFilters (String pattern)
specifier|private
name|RouteFilters
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (RouteDefinition route)
specifier|public
name|Boolean
name|apply
parameter_list|(
name|RouteDefinition
name|route
parameter_list|)
block|{
name|boolean
name|match
init|=
literal|false
decl_stmt|;
name|String
name|id
init|=
name|route
operator|.
name|getId
argument_list|()
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
name|match
operator|=
name|PatternHelper
operator|.
name|matchPattern
argument_list|(
name|id
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|match
operator|&&
name|route
operator|.
name|getInput
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|uri
init|=
name|route
operator|.
name|getInput
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
decl_stmt|;
name|match
operator|=
name|PatternHelper
operator|.
name|matchPattern
argument_list|(
name|uri
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
block|}
return|return
name|match
return|;
block|}
block|}
end_class

end_unit

