begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|CamelContextStatDump
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
comment|/**  * Command to display detailed information about a given {@link org.apache.camel.CamelContext}.  */
end_comment

begin_class
DECL|class|ContextInfoCommand
specifier|public
class|class
name|ContextInfoCommand
extends|extends
name|AbstractContextCommand
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
DECL|field|verbose
specifier|private
name|boolean
name|verbose
decl_stmt|;
comment|/**      * @param context The name of the Camel context      * @param verbose Whether to output verbose      */
DECL|method|ContextInfoCommand (String context, boolean verbose)
specifier|public
name|ContextInfoCommand
parameter_list|(
name|String
name|context
parameter_list|,
name|boolean
name|verbose
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|this
operator|.
name|verbose
operator|=
name|verbose
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
DECL|method|performContextCommand (CamelController camelController, String contextName, PrintStream out, PrintStream err)
specifier|protected
name|Object
name|performContextCommand
parameter_list|(
name|CamelController
name|camelController
parameter_list|,
name|String
name|contextName
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|row
init|=
name|camelController
operator|.
name|getCamelContextInformation
argument_list|(
name|context
argument_list|)
decl_stmt|;
if|if
condition|(
name|row
operator|==
literal|null
operator|||
name|row
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|err
operator|.
name|println
argument_list|(
literal|"Camel context "
operator|+
name|context
operator|+
literal|" not found."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
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
literal|"\u001B[1mCamel Context "
operator|+
name|context
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
literal|"\tName: "
operator|+
name|row
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
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
literal|"\tManagementName: "
operator|+
name|row
operator|.
name|get
argument_list|(
literal|"managementName"
argument_list|)
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
literal|"\tVersion: "
operator|+
name|row
operator|.
name|get
argument_list|(
literal|"version"
argument_list|)
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
literal|"\tStatus: "
operator|+
name|row
operator|.
name|get
argument_list|(
literal|"status"
argument_list|)
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
literal|"\tUptime: "
operator|+
name|row
operator|.
name|get
argument_list|(
literal|"uptime"
argument_list|)
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
literal|"\u001B[1mMiscellaneous\u001B[0m"
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
literal|"\tSuspended: "
operator|+
name|row
operator|.
name|get
argument_list|(
literal|"suspended"
argument_list|)
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
literal|"\tShutdown Timeout: "
operator|+
name|row
operator|.
name|get
argument_list|(
literal|"shutdownTimeout"
argument_list|)
operator|+
literal|" sec."
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|row
operator|.
name|get
argument_list|(
literal|"managementStatisticsLevel"
argument_list|)
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
literal|"\tManagement StatisticsLevel: "
operator|+
name|row
operator|.
name|get
argument_list|(
literal|"managementStatisticsLevel"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|(
name|stringEscape
operator|.
name|unescapeJava
argument_list|(
literal|"\tAllow UseOriginalMessage: "
operator|+
name|row
operator|.
name|get
argument_list|(
literal|"allowUseOriginalMessage"
argument_list|)
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
literal|"\tMessage History: "
operator|+
name|row
operator|.
name|get
argument_list|(
literal|"messageHistory"
argument_list|)
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
literal|"\tTracing: "
operator|+
name|row
operator|.
name|get
argument_list|(
literal|"tracing"
argument_list|)
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
literal|"\tLog Mask: "
operator|+
name|row
operator|.
name|get
argument_list|(
literal|"logMask"
argument_list|)
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
name|row
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|key
operator|.
name|startsWith
argument_list|(
literal|"property."
argument_list|)
condition|)
block|{
name|key
operator|=
name|key
operator|.
name|substring
argument_list|(
literal|9
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
literal|"\t"
operator|+
name|key
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
block|}
if|if
condition|(
name|verbose
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
literal|"\u001B[1mAdvanced\u001B[0m"
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
literal|"\tClassResolver: "
operator|+
name|row
operator|.
name|get
argument_list|(
literal|"classResolver"
argument_list|)
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
literal|"\tPackageScanClassResolver: "
operator|+
name|row
operator|.
name|get
argument_list|(
literal|"packageScanClassResolver"
argument_list|)
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
literal|"\tApplicationContextClassLoader: "
operator|+
name|row
operator|.
name|get
argument_list|(
literal|"applicationContextClassLoader"
argument_list|)
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
literal|"\tHeadersMapFactory: "
operator|+
name|row
operator|.
name|get
argument_list|(
literal|"headersMapFactory"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|printStatistics
argument_list|(
name|camelController
argument_list|,
name|out
argument_list|)
expr_stmt|;
comment|// add type converter details
name|out
operator|.
name|println
argument_list|(
name|stringEscape
operator|.
name|unescapeJava
argument_list|(
literal|"\tNumber of type converters: "
operator|+
name|row
operator|.
name|get
argument_list|(
literal|"typeConverter.numberOfTypeConverters"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|boolean
name|enabled
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|row
operator|.
name|get
argument_list|(
literal|"typeConverter.statisticsEnabled"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|enabled
operator|=
operator|(
name|boolean
operator|)
name|row
operator|.
name|get
argument_list|(
literal|"typeConverter.statisticsEnabled"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|enabled
condition|)
block|{
name|long
name|noop
init|=
operator|(
name|long
operator|)
name|row
operator|.
name|get
argument_list|(
literal|"typeConverter.noopCounter"
argument_list|)
decl_stmt|;
name|long
name|attempt
init|=
operator|(
name|long
operator|)
name|row
operator|.
name|get
argument_list|(
literal|"typeConverter.attemptCounter"
argument_list|)
decl_stmt|;
name|long
name|hit
init|=
operator|(
name|long
operator|)
name|row
operator|.
name|get
argument_list|(
literal|"typeConverter.hitCounter"
argument_list|)
decl_stmt|;
name|long
name|miss
init|=
operator|(
name|long
operator|)
name|row
operator|.
name|get
argument_list|(
literal|"typeConverter.missCounter"
argument_list|)
decl_stmt|;
name|long
name|failed
init|=
operator|(
name|long
operator|)
name|row
operator|.
name|get
argument_list|(
literal|"typeConverter.failedCounter"
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
name|String
operator|.
name|format
argument_list|(
literal|"\tType converter usage: [noop=%s, attempts=%s, hits=%s, misses=%s, failures=%s]"
argument_list|,
name|noop
argument_list|,
name|attempt
argument_list|,
name|hit
argument_list|,
name|miss
argument_list|,
name|failed
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// add async processor await details
name|out
operator|.
name|println
argument_list|(
name|stringEscape
operator|.
name|unescapeJava
argument_list|(
literal|"\tNumber of blocked threads: "
operator|+
name|row
operator|.
name|get
argument_list|(
literal|"asyncProcessorAwaitManager.size"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|enabled
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|row
operator|.
name|get
argument_list|(
literal|"asyncProcessorAwaitManager.statisticsEnabled"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|enabled
operator|=
operator|(
name|boolean
operator|)
name|row
operator|.
name|get
argument_list|(
literal|"asyncProcessorAwaitManager.statisticsEnabled"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|enabled
condition|)
block|{
name|long
name|blocked
init|=
operator|(
name|long
operator|)
name|row
operator|.
name|get
argument_list|(
literal|"asyncProcessorAwaitManager.threadsBlocked"
argument_list|)
decl_stmt|;
name|long
name|interrupted
init|=
operator|(
name|long
operator|)
name|row
operator|.
name|get
argument_list|(
literal|"asyncProcessorAwaitManager.threadsInterrupted"
argument_list|)
decl_stmt|;
name|long
name|total
init|=
operator|(
name|long
operator|)
name|row
operator|.
name|get
argument_list|(
literal|"asyncProcessorAwaitManager.totalDuration"
argument_list|)
decl_stmt|;
name|long
name|min
init|=
operator|(
name|long
operator|)
name|row
operator|.
name|get
argument_list|(
literal|"asyncProcessorAwaitManager.minDuration"
argument_list|)
decl_stmt|;
name|long
name|max
init|=
operator|(
name|long
operator|)
name|row
operator|.
name|get
argument_list|(
literal|"asyncProcessorAwaitManager.maxDuration"
argument_list|)
decl_stmt|;
name|long
name|mean
init|=
operator|(
name|long
operator|)
name|row
operator|.
name|get
argument_list|(
literal|"asyncProcessorAwaitManager.meanDuration"
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
name|String
operator|.
name|format
argument_list|(
literal|"\tAsyncProcessorAwaitManager usage: [blocked=%s, interrupted=%s, total=%s msec, min=%s msec, max=%s msec, mean=%s msec]"
argument_list|,
name|blocked
argument_list|,
name|interrupted
argument_list|,
name|total
argument_list|,
name|min
argument_list|,
name|max
argument_list|,
name|mean
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// add stream caching details if enabled
name|enabled
operator|=
operator|(
name|boolean
operator|)
name|row
operator|.
name|get
argument_list|(
literal|"streamCachingEnabled"
argument_list|)
expr_stmt|;
if|if
condition|(
name|enabled
condition|)
block|{
name|Object
name|spoolDirectory
init|=
name|safeNull
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"streamCaching.spoolDirectory"
argument_list|)
argument_list|)
decl_stmt|;
name|Object
name|spoolCipher
init|=
name|safeNull
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"streamCaching.spoolCipher"
argument_list|)
argument_list|)
decl_stmt|;
name|Object
name|spoolThreshold
init|=
name|safeNull
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"streamCaching.spoolThreshold"
argument_list|)
argument_list|)
decl_stmt|;
name|Object
name|spoolUsedHeapMemoryThreshold
init|=
name|safeNull
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"streamCaching.spoolUsedHeapMemoryThreshold"
argument_list|)
argument_list|)
decl_stmt|;
name|Object
name|spoolUsedHeapMemoryLimit
init|=
name|safeNull
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"streamCaching.spoolUsedHeapMemoryLimit"
argument_list|)
argument_list|)
decl_stmt|;
name|Object
name|anySpoolRules
init|=
name|safeNull
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"streamCaching.anySpoolRules"
argument_list|)
argument_list|)
decl_stmt|;
name|Object
name|bufferSize
init|=
name|safeNull
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"streamCaching.bufferSize"
argument_list|)
argument_list|)
decl_stmt|;
name|Object
name|removeSpoolDirectoryWhenStopping
init|=
name|safeNull
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"streamCaching.removeSpoolDirectoryWhenStopping"
argument_list|)
argument_list|)
decl_stmt|;
name|boolean
name|statisticsEnabled
init|=
operator|(
name|boolean
operator|)
name|row
operator|.
name|get
argument_list|(
literal|"streamCaching.statisticsEnabled"
argument_list|)
decl_stmt|;
name|String
name|text
init|=
name|String
operator|.
name|format
argument_list|(
literal|"\tStream caching: [spoolDirectory=%s, spoolCipher=%s, spoolThreshold=%s, spoolUsedHeapMemoryThreshold=%s, "
operator|+
literal|"spoolUsedHeapMemoryLimit=%s, anySpoolRules=%s, bufferSize=%s, removeSpoolDirectoryWhenStopping=%s, statisticsEnabled=%s]"
argument_list|,
name|spoolDirectory
argument_list|,
name|spoolCipher
argument_list|,
name|spoolThreshold
argument_list|,
name|spoolUsedHeapMemoryThreshold
argument_list|,
name|spoolUsedHeapMemoryLimit
argument_list|,
name|anySpoolRules
argument_list|,
name|bufferSize
argument_list|,
name|removeSpoolDirectoryWhenStopping
argument_list|,
name|statisticsEnabled
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
name|text
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|statisticsEnabled
condition|)
block|{
name|Object
name|cacheMemoryCounter
init|=
name|safeNull
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"streamCaching.cacheMemoryCounter"
argument_list|)
argument_list|)
decl_stmt|;
name|Object
name|cacheMemorySize
init|=
name|safeNull
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"streamCaching.cacheMemorySize"
argument_list|)
argument_list|)
decl_stmt|;
name|Object
name|cacheMemoryAverageSize
init|=
name|safeNull
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"streamCaching.cacheMemoryAverageSize"
argument_list|)
argument_list|)
decl_stmt|;
name|Object
name|cacheSpoolCounter
init|=
name|safeNull
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"streamCaching.cacheSpoolCounter"
argument_list|)
argument_list|)
decl_stmt|;
name|Object
name|cacheSpoolSize
init|=
name|safeNull
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"streamCaching.cacheSpoolSize"
argument_list|)
argument_list|)
decl_stmt|;
name|Object
name|cacheSpoolAverageSize
init|=
name|safeNull
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"streamCaching.cacheSpoolAverageSize"
argument_list|)
argument_list|)
decl_stmt|;
name|text
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"\t                       [cacheMemoryCounter=%s, cacheMemorySize=%s, cacheMemoryAverageSize=%s, cacheSpoolCounter=%s, "
operator|+
literal|"cacheSpoolSize=%s, cacheSpoolAverageSize=%s]"
argument_list|,
name|cacheMemoryCounter
argument_list|,
name|cacheMemorySize
argument_list|,
name|cacheMemoryAverageSize
argument_list|,
name|cacheSpoolCounter
argument_list|,
name|cacheSpoolSize
argument_list|,
name|cacheSpoolAverageSize
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
name|text
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|long
name|totalRoutes
init|=
operator|(
name|long
operator|)
name|row
operator|.
name|get
argument_list|(
literal|"totalRoutes"
argument_list|)
decl_stmt|;
name|long
name|startedRoutes
init|=
operator|(
name|long
operator|)
name|row
operator|.
name|get
argument_list|(
literal|"totalRoutes"
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
literal|"\tNumber of running routes: "
operator|+
name|startedRoutes
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
literal|"\tNumber of not running routes: "
operator|+
operator|(
name|totalRoutes
operator|-
name|startedRoutes
operator|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|printStatistics (CamelController camelController, PrintStream out)
specifier|protected
name|void
name|printStatistics
parameter_list|(
name|CamelController
name|camelController
parameter_list|,
name|PrintStream
name|out
parameter_list|)
throws|throws
name|Exception
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
literal|"\u001B[1mStatistics\u001B[0m"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|xml
init|=
name|camelController
operator|.
name|getCamelContextStatsAsXml
argument_list|(
name|context
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
name|CamelContextStatDump
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
name|CamelContextStatDump
name|stat
init|=
operator|(
name|CamelContextStatDump
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
name|stat
operator|.
name|getExchangesCompleted
argument_list|()
operator|+
name|stat
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
name|stat
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
name|stat
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
name|stat
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
name|stat
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
name|stat
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
name|stat
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
name|stat
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
name|stat
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
name|stat
operator|.
name|getDeltaProcessingTime
argument_list|()
operator|+
literal|" ms"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|isEmpty
argument_list|(
name|stat
operator|.
name|getStartTimestamp
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
literal|"\tStart Statistics Date:"
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
name|stat
operator|.
name|getStartTimestamp
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
literal|"\tStart Statistics Date: "
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
name|stat
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
name|stat
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
name|stat
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
name|stat
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
name|stat
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
name|stat
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
block|}
block|}
block|}
end_class

end_unit

