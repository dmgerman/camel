begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.api.management.mbean
package|package
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
name|api
operator|.
name|management
operator|.
name|ManagedAttribute
import|;
end_import

begin_interface
DECL|interface|ManagedStreamCachingStrategyMBean
specifier|public
interface|interface
name|ManagedStreamCachingStrategyMBean
block|{
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Directory used when overflow and spooling to disk"
argument_list|)
DECL|method|getSpoolDirectory ()
name|String
name|getSpoolDirectory
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Chiper used if writing with encryption"
argument_list|)
DECL|method|getSpoolChiper ()
name|String
name|getSpoolChiper
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Threshold in bytes when overflow and spooling to disk instead of keeping in memory"
argument_list|)
DECL|method|setSpoolThreshold (long threshold)
name|void
name|setSpoolThreshold
parameter_list|(
name|long
name|threshold
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Threshold in bytes when overflow and spooling to disk instead of keeping in memory"
argument_list|)
DECL|method|getSpoolThreshold ()
name|long
name|getSpoolThreshold
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Buffer size in bytes to use when coping between buffers"
argument_list|)
DECL|method|setBufferSize (int bufferSize)
name|void
name|setBufferSize
parameter_list|(
name|int
name|bufferSize
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Buffer size in bytes to use when coping between buffers"
argument_list|)
DECL|method|getBufferSize ()
name|int
name|getBufferSize
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether to remove spool directory when stopping"
argument_list|)
DECL|method|setRemoveSpoolDirectoryWhenStopping (boolean remove)
name|void
name|setRemoveSpoolDirectoryWhenStopping
parameter_list|(
name|boolean
name|remove
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether to remove spool directory when stopping"
argument_list|)
DECL|method|isRemoveSpoolDirectoryWhenStopping ()
name|boolean
name|isRemoveSpoolDirectoryWhenStopping
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

