begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.springboot.ha
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
operator|.
name|springboot
operator|.
name|ha
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.file.cluster.service"
argument_list|)
DECL|class|FileLockClusterServiceConfiguration
specifier|public
class|class
name|FileLockClusterServiceConfiguration
block|{
comment|/**      * Sets if the zookeeper cluster service should be enabled or not, default is false.      */
DECL|field|enabled
specifier|private
name|boolean
name|enabled
decl_stmt|;
comment|/**      * Cluster Service ID      */
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
comment|/**      * The root path.      */
DECL|field|root
specifier|private
name|String
name|root
decl_stmt|;
comment|/**      * The time to wait before starting to try to acquire lock.      */
DECL|field|acquireLockDelay
specifier|private
name|String
name|acquireLockDelay
decl_stmt|;
comment|/**      * The time to wait between attempts to try to acquire lock.      */
DECL|field|acquireLockInterval
specifier|private
name|String
name|acquireLockInterval
decl_stmt|;
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
DECL|method|getRoot ()
specifier|public
name|String
name|getRoot
parameter_list|()
block|{
return|return
name|root
return|;
block|}
DECL|method|setRoot (String root)
specifier|public
name|void
name|setRoot
parameter_list|(
name|String
name|root
parameter_list|)
block|{
name|this
operator|.
name|root
operator|=
name|root
expr_stmt|;
block|}
DECL|method|getAcquireLockDelay ()
specifier|public
name|String
name|getAcquireLockDelay
parameter_list|()
block|{
return|return
name|acquireLockDelay
return|;
block|}
DECL|method|setAcquireLockDelay (String acquireLockDelay)
specifier|public
name|void
name|setAcquireLockDelay
parameter_list|(
name|String
name|acquireLockDelay
parameter_list|)
block|{
name|this
operator|.
name|acquireLockDelay
operator|=
name|acquireLockDelay
expr_stmt|;
block|}
DECL|method|getAcquireLockInterval ()
specifier|public
name|String
name|getAcquireLockInterval
parameter_list|()
block|{
return|return
name|acquireLockInterval
return|;
block|}
DECL|method|setAcquireLockInterval (String acquireLockInterval)
specifier|public
name|void
name|setAcquireLockInterval
parameter_list|(
name|String
name|acquireLockInterval
parameter_list|)
block|{
name|this
operator|.
name|acquireLockInterval
operator|=
name|acquireLockInterval
expr_stmt|;
block|}
block|}
end_class

end_unit

