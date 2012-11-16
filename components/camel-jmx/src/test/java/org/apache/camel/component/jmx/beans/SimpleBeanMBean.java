begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jmx.beans
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
operator|.
name|beans
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_interface
DECL|interface|SimpleBeanMBean
specifier|public
interface|interface
name|SimpleBeanMBean
extends|extends
name|Serializable
block|{
DECL|method|getStringValue ()
name|String
name|getStringValue
parameter_list|()
function_decl|;
DECL|method|setStringValue (String aValue)
name|void
name|setStringValue
parameter_list|(
name|String
name|aValue
parameter_list|)
function_decl|;
DECL|method|touch ()
name|void
name|touch
parameter_list|()
function_decl|;
DECL|method|userData (String aUserData)
name|void
name|userData
parameter_list|(
name|String
name|aUserData
parameter_list|)
function_decl|;
DECL|method|triggerConnectionNotification ()
name|void
name|triggerConnectionNotification
parameter_list|()
function_decl|;
DECL|method|triggerMBeanServerNotification ()
name|void
name|triggerMBeanServerNotification
parameter_list|()
throws|throws
name|Exception
function_decl|;
DECL|method|triggerRelationNotification ()
name|void
name|triggerRelationNotification
parameter_list|()
throws|throws
name|Exception
function_decl|;
DECL|method|triggerTimerNotification ()
name|void
name|triggerTimerNotification
parameter_list|()
function_decl|;
DECL|method|getMonitorNumber ()
name|Integer
name|getMonitorNumber
parameter_list|()
function_decl|;
DECL|method|setMonitorNumber (Integer aMonitorNumber)
name|void
name|setMonitorNumber
parameter_list|(
name|Integer
name|aMonitorNumber
parameter_list|)
function_decl|;
DECL|method|getLongNumber ()
name|Long
name|getLongNumber
parameter_list|()
function_decl|;
DECL|method|setLongNumber (Long aMonitorNumber)
name|void
name|setLongNumber
parameter_list|(
name|Long
name|aMonitorNumber
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

