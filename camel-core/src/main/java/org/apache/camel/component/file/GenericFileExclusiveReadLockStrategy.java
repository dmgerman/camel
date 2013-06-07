begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
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
name|LoggingLevel
import|;
end_import

begin_comment
comment|/**  * Strategy for acquiring exclusive read locks for files to be consumed. After  * granting the read lock it is released, we just want to make sure that when  * we start consuming the file its not currently in progress of being written by  * third party.  *<p/>  * Camel supports out of the box the following strategies:  *<ul>  *<li>FileRenameExclusiveReadLockStrategy waiting until its possible to rename the file.</li>  *<li>FileLockExclusiveReadLockStrategy acquiring a RW file lock for the duration of the processing.</li>  *<li>MarkerFileExclusiveReadLockStrategy using a marker file for acquiring read lock.</li>  *<li>FileChangedExclusiveReadLockStrategy using a file changed detection for acquiring read lock.</li>  *</ul>  */
end_comment

begin_interface
DECL|interface|GenericFileExclusiveReadLockStrategy
specifier|public
interface|interface
name|GenericFileExclusiveReadLockStrategy
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**      * Allows custom logic to be run on startup preparing the strategy, such as removing old lock files etc.      *      * @param operations generic file operations      * @param endpoint   the endpoint      * @throws Exception can be thrown in case of errors      */
DECL|method|prepareOnStartup (GenericFileOperations<T> operations, GenericFileEndpoint<T> endpoint)
name|void
name|prepareOnStartup
parameter_list|(
name|GenericFileOperations
argument_list|<
name|T
argument_list|>
name|operations
parameter_list|,
name|GenericFileEndpoint
argument_list|<
name|T
argument_list|>
name|endpoint
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Acquires exclusive read lock to the file.      *      * @param operations generic file operations      * @param file       the file      * @param exchange   the exchange      * @return<tt>true</tt> if read lock was acquired. If<tt>false</tt> Camel      *         will skip the file and try it on the next poll      * @throws Exception can be thrown in case of errors      */
DECL|method|acquireExclusiveReadLock (GenericFileOperations<T> operations, GenericFile<T> file, Exchange exchange)
name|boolean
name|acquireExclusiveReadLock
parameter_list|(
name|GenericFileOperations
argument_list|<
name|T
argument_list|>
name|operations
parameter_list|,
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Releases the exclusive read lock granted by the<tt>acquireExclusiveReadLock</tt> method.      *      * @param operations generic file operations      * @param file       the file      * @param exchange   the exchange      * @throws Exception can be thrown in case of errors      */
DECL|method|releaseExclusiveReadLock (GenericFileOperations<T> operations, GenericFile<T> file, Exchange exchange)
name|void
name|releaseExclusiveReadLock
parameter_list|(
name|GenericFileOperations
argument_list|<
name|T
argument_list|>
name|operations
parameter_list|,
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Sets an optional timeout period.      *<p/>      * If the readlock could not be granted within the time period then the wait is stopped and the      *<tt>acquireExclusiveReadLock</tt> method returns<tt>false</tt>.      *      * @param timeout period in millis      */
DECL|method|setTimeout (long timeout)
name|void
name|setTimeout
parameter_list|(
name|long
name|timeout
parameter_list|)
function_decl|;
comment|/**      * Sets the check interval period.      *<p/>      * The check interval is used for sleeping between attempts to acquire read lock.      * Setting a high value allows to cater for<i>slow writes</i> in case the producer      * of the file is slow.      *<p/>      * The default period is 1000 millis.      *      * @param checkInterval interval in millis      */
DECL|method|setCheckInterval (long checkInterval)
name|void
name|setCheckInterval
parameter_list|(
name|long
name|checkInterval
parameter_list|)
function_decl|;
comment|/**      * Sets logging level used when a read lock could not be acquired.      *<p/>      * Logging level used when a read lock could not be acquired.      *<p/>      * The default logging level is WARN      * @param readLockLoggingLevel LoggingLevel      */
DECL|method|setReadLockLoggingLevel (LoggingLevel readLockLoggingLevel)
name|void
name|setReadLockLoggingLevel
parameter_list|(
name|LoggingLevel
name|readLockLoggingLevel
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

