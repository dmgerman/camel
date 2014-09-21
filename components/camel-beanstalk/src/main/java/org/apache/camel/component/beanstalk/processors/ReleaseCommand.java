begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.beanstalk.processors
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|beanstalk
operator|.
name|processors
package|;
end_package

begin_import
import|import
name|com
operator|.
name|surftools
operator|.
name|BeanstalkClient
operator|.
name|Client
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
name|NoSuchHeaderException
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
name|beanstalk
operator|.
name|BeanstalkEndpoint
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
name|beanstalk
operator|.
name|BeanstalkExchangeHelper
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
name|beanstalk
operator|.
name|Headers
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
DECL|class|ReleaseCommand
specifier|public
class|class
name|ReleaseCommand
extends|extends
name|DefaultCommand
block|{
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
name|ReleaseCommand
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|ReleaseCommand (BeanstalkEndpoint endpoint)
specifier|public
name|ReleaseCommand
parameter_list|(
name|BeanstalkEndpoint
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
DECL|method|act (final Client client, final Exchange exchange)
specifier|public
name|void
name|act
parameter_list|(
specifier|final
name|Client
name|client
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|NoSuchHeaderException
block|{
specifier|final
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
specifier|final
name|Long
name|jobId
init|=
name|BeanstalkExchangeHelper
operator|.
name|getJobID
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
specifier|final
name|long
name|priority
init|=
name|BeanstalkExchangeHelper
operator|.
name|getPriority
argument_list|(
name|endpoint
argument_list|,
name|in
argument_list|)
decl_stmt|;
specifier|final
name|int
name|delay
init|=
name|BeanstalkExchangeHelper
operator|.
name|getDelay
argument_list|(
name|endpoint
argument_list|,
name|in
argument_list|)
decl_stmt|;
specifier|final
name|boolean
name|result
init|=
name|client
operator|.
name|release
argument_list|(
name|jobId
argument_list|,
name|priority
argument_list|,
name|delay
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|result
operator|&&
name|LOG
operator|.
name|isWarnEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Failed to release job %d (priority %d, delay %d)"
argument_list|,
name|jobId
argument_list|,
name|priority
argument_list|,
name|delay
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Job %d released with priority %d, delay %d seconds. Result is %b"
argument_list|,
name|jobId
argument_list|,
name|priority
argument_list|,
name|delay
argument_list|,
name|result
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|answerWith
argument_list|(
name|exchange
argument_list|,
name|Headers
operator|.
name|RESULT
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

