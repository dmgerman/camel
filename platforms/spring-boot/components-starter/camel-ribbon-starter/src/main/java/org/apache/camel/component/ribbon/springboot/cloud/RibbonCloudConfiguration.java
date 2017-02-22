begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ribbon.springboot.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ribbon
operator|.
name|springboot
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
name|ribbon
operator|.
name|RibbonConfiguration
import|;
end_import

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
literal|"camel.cloud.ribbon"
argument_list|)
DECL|class|RibbonCloudConfiguration
specifier|public
class|class
name|RibbonCloudConfiguration
block|{
DECL|field|enabled
specifier|private
name|boolean
name|enabled
init|=
literal|true
decl_stmt|;
DECL|field|loadBalancer
specifier|private
name|LoadBalancerConfiguration
name|loadBalancer
init|=
operator|new
name|LoadBalancerConfiguration
argument_list|()
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
DECL|method|getLoadBalancer ()
specifier|public
name|LoadBalancerConfiguration
name|getLoadBalancer
parameter_list|()
block|{
return|return
name|loadBalancer
return|;
block|}
comment|// *************************************************************************
comment|//
comment|// *************************************************************************
DECL|class|LoadBalancerConfiguration
specifier|public
class|class
name|LoadBalancerConfiguration
extends|extends
name|RibbonConfiguration
block|{     }
block|}
end_class

end_unit

