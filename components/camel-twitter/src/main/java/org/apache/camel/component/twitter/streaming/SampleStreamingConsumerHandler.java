begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.twitter.streaming
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
name|StallWarning
import|;
end_import

begin_comment
comment|/**  * Consumes the sample stream  */
end_comment

begin_class
DECL|class|SampleStreamingConsumerHandler
specifier|public
class|class
name|SampleStreamingConsumerHandler
extends|extends
name|AbstractStreamingConsumerHandler
block|{
DECL|method|SampleStreamingConsumerHandler (TwitterEndpoint endpoint)
specifier|public
name|SampleStreamingConsumerHandler
parameter_list|(
name|TwitterEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
name|getTwitterStream
argument_list|()
operator|.
name|sample
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onStallWarning (StallWarning stallWarning)
specifier|public
name|void
name|onStallWarning
parameter_list|(
name|StallWarning
name|stallWarning
parameter_list|)
block|{
comment|// noop
block|}
block|}
end_class

end_unit

