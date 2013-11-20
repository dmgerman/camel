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
name|List
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
name|UnitUtils
operator|.
name|printUnitFromBytes
import|;
end_import

begin_comment
comment|/**  * Command to display detailed information about a Camel context.  */
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
literal|"context-info"
argument_list|,
name|description
operator|=
literal|"Display detailed information about a Camel context."
argument_list|)
DECL|class|ContextInfo
specifier|public
class|class
name|ContextInfo
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
literal|"name"
argument_list|,
name|description
operator|=
literal|"The name of the Camel context"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
DECL|field|name
name|String
name|name
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
literal|"mode"
argument_list|,
name|description
operator|=
literal|"Allows for different display modes (--verbose, etc)"
argument_list|,
name|required
operator|=
literal|false
argument_list|,
name|multiValued
operator|=
literal|false
argument_list|)
DECL|field|mode
name|String
name|mode
decl_stmt|;
DECL|method|doExecute ()
specifier|public
name|Object
name|doExecute
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|camelContext
init|=
name|camelController
operator|.
name|getCamelContext
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|camelContext
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
literal|"Camel context "
operator|+
name|name
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
literal|"\u001B[1m\u001B[33mCamel Context "
operator|+
name|name
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
literal|"\tName: "
operator|+
name|camelContext
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
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"\tManagementName: "
operator|+
name|camelContext
operator|.
name|getManagementName
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
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"\tVersion: "
operator|+
name|camelContext
operator|.
name|getVersion
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
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"\tStatus: "
operator|+
name|camelContext
operator|.
name|getStatus
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
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"\tUptime: "
operator|+
name|camelContext
operator|.
name|getUptime
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// the statistics are in the mbeans
name|printCamelManagedBeansStatus
argument_list|(
name|camelContext
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
literal|"\u001B[1mMiscellaneous\u001B[0m"
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
literal|"\tAuto Startup: "
operator|+
name|camelContext
operator|.
name|isAutoStartup
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
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"\tStarting Routes: "
operator|+
name|camelContext
operator|.
name|isStartingRoutes
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
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"\tSuspended: "
operator|+
name|camelContext
operator|.
name|isSuspended
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
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"\tShutdown timeout: "
operator|+
name|camelContext
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|getTimeUnit
argument_list|()
operator|.
name|toSeconds
argument_list|(
name|camelContext
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|getTimeout
argument_list|()
argument_list|)
operator|+
literal|" sec."
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
literal|"\tMessage History: "
operator|+
name|camelContext
operator|.
name|isMessageHistory
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
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"\tTracing: "
operator|+
name|camelContext
operator|.
name|isTracing
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
name|String
argument_list|>
name|entry
range|:
name|camelContext
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
literal|"\u001B[1mAdvanced\u001B[0m"
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
literal|"\tClassResolver: "
operator|+
name|camelContext
operator|.
name|getClassResolver
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
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"\tPackageScanClassResolver: "
operator|+
name|camelContext
operator|.
name|getPackageScanClassResolver
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
name|StringEscapeUtils
operator|.
name|unescapeJava
argument_list|(
literal|"\tApplicationContextClassLoader: "
operator|+
name|camelContext
operator|.
name|getApplicationContextClassLoader
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
literal|"\u001B[1mComponents\u001B[0m"
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|component
range|:
name|camelContext
operator|.
name|getComponentNames
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
name|component
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
literal|"\u001B[1mDataformats\u001B[0m"
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|name
range|:
name|camelContext
operator|.
name|getDataFormats
argument_list|()
operator|.
name|keySet
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
name|name
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
literal|"\u001B[1mLanguages\u001B[0m"
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|language
range|:
name|camelContext
operator|.
name|getLanguageNames
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
name|language
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|mode
operator|!=
literal|null
operator|&&
name|mode
operator|.
name|equals
argument_list|(
literal|"--verbose"
argument_list|)
condition|)
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
literal|"\u001B[1mEndpoints\u001B[0m"
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Endpoint
name|endpoint
range|:
name|camelContext
operator|.
name|getEndpoints
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
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
literal|"\u001B[1mRoutes\u001B[0m"
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Route
name|route
range|:
name|camelContext
operator|.
name|getRoutes
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
name|route
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|printCamelManagedBeansStatus (CamelContext camelContext)
specifier|protected
name|void
name|printCamelManagedBeansStatus
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
throws|throws
name|Exception
block|{
comment|// the statistics are in the mbeans
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
name|ObjectName
name|contextMBean
init|=
literal|null
decl_stmt|;
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
literal|":type=context,name=\""
operator|+
name|name
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
name|contextMBean
operator|=
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|mBeanServer
operator|.
name|isRegistered
argument_list|(
name|contextMBean
argument_list|)
condition|)
block|{
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
name|contextMBean
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
name|contextMBean
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
name|contextMBean
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
name|contextMBean
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
literal|"ms"
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
name|contextMBean
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
literal|"ms"
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
name|contextMBean
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
literal|"ms"
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
name|contextMBean
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
literal|"ms"
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
name|contextMBean
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
literal|"ms"
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
name|contextMBean
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
literal|"ms"
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
name|contextMBean
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
name|contextMBean
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
name|contextMBean
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
name|contextMBean
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
name|contextMBean
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
name|contextMBean
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
comment|// add type converter statistics if enabled
if|if
condition|(
name|camelContext
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|getStatistics
argument_list|()
operator|.
name|isStatisticsEnabled
argument_list|()
condition|)
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
name|String
operator|.
name|format
argument_list|(
literal|"\tTypeConverterRegistry utilization: [attempts=%s, hits=%s, misses=%s, failures=%s]"
argument_list|,
name|camelContext
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|getStatistics
argument_list|()
operator|.
name|getAttemptCounter
argument_list|()
argument_list|,
name|camelContext
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|getStatistics
argument_list|()
operator|.
name|getHitCounter
argument_list|()
argument_list|,
name|camelContext
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|getStatistics
argument_list|()
operator|.
name|getMissCounter
argument_list|()
argument_list|,
name|camelContext
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|getStatistics
argument_list|()
operator|.
name|getFailedCounter
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// add stream caching details if enabled
if|if
condition|(
name|camelContext
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|isEnabled
argument_list|()
condition|)
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
name|String
operator|.
name|format
argument_list|(
literal|"\tStreamCachingStrategy: [spoolDirectory=%s, spoolChiper=%s, spoolThreshold=%s, spoolUsedHeapMemoryThreshold=%s, "
operator|+
literal|"spoolUsedHeapMemoryLimit=%s, anySpoolRules=%s, bufferSize=%s, removeSpoolDirectoryWhenStopping=%s, statisticsEnabled=%s]"
argument_list|,
name|camelContext
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|getSpoolDirectory
argument_list|()
argument_list|,
name|camelContext
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|getSpoolChiper
argument_list|()
argument_list|,
name|camelContext
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|getSpoolThreshold
argument_list|()
argument_list|,
name|camelContext
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|getSpoolUsedHeapMemoryThreshold
argument_list|()
argument_list|,
name|camelContext
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|getSpoolUsedHeapMemoryLimit
argument_list|()
argument_list|,
name|camelContext
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|isAnySpoolRules
argument_list|()
argument_list|,
name|camelContext
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|getBufferSize
argument_list|()
argument_list|,
name|camelContext
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|isRemoveSpoolDirectoryWhenStopping
argument_list|()
argument_list|,
name|camelContext
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|getStatistics
argument_list|()
operator|.
name|isStatisticsEnabled
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|camelContext
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|getStatistics
argument_list|()
operator|.
name|isStatisticsEnabled
argument_list|()
condition|)
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
name|String
operator|.
name|format
argument_list|(
literal|"\t                       [cacheMemoryCounter=%s, cacheMemorySize=%s, cacheMemoryAverageSize=%s, cacheSpoolCounter=%s, "
operator|+
literal|"cacheSpoolSize=%s, cacheSpoolAverageSize=%s]"
argument_list|,
name|camelContext
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|getStatistics
argument_list|()
operator|.
name|getCacheMemoryCounter
argument_list|()
argument_list|,
name|printUnitFromBytes
argument_list|(
name|camelContext
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|getStatistics
argument_list|()
operator|.
name|getCacheMemorySize
argument_list|()
argument_list|)
argument_list|,
name|printUnitFromBytes
argument_list|(
name|camelContext
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|getStatistics
argument_list|()
operator|.
name|getCacheMemoryAverageSize
argument_list|()
argument_list|)
argument_list|,
name|camelContext
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|getStatistics
argument_list|()
operator|.
name|getCacheSpoolCounter
argument_list|()
argument_list|,
name|printUnitFromBytes
argument_list|(
name|camelContext
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|getStatistics
argument_list|()
operator|.
name|getCacheSpoolSize
argument_list|()
argument_list|)
argument_list|,
name|printUnitFromBytes
argument_list|(
name|camelContext
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|getStatistics
argument_list|()
operator|.
name|getCacheSpoolAverageSize
argument_list|()
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|long
name|activeRoutes
init|=
literal|0
decl_stmt|;
name|long
name|inactiveRoutes
init|=
literal|0
decl_stmt|;
name|List
argument_list|<
name|Route
argument_list|>
name|routeList
init|=
name|camelContext
operator|.
name|getRoutes
argument_list|()
decl_stmt|;
for|for
control|(
name|Route
name|route
range|:
name|routeList
control|)
block|{
if|if
condition|(
name|camelContext
operator|.
name|getRouteStatus
argument_list|(
name|route
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|isStarted
argument_list|()
condition|)
block|{
name|activeRoutes
operator|++
expr_stmt|;
block|}
else|else
block|{
name|inactiveRoutes
operator|++
expr_stmt|;
block|}
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
literal|"\tNumber of running routes: "
operator|+
name|activeRoutes
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
literal|"\tNumber of not running routes: "
operator|+
name|inactiveRoutes
argument_list|)
argument_list|)
expr_stmt|;
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
block|}
block|}
end_class

end_unit

