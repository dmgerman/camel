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
name|util
operator|.
name|concurrent
operator|.
name|RejectedExecutionHandler
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ThreadPoolRejectedPolicy
import|;
end_import

begin_comment
comment|/**  * A profile which defines thread pool settings.  *  * @version   */
end_comment

begin_interface
DECL|interface|ThreadPoolProfile
specifier|public
interface|interface
name|ThreadPoolProfile
block|{
comment|/**      * Gets the id of this profile      *      * @return the id of this profile      */
DECL|method|getId ()
name|String
name|getId
parameter_list|()
function_decl|;
comment|/**      * Whether this profile is the default profile (there can only be one).      *      * @return<tt>true</tt> if its the default profile,<tt>false</tt> otherwise      */
DECL|method|isDefaultProfile ()
name|Boolean
name|isDefaultProfile
parameter_list|()
function_decl|;
comment|/**      * Sets whether this profile is the default profile (there can only be one).      *      * @param defaultProfile the option      */
DECL|method|setDefaultProfile (Boolean defaultProfile)
name|void
name|setDefaultProfile
parameter_list|(
name|Boolean
name|defaultProfile
parameter_list|)
function_decl|;
comment|/**      * Gets the core pool size (threads to keep minimum in pool)      *      * @return the pool size      */
DECL|method|getPoolSize ()
name|Integer
name|getPoolSize
parameter_list|()
function_decl|;
comment|/**      * Sets the core pool size (threads to keep minimum in pool)      *      * @param poolSize the pool size      */
DECL|method|setPoolSize (Integer poolSize)
name|void
name|setPoolSize
parameter_list|(
name|Integer
name|poolSize
parameter_list|)
function_decl|;
comment|/**      * Gets the maximum pool size      *      * @return the maximum pool size      */
DECL|method|getMaxPoolSize ()
name|Integer
name|getMaxPoolSize
parameter_list|()
function_decl|;
comment|/**      * Sets the maximum pool size      *      * @param maxPoolSize the maximum pool size      */
DECL|method|setMaxPoolSize (Integer maxPoolSize)
name|void
name|setMaxPoolSize
parameter_list|(
name|Integer
name|maxPoolSize
parameter_list|)
function_decl|;
comment|/**      * Gets the keep alive time for inactive threads      *      * @return the keep alive time      */
DECL|method|getKeepAliveTime ()
name|Long
name|getKeepAliveTime
parameter_list|()
function_decl|;
comment|/**      * Sets the keep alive time for inactive threads      *      * @param keepAliveTime the keep alive time      */
DECL|method|setKeepAliveTime (Long keepAliveTime)
name|void
name|setKeepAliveTime
parameter_list|(
name|Long
name|keepAliveTime
parameter_list|)
function_decl|;
comment|/**      * Gets the time unit used for keep alive time      *      * @return the time unit      */
DECL|method|getTimeUnit ()
name|TimeUnit
name|getTimeUnit
parameter_list|()
function_decl|;
comment|/**      * Sets the time unit used for keep alive time      *      * @param timeUnit the time unit      */
DECL|method|setTimeUnit (TimeUnit timeUnit)
name|void
name|setTimeUnit
parameter_list|(
name|TimeUnit
name|timeUnit
parameter_list|)
function_decl|;
comment|/**      * Gets the maximum number of tasks in the work queue.      *<p/>      * Use<tt>-1</tt> or<tt>Integer.MAX_VALUE</tt> for an unbounded queue      *      * @return the max queue size      */
DECL|method|getMaxQueueSize ()
name|Integer
name|getMaxQueueSize
parameter_list|()
function_decl|;
comment|/**      * Sets the maximum number of tasks in the work queue.      *<p/>      * Use<tt>-1</tt> or<tt>Integer.MAX_VALUE</tt> for an unbounded queue      *      * @param maxQueueSize the max queue size      */
DECL|method|setMaxQueueSize (Integer maxQueueSize)
name|void
name|setMaxQueueSize
parameter_list|(
name|Integer
name|maxQueueSize
parameter_list|)
function_decl|;
comment|/**      * Gets the handler for tasks which cannot be executed by the thread pool.      *      * @return the policy for the handler      */
DECL|method|getRejectedPolicy ()
name|ThreadPoolRejectedPolicy
name|getRejectedPolicy
parameter_list|()
function_decl|;
comment|/**      * Gets the handler for tasks which cannot be executed by the thread pool.      *      * @return the handler, or<tt>null</tt> if none defined      */
DECL|method|getRejectedExecutionHandler ()
name|RejectedExecutionHandler
name|getRejectedExecutionHandler
parameter_list|()
function_decl|;
comment|/**      * Sets the handler for tasks which cannot be executed by the thread pool.      *      * @param rejectedPolicy  the policy for the handler      */
DECL|method|setRejectedPolicy (ThreadPoolRejectedPolicy rejectedPolicy)
name|void
name|setRejectedPolicy
parameter_list|(
name|ThreadPoolRejectedPolicy
name|rejectedPolicy
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

