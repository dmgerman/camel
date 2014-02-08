begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|kafka
operator|.
name|producer
operator|.
name|Partitioner
import|;
end_import

begin_import
import|import
name|kafka
operator|.
name|utils
operator|.
name|VerifiableProperties
import|;
end_import

begin_class
DECL|class|SimplePartitioner
specifier|public
class|class
name|SimplePartitioner
implements|implements
name|Partitioner
argument_list|<
name|String
argument_list|>
block|{
DECL|method|SimplePartitioner (VerifiableProperties props)
specifier|public
name|SimplePartitioner
parameter_list|(
name|VerifiableProperties
name|props
parameter_list|)
block|{     }
comment|/**      * Uses the key to calculate a partition bucket id for routing      * the data to the appropriate broker partition      *      * @return an integer between 0 and numPartitions-1      */
annotation|@
name|Override
DECL|method|partition (String key, int numPartitions)
specifier|public
name|int
name|partition
parameter_list|(
name|String
name|key
parameter_list|,
name|int
name|numPartitions
parameter_list|)
block|{
return|return
name|key
operator|.
name|hashCode
argument_list|()
operator|%
name|numPartitions
return|;
block|}
block|}
end_class

end_unit

