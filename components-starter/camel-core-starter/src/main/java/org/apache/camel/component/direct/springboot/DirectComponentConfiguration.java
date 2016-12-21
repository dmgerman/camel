begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.direct.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|direct
operator|.
name|springboot
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

begin_comment
comment|/**  * The direct component provides direct synchronous call to another endpoint  * from the same CamelContext.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.direct"
argument_list|)
DECL|class|DirectComponentConfiguration
specifier|public
class|class
name|DirectComponentConfiguration
block|{
comment|/**      * If sending a message to a direct endpoint which has no active consumer      * then we can tell the producer to block and wait for the consumer to      * become active.      */
DECL|field|block
specifier|private
name|Boolean
name|block
init|=
literal|false
decl_stmt|;
comment|/**      * The timeout value to use if block is enabled.      */
DECL|field|timeout
specifier|private
name|Long
name|timeout
init|=
literal|30000L
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
block|}
end_class

end_unit

