begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kafka.embedded
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
operator|.
name|embedded
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|kafka
operator|.
name|admin
operator|.
name|AdminUtils
import|;
end_import

begin_import
import|import
name|kafka
operator|.
name|admin
operator|.
name|RackAwareMode
import|;
end_import

begin_import
import|import
name|kafka
operator|.
name|metrics
operator|.
name|KafkaMetricsReporter
import|;
end_import

begin_import
import|import
name|kafka
operator|.
name|server
operator|.
name|KafkaConfig
import|;
end_import

begin_import
import|import
name|kafka
operator|.
name|server
operator|.
name|KafkaServer
import|;
end_import

begin_import
import|import
name|kafka
operator|.
name|utils
operator|.
name|ZkUtils
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
name|test
operator|.
name|AvailablePortFinder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|ExternalResource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|scala
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|scala
operator|.
name|collection
operator|.
name|mutable
operator|.
name|Buffer
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kafka
operator|.
name|embedded
operator|.
name|TestUtils
operator|.
name|constructTempDir
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kafka
operator|.
name|embedded
operator|.
name|TestUtils
operator|.
name|perTest
import|;
end_import

begin_class
DECL|class|EmbeddedKafkaBroker
specifier|public
class|class
name|EmbeddedKafkaBroker
extends|extends
name|ExternalResource
block|{
DECL|field|log
specifier|private
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|brokerId
specifier|private
specifier|final
name|Integer
name|brokerId
decl_stmt|;
DECL|field|port
specifier|private
specifier|final
name|Integer
name|port
decl_stmt|;
DECL|field|zkConnection
specifier|private
specifier|final
name|String
name|zkConnection
decl_stmt|;
DECL|field|baseProperties
specifier|private
specifier|final
name|Properties
name|baseProperties
decl_stmt|;
DECL|field|brokerList
specifier|private
specifier|final
name|String
name|brokerList
decl_stmt|;
DECL|field|kafkaServer
specifier|private
name|KafkaServer
name|kafkaServer
decl_stmt|;
DECL|field|logDir
specifier|private
name|File
name|logDir
decl_stmt|;
DECL|field|zkUtils
specifier|private
name|ZkUtils
name|zkUtils
decl_stmt|;
DECL|method|EmbeddedKafkaBroker (int brokerId, String zkConnection)
specifier|public
name|EmbeddedKafkaBroker
parameter_list|(
name|int
name|brokerId
parameter_list|,
name|String
name|zkConnection
parameter_list|)
block|{
name|this
argument_list|(
name|brokerId
argument_list|,
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
argument_list|,
name|zkConnection
argument_list|,
operator|new
name|Properties
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|EmbeddedKafkaBroker (int brokerId, int port, String zkConnection, Properties baseProperties)
specifier|public
name|EmbeddedKafkaBroker
parameter_list|(
name|int
name|brokerId
parameter_list|,
name|int
name|port
parameter_list|,
name|String
name|zkConnection
parameter_list|,
name|Properties
name|baseProperties
parameter_list|)
block|{
name|this
operator|.
name|brokerId
operator|=
name|brokerId
expr_stmt|;
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
name|this
operator|.
name|zkConnection
operator|=
name|zkConnection
expr_stmt|;
name|this
operator|.
name|baseProperties
operator|=
name|baseProperties
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Starting broker[{}] on port {}"
argument_list|,
name|brokerId
argument_list|,
name|port
argument_list|)
expr_stmt|;
name|this
operator|.
name|brokerList
operator|=
literal|"localhost:"
operator|+
name|this
operator|.
name|port
expr_stmt|;
block|}
DECL|method|getZkUtils ()
specifier|public
name|ZkUtils
name|getZkUtils
parameter_list|()
block|{
return|return
name|zkUtils
return|;
block|}
DECL|method|createTopic (String topic, int partitionCount)
specifier|public
name|void
name|createTopic
parameter_list|(
name|String
name|topic
parameter_list|,
name|int
name|partitionCount
parameter_list|)
block|{
name|AdminUtils
operator|.
name|createTopic
argument_list|(
name|getZkUtils
argument_list|()
argument_list|,
name|topic
argument_list|,
name|partitionCount
argument_list|,
literal|1
argument_list|,
operator|new
name|Properties
argument_list|()
argument_list|,
name|RackAwareMode
operator|.
name|Enforced$
operator|.
name|MODULE$
argument_list|)
expr_stmt|;
block|}
DECL|method|before ()
specifier|public
name|void
name|before
parameter_list|()
block|{
name|logDir
operator|=
name|constructTempDir
argument_list|(
name|perTest
argument_list|(
literal|"kafka-log"
argument_list|)
argument_list|)
expr_stmt|;
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|putAll
argument_list|(
name|baseProperties
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setProperty
argument_list|(
literal|"zookeeper.connect"
argument_list|,
name|zkConnection
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setProperty
argument_list|(
literal|"broker.id"
argument_list|,
name|brokerId
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setProperty
argument_list|(
literal|"host.name"
argument_list|,
literal|"localhost"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setProperty
argument_list|(
literal|"port"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|port
argument_list|)
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setProperty
argument_list|(
literal|"log.dir"
argument_list|,
name|logDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setProperty
argument_list|(
literal|"num.partitions"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setProperty
argument_list|(
literal|"auto.create.topics.enable"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"log directory: "
operator|+
name|logDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setProperty
argument_list|(
literal|"log.flush.interval.messages"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setProperty
argument_list|(
literal|"offsets.topic.replication.factor"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|kafkaServer
operator|=
name|startBroker
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
DECL|method|startBroker (Properties props)
specifier|private
name|KafkaServer
name|startBroker
parameter_list|(
name|Properties
name|props
parameter_list|)
block|{
name|zkUtils
operator|=
name|ZkUtils
operator|.
name|apply
argument_list|(
name|zkConnection
argument_list|,
literal|30000
argument_list|,
literal|30000
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|KafkaMetricsReporter
argument_list|>
name|kmrList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Buffer
argument_list|<
name|KafkaMetricsReporter
argument_list|>
name|metricsList
init|=
name|scala
operator|.
name|collection
operator|.
name|JavaConversions
operator|.
name|asScalaBuffer
argument_list|(
name|kmrList
argument_list|)
decl_stmt|;
name|KafkaServer
name|server
init|=
operator|new
name|KafkaServer
argument_list|(
operator|new
name|KafkaConfig
argument_list|(
name|props
argument_list|)
argument_list|,
operator|new
name|SystemTime
argument_list|()
argument_list|,
name|Option
operator|.
expr|<
name|String
operator|>
name|empty
argument_list|()
argument_list|,
name|metricsList
argument_list|)
decl_stmt|;
name|server
operator|.
name|startup
argument_list|()
expr_stmt|;
return|return
name|server
return|;
block|}
DECL|method|getBrokerList ()
specifier|public
name|String
name|getBrokerList
parameter_list|()
block|{
return|return
name|brokerList
return|;
block|}
DECL|method|getPort ()
specifier|public
name|Integer
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|after ()
specifier|public
name|void
name|after
parameter_list|()
block|{
name|kafkaServer
operator|.
name|shutdown
argument_list|()
expr_stmt|;
try|try
block|{
name|TestUtils
operator|.
name|deleteFile
argument_list|(
name|logDir
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Could not delete {} - not found"
argument_list|,
name|logDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"EmbeddedKafkaBroker{"
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"brokerList='"
argument_list|)
operator|.
name|append
argument_list|(
name|brokerList
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

