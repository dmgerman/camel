begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pulsar.utils
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pulsar
operator|.
name|utils
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Queue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentLinkedQueue
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pulsar
operator|.
name|client
operator|.
name|api
operator|.
name|Consumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pulsar
operator|.
name|client
operator|.
name|api
operator|.
name|PulsarClientException
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
DECL|class|PulsarUtils
specifier|public
specifier|final
class|class
name|PulsarUtils
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
name|PulsarUtils
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|PulsarUtils ()
specifier|private
name|PulsarUtils
parameter_list|()
block|{     }
DECL|method|stopConsumers (final Queue<Consumer<byte[]>> consumers)
specifier|public
specifier|static
name|Queue
argument_list|<
name|Consumer
argument_list|<
name|byte
index|[]
argument_list|>
argument_list|>
name|stopConsumers
parameter_list|(
specifier|final
name|Queue
argument_list|<
name|Consumer
argument_list|<
name|byte
index|[]
argument_list|>
argument_list|>
name|consumers
parameter_list|)
throws|throws
name|PulsarClientException
block|{
while|while
condition|(
operator|!
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Consumer
argument_list|<
name|byte
index|[]
argument_list|>
name|consumer
init|=
name|consumers
operator|.
name|poll
argument_list|()
decl_stmt|;
if|if
condition|(
name|consumer
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|consumer
operator|.
name|unsubscribe
argument_list|()
expr_stmt|;
name|consumer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore during stopping
if|if
condition|(
name|e
operator|instanceof
name|PulsarClientException
operator|.
name|AlreadyClosedException
condition|)
block|{
comment|// ignore
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Error stopping consumer: "
operator|+
name|consumer
operator|+
literal|" due to "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|". This exception is ignored"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
operator|new
name|ConcurrentLinkedQueue
argument_list|<>
argument_list|()
return|;
block|}
block|}
end_class

end_unit

