begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.directvm.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|directvm
operator|.
name|springboot
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
name|spi
operator|.
name|HeaderFilterStrategy
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
name|NestedConfigurationProperty
import|;
end_import

begin_comment
comment|/**  * The direct-vm component provides direct synchronous call to another endpoint  * from any CamelContext in the same JVM.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.direct-vm"
argument_list|)
DECL|class|DirectVmComponentConfiguration
specifier|public
class|class
name|DirectVmComponentConfiguration
block|{
comment|/**      * If sending a message to a direct endpoint which has no active consumer      * then we can tell the producer to block and wait for the consumer to      * become active.      */
DECL|field|block
specifier|private
name|Boolean
name|block
decl_stmt|;
comment|/**      * The timeout value to use if block is enabled.      */
DECL|field|timeout
specifier|private
name|Long
name|timeout
init|=
literal|30000L
decl_stmt|;
comment|/**      * Sets a HeaderFilterStrategy that will only be applied on producer      * endpoints (on both directions: request and response). Default value:      * none.      */
annotation|@
name|NestedConfigurationProperty
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
decl_stmt|;
comment|/**      * Whether to propagate or not properties from the producer side to the      * consumer side and vice versa. Default value: true.      */
DECL|field|propagateProperties
specifier|private
name|Boolean
name|propagateProperties
init|=
literal|true
decl_stmt|;
DECL|method|getBlock ()
specifier|public
name|Boolean
name|getBlock
parameter_list|()
block|{
return|return
name|block
return|;
block|}
DECL|method|setBlock (Boolean block)
specifier|public
name|void
name|setBlock
parameter_list|(
name|Boolean
name|block
parameter_list|)
block|{
name|this
operator|.
name|block
operator|=
name|block
expr_stmt|;
block|}
DECL|method|getTimeout ()
specifier|public
name|Long
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
DECL|method|setTimeout (Long timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|Long
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
DECL|method|getHeaderFilterStrategy ()
specifier|public
name|HeaderFilterStrategy
name|getHeaderFilterStrategy
parameter_list|()
block|{
return|return
name|headerFilterStrategy
return|;
block|}
DECL|method|setHeaderFilterStrategy ( HeaderFilterStrategy headerFilterStrategy)
specifier|public
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
name|headerFilterStrategy
parameter_list|)
block|{
name|this
operator|.
name|headerFilterStrategy
operator|=
name|headerFilterStrategy
expr_stmt|;
block|}
DECL|method|getPropagateProperties ()
specifier|public
name|Boolean
name|getPropagateProperties
parameter_list|()
block|{
return|return
name|propagateProperties
return|;
block|}
DECL|method|setPropagateProperties (Boolean propagateProperties)
specifier|public
name|void
name|setPropagateProperties
parameter_list|(
name|Boolean
name|propagateProperties
parameter_list|)
block|{
name|this
operator|.
name|propagateProperties
operator|=
name|propagateProperties
expr_stmt|;
block|}
block|}
end_class

end_unit

