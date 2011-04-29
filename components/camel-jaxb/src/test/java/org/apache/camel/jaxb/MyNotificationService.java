begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.jaxb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|jaxb
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
name|Exchange
import|;
end_import

begin_comment
comment|/**  * NotificationType does not have any JAXB ObjectFactory so we should test you can still route using that  */
end_comment

begin_class
DECL|class|MyNotificationService
specifier|public
class|class
name|MyNotificationService
block|{
DECL|method|createNotification (Exchange exchange)
specifier|public
name|void
name|createNotification
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|NotificationType
name|notification
init|=
operator|new
name|NotificationType
argument_list|()
decl_stmt|;
name|notification
operator|.
name|setEvent
argument_list|(
literal|"Hello"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|notification
argument_list|)
expr_stmt|;
block|}
DECL|method|sendNotification (Exchange exchange)
specifier|public
name|void
name|sendNotification
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|NotificationType
name|notification
init|=
operator|(
name|NotificationType
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|createProducerTemplate
argument_list|()
operator|.
name|sendBody
argument_list|(
literal|"mock:notify"
argument_list|,
name|notification
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

