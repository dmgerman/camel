begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.twitter.consumer
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|twitter
operator|.
name|data
operator|.
name|Status
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|TwitterException
import|;
end_import

begin_interface
DECL|interface|Twitter4JConsumer
specifier|public
interface|interface
name|Twitter4JConsumer
block|{
DECL|method|requestPollingStatus (long lastStatusUpdateId)
name|Iterator
argument_list|<
name|Status
argument_list|>
name|requestPollingStatus
parameter_list|(
name|long
name|lastStatusUpdateId
parameter_list|)
throws|throws
name|TwitterException
function_decl|;
DECL|method|requestDirectStatus ()
name|Iterator
argument_list|<
name|Status
argument_list|>
name|requestDirectStatus
parameter_list|()
throws|throws
name|TwitterException
function_decl|;
block|}
end_interface

end_unit

