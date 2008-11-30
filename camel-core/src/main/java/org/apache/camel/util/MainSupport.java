begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicBoolean
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
name|JAXBException
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
name|ProducerTemplate
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
name|camel
operator|.
name|impl
operator|.
name|DefaultCamelContext
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
name|impl
operator|.
name|ServiceSupport
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
name|RouteType
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
name|Debugger
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
name|view
operator|.
name|ModelFileGenerator
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
name|view
operator|.
name|RouteDotGenerator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|MainSupport
specifier|public
specifier|abstract
class|class
name|MainSupport
extends|extends
name|ServiceSupport
block|{
DECL|field|LOG
specifier|protected
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|MainSupport
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|dotOutputDir
specifier|protected
name|String
name|dotOutputDir
decl_stmt|;
DECL|field|options
specifier|private
name|List
argument_list|<
name|Option
argument_list|>
name|options
init|=
operator|new
name|ArrayList
argument_list|<
name|Option
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|latch
specifier|private
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|field|completed
specifier|private
name|AtomicBoolean
name|completed
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
DECL|field|duration
specifier|private
name|long
name|duration
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|timeUnit
specifier|private
name|TimeUnit
name|timeUnit
init|=
name|TimeUnit
operator|.
name|MILLISECONDS
decl_stmt|;
DECL|field|routesOutputFile
specifier|private
name|String
name|routesOutputFile
decl_stmt|;
DECL|field|aggregateDot
specifier|private
name|boolean
name|aggregateDot
decl_stmt|;
DECL|field|debug
specifier|private
name|boolean
name|debug
decl_stmt|;
DECL|field|trace
specifier|private
name|boolean
name|trace
decl_stmt|;
DECL|field|routeBuilders
specifier|private
name|List
argument_list|<
name|RouteBuilder
argument_list|>
name|routeBuilders
init|=
operator|new
name|ArrayList
argument_list|<
name|RouteBuilder
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|camelContexts
specifier|private
name|List
argument_list|<
name|CamelContext
argument_list|>
name|camelContexts
init|=
operator|new
name|ArrayList
argument_list|<
name|CamelContext
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|camelTemplate
specifier|private
name|ProducerTemplate
name|camelTemplate
decl_stmt|;
DECL|method|MainSupport ()
specifier|protected
name|MainSupport
parameter_list|()
block|{
name|addOption
argument_list|(
operator|new
name|Option
argument_list|(
literal|"h"
argument_list|,
literal|"help"
argument_list|,
literal|"Displays the help screen"
argument_list|)
block|{
specifier|protected
name|void
name|doProcess
parameter_list|(
name|String
name|arg
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
block|{
name|showOptions
argument_list|()
expr_stmt|;
name|completed
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|addOption
argument_list|(
operator|new
name|ParameterOption
argument_list|(
literal|"o"
argument_list|,
literal|"outdir"
argument_list|,
literal|"Sets the DOT output directory where the visual representations of the routes are generated"
argument_list|,
literal|"dot"
argument_list|)
block|{
specifier|protected
name|void
name|doProcess
parameter_list|(
name|String
name|arg
parameter_list|,
name|String
name|parameter
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
block|{
name|setDotOutputDir
argument_list|(
name|parameter
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|addOption
argument_list|(
operator|new
name|ParameterOption
argument_list|(
literal|"ad"
argument_list|,
literal|"aggregate-dot"
argument_list|,
literal|"Aggregates all routes (in addition to individual route generation) into one context to create one monolithic DOT file for visual representations the entire system."
argument_list|,
literal|"aggregate-dot"
argument_list|)
block|{
specifier|protected
name|void
name|doProcess
parameter_list|(
name|String
name|arg
parameter_list|,
name|String
name|parameter
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
block|{
name|setAggregateDot
argument_list|(
literal|"true"
operator|.
name|equals
argument_list|(
name|parameter
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|addOption
argument_list|(
operator|new
name|ParameterOption
argument_list|(
literal|"d"
argument_list|,
literal|"duration"
argument_list|,
literal|"Sets the time duration that the applicaiton will run for, by default in milliseconds. You can use '10s' for 10 seconds etc"
argument_list|,
literal|"duration"
argument_list|)
block|{
specifier|protected
name|void
name|doProcess
parameter_list|(
name|String
name|arg
parameter_list|,
name|String
name|parameter
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
block|{
name|String
name|value
init|=
name|parameter
operator|.
name|toUpperCase
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|.
name|endsWith
argument_list|(
literal|"S"
argument_list|)
condition|)
block|{
name|value
operator|=
name|value
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|value
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
name|setTimeUnit
argument_list|(
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
name|setDuration
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|addOption
argument_list|(
operator|new
name|Option
argument_list|(
literal|"x"
argument_list|,
literal|"debug"
argument_list|,
literal|"Enables the debugger"
argument_list|)
block|{
specifier|protected
name|void
name|doProcess
parameter_list|(
name|String
name|arg
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
block|{
name|enableDebug
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|addOption
argument_list|(
operator|new
name|Option
argument_list|(
literal|"t"
argument_list|,
literal|"trace"
argument_list|,
literal|"Enables tracing"
argument_list|)
block|{
specifier|protected
name|void
name|doProcess
parameter_list|(
name|String
name|arg
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
block|{
name|enableTrace
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|addOption
argument_list|(
operator|new
name|ParameterOption
argument_list|(
literal|"out"
argument_list|,
literal|"output"
argument_list|,
literal|"Output all routes to the specified XML file"
argument_list|,
literal|"filename"
argument_list|)
block|{
specifier|protected
name|void
name|doProcess
parameter_list|(
name|String
name|arg
parameter_list|,
name|String
name|parameter
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
block|{
name|setRoutesOutputFile
argument_list|(
name|parameter
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * Runs this process with the given arguments      */
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
if|if
condition|(
operator|!
name|completed
operator|.
name|get
argument_list|()
condition|)
block|{
try|try
block|{
name|start
argument_list|()
expr_stmt|;
name|waitUntilCompleted
argument_list|()
expr_stmt|;
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Failed: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Marks this process as being completed      */
DECL|method|completed ()
specifier|public
name|void
name|completed
parameter_list|()
block|{
name|completed
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
comment|/**      * Displays the command line options      */
DECL|method|showOptions ()
specifier|public
name|void
name|showOptions
parameter_list|()
block|{
name|showOptionsHeader
argument_list|()
expr_stmt|;
for|for
control|(
name|Option
name|option
range|:
name|options
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|option
operator|.
name|getInformation
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Parses the command line arguments      */
DECL|method|parseArguments (String[] arguments)
specifier|public
name|void
name|parseArguments
parameter_list|(
name|String
index|[]
name|arguments
parameter_list|)
block|{
name|LinkedList
argument_list|<
name|String
argument_list|>
name|args
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|arguments
argument_list|)
argument_list|)
decl_stmt|;
name|boolean
name|valid
init|=
literal|true
decl_stmt|;
while|while
condition|(
operator|!
name|args
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|arg
init|=
name|args
operator|.
name|removeFirst
argument_list|()
decl_stmt|;
name|boolean
name|handled
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Option
name|option
range|:
name|options
control|)
block|{
if|if
condition|(
name|option
operator|.
name|processOption
argument_list|(
name|arg
argument_list|,
name|args
argument_list|)
condition|)
block|{
name|handled
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|handled
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Unknown option: "
operator|+
name|arg
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|valid
operator|=
literal|false
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|valid
condition|)
block|{
name|showOptions
argument_list|()
expr_stmt|;
name|completed
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|addOption (Option option)
specifier|public
name|void
name|addOption
parameter_list|(
name|Option
name|option
parameter_list|)
block|{
name|options
operator|.
name|add
argument_list|(
name|option
argument_list|)
expr_stmt|;
block|}
DECL|method|getDuration ()
specifier|public
name|long
name|getDuration
parameter_list|()
block|{
return|return
name|duration
return|;
block|}
comment|/**      * Sets the duration to run the application for in milliseconds until it      * should be terminated. Defaults to -1. Any value<= 0 will run forever.      *      * @param duration      */
DECL|method|setDuration (long duration)
specifier|public
name|void
name|setDuration
parameter_list|(
name|long
name|duration
parameter_list|)
block|{
name|this
operator|.
name|duration
operator|=
name|duration
expr_stmt|;
block|}
DECL|method|getTimeUnit ()
specifier|public
name|TimeUnit
name|getTimeUnit
parameter_list|()
block|{
return|return
name|timeUnit
return|;
block|}
comment|/**      * Sets the time unit duration      */
DECL|method|setTimeUnit (TimeUnit timeUnit)
specifier|public
name|void
name|setTimeUnit
parameter_list|(
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
name|this
operator|.
name|timeUnit
operator|=
name|timeUnit
expr_stmt|;
block|}
DECL|method|getDotOutputDir ()
specifier|public
name|String
name|getDotOutputDir
parameter_list|()
block|{
return|return
name|dotOutputDir
return|;
block|}
comment|/**      * Sets the output directory of the generated DOT Files to show the visual      * representation of the routes. A null value disables the dot file      * generation      */
DECL|method|setDotOutputDir (String dotOutputDir)
specifier|public
name|void
name|setDotOutputDir
parameter_list|(
name|String
name|dotOutputDir
parameter_list|)
block|{
name|this
operator|.
name|dotOutputDir
operator|=
name|dotOutputDir
expr_stmt|;
block|}
DECL|method|setAggregateDot (boolean aggregateDot)
specifier|public
name|void
name|setAggregateDot
parameter_list|(
name|boolean
name|aggregateDot
parameter_list|)
block|{
name|this
operator|.
name|aggregateDot
operator|=
name|aggregateDot
expr_stmt|;
block|}
DECL|method|isAggregateDot ()
specifier|public
name|boolean
name|isAggregateDot
parameter_list|()
block|{
return|return
name|aggregateDot
return|;
block|}
DECL|method|isDebug ()
specifier|public
name|boolean
name|isDebug
parameter_list|()
block|{
return|return
name|debug
return|;
block|}
DECL|method|enableDebug ()
specifier|public
name|void
name|enableDebug
parameter_list|()
block|{
name|this
operator|.
name|debug
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|isTrace ()
specifier|public
name|boolean
name|isTrace
parameter_list|()
block|{
return|return
name|trace
return|;
block|}
DECL|method|enableTrace ()
specifier|public
name|void
name|enableTrace
parameter_list|()
block|{
name|this
operator|.
name|trace
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|setRoutesOutputFile (String routesOutputFile)
specifier|public
name|void
name|setRoutesOutputFile
parameter_list|(
name|String
name|routesOutputFile
parameter_list|)
block|{
name|this
operator|.
name|routesOutputFile
operator|=
name|routesOutputFile
expr_stmt|;
block|}
DECL|method|getRoutesOutputFile ()
specifier|public
name|String
name|getRoutesOutputFile
parameter_list|()
block|{
return|return
name|routesOutputFile
return|;
block|}
comment|/**      * Returns the currently active debugger if one is enabled      *      * @return the current debugger or null if none is active      * @see #enableDebug()      */
DECL|method|getDebugger ()
specifier|public
name|Debugger
name|getDebugger
parameter_list|()
block|{
for|for
control|(
name|CamelContext
name|camelContext
range|:
name|camelContexts
control|)
block|{
name|Debugger
name|debugger
init|=
name|Debugger
operator|.
name|getDebugger
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
if|if
condition|(
name|debugger
operator|!=
literal|null
condition|)
block|{
return|return
name|debugger
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Apache Camel "
operator|+
name|getVersion
argument_list|()
operator|+
literal|" starting"
argument_list|)
expr_stmt|;
block|}
DECL|method|waitUntilCompleted ()
specifier|protected
name|void
name|waitUntilCompleted
parameter_list|()
block|{
while|while
condition|(
operator|!
name|completed
operator|.
name|get
argument_list|()
condition|)
block|{
try|try
block|{
if|if
condition|(
name|duration
operator|>
literal|0
condition|)
block|{
name|TimeUnit
name|unit
init|=
name|getTimeUnit
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Waiting for: "
operator|+
name|duration
operator|+
literal|" "
operator|+
name|unit
argument_list|)
expr_stmt|;
name|latch
operator|.
name|await
argument_list|(
name|duration
argument_list|,
name|unit
argument_list|)
expr_stmt|;
name|completed
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|latch
operator|.
name|await
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Caught: "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Parses the command line arguments then runs the program      */
DECL|method|run (String[] args)
specifier|public
name|void
name|run
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|parseArguments
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|run
argument_list|()
expr_stmt|;
block|}
comment|/**      * Displays the header message for the command line options      */
DECL|method|showOptionsHeader ()
specifier|public
name|void
name|showOptionsHeader
parameter_list|()
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Apache Camel Runner takes the following options"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
DECL|method|getCamelContexts ()
specifier|public
name|List
argument_list|<
name|CamelContext
argument_list|>
name|getCamelContexts
parameter_list|()
block|{
return|return
name|camelContexts
return|;
block|}
DECL|method|getRouteBuilders ()
specifier|public
name|List
argument_list|<
name|RouteBuilder
argument_list|>
name|getRouteBuilders
parameter_list|()
block|{
return|return
name|routeBuilders
return|;
block|}
DECL|method|setRouteBuilders (List<RouteBuilder> routeBuilders)
specifier|public
name|void
name|setRouteBuilders
parameter_list|(
name|List
argument_list|<
name|RouteBuilder
argument_list|>
name|routeBuilders
parameter_list|)
block|{
name|this
operator|.
name|routeBuilders
operator|=
name|routeBuilders
expr_stmt|;
block|}
DECL|method|getRouteDefinitions ()
specifier|public
name|List
argument_list|<
name|RouteType
argument_list|>
name|getRouteDefinitions
parameter_list|()
block|{
name|List
argument_list|<
name|RouteType
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|RouteType
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|CamelContext
name|camelContext
range|:
name|camelContexts
control|)
block|{
name|answer
operator|.
name|addAll
argument_list|(
name|camelContext
operator|.
name|getRouteDefinitions
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Returns a {@link org.apache.camel.ProducerTemplate} from the Spring {@link org.springframework.context.ApplicationContext} instances      * or lazily creates a new one dynamically      */
DECL|method|getCamelTemplate ()
specifier|public
name|ProducerTemplate
name|getCamelTemplate
parameter_list|()
block|{
if|if
condition|(
name|camelTemplate
operator|==
literal|null
condition|)
block|{
name|camelTemplate
operator|=
name|findOrCreateCamelTemplate
argument_list|()
expr_stmt|;
block|}
return|return
name|camelTemplate
return|;
block|}
DECL|method|findOrCreateCamelTemplate ()
specifier|protected
specifier|abstract
name|ProducerTemplate
name|findOrCreateCamelTemplate
parameter_list|()
function_decl|;
DECL|method|getCamelContextMap ()
specifier|protected
specifier|abstract
name|Map
argument_list|<
name|String
argument_list|,
name|CamelContext
argument_list|>
name|getCamelContextMap
parameter_list|()
function_decl|;
DECL|method|postProcessContext ()
specifier|protected
name|void
name|postProcessContext
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|CamelContext
argument_list|>
name|map
init|=
name|getCamelContextMap
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|CamelContext
argument_list|>
argument_list|>
name|entries
init|=
name|map
operator|.
name|entrySet
argument_list|()
decl_stmt|;
name|int
name|size
init|=
name|entries
operator|.
name|size
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|CamelContext
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
name|String
name|name
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|CamelContext
name|camelContext
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|camelContexts
operator|.
name|add
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|generateDot
argument_list|(
name|name
argument_list|,
name|camelContext
argument_list|,
name|size
argument_list|)
expr_stmt|;
name|postProcesCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isAggregateDot
argument_list|()
condition|)
block|{
name|generateDot
argument_list|(
literal|"aggregate"
argument_list|,
name|aggregateCamelContext
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|getRoutesOutputFile
argument_list|()
argument_list|)
condition|)
block|{
name|outputRoutesToFile
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|outputRoutesToFile ()
specifier|protected
name|void
name|outputRoutesToFile
parameter_list|()
throws|throws
name|IOException
throws|,
name|JAXBException
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotNullAndNonEmpty
argument_list|(
name|getRoutesOutputFile
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Generating routes as XML in the file named: "
operator|+
name|getRoutesOutputFile
argument_list|()
argument_list|)
expr_stmt|;
name|ModelFileGenerator
name|generator
init|=
name|createModelFileGenerator
argument_list|()
decl_stmt|;
name|generator
operator|.
name|marshalRoutesUsingJaxb
argument_list|(
name|getRoutesOutputFile
argument_list|()
argument_list|,
name|getRouteDefinitions
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createModelFileGenerator ()
specifier|protected
specifier|abstract
name|ModelFileGenerator
name|createModelFileGenerator
parameter_list|()
throws|throws
name|JAXBException
function_decl|;
DECL|method|generateDot (String name, CamelContext camelContext, int size)
specifier|protected
name|void
name|generateDot
parameter_list|(
name|String
name|name
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|,
name|int
name|size
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|outputDir
init|=
name|dotOutputDir
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotNullAndNonEmpty
argument_list|(
name|outputDir
argument_list|)
condition|)
block|{
if|if
condition|(
name|size
operator|>
literal|1
condition|)
block|{
name|outputDir
operator|+=
literal|"/"
operator|+
name|name
expr_stmt|;
block|}
name|RouteDotGenerator
name|generator
init|=
operator|new
name|RouteDotGenerator
argument_list|(
name|outputDir
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Generating DOT file for routes: "
operator|+
name|outputDir
operator|+
literal|" for: "
operator|+
name|camelContext
operator|+
literal|" with name: "
operator|+
name|name
argument_list|)
expr_stmt|;
name|generator
operator|.
name|drawRoutes
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Used for aggregate dot generation, generate a single camel context containing all of the available contexts      */
DECL|method|aggregateCamelContext ()
specifier|private
name|CamelContext
name|aggregateCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|camelContexts
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|camelContexts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
else|else
block|{
name|DefaultCamelContext
name|answer
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
for|for
control|(
name|CamelContext
name|camelContext
range|:
name|camelContexts
control|)
block|{
name|answer
operator|.
name|addRouteDefinitions
argument_list|(
name|camelContext
operator|.
name|getRouteDefinitions
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
DECL|method|postProcesCamelContext (CamelContext camelContext)
specifier|protected
name|void
name|postProcesCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|RouteBuilder
name|routeBuilder
range|:
name|routeBuilders
control|)
block|{
name|camelContext
operator|.
name|addRoutes
argument_list|(
name|routeBuilder
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addRouteBuilder (RouteBuilder routeBuilder)
specifier|public
name|void
name|addRouteBuilder
parameter_list|(
name|RouteBuilder
name|routeBuilder
parameter_list|)
block|{
name|getRouteBuilders
argument_list|()
operator|.
name|add
argument_list|(
name|routeBuilder
argument_list|)
expr_stmt|;
block|}
DECL|class|Option
specifier|public
specifier|abstract
class|class
name|Option
block|{
DECL|field|abbreviation
specifier|private
name|String
name|abbreviation
decl_stmt|;
DECL|field|fullName
specifier|private
name|String
name|fullName
decl_stmt|;
DECL|field|description
specifier|private
name|String
name|description
decl_stmt|;
DECL|method|Option (String abbreviation, String fullName, String description)
specifier|protected
name|Option
parameter_list|(
name|String
name|abbreviation
parameter_list|,
name|String
name|fullName
parameter_list|,
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|abbreviation
operator|=
literal|"-"
operator|+
name|abbreviation
expr_stmt|;
name|this
operator|.
name|fullName
operator|=
literal|"-"
operator|+
name|fullName
expr_stmt|;
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
DECL|method|processOption (String arg, LinkedList<String> remainingArgs)
specifier|public
name|boolean
name|processOption
parameter_list|(
name|String
name|arg
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
block|{
if|if
condition|(
name|arg
operator|.
name|equalsIgnoreCase
argument_list|(
name|abbreviation
argument_list|)
operator|||
name|fullName
operator|.
name|startsWith
argument_list|(
name|arg
argument_list|)
condition|)
block|{
name|doProcess
argument_list|(
name|arg
argument_list|,
name|remainingArgs
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|getAbbreviation ()
specifier|public
name|String
name|getAbbreviation
parameter_list|()
block|{
return|return
name|abbreviation
return|;
block|}
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
DECL|method|getFullName ()
specifier|public
name|String
name|getFullName
parameter_list|()
block|{
return|return
name|fullName
return|;
block|}
DECL|method|getInformation ()
specifier|public
name|String
name|getInformation
parameter_list|()
block|{
return|return
literal|"  "
operator|+
name|getAbbreviation
argument_list|()
operator|+
literal|" or "
operator|+
name|getFullName
argument_list|()
operator|+
literal|" = "
operator|+
name|getDescription
argument_list|()
return|;
block|}
DECL|method|doProcess (String arg, LinkedList<String> remainingArgs)
specifier|protected
specifier|abstract
name|void
name|doProcess
parameter_list|(
name|String
name|arg
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
function_decl|;
block|}
DECL|class|ParameterOption
specifier|public
specifier|abstract
class|class
name|ParameterOption
extends|extends
name|Option
block|{
DECL|field|parameterName
specifier|private
name|String
name|parameterName
decl_stmt|;
DECL|method|ParameterOption (String abbreviation, String fullName, String description, String parameterName)
specifier|protected
name|ParameterOption
parameter_list|(
name|String
name|abbreviation
parameter_list|,
name|String
name|fullName
parameter_list|,
name|String
name|description
parameter_list|,
name|String
name|parameterName
parameter_list|)
block|{
name|super
argument_list|(
name|abbreviation
argument_list|,
name|fullName
argument_list|,
name|description
argument_list|)
expr_stmt|;
name|this
operator|.
name|parameterName
operator|=
name|parameterName
expr_stmt|;
block|}
DECL|method|doProcess (String arg, LinkedList<String> remainingArgs)
specifier|protected
name|void
name|doProcess
parameter_list|(
name|String
name|arg
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
block|{
if|if
condition|(
name|remainingArgs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Expected fileName for "
argument_list|)
expr_stmt|;
name|showOptions
argument_list|()
expr_stmt|;
name|completed
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|String
name|parameter
init|=
name|remainingArgs
operator|.
name|removeFirst
argument_list|()
decl_stmt|;
name|doProcess
argument_list|(
name|arg
argument_list|,
name|parameter
argument_list|,
name|remainingArgs
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getInformation ()
specifier|public
name|String
name|getInformation
parameter_list|()
block|{
return|return
literal|"  "
operator|+
name|getAbbreviation
argument_list|()
operator|+
literal|" or "
operator|+
name|getFullName
argument_list|()
operator|+
literal|"<"
operator|+
name|parameterName
operator|+
literal|"> = "
operator|+
name|getDescription
argument_list|()
return|;
block|}
DECL|method|doProcess (String arg, String parameter, LinkedList<String> remainingArgs)
specifier|protected
specifier|abstract
name|void
name|doProcess
parameter_list|(
name|String
name|arg
parameter_list|,
name|String
name|parameter
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|remainingArgs
parameter_list|)
function_decl|;
block|}
block|}
end_class

end_unit

