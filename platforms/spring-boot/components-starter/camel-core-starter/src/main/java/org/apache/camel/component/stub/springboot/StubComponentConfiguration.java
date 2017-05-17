begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.stub.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|stub
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|component
operator|.
name|seda
operator|.
name|BlockingQueueFactory
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
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
comment|/**  * The stub component provides a simple way to stub out any physical endpoints  * while in development or testing.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.stub"
argument_list|)
DECL|class|StubComponentConfiguration
specifier|public
class|class
name|StubComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Sets the default maximum capacity of the SEDA queue (i.e. the number of      * messages it can hold).      */
DECL|field|queueSize
specifier|private
name|Integer
name|queueSize
decl_stmt|;
comment|/**      * Sets the default number of concurrent threads processing exchanges.      */
DECL|field|concurrentConsumers
specifier|private
name|Integer
name|concurrentConsumers
init|=
literal|1
decl_stmt|;
comment|/**      * Sets the default queue factory.      */
annotation|@
name|NestedConfigurationProperty
DECL|field|defaultQueueFactory
specifier|private
name|BlockingQueueFactory
argument_list|<
name|Exchange
argument_list|>
name|defaultQueueFactory
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getQueueSize ()
specifier|public
name|Integer
name|getQueueSize
parameter_list|()
block|{
return|return
name|queueSize
return|;
block|}
DECL|method|setQueueSize (Integer queueSize)
specifier|public
name|void
name|setQueueSize
parameter_list|(
name|Integer
name|queueSize
parameter_list|)
block|{
name|this
operator|.
name|queueSize
operator|=
name|queueSize
expr_stmt|;
block|}
DECL|method|getConcurrentConsumers ()
specifier|public
name|Integer
name|getConcurrentConsumers
parameter_list|()
block|{
return|return
name|concurrentConsumers
return|;
block|}
DECL|method|setConcurrentConsumers (Integer concurrentConsumers)
specifier|public
name|void
name|setConcurrentConsumers
parameter_list|(
name|Integer
name|concurrentConsumers
parameter_list|)
block|{
name|this
operator|.
name|concurrentConsumers
operator|=
name|concurrentConsumers
expr_stmt|;
block|}
DECL|method|getDefaultQueueFactory ()
specifier|public
name|BlockingQueueFactory
argument_list|<
name|Exchange
argument_list|>
name|getDefaultQueueFactory
parameter_list|()
block|{
return|return
name|defaultQueueFactory
return|;
block|}
DECL|method|setDefaultQueueFactory ( BlockingQueueFactory<Exchange> defaultQueueFactory)
specifier|public
name|void
name|setDefaultQueueFactory
parameter_list|(
name|BlockingQueueFactory
argument_list|<
name|Exchange
argument_list|>
name|defaultQueueFactory
parameter_list|)
block|{
name|this
operator|.
name|defaultQueueFactory
operator|=
name|defaultQueueFactory
expr_stmt|;
block|}
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
block|}
end_class

end_unit

