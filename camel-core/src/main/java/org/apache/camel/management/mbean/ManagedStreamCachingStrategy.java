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
name|ManagementStrategy
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
DECL|method|init (ManagementStrategy strategy)
specifier|public
name|void
name|init
parameter_list|(
name|ManagementStrategy
name|strategy
parameter_list|)
block|{
comment|// do nothing
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
block|}
end_class

end_unit

