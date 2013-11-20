begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.karaf.commands
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|karaf
operator|.
name|commands
package|;
end_package

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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
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
name|Route
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
name|ModelHelper
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
name|spi
operator|.
name|ManagementAgent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|gogo
operator|.
name|commands
operator|.
name|Argument
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|gogo
operator|.
name|commands
operator|.
name|Command
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|util
operator|.
name|StringEscapeUtils
import|;
end_import

begin_comment
comment|/**  * Command to display detailed information about a Camel route.  */
end_comment

begin_class
annotation|@
name|Command
argument_list|(
name|scope
operator|=
literal|"camel"
argument_list|,
name|name
operator|=
literal|"route-info"
argument_list|,
name|description
operator|=
literal|"Display information about a Camel route."
argument_list|)
DECL|class|RouteInfo
specifier|public
class|class
name|RouteInfo
extends|extends
name|CamelCommandSupport
block|{
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|name
operator|=
literal|"route"
argument_list|,
name|description
operator|=
literal|"The Camel route ID."
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
DECL|field|route
name|String
name|route
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|1
argument_list|,
name|name
operator|=
literal|"context"
argument_list|,
name|description
operator|=
literal|"The Camel context name."
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
DECL|field|context
name|String
name|context
decl_stmt|;
DECL|method|doExecute ()
specifier|public
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|Route
name|camelRoute
init|=
name|camelController
operator|.
name|getRoute
argument_list|(
name|route
argument_list|,
name|context
argument_list|)
decl_stmt|;
if|if
condition|(
name|camelRoute
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Camel route "
operator|+
name|route
operator|+
literal|" not found."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"\u001B[1m\u001B[33mCamel Route "
operator|+
name|camelRoute
operator|.
name|getId
argument_list|()
operator|+
literal|"\u001B[0m"
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"\tCamel Context: "
operator|+
name|camelRoute
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"\u001B[1mProperties\u001B[0m"
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|camelRoute
operator|.
name|getProperties
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"\t"
operator|+
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|" = "
operator|+
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"\u001B[1mStatistics\u001B[0m"
argument_list|)
argument_list|)
expr_stmt|;
name|CamelContext
name|camelContext
init|=
name|camelRoute
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getCamelContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|camelContext
operator|!=
literal|null
condition|)
block|{
name|ManagementAgent
name|agent
init|=
name|camelContext
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
decl_stmt|;
if|if
condition|(
name|agent
operator|!=
literal|null
condition|)
block|{
name|MBeanServer
name|mBeanServer
init|=
name|agent
operator|.
name|getMBeanServer
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|set
init|=
name|mBeanServer
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
name|agent
operator|.
name|getMBeanObjectDomainName
argument_list|()
operator|+
literal|":type=routes,name=\""
operator|+
name|route
operator|+
literal|"\",*"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|ObjectName
argument_list|>
name|iterator
init|=
name|set
operator|.
name|iterator
argument_list|()
decl_stmt|;
if|if
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ObjectName
name|routeMBean
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// the route must be part of the camel context
name|String
name|camelId
init|=
operator|(
name|String
operator|)
name|mBeanServer
operator|.
name|getAttribute
argument_list|(
name|routeMBean
argument_list|,
literal|"CamelId"
argument_list|)
decl_stmt|;
if|if
condition|(
name|camelId
operator|!=
literal|null
operator|&&
name|camelId
operator|.
name|equals
argument_list|(
name|camelContext
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|Integer
name|inflightExchange
init|=
operator|(
name|Integer
operator|)
name|mBeanServer
operator|.
name|getAttribute
argument_list|(
name|routeMBean
argument_list|,
literal|"InflightExchanges"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"\tInflight Exchanges: "
operator|+
name|inflightExchange
argument_list|)
argument_list|)
expr_stmt|;
name|Long
name|exchangesTotal
init|=
operator|(
name|Long
operator|)
name|mBeanServer
operator|.
name|getAttribute
argument_list|(
name|routeMBean
argument_list|,
literal|"ExchangesTotal"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"\tExchanges Total: "
operator|+
name|exchangesTotal
argument_list|)
argument_list|)
expr_stmt|;
name|Long
name|exchangesCompleted
init|=
operator|(
name|Long
operator|)
name|mBeanServer
operator|.
name|getAttribute
argument_list|(
name|routeMBean
argument_list|,
literal|"ExchangesCompleted"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"\tExchanges Completed: "
operator|+
name|exchangesCompleted
argument_list|)
argument_list|)
expr_stmt|;
name|Long
name|exchangesFailed
init|=
operator|(
name|Long
operator|)
name|mBeanServer
operator|.
name|getAttribute
argument_list|(
name|routeMBean
argument_list|,
literal|"ExchangesFailed"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"\tExchanges Failed: "
operator|+
name|exchangesFailed
argument_list|)
argument_list|)
expr_stmt|;
name|Long
name|minProcessingTime
init|=
operator|(
name|Long
operator|)
name|mBeanServer
operator|.
name|getAttribute
argument_list|(
name|routeMBean
argument_list|,
literal|"MinProcessingTime"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"\tMin Processing Time: "
operator|+
name|minProcessingTime
operator|+
literal|" ms"
argument_list|)
argument_list|)
expr_stmt|;
name|Long
name|maxProcessingTime
init|=
operator|(
name|Long
operator|)
name|mBeanServer
operator|.
name|getAttribute
argument_list|(
name|routeMBean
argument_list|,
literal|"MaxProcessingTime"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"\tMax Processing Time: "
operator|+
name|maxProcessingTime
operator|+
literal|" ms"
argument_list|)
argument_list|)
expr_stmt|;
name|Long
name|meanProcessingTime
init|=
operator|(
name|Long
operator|)
name|mBeanServer
operator|.
name|getAttribute
argument_list|(
name|routeMBean
argument_list|,
literal|"MeanProcessingTime"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"\tMean Processing Time: "
operator|+
name|meanProcessingTime
operator|+
literal|" ms"
argument_list|)
argument_list|)
expr_stmt|;
name|Long
name|totalProcessingTime
init|=
operator|(
name|Long
operator|)
name|mBeanServer
operator|.
name|getAttribute
argument_list|(
name|routeMBean
argument_list|,
literal|"TotalProcessingTime"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"\tTotal Processing Time: "
operator|+
name|totalProcessingTime
operator|+
literal|" ms"
argument_list|)
argument_list|)
expr_stmt|;
name|Long
name|lastProcessingTime
init|=
operator|(
name|Long
operator|)
name|mBeanServer
operator|.
name|getAttribute
argument_list|(
name|routeMBean
argument_list|,
literal|"LastProcessingTime"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"\tLast Processing Time: "
operator|+
name|lastProcessingTime
operator|+
literal|" ms"
argument_list|)
argument_list|)
expr_stmt|;
name|Long
name|deltaProcessingTime
init|=
operator|(
name|Long
operator|)
name|mBeanServer
operator|.
name|getAttribute
argument_list|(
name|routeMBean
argument_list|,
literal|"DeltaProcessingTime"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"\tDelta Processing Time: "
operator|+
name|deltaProcessingTime
operator|+
literal|" ms"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|load01
init|=
operator|(
name|String
operator|)
name|mBeanServer
operator|.
name|getAttribute
argument_list|(
name|routeMBean
argument_list|,
literal|"Load01"
argument_list|)
decl_stmt|;
name|String
name|load05
init|=
operator|(
name|String
operator|)
name|mBeanServer
operator|.
name|getAttribute
argument_list|(
name|routeMBean
argument_list|,
literal|"Load05"
argument_list|)
decl_stmt|;
name|String
name|load15
init|=
operator|(
name|String
operator|)
name|mBeanServer
operator|.
name|getAttribute
argument_list|(
name|routeMBean
argument_list|,
literal|"Load15"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"\tLoad Avg: "
operator|+
name|load01
operator|+
literal|", "
operator|+
name|load05
operator|+
literal|", "
operator|+
name|load15
argument_list|)
argument_list|)
expr_stmt|;
comment|// Test for null to see if a any exchanges have been processed first to avoid NPE
name|Object
name|resetTimestampObj
init|=
name|mBeanServer
operator|.
name|getAttribute
argument_list|(
name|routeMBean
argument_list|,
literal|"ResetTimestamp"
argument_list|)
decl_stmt|;
name|SimpleDateFormat
name|format
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd HH:mm:ss"
argument_list|)
decl_stmt|;
if|if
condition|(
name|resetTimestampObj
operator|==
literal|null
condition|)
block|{
comment|// Print an empty value for scripting
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|StringEscapeUtils
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
name|firstExchangeTimestamp
init|=
operator|(
name|Date
operator|)
name|resetTimestampObj
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"\tReset Statistics Date: "
operator|+
name|format
operator|.
name|format
argument_list|(
name|firstExchangeTimestamp
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Test for null to see if a any exchanges have been processed first to avoid NPE
name|Object
name|firstExchangeTimestampObj
init|=
name|mBeanServer
operator|.
name|getAttribute
argument_list|(
name|routeMBean
argument_list|,
literal|"FirstExchangeCompletedTimestamp"
argument_list|)
decl_stmt|;
if|if
condition|(
name|firstExchangeTimestampObj
operator|==
literal|null
condition|)
block|{
comment|// Print an empty value for scripting
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|StringEscapeUtils
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
name|firstExchangeTimestamp
init|=
operator|(
name|Date
operator|)
name|firstExchangeTimestampObj
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"\tFirst Exchange Date: "
operator|+
name|format
operator|.
name|format
argument_list|(
name|firstExchangeTimestamp
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Again, check for null to avoid NPE
name|Object
name|lastExchangeCompletedTimestampObj
init|=
name|mBeanServer
operator|.
name|getAttribute
argument_list|(
name|routeMBean
argument_list|,
literal|"LastExchangeCompletedTimestamp"
argument_list|)
decl_stmt|;
if|if
condition|(
name|lastExchangeCompletedTimestampObj
operator|==
literal|null
condition|)
block|{
comment|// Print an empty value for scripting
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"\tLast Exchange Completed Date:"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Date
name|lastExchangeCompletedTimestamp
init|=
operator|(
name|Date
operator|)
name|lastExchangeCompletedTimestampObj
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"\tLast Exchange Completed Date: "
operator|+
name|format
operator|.
name|format
argument_list|(
name|lastExchangeCompletedTimestamp
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"\u001B[31mJMX Agent of Camel is not reachable. Maybe it has been disabled on the Camel context"
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"In consequence, some statistics are not available.\u001B[0m"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"\u001B[1mDefinition\u001B[0m"
argument_list|)
argument_list|)
expr_stmt|;
name|RouteDefinition
name|definition
init|=
name|camelController
operator|.
name|getRouteDefinition
argument_list|(
name|route
argument_list|,
name|camelRoute
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
name|ModelHelper
operator|.
name|dumpModelAsXml
argument_list|(
name|definition
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

