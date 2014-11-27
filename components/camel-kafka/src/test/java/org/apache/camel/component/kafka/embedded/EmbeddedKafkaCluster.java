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
name|org
operator|.
name|I0Itec
operator|.
name|zkclient
operator|.
name|ZkClient
import|;
end_import

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
name|*
import|;
end_import

begin_class
DECL|class|EmbeddedKafkaCluster
specifier|public
class|class
name|EmbeddedKafkaCluster
block|{
DECL|field|ports
specifier|private
specifier|final
name|List
argument_list|<
name|Integer
argument_list|>
name|ports
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
DECL|field|brokers
specifier|private
specifier|final
name|List
argument_list|<
name|KafkaServer
argument_list|>
name|brokers
decl_stmt|;
DECL|field|logDirs
specifier|private
specifier|final
name|List
argument_list|<
name|File
argument_list|>
name|logDirs
decl_stmt|;
DECL|method|EmbeddedKafkaCluster (String zkConnection)
specifier|public
name|EmbeddedKafkaCluster
parameter_list|(
name|String
name|zkConnection
parameter_list|)
block|{
name|this
argument_list|(
name|zkConnection
argument_list|,
operator|new
name|Properties
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|EmbeddedKafkaCluster (String zkConnection, Properties baseProperties)
specifier|public
name|EmbeddedKafkaCluster
parameter_list|(
name|String
name|zkConnection
parameter_list|,
name|Properties
name|baseProperties
parameter_list|)
block|{
name|this
argument_list|(
name|zkConnection
argument_list|,
name|baseProperties
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|EmbeddedKafkaCluster (String zkConnection, Properties baseProperties, List<Integer> ports)
specifier|public
name|EmbeddedKafkaCluster
parameter_list|(
name|String
name|zkConnection
parameter_list|,
name|Properties
name|baseProperties
parameter_list|,
name|List
argument_list|<
name|Integer
argument_list|>
name|ports
parameter_list|)
block|{
name|this
operator|.
name|zkConnection
operator|=
name|zkConnection
expr_stmt|;
name|this
operator|.
name|ports
operator|=
name|resolvePorts
argument_list|(
name|ports
argument_list|)
expr_stmt|;
name|this
operator|.
name|baseProperties
operator|=
name|baseProperties
expr_stmt|;
name|this
operator|.
name|brokers
operator|=
operator|new
name|ArrayList
argument_list|<
name|KafkaServer
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|logDirs
operator|=
operator|new
name|ArrayList
argument_list|<
name|File
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|brokerList
operator|=
name|constructBrokerList
argument_list|(
name|this
operator|.
name|ports
argument_list|)
expr_stmt|;
block|}
DECL|method|getZkClient ()
specifier|public
name|ZkClient
name|getZkClient
parameter_list|()
block|{
for|for
control|(
name|KafkaServer
name|server
range|:
name|brokers
control|)
block|{
return|return
name|server
operator|.
name|zkClient
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|createTopics (String...topics)
specifier|public
name|void
name|createTopics
parameter_list|(
name|String
modifier|...
name|topics
parameter_list|)
block|{
for|for
control|(
name|String
name|topic
range|:
name|topics
control|)
block|{
name|AdminUtils
operator|.
name|createTopic
argument_list|(
name|getZkClient
argument_list|()
argument_list|,
name|topic
argument_list|,
literal|2
argument_list|,
literal|1
argument_list|,
operator|new
name|Properties
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|resolvePorts (List<Integer> ports)
specifier|private
name|List
argument_list|<
name|Integer
argument_list|>
name|resolvePorts
parameter_list|(
name|List
argument_list|<
name|Integer
argument_list|>
name|ports
parameter_list|)
block|{
name|List
argument_list|<
name|Integer
argument_list|>
name|resolvedPorts
init|=
operator|new
name|ArrayList
argument_list|<
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Integer
name|port
range|:
name|ports
control|)
block|{
name|resolvedPorts
operator|.
name|add
argument_list|(
name|resolvePort
argument_list|(
name|port
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|resolvedPorts
return|;
block|}
DECL|method|resolvePort (int port)
specifier|private
name|int
name|resolvePort
parameter_list|(
name|int
name|port
parameter_list|)
block|{
if|if
condition|(
name|port
operator|==
operator|-
literal|1
condition|)
block|{
return|return
name|TestUtils
operator|.
name|getAvailablePort
argument_list|()
return|;
block|}
return|return
name|port
return|;
block|}
DECL|method|constructBrokerList (List<Integer> ports)
specifier|private
name|String
name|constructBrokerList
parameter_list|(
name|List
argument_list|<
name|Integer
argument_list|>
name|ports
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|Integer
name|port
range|:
name|ports
control|)
block|{
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"localhost:"
argument_list|)
operator|.
name|append
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|startup ()
specifier|public
name|void
name|startup
parameter_list|()
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|ports
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Integer
name|port
init|=
name|ports
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|File
name|logDir
init|=
name|TestUtils
operator|.
name|constructTempDir
argument_list|(
literal|"kafka-local"
argument_list|)
decl_stmt|;
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
name|String
operator|.
name|valueOf
argument_list|(
name|i
operator|+
literal|1
argument_list|)
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
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"EmbeddedKafkaCluster: local directory: "
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
name|KafkaServer
name|broker
init|=
name|startBroker
argument_list|(
name|properties
argument_list|)
decl_stmt|;
name|brokers
operator|.
name|add
argument_list|(
name|broker
argument_list|)
expr_stmt|;
name|logDirs
operator|.
name|add
argument_list|(
name|logDir
argument_list|)
expr_stmt|;
block|}
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
DECL|method|getProps ()
specifier|public
name|Properties
name|getProps
parameter_list|()
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|props
operator|.
name|putAll
argument_list|(
name|baseProperties
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"metadata.broker.list"
argument_list|,
name|brokerList
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"zookeeper.connect"
argument_list|,
name|zkConnection
argument_list|)
expr_stmt|;
return|return
name|props
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
DECL|method|getPorts ()
specifier|public
name|List
argument_list|<
name|Integer
argument_list|>
name|getPorts
parameter_list|()
block|{
return|return
name|ports
return|;
block|}
DECL|method|getZkConnection ()
specifier|public
name|String
name|getZkConnection
parameter_list|()
block|{
return|return
name|zkConnection
return|;
block|}
DECL|method|shutdown ()
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
for|for
control|(
name|KafkaServer
name|broker
range|:
name|brokers
control|)
block|{
try|try
block|{
name|broker
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
for|for
control|(
name|File
name|logDir
range|:
name|logDirs
control|)
block|{
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
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
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
literal|"EmbeddedKafkaCluster{"
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

