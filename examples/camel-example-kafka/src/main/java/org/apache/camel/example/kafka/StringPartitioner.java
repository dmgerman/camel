begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.kafka
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
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
name|Partitioner
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
name|common
operator|.
name|Cluster
import|;
end_import

begin_class
DECL|class|StringPartitioner
specifier|public
class|class
name|StringPartitioner
implements|implements
name|Partitioner
block|{
comment|/**      *       */
DECL|method|StringPartitioner ()
specifier|public
name|StringPartitioner
parameter_list|()
block|{
comment|// TODO Auto-generated constructor stub
block|}
comment|/*      * (non-Javadoc)      *       * @see org.apache.kafka.common.Configurable#configure(java.util.Map)      */
annotation|@
name|Override
DECL|method|configure (Map<String, ?> configs)
specifier|public
name|void
name|configure
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|configs
parameter_list|)
block|{     }
comment|/*      * (non-Javadoc)      *       * @see      * org.apache.kafka.clients.producer.Partitioner#partition(java.lang.String,      * java.lang.Object, byte[], java.lang.Object, byte[],      * org.apache.kafka.common.Cluster)      */
annotation|@
name|Override
DECL|method|partition (String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster)
specifier|public
name|int
name|partition
parameter_list|(
name|String
name|topic
parameter_list|,
name|Object
name|key
parameter_list|,
name|byte
index|[]
name|keyBytes
parameter_list|,
name|Object
name|value
parameter_list|,
name|byte
index|[]
name|valueBytes
parameter_list|,
name|Cluster
name|cluster
parameter_list|)
block|{
name|int
name|partId
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|key
operator|!=
literal|null
operator|&&
name|key
operator|instanceof
name|String
condition|)
block|{
name|String
name|sKey
init|=
operator|(
name|String
operator|)
name|key
decl_stmt|;
name|int
name|len
init|=
name|sKey
operator|.
name|length
argument_list|()
decl_stmt|;
comment|// This will return either 1 or zero
name|partId
operator|=
name|len
operator|%
literal|2
expr_stmt|;
block|}
return|return
name|partId
return|;
block|}
comment|/*      * (non-Javadoc)      *       * @see org.apache.kafka.clients.producer.Partitioner#close()      */
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{     }
block|}
end_class

end_unit

