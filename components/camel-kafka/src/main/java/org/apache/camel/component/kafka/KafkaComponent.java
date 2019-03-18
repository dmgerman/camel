begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kafka
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kafka
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|ExecutorService
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
name|CamelContext
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
name|SSLContextParametersAware
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
name|spi
operator|.
name|Metadata
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
name|spi
operator|.
name|annotations
operator|.
name|Component
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
name|support
operator|.
name|DefaultComponent
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_class
annotation|@
name|Component
argument_list|(
literal|"kafka"
argument_list|)
DECL|class|KafkaComponent
specifier|public
class|class
name|KafkaComponent
extends|extends
name|DefaultComponent
implements|implements
name|SSLContextParametersAware
block|{
DECL|field|configuration
specifier|private
name|KafkaConfiguration
name|configuration
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|workerPool
specifier|private
name|ExecutorService
name|workerPool
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|useGlobalSslContextParameters
specifier|private
name|boolean
name|useGlobalSslContextParameters
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|breakOnFirstError
specifier|private
name|boolean
name|breakOnFirstError
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|allowManualCommit
specifier|private
name|boolean
name|allowManualCommit
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|)
DECL|field|kafkaManualCommitFactory
specifier|private
name|KafkaManualCommitFactory
name|kafkaManualCommitFactory
init|=
operator|new
name|DefaultKafkaManualCommitFactory
argument_list|()
decl_stmt|;
DECL|method|KafkaComponent ()
specifier|public
name|KafkaComponent
parameter_list|()
block|{     }
DECL|method|KafkaComponent (CamelContext context)
specifier|public
name|KafkaComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> params)
specifier|protected
name|KafkaEndpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|remaining
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Topic must be configured on endpoint using syntax kafka:topic"
argument_list|)
throw|;
block|}
name|KafkaEndpoint
name|endpoint
init|=
operator|new
name|KafkaEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|KafkaConfiguration
name|copy
init|=
name|configuration
operator|.
name|copy
argument_list|()
decl_stmt|;
name|endpoint
operator|.
name|setConfiguration
argument_list|(
name|copy
argument_list|)
expr_stmt|;
block|}
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setTopic
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setWorkerPool
argument_list|(
name|getWorkerPool
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setBreakOnFirstError
argument_list|(
name|isBreakOnFirstError
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setAllowManualCommit
argument_list|(
name|isAllowManualCommit
argument_list|()
argument_list|)
expr_stmt|;
comment|// brokers can be configured on either component or endpoint level
comment|// and the consumer and produce is aware of this and act accordingly
name|setProperties
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
argument_list|,
name|params
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|params
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSslContextParameters
argument_list|()
operator|==
literal|null
condition|)
block|{
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setSslContextParameters
argument_list|(
name|retrieveGlobalSslContextParameters
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|endpoint
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|KafkaConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
comment|/**      * Allows to pre-configure the Kafka component with common options that the endpoints will reuse.      */
DECL|method|setConfiguration (KafkaConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|KafkaConfiguration
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
DECL|method|getBrokers ()
specifier|public
name|String
name|getBrokers
parameter_list|()
block|{
return|return
name|configuration
operator|!=
literal|null
condition|?
name|configuration
operator|.
name|getBrokers
argument_list|()
else|:
literal|null
return|;
block|}
comment|/**      * URL of the Kafka brokers to use.      * The format is host1:port1,host2:port2, and the list can be a subset of brokers or a VIP pointing to a subset of brokers.      *<p/>      * This option is known as<tt>bootstrap.servers</tt> in the Kafka documentation.      */
DECL|method|setBrokers (String brokers)
specifier|public
name|void
name|setBrokers
parameter_list|(
name|String
name|brokers
parameter_list|)
block|{
if|if
condition|(
name|configuration
operator|==
literal|null
condition|)
block|{
name|configuration
operator|=
operator|new
name|KafkaConfiguration
argument_list|()
expr_stmt|;
block|}
name|configuration
operator|.
name|setBrokers
argument_list|(
name|brokers
argument_list|)
expr_stmt|;
block|}
DECL|method|getWorkerPool ()
specifier|public
name|ExecutorService
name|getWorkerPool
parameter_list|()
block|{
return|return
name|workerPool
return|;
block|}
comment|/**      * To use a shared custom worker pool for continue routing {@link Exchange} after kafka server has acknowledge      * the message that was sent to it from {@link KafkaProducer} using asynchronous non-blocking processing.      * If using this option then you must handle the lifecycle of the thread pool to shut the pool down when no longer needed.      */
DECL|method|setWorkerPool (ExecutorService workerPool)
specifier|public
name|void
name|setWorkerPool
parameter_list|(
name|ExecutorService
name|workerPool
parameter_list|)
block|{
name|this
operator|.
name|workerPool
operator|=
name|workerPool
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isUseGlobalSslContextParameters ()
specifier|public
name|boolean
name|isUseGlobalSslContextParameters
parameter_list|()
block|{
return|return
name|this
operator|.
name|useGlobalSslContextParameters
return|;
block|}
comment|/**      * Enable usage of global SSL context parameters.      */
annotation|@
name|Override
DECL|method|setUseGlobalSslContextParameters (boolean useGlobalSslContextParameters)
specifier|public
name|void
name|setUseGlobalSslContextParameters
parameter_list|(
name|boolean
name|useGlobalSslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|useGlobalSslContextParameters
operator|=
name|useGlobalSslContextParameters
expr_stmt|;
block|}
DECL|method|isBreakOnFirstError ()
specifier|public
name|boolean
name|isBreakOnFirstError
parameter_list|()
block|{
return|return
name|breakOnFirstError
return|;
block|}
comment|/**      * This options controls what happens when a consumer is processing an exchange and it fails.      * If the option is<tt>false</tt> then the consumer continues to the next message and processes it.      * If the option is<tt>true</tt> then the consumer breaks out, and will seek back to offset of the      * message that caused a failure, and then re-attempt to process this message. However this can lead      * to endless processing of the same message if its bound to fail every time, eg a poison message.      * Therefore its recommended to deal with that for example by using Camel's error handler.      */
DECL|method|setBreakOnFirstError (boolean breakOnFirstError)
specifier|public
name|void
name|setBreakOnFirstError
parameter_list|(
name|boolean
name|breakOnFirstError
parameter_list|)
block|{
name|this
operator|.
name|breakOnFirstError
operator|=
name|breakOnFirstError
expr_stmt|;
block|}
DECL|method|isAllowManualCommit ()
specifier|public
name|boolean
name|isAllowManualCommit
parameter_list|()
block|{
return|return
name|allowManualCommit
return|;
block|}
comment|/**      * Whether to allow doing manual commits via {@link KafkaManualCommit}.      *<p/>      * If this option is enabled then an instance of {@link KafkaManualCommit} is stored on the {@link Exchange} message header,      * which allows end users to access this API and perform manual offset commits via the Kafka consumer.      */
DECL|method|setAllowManualCommit (boolean allowManualCommit)
specifier|public
name|void
name|setAllowManualCommit
parameter_list|(
name|boolean
name|allowManualCommit
parameter_list|)
block|{
name|this
operator|.
name|allowManualCommit
operator|=
name|allowManualCommit
expr_stmt|;
block|}
DECL|method|getKafkaManualCommitFactory ()
specifier|public
name|KafkaManualCommitFactory
name|getKafkaManualCommitFactory
parameter_list|()
block|{
return|return
name|kafkaManualCommitFactory
return|;
block|}
comment|/**      * Factory to use for creating {@link KafkaManualCommit} instances. This allows to plugin a custom factory      * to create custom {@link KafkaManualCommit} instances in case special logic is needed when doing manual commits      * that deviates from the default implementation that comes out of the box.      */
DECL|method|setKafkaManualCommitFactory (KafkaManualCommitFactory kafkaManualCommitFactory)
specifier|public
name|void
name|setKafkaManualCommitFactory
parameter_list|(
name|KafkaManualCommitFactory
name|kafkaManualCommitFactory
parameter_list|)
block|{
name|this
operator|.
name|kafkaManualCommitFactory
operator|=
name|kafkaManualCommitFactory
expr_stmt|;
block|}
block|}
end_class

end_unit

