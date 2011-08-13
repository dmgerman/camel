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
name|Serializable
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
comment|/**  * A profile which defines thread pool settings.  *<p/>  * See more details at<a href="http://camel.apache.org/threading-model.html">threading model</a>  *  * @version   */
end_comment

begin_class
DECL|class|ThreadPoolProfile
specifier|public
class|class
name|ThreadPoolProfile
implements|implements
name|Serializable
block|{
comment|// TODO: Camel 2.9/3.0 consider moving to org.apache.camel
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|field|defaultProfile
specifier|private
name|Boolean
name|defaultProfile
decl_stmt|;
DECL|field|poolSize
specifier|private
name|Integer
name|poolSize
decl_stmt|;
DECL|field|maxPoolSize
specifier|private
name|Integer
name|maxPoolSize
decl_stmt|;
DECL|field|keepAliveTime
specifier|private
name|Long
name|keepAliveTime
decl_stmt|;
DECL|field|timeUnit
specifier|private
name|TimeUnit
name|timeUnit
decl_stmt|;
DECL|field|maxQueueSize
specifier|private
name|Integer
name|maxQueueSize
decl_stmt|;
DECL|field|rejectedPolicy
specifier|private
name|ThreadPoolRejectedPolicy
name|rejectedPolicy
decl_stmt|;
comment|/**      * Creates a new thread pool profile, with no id set.      */
DECL|method|ThreadPoolProfile ()
specifier|public
name|ThreadPoolProfile
parameter_list|()
block|{     }
comment|/**      * Creates a new thread pool profile      *      * @param id id of the profile      */
DECL|method|ThreadPoolProfile (String id)
specifier|public
name|ThreadPoolProfile
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
comment|/**      * Gets the id of this profile      *      * @return the id of this profile      */
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
comment|/**      * Sets the id of this profile      *      * @param id profile id      */
DECL|method|setId (String id)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
comment|/**      * Whether this profile is the default profile (there can only be one).      *      * @return<tt>true</tt> if its the default profile,<tt>false</tt> otherwise      */
DECL|method|isDefaultProfile ()
specifier|public
name|Boolean
name|isDefaultProfile
parameter_list|()
block|{
return|return
name|defaultProfile
operator|!=
literal|null
operator|&&
name|defaultProfile
return|;
block|}
comment|/**      * Sets whether this profile is the default profile (there can only be one).      *      * @param defaultProfile the option      */
DECL|method|setDefaultProfile (Boolean defaultProfile)
specifier|public
name|void
name|setDefaultProfile
parameter_list|(
name|Boolean
name|defaultProfile
parameter_list|)
block|{
name|this
operator|.
name|defaultProfile
operator|=
name|defaultProfile
expr_stmt|;
block|}
comment|/**      * Gets the core pool size (threads to keep minimum in pool)      *      * @return the pool size      */
DECL|method|getPoolSize ()
specifier|public
name|Integer
name|getPoolSize
parameter_list|()
block|{
return|return
name|poolSize
return|;
block|}
comment|/**      * Sets the core pool size (threads to keep minimum in pool)      *      * @param poolSize the pool size      */
DECL|method|setPoolSize (Integer poolSize)
specifier|public
name|void
name|setPoolSize
parameter_list|(
name|Integer
name|poolSize
parameter_list|)
block|{
name|this
operator|.
name|poolSize
operator|=
name|poolSize
expr_stmt|;
block|}
comment|/**      * Gets the maximum pool size      *      * @return the maximum pool size      */
DECL|method|getMaxPoolSize ()
specifier|public
name|Integer
name|getMaxPoolSize
parameter_list|()
block|{
return|return
name|maxPoolSize
return|;
block|}
comment|/**      * Sets the maximum pool size      *      * @param maxPoolSize the max pool size      */
DECL|method|setMaxPoolSize (Integer maxPoolSize)
specifier|public
name|void
name|setMaxPoolSize
parameter_list|(
name|Integer
name|maxPoolSize
parameter_list|)
block|{
name|this
operator|.
name|maxPoolSize
operator|=
name|maxPoolSize
expr_stmt|;
block|}
comment|/**      * Gets the keep alive time for inactive threads      *      * @return the keep alive time      */
DECL|method|getKeepAliveTime ()
specifier|public
name|Long
name|getKeepAliveTime
parameter_list|()
block|{
return|return
name|keepAliveTime
return|;
block|}
comment|/**      * Sets the keep alive time for inactive threads      *      * @param keepAliveTime the keep alive time      */
DECL|method|setKeepAliveTime (Long keepAliveTime)
specifier|public
name|void
name|setKeepAliveTime
parameter_list|(
name|Long
name|keepAliveTime
parameter_list|)
block|{
name|this
operator|.
name|keepAliveTime
operator|=
name|keepAliveTime
expr_stmt|;
block|}
comment|/**      * Gets the time unit used for keep alive time      *      * @return the time unit      */
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
comment|/**      * Sets the time unit used for keep alive time      *      * @param timeUnit the time unit      */
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
comment|/**      * Gets the maximum number of tasks in the work queue.      *<p/>      * Use<tt>-1</tt> or<tt>Integer.MAX_VALUE</tt> for an unbounded queue      *      * @return the max queue size      */
DECL|method|getMaxQueueSize ()
specifier|public
name|Integer
name|getMaxQueueSize
parameter_list|()
block|{
return|return
name|maxQueueSize
return|;
block|}
comment|/**      * Sets the maximum number of tasks in the work queue.      *<p/>      * Use<tt>-1</tt> or<tt>Integer.MAX_VALUE</tt> for an unbounded queue      *      * @param maxQueueSize the max queue size      */
DECL|method|setMaxQueueSize (Integer maxQueueSize)
specifier|public
name|void
name|setMaxQueueSize
parameter_list|(
name|Integer
name|maxQueueSize
parameter_list|)
block|{
name|this
operator|.
name|maxQueueSize
operator|=
name|maxQueueSize
expr_stmt|;
block|}
comment|/**      * Gets the policy for tasks which cannot be executed by the thread pool.      *      * @return the policy for the handler      */
DECL|method|getRejectedPolicy ()
specifier|public
name|ThreadPoolRejectedPolicy
name|getRejectedPolicy
parameter_list|()
block|{
return|return
name|rejectedPolicy
return|;
block|}
comment|/**      * Gets the handler for tasks which cannot be executed by the thread pool.      *      * @return the handler, or<tt>null</tt> if none defined      */
DECL|method|getRejectedExecutionHandler ()
specifier|public
name|RejectedExecutionHandler
name|getRejectedExecutionHandler
parameter_list|()
block|{
if|if
condition|(
name|rejectedPolicy
operator|!=
literal|null
condition|)
block|{
return|return
name|rejectedPolicy
operator|.
name|asRejectedExecutionHandler
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Sets the handler for tasks which cannot be executed by the thread pool.      *      * @param rejectedPolicy  the policy for the handler      */
DECL|method|setRejectedPolicy (ThreadPoolRejectedPolicy rejectedPolicy)
specifier|public
name|void
name|setRejectedPolicy
parameter_list|(
name|ThreadPoolRejectedPolicy
name|rejectedPolicy
parameter_list|)
block|{
name|this
operator|.
name|rejectedPolicy
operator|=
name|rejectedPolicy
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
literal|"ThreadPoolProfile["
operator|+
name|id
operator|+
literal|" ("
operator|+
name|defaultProfile
operator|+
literal|") size:"
operator|+
name|poolSize
operator|+
literal|"-"
operator|+
name|maxPoolSize
operator|+
literal|", keepAlive: "
operator|+
name|keepAliveTime
operator|+
literal|" "
operator|+
name|timeUnit
operator|+
literal|", maxQueue: "
operator|+
name|maxQueueSize
operator|+
literal|", rejectedPolicy:"
operator|+
name|rejectedPolicy
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

