begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.twitter.consumer.streaming
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|twitter
operator|.
name|consumer
operator|.
name|streaming
package|;
end_package

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
name|twitter
operator|.
name|TwitterEndpoint
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|FilterQuery
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|TwitterStream
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|TwitterStreamFactory
import|;
end_import

begin_comment
comment|/**  * Consumes the filter stream  *  */
end_comment

begin_class
DECL|class|FilterConsumer
specifier|public
class|class
name|FilterConsumer
extends|extends
name|StreamingConsumer
block|{
DECL|method|FilterConsumer (TwitterEndpoint te)
specifier|public
name|FilterConsumer
parameter_list|(
name|TwitterEndpoint
name|te
parameter_list|)
block|{
name|super
argument_list|(
name|te
argument_list|)
expr_stmt|;
name|TwitterStream
name|twitterStream
init|=
operator|new
name|TwitterStreamFactory
argument_list|(
name|te
operator|.
name|getProperties
argument_list|()
operator|.
name|getConfiguration
argument_list|()
argument_list|)
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|twitterStream
operator|.
name|addListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|String
name|allLocationsString
init|=
name|te
operator|.
name|getProperties
argument_list|()
operator|.
name|getLocations
argument_list|()
decl_stmt|;
name|String
index|[]
name|locationStrings
init|=
name|allLocationsString
operator|.
name|split
argument_list|(
literal|";"
argument_list|)
decl_stmt|;
name|double
index|[]
index|[]
name|locations
init|=
operator|new
name|double
index|[
name|locationStrings
operator|.
name|length
index|]
index|[
literal|2
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|locationStrings
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
index|[]
name|coords
init|=
name|locationStrings
index|[
name|i
index|]
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|locations
index|[
name|i
index|]
index|[
literal|0
index|]
operator|=
name|Double
operator|.
name|valueOf
argument_list|(
name|coords
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|locations
index|[
name|i
index|]
index|[
literal|1
index|]
operator|=
name|Double
operator|.
name|valueOf
argument_list|(
name|coords
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
name|FilterQuery
name|fq
init|=
operator|new
name|FilterQuery
argument_list|()
decl_stmt|;
name|fq
operator|.
name|locations
argument_list|(
name|locations
argument_list|)
expr_stmt|;
name|twitterStream
operator|.
name|filter
argument_list|(
name|fq
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

