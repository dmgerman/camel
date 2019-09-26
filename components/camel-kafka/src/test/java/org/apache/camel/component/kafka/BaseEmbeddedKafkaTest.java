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
name|Properties
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
name|BindToRegistry
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
name|component
operator|.
name|kafka
operator|.
name|embedded
operator|.
name|EmbeddedKafkaBroker
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
name|kafka
operator|.
name|embedded
operator|.
name|EmbeddedZookeeper
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
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|producer
operator|.
name|ProducerConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|ClassRule
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

begin_class
DECL|class|BaseEmbeddedKafkaTest
specifier|public
class|class
name|BaseEmbeddedKafkaTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|ClassRule
DECL|field|zookeeper
specifier|public
specifier|static
name|EmbeddedZookeeper
name|zookeeper
init|=
operator|new
name|EmbeddedZookeeper
argument_list|(
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|ClassRule
DECL|field|kafkaBroker
specifier|public
specifier|static
name|EmbeddedKafkaBroker
name|kafkaBroker
init|=
operator|new
name|EmbeddedKafkaBroker
argument_list|(
literal|0
argument_list|,
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
argument_list|,
name|zookeeper
operator|.
name|getConnection
argument_list|()
argument_list|,
operator|new
name|Properties
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|BaseEmbeddedKafkaTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|beforeClass ()
specifier|public
specifier|static
name|void
name|beforeClass
parameter_list|()
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"### Embedded Zookeeper connection: "
operator|+
name|zookeeper
operator|.
name|getConnection
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"### Embedded Kafka cluster broker list: "
operator|+
name|kafkaBroker
operator|.
name|getBrokerList
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getDefaultProperties ()
specifier|protected
name|Properties
name|getDefaultProperties
parameter_list|()
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Connecting to Kafka port {}"
argument_list|,
name|kafkaBroker
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|ProducerConfig
operator|.
name|BOOTSTRAP_SERVERS_CONFIG
argument_list|,
name|kafkaBroker
operator|.
name|getBrokerList
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|ProducerConfig
operator|.
name|KEY_SERIALIZER_CLASS_CONFIG
argument_list|,
name|KafkaConstants
operator|.
name|KAFKA_DEFAULT_SERIALIZER
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|ProducerConfig
operator|.
name|VALUE_SERIALIZER_CLASS_CONFIG
argument_list|,
name|KafkaConstants
operator|.
name|KAFKA_DEFAULT_SERIALIZER
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|ProducerConfig
operator|.
name|PARTITIONER_CLASS_CONFIG
argument_list|,
name|KafkaConstants
operator|.
name|KAFKA_DEFAULT_PARTITIONER
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|ProducerConfig
operator|.
name|ACKS_CONFIG
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
return|return
name|props
return|;
block|}
annotation|@
name|BindToRegistry
argument_list|(
literal|"prop"
argument_list|)
DECL|method|loadProperties ()
specifier|public
name|Properties
name|loadProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|prop
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|prop
operator|.
name|setProperty
argument_list|(
literal|"zookeeperPort"
argument_list|,
literal|""
operator|+
name|getZookeeperPort
argument_list|()
argument_list|)
expr_stmt|;
name|prop
operator|.
name|setProperty
argument_list|(
literal|"kafkaPort"
argument_list|,
literal|""
operator|+
name|getKafkaPort
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|prop
return|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|getPropertiesComponent
argument_list|()
operator|.
name|setLocation
argument_list|(
literal|"ref:prop"
argument_list|)
expr_stmt|;
name|KafkaComponent
name|kafka
init|=
operator|new
name|KafkaComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|kafka
operator|.
name|setBrokers
argument_list|(
literal|"localhost:"
operator|+
name|getKafkaPort
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"kafka"
argument_list|,
name|kafka
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
DECL|method|getZookeeperPort ()
specifier|protected
specifier|static
name|int
name|getZookeeperPort
parameter_list|()
block|{
return|return
name|zookeeper
operator|.
name|getPort
argument_list|()
return|;
block|}
DECL|method|getKafkaPort ()
specifier|protected
specifier|static
name|int
name|getKafkaPort
parameter_list|()
block|{
return|return
name|kafkaBroker
operator|.
name|getPort
argument_list|()
return|;
block|}
block|}
end_class

end_unit

