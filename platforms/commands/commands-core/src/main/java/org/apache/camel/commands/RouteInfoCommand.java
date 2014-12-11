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
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|util
operator|.
name|ObjectHelper
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
name|RouteStatDump
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
name|isEmpty
import|;
end_import

begin_comment
comment|/**  * Command to display detailed information about a Camel route.  */
end_comment

begin_class
DECL|class|RouteInfoCommand
specifier|public
class|class
name|RouteInfoCommand
extends|extends
name|AbstractRouteCommand
block|{
DECL|field|XML_TIMESTAMP_FORMAT
specifier|public
specifier|static
specifier|final
name|String
name|XML_TIMESTAMP_FORMAT
init|=
literal|"yyyy-MM-dd'T'HH:mm:ss.SSSZ"
decl_stmt|;
DECL|field|OUTPUT_TIMESTAMP_FORMAT
specifier|public
specifier|static
specifier|final
name|String
name|OUTPUT_TIMESTAMP_FORMAT
init|=
literal|"yyyy-MM-dd HH:mm:ss"
decl_stmt|;
DECL|field|stringEscape
specifier|private
name|StringEscape
name|stringEscape
decl_stmt|;
DECL|method|RouteInfoCommand (String route, String context)
specifier|public
name|RouteInfoCommand
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
comment|/**      * Sets the {@link org.apache.camel.commands.StringEscape} to use.      */
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
name|out
operator|.
name|println
argument_list|(
name|stringEscape
operator|.
name|unescapeJava
argument_list|(
literal|"\u001B[1mCamel Route "
operator|+
name|routeId
operator|+
literal|"\u001B[0m"
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
literal|"\u001B[1mStatistics\u001B[0m"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|xml
init|=
name|camelController
operator|.
name|getRouteStatsAsXml
argument_list|(
name|routeId
argument_list|,
name|contextName
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|xml
operator|!=
literal|null
condition|)
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
name|total
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
name|stringEscape
operator|.
name|unescapeJava
argument_list|(
literal|"\tExchanges Total: "
operator|+
name|total
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
literal|"\tExchanges Completed: "
operator|+
name|route
operator|.
name|getExchangesCompleted
argument_list|()
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
literal|"\tExchanges Failed: "
operator|+
name|route
operator|.
name|getExchangesFailed
argument_list|()
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
literal|"\tExchanges Inflight: "
operator|+
name|route
operator|.
name|getExchangesInflight
argument_list|()
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
literal|"\tMin Processing Time: "
operator|+
name|route
operator|.
name|getMinProcessingTime
argument_list|()
operator|+
literal|" ms"
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
literal|"\tMax Processing Time: "
operator|+
name|route
operator|.
name|getMaxProcessingTime
argument_list|()
operator|+
literal|" ms"
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
literal|"\tMean Processing Time: "
operator|+
name|route
operator|.
name|getMeanProcessingTime
argument_list|()
operator|+
literal|" ms"
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
literal|"\tTotal Processing Time: "
operator|+
name|route
operator|.
name|getTotalProcessingTime
argument_list|()
operator|+
literal|" ms"
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
literal|"\tLast Processing Time: "
operator|+
name|route
operator|.
name|getLastProcessingTime
argument_list|()
operator|+
literal|" ms"
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
literal|"\tDelta Processing Time: "
operator|+
name|route
operator|.
name|getDeltaProcessingTime
argument_list|()
operator|+
literal|" ms"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Test for null to see if a any exchanges have been processed first to avoid NPE
if|if
condition|(
name|isEmpty
argument_list|(
name|route
operator|.
name|getResetTimestamp
argument_list|()
argument_list|)
condition|)
block|{
comment|// Print an empty value for scripting
name|out
operator|.
name|println
argument_list|(
name|stringEscape
operator|.
name|unescapeJava
argument_list|(
literal|"\tReset Statistics Date:"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Date
name|date
init|=
operator|new
name|SimpleDateFormat
argument_list|(
name|XML_TIMESTAMP_FORMAT
argument_list|)
operator|.
name|parse
argument_list|(
name|route
operator|.
name|getResetTimestamp
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|text
init|=
operator|new
name|SimpleDateFormat
argument_list|(
name|OUTPUT_TIMESTAMP_FORMAT
argument_list|)
operator|.
name|format
argument_list|(
name|date
argument_list|)
decl_stmt|;
name|out
operator|.
name|println
argument_list|(
name|stringEscape
operator|.
name|unescapeJava
argument_list|(
literal|"\tReset Statistics Date: "
operator|+
name|text
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Test for null to see if a any exchanges have been processed first to avoid NPE
if|if
condition|(
name|isEmpty
argument_list|(
name|route
operator|.
name|getFirstExchangeCompletedTimestamp
argument_list|()
argument_list|)
condition|)
block|{
comment|// Print an empty value for scripting
name|out
operator|.
name|println
argument_list|(
name|stringEscape
operator|.
name|unescapeJava
argument_list|(
literal|"\tFirst Exchange Date:"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Date
name|date
init|=
operator|new
name|SimpleDateFormat
argument_list|(
name|XML_TIMESTAMP_FORMAT
argument_list|)
operator|.
name|parse
argument_list|(
name|route
operator|.
name|getFirstExchangeCompletedTimestamp
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|text
init|=
operator|new
name|SimpleDateFormat
argument_list|(
name|OUTPUT_TIMESTAMP_FORMAT
argument_list|)
operator|.
name|format
argument_list|(
name|date
argument_list|)
decl_stmt|;
name|out
operator|.
name|println
argument_list|(
name|stringEscape
operator|.
name|unescapeJava
argument_list|(
literal|"\tFirst Exchange Date: "
operator|+
name|text
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Test for null to see if a any exchanges have been processed first to avoid NPE
if|if
condition|(
name|isEmpty
argument_list|(
name|route
operator|.
name|getLastExchangeCompletedTimestamp
argument_list|()
argument_list|)
condition|)
block|{
comment|// Print an empty value for scripting
name|out
operator|.
name|println
argument_list|(
name|stringEscape
operator|.
name|unescapeJava
argument_list|(
literal|"\tLast Exchange Date:"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Date
name|date
init|=
operator|new
name|SimpleDateFormat
argument_list|(
name|XML_TIMESTAMP_FORMAT
argument_list|)
operator|.
name|parse
argument_list|(
name|route
operator|.
name|getLastExchangeCompletedTimestamp
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|text
init|=
operator|new
name|SimpleDateFormat
argument_list|(
name|OUTPUT_TIMESTAMP_FORMAT
argument_list|)
operator|.
name|format
argument_list|(
name|date
argument_list|)
decl_stmt|;
name|out
operator|.
name|println
argument_list|(
name|stringEscape
operator|.
name|unescapeJava
argument_list|(
literal|"\tLast Exchange Date: "
operator|+
name|text
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|xml
operator|=
name|camelController
operator|.
name|getRouteModelAsXml
argument_list|(
name|routeId
argument_list|,
name|contextName
argument_list|)
expr_stmt|;
if|if
condition|(
name|xml
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
name|stringEscape
operator|.
name|unescapeJava
argument_list|(
literal|"\u001B[1mDefinition\u001B[0m"
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
name|xml
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

