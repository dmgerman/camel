begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.batch.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
operator|.
name|batch
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

begin_comment
comment|/**  * The sjms-batch component is a specialized for highly performant,  * transactional batch consumption from a JMS queue.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.component.sjms-batch"
argument_list|)
DECL|class|SjmsBatchComponentConfiguration
specifier|public
class|class
name|SjmsBatchComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the sjms-batch component. This is      * enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * A ConnectionFactory is required to enable the SjmsBatchComponent. The      * option is a javax.jms.ConnectionFactory type.      */
DECL|field|connectionFactory
specifier|private
name|String
name|connectionFactory
decl_stmt|;
comment|/**      * Whether to startup the consumer message listener asynchronously, when      * starting a route. For example if a JmsConsumer cannot get a connection to      * a remote JMS broker, then it may block while retrying and/or failover.      * This will cause Camel to block while starting routes. By setting this      * option to true, you will let routes startup, while the JmsConsumer      * connects to the JMS broker using a dedicated thread in asynchronous mode.      * If this option is used, then beware that if the connection could not be      * established, then an exception is logged at WARN level, and the consumer      * will not be able to receive messages; You can then restart the route to      * retry.      */
DECL|field|asyncStartListener
specifier|private
name|Boolean
name|asyncStartListener
init|=
literal|false
decl_stmt|;
comment|/**      * Specifies the interval between recovery attempts, i.e. when a connection      * is being refreshed, in milliseconds. The default is 5000 ms, that is, 5      * seconds.      */
DECL|field|recoveryInterval
specifier|private
name|Integer
name|recoveryInterval
init|=
literal|5000
decl_stmt|;
comment|/**      * To use a custom org.apache.camel.spi.HeaderFilterStrategy to filter      * header to and from Camel message. The option is a      * org.apache.camel.spi.HeaderFilterStrategy type.      */
DECL|field|headerFilterStrategy
specifier|private
name|String
name|headerFilterStrategy
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
comment|/**      * Whether the component should use basic property binding (Camel 2.x) or      * the newer property binding with additional capabilities      */
DECL|field|basicPropertyBinding
specifier|private
name|Boolean
name|basicPropertyBinding
init|=
literal|false
decl_stmt|;
DECL|method|getConnectionFactory ()
specifier|public
name|String
name|getConnectionFactory
parameter_list|()
block|{
return|return
name|connectionFactory
return|;
block|}
DECL|method|setConnectionFactory (String connectionFactory)
specifier|public
name|void
name|setConnectionFactory
parameter_list|(
name|String
name|connectionFactory
parameter_list|)
block|{
name|this
operator|.
name|connectionFactory
operator|=
name|connectionFactory
expr_stmt|;
block|}
DECL|method|getAsyncStartListener ()
specifier|public
name|Boolean
name|getAsyncStartListener
parameter_list|()
block|{
return|return
name|asyncStartListener
return|;
block|}
DECL|method|setAsyncStartListener (Boolean asyncStartListener)
specifier|public
name|void
name|setAsyncStartListener
parameter_list|(
name|Boolean
name|asyncStartListener
parameter_list|)
block|{
name|this
operator|.
name|asyncStartListener
operator|=
name|asyncStartListener
expr_stmt|;
block|}
DECL|method|getRecoveryInterval ()
specifier|public
name|Integer
name|getRecoveryInterval
parameter_list|()
block|{
return|return
name|recoveryInterval
return|;
block|}
DECL|method|setRecoveryInterval (Integer recoveryInterval)
specifier|public
name|void
name|setRecoveryInterval
parameter_list|(
name|Integer
name|recoveryInterval
parameter_list|)
block|{
name|this
operator|.
name|recoveryInterval
operator|=
name|recoveryInterval
expr_stmt|;
block|}
DECL|method|getHeaderFilterStrategy ()
specifier|public
name|String
name|getHeaderFilterStrategy
parameter_list|()
block|{
return|return
name|headerFilterStrategy
return|;
block|}
DECL|method|setHeaderFilterStrategy (String headerFilterStrategy)
specifier|public
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|String
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
DECL|method|getBasicPropertyBinding ()
specifier|public
name|Boolean
name|getBasicPropertyBinding
parameter_list|()
block|{
return|return
name|basicPropertyBinding
return|;
block|}
DECL|method|setBasicPropertyBinding (Boolean basicPropertyBinding)
specifier|public
name|void
name|setBasicPropertyBinding
parameter_list|(
name|Boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|this
operator|.
name|basicPropertyBinding
operator|=
name|basicPropertyBinding
expr_stmt|;
block|}
block|}
end_class

end_unit

