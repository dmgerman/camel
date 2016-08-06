begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.log
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|log
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
name|Consumer
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
name|Level
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
name|LogManager
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
name|Appender
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
name|LoggerContext
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
name|AppenderRef
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
name|Configuration
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
name|LoggerConfig
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
name|layout
operator|.
name|PatternLayout
import|;
end_import

begin_class
DECL|class|ConsumingAppender
specifier|public
class|class
name|ConsumingAppender
extends|extends
name|AbstractAppender
block|{
DECL|field|consumer
specifier|private
specifier|final
name|Consumer
argument_list|<
name|LogEvent
argument_list|>
name|consumer
decl_stmt|;
DECL|method|ConsumingAppender (String name, Consumer<LogEvent> consumer)
specifier|public
name|ConsumingAppender
parameter_list|(
name|String
name|name
parameter_list|,
name|Consumer
argument_list|<
name|LogEvent
argument_list|>
name|consumer
parameter_list|)
block|{
name|this
argument_list|(
name|name
argument_list|,
name|PatternLayout
operator|.
name|SIMPLE_CONVERSION_PATTERN
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
block|}
DECL|method|ConsumingAppender (String name, String pattern, Consumer<LogEvent> consumer)
specifier|public
name|ConsumingAppender
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|pattern
parameter_list|,
name|Consumer
argument_list|<
name|LogEvent
argument_list|>
name|consumer
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|,
literal|null
argument_list|,
name|PatternLayout
operator|.
name|newBuilder
argument_list|()
operator|.
name|withPattern
argument_list|(
name|pattern
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|consumer
operator|=
name|consumer
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|append (LogEvent event)
specifier|public
name|void
name|append
parameter_list|(
name|LogEvent
name|event
parameter_list|)
block|{
name|this
operator|.
name|consumer
operator|.
name|accept
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
comment|// *******************
comment|// Helpers
comment|// *******************
DECL|method|newAppender (String loggerName, String appenderName, Level level, Consumer<LogEvent> consumer)
specifier|public
specifier|static
name|Appender
name|newAppender
parameter_list|(
name|String
name|loggerName
parameter_list|,
name|String
name|appenderName
parameter_list|,
name|Level
name|level
parameter_list|,
name|Consumer
argument_list|<
name|LogEvent
argument_list|>
name|consumer
parameter_list|)
block|{
return|return
name|newAppender
argument_list|(
name|loggerName
argument_list|,
name|appenderName
argument_list|,
name|PatternLayout
operator|.
name|SIMPLE_CONVERSION_PATTERN
argument_list|,
name|level
argument_list|,
name|consumer
argument_list|)
return|;
block|}
DECL|method|newAppender (String loggerName, String appenderName, String patter, Level level, Consumer<LogEvent> consumer)
specifier|public
specifier|static
name|Appender
name|newAppender
parameter_list|(
name|String
name|loggerName
parameter_list|,
name|String
name|appenderName
parameter_list|,
name|String
name|patter
parameter_list|,
name|Level
name|level
parameter_list|,
name|Consumer
argument_list|<
name|LogEvent
argument_list|>
name|consumer
parameter_list|)
block|{
specifier|final
name|LoggerContext
name|ctx
init|=
operator|(
name|LoggerContext
operator|)
name|LogManager
operator|.
name|getContext
argument_list|(
literal|false
argument_list|)
decl_stmt|;
specifier|final
name|Configuration
name|config
init|=
name|ctx
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|config
operator|.
name|removeLogger
argument_list|(
name|loggerName
argument_list|)
expr_stmt|;
name|ConsumingAppender
name|appender
init|=
operator|new
name|ConsumingAppender
argument_list|(
name|appenderName
argument_list|,
name|patter
argument_list|,
name|consumer
argument_list|)
decl_stmt|;
name|appender
operator|.
name|start
argument_list|()
expr_stmt|;
name|LoggerConfig
name|loggerConfig
init|=
name|LoggerConfig
operator|.
name|createLogger
argument_list|(
literal|true
argument_list|,
name|level
argument_list|,
name|loggerName
argument_list|,
literal|"true"
argument_list|,
operator|new
name|AppenderRef
index|[]
block|{
name|AppenderRef
operator|.
name|createAppenderRef
argument_list|(
name|appenderName
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
block|}
argument_list|,
literal|null
argument_list|,
name|config
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|loggerConfig
operator|.
name|addAppender
argument_list|(
name|appender
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|config
operator|.
name|addLogger
argument_list|(
name|loggerName
argument_list|,
name|loggerConfig
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|updateLoggers
argument_list|()
expr_stmt|;
return|return
name|appender
return|;
block|}
block|}
end_class

end_unit

