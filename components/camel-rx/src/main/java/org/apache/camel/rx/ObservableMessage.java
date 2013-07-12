begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.rx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|rx
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
name|rx
operator|.
name|support
operator|.
name|ExchangeToMessageFunc1
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
name|rx
operator|.
name|support
operator|.
name|ObservableProcessor
import|;
end_import

begin_comment
comment|/**  * A base class for a {@link org.apache.camel.Processor} which allows you to process  * messages using an {@link rx.Observable<Message>} by implementing the  * abstract {@link org.apache.camel.rx.support.ObservableProcessor#configure(rx.Observable)} method.  */
end_comment

begin_class
DECL|class|ObservableMessage
specifier|public
specifier|abstract
class|class
name|ObservableMessage
extends|extends
name|ObservableProcessor
argument_list|<
name|Message
argument_list|>
block|{
DECL|method|ObservableMessage ()
specifier|public
name|ObservableMessage
parameter_list|()
block|{
name|super
argument_list|(
name|ExchangeToMessageFunc1
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

