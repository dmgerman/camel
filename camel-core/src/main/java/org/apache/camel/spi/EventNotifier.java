begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EventObject
import|;
end_import

begin_comment
comment|/**  * Notifier to send {@link java.util.EventObject events}.  *  * @see org.apache.camel.spi.EventFactory  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|EventNotifier
specifier|public
interface|interface
name|EventNotifier
block|{
comment|/**      * Notifies the given event      *      * @param event the event      * @throws Exception can be thrown if notification failed      */
DECL|method|notify (EventObject event)
name|void
name|notify
parameter_list|(
name|EventObject
name|event
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Checks whether notification for the given event is enabled.      *<p/>      * If disabled the event will not be sent and silently ignored instead.      *      * @param event the event      * @return<tt>true</tt> if the event should be sent,<tt>false</tt> to silently ignore it      */
DECL|method|isEnabled (EventObject event)
name|boolean
name|isEnabled
parameter_list|(
name|EventObject
name|event
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

