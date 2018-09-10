begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|management
operator|.
name|ManagementFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|management
operator|.
name|MemoryMXBean
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|UUID
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
name|CamelContextAware
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
name|Exchange
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
name|Message
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
name|StreamCache
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
name|StreamCachingStrategy
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
name|FilePathResolver
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
name|FileUtil
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
name|IOHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Default implementation of {@link StreamCachingStrategy}  */
end_comment

begin_class
DECL|class|DefaultStreamCachingStrategy
specifier|public
class|class
name|DefaultStreamCachingStrategy
extends|extends
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|ServiceSupport
implements|implements
name|CamelContextAware
implements|,
name|StreamCachingStrategy
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DefaultStreamCachingStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|enabled
specifier|private
name|boolean
name|enabled
decl_stmt|;
DECL|field|spoolDirectory
specifier|private
name|File
name|spoolDirectory
decl_stmt|;
DECL|field|spoolDirectoryName
specifier|private
specifier|transient
name|String
name|spoolDirectoryName
init|=
literal|"${java.io.tmpdir}/camel/camel-tmp-#uuid#"
decl_stmt|;
DECL|field|spoolThreshold
specifier|private
name|long
name|spoolThreshold
init|=
name|StreamCache
operator|.
name|DEFAULT_SPOOL_THRESHOLD
decl_stmt|;
DECL|field|spoolUsedHeapMemoryThreshold
specifier|private
name|int
name|spoolUsedHeapMemoryThreshold
decl_stmt|;
DECL|field|spoolUsedHeapMemoryLimit
specifier|private
name|SpoolUsedHeapMemoryLimit
name|spoolUsedHeapMemoryLimit
decl_stmt|;
DECL|field|spoolChiper
specifier|private
name|String
name|spoolChiper
decl_stmt|;
DECL|field|bufferSize
specifier|private
name|int
name|bufferSize
init|=
name|IOHelper
operator|.
name|DEFAULT_BUFFER_SIZE
decl_stmt|;
DECL|field|removeSpoolDirectoryWhenStopping
specifier|private
name|boolean
name|removeSpoolDirectoryWhenStopping
init|=
literal|true
decl_stmt|;
DECL|field|statistics
specifier|private
specifier|final
name|UtilizationStatistics
name|statistics
init|=
operator|new
name|UtilizationStatistics
argument_list|()
decl_stmt|;
DECL|field|spoolRules
specifier|private
specifier|final
name|Set
argument_list|<
name|SpoolRule
argument_list|>
name|spoolRules
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|anySpoolRules
specifier|private
name|boolean
name|anySpoolRules
decl_stmt|;
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|isEnabled ()
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
name|enabled
return|;
block|}
DECL|method|setEnabled (boolean enabled)
specifier|public
name|void
name|setEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
name|this
operator|.
name|enabled
operator|=
name|enabled
expr_stmt|;
block|}
DECL|method|setSpoolDirectory (String path)
specifier|public
name|void
name|setSpoolDirectory
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|spoolDirectoryName
operator|=
name|path
expr_stmt|;
block|}
DECL|method|setSpoolDirectory (File path)
specifier|public
name|void
name|setSpoolDirectory
parameter_list|(
name|File
name|path
parameter_list|)
block|{
name|this
operator|.
name|spoolDirectory
operator|=
name|path
expr_stmt|;
block|}
DECL|method|getSpoolDirectory ()
specifier|public
name|File
name|getSpoolDirectory
parameter_list|()
block|{
return|return
name|spoolDirectory
return|;
block|}
DECL|method|getSpoolThreshold ()
specifier|public
name|long
name|getSpoolThreshold
parameter_list|()
block|{
return|return
name|spoolThreshold
return|;
block|}
DECL|method|getSpoolUsedHeapMemoryThreshold ()
specifier|public
name|int
name|getSpoolUsedHeapMemoryThreshold
parameter_list|()
block|{
return|return
name|spoolUsedHeapMemoryThreshold
return|;
block|}
DECL|method|setSpoolUsedHeapMemoryThreshold (int spoolHeapMemoryWatermarkThreshold)
specifier|public
name|void
name|setSpoolUsedHeapMemoryThreshold
parameter_list|(
name|int
name|spoolHeapMemoryWatermarkThreshold
parameter_list|)
block|{
name|this
operator|.
name|spoolUsedHeapMemoryThreshold
operator|=
name|spoolHeapMemoryWatermarkThreshold
expr_stmt|;
block|}
DECL|method|getSpoolUsedHeapMemoryLimit ()
specifier|public
name|SpoolUsedHeapMemoryLimit
name|getSpoolUsedHeapMemoryLimit
parameter_list|()
block|{
return|return
name|spoolUsedHeapMemoryLimit
return|;
block|}
DECL|method|setSpoolUsedHeapMemoryLimit (SpoolUsedHeapMemoryLimit spoolUsedHeapMemoryLimit)
specifier|public
name|void
name|setSpoolUsedHeapMemoryLimit
parameter_list|(
name|SpoolUsedHeapMemoryLimit
name|spoolUsedHeapMemoryLimit
parameter_list|)
block|{
name|this
operator|.
name|spoolUsedHeapMemoryLimit
operator|=
name|spoolUsedHeapMemoryLimit
expr_stmt|;
block|}
DECL|method|setSpoolThreshold (long spoolThreshold)
specifier|public
name|void
name|setSpoolThreshold
parameter_list|(
name|long
name|spoolThreshold
parameter_list|)
block|{
name|this
operator|.
name|spoolThreshold
operator|=
name|spoolThreshold
expr_stmt|;
block|}
DECL|method|getSpoolChiper ()
specifier|public
name|String
name|getSpoolChiper
parameter_list|()
block|{
return|return
name|spoolChiper
return|;
block|}
DECL|method|setSpoolChiper (String spoolChiper)
specifier|public
name|void
name|setSpoolChiper
parameter_list|(
name|String
name|spoolChiper
parameter_list|)
block|{
name|this
operator|.
name|spoolChiper
operator|=
name|spoolChiper
expr_stmt|;
block|}
DECL|method|getBufferSize ()
specifier|public
name|int
name|getBufferSize
parameter_list|()
block|{
return|return
name|bufferSize
return|;
block|}
DECL|method|setBufferSize (int bufferSize)
specifier|public
name|void
name|setBufferSize
parameter_list|(
name|int
name|bufferSize
parameter_list|)
block|{
name|this
operator|.
name|bufferSize
operator|=
name|bufferSize
expr_stmt|;
block|}
DECL|method|isRemoveSpoolDirectoryWhenStopping ()
specifier|public
name|boolean
name|isRemoveSpoolDirectoryWhenStopping
parameter_list|()
block|{
return|return
name|removeSpoolDirectoryWhenStopping
return|;
block|}
DECL|method|setRemoveSpoolDirectoryWhenStopping (boolean removeSpoolDirectoryWhenStopping)
specifier|public
name|void
name|setRemoveSpoolDirectoryWhenStopping
parameter_list|(
name|boolean
name|removeSpoolDirectoryWhenStopping
parameter_list|)
block|{
name|this
operator|.
name|removeSpoolDirectoryWhenStopping
operator|=
name|removeSpoolDirectoryWhenStopping
expr_stmt|;
block|}
DECL|method|isAnySpoolRules ()
specifier|public
name|boolean
name|isAnySpoolRules
parameter_list|()
block|{
return|return
name|anySpoolRules
return|;
block|}
DECL|method|setAnySpoolRules (boolean anySpoolTasks)
specifier|public
name|void
name|setAnySpoolRules
parameter_list|(
name|boolean
name|anySpoolTasks
parameter_list|)
block|{
name|this
operator|.
name|anySpoolRules
operator|=
name|anySpoolTasks
expr_stmt|;
block|}
DECL|method|getStatistics ()
specifier|public
name|Statistics
name|getStatistics
parameter_list|()
block|{
return|return
name|statistics
return|;
block|}
DECL|method|shouldSpoolCache (long length)
specifier|public
name|boolean
name|shouldSpoolCache
parameter_list|(
name|long
name|length
parameter_list|)
block|{
if|if
condition|(
operator|!
name|enabled
operator|||
name|spoolRules
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|boolean
name|all
init|=
literal|true
decl_stmt|;
name|boolean
name|any
init|=
literal|false
decl_stmt|;
for|for
control|(
name|SpoolRule
name|rule
range|:
name|spoolRules
control|)
block|{
name|boolean
name|result
init|=
name|rule
operator|.
name|shouldSpoolCache
argument_list|(
name|length
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|result
condition|)
block|{
name|all
operator|=
literal|false
expr_stmt|;
if|if
condition|(
operator|!
name|anySpoolRules
condition|)
block|{
comment|// no need to check anymore
break|break;
block|}
block|}
else|else
block|{
name|any
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|anySpoolRules
condition|)
block|{
comment|// no need to check anymore
break|break;
block|}
block|}
block|}
name|boolean
name|answer
init|=
name|anySpoolRules
condition|?
name|any
else|:
name|all
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Should spool cache {} -> {}"
argument_list|,
name|length
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|addSpoolRule (SpoolRule rule)
specifier|public
name|void
name|addSpoolRule
parameter_list|(
name|SpoolRule
name|rule
parameter_list|)
block|{
name|spoolRules
operator|.
name|add
argument_list|(
name|rule
argument_list|)
expr_stmt|;
block|}
DECL|method|cache (Exchange exchange)
specifier|public
name|StreamCache
name|cache
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Message
name|message
init|=
name|exchange
operator|.
name|hasOut
argument_list|()
condition|?
name|exchange
operator|.
name|getOut
argument_list|()
else|:
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|StreamCache
name|cache
init|=
name|message
operator|.
name|getBody
argument_list|(
name|StreamCache
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|cache
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Cached stream to {} -> {}"
argument_list|,
name|cache
operator|.
name|inMemory
argument_list|()
condition|?
literal|"memory"
else|:
literal|"spool"
argument_list|,
name|cache
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|statistics
operator|.
name|isStatisticsEnabled
argument_list|()
condition|)
block|{
try|try
block|{
if|if
condition|(
name|cache
operator|.
name|inMemory
argument_list|()
condition|)
block|{
name|statistics
operator|.
name|updateMemory
argument_list|(
name|cache
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|statistics
operator|.
name|updateSpool
argument_list|(
name|cache
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Error updating cache statistics. This exception is ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|cache
return|;
block|}
DECL|method|resolveSpoolDirectory (String path)
specifier|protected
name|String
name|resolveSpoolDirectory
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|String
name|name
init|=
name|camelContext
operator|.
name|getManagementNameStrategy
argument_list|()
operator|.
name|resolveManagementName
argument_list|(
name|path
argument_list|,
name|camelContext
operator|.
name|getName
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|name
operator|=
name|customResolveManagementName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
comment|// and then check again with invalid check to ensure all ## is resolved
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|name
operator|=
name|camelContext
operator|.
name|getManagementNameStrategy
argument_list|()
operator|.
name|resolveManagementName
argument_list|(
name|name
argument_list|,
name|camelContext
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
name|name
return|;
block|}
DECL|method|customResolveManagementName (String pattern)
specifier|protected
name|String
name|customResolveManagementName
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
if|if
condition|(
name|pattern
operator|.
name|contains
argument_list|(
literal|"#uuid#"
argument_list|)
condition|)
block|{
name|String
name|uuid
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|pattern
operator|=
name|pattern
operator|.
name|replaceFirst
argument_list|(
literal|"#uuid#"
argument_list|,
name|uuid
argument_list|)
expr_stmt|;
block|}
return|return
name|FilePathResolver
operator|.
name|resolvePath
argument_list|(
name|pattern
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|enabled
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"StreamCaching is not enabled"
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|spoolUsedHeapMemoryThreshold
operator|>
literal|99
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"SpoolHeapMemoryWatermarkThreshold must not be higher than 99, was: "
operator|+
name|spoolUsedHeapMemoryThreshold
argument_list|)
throw|;
block|}
comment|// if we can overflow to disk then make sure directory exists / is created
if|if
condition|(
name|spoolThreshold
operator|>
literal|0
operator|||
name|spoolUsedHeapMemoryThreshold
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|spoolDirectory
operator|==
literal|null
operator|&&
name|spoolDirectoryName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"SpoolDirectory must be configured when using SpoolThreshold> 0"
argument_list|)
throw|;
block|}
if|if
condition|(
name|spoolDirectory
operator|==
literal|null
condition|)
block|{
name|String
name|name
init|=
name|resolveSpoolDirectory
argument_list|(
name|spoolDirectoryName
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|spoolDirectory
operator|=
operator|new
name|File
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|spoolDirectoryName
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot resolve spool directory from pattern: "
operator|+
name|spoolDirectoryName
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|spoolDirectory
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
name|spoolDirectory
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using spool directory: {}"
argument_list|,
name|spoolDirectory
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Spool directory: {} is not a directory. This may cause problems spooling to disk for the stream caching!"
argument_list|,
name|spoolDirectory
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|boolean
name|created
init|=
name|spoolDirectory
operator|.
name|mkdirs
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|created
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Cannot create spool directory: {}. This may cause problems spooling to disk for the stream caching!"
argument_list|,
name|spoolDirectory
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Created spool directory: {}"
argument_list|,
name|spoolDirectory
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|spoolThreshold
operator|>
literal|0
condition|)
block|{
name|spoolRules
operator|.
name|add
argument_list|(
operator|new
name|FixedThresholdSpoolRule
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|spoolUsedHeapMemoryThreshold
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|spoolUsedHeapMemoryLimit
operator|==
literal|null
condition|)
block|{
comment|// use max by default
name|spoolUsedHeapMemoryLimit
operator|=
name|SpoolUsedHeapMemoryLimit
operator|.
name|Max
expr_stmt|;
block|}
name|spoolRules
operator|.
name|add
argument_list|(
operator|new
name|UsedHeapMemorySpoolRule
argument_list|(
name|spoolUsedHeapMemoryLimit
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"StreamCaching configuration {}"
argument_list|,
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|spoolDirectory
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"StreamCaching in use with spool directory: {} and rules: {}"
argument_list|,
name|spoolDirectory
operator|.
name|getPath
argument_list|()
argument_list|,
name|spoolRules
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"StreamCaching in use with rules: {}"
argument_list|,
name|spoolRules
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|spoolThreshold
operator|>
literal|0
operator|&
name|spoolDirectory
operator|!=
literal|null
operator|&&
name|isRemoveSpoolDirectoryWhenStopping
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Removing spool directory: {}"
argument_list|,
name|spoolDirectory
argument_list|)
expr_stmt|;
name|FileUtil
operator|.
name|removeDir
argument_list|(
name|spoolDirectory
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
operator|&&
name|statistics
operator|.
name|isStatisticsEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Stopping StreamCachingStrategy with statistics: {}"
argument_list|,
name|statistics
argument_list|)
expr_stmt|;
block|}
name|statistics
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"DefaultStreamCachingStrategy["
operator|+
literal|"spoolDirectory="
operator|+
name|spoolDirectory
operator|+
literal|", spoolChiper="
operator|+
name|spoolChiper
operator|+
literal|", spoolThreshold="
operator|+
name|spoolThreshold
operator|+
literal|", spoolUsedHeapMemoryThreshold="
operator|+
name|spoolUsedHeapMemoryThreshold
operator|+
literal|", bufferSize="
operator|+
name|bufferSize
operator|+
literal|", anySpoolRules="
operator|+
name|anySpoolRules
operator|+
literal|"]"
return|;
block|}
DECL|class|FixedThresholdSpoolRule
specifier|private
specifier|final
class|class
name|FixedThresholdSpoolRule
implements|implements
name|SpoolRule
block|{
DECL|method|shouldSpoolCache (long length)
specifier|public
name|boolean
name|shouldSpoolCache
parameter_list|(
name|long
name|length
parameter_list|)
block|{
if|if
condition|(
name|spoolThreshold
operator|>
literal|0
operator|&&
name|length
operator|>
name|spoolThreshold
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Should spool cache fixed threshold {}> {} -> true"
argument_list|,
name|length
argument_list|,
name|spoolThreshold
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
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|spoolThreshold
operator|<
literal|1024
condition|)
block|{
return|return
literal|"Spool> "
operator|+
name|spoolThreshold
operator|+
literal|" bytes body size"
return|;
block|}
else|else
block|{
return|return
literal|"Spool> "
operator|+
operator|(
name|spoolThreshold
operator|>>
literal|10
operator|)
operator|+
literal|"K body size"
return|;
block|}
block|}
block|}
DECL|class|UsedHeapMemorySpoolRule
specifier|private
specifier|final
class|class
name|UsedHeapMemorySpoolRule
implements|implements
name|SpoolRule
block|{
DECL|field|heapUsage
specifier|private
specifier|final
name|MemoryMXBean
name|heapUsage
decl_stmt|;
DECL|field|limit
specifier|private
specifier|final
name|SpoolUsedHeapMemoryLimit
name|limit
decl_stmt|;
DECL|method|UsedHeapMemorySpoolRule (SpoolUsedHeapMemoryLimit limit)
specifier|private
name|UsedHeapMemorySpoolRule
parameter_list|(
name|SpoolUsedHeapMemoryLimit
name|limit
parameter_list|)
block|{
name|this
operator|.
name|limit
operator|=
name|limit
expr_stmt|;
name|this
operator|.
name|heapUsage
operator|=
name|ManagementFactory
operator|.
name|getMemoryMXBean
argument_list|()
expr_stmt|;
block|}
DECL|method|shouldSpoolCache (long length)
specifier|public
name|boolean
name|shouldSpoolCache
parameter_list|(
name|long
name|length
parameter_list|)
block|{
if|if
condition|(
name|spoolUsedHeapMemoryThreshold
operator|>
literal|0
condition|)
block|{
comment|// must use double to calculate with decimals for the percentage
name|double
name|used
init|=
name|heapUsage
operator|.
name|getHeapMemoryUsage
argument_list|()
operator|.
name|getUsed
argument_list|()
decl_stmt|;
name|double
name|upper
init|=
name|limit
operator|==
name|SpoolUsedHeapMemoryLimit
operator|.
name|Committed
condition|?
name|heapUsage
operator|.
name|getHeapMemoryUsage
argument_list|()
operator|.
name|getCommitted
argument_list|()
else|:
name|heapUsage
operator|.
name|getHeapMemoryUsage
argument_list|()
operator|.
name|getMax
argument_list|()
decl_stmt|;
name|double
name|calc
init|=
operator|(
name|used
operator|/
name|upper
operator|)
operator|*
literal|100
decl_stmt|;
name|int
name|percentage
init|=
operator|(
name|int
operator|)
name|calc
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|long
name|u
init|=
name|heapUsage
operator|.
name|getHeapMemoryUsage
argument_list|()
operator|.
name|getUsed
argument_list|()
decl_stmt|;
name|long
name|c
init|=
name|heapUsage
operator|.
name|getHeapMemoryUsage
argument_list|()
operator|.
name|getCommitted
argument_list|()
decl_stmt|;
name|long
name|m
init|=
name|heapUsage
operator|.
name|getHeapMemoryUsage
argument_list|()
operator|.
name|getMax
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Heap memory: [used={}M ({}%), committed={}M, max={}M]"
argument_list|,
name|u
operator|>>
literal|20
argument_list|,
name|percentage
argument_list|,
name|c
operator|>>
literal|20
argument_list|,
name|m
operator|>>
literal|20
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|percentage
operator|>
name|spoolUsedHeapMemoryThreshold
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Should spool cache heap memory threshold {}> {} -> true"
argument_list|,
name|percentage
argument_list|,
name|spoolUsedHeapMemoryThreshold
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Spool> "
operator|+
name|spoolUsedHeapMemoryThreshold
operator|+
literal|"% used of "
operator|+
name|limit
operator|+
literal|" heap memory"
return|;
block|}
block|}
comment|/**      * Represents utilization statistics.      */
DECL|class|UtilizationStatistics
specifier|private
specifier|static
specifier|final
class|class
name|UtilizationStatistics
implements|implements
name|Statistics
block|{
DECL|field|statisticsEnabled
specifier|private
name|boolean
name|statisticsEnabled
decl_stmt|;
DECL|field|memoryCounter
specifier|private
specifier|volatile
name|long
name|memoryCounter
decl_stmt|;
DECL|field|memorySize
specifier|private
specifier|volatile
name|long
name|memorySize
decl_stmt|;
DECL|field|memoryAverageSize
specifier|private
specifier|volatile
name|long
name|memoryAverageSize
decl_stmt|;
DECL|field|spoolCounter
specifier|private
specifier|volatile
name|long
name|spoolCounter
decl_stmt|;
DECL|field|spoolSize
specifier|private
specifier|volatile
name|long
name|spoolSize
decl_stmt|;
DECL|field|spoolAverageSize
specifier|private
specifier|volatile
name|long
name|spoolAverageSize
decl_stmt|;
DECL|method|updateMemory (long size)
specifier|synchronized
name|void
name|updateMemory
parameter_list|(
name|long
name|size
parameter_list|)
block|{
name|memoryCounter
operator|++
expr_stmt|;
name|memorySize
operator|+=
name|size
expr_stmt|;
name|memoryAverageSize
operator|=
name|memorySize
operator|/
name|memoryCounter
expr_stmt|;
block|}
DECL|method|updateSpool (long size)
specifier|synchronized
name|void
name|updateSpool
parameter_list|(
name|long
name|size
parameter_list|)
block|{
name|spoolCounter
operator|++
expr_stmt|;
name|spoolSize
operator|+=
name|size
expr_stmt|;
name|spoolAverageSize
operator|=
name|spoolSize
operator|/
name|spoolCounter
expr_stmt|;
block|}
DECL|method|getCacheMemoryCounter ()
specifier|public
name|long
name|getCacheMemoryCounter
parameter_list|()
block|{
return|return
name|memoryCounter
return|;
block|}
DECL|method|getCacheMemorySize ()
specifier|public
name|long
name|getCacheMemorySize
parameter_list|()
block|{
return|return
name|memorySize
return|;
block|}
DECL|method|getCacheMemoryAverageSize ()
specifier|public
name|long
name|getCacheMemoryAverageSize
parameter_list|()
block|{
return|return
name|memoryAverageSize
return|;
block|}
DECL|method|getCacheSpoolCounter ()
specifier|public
name|long
name|getCacheSpoolCounter
parameter_list|()
block|{
return|return
name|spoolCounter
return|;
block|}
DECL|method|getCacheSpoolSize ()
specifier|public
name|long
name|getCacheSpoolSize
parameter_list|()
block|{
return|return
name|spoolSize
return|;
block|}
DECL|method|getCacheSpoolAverageSize ()
specifier|public
name|long
name|getCacheSpoolAverageSize
parameter_list|()
block|{
return|return
name|spoolAverageSize
return|;
block|}
DECL|method|reset ()
specifier|public
specifier|synchronized
name|void
name|reset
parameter_list|()
block|{
name|memoryCounter
operator|=
literal|0
expr_stmt|;
name|memorySize
operator|=
literal|0
expr_stmt|;
name|memoryAverageSize
operator|=
literal|0
expr_stmt|;
name|spoolCounter
operator|=
literal|0
expr_stmt|;
name|spoolSize
operator|=
literal|0
expr_stmt|;
name|spoolAverageSize
operator|=
literal|0
expr_stmt|;
block|}
DECL|method|isStatisticsEnabled ()
specifier|public
name|boolean
name|isStatisticsEnabled
parameter_list|()
block|{
return|return
name|statisticsEnabled
return|;
block|}
DECL|method|setStatisticsEnabled (boolean statisticsEnabled)
specifier|public
name|void
name|setStatisticsEnabled
parameter_list|(
name|boolean
name|statisticsEnabled
parameter_list|)
block|{
name|this
operator|.
name|statisticsEnabled
operator|=
name|statisticsEnabled
expr_stmt|;
block|}
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"[memoryCounter=%s, memorySize=%s, memoryAverageSize=%s, spoolCounter=%s, spoolSize=%s, spoolAverageSize=%s]"
argument_list|,
name|memoryCounter
argument_list|,
name|memorySize
argument_list|,
name|memoryAverageSize
argument_list|,
name|spoolCounter
argument_list|,
name|spoolSize
argument_list|,
name|spoolAverageSize
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

