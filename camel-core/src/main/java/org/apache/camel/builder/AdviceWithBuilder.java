begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
name|model
operator|.
name|PipelineDefinition
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
name|model
operator|.
name|RouteDefinition
import|;
end_import

begin_comment
comment|/**  * A builder when using the<a href="http://camel.apache.org/advicewith.html">advice with</a> feature.  */
end_comment

begin_class
DECL|class|AdviceWithBuilder
specifier|public
class|class
name|AdviceWithBuilder
parameter_list|<
name|T
extends|extends
name|ProcessorDefinition
parameter_list|<
name|?
parameter_list|>
parameter_list|>
block|{
DECL|field|builder
specifier|private
specifier|final
name|AdviceWithRouteBuilder
name|builder
decl_stmt|;
DECL|field|id
specifier|private
specifier|final
name|String
name|id
decl_stmt|;
DECL|field|toString
specifier|private
specifier|final
name|String
name|toString
decl_stmt|;
DECL|field|type
specifier|private
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|type
decl_stmt|;
DECL|field|selectFirst
specifier|private
name|boolean
name|selectFirst
decl_stmt|;
DECL|field|selectLast
specifier|private
name|boolean
name|selectLast
decl_stmt|;
DECL|field|selectFrom
specifier|private
name|int
name|selectFrom
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|selectTo
specifier|private
name|int
name|selectTo
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|maxDeep
specifier|private
name|int
name|maxDeep
init|=
operator|-
literal|1
decl_stmt|;
DECL|method|AdviceWithBuilder (AdviceWithRouteBuilder builder, String id, String toString, Class<T> type)
specifier|public
name|AdviceWithBuilder
parameter_list|(
name|AdviceWithRouteBuilder
name|builder
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|toString
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|this
operator|.
name|builder
operator|=
name|builder
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|toString
operator|=
name|toString
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
if|if
condition|(
name|id
operator|==
literal|null
operator|&&
name|toString
operator|==
literal|null
operator|&&
name|type
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Either id, toString or type must be specified"
argument_list|)
throw|;
block|}
block|}
comment|/**      * Will only apply the first node matched.      *      * @return the builder to build the nodes.      */
DECL|method|selectFirst ()
specifier|public
name|AdviceWithBuilder
argument_list|<
name|T
argument_list|>
name|selectFirst
parameter_list|()
block|{
name|selectFirst
operator|=
literal|true
expr_stmt|;
name|selectLast
operator|=
literal|false
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Will only apply the last node matched.      *      * @return the builder to build the nodes.      */
DECL|method|selectLast ()
specifier|public
name|AdviceWithBuilder
argument_list|<
name|T
argument_list|>
name|selectLast
parameter_list|()
block|{
name|selectLast
operator|=
literal|true
expr_stmt|;
name|selectFirst
operator|=
literal|false
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Will only apply the n'th node matched.      *      * @param index index of node to match (is 0-based)      * @return the builder to build the nodes.      */
DECL|method|selectIndex (int index)
specifier|public
name|AdviceWithBuilder
argument_list|<
name|T
argument_list|>
name|selectIndex
parameter_list|(
name|int
name|index
parameter_list|)
block|{
if|if
condition|(
name|index
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Index must be a non negative number, was: "
operator|+
name|index
argument_list|)
throw|;
block|}
name|selectFrom
operator|=
name|index
expr_stmt|;
name|selectTo
operator|=
name|index
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Will only apply the node in the index range matched.      *      * @param from from index of node to start matching (inclusive)      * @param to to index of node to stop matching (inclusive)      * @return the builder to build the nodes.      */
DECL|method|selectRange (int from, int to)
specifier|public
name|AdviceWithBuilder
argument_list|<
name|T
argument_list|>
name|selectRange
parameter_list|(
name|int
name|from
parameter_list|,
name|int
name|to
parameter_list|)
block|{
if|if
condition|(
name|from
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"From must be a non negative number, was: "
operator|+
name|from
argument_list|)
throw|;
block|}
if|if
condition|(
name|from
operator|>
name|to
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"From must be equal or lower than to. from: "
operator|+
name|from
operator|+
literal|", to: "
operator|+
name|to
argument_list|)
throw|;
block|}
name|selectFrom
operator|=
name|from
expr_stmt|;
name|selectTo
operator|=
name|to
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Will only apply for nodes maximum levels deep.      *<p/>      * The first level is<tt>1</tt>, and level<tt>2</tt> is the children of the the first level nodes, and so on.      *<p/>      * Use zero or negative value for unbounded level.      *      * @param maxDeep the maximum levels to traverse deep in the Camel route tree.      * @return the builder to build the nodes.      */
DECL|method|maxDeep (int maxDeep)
specifier|public
name|AdviceWithBuilder
argument_list|<
name|T
argument_list|>
name|maxDeep
parameter_list|(
name|int
name|maxDeep
parameter_list|)
block|{
if|if
condition|(
name|maxDeep
operator|==
literal|0
condition|)
block|{
comment|// disable it
name|this
operator|.
name|maxDeep
operator|=
operator|-
literal|1
expr_stmt|;
block|}
name|this
operator|.
name|maxDeep
operator|=
name|maxDeep
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Replaces the matched node(s) with the following nodes.      *      * @return the builder to build the nodes.      */
DECL|method|replace ()
specifier|public
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|replace
parameter_list|()
block|{
name|RouteDefinition
name|route
init|=
name|builder
operator|.
name|getOriginalRoute
argument_list|()
decl_stmt|;
name|PipelineDefinition
name|answer
init|=
operator|new
name|PipelineDefinition
argument_list|()
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|getAdviceWithTasks
argument_list|()
operator|.
name|add
argument_list|(
name|AdviceWithTasks
operator|.
name|replaceById
argument_list|(
name|route
argument_list|,
name|id
argument_list|,
name|answer
argument_list|,
name|selectFirst
argument_list|,
name|selectLast
argument_list|,
name|selectFrom
argument_list|,
name|selectTo
argument_list|,
name|maxDeep
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|toString
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|getAdviceWithTasks
argument_list|()
operator|.
name|add
argument_list|(
name|AdviceWithTasks
operator|.
name|replaceByToString
argument_list|(
name|route
argument_list|,
name|toString
argument_list|,
name|answer
argument_list|,
name|selectFirst
argument_list|,
name|selectLast
argument_list|,
name|selectFrom
argument_list|,
name|selectTo
argument_list|,
name|maxDeep
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|getAdviceWithTasks
argument_list|()
operator|.
name|add
argument_list|(
name|AdviceWithTasks
operator|.
name|replaceByType
argument_list|(
name|route
argument_list|,
name|type
argument_list|,
name|answer
argument_list|,
name|selectFirst
argument_list|,
name|selectLast
argument_list|,
name|selectFrom
argument_list|,
name|selectTo
argument_list|,
name|maxDeep
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Removes the matched node(s)      */
DECL|method|remove ()
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|RouteDefinition
name|route
init|=
name|builder
operator|.
name|getOriginalRoute
argument_list|()
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|getAdviceWithTasks
argument_list|()
operator|.
name|add
argument_list|(
name|AdviceWithTasks
operator|.
name|removeById
argument_list|(
name|route
argument_list|,
name|id
argument_list|,
name|selectFirst
argument_list|,
name|selectLast
argument_list|,
name|selectFrom
argument_list|,
name|selectTo
argument_list|,
name|maxDeep
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|toString
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|getAdviceWithTasks
argument_list|()
operator|.
name|add
argument_list|(
name|AdviceWithTasks
operator|.
name|removeByToString
argument_list|(
name|route
argument_list|,
name|toString
argument_list|,
name|selectLast
argument_list|,
name|selectFirst
argument_list|,
name|selectFrom
argument_list|,
name|selectTo
argument_list|,
name|maxDeep
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|getAdviceWithTasks
argument_list|()
operator|.
name|add
argument_list|(
name|AdviceWithTasks
operator|.
name|removeByType
argument_list|(
name|route
argument_list|,
name|type
argument_list|,
name|selectFirst
argument_list|,
name|selectFirst
argument_list|,
name|selectFrom
argument_list|,
name|selectTo
argument_list|,
name|maxDeep
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Insert the following node(s)<b>before</b> the matched node(s)      *      * @return the builder to build the nodes.      */
DECL|method|before ()
specifier|public
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|before
parameter_list|()
block|{
name|RouteDefinition
name|route
init|=
name|builder
operator|.
name|getOriginalRoute
argument_list|()
decl_stmt|;
name|PipelineDefinition
name|answer
init|=
operator|new
name|PipelineDefinition
argument_list|()
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|getAdviceWithTasks
argument_list|()
operator|.
name|add
argument_list|(
name|AdviceWithTasks
operator|.
name|beforeById
argument_list|(
name|route
argument_list|,
name|id
argument_list|,
name|answer
argument_list|,
name|selectFirst
argument_list|,
name|selectLast
argument_list|,
name|selectFrom
argument_list|,
name|selectTo
argument_list|,
name|maxDeep
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|toString
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|getAdviceWithTasks
argument_list|()
operator|.
name|add
argument_list|(
name|AdviceWithTasks
operator|.
name|beforeByToString
argument_list|(
name|route
argument_list|,
name|toString
argument_list|,
name|answer
argument_list|,
name|selectLast
argument_list|,
name|selectFirst
argument_list|,
name|selectFrom
argument_list|,
name|selectTo
argument_list|,
name|maxDeep
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|getAdviceWithTasks
argument_list|()
operator|.
name|add
argument_list|(
name|AdviceWithTasks
operator|.
name|beforeByType
argument_list|(
name|route
argument_list|,
name|type
argument_list|,
name|answer
argument_list|,
name|selectFirst
argument_list|,
name|selectLast
argument_list|,
name|selectFrom
argument_list|,
name|selectTo
argument_list|,
name|maxDeep
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Insert the following node(s)<b>after</b> the matched node(s)      *      * @return the builder to build the nodes.      */
DECL|method|after ()
specifier|public
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|after
parameter_list|()
block|{
name|RouteDefinition
name|route
init|=
name|builder
operator|.
name|getOriginalRoute
argument_list|()
decl_stmt|;
name|PipelineDefinition
name|answer
init|=
operator|new
name|PipelineDefinition
argument_list|()
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|getAdviceWithTasks
argument_list|()
operator|.
name|add
argument_list|(
name|AdviceWithTasks
operator|.
name|afterById
argument_list|(
name|route
argument_list|,
name|id
argument_list|,
name|answer
argument_list|,
name|selectFirst
argument_list|,
name|selectLast
argument_list|,
name|selectFrom
argument_list|,
name|selectTo
argument_list|,
name|maxDeep
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|toString
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|getAdviceWithTasks
argument_list|()
operator|.
name|add
argument_list|(
name|AdviceWithTasks
operator|.
name|afterByToString
argument_list|(
name|route
argument_list|,
name|toString
argument_list|,
name|answer
argument_list|,
name|selectLast
argument_list|,
name|selectFirst
argument_list|,
name|selectFrom
argument_list|,
name|selectTo
argument_list|,
name|maxDeep
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|getAdviceWithTasks
argument_list|()
operator|.
name|add
argument_list|(
name|AdviceWithTasks
operator|.
name|afterByType
argument_list|(
name|route
argument_list|,
name|type
argument_list|,
name|answer
argument_list|,
name|selectFirst
argument_list|,
name|selectLast
argument_list|,
name|selectFrom
argument_list|,
name|selectTo
argument_list|,
name|maxDeep
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

