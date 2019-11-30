begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.engine
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|engine
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|Route
import|;
end_import

begin_comment
comment|/**  * A {@link SupervisingRouteController.Filter} which blacklists routes.  */
end_comment

begin_class
DECL|class|SupervisingRouteControllerFilters
specifier|public
specifier|final
class|class
name|SupervisingRouteControllerFilters
block|{
DECL|method|SupervisingRouteControllerFilters ()
specifier|private
name|SupervisingRouteControllerFilters
parameter_list|()
block|{     }
DECL|class|BlackList
specifier|public
specifier|static
specifier|final
class|class
name|BlackList
implements|implements
name|SupervisingRouteController
operator|.
name|Filter
block|{
DECL|field|names
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|names
decl_stmt|;
DECL|method|BlackList (String name)
specifier|public
name|BlackList
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|BlackList (Collection<String> names)
specifier|public
name|BlackList
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|names
parameter_list|)
block|{
name|this
operator|.
name|names
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|names
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (Route route)
specifier|public
name|SupervisingRouteController
operator|.
name|FilterResult
name|apply
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
name|boolean
name|supervised
init|=
operator|!
name|names
operator|.
name|contains
argument_list|(
name|route
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|SupervisingRouteController
operator|.
name|FilterResult
argument_list|(
name|supervised
argument_list|,
name|supervised
condition|?
literal|null
else|:
literal|"black-listed"
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

