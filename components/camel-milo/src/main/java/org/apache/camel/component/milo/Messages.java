begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.milo
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|milo
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
name|support
operator|.
name|DefaultMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|types
operator|.
name|builtin
operator|.
name|DataValue
import|;
end_import

begin_class
DECL|class|Messages
specifier|public
specifier|final
class|class
name|Messages
block|{
DECL|method|Messages ()
specifier|private
name|Messages
parameter_list|()
block|{     }
comment|/**      * Fill a Message from a DataValue      *      * @param value the value to feed from      * @param result the result to feed to      */
DECL|method|fillFromDataValue (final DataValue value, final DefaultMessage result)
specifier|public
specifier|static
name|void
name|fillFromDataValue
parameter_list|(
specifier|final
name|DataValue
name|value
parameter_list|,
specifier|final
name|DefaultMessage
name|result
parameter_list|)
block|{
name|result
operator|.
name|setBody
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|result
operator|.
name|setFault
argument_list|(
name|value
operator|.
name|getStatusCode
argument_list|()
operator|.
name|isBad
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

