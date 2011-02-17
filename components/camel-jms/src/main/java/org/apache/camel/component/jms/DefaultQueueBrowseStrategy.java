begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
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
name|javax
operator|.
name|jms
operator|.
name|JMSException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|QueueBrowser
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Session
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
name|springframework
operator|.
name|jms
operator|.
name|core
operator|.
name|BrowserCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|core
operator|.
name|JmsOperations
import|;
end_import

begin_comment
comment|/**  * A default implementation of queue browsing using the Spring 2.5.x {@link BrowserCallback}  * @version   */
end_comment

begin_class
DECL|class|DefaultQueueBrowseStrategy
specifier|public
class|class
name|DefaultQueueBrowseStrategy
implements|implements
name|QueueBrowseStrategy
block|{
DECL|method|browse (JmsOperations template, String queue, final JmsQueueEndpoint endpoint)
specifier|public
name|List
argument_list|<
name|Exchange
argument_list|>
name|browse
parameter_list|(
name|JmsOperations
name|template
parameter_list|,
name|String
name|queue
parameter_list|,
specifier|final
name|JmsQueueEndpoint
name|endpoint
parameter_list|)
block|{
return|return
operator|(
name|List
argument_list|<
name|Exchange
argument_list|>
operator|)
name|template
operator|.
name|browse
argument_list|(
name|queue
argument_list|,
operator|new
name|BrowserCallback
argument_list|()
block|{
specifier|public
name|Object
name|doInJms
parameter_list|(
name|Session
name|session
parameter_list|,
name|QueueBrowser
name|browser
parameter_list|)
throws|throws
name|JMSException
block|{
comment|// TODO not the best implementation in the world as we have to browse
comment|// the entire queue, which could be massive
name|List
argument_list|<
name|Exchange
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Exchange
argument_list|>
argument_list|()
decl_stmt|;
name|Enumeration
name|iter
init|=
name|browser
operator|.
name|getEnumeration
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|Message
name|message
init|=
operator|(
name|Message
operator|)
name|iter
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
argument_list|)
return|;
block|}
block|}
end_class

end_unit

