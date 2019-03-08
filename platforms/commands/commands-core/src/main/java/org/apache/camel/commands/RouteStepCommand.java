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
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Unmarshaller
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
name|dump
operator|.
name|ProcessorStatDump
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
name|dump
operator|.
name|RouteStatDump
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
name|dump
operator|.
name|StepStatDump
import|;
end_import

begin_comment
comment|/**  * Command to display step information about a Camel route.  */
end_comment

begin_class
DECL|class|RouteStepCommand
specifier|public
class|class
name|RouteStepCommand
extends|extends
name|AbstractRouteCommand
block|{
DECL|field|HEADER_FORMAT
specifier|protected
specifier|static
specifier|final
name|String
name|HEADER_FORMAT
init|=
literal|"%-30s %10s %12s %12s %12s %12s %12s %12s"
decl_stmt|;
DECL|field|OUTPUT_FORMAT
specifier|protected
specifier|static
specifier|final
name|String
name|OUTPUT_FORMAT
init|=
literal|"%-30s %10d %12d %12d %12d %12d %12d %12d"
decl_stmt|;
DECL|field|stringEscape
specifier|private
name|StringEscape
name|stringEscape
decl_stmt|;
DECL|field|previousCamelContextName
specifier|private
specifier|volatile
name|String
name|previousCamelContextName
decl_stmt|;
DECL|method|RouteStepCommand (String route, String context)
specifier|public
name|RouteStepCommand
parameter_list|(
name|String
name|route
parameter_list|,
name|String
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|route
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the {@link StringEscape} to use.      */
DECL|method|setStringEscape (StringEscape stringEscape)
specifier|public
name|void
name|setStringEscape
parameter_list|(
name|StringEscape
name|stringEscape
parameter_list|)
block|{
name|this
operator|.
name|stringEscape
operator|=
name|stringEscape
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|executeOnRoute (CamelController camelController, String contextName, String routeId, PrintStream out, PrintStream err)
specifier|public
name|void
name|executeOnRoute
parameter_list|(
name|CamelController
name|camelController
parameter_list|,
name|String
name|contextName
parameter_list|,
name|String
name|routeId
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
name|JAXBContext
name|context
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|RouteStatDump
operator|.
name|class
argument_list|)
decl_stmt|;
name|Unmarshaller
name|unmarshaller
init|=
name|context
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
comment|// write new header for new camel context
if|if
condition|(
name|previousCamelContextName
operator|==
literal|null
operator|||
operator|!
name|previousCamelContextName
operator|.
name|equals
argument_list|(
name|contextName
argument_list|)
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|stringEscape
operator|.
name|unescapeJava
argument_list|(
literal|"\u001B[1mStep\u001B[0m"
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|stringEscape
operator|.
name|unescapeJava
argument_list|(
literal|"\tCamel Context: "
operator|+
name|contextName
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|HEADER_FORMAT
argument_list|,
literal|"Id"
argument_list|,
literal|"Count"
argument_list|,
literal|"Last (ms)"
argument_list|,
literal|"Delta (ms)"
argument_list|,
literal|"Mean (ms)"
argument_list|,
literal|"Min (ms)"
argument_list|,
literal|"Max (ms)"
argument_list|,
literal|"Total (ms)"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|xml
init|=
name|camelController
operator|.
name|getStepStatsAsXml
argument_list|(
name|routeId
argument_list|,
name|contextName
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|xml
operator|!=
literal|null
condition|)
block|{
name|RouteStatDump
name|route
init|=
operator|(
name|RouteStatDump
operator|)
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
operator|new
name|StringReader
argument_list|(
name|xml
argument_list|)
argument_list|)
decl_stmt|;
name|long
name|count
init|=
name|route
operator|.
name|getExchangesCompleted
argument_list|()
operator|+
name|route
operator|.
name|getExchangesFailed
argument_list|()
decl_stmt|;
name|out
operator|.
name|println
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|OUTPUT_FORMAT
argument_list|,
name|route
operator|.
name|getId
argument_list|()
argument_list|,
name|count
argument_list|,
name|route
operator|.
name|getLastProcessingTime
argument_list|()
argument_list|,
name|route
operator|.
name|getDeltaProcessingTime
argument_list|()
argument_list|,
name|route
operator|.
name|getMeanProcessingTime
argument_list|()
argument_list|,
name|route
operator|.
name|getMinProcessingTime
argument_list|()
argument_list|,
name|route
operator|.
name|getMaxProcessingTime
argument_list|()
argument_list|,
name|route
operator|.
name|getTotalProcessingTime
argument_list|()
argument_list|,
name|route
operator|.
name|getSelfProcessingTime
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|StepStatDump
name|ss
range|:
name|route
operator|.
name|getStepStats
argument_list|()
control|)
block|{
name|count
operator|=
name|ss
operator|.
name|getExchangesCompleted
argument_list|()
operator|+
name|ss
operator|.
name|getExchangesFailed
argument_list|()
expr_stmt|;
comment|// indent step id with 2 spaces
name|out
operator|.
name|println
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|OUTPUT_FORMAT
argument_list|,
literal|"  "
operator|+
name|ss
operator|.
name|getId
argument_list|()
argument_list|,
name|count
argument_list|,
name|ss
operator|.
name|getLastProcessingTime
argument_list|()
argument_list|,
name|ss
operator|.
name|getDeltaProcessingTime
argument_list|()
argument_list|,
name|ss
operator|.
name|getMeanProcessingTime
argument_list|()
argument_list|,
name|ss
operator|.
name|getMinProcessingTime
argument_list|()
argument_list|,
name|ss
operator|.
name|getMaxProcessingTime
argument_list|()
argument_list|,
name|ss
operator|.
name|getTotalProcessingTime
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// we want to group routes from the same context in the same table
name|previousCamelContextName
operator|=
name|contextName
expr_stmt|;
block|}
block|}
end_class

end_unit

