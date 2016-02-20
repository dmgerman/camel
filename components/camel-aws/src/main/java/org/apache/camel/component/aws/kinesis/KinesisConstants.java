begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.kinesis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|kinesis
package|;
end_package

begin_interface
DECL|interface|KinesisConstants
specifier|public
interface|interface
name|KinesisConstants
block|{
DECL|field|SEQUENCE_NUMBER
name|String
name|SEQUENCE_NUMBER
init|=
literal|"CamelAwsKinesisSequenceNumber"
decl_stmt|;
DECL|field|APPROX_ARRIVAL_TIME
name|String
name|APPROX_ARRIVAL_TIME
init|=
literal|"CamelAwsKinesisApproximateArrivalTimestamp"
decl_stmt|;
DECL|field|PARTITION_KEY
name|String
name|PARTITION_KEY
init|=
literal|"CamelAwsKinesisPartitionKey"
decl_stmt|;
comment|/**      * in a Kinesis Record object, the shard ID is used on writes to indicate where the data was stored      */
DECL|field|SHARD_ID
name|String
name|SHARD_ID
init|=
literal|"CamelAwsKinesisShardId"
decl_stmt|;
block|}
end_interface

end_unit

