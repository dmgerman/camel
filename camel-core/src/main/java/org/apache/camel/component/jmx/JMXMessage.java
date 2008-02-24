begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jmx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jmx
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|Notification
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
name|DefaultMessage
import|;
end_import

begin_comment
comment|/**  * A {@link Message} for a JMX Notification  *   * @version $Revision$  */
end_comment

begin_class
DECL|class|JMXMessage
specifier|public
class|class
name|JMXMessage
extends|extends
name|DefaultMessage
block|{
DECL|field|notification
specifier|private
name|Notification
name|notification
decl_stmt|;
DECL|method|JMXMessage ()
specifier|public
name|JMXMessage
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|JMXMessage (Notification notification)
specifier|public
name|JMXMessage
parameter_list|(
name|Notification
name|notification
parameter_list|)
block|{
name|this
operator|.
name|notification
operator|=
name|notification
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"JMXMessage: "
operator|+
name|notification
return|;
block|}
annotation|@
name|Override
DECL|method|getExchange ()
specifier|public
name|JMXExchange
name|getExchange
parameter_list|()
block|{
return|return
operator|(
name|JMXExchange
operator|)
name|super
operator|.
name|getExchange
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|newInstance ()
specifier|public
name|JMXMessage
name|newInstance
parameter_list|()
block|{
return|return
operator|new
name|JMXMessage
argument_list|()
return|;
block|}
DECL|method|getNotification ()
specifier|public
name|Notification
name|getNotification
parameter_list|()
block|{
return|return
name|notification
return|;
block|}
block|}
end_class

end_unit

