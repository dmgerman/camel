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

begin_comment
comment|/**  * Consumes the sample stream  */
end_comment

begin_class
DECL|class|SampleConsumer
specifier|public
class|class
name|SampleConsumer
extends|extends
name|StreamingConsumer
block|{
DECL|method|SampleConsumer (TwitterEndpoint te)
specifier|public
name|SampleConsumer
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
block|}
DECL|method|startStreaming ()
specifier|protected
name|void
name|startStreaming
parameter_list|()
block|{
name|twitterStream
operator|.
name|sample
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

