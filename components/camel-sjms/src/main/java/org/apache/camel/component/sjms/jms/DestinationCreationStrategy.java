begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
operator|.
name|jms
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Destination
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
name|Session
import|;
end_import

begin_comment
comment|/**  * Strategy for creating Destination's  */
end_comment

begin_interface
DECL|interface|DestinationCreationStrategy
specifier|public
interface|interface
name|DestinationCreationStrategy
block|{
DECL|method|createDestination (Session session, String name, boolean topic)
name|Destination
name|createDestination
parameter_list|(
name|Session
name|session
parameter_list|,
name|String
name|name
parameter_list|,
name|boolean
name|topic
parameter_list|)
throws|throws
name|JMSException
function_decl|;
DECL|method|createTemporaryDestination (Session session, boolean topic)
name|Destination
name|createTemporaryDestination
parameter_list|(
name|Session
name|session
parameter_list|,
name|boolean
name|topic
parameter_list|)
throws|throws
name|JMSException
function_decl|;
block|}
end_interface

end_unit

