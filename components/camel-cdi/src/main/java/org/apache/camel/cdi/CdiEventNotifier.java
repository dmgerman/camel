begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

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
name|EventObject
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
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|BeanManager
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
name|management
operator|.
name|event
operator|.
name|RouteAddedEvent
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
name|management
operator|.
name|event
operator|.
name|RouteRemovedEvent
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
name|management
operator|.
name|event
operator|.
name|RouteStartedEvent
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
name|management
operator|.
name|event
operator|.
name|RouteStoppedEvent
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
name|EventNotifierSupport
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|isNotEmpty
import|;
end_import

begin_class
DECL|class|CdiEventNotifier
specifier|final
class|class
name|CdiEventNotifier
extends|extends
name|EventNotifierSupport
block|{
DECL|field|manager
specifier|private
specifier|final
name|BeanManager
name|manager
decl_stmt|;
DECL|field|qualifiers
specifier|private
specifier|final
name|Annotation
index|[]
name|qualifiers
decl_stmt|;
DECL|method|CdiEventNotifier (BeanManager manager, Collection<Annotation> qualifiers)
name|CdiEventNotifier
parameter_list|(
name|BeanManager
name|manager
parameter_list|,
name|Collection
argument_list|<
name|Annotation
argument_list|>
name|qualifiers
parameter_list|)
block|{
name|this
operator|.
name|manager
operator|=
name|manager
expr_stmt|;
name|this
operator|.
name|qualifiers
operator|=
name|qualifiers
operator|.
name|toArray
argument_list|(
operator|new
name|Annotation
index|[
name|qualifiers
operator|.
name|size
argument_list|()
index|]
argument_list|)
expr_stmt|;
comment|// TODO: be more fine grained for the kind of events that are emitted depending on the observed event types
block|}
annotation|@
name|Override
DECL|method|notify (EventObject event)
specifier|public
name|void
name|notify
parameter_list|(
name|EventObject
name|event
parameter_list|)
block|{
name|String
name|id
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|event
operator|instanceof
name|RouteAddedEvent
condition|)
block|{
name|id
operator|=
operator|(
operator|(
name|RouteAddedEvent
operator|)
name|event
operator|)
operator|.
name|getRoute
argument_list|()
operator|.
name|getId
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|event
operator|instanceof
name|RouteStartedEvent
condition|)
block|{
name|id
operator|=
operator|(
operator|(
name|RouteStartedEvent
operator|)
name|event
operator|)
operator|.
name|getRoute
argument_list|()
operator|.
name|getId
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|event
operator|instanceof
name|RouteStoppedEvent
condition|)
block|{
name|id
operator|=
operator|(
operator|(
name|RouteStoppedEvent
operator|)
name|event
operator|)
operator|.
name|getRoute
argument_list|()
operator|.
name|getId
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|event
operator|instanceof
name|RouteRemovedEvent
condition|)
block|{
name|id
operator|=
operator|(
operator|(
name|RouteRemovedEvent
operator|)
name|event
operator|)
operator|.
name|getRoute
argument_list|()
operator|.
name|getId
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|isNotEmpty
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|Annotation
argument_list|>
name|annotations
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Collections
operator|.
name|addAll
argument_list|(
name|annotations
argument_list|,
name|qualifiers
argument_list|)
expr_stmt|;
name|annotations
operator|.
name|add
argument_list|(
name|NamedLiteral
operator|.
name|of
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|manager
operator|.
name|fireEvent
argument_list|(
name|event
argument_list|,
name|annotations
operator|.
name|stream
argument_list|()
operator|.
name|toArray
argument_list|(
name|Annotation
index|[]
operator|::
operator|new
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|manager
operator|.
name|fireEvent
argument_list|(
name|event
argument_list|,
name|qualifiers
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|isEnabled (EventObject event)
specifier|public
name|boolean
name|isEnabled
parameter_list|(
name|EventObject
name|event
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

