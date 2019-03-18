begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kubernetes.cloud
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
name|cloud
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
name|component
operator|.
name|kubernetes
operator|.
name|KubernetesConfiguration
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
name|impl
operator|.
name|cloud
operator|.
name|DefaultServiceDiscovery
import|;
end_import

begin_comment
comment|/**  * Discovers where services are running on which servers in Kubernetes.  */
end_comment

begin_class
DECL|class|KubernetesServiceDiscovery
specifier|public
specifier|abstract
class|class
name|KubernetesServiceDiscovery
extends|extends
name|DefaultServiceDiscovery
block|{
DECL|field|configuration
specifier|private
specifier|final
name|KubernetesConfiguration
name|configuration
decl_stmt|;
DECL|method|KubernetesServiceDiscovery (KubernetesConfiguration configuration)
specifier|public
name|KubernetesServiceDiscovery
parameter_list|(
name|KubernetesConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getConfiguration ()
specifier|protected
name|KubernetesConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|this
operator|.
name|configuration
return|;
block|}
block|}
end_class

end_unit

