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
name|Service
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

begin_comment
comment|/**  * Strategy for using<a href="http://camel.apache.org/stream-caching.html">stream caching</a>.  */
end_comment

begin_interface
DECL|interface|StreamCachingStrategy
specifier|public
interface|interface
name|StreamCachingStrategy
extends|extends
name|Service
block|{
comment|/**      * Utilization statistics of stream caching.      */
DECL|interface|Statistics
interface|interface
name|Statistics
block|{
comment|/**          * Gets the counter for number of in-memory {@link StreamCache} created.          */
DECL|method|getCacheMemoryCounter ()
name|long
name|getCacheMemoryCounter
parameter_list|()
function_decl|;
comment|/**          * Gets the counter for number of spooled (not in-memory) {@link StreamCache} created.          */
DECL|method|getCacheSpoolCounter ()
name|long
name|getCacheSpoolCounter
parameter_list|()
function_decl|;
comment|/**          * Gets the total accumulated number of bytes which has been stream cached for in-memory stream caches.          */
DECL|method|getCacheMemorySize ()
name|long
name|getCacheMemorySize
parameter_list|()
function_decl|;
comment|/**          * Gets the total accumulated number of bytes which has been stream cached for spooled stream caches.          */
DECL|method|getCacheSpoolSize ()
name|long
name|getCacheSpoolSize
parameter_list|()
function_decl|;
comment|/**          * Reset the counters          */
DECL|method|reset ()
name|void
name|reset
parameter_list|()
function_decl|;
comment|/**          * Whether statistics is enabled.          */
DECL|method|isStatisticsEnabled ()
name|boolean
name|isStatisticsEnabled
parameter_list|()
function_decl|;
comment|/**          * Sets whether statistics is enabled.          *          * @param statisticsEnabled<tt>true</tt> to enable          */
DECL|method|setStatisticsEnabled (boolean statisticsEnabled)
name|void
name|setStatisticsEnabled
parameter_list|(
name|boolean
name|statisticsEnabled
parameter_list|)
function_decl|;
block|}
comment|/**      * Sets whether the stream caching is enabled.      *<p/>      *<b>Notice:</b> This cannot be changed at runtime.      */
DECL|method|setEnabled (boolean enabled)
name|void
name|setEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
function_decl|;
DECL|method|isEnabled ()
name|boolean
name|isEnabled
parameter_list|()
function_decl|;
comment|/**      * Sets the spool (temporary) directory to use for overflow and spooling to disk.      *<p/>      * If no spool directory has been explicit configured, then a temporary directory      * is created in the<tt>java.io.tmpdir</tt> directory.      */
DECL|method|setSpoolDirectory (File path)
name|void
name|setSpoolDirectory
parameter_list|(
name|File
name|path
parameter_list|)
function_decl|;
DECL|method|getSpoolDirectory ()
name|File
name|getSpoolDirectory
parameter_list|()
function_decl|;
DECL|method|setSpoolDirectory (String path)
name|void
name|setSpoolDirectory
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
comment|/**      * Sets the buffer size to use when allocating in-memory buffers used for in-memory stream caches.      *<p/>      * The default size is {@link org.apache.camel.util.IOHelper#DEFAULT_BUFFER_SIZE}      */
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
DECL|method|setRemoveSpoolDirectoryWhenStopping (boolean remove)
name|void
name|setRemoveSpoolDirectoryWhenStopping
parameter_list|(
name|boolean
name|remove
parameter_list|)
function_decl|;
DECL|method|isRemoveSpoolDirectoryWhenStopping ()
name|boolean
name|isRemoveSpoolDirectoryWhenStopping
parameter_list|()
function_decl|;
comment|/**      * Gets the utilization statistics.      */
DECL|method|getStatistics ()
name|Statistics
name|getStatistics
parameter_list|()
function_decl|;
comment|/**      * Caches the body aas a {@link StreamCache}.      *      * @param exchange the exchange      * @return the body cached as a {@link StreamCache}, or<tt>null</tt> if not possible or no need to cache the body      */
DECL|method|cache (Exchange exchange)
name|StreamCache
name|cache
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

