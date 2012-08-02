begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Session
import|;
end_import

begin_comment
comment|/**  * Session acknowledge enum keys  *   */
end_comment

begin_enum
DECL|enum|SessionAcknowledgementType
specifier|public
enum|enum
name|SessionAcknowledgementType
block|{
DECL|enumConstant|AUTO_ACKNOWLEDGE
name|AUTO_ACKNOWLEDGE
parameter_list|(
name|Session
operator|.
name|AUTO_ACKNOWLEDGE
parameter_list|)
operator|,
DECL|enumConstant|CLIENT_ACKNOWLEDGE
constructor|CLIENT_ACKNOWLEDGE(Session.CLIENT_ACKNOWLEDGE
block|)
enum|,
DECL|enumConstant|DUPS_OK_ACKNOWLEDGE
name|DUPS_OK_ACKNOWLEDGE
argument_list|(
name|Session
operator|.
name|DUPS_OK_ACKNOWLEDGE
argument_list|)
operator|,
DECL|enumConstant|SESSION_TRANSACTED
name|SESSION_TRANSACTED
argument_list|(
name|Session
operator|.
name|SESSION_TRANSACTED
argument_list|)
enum|;
end_enum

begin_decl_stmt
DECL|field|intValue
specifier|private
name|int
name|intValue
init|=
operator|-
literal|1
decl_stmt|;
end_decl_stmt

begin_constructor
DECL|method|SessionAcknowledgementType (int intValue)
specifier|private
name|SessionAcknowledgementType
parameter_list|(
name|int
name|intValue
parameter_list|)
block|{
name|this
operator|.
name|intValue
operator|=
name|intValue
expr_stmt|;
block|}
end_constructor

begin_function
DECL|method|intValue ()
specifier|public
name|int
name|intValue
parameter_list|()
block|{
return|return
name|this
operator|.
name|intValue
return|;
block|}
end_function

unit|}
end_unit

