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
name|Iterator
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
name|model
operator|.
name|FromDefinition
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
name|ProcessorDefinitionHelper
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|EndpointHelper
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
comment|/**  * {@link AdviceWithTask} tasks which are used by the {@link AdviceWithRouteBuilder}.  */
end_comment

begin_class
DECL|class|AdviceWithTasks
specifier|public
specifier|final
class|class
name|AdviceWithTasks
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AdviceWithTasks
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|AdviceWithTasks ()
specifier|private
name|AdviceWithTasks
parameter_list|()
block|{
comment|// utility class
block|}
comment|/**      * Match by is used for pluggable match by logic.      */
DECL|interface|MatchBy
specifier|private
interface|interface
name|MatchBy
block|{
DECL|method|getId ()
name|String
name|getId
parameter_list|()
function_decl|;
DECL|method|match (ProcessorDefinition processor)
name|boolean
name|match
parameter_list|(
name|ProcessorDefinition
name|processor
parameter_list|)
function_decl|;
block|}
comment|/**      * Will match by id of the processor.      */
DECL|class|MatchById
specifier|private
specifier|static
specifier|final
class|class
name|MatchById
implements|implements
name|MatchBy
block|{
DECL|field|id
specifier|private
specifier|final
name|String
name|id
decl_stmt|;
DECL|method|MatchById (String id)
specifier|private
name|MatchById
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|match (ProcessorDefinition processor)
specifier|public
name|boolean
name|match
parameter_list|(
name|ProcessorDefinition
name|processor
parameter_list|)
block|{
return|return
name|EndpointHelper
operator|.
name|matchPattern
argument_list|(
name|processor
operator|.
name|getId
argument_list|()
argument_list|,
name|id
argument_list|)
return|;
block|}
block|}
comment|/**      * Will match by the to string representation of the processor.      */
DECL|class|MatchByToString
specifier|private
specifier|static
specifier|final
class|class
name|MatchByToString
implements|implements
name|MatchBy
block|{
DECL|field|toString
specifier|private
specifier|final
name|String
name|toString
decl_stmt|;
DECL|method|MatchByToString (String toString)
specifier|private
name|MatchByToString
parameter_list|(
name|String
name|toString
parameter_list|)
block|{
name|this
operator|.
name|toString
operator|=
name|toString
expr_stmt|;
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|toString
return|;
block|}
DECL|method|match (ProcessorDefinition processor)
specifier|public
name|boolean
name|match
parameter_list|(
name|ProcessorDefinition
name|processor
parameter_list|)
block|{
return|return
name|EndpointHelper
operator|.
name|matchPattern
argument_list|(
name|processor
operator|.
name|toString
argument_list|()
argument_list|,
name|toString
argument_list|)
return|;
block|}
block|}
comment|/**      * Will match by the type of the processor.      */
DECL|class|MatchByType
specifier|private
specifier|static
specifier|final
class|class
name|MatchByType
implements|implements
name|MatchBy
block|{
DECL|field|type
specifier|private
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|type
decl_stmt|;
DECL|method|MatchByType (Class<?> type)
specifier|private
name|MatchByType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|type
operator|.
name|getSimpleName
argument_list|()
return|;
block|}
DECL|method|match (ProcessorDefinition processor)
specifier|public
name|boolean
name|match
parameter_list|(
name|ProcessorDefinition
name|processor
parameter_list|)
block|{
return|return
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|processor
operator|.
name|getClass
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|method|replaceByToString (final RouteDefinition route, final String toString, final ProcessorDefinition replace, boolean selectFirst, boolean selectLast, int selectFrom, int selectTo)
specifier|public
specifier|static
name|AdviceWithTask
name|replaceByToString
parameter_list|(
specifier|final
name|RouteDefinition
name|route
parameter_list|,
specifier|final
name|String
name|toString
parameter_list|,
specifier|final
name|ProcessorDefinition
name|replace
parameter_list|,
name|boolean
name|selectFirst
parameter_list|,
name|boolean
name|selectLast
parameter_list|,
name|int
name|selectFrom
parameter_list|,
name|int
name|selectTo
parameter_list|)
block|{
name|MatchBy
name|matchBy
init|=
operator|new
name|MatchByToString
argument_list|(
name|toString
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|ProcessorDefinition
argument_list|>
name|it
init|=
name|AdviceWithTasks
operator|.
name|createMatchByIterator
argument_list|(
name|route
argument_list|,
name|matchBy
argument_list|,
name|selectFirst
argument_list|,
name|selectLast
argument_list|,
name|selectFrom
argument_list|,
name|selectTo
argument_list|)
decl_stmt|;
return|return
name|doReplace
argument_list|(
name|route
argument_list|,
operator|new
name|MatchByToString
argument_list|(
name|toString
argument_list|)
argument_list|,
name|replace
argument_list|,
name|it
argument_list|)
return|;
block|}
DECL|method|replaceById (final RouteDefinition route, final String id, final ProcessorDefinition replace, boolean selectFirst, boolean selectLast, int selectFrom, int selectTo)
specifier|public
specifier|static
name|AdviceWithTask
name|replaceById
parameter_list|(
specifier|final
name|RouteDefinition
name|route
parameter_list|,
specifier|final
name|String
name|id
parameter_list|,
specifier|final
name|ProcessorDefinition
name|replace
parameter_list|,
name|boolean
name|selectFirst
parameter_list|,
name|boolean
name|selectLast
parameter_list|,
name|int
name|selectFrom
parameter_list|,
name|int
name|selectTo
parameter_list|)
block|{
name|MatchBy
name|matchBy
init|=
operator|new
name|MatchById
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|ProcessorDefinition
argument_list|>
name|it
init|=
name|AdviceWithTasks
operator|.
name|createMatchByIterator
argument_list|(
name|route
argument_list|,
name|matchBy
argument_list|,
name|selectFirst
argument_list|,
name|selectLast
argument_list|,
name|selectFrom
argument_list|,
name|selectTo
argument_list|)
decl_stmt|;
return|return
name|doReplace
argument_list|(
name|route
argument_list|,
name|matchBy
argument_list|,
name|replace
argument_list|,
name|it
argument_list|)
return|;
block|}
DECL|method|replaceByType (final RouteDefinition route, final Class type, final ProcessorDefinition replace, boolean selectFirst, boolean selectLast, int selectFrom, int selectTo)
specifier|public
specifier|static
name|AdviceWithTask
name|replaceByType
parameter_list|(
specifier|final
name|RouteDefinition
name|route
parameter_list|,
specifier|final
name|Class
name|type
parameter_list|,
specifier|final
name|ProcessorDefinition
name|replace
parameter_list|,
name|boolean
name|selectFirst
parameter_list|,
name|boolean
name|selectLast
parameter_list|,
name|int
name|selectFrom
parameter_list|,
name|int
name|selectTo
parameter_list|)
block|{
name|MatchBy
name|matchBy
init|=
operator|new
name|MatchByType
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|ProcessorDefinition
argument_list|>
name|it
init|=
name|AdviceWithTasks
operator|.
name|createMatchByIterator
argument_list|(
name|route
argument_list|,
name|matchBy
argument_list|,
name|selectFirst
argument_list|,
name|selectLast
argument_list|,
name|selectFrom
argument_list|,
name|selectTo
argument_list|)
decl_stmt|;
return|return
name|doReplace
argument_list|(
name|route
argument_list|,
name|matchBy
argument_list|,
name|replace
argument_list|,
name|it
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|doReplace (final RouteDefinition route, final MatchBy matchBy, final ProcessorDefinition replace, final Iterator<ProcessorDefinition> it)
specifier|private
specifier|static
name|AdviceWithTask
name|doReplace
parameter_list|(
specifier|final
name|RouteDefinition
name|route
parameter_list|,
specifier|final
name|MatchBy
name|matchBy
parameter_list|,
specifier|final
name|ProcessorDefinition
name|replace
parameter_list|,
specifier|final
name|Iterator
argument_list|<
name|ProcessorDefinition
argument_list|>
name|it
parameter_list|)
block|{
return|return
operator|new
name|AdviceWithTask
argument_list|()
block|{
specifier|public
name|void
name|task
parameter_list|()
throws|throws
name|Exception
block|{
name|boolean
name|match
init|=
literal|false
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ProcessorDefinition
name|output
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|matchBy
operator|.
name|match
argument_list|(
name|output
argument_list|)
condition|)
block|{
name|ProcessorDefinition
name|parent
init|=
name|output
operator|.
name|getParent
argument_list|()
decl_stmt|;
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
name|int
name|index
init|=
name|parent
operator|.
name|getOutputs
argument_list|()
operator|.
name|indexOf
argument_list|(
name|output
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|!=
operator|-
literal|1
condition|)
block|{
name|match
operator|=
literal|true
expr_stmt|;
name|parent
operator|.
name|getOutputs
argument_list|()
operator|.
name|add
argument_list|(
name|index
operator|+
literal|1
argument_list|,
name|replace
argument_list|)
expr_stmt|;
name|Object
name|old
init|=
name|parent
operator|.
name|getOutputs
argument_list|()
operator|.
name|remove
argument_list|(
name|index
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"AdviceWith ("
operator|+
name|matchBy
operator|.
name|getId
argument_list|()
operator|+
literal|") : ["
operator|+
name|old
operator|+
literal|"] --> replace ["
operator|+
name|replace
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
operator|!
name|match
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"There are no outputs which matches: "
operator|+
name|matchBy
operator|.
name|getId
argument_list|()
operator|+
literal|" in the route: "
operator|+
name|route
argument_list|)
throw|;
block|}
block|}
block|}
return|;
block|}
DECL|method|removeByToString (final RouteDefinition route, final String toString, boolean selectFirst, boolean selectLast, int selectFrom, int selectTo)
specifier|public
specifier|static
name|AdviceWithTask
name|removeByToString
parameter_list|(
specifier|final
name|RouteDefinition
name|route
parameter_list|,
specifier|final
name|String
name|toString
parameter_list|,
name|boolean
name|selectFirst
parameter_list|,
name|boolean
name|selectLast
parameter_list|,
name|int
name|selectFrom
parameter_list|,
name|int
name|selectTo
parameter_list|)
block|{
name|MatchBy
name|matchBy
init|=
operator|new
name|MatchByToString
argument_list|(
name|toString
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|ProcessorDefinition
argument_list|>
name|it
init|=
name|AdviceWithTasks
operator|.
name|createMatchByIterator
argument_list|(
name|route
argument_list|,
name|matchBy
argument_list|,
name|selectFirst
argument_list|,
name|selectLast
argument_list|,
name|selectFrom
argument_list|,
name|selectTo
argument_list|)
decl_stmt|;
return|return
name|doRemove
argument_list|(
name|route
argument_list|,
name|matchBy
argument_list|,
name|it
argument_list|)
return|;
block|}
DECL|method|removeById (final RouteDefinition route, final String id, boolean selectFirst, boolean selectLast, int selectFrom, int selectTo)
specifier|public
specifier|static
name|AdviceWithTask
name|removeById
parameter_list|(
specifier|final
name|RouteDefinition
name|route
parameter_list|,
specifier|final
name|String
name|id
parameter_list|,
name|boolean
name|selectFirst
parameter_list|,
name|boolean
name|selectLast
parameter_list|,
name|int
name|selectFrom
parameter_list|,
name|int
name|selectTo
parameter_list|)
block|{
name|MatchBy
name|matchBy
init|=
operator|new
name|MatchById
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|ProcessorDefinition
argument_list|>
name|it
init|=
name|AdviceWithTasks
operator|.
name|createMatchByIterator
argument_list|(
name|route
argument_list|,
name|matchBy
argument_list|,
name|selectFirst
argument_list|,
name|selectLast
argument_list|,
name|selectFrom
argument_list|,
name|selectTo
argument_list|)
decl_stmt|;
return|return
name|doRemove
argument_list|(
name|route
argument_list|,
name|matchBy
argument_list|,
name|it
argument_list|)
return|;
block|}
DECL|method|removeByType (final RouteDefinition route, final Class type, boolean selectFirst, boolean selectLast, int selectFrom, int selectTo)
specifier|public
specifier|static
name|AdviceWithTask
name|removeByType
parameter_list|(
specifier|final
name|RouteDefinition
name|route
parameter_list|,
specifier|final
name|Class
name|type
parameter_list|,
name|boolean
name|selectFirst
parameter_list|,
name|boolean
name|selectLast
parameter_list|,
name|int
name|selectFrom
parameter_list|,
name|int
name|selectTo
parameter_list|)
block|{
name|MatchBy
name|matchBy
init|=
operator|new
name|MatchByType
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|ProcessorDefinition
argument_list|>
name|it
init|=
name|AdviceWithTasks
operator|.
name|createMatchByIterator
argument_list|(
name|route
argument_list|,
name|matchBy
argument_list|,
name|selectFirst
argument_list|,
name|selectLast
argument_list|,
name|selectFrom
argument_list|,
name|selectTo
argument_list|)
decl_stmt|;
return|return
name|doRemove
argument_list|(
name|route
argument_list|,
name|matchBy
argument_list|,
name|it
argument_list|)
return|;
block|}
DECL|method|doRemove (final RouteDefinition route, final MatchBy matchBy, final Iterator<ProcessorDefinition> it)
specifier|private
specifier|static
name|AdviceWithTask
name|doRemove
parameter_list|(
specifier|final
name|RouteDefinition
name|route
parameter_list|,
specifier|final
name|MatchBy
name|matchBy
parameter_list|,
specifier|final
name|Iterator
argument_list|<
name|ProcessorDefinition
argument_list|>
name|it
parameter_list|)
block|{
return|return
operator|new
name|AdviceWithTask
argument_list|()
block|{
specifier|public
name|void
name|task
parameter_list|()
throws|throws
name|Exception
block|{
name|boolean
name|match
init|=
literal|false
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ProcessorDefinition
name|output
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|matchBy
operator|.
name|match
argument_list|(
name|output
argument_list|)
condition|)
block|{
name|ProcessorDefinition
name|parent
init|=
name|output
operator|.
name|getParent
argument_list|()
decl_stmt|;
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
name|int
name|index
init|=
name|parent
operator|.
name|getOutputs
argument_list|()
operator|.
name|indexOf
argument_list|(
name|output
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|!=
operator|-
literal|1
condition|)
block|{
name|match
operator|=
literal|true
expr_stmt|;
name|Object
name|old
init|=
name|parent
operator|.
name|getOutputs
argument_list|()
operator|.
name|remove
argument_list|(
name|index
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"AdviceWith ("
operator|+
name|matchBy
operator|.
name|getId
argument_list|()
operator|+
literal|") : ["
operator|+
name|old
operator|+
literal|"] --> remove"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
operator|!
name|match
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"There are no outputs which matches: "
operator|+
name|matchBy
operator|.
name|getId
argument_list|()
operator|+
literal|" in the route: "
operator|+
name|route
argument_list|)
throw|;
block|}
block|}
block|}
return|;
block|}
DECL|method|beforeByToString (final RouteDefinition route, final String toString, final ProcessorDefinition before, boolean selectFirst, boolean selectLast, int selectFrom, int selectTo)
specifier|public
specifier|static
name|AdviceWithTask
name|beforeByToString
parameter_list|(
specifier|final
name|RouteDefinition
name|route
parameter_list|,
specifier|final
name|String
name|toString
parameter_list|,
specifier|final
name|ProcessorDefinition
name|before
parameter_list|,
name|boolean
name|selectFirst
parameter_list|,
name|boolean
name|selectLast
parameter_list|,
name|int
name|selectFrom
parameter_list|,
name|int
name|selectTo
parameter_list|)
block|{
name|MatchBy
name|matchBy
init|=
operator|new
name|MatchByToString
argument_list|(
name|toString
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|ProcessorDefinition
argument_list|>
name|it
init|=
name|AdviceWithTasks
operator|.
name|createMatchByIterator
argument_list|(
name|route
argument_list|,
name|matchBy
argument_list|,
name|selectFirst
argument_list|,
name|selectLast
argument_list|,
name|selectFrom
argument_list|,
name|selectTo
argument_list|)
decl_stmt|;
return|return
name|doBefore
argument_list|(
name|route
argument_list|,
name|matchBy
argument_list|,
name|before
argument_list|,
name|it
argument_list|)
return|;
block|}
DECL|method|beforeById (final RouteDefinition route, final String id, final ProcessorDefinition before, boolean selectFirst, boolean selectLast, int selectFrom, int selectTo)
specifier|public
specifier|static
name|AdviceWithTask
name|beforeById
parameter_list|(
specifier|final
name|RouteDefinition
name|route
parameter_list|,
specifier|final
name|String
name|id
parameter_list|,
specifier|final
name|ProcessorDefinition
name|before
parameter_list|,
name|boolean
name|selectFirst
parameter_list|,
name|boolean
name|selectLast
parameter_list|,
name|int
name|selectFrom
parameter_list|,
name|int
name|selectTo
parameter_list|)
block|{
name|MatchBy
name|matchBy
init|=
operator|new
name|MatchById
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|ProcessorDefinition
argument_list|>
name|it
init|=
name|AdviceWithTasks
operator|.
name|createMatchByIterator
argument_list|(
name|route
argument_list|,
name|matchBy
argument_list|,
name|selectFirst
argument_list|,
name|selectLast
argument_list|,
name|selectFrom
argument_list|,
name|selectTo
argument_list|)
decl_stmt|;
return|return
name|doBefore
argument_list|(
name|route
argument_list|,
name|matchBy
argument_list|,
name|before
argument_list|,
name|it
argument_list|)
return|;
block|}
DECL|method|beforeByType (final RouteDefinition route, final Class type, final ProcessorDefinition before, boolean selectFirst, boolean selectLast, int selectFrom, int selectTo)
specifier|public
specifier|static
name|AdviceWithTask
name|beforeByType
parameter_list|(
specifier|final
name|RouteDefinition
name|route
parameter_list|,
specifier|final
name|Class
name|type
parameter_list|,
specifier|final
name|ProcessorDefinition
name|before
parameter_list|,
name|boolean
name|selectFirst
parameter_list|,
name|boolean
name|selectLast
parameter_list|,
name|int
name|selectFrom
parameter_list|,
name|int
name|selectTo
parameter_list|)
block|{
name|MatchBy
name|matchBy
init|=
operator|new
name|MatchByType
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|ProcessorDefinition
argument_list|>
name|it
init|=
name|AdviceWithTasks
operator|.
name|createMatchByIterator
argument_list|(
name|route
argument_list|,
name|matchBy
argument_list|,
name|selectFirst
argument_list|,
name|selectLast
argument_list|,
name|selectFrom
argument_list|,
name|selectTo
argument_list|)
decl_stmt|;
return|return
name|doBefore
argument_list|(
name|route
argument_list|,
name|matchBy
argument_list|,
name|before
argument_list|,
name|it
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|doBefore (final RouteDefinition route, final MatchBy matchBy, final ProcessorDefinition before, final Iterator<ProcessorDefinition> it)
specifier|private
specifier|static
name|AdviceWithTask
name|doBefore
parameter_list|(
specifier|final
name|RouteDefinition
name|route
parameter_list|,
specifier|final
name|MatchBy
name|matchBy
parameter_list|,
specifier|final
name|ProcessorDefinition
name|before
parameter_list|,
specifier|final
name|Iterator
argument_list|<
name|ProcessorDefinition
argument_list|>
name|it
parameter_list|)
block|{
return|return
operator|new
name|AdviceWithTask
argument_list|()
block|{
specifier|public
name|void
name|task
parameter_list|()
throws|throws
name|Exception
block|{
name|boolean
name|match
init|=
literal|false
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ProcessorDefinition
name|output
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|matchBy
operator|.
name|match
argument_list|(
name|output
argument_list|)
condition|)
block|{
name|ProcessorDefinition
name|parent
init|=
name|output
operator|.
name|getParent
argument_list|()
decl_stmt|;
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
name|int
name|index
init|=
name|parent
operator|.
name|getOutputs
argument_list|()
operator|.
name|indexOf
argument_list|(
name|output
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|!=
operator|-
literal|1
condition|)
block|{
name|match
operator|=
literal|true
expr_stmt|;
name|Object
name|existing
init|=
name|parent
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
name|parent
operator|.
name|getOutputs
argument_list|()
operator|.
name|add
argument_list|(
name|index
argument_list|,
name|before
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"AdviceWith ("
operator|+
name|matchBy
operator|.
name|getId
argument_list|()
operator|+
literal|") : ["
operator|+
name|existing
operator|+
literal|"] --> before ["
operator|+
name|before
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
operator|!
name|match
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"There are no outputs which matches: "
operator|+
name|matchBy
operator|.
name|getId
argument_list|()
operator|+
literal|" in the route: "
operator|+
name|route
argument_list|)
throw|;
block|}
block|}
block|}
return|;
block|}
DECL|method|afterByToString (final RouteDefinition route, final String toString, final ProcessorDefinition after, boolean selectFirst, boolean selectLast, int selectFrom, int selectTo)
specifier|public
specifier|static
name|AdviceWithTask
name|afterByToString
parameter_list|(
specifier|final
name|RouteDefinition
name|route
parameter_list|,
specifier|final
name|String
name|toString
parameter_list|,
specifier|final
name|ProcessorDefinition
name|after
parameter_list|,
name|boolean
name|selectFirst
parameter_list|,
name|boolean
name|selectLast
parameter_list|,
name|int
name|selectFrom
parameter_list|,
name|int
name|selectTo
parameter_list|)
block|{
name|MatchBy
name|matchBy
init|=
operator|new
name|MatchByToString
argument_list|(
name|toString
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|ProcessorDefinition
argument_list|>
name|it
init|=
name|AdviceWithTasks
operator|.
name|createMatchByIterator
argument_list|(
name|route
argument_list|,
name|matchBy
argument_list|,
name|selectFirst
argument_list|,
name|selectLast
argument_list|,
name|selectFrom
argument_list|,
name|selectTo
argument_list|)
decl_stmt|;
return|return
name|doAfter
argument_list|(
name|route
argument_list|,
name|matchBy
argument_list|,
name|after
argument_list|,
name|it
argument_list|)
return|;
block|}
DECL|method|afterById (final RouteDefinition route, final String id, final ProcessorDefinition after, boolean selectFirst, boolean selectLast, int selectFrom, int selectTo)
specifier|public
specifier|static
name|AdviceWithTask
name|afterById
parameter_list|(
specifier|final
name|RouteDefinition
name|route
parameter_list|,
specifier|final
name|String
name|id
parameter_list|,
specifier|final
name|ProcessorDefinition
name|after
parameter_list|,
name|boolean
name|selectFirst
parameter_list|,
name|boolean
name|selectLast
parameter_list|,
name|int
name|selectFrom
parameter_list|,
name|int
name|selectTo
parameter_list|)
block|{
name|MatchBy
name|matchBy
init|=
operator|new
name|MatchById
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|ProcessorDefinition
argument_list|>
name|it
init|=
name|AdviceWithTasks
operator|.
name|createMatchByIterator
argument_list|(
name|route
argument_list|,
name|matchBy
argument_list|,
name|selectFirst
argument_list|,
name|selectLast
argument_list|,
name|selectFrom
argument_list|,
name|selectTo
argument_list|)
decl_stmt|;
return|return
name|doAfter
argument_list|(
name|route
argument_list|,
name|matchBy
argument_list|,
name|after
argument_list|,
name|it
argument_list|)
return|;
block|}
DECL|method|afterByType (final RouteDefinition route, final Class type, final ProcessorDefinition after, boolean selectFirst, boolean selectLast, int selectFrom, int selectTo)
specifier|public
specifier|static
name|AdviceWithTask
name|afterByType
parameter_list|(
specifier|final
name|RouteDefinition
name|route
parameter_list|,
specifier|final
name|Class
name|type
parameter_list|,
specifier|final
name|ProcessorDefinition
name|after
parameter_list|,
name|boolean
name|selectFirst
parameter_list|,
name|boolean
name|selectLast
parameter_list|,
name|int
name|selectFrom
parameter_list|,
name|int
name|selectTo
parameter_list|)
block|{
name|MatchBy
name|matchBy
init|=
operator|new
name|MatchByType
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|ProcessorDefinition
argument_list|>
name|it
init|=
name|AdviceWithTasks
operator|.
name|createMatchByIterator
argument_list|(
name|route
argument_list|,
name|matchBy
argument_list|,
name|selectFirst
argument_list|,
name|selectLast
argument_list|,
name|selectFrom
argument_list|,
name|selectTo
argument_list|)
decl_stmt|;
return|return
name|doAfter
argument_list|(
name|route
argument_list|,
name|matchBy
argument_list|,
name|after
argument_list|,
name|it
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|doAfter (final RouteDefinition route, final MatchBy matchBy, final ProcessorDefinition after, final Iterator<ProcessorDefinition> it)
specifier|private
specifier|static
name|AdviceWithTask
name|doAfter
parameter_list|(
specifier|final
name|RouteDefinition
name|route
parameter_list|,
specifier|final
name|MatchBy
name|matchBy
parameter_list|,
specifier|final
name|ProcessorDefinition
name|after
parameter_list|,
specifier|final
name|Iterator
argument_list|<
name|ProcessorDefinition
argument_list|>
name|it
parameter_list|)
block|{
return|return
operator|new
name|AdviceWithTask
argument_list|()
block|{
specifier|public
name|void
name|task
parameter_list|()
throws|throws
name|Exception
block|{
name|boolean
name|match
init|=
literal|false
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ProcessorDefinition
name|output
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|matchBy
operator|.
name|match
argument_list|(
name|output
argument_list|)
condition|)
block|{
name|ProcessorDefinition
name|parent
init|=
name|output
operator|.
name|getParent
argument_list|()
decl_stmt|;
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
name|int
name|index
init|=
name|parent
operator|.
name|getOutputs
argument_list|()
operator|.
name|indexOf
argument_list|(
name|output
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|!=
operator|-
literal|1
condition|)
block|{
name|match
operator|=
literal|true
expr_stmt|;
name|Object
name|existing
init|=
name|parent
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
name|parent
operator|.
name|getOutputs
argument_list|()
operator|.
name|add
argument_list|(
name|index
operator|+
literal|1
argument_list|,
name|after
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"AdviceWith ("
operator|+
name|matchBy
operator|.
name|getId
argument_list|()
operator|+
literal|") : ["
operator|+
name|existing
operator|+
literal|"] --> after ["
operator|+
name|after
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
operator|!
name|match
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"There are no outputs which matches: "
operator|+
name|matchBy
operator|.
name|getId
argument_list|()
operator|+
literal|" in the route: "
operator|+
name|route
argument_list|)
throw|;
block|}
block|}
block|}
return|;
block|}
DECL|method|replaceFromWith (final RouteDefinition route, final String uri)
specifier|public
specifier|static
name|AdviceWithTask
name|replaceFromWith
parameter_list|(
specifier|final
name|RouteDefinition
name|route
parameter_list|,
specifier|final
name|String
name|uri
parameter_list|)
block|{
return|return
operator|new
name|AdviceWithTask
argument_list|()
block|{
specifier|public
name|void
name|task
parameter_list|()
throws|throws
name|Exception
block|{
name|FromDefinition
name|from
init|=
name|route
operator|.
name|getInputs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"AdviceWith replace input from [{}] --> [{}]"
argument_list|,
name|from
operator|.
name|getUriOrRef
argument_list|()
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|from
operator|.
name|setEndpoint
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|from
operator|.
name|setRef
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|from
operator|.
name|setUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|replaceFrom (final RouteDefinition route, final Endpoint endpoint)
specifier|public
specifier|static
name|AdviceWithTask
name|replaceFrom
parameter_list|(
specifier|final
name|RouteDefinition
name|route
parameter_list|,
specifier|final
name|Endpoint
name|endpoint
parameter_list|)
block|{
return|return
operator|new
name|AdviceWithTask
argument_list|()
block|{
specifier|public
name|void
name|task
parameter_list|()
throws|throws
name|Exception
block|{
name|FromDefinition
name|from
init|=
name|route
operator|.
name|getInputs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"AdviceWith replace input from [{}] --> [{}]"
argument_list|,
name|from
operator|.
name|getUriOrRef
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|from
operator|.
name|setRef
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|from
operator|.
name|setUri
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|from
operator|.
name|setEndpoint
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|/**      * Create iterator which walks the route, and only returns nodes which matches the given set of criteria.      *      * @param route        the route      * @param matchBy      match by which must match      * @param selectFirst  optional to select only the first      * @param selectLast   optional to select only the last      * @param selectFrom   optional to select index/range      * @param selectTo     optional to select index/range      *       * @return the iterator      */
DECL|method|createMatchByIterator (final RouteDefinition route, final MatchBy matchBy, final boolean selectFirst, final boolean selectLast, final int selectFrom, final int selectTo)
specifier|private
specifier|static
name|Iterator
argument_list|<
name|ProcessorDefinition
argument_list|>
name|createMatchByIterator
parameter_list|(
specifier|final
name|RouteDefinition
name|route
parameter_list|,
specifier|final
name|MatchBy
name|matchBy
parameter_list|,
specifier|final
name|boolean
name|selectFirst
parameter_list|,
specifier|final
name|boolean
name|selectLast
parameter_list|,
specifier|final
name|int
name|selectFrom
parameter_list|,
specifier|final
name|int
name|selectTo
parameter_list|)
block|{
comment|// first iterator and apply match by
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|matched
init|=
operator|new
name|ArrayList
argument_list|<
name|ProcessorDefinition
argument_list|>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|ProcessorDefinition
argument_list|>
name|itAll
init|=
name|ProcessorDefinitionHelper
operator|.
name|filterTypeInOutputs
argument_list|(
name|route
operator|.
name|getOutputs
argument_list|()
argument_list|,
name|ProcessorDefinition
operator|.
name|class
argument_list|)
decl_stmt|;
while|while
condition|(
name|itAll
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ProcessorDefinition
name|next
init|=
name|itAll
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|matchBy
operator|.
name|match
argument_list|(
name|next
argument_list|)
condition|)
block|{
name|matched
operator|.
name|add
argument_list|(
name|next
argument_list|)
expr_stmt|;
block|}
block|}
comment|// and then apply the selector iterator
return|return
name|createSelectorIterator
argument_list|(
name|matched
argument_list|,
name|selectFirst
argument_list|,
name|selectLast
argument_list|,
name|selectFrom
argument_list|,
name|selectTo
argument_list|)
return|;
block|}
DECL|method|createSelectorIterator (final List<ProcessorDefinition> list, final boolean selectFirst, final boolean selectLast, final int selectFrom, final int selectTo)
specifier|private
specifier|static
name|Iterator
argument_list|<
name|ProcessorDefinition
argument_list|>
name|createSelectorIterator
parameter_list|(
specifier|final
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|list
parameter_list|,
specifier|final
name|boolean
name|selectFirst
parameter_list|,
specifier|final
name|boolean
name|selectLast
parameter_list|,
specifier|final
name|int
name|selectFrom
parameter_list|,
specifier|final
name|int
name|selectTo
parameter_list|)
block|{
return|return
operator|new
name|Iterator
argument_list|<
name|ProcessorDefinition
argument_list|>
argument_list|()
block|{
specifier|private
name|int
name|current
decl_stmt|;
specifier|private
name|boolean
name|done
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
if|if
condition|(
name|list
operator|.
name|isEmpty
argument_list|()
operator|||
name|done
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|selectFirst
condition|)
block|{
name|done
operator|=
literal|true
expr_stmt|;
comment|// spool to first
name|current
operator|=
literal|0
expr_stmt|;
return|return
literal|true
return|;
block|}
if|if
condition|(
name|selectLast
condition|)
block|{
name|done
operator|=
literal|true
expr_stmt|;
comment|// spool to last
name|current
operator|=
name|list
operator|.
name|size
argument_list|()
operator|-
literal|1
expr_stmt|;
return|return
literal|true
return|;
block|}
if|if
condition|(
name|selectFrom
operator|>=
literal|0
operator|&&
name|selectTo
operator|>=
literal|0
condition|)
block|{
comment|// check for out of bounds
if|if
condition|(
name|selectFrom
operator|>=
name|list
operator|.
name|size
argument_list|()
operator|||
name|selectTo
operator|>=
name|list
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|current
operator|<
name|selectFrom
condition|)
block|{
comment|// spool to beginning of range
name|current
operator|=
name|selectFrom
expr_stmt|;
block|}
return|return
name|current
operator|>=
name|selectFrom
operator|&&
name|current
operator|<=
name|selectTo
return|;
block|}
return|return
name|current
operator|<
name|list
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|ProcessorDefinition
name|next
parameter_list|()
block|{
name|ProcessorDefinition
name|answer
init|=
name|list
operator|.
name|get
argument_list|(
name|current
argument_list|)
decl_stmt|;
name|current
operator|++
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|remove
parameter_list|()
block|{
comment|// noop
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

