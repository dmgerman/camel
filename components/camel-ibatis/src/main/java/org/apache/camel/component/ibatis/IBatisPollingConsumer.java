begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ibatis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ibatis
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
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
name|com
operator|.
name|ibatis
operator|.
name|sqlmap
operator|.
name|client
operator|.
name|SqlMapClient
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
name|Exchange
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
name|Message
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
name|RuntimeCamelException
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
name|impl
operator|.
name|PollingConsumerSupport
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|IBatisPollingConsumer
specifier|public
class|class
name|IBatisPollingConsumer
extends|extends
name|PollingConsumerSupport
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|IBatisEndpoint
name|endpoint
decl_stmt|;
DECL|method|IBatisPollingConsumer (IBatisEndpoint endpoint)
specifier|public
name|IBatisPollingConsumer
parameter_list|(
name|IBatisEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
DECL|method|receive (long timeout)
specifier|public
name|Exchange
name|receive
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
return|return
name|receiveNoWait
argument_list|()
return|;
block|}
DECL|method|receive ()
specifier|public
name|Exchange
name|receive
parameter_list|()
block|{
return|return
name|receiveNoWait
argument_list|()
return|;
block|}
DECL|method|receiveNoWait ()
specifier|public
name|Exchange
name|receiveNoWait
parameter_list|()
block|{
try|try
block|{
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|endpoint
operator|.
name|query
argument_list|(
name|in
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Failed to poll: "
operator|+
name|endpoint
operator|+
literal|". Reason: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{     }
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{     }
block|}
end_class

end_unit

