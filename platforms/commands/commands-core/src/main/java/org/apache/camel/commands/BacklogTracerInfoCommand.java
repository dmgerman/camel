begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.commands
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|commands
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
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
name|CamelContext
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
name|processor
operator|.
name|interceptor
operator|.
name|BacklogTracer
import|;
end_import

begin_comment
comment|/**  * Command to use the<a href="camel.apache.org/backlogtracer">Backlog Tracer</a>.  */
end_comment

begin_class
DECL|class|BacklogTracerInfoCommand
specifier|public
class|class
name|BacklogTracerInfoCommand
extends|extends
name|AbstractContextCommand
block|{
DECL|method|BacklogTracerInfoCommand (String context)
specifier|public
name|BacklogTracerInfoCommand
parameter_list|(
name|String
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|performContextCommand (CamelController camelController, CamelContext camelContext, PrintStream out, PrintStream err)
specifier|protected
name|Object
name|performContextCommand
parameter_list|(
name|CamelController
name|camelController
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|,
name|PrintStream
name|out
parameter_list|,
name|PrintStream
name|err
parameter_list|)
throws|throws
name|Exception
block|{
name|BacklogTracer
name|backlogTracer
init|=
name|BacklogTracer
operator|.
name|getBacklogTracer
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
if|if
condition|(
name|backlogTracer
operator|==
literal|null
condition|)
block|{
name|backlogTracer
operator|=
operator|(
name|BacklogTracer
operator|)
name|camelContext
operator|.
name|getDefaultBacklogTracer
argument_list|()
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|(
literal|"BacklogTracer context:\t\t"
operator|+
name|camelContext
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"BacklogTracer enabled:\t\t"
operator|+
name|backlogTracer
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"BacklogTracer pattern:\t\t"
operator|+
operator|(
name|backlogTracer
operator|.
name|getTracePattern
argument_list|()
operator|!=
literal|null
condition|?
name|backlogTracer
operator|.
name|getTracePattern
argument_list|()
else|:
literal|""
operator|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"BacklogTracer filter:\t\t"
operator|+
operator|(
name|backlogTracer
operator|.
name|getTraceFilter
argument_list|()
operator|!=
literal|null
condition|?
name|backlogTracer
operator|.
name|getTraceFilter
argument_list|()
else|:
literal|""
operator|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"BacklogTracer removeOnDump:\t"
operator|+
name|backlogTracer
operator|.
name|isRemoveOnDump
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"BacklogTracer backlogSize:\t"
operator|+
name|backlogTracer
operator|.
name|getBacklogSize
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"BacklogTracer tracerCount:\t"
operator|+
name|backlogTracer
operator|.
name|getTraceCounter
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"BacklogTracer body..."
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"\tmaxChars:\t\t"
operator|+
name|backlogTracer
operator|.
name|getBodyMaxChars
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"\tincludeFiles:\t\t"
operator|+
name|backlogTracer
operator|.
name|isBodyIncludeFiles
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"\tincludeStreams:\t\t"
operator|+
name|backlogTracer
operator|.
name|isBodyIncludeStreams
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

