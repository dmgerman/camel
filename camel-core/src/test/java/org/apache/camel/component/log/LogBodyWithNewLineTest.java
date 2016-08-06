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
name|io
operator|.
name|StringWriter
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
name|ContextTestSupport
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
name|builder
operator|.
name|RouteBuilder
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
name|WriterAppender
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
name|layout
operator|.
name|PatternLayout
import|;
end_import

begin_class
DECL|class|LogBodyWithNewLineTest
specifier|public
class|class
name|LogBodyWithNewLineTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|writer
specifier|private
name|StringWriter
name|writer
decl_stmt|;
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|writer
operator|=
operator|new
name|StringWriter
argument_list|()
expr_stmt|;
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
name|Appender
name|appender
init|=
name|WriterAppender
operator|.
name|newBuilder
argument_list|()
operator|.
name|setLayout
argument_list|(
name|PatternLayout
operator|.
name|newBuilder
argument_list|()
operator|.
name|withPattern
argument_list|(
name|PatternLayout
operator|.
name|SIMPLE_CONVERSION_PATTERN
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|setTarget
argument_list|(
name|writer
argument_list|)
operator|.
name|setName
argument_list|(
literal|"Writer"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|appender
operator|.
name|start
argument_list|()
expr_stmt|;
name|config
operator|.
name|addAppender
argument_list|(
name|appender
argument_list|)
expr_stmt|;
name|config
operator|.
name|getRootLogger
argument_list|()
operator|.
name|removeAppender
argument_list|(
literal|"Writer"
argument_list|)
expr_stmt|;
name|config
operator|.
name|getRootLogger
argument_list|()
operator|.
name|addAppender
argument_list|(
name|appender
argument_list|,
name|Level
operator|.
name|INFO
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|updateLoggers
argument_list|()
expr_stmt|;
block|}
DECL|method|testNoSkip ()
specifier|public
name|void
name|testNoSkip
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|ls
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"line.separator"
argument_list|)
decl_stmt|;
name|String
name|body
init|=
literal|"1"
operator|+
name|ls
operator|+
literal|"2"
operator|+
name|ls
operator|+
literal|"3"
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"{}"
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|writer
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
name|body
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSkip ()
specifier|public
name|void
name|testSkip
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|ls
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"line.separator"
argument_list|)
decl_stmt|;
name|String
name|body
init|=
literal|"1"
operator|+
name|ls
operator|+
literal|"2"
operator|+
name|ls
operator|+
literal|"3"
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:skip"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"{}"
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|writer
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
literal|"123"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:logger_name?level=INFO&showAll=true&skipBodyLineSeparator=false"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:skip"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:logger_name?level=INFO&showAll=true&skipBodyLineSeparator=true"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

