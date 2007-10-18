begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms.requestor
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
operator|.
name|requestor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Callable
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
name|FutureTask
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

begin_comment
comment|/**  * A {@link FutureTask} which implements {@link ReplyHandler}  * so that it can be used as a handler for a correlation ID  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|FutureHandler
specifier|public
class|class
name|FutureHandler
extends|extends
name|FutureTask
implements|implements
name|ReplyHandler
block|{
DECL|field|EMPTY_CALLABLE
specifier|private
specifier|static
specifier|final
name|Callable
name|EMPTY_CALLABLE
init|=
operator|new
name|Callable
argument_list|()
block|{
specifier|public
name|Object
name|call
parameter_list|()
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
DECL|method|FutureHandler ()
specifier|public
name|FutureHandler
parameter_list|()
block|{
name|super
argument_list|(
name|EMPTY_CALLABLE
argument_list|)
expr_stmt|;
block|}
DECL|method|set (Object result)
specifier|public
specifier|synchronized
name|void
name|set
parameter_list|(
name|Object
name|result
parameter_list|)
block|{
name|super
operator|.
name|set
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|handle (Message message)
specifier|public
name|boolean
name|handle
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|JMSException
block|{
name|set
argument_list|(
name|message
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

