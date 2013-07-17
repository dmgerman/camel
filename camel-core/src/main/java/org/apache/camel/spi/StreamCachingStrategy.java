begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
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

begin_comment
comment|/**  * Strategy for using<a href="http://camel.apache.org/stream-caching.html">stream caching</a>.  */
end_comment

begin_interface
DECL|interface|StreamCachingStrategy
specifier|public
interface|interface
name|StreamCachingStrategy
block|{
comment|/**      * Sets the temporary directory to use for overflow and spooling to disk.      *<p/>      * If no temporary directory has been explicit configured, then a directory      * is created in the<tt>java.io.tmpdir</tt> directory.      */
DECL|method|setTemporaryDirectory (File path)
name|void
name|setTemporaryDirectory
parameter_list|(
name|File
name|path
parameter_list|)
function_decl|;
DECL|method|getTemporaryDirectory ()
name|File
name|getTemporaryDirectory
parameter_list|()
function_decl|;
DECL|method|setTemporaryDirectory (String path)
name|void
name|setTemporaryDirectory
parameter_list|(
name|String
name|path
parameter_list|)
function_decl|;
comment|/**      * Threshold in bytes when overflow to disk is activated.      *<p/>      * The default threshold is {@link org.apache.camel.StreamCache#DEFAULT_SPOOL_THRESHOLD} bytes (eg 128kb).      * Use<tt>-1</tt> to disable overflow to disk.      */
DECL|method|setSpoolThreshold (long threshold)
name|void
name|setSpoolThreshold
parameter_list|(
name|long
name|threshold
parameter_list|)
function_decl|;
DECL|method|getSpoolThreshold ()
name|long
name|getSpoolThreshold
parameter_list|()
function_decl|;
comment|/**      * Sets the buffer size to use when copying between buffers.      *<p/>      * The default size is {@link org.apache.camel.util.IOHelper#DEFAULT_BUFFER_SIZE}      */
DECL|method|setBufferSize (int bufferSize)
name|void
name|setBufferSize
parameter_list|(
name|int
name|bufferSize
parameter_list|)
function_decl|;
DECL|method|getBufferSize ()
name|int
name|getBufferSize
parameter_list|()
function_decl|;
comment|/**      * Sets a chiper name to use when spooling to disk to write with encryption.      *<p/>      * By default the data is not encrypted.      */
DECL|method|setSpoolChiper (String chiper)
name|void
name|setSpoolChiper
parameter_list|(
name|String
name|chiper
parameter_list|)
function_decl|;
DECL|method|getSpoolChiper ()
name|String
name|getSpoolChiper
parameter_list|()
function_decl|;
comment|/**      * Whether to remove the temporary directory when stopping.      *<p/>      * This option is default<tt>true</tt>      */
DECL|method|setRemoveTemporaryDirectoryWhenStopping (boolean remove)
name|void
name|setRemoveTemporaryDirectoryWhenStopping
parameter_list|(
name|boolean
name|remove
parameter_list|)
function_decl|;
DECL|method|isRemoveTemporaryDirectoryWhenStopping ()
name|boolean
name|isRemoveTemporaryDirectoryWhenStopping
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

