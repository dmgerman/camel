begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.soroushbot.models
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|soroushbot
operator|.
name|models
package|;
end_package

begin_enum
DECL|enum|MinorType
specifier|public
enum|enum
name|MinorType
block|{
DECL|enumConstant|TEXT
name|TEXT
block|,
DECL|enumConstant|FILE
name|FILE
block|,
DECL|enumConstant|LOCATION
name|LOCATION
block|,
DECL|enumConstant|INFORMATION
name|INFORMATION
block|,
DECL|enumConstant|START
name|START
block|,
DECL|enumConstant|STOP
name|STOP
block|,
DECL|enumConstant|SELF_SEEN
name|SELF_SEEN
block|,
DECL|enumConstant|CHANGE
name|CHANGE
block|}
end_enum

end_unit

