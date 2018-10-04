begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
operator|.
name|mbean
package|;
end_package

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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|ManagedStreamCachingStrategyMBean
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

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed StreamCachingStrategy"
argument_list|)
DECL|class|ManagedStreamCachingStrategy
specifier|public
class|class
name|ManagedStreamCachingStrategy
extends|extends
name|ManagedService
implements|implements
name|ManagedStreamCachingStrategyMBean
block|{
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|streamCachingStrategy
specifier|private
specifier|final
name|StreamCachingStrategy
name|streamCachingStrategy
decl_stmt|;
DECL|method|ManagedStreamCachingStrategy (CamelContext camelContext, StreamCachingStrategy streamCachingStrategy)
specifier|public
name|ManagedStreamCachingStrategy
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|StreamCachingStrategy
name|streamCachingStrategy
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|,
name|streamCachingStrategy
argument_list|)
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|streamCachingStrategy
operator|=
name|streamCachingStrategy
expr_stmt|;
block|}
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
DECL|method|getStreamCachingStrategy ()
specifier|public
name|StreamCachingStrategy
name|getStreamCachingStrategy
parameter_list|()
block|{
return|return
name|streamCachingStrategy
return|;
block|}
DECL|method|isEnabled ()
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
name|streamCachingStrategy
operator|.
name|isEnabled
argument_list|()
return|;
block|}
DECL|method|getSpoolDirectory ()
specifier|public
name|String
name|getSpoolDirectory
parameter_list|()
block|{
return|return
name|streamCachingStrategy
operator|.
name|getSpoolDirectory
argument_list|()
operator|.
name|getPath
argument_list|()
return|;
block|}
DECL|method|getSpoolChiper ()
specifier|public
name|String
name|getSpoolChiper
parameter_list|()
block|{
return|return
name|streamCachingStrategy
operator|.
name|getSpoolChiper
argument_list|()
return|;
block|}
DECL|method|setSpoolThreshold (long threshold)
specifier|public
name|void
name|setSpoolThreshold
parameter_list|(
name|long
name|threshold
parameter_list|)
block|{
name|streamCachingStrategy
operator|.
name|setSpoolThreshold
argument_list|(
name|threshold
argument_list|)
expr_stmt|;
block|}
DECL|method|getSpoolThreshold ()
specifier|public
name|long
name|getSpoolThreshold
parameter_list|()
block|{
return|return
name|streamCachingStrategy
operator|.
name|getSpoolThreshold
argument_list|()
return|;
block|}
DECL|method|setSpoolUsedHeapMemoryThreshold (int percentage)
specifier|public
name|void
name|setSpoolUsedHeapMemoryThreshold
parameter_list|(
name|int
name|percentage
parameter_list|)
block|{
name|streamCachingStrategy
operator|.
name|setSpoolUsedHeapMemoryThreshold
argument_list|(
name|percentage
argument_list|)
expr_stmt|;
block|}
DECL|method|getSpoolUsedHeapMemoryThreshold ()
specifier|public
name|int
name|getSpoolUsedHeapMemoryThreshold
parameter_list|()
block|{
return|return
name|streamCachingStrategy
operator|.
name|getSpoolUsedHeapMemoryThreshold
argument_list|()
return|;
block|}
DECL|method|setSpoolUsedHeapMemoryLimit (SpoolUsedHeapMemoryLimit limit)
specifier|public
name|void
name|setSpoolUsedHeapMemoryLimit
parameter_list|(
name|SpoolUsedHeapMemoryLimit
name|limit
parameter_list|)
block|{
name|StreamCachingStrategy
operator|.
name|SpoolUsedHeapMemoryLimit
name|l
decl_stmt|;
if|if
condition|(
name|limit
operator|==
literal|null
condition|)
block|{
name|l
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
switch|switch
condition|(
name|limit
condition|)
block|{
case|case
name|Committed
case|:
name|l
operator|=
name|StreamCachingStrategy
operator|.
name|SpoolUsedHeapMemoryLimit
operator|.
name|Committed
expr_stmt|;
break|break;
case|case
name|Max
case|:
name|l
operator|=
name|StreamCachingStrategy
operator|.
name|SpoolUsedHeapMemoryLimit
operator|.
name|Max
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalStateException
argument_list|()
throw|;
block|}
block|}
name|streamCachingStrategy
operator|.
name|setSpoolUsedHeapMemoryLimit
argument_list|(
name|l
argument_list|)
expr_stmt|;
block|}
DECL|method|getSpoolUsedHeapMemoryLimit ()
specifier|public
name|SpoolUsedHeapMemoryLimit
name|getSpoolUsedHeapMemoryLimit
parameter_list|()
block|{
name|StreamCachingStrategy
operator|.
name|SpoolUsedHeapMemoryLimit
name|l
init|=
name|streamCachingStrategy
operator|.
name|getSpoolUsedHeapMemoryLimit
argument_list|()
decl_stmt|;
if|if
condition|(
name|l
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
switch|switch
condition|(
name|l
condition|)
block|{
case|case
name|Committed
case|:
return|return
name|SpoolUsedHeapMemoryLimit
operator|.
name|Committed
return|;
case|case
name|Max
case|:
return|return
name|SpoolUsedHeapMemoryLimit
operator|.
name|Max
return|;
default|default:
throw|throw
operator|new
name|IllegalStateException
argument_list|()
throw|;
block|}
block|}
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
name|streamCachingStrategy
operator|.
name|setBufferSize
argument_list|(
name|bufferSize
argument_list|)
expr_stmt|;
block|}
DECL|method|getBufferSize ()
specifier|public
name|int
name|getBufferSize
parameter_list|()
block|{
return|return
name|streamCachingStrategy
operator|.
name|getBufferSize
argument_list|()
return|;
block|}
DECL|method|setRemoveSpoolDirectoryWhenStopping (boolean remove)
specifier|public
name|void
name|setRemoveSpoolDirectoryWhenStopping
parameter_list|(
name|boolean
name|remove
parameter_list|)
block|{
name|streamCachingStrategy
operator|.
name|setRemoveSpoolDirectoryWhenStopping
argument_list|(
name|remove
argument_list|)
expr_stmt|;
block|}
DECL|method|isRemoveSpoolDirectoryWhenStopping ()
specifier|public
name|boolean
name|isRemoveSpoolDirectoryWhenStopping
parameter_list|()
block|{
return|return
name|streamCachingStrategy
operator|.
name|isRemoveSpoolDirectoryWhenStopping
argument_list|()
return|;
block|}
DECL|method|setAnySpoolRules (boolean any)
specifier|public
name|void
name|setAnySpoolRules
parameter_list|(
name|boolean
name|any
parameter_list|)
block|{
name|streamCachingStrategy
operator|.
name|setAnySpoolRules
argument_list|(
name|any
argument_list|)
expr_stmt|;
block|}
DECL|method|isAnySpoolRules ()
specifier|public
name|boolean
name|isAnySpoolRules
parameter_list|()
block|{
return|return
name|streamCachingStrategy
operator|.
name|isAnySpoolRules
argument_list|()
return|;
block|}
DECL|method|getCacheMemoryCounter ()
specifier|public
name|long
name|getCacheMemoryCounter
parameter_list|()
block|{
return|return
name|streamCachingStrategy
operator|.
name|getStatistics
argument_list|()
operator|.
name|getCacheMemoryCounter
argument_list|()
return|;
block|}
DECL|method|getCacheMemorySize ()
specifier|public
name|long
name|getCacheMemorySize
parameter_list|()
block|{
return|return
name|streamCachingStrategy
operator|.
name|getStatistics
argument_list|()
operator|.
name|getCacheMemorySize
argument_list|()
return|;
block|}
DECL|method|getCacheMemoryAverageSize ()
specifier|public
name|long
name|getCacheMemoryAverageSize
parameter_list|()
block|{
return|return
name|streamCachingStrategy
operator|.
name|getStatistics
argument_list|()
operator|.
name|getCacheMemoryAverageSize
argument_list|()
return|;
block|}
DECL|method|getCacheSpoolCounter ()
specifier|public
name|long
name|getCacheSpoolCounter
parameter_list|()
block|{
return|return
name|streamCachingStrategy
operator|.
name|getStatistics
argument_list|()
operator|.
name|getCacheSpoolCounter
argument_list|()
return|;
block|}
DECL|method|getCacheSpoolSize ()
specifier|public
name|long
name|getCacheSpoolSize
parameter_list|()
block|{
return|return
name|streamCachingStrategy
operator|.
name|getStatistics
argument_list|()
operator|.
name|getCacheSpoolSize
argument_list|()
return|;
block|}
DECL|method|getCacheSpoolAverageSize ()
specifier|public
name|long
name|getCacheSpoolAverageSize
parameter_list|()
block|{
return|return
name|streamCachingStrategy
operator|.
name|getStatistics
argument_list|()
operator|.
name|getCacheSpoolAverageSize
argument_list|()
return|;
block|}
DECL|method|isStatisticsEnabled ()
specifier|public
name|boolean
name|isStatisticsEnabled
parameter_list|()
block|{
return|return
name|streamCachingStrategy
operator|.
name|getStatistics
argument_list|()
operator|.
name|isStatisticsEnabled
argument_list|()
return|;
block|}
DECL|method|setStatisticsEnabled (boolean enabled)
specifier|public
name|void
name|setStatisticsEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
name|streamCachingStrategy
operator|.
name|getStatistics
argument_list|()
operator|.
name|setStatisticsEnabled
argument_list|(
name|enabled
argument_list|)
expr_stmt|;
block|}
DECL|method|resetStatistics ()
specifier|public
name|void
name|resetStatistics
parameter_list|()
block|{
name|streamCachingStrategy
operator|.
name|getStatistics
argument_list|()
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

