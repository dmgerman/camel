begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty4
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayDeque
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
name|Deque
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|logging
operator|.
name|log4j
operator|.
name|core
operator|.
name|Filter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|logging
operator|.
name|log4j
operator|.
name|core
operator|.
name|Layout
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|logging
operator|.
name|log4j
operator|.
name|core
operator|.
name|LogEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|logging
operator|.
name|log4j
operator|.
name|core
operator|.
name|appender
operator|.
name|AbstractAppender
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|logging
operator|.
name|log4j
operator|.
name|core
operator|.
name|config
operator|.
name|plugins
operator|.
name|Plugin
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|logging
operator|.
name|log4j
operator|.
name|core
operator|.
name|config
operator|.
name|plugins
operator|.
name|PluginAttribute
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|logging
operator|.
name|log4j
operator|.
name|core
operator|.
name|config
operator|.
name|plugins
operator|.
name|PluginElement
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|logging
operator|.
name|log4j
operator|.
name|core
operator|.
name|config
operator|.
name|plugins
operator|.
name|PluginFactory
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
annotation|@
name|Plugin
argument_list|(
name|name
operator|=
literal|"LogCaptureAppender"
argument_list|,
name|category
operator|=
literal|"Core"
argument_list|,
name|elementType
operator|=
literal|"appender"
argument_list|,
name|printObject
operator|=
literal|true
argument_list|)
DECL|class|LogCaptureAppender
specifier|public
class|class
name|LogCaptureAppender
extends|extends
name|AbstractAppender
block|{
DECL|field|LOG_EVENTS
specifier|private
specifier|static
specifier|final
name|Deque
argument_list|<
name|LogEvent
argument_list|>
name|LOG_EVENTS
init|=
operator|new
name|ArrayDeque
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|LogCaptureAppender (String name, Filter filter, Layout<? extends Serializable> layout)
specifier|public
name|LogCaptureAppender
parameter_list|(
name|String
name|name
parameter_list|,
name|Filter
name|filter
parameter_list|,
name|Layout
argument_list|<
name|?
extends|extends
name|Serializable
argument_list|>
name|layout
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|,
name|filter
argument_list|,
name|layout
argument_list|)
expr_stmt|;
block|}
DECL|method|LogCaptureAppender (String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions)
specifier|public
name|LogCaptureAppender
parameter_list|(
name|String
name|name
parameter_list|,
name|Filter
name|filter
parameter_list|,
name|Layout
argument_list|<
name|?
extends|extends
name|Serializable
argument_list|>
name|layout
parameter_list|,
name|boolean
name|ignoreExceptions
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|,
name|filter
argument_list|,
name|layout
argument_list|,
name|ignoreExceptions
argument_list|)
expr_stmt|;
block|}
annotation|@
name|PluginFactory
DECL|method|createAppender (@luginAttributeR) final String name, @PluginElement(R) final Filter filter)
specifier|public
specifier|static
name|LogCaptureAppender
name|createAppender
parameter_list|(
annotation|@
name|PluginAttribute
argument_list|(
literal|"name"
argument_list|)
specifier|final
name|String
name|name
parameter_list|,
annotation|@
name|PluginElement
argument_list|(
literal|"Filter"
argument_list|)
specifier|final
name|Filter
name|filter
parameter_list|)
block|{
return|return
operator|new
name|LogCaptureAppender
argument_list|(
name|name
argument_list|,
name|filter
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|append (LogEvent logEvent)
specifier|public
name|void
name|append
parameter_list|(
name|LogEvent
name|logEvent
parameter_list|)
block|{
name|LOG_EVENTS
operator|.
name|add
argument_list|(
name|logEvent
argument_list|)
expr_stmt|;
block|}
DECL|method|reset ()
specifier|public
specifier|static
name|void
name|reset
parameter_list|()
block|{
name|LOG_EVENTS
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|getEvents ()
specifier|public
specifier|static
name|Collection
argument_list|<
name|LogEvent
argument_list|>
name|getEvents
parameter_list|()
block|{
return|return
name|LOG_EVENTS
return|;
block|}
block|}
end_class

end_unit

