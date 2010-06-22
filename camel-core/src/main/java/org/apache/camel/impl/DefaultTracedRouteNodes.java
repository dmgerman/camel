begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Stack
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
name|atomic
operator|.
name|AtomicInteger
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
name|RouteNode
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
name|ProcessorDefinition
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
name|TracedRouteNodes
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultTracedRouteNodes
specifier|public
class|class
name|DefaultTracedRouteNodes
implements|implements
name|TracedRouteNodes
block|{
DECL|field|routeNodes
specifier|private
specifier|final
name|Stack
argument_list|<
name|List
argument_list|<
name|RouteNode
argument_list|>
argument_list|>
name|routeNodes
init|=
operator|new
name|Stack
argument_list|<
name|List
argument_list|<
name|RouteNode
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|nodeCounter
specifier|private
specifier|final
name|Map
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|,
name|AtomicInteger
argument_list|>
name|nodeCounter
init|=
operator|new
name|HashMap
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|,
name|AtomicInteger
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|DefaultTracedRouteNodes ()
specifier|public
name|DefaultTracedRouteNodes
parameter_list|()
block|{
comment|// create an empty list to start with
name|routeNodes
operator|.
name|push
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|RouteNode
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|addTraced (RouteNode entry)
specifier|public
name|void
name|addTraced
parameter_list|(
name|RouteNode
name|entry
parameter_list|)
block|{
name|List
argument_list|<
name|RouteNode
argument_list|>
name|list
init|=
name|routeNodes
operator|.
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
name|routeNodes
operator|.
name|peek
argument_list|()
decl_stmt|;
if|if
condition|(
name|list
operator|==
literal|null
condition|)
block|{
name|list
operator|=
operator|new
name|ArrayList
argument_list|<
name|RouteNode
argument_list|>
argument_list|()
expr_stmt|;
name|routeNodes
operator|.
name|push
argument_list|(
name|list
argument_list|)
expr_stmt|;
block|}
name|list
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
DECL|method|getLastNode ()
specifier|public
name|RouteNode
name|getLastNode
parameter_list|()
block|{
name|List
argument_list|<
name|RouteNode
argument_list|>
name|list
init|=
name|routeNodes
operator|.
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
name|routeNodes
operator|.
name|peek
argument_list|()
decl_stmt|;
if|if
condition|(
name|list
operator|==
literal|null
operator|||
name|list
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|list
operator|.
name|get
argument_list|(
name|list
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
return|;
block|}
DECL|method|getSecondLastNode ()
specifier|public
name|RouteNode
name|getSecondLastNode
parameter_list|()
block|{
name|List
argument_list|<
name|RouteNode
argument_list|>
name|list
init|=
name|routeNodes
operator|.
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
name|routeNodes
operator|.
name|peek
argument_list|()
decl_stmt|;
if|if
condition|(
name|list
operator|==
literal|null
operator|||
name|list
operator|.
name|isEmpty
argument_list|()
operator|||
name|list
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|list
operator|.
name|get
argument_list|(
name|list
operator|.
name|size
argument_list|()
operator|-
literal|2
argument_list|)
return|;
block|}
DECL|method|getNodes ()
specifier|public
name|List
argument_list|<
name|RouteNode
argument_list|>
name|getNodes
parameter_list|()
block|{
name|List
argument_list|<
name|RouteNode
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|RouteNode
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|List
argument_list|<
name|RouteNode
argument_list|>
name|list
range|:
name|routeNodes
control|)
block|{
name|answer
operator|.
name|addAll
argument_list|(
name|list
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|answer
argument_list|)
return|;
block|}
DECL|method|popBlock ()
specifier|public
name|void
name|popBlock
parameter_list|()
block|{
if|if
condition|(
operator|!
name|routeNodes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|routeNodes
operator|.
name|pop
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|pushBlock ()
specifier|public
name|void
name|pushBlock
parameter_list|()
block|{
comment|// push a new block and add the last node as starting point
name|RouteNode
name|last
init|=
name|getLastNode
argument_list|()
decl_stmt|;
name|routeNodes
operator|.
name|push
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|RouteNode
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|last
operator|!=
literal|null
condition|)
block|{
name|addTraced
argument_list|(
name|last
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|routeNodes
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|getAndIncrementCounter (ProcessorDefinition<?> node)
specifier|public
name|int
name|getAndIncrementCounter
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|node
parameter_list|)
block|{
name|AtomicInteger
name|count
init|=
name|nodeCounter
operator|.
name|get
argument_list|(
name|node
argument_list|)
decl_stmt|;
if|if
condition|(
name|count
operator|==
literal|null
condition|)
block|{
name|count
operator|=
operator|new
name|AtomicInteger
argument_list|()
expr_stmt|;
name|nodeCounter
operator|.
name|put
argument_list|(
name|node
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
return|return
name|count
operator|.
name|getAndIncrement
argument_list|()
return|;
block|}
block|}
end_class

end_unit

