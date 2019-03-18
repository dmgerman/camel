begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.quickfixj
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|quickfixj
package|;
end_package

begin_comment
comment|/**  * FIX message types provided by the QuickfixjEngine. This is used in the  * Camel exchange headers to specify the category of the message payload.  * This allow Camel to route and filter the messages based on the category.  */
end_comment

begin_enum
DECL|enum|QuickfixjEventCategory
specifier|public
enum|enum
name|QuickfixjEventCategory
block|{
DECL|enumConstant|AppMessageReceived
name|AppMessageReceived
block|,
DECL|enumConstant|AppMessageSent
name|AppMessageSent
block|,
DECL|enumConstant|AdminMessageReceived
name|AdminMessageReceived
block|,
DECL|enumConstant|AdminMessageSent
name|AdminMessageSent
block|,
DECL|enumConstant|SessionCreated
name|SessionCreated
block|,
DECL|enumConstant|SessionLogon
name|SessionLogon
block|,
DECL|enumConstant|SessionLogoff
name|SessionLogoff
block|}
end_enum

end_unit

