begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kubernetes.consumer.common
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kubernetes
operator|.
name|consumer
operator|.
name|common
package|;
end_package

begin_import
import|import
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|api
operator|.
name|model
operator|.
name|ConfigMap
import|;
end_import

begin_import
import|import
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|client
operator|.
name|Watcher
operator|.
name|Action
import|;
end_import

begin_class
DECL|class|ConfigMapEvent
specifier|public
class|class
name|ConfigMapEvent
block|{
DECL|field|action
specifier|private
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|client
operator|.
name|Watcher
operator|.
name|Action
name|action
decl_stmt|;
DECL|field|configMap
specifier|private
name|ConfigMap
name|configMap
decl_stmt|;
DECL|method|ConfigMapEvent (Action action, ConfigMap configMap)
specifier|public
name|ConfigMapEvent
parameter_list|(
name|Action
name|action
parameter_list|,
name|ConfigMap
name|configMap
parameter_list|)
block|{
name|this
operator|.
name|action
operator|=
name|action
expr_stmt|;
name|this
operator|.
name|configMap
operator|=
name|configMap
expr_stmt|;
block|}
DECL|method|getAction ()
specifier|public
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|client
operator|.
name|Watcher
operator|.
name|Action
name|getAction
parameter_list|()
block|{
return|return
name|action
return|;
block|}
DECL|method|setAction (io.fabric8.kubernetes.client.Watcher.Action action)
specifier|public
name|void
name|setAction
parameter_list|(
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|client
operator|.
name|Watcher
operator|.
name|Action
name|action
parameter_list|)
block|{
name|this
operator|.
name|action
operator|=
name|action
expr_stmt|;
block|}
DECL|method|getConfigMap ()
specifier|public
name|ConfigMap
name|getConfigMap
parameter_list|()
block|{
return|return
name|configMap
return|;
block|}
DECL|method|setConfigMap (ConfigMap configMap)
specifier|public
name|void
name|setConfigMap
parameter_list|(
name|ConfigMap
name|configMap
parameter_list|)
block|{
name|this
operator|.
name|configMap
operator|=
name|configMap
expr_stmt|;
block|}
block|}
end_class

end_unit

